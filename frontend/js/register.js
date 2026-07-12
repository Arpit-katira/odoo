/**
 * TransitOps — Registration Page Logic
 */

document.addEventListener('DOMContentLoaded', async () => {
  initTheme();

  const form = $('#registerForm');
  const errorBox = $('#registerError');
  const successBox = $('#registerSuccess');
  const roleSelect = $('#roleId');

  // Redirect if already logged in
  const user = loadFromStorage('transitops-user');
  if (user) {
    window.location.href = 'dashboard.html';
    return;
  }

  try {
    await API.seed();
    const roles = await API.getRoles();
    roleSelect.innerHTML = `<option value="">Select a role</option>` +
      roles.map(r => `<option value="${r.id}">${escapeHtml(r.name)}</option>`).join('');
  } catch (err) {
    toast(err.message, 'error');
  }

  form.addEventListener('submit', async (e) => {
    e.preventDefault();
    errorBox.classList.add('hidden');
    successBox.classList.add('hidden');

    const data = parseForm(form);

    if (data.password !== data.confirmPassword) {
      errorBox.textContent = 'Passwords do not match.';
      errorBox.classList.remove('hidden');
      return;
    }

    const btn = form.querySelector('button[type="submit"]');
    const originalText = btn.textContent;

    try {
      btn.disabled = true;
      btn.textContent = 'Creating account...';
      await API.register(data);
      successBox.textContent = 'Account created successfully! Redirecting to login...';
      successBox.classList.remove('hidden');
      form.reset();
      setTimeout(() => {
        window.location.href = 'index.html';
      }, 1500);
    } catch (err) {
      errorBox.textContent = err.message;
      errorBox.classList.remove('hidden');
    } finally {
      btn.disabled = false;
      btn.textContent = originalText;
    }
  });
});
