document.addEventListener('DOMContentLoaded', async () => {
  initTheme();
  initSidebar();
  initLogout();

  const user = guardPageAccess('drivers');
  if (!user) return;
  renderUserPill(user);
  applyRoleAccess(user.role);

  const tableBody = $('#driversTableBody');
  const searchInput = $('#searchDrivers');
  const filterStatus = $('#filterStatus');
  const filterExpiry = $('#filterExpiry');
  const modal = $('#driverModal');
  const form = $('#driverForm');
  const modalTitle = $('#modalTitle');

  let drivers = [];

  await loadDrivers();

  const btnAdd = $('#btnAddDriver');
  if (btnAdd) btnAdd.addEventListener('click', () => openModal());

  searchInput.addEventListener('input', debounce(render, 200));
  filterStatus.addEventListener('change', render);
  filterExpiry.addEventListener('change', render);

  $$('[data-close-modal]').forEach(btn => btn.addEventListener('click', closeModal));
  modal.addEventListener('click', (e) => { if (e.target === modal) closeModal(); });

  const canModify = canEdit('drivers', user.role);

  if (canModify) {
    form.addEventListener('submit', async (e) => {
    e.preventDefault();
    const data = parseForm(form);
    const isEdit = !!data.id;

    try {
      if (isEdit) {
        await API.updateDriver(data.id, data);
        toast('Driver updated successfully', 'success');
      } else {
        data.status = data.status || 'Available';
        await API.createDriver(data);
        toast('Driver added successfully', 'success');
      }
      closeModal();
      await loadDrivers();
    } catch (err) {
      toast(err.message, 'error');
    }
    });

    tableBody.addEventListener('click', async (e) => {
    const btn = e.target.closest('button');
    if (!btn) return;
    const id = btn.dataset.id;

    if (btn.classList.contains('btn-edit')) {
      const driver = drivers.find(d => d.id === id);
      openModal(driver);
    }

    if (btn.classList.contains('btn-delete')) {
      if (confirmAction('Are you sure you want to delete this driver?')) {
        try {
          await API.deleteDriver(id);
          toast('Driver deleted', 'success');
          await loadDrivers();
        } catch (err) {
          toast(err.message, 'error');
        }
      }
    }
  });

  }

  async function loadDrivers() {
    await API.seed();
    drivers = await API.getDrivers();
    render();
  }

  function getLicenseStatus(expiryDate) {
    const today = new Date();
    const exp = new Date(expiryDate);
    const diffDays = Math.ceil((exp - today) / (1000 * 60 * 60 * 24));
    if (diffDays < 0) return { label: 'Expired', class: 'badge-retired' };
    if (diffDays <= 30) return { label: `Expiring in ${diffDays}d`, class: 'badge-in-shop' };
    return { label: 'Valid', class: 'badge-available' };
  }

  function render() {
    const q = searchInput.value.trim();
    let data = filterData(drivers, q, ['name', 'licenseNo', 'contact']);
    if (filterStatus.value) data = data.filter(d => d.status === filterStatus.value);

    if (filterExpiry.value) {
      data = data.filter(d => {
        const ls = getLicenseStatus(d.licenseExpiry).label.toLowerCase();
        if (filterExpiry.value === 'expired') return ls.includes('expired');
        if (filterExpiry.value === 'expiring') return ls.includes('expiring');
        if (filterExpiry.value === 'valid') return ls.includes('valid');
        return true;
      });
    }

    if (!data.length) {
      tableBody.innerHTML = `<tr><td colspan="8" class="text-center text-muted">No drivers found.</td></tr>`;
      return;
    }

    tableBody.innerHTML = data.map(d => {
      const ls = getLicenseStatus(d.licenseExpiry);
      return `
        <tr>
          <td><strong>${escapeHtml(d.name)}</strong></td>
          <td>${escapeHtml(d.licenseNo)}</td>
          <td>${escapeHtml(d.licenseCategory)}</td>
          <td>
            <div>${formatDate(d.licenseExpiry)}</div>
            <span class="badge ${ls.class}" style="margin-top: 0.25rem;">${ls.label}</span>
          </td>
          <td>${escapeHtml(d.contact)}</td>
          <td>${d.safetyScore ?? '-'}/100</td>
          <td>${renderBadge(getStatusClass(d.status), { label: d.status })}</td>
          ${canModify ? `
          <td style="text-align: right;">
            <button class="btn btn-ghost btn-sm btn-edit" data-id="${d.id}">✏️</button>
            <button class="btn btn-danger btn-sm btn-delete" data-id="${d.id}">🗑️</button>
          </td>` : ''}
        </tr>
      `;
    }).join('');
  }

  function openModal(driver = null) {
    form.reset();
    if (driver) {
      modalTitle.textContent = 'Edit Driver';
      populateForm(form, driver);
    } else {
      modalTitle.textContent = 'Add Driver';
      $('#driverId').value = '';
    }
    modal.classList.add('open');
  }

  function closeModal() {
    modal.classList.remove('open');
  }
});
