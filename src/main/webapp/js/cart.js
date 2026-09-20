async function renderCart() {
  const container = document.getElementById('cartContainer');
  const res = await fetch('/api/v1/cart');
  if (res.status === 401) {
    window.location.href = '/index.jsp';
    return;
  }
  const json = await res.json();
  container.innerHTML = '';

  let grandTotal = 0;
  json.data.forEach(item => {
    grandTotal += item.productPrice * item.quantity;
    const div = document.createElement('div');
    div.className = 'card-3d';
    div.style.marginBottom = '1rem';
    div.innerHTML = `
      <div style="display:flex; justify-content:space-between; align-items:center;">
        <div>
          <h3>${item.productName}</h3>
          <p>Quantity: ${item.quantity} | ₹${item.productPrice.toFixed(2)} each</p>
        </div>
        <button class="btn-glass" onclick="removeCartItem(${item.id})">Remove</button>
      </div>
    `;
    container.appendChild(div);
  });
  document.getElementById('cartTotal').innerText = `Total: ₹${grandTotal.toFixed(2)}`;
}

async function removeCartItem(id) {
  await fetch(`/api/v1/cart/${id}`, { method: 'DELETE' });
  renderCart();
}

async function executeMockCheckout() {
  const res = await fetch('/api/v1/orders', { method: 'POST' });
  const json = await res.json();
  if (json.success) {
    alert('Mock payment confirmed! Order placed successfully.');
    window.location.href = '/orders.jsp';
  } else {
    alert(json.error.message);
  }
}