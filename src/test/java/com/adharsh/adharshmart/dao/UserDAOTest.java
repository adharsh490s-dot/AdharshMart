package com.adharsh.adharshmart.dao;

import com.adharsh.adharshmart.listener.DBConnectionListener;
import com.adharsh.adharshmart.model.User;
import org.junit.jupiter.api.*;

import javax.servlet.ServletContextEvent;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

public class UserDAOTest {
    private static DBConnectionListener listener;
    private UserDAO userDAO;

    @BeforeAll
    public static void setUp() {
        listener = new DBConnectionListener();
        ServletContextEvent sce = new ServletContextEvent(mock(javax.servlet.ServletContext.class));
        listener.contextInitialized(sce);
    }

    @AfterAll
    public static void tearDown() {
        listener.contextDestroyed(null);
    }

    @BeforeEach
    public void initDAO() {
        userDAO = new UserDAO();
    }

    @Test
    public void testCreateAndFindUser() {
        User user = new User();
        user.setName("Test User");
        user.setEmail("junit@test.com");
        user.setPasswordHash("hashed_val");
        user.setRole("BUYER");

        userDAO.create(user);
        Optional<User> retrieved = userDAO.findByEmail("junit@test.com");

        assertTrue(retrieved.isPresent());
        assertEquals("Test User", retrieved.get().getName());
    }
}