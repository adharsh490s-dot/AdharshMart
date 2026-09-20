<%@ page contentType="text/html;charset=UTF-8" language="java" isELIgnored="true" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Admin Inventory | AdharshMart</title>
    <link rel="stylesheet" href="css/glassmorphism-3d.css">
    <style>
        body { padding: 32px; }
        .admin-shell { max-width: 1100px; margin: auto; padding: 28px; }
        .form-grid { display: grid; grid-template-columns: repeat(auto-fit, minmax(180px, 1fr)); gap: 10px; margin: 20px 0 32px; }
        input, textarea, button { width: 100%; padding: 10px; border: 1px solid rgba(25,35,45,.18); border-radius: 4px; font: inherit; }
        textarea { min-height: 70px; grid-column: 1 / -1; }
        button { background: var(--accent); color: white; cursor: pointer; }
        table { width: 100%; border-collapse: collapse; }
        th, td { padding: 12px 8px; text-align: left; border-bottom: 1px solid rgba(25,35,45,.14); }
        .stock-input { max-width: 90px; }
        .danger { background: #9b2226; }
        #status { min-height: 24px; }
        @media (max-width: 650px) { body { padding: 12px; } th:nth-child(3), td:nth-child(3) { display: none; } }
    </style>
</head>
<body>
<main class="glass admin-shell">
    <h1>Inventory management</h1>
    <p id="status"></p>

    <h2>Add product</h2>
    <form id="productForm" class="form-grid">
        <input name="name" placeholder="Product name" required>
        <input name="price" type="number" min="0.01" step="0.01" placeholder="Price" required>
        <input name="stockQty" type="number" min="0" placeholder="Initial stock" required>
        <input name="category" placeholder="Category" required>
        <input name="imageUrl" type="url" placeholder="Image URL">
        <textarea name="description" placeholder="Description"></textarea>
        <button type="submit">Add product</button>
    </form>

    <h2>Products and stock</h2>
    <table>
        <thead><tr><th>Product</th><th>Category</th><th>Price</th><th>Stock</th><th>Actions</th></tr></thead>
        <tbody id="inventory"></tbody>
    </table>
</main>
<script>
const statusBox = document.getElementById('status');
const setStatus = (message, error = false) => { statusBox.textContent = message; statusBox.style.color = error ? '#9b2226' : 'inherit'; };

async function loadInventory() {
    const response = await fetch('api/v1/admin');
    const body = await response.json();
    if (!response.ok || !body.success) throw new Error(body.error || 'Admin access required.');
    document.getElementById('inventory').innerHTML = body.data.products.map(product => `
        <tr>
            <td>${escapeHtml(product.name)}</td>
            <td>${escapeHtml(product.category)}</td>
            <td>${Number(product.price).toFixed(2)}</td>
            <td><input class="stock-input" type="number" min="0" value="${product.stockQty}" id="stock-${product.id}"></td>
            <td>
                <button onclick="updateStock(${product.id})">Save stock</button>
                <button class="danger" onclick="deleteProduct(${product.id})">Delete</button>
            </td>
        </tr>`).join('');
}

async function updateStock(productId) {
    const stockQty = Number(document.getElementById(`stock-${productId}`).value);
    const response = await fetch(`api/v1/admin/inventory/${productId}`, { method: 'PUT', headers: {'Content-Type': 'application/json'}, body: JSON.stringify({stockQty}) });
    const body = await response.json();
    setStatus(response.ok ? 'Inventory updated.' : body.error || 'Update failed.', !response.ok);
    if (response.ok) loadInventory();
}

async function deleteProduct(productId) {
    if (!confirm('Delete this product?')) return;
    const response = await fetch(`api/v1/products/${productId}`, { method: 'DELETE' });
    const body = await response.json();
    setStatus(response.ok ? 'Product deleted.' : body.error || 'Delete failed.', !response.ok);
    if (response.ok) loadInventory();
}

document.getElementById('productForm').addEventListener('submit', async event => {
    event.preventDefault();
    const data = Object.fromEntries(new FormData(event.target));
    data.price = Number(data.price);
    data.stockQty = Number(data.stockQty);
    const response = await fetch('api/v1/products', { method: 'POST', headers: {'Content-Type': 'application/json'}, body: JSON.stringify(data) });
    const body = await response.json();
    setStatus(response.ok ? 'Product added.' : body.error || 'Creation failed.', !response.ok);
    if (response.ok) { event.target.reset(); loadInventory(); }
});

function escapeHtml(value) { return String(value ?? '').replace(/[&<>"']/g, character => ({'&':'&amp;','<':'&lt;','>':'&gt;','"':'&quot;',"'":'&#039;'}[character])); }
loadInventory().catch(error => setStatus(error.message, true));
</script>
</body>
</html>
