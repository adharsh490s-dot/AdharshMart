package com.adharsh.adharshmart.dao;

import com.adharsh.adharshmart.listener.DBConnectionListener;
import com.adharsh.adharshmart.model.Product;
import com.adharsh.adharshmart.model.User;
import org.junit.jupiter.api.*;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class ProductDAOTest {

    private static DBConnectionListener listener;
    private ProductDAO productDAO;
    private static Long testSellerId;

    @BeforeAll
    public static void setupDatabase() {
        listener = new DBConnectionListener();
        ServletContext mockContext = mock(ServletContext.class);
        listener.contextInitialized(new ServletContextEvent(mockContext));

        // Seed a valid seller record to satisfy FK constraint on products table
        UserDAO userDAO = new UserDAO();
        User seller = new User();
        seller.setName("DAO Test Seller");
        seller.setEmail("dao_seller@adharshmart.com");
        seller.setPasswordHash("$2a$12$e88yvVb1aH03zWffYyE43eH2fJ9z2pQ8XGceI4F8JkH6x9UeZz6Ue");
        seller.setRole("SELLER");
        User savedSeller = userDAO.create(seller);
        testSellerId = savedSeller.getId();
    }

    @AfterAll
    public static void tearDownDatabase() {
        listener.contextDestroyed(null);
    }

    @BeforeEach
    public void init() {
        productDAO = new ProductDAO();
    }

    @Test
    @Order(1)
    public void testSaveProduct() {
        Product product = new Product();
        product.setSellerId(testSellerId);
        product.setName("Quantum Neon Visor");
        product.setDescription("3D augmented heads-up display visor.");
        product.setPrice(new BigDecimal("189.99"));
        product.setStockQty(10);
        product.setCategory("Wearables");
        product.setImageUrl("https://example.com/visor.png");

        Product created = productDAO.save(product);

        assertNotNull(created.getId(), "Generated ID must not be null after insertion");
        assertTrue(created.getId() > 0);
    }

    @Test
    @Order(2)
    public void testFindAllWithCategoryAndSearchFilter() {
        // Save test items across distinct categories
        Product p1 = new Product(null, testSellerId, "Hologram Speaker", "Deep bass audio", new BigDecimal("79.99"), 5, "Audio", "", null);
        Product p2 = new Product(null, testSellerId, "Cybernetic Mouse", "Glass ergonomic body", new BigDecimal("49.50"), 12, "Peripherals", "", null);
        productDAO.save(p1);
        productDAO.save(p2);

        // Filter by category
        List<Product> audioItems = productDAO.findAll("Audio", null);
        assertFalse(audioItems.isEmpty());
        assertTrue(audioItems.stream().allMatch(p -> "Audio".equalsIgnoreCase(p.getCategory())));

        // Filter by keyword search (case-insensitive)
        List<Product> searchResults = productDAO.findAll(null, "mouse");
        assertEquals(1, searchResults.size());
        assertEquals("Cybernetic Mouse", searchResults.get(0).getName());
    }

    @Test
    @Order(3)
    public void testUpdateProduct() {
        Product product = new Product(null, testSellerId, "Original Name", "Original Desc", new BigDecimal("99.00"), 8, "General", "", null);
        Product saved = productDAO.save(product);

        saved.setName("Updated Futuristic Device");
        saved.setPrice(new BigDecimal("129.99"));
        saved.setStockQty(20);

        boolean updated = productDAO.update(saved);
        assertTrue(updated, "Update query must affect at least 1 row");

        Optional<Product> reloaded = productDAO.findById(saved.getId());
        assertTrue(reloaded.isPresent());
        assertEquals("Updated Futuristic Device", reloaded.get().getName());
        assertEquals(new BigDecimal("129.99"), reloaded.get().getPrice());
        assertEquals(20, reloaded.get().getStockQty());
    }

    @Test
    @Order(4)
    public void testDeleteProduct() {
        Product product = new Product(null, testSellerId, "Disposable Product", "Will be deleted", new BigDecimal("19.99"), 2, "Test", "", null);
        Product saved = productDAO.save(product);
        Long id = saved.getId();

        // 1. Unauthorized seller cannot delete
        boolean unauthorizedDelete = productDAO.delete(id, 99999L, false);
        assertFalse(unauthorizedDelete, "Non-owning seller must not be able to delete listing");

        // 2. Owning seller can delete
        boolean deletedByOwner = productDAO.delete(id, testSellerId, false);
        assertTrue(deletedByOwner);

        // Verify removal from DB
        Optional<Product> lookup = productDAO.findById(id);
        assertTrue(lookup.isEmpty());
    }
}