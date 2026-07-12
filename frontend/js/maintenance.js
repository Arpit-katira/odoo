

document.addEventListener('DOMContentLoaded', async () => {
  initTheme();
  initSidebar();
  initLogout();

  const user = guardPageAccess('maintenance');
  if (!user) return;
  renderUserPill(user);
  applyRoleAccess(user.role);

  const tableBody = $('#maintenanceTableBody');
  const searchInput = $('#searchMaintenance');
  const filterStatus = $('#filterStatus');
  const modal = $('#maintenanceModal');
  const form = $('#maintenanceForm');

  let vehicles = [];
  let maintenance = [];

  await loadData();

  const btnAdd = $('#btnAddMaintenance');
  if (btnAdd) btnAdd.addEventListener('click', () => openModal());

  searchInput.addEventListener('input', debounce(render, 200));
  filterStatus.addEventListener('change', render);

  $$('[data-close-modal]').forEach(btn => btn.addEventListener('click', closeModal));
  modal.addEventListener('click', (e) => { if (e.target === modal) closeModal(); });

  const canModify = canEdit('maintenance', user.role);

  if (canModify) {
    form.addEventListener('submit', async (e) => {
    e.preventDefault();
    const data = parseForm(form);
    try {
      await API.createMaintenance(data);
      toast('Maintenance logged. Vehicle moved to In Shop.', 'success');
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

    if (btn.classList.contains('btn-close-maint')) {
      try {
        await API.closeMaintenance(id);
        toast('Maintenance closed. Vehicle is now Available.', 'success');
        await loadData();
      } catch (err) {
        toast(err.message, 'error');
      }
    }

    if (btn.classList.contains('btn-delete')) {
      if (confirmAction('Delete this maintenance record?')) {
        try {
          await API.deleteMaintenance(id);
          toast('Record deleted', 'success');
          await loadData();
        } catch (err) {
          toast(err.message, 'error');
        }
      }
    }
  });

  }

  async function loadData() {
    await API.seed();
    [vehicles, maintenance] = await Promise.all([
      API.getVehicles(),
      API.getMaintenance()
    ]);
    render();
  }

  function render() {
    const q = searchInput.value.trim();
    let data = maintenance.filter(m => {
      const vehicle = vehicles.find(v => v.id === m.vehicleId);
      const text = `${vehicle?.name || ''} ${vehicle?.regNo || ''} ${m.type} ${m.description}`.toLowerCase();
      return !q || text.includes(q.toLowerCase());
    });
    if (filterStatus.value) data = data.filter(m => m.status === filterStatus.value);

    if (!data.length) {
      tableBody.innerHTML = `<tr><td colspan="8" class="text-center text-muted">No maintenance records found.</td></tr>`;
      return;
    }

    tableBody.innerHTML = data.map(m => {
      const vehicle = vehicles.find(v => v.id === m.vehicleId);
      return `
        <tr>
          <td><strong>${escapeHtml(vehicle?.name || '-')}</strong><div class="text-xs text-muted">${escapeHtml(vehicle?.regNo || '-')}</div></td>
          <td>${escapeHtml(m.type)}</td>
          <td>${escapeHtml(m.description || '-')}</td>
          <td>${formatDate(m.startDate)}</td>
          <td>${m.endDate ? formatDate(m.endDate) : '-'}</td>
          <td>${formatCurrency(m.cost)}</td>
          <td>${renderBadge(m.status.toLowerCase(), { label: m.status })}</td>
          ${canModify ? `
          <td style="text-align: right;">
            ${m.status === 'Open'
              ? `<button class="btn btn-success btn-sm btn-close-maint" data-id="${m.id}">Close</button>`
              : ''}
            <button class="btn btn-danger btn-sm btn-delete" data-id="${m.id}">🗑️</button>
          </td>` : ''}
        </tr>
      `;
    }).join('');
  }

  function openModal() {
    form.reset();
    const select = $('#maintVehicleId');
    select.innerHTML = `<option value="">Select vehicle</option>` +
      vehicles.map(v => `<option value="${v.id}">${escapeHtml(v.name)} (${escapeHtml(v.regNo)})</option>`).join('');
    modal.classList.add('open');
  }

  function closeModal() {
    modal.classList.remove('open');
  }
});
