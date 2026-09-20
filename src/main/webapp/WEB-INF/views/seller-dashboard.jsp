<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <title>Seller Dashboard - AdharshMart</title>
    <link rel="stylesheet" href="../../css/glassmorphism-3d.css">
</head>
<body>
    <div class="container">
        <h2>Create Product Listing</h2>
        <div class="card-3d" style="max-width: 500px; margin-top: 1rem;">
            <input type="text" id="pName" placeholder="Product Name" style="margin-bottom: 0.5rem; width:100%; padding: 0.5rem;">
            <textarea id="pDesc" placeholder="Description" style="margin-bottom: 0.5rem; width:100%; padding: 0.5rem;"></textarea>
            <input type="number" id="pPrice" placeholder="Price" style="margin-bottom: 0.5rem; width:100%; padding: 0.5rem;">
            <input type="number" id="pStock" placeholder="Stock Qty" style="margin-bottom: 0.5rem; width:100%; padding: 0.5rem;">
            <input type="text" id="pCat" placeholder="Category" style="margin-bottom: 0.5rem; width:100%; padding: 0.5rem;">
            <input type="text" id="pImg" placeholder="Image URL" style="margin-bottom: 0.5rem; width:100%; padding: 0.5rem;">
            <button class="btn-glass" onclick="createProduct()">Publish Listing</button>
        </div>
    </div>
    <script>
        async function createProduct() {
            const payload = {
                name: document.getElementById('pName').value,
                description: document.getElementById('pDesc').value,
                price: parseFloat(document.getElementById('pPrice').value),
                stockQty: parseInt(document.getElementById('pStock').value),
                category: document.getElementById('pCat').value,
                imageUrl: document.getElementById('pImg').value
            };
            const res = await fetch('/api/v1/products', {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify(payload)
            });
            if (res.ok) {
                alert('Product created successfully');
                window.location.href = '/index.jsp';
            }
        }
    </script>
</body>
</html>