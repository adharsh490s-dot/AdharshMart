function toggleChat() {
  const box = document.getElementById('chatBox');
  box.style.display = box.style.display === 'flex' ? 'none' : 'flex';
}

async function sendChatMessage() {
  const input = document.getElementById('chatInput');
  const msg = input.value.trim();
  if (!msg) return;

  const msgContainer = document.getElementById('chatMessages');
  msgContainer.innerHTML += `<div class="msg msg-user">${msg}</div>`;
  input.value = '';
  msgContainer.scrollTop = msgContainer.scrollHeight;

  try {
    const res = await fetch('/api/v1/chat', {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify({ message: msg })
    });
    const json = await res.json();
    msgContainer.innerHTML += `<div class="msg msg-bot">${json.data.reply}</div>`;
  } catch (err) {
    msgContainer.innerHTML += `<div class="msg msg-bot">Service degraded. Try again later.</div>`;
  }
  msgContainer.scrollTop = msgContainer.scrollHeight;
}