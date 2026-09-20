<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>AdharshMart | 3D Glass Marketplace</title>
    <link rel="stylesheet" href="css/glassmorphism-3d.css">
    <link rel="stylesheet" href="css/chatbot-widget.css">
</head>
<body>
    <div class="orb orb-1"></div>
    <div class="orb orb-2"></div>

    <header class="navbar">
        <div class="logo">AdharshMart</div>
        <div class="search-box">
            <input type="text" id="searchInput" placeholder="Search catalog...">
        </div>
        <div>
            <% if (session.getAttribute("userName") == null) { %>
                <button class="btn-glass" onclick="openModal('authModal')">Sign In</button>
            <% } else { %>
                <span><%= session.getAttribute("userName") %></span>
                <% if ("ADMIN".equalsIgnoreCase((String) session.getAttribute("userRole"))) { %>
                    <a href="admin.jsp"><button class="btn-glass">Admin</button></a>
                <% } %>
                <button class="btn-glass" onclick="fetch('/api/v1/auth/logout',{method:'POST'}).then(()=>location.reload())">Logout</button>
            <% } %>
            <a href="WEB-INF/views/cart.jsp"><button class="btn-glass">Cart</button></a>
        </div>
    </header>

    <main class="container">
        <div class="grid" id="productGrid"></div>
    </main>

    <div class="chat-fab" onclick="toggleChat()">ðŸ’¬</div>
    <div class="chat-box" id="chatBox">
        <div class="chat-header">Store Assistant</div>
        <div class="chat-messages" id="chatMessages">
            <div class="msg msg-bot">Hello! Ask me anything about listings, shipping, or orders.</div>
        </div>
        <div class="chat-input">
            <input type="text" id="chatInput" placeholder="Type a message..." onkeydown="if(event.key==='Enter') sendChatMessage()">
            <button class="btn-glass" onclick="sendChatMessage()">Send</button>
        </div>
    </div>

    <div class="modal-backdrop" id="authModal">
        <div class="modal-window">
            <h2 id="authTitle" style="margin-bottom:1rem">Sign In</h2>
            <div id="registerFields" style="display:none; gap:0.75rem; flex-direction:column; margin-bottom:1rem;">
                <input type="text" id="registerName" placeholder="Full name">
                <select id="registerRole" style="padding: 10px; border: 1px solid rgba(25,35,45,.18); border-radius: 4px;">
                    <option value="BUYER">Buyer</option>
                    <option value="SELLER">Seller</option>
                </select>
            </div>
            <input type="email" id="loginEmail" placeholder="Email">
            <input type="password" id="loginPass" placeholder="Password">
            <div style="display:flex; justify-content:flex-end; gap:0.5rem; margin-top:1rem; flex-wrap:wrap;">
                <button class="btn-glass" onclick="closeModal('authModal'); resetAuthForm();">Cancel</button>
                <button class="btn-glass" id="toggleAuthModeBtn" onclick="toggleAuthMode()">Create account</button>
                <button class="btn-glass" id="submitAuthBtn" onclick="submitLogin()">Sign In</button>
            </div>
        </div>
    </div>

    <script src="js/app.js"></script>
    <script src="js/chatbot.js"></script>
</body>
</html>