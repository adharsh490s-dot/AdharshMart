function apply3DTilt() {
  const cards = document.querySelectorAll('.card-3d');
  cards.forEach(card => {
    card.addEventListener('mousemove', (e) => {
      const rect = card.getBoundingClientRect();
      const x = e.clientX - rect.left - rect.width / 2;
      const y = e.clientY - rect.top - rect.height / 2;
      card.style.transform = `perspective(1000px) rotateX(${-y / 8}deg) rotateY(${x / 8}deg) scale3d(1.02, 1.02, 1.02)`;
    });
    card.addEventListener('mouseleave', () => {
      card.style.transform = 'perspective(1000px) rotateX(0deg) rotateY(0deg) scale3d(1, 1, 1)';
    });
  });
}

async function loadProducts(keyword = '') {
  const url = keyword ? `/api/v1/products?search=${encodeURIComponent(keyword)}` : '/api/v1/products';
  const res = await fetch(url);
  const data = await res.json();
  const grid = document.getElementById('productGrid');
  grid.innerHTML = '';

  if (!data.success || data.data.length === 0) {
    grid.innerHTML = '<p style="color:var(--text-muted)">No items currently available.</p>';
    return;
  }

  data.data.forEach(p => {
    const card = document.createElement('div');
    card.className = 'card-3d';
    card.innerHTML = `
      <div>
        <img src="${p.imageUrl || 'https://images.unsplash.com/photo-1546435770-a3e426bf472b?w=500'}" alt="${p.name}">
        <h3>${p.name}</h3>
        <p>${p.description}</p>
      </div>
      <div class="card-footer">
        <span class="price">₹${parseFloat(p.price).toFixed(2)}</span>
        <button class="btn-glass" onclick="addToCart(${p.id})">Add</button>
      </div>
    `;
    grid.appendChild(card);
  });
  apply3DTilt();
}

async function addToCart(productId) {
  const res = await fetch('/api/v1/cart', {
    method: 'POST',
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify({ productId, quantity: 1 })
  });
  if (res.status === 401) {
    openModal('authModal');
    return;
  }
  alert('Item added to cart!');
}

function openModal(id) { document.getElementById(id).style.display = 'flex'; }
function closeModal(id) { document.getElementById(id).style.display = 'none'; }

async function submitLogin() {
  const email = document.getElementById('loginEmail').value;
  const password = document.getElementById('loginPass').value;
  const res = await fetch('/api/v1/auth/login', {
    method: 'POST',
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify({ email, password })
  });
  const json = await res.json();
  if (json.success) {
    location.reload();
  } else {
    alert(json.error.message);
  }
}

document.addEventListener('DOMContentLoaded', () => {
  loadProducts();
  document.getElementById('searchInput').addEventListener('input', (e) => loadProducts(e.target.value));
});