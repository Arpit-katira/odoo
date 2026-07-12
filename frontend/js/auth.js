
document.addEventListener('DOMContentLoaded', () => {
  initTheme();

  const form = $('#loginForm');
  const errorBox = $('#loginError');

  // Auto redirect if already logged in with valid role
  const user = loadFromStorage('transitops-user');
  if (user && canView('dashboard', user.role)) {
    window.location.href = 'dashboard.html';
  } else if (user) {
    // Invalid/corrupted user data — clear it
    localStorage.removeItem('transitops-user');
  }

  if (!form) return;

  form.addEventListener('submit', async (e) => {
    e.preventDefault();
    if (errorBox) errorBox.classList.add('hidden');

    const email = form.email.value.trim();
    const password = form.password.value;
    const btn = form.querySelector('button[type="submit"]');
    const originalText = btn.textContent;

    try {
      btn.disabled = true;
      btn.textContent = 'Signing in...';
      const user = await API.login(email, password);
      saveToStorage('transitops-user', user);
      toast(`Welcome, ${user.name}!`, 'success');
      window.location.href = 'dashboard.html';
    } catch (err) {
      if (errorBox) {
        errorBox.textContent = err.message;
        errorBox.classList.remove('hidden');
      }
      toast(err.message, 'error');
    } finally {
      btn.disabled = false;
      btn.textContent = originalText;
    }
  });
});
