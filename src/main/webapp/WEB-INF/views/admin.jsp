<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <title>Admin Portal - AdharshMart</title>
    <link rel="stylesheet" href="../../css/glassmorphism-3d.css">
</head>
<body>
    <div class="container">
        <h2>Admin Management Overview</h2>
        <div id="adminData" style="margin-top: 1rem;"></div>
    </div>
    <script>
        fetch('/api/v1/admin').then(r => r.json()).then(d => {
            if (d.success) {
                document.getElementById('adminData').innerHTML = `
                    <div class="card-3d">
                        <p>Total Platform Users: ${d.data.users.length}</p>
                        <p>Total Orders Executed: ${d.data.orders.length}</p>
                    </div>
                `;
            }
        });
    </script>
</body>
</html>