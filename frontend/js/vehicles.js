/**
 * TransitOps — Vehicle Registry Page Logic
 */

document.addEventListener('DOMContentLoaded', async () => {
  initTheme();
  initSidebar();
  initLogout();

  const user = guardPageAccess('vehicles');
  if (!user) return;
  renderUserPill(user);
  applyRoleAccess(user.role);

  const tableBody = $('#vehiclesTableBody');
  const searchInput = $('#searchVehicles');
  const filterStatus = $('#filterStatus');
  const filterType = $('#filterType');
  const modal = $('#vehicleModal');
  const form = $('#vehicleForm');
  const modalTitle = $('#modalTitle');

  let vehicles = [];

  await loadVehicles();

  const btnAdd = $('#btnAddVehicle');
  if (btnAdd) btnAdd.addEventListener('click', () => openModal());

  searchInput.addEventListener('input', debounce(render, 200));
  filterStatus.addEventListener('change', render);
  filterType.addEventListener('change', render);

  $$('[data-close-modal]').forEach(btn => {
    btn.addEventListener('click', closeModal);
  });

  modal.addEventListener('click', (e) => {
    if (e.target === modal) closeModal();
  });

  const canModify = canEdit('vehicles', user.role);

  if (canModify) {
    form.addEventListener('submit', async (e) => {
    e.preventDefault();
    const data = parseForm(form);
    const isEdit = !!data.id;

    try {
      if (isEdit) {
        await API.updateVehicle(data.id, data);
        toast('Vehicle updated successfully', 'success');
      } else {
        data.status = data.status || 'Available';
        await API.createVehicle(data);
        toast('Vehicle registered successfully', 'success');
      }
      closeModal();
      await loadVehicles();
    } catch (err) {
      toast(err.message, 'error');
    }
    });

    tableBody.addEventListener('click', async (e) => {
    const btn = e.target.closest('button');
    if (!btn) return;
    const id = btn.dataset.id;

    if (btn.classList.contains('btn-edit')) {
      const vehicle = vehicles.find(v => v.id === id);
      openModal(vehicle);
    }

    if (btn.classList.contains('btn-delete')) {
      if (confirmAction('Are you sure you want to delete this vehicle?')) {
        try {
          await API.deleteVehicle(id);
          toast('Vehicle deleted', 'success');
          await loadVehicles();
        } catch (err) {
          toast(err.message, 'error');
        }
      }
    }
  });

  }

  async function loadVehicles() {
    await API.seed();
    vehicles = await API.getVehicles();
    render();
  }

  function render() {
    const q = searchInput.value.trim();
    let data = filterData(vehicles, q, ['regNo', 'name', 'type', 'region']);
    if (filterStatus.value) data = data.filter(v => v.status === filterStatus.value);
    if (filterType.value) data = data.filter(v => v.type === filterType.value);

    if (!data.length) {
      tableBody.innerHTML = `<tr><td colspan="9" class="text-center text-muted">No vehicles found.</td></tr>`;
      return;
    }

    tableBody.innerHTML = data.map(v => `
      <tr>
        <td>${escapeHtml(v.regNo)}</td>
        <td><strong>${escapeHtml(v.name)}</strong></td>
        <td>${escapeHtml(v.type)}</td>
        <td>${formatNumber(v.capacity)}</td>
        <td>${formatNumber(v.odometer)} km</td>
        <td>${formatCurrency(v.acquisitionCost)}</td>
        <td>${escapeHtml(v.region || '-')}</td>
        <td>${renderBadge(getStatusClass(v.status), { label: v.status })}</td>
        ${canModify ? `
        <td style="text-align: right;">
          <button class="btn btn-ghost btn-sm btn-edit" data-id="${v.id}">✏️</button>
          <button class="btn btn-danger btn-sm btn-delete" data-id="${v.id}">🗑️</button>
        </td>` : ''}
      </tr>
    `).join('');
  }

  function openModal(vehicle = null) {
    form.reset();
    if (vehicle) {
      modalTitle.textContent = 'Edit Vehicle';
      populateForm(form, vehicle);
    } else {
      modalTitle.textContent = 'Add Vehicle';
      $('#vehicleId').value = '';
    }
    modal.classList.add('open');
  }

  function closeModal() {
    modal.classList.remove('open');
  }
});
