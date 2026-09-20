<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <title>Orders - AdharshMart</title>
    <link rel="stylesheet" href="../../css/glassmorphism-3d.css">
</head>
<body>
    <div class="container">
        <h2>Order History</h2>
        <div id="ordersList" style="margin-top: 1.5rem;"></div>
    </div>
    <script>
        fetch('/api/v1/orders').then(res => res.json()).then(res => {
            const container = document.getElementById('ordersList');
            res.data.forEach(o => {
                container.innerHTML += `
                    <div class="card-3d" style="margin-bottom: 1rem;">
                        <h3>Order #${o.id}</h3>
                        <p>Status: ${o.status} | Total: â‚¹${parseFloat(o.totalAmount).toFixed(2)}</p>
                    </div>
                `;
            });
        });
    </script>
</body>
</html>