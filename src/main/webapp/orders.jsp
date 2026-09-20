<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Orders - AdharshMart</title>
    <!-- Dynamic Context Path ensures CSS always loads properly -->
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/glassmorphism-3d.css">
</head>
<body>
    <!-- Background glowing orbs required for 3D glass contrast -->
    <div class="glow-orb orb-1"></div>
    <div class="glow-orb orb-2"></div>

    <div class="container">
        <div style="display: flex; justify-content: space-between; align-items: center; margin-bottom: 2rem;">
            <h2>Order History</h2>
            <a href="${pageContext.request.contextPath}/"><button class="btn-glass">Back to Store</button></a>
        </div>
        
        <div id="ordersList">
            <p style="color: var(--text-muted);">Loading your orders...</p>
        </div>
    </div>

    <script>
        const contextPath = '${pageContext.request.contextPath}';

        fetch(contextPath + '/api/v1/orders')
            .then(async res => {
                if (res.status === 401) {
                    alert('Session expired. Please sign in to view your orders.');
                    window.location.href = contextPath + '/';
                    return null;
                }
                if (!res.ok) {
                    throw new Error('Server returned status: ' + res.status);
                }
                return res.json();
            })
            .then(res => {
                if (!res) return;
                const container = document.getElementById('ordersList');
                
                if (!res.success || !res.data || res.data.length === 0) {
                    container.innerHTML = '<div class="glass-card-3d"><p style="color: var(--text-muted);">No orders found. Start shopping to place your first order!</p></div>';
                    return;
                }

                container.innerHTML = '';
                res.data.forEach(o => {
                    const orderDate = o.createdAt ? new Date(o.createdAt).toLocaleDateString() : 'Recent';
                    container.innerHTML += `
                        <div class="glass-card-3d" style="margin-bottom: 1.2rem; transform: none;">
                            <div style="display: flex; justify-content: space-between; align-items: center;">
                                <div>
                                    <h3 style="color: var(--text-primary); margin-bottom: 0.3rem;">Order #\${o.id}</h3>
                                    <p style="color: var(--text-muted); font-size: 0.85rem;">Placed on: \${orderDate}</p>
                                </div>
                                <div style="text-align: right;">
                                    <span style="font-size: 1.15rem; font-weight: 700; color: var(--neon-blue);">₹\${parseFloat(o.totalAmount).toFixed(2)}</span>
                                    <div style="margin-top: 0.3rem;">
                                        <span style="display: inline-block; padding: 0.2rem 0.6rem; border-radius: 8px; font-size: 0.75rem; background: rgba(0, 210, 255, 0.15); border: 1px solid var(--neon-blue); color: var(--neon-blue);">
                                            \${o.status}
                                        </span>
                                    </div>
                                </div>
                            </div>
                        </div>
                    `;
                });
            })
            .catch(err => {
                console.error('Error fetching orders:', err);
                document.getElementById('ordersList').innerHTML = '<div class="glass-card-3d"><p style="color: #ff4d4f;">Failed to load orders. Please check your backend connection.</p></div>';
            });
    </script>
</body>
</html>