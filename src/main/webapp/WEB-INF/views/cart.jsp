<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <title>Cart - AdharshMart</title>
    <link rel="stylesheet" href="../../css/glassmorphism-3d.css">
</head>
<body>
    <div class="container">
        <h2>Your Cart</h2>
        <div id="cartContainer" style="margin-top: 1.5rem;"></div>
        <h3 id="cartTotal" style="margin-top: 1rem; color: var(--neon-blue);"></h3>
        <button class="btn-glass" onclick="executeMockCheckout()" style="margin-top: 1rem;">Confirm Mock Payment</button>
    </div>
    <script src="../../js/cart.js"></script>
    <script>document.addEventListener('DOMContentLoaded', renderCart);</script>
</body>
</html>