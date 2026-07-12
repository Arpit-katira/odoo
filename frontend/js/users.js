
document.addEventListener('DOMContentLoaded', async () => {
  initTheme();
  initSidebar();
  initLogout();

  const user = guardPageAccess('users');
  if (!user) return;
  renderUserPill(user);
  applyRoleAccess(user.role);

  const tableBody = $('#usersTableBody');
  const searchInput = $('#searchUsers');
  const filterRole = $('#filterRole');
  const filterActive = $('#filterActive');
  const modal = $('#userModal');
  const form = $('#userForm');
  const modalTitle = $('#modalTitle');

  let users = [];
  let roles = [];

  await loadData();

  $('#btnAddUser').addEventListener('click', () => openModal());

  searchInput.addEventListener('input', debounce(render, 200));
  filterRole.addEventListener('change', render);
  filterActive.addEventListener('change', render);

  $$('[data-close-modal]').forEach(btn => btn.addEventListener('click', closeModal));
  modal.addEventListener('click', (e) => { if (e.target === modal) closeModal(); });

  form.addEventListener('submit', async (e) => {
    e.preventDefault();
    const data = parseForm(form);
    data.isActive = data.isActive === 'true';
    const isEdit = !!data.id;

    try {
      if (isEdit) {
        const current = users.find(u => u.id === data.id);
        if (data.password === current?.password) {
          delete data.password;
        }
        await API.updateUser(data.id, data);
        toast('User updated successfully', 'success');
      } else {
        await API.register(data);
        toast('User created successfully', 'success');
      }
      closeModal();
      await loadData();
    } catch (err) {
      toast(err.message, 'error');
    }
  });

  tableBody.addEventListener('click', async (e) => {
    const btn = e.target.closest('button');
    if (!btn) return;
    const id = btn.dataset.id;

    if (btn.classList.contains('btn-edit')) {
      const u = users.find(x => x.id === id);
      openModal(u);
    }

    if (btn.classList.contains('btn-toggle')) {
      const u = users.find(x => x.id === id);
      try {
        await API.updateUser(id, { isActive: !u.isActive });
        toast(`User ${u.isActive ? 'deactivated' : 'activated'}`, 'success');
        await loadData();
      } catch (err) {
        toast(err.message, 'error');
      }
    }

    if (btn.classList.contains('btn-delete')) {
      if (confirmAction('Are you sure you want to delete this user?')) {
        try {
          await API.deleteUser(id);
          toast('User deleted', 'success');
          await loadData();
        } catch (err) {
          toast(err.message, 'error');
        }
      }
    }
  });

  async function loadData() {
    await API.seed();
    [users, roles] = await Promise.all([API.getUsers(), API.getRoles()]);

    filterRole.innerHTML = `<option value="">All Roles</option>` +
      roles.map(r => `<option value="${r.id}">${escapeHtml(r.name)}</option>`).join('');

    render();
  }

  function getRoleName(roleId) {
    return roles.find(r => r.id === roleId)?.name || '-';
  }

  function render() {
    const q = searchInput.value.trim();
    let data = users.filter(u => {
      const text = `${u.name || ''} ${u.email || ''} ${getRoleName(u.roleId)}`.toLowerCase();
      return !q || text.includes(q.toLowerCase());
    });
    if (filterRole.value) data = data.filter(u => u.roleId === filterRole.value);
    if (filterActive.value) data = data.filter(u => String(u.isActive) === filterActive.value);

    if (!data.length) {
      tableBody.innerHTML = `<tr><td colspan="6" class="text-center text-muted">No users found.</td></tr>`;
      return;
    }

    tableBody.innerHTML = data.map(u => `
      <tr>
        <td><strong>${escapeHtml(u.name)}</strong></td>
        <td>${escapeHtml(u.email)}</td>
        <td>${escapeHtml(u.phoneNumber || '-')}</td>
        <td>${escapeHtml(getRoleName(u.roleId))}</td>
        <td>${renderBadge(u.isActive ? 'available' : 'retired', { label: u.isActive ? 'Active' : 'Inactive' })}</td>
        <td style="text-align: right;">
          <button class="btn btn-ghost btn-sm btn-edit" data-id="${u.id}">✏️</button>
          <button class="btn btn-warning btn-sm btn-toggle" data-id="${u.id}">${u.isActive ? 'Deactivate' : 'Activate'}</button>
          <button class="btn btn-danger btn-sm btn-delete" data-id="${u.id}">🗑️</button>
        </td>
      </tr>
    `).join('');
  }

  function openModal(userToEdit = null) {
    form.reset();
    const roleSelect = $('#userRoleId');
    roleSelect.innerHTML = `<option value="">Select role</option>` +
      roles.map(r => `<option value="${r.id}">${escapeHtml(r.name)}</option>`).join('');

    if (userToEdit) {
      modalTitle.textContent = 'Edit User';
      populateForm(form, { ...userToEdit, password: userToEdit.password || '', isActive: String(userToEdit.isActive) });
      $('#userPassword').required = false;
    } else {
      modalTitle.textContent = 'Add User';
      $('#userId').value = '';
      $('#userPassword').required = true;
    }
    modal.classList.add('open');
  }

  function closeModal() {
    modal.classList.remove('open');
  }
});
