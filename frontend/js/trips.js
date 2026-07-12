/**
 * TransitOps — Trip Management Page Logic
 */

document.addEventListener('DOMContentLoaded', async () => {
  initTheme();
  initSidebar();
  initLogout();

  const user = guardPageAccess('trips');
  if (!user) return;
  renderUserPill(user);
  applyRoleAccess(user.role);

  const tableBody = $('#tripsTableBody');
  const searchInput = $('#searchTrips');
  const filterStatus = $('#filterStatus');

  const tripModal = $('#tripModal');
  const tripForm = $('#tripForm');
  const tripVehicleSelect = $('#tripVehicleId');
  const tripDriverSelect = $('#tripDriverId');
  const tripCargoWeight = $('#tripCargoWeight');
  const capacityHint = $('#capacityHint');

  const completeModal = $('#completeModal');
  const completeForm = $('#completeForm');

  let vehicles = [];
  let drivers = [];
  let trips = [];

  await loadData();

  const btnAdd = $('#btnAddTrip');
  if (btnAdd) btnAdd.addEventListener('click', () => openTripModal());

  searchInput.addEventListener('input', debounce(render, 200));
  filterStatus.addEventListener('change', render);

  $$('[data-close-modal]').forEach(btn => btn.addEventListener('click', closeTripModal));
  $$('[data-close-complete]').forEach(btn => btn.addEventListener('click', closeCompleteModal));
  tripModal.addEventListener('click', (e) => { if (e.target === tripModal) closeTripModal(); });
  completeModal.addEventListener('click', (e) => { if (e.target === completeModal) closeCompleteModal(); });

  tripVehicleSelect.addEventListener('change', updateCapacityHint);
  tripCargoWeight.addEventListener('input', updateCapacityHint);

  const canModify = canEdit('trips', user.role);

  if (canModify) {
    tripForm.addEventListener('submit', async (e) => {
    e.preventDefault();
    const data = parseForm(tripForm);
    const isEdit = !!data.id;

    try {
      if (isEdit) {
        await API.updateTrip(data.id, data);
        toast('Trip updated', 'success');
      } else {
        await API.createTrip(data);
        toast('Trip created successfully', 'success');
      }
      closeTripModal();
      await loadData();
    } catch (err) {
      toast(err.message, 'error');
    }
    });

    completeForm.addEventListener('submit', async (e) => {
    e.preventDefault();
    const data = parseForm(completeForm);
    try {
      await API.completeTrip(data.tripId, data);
      toast('Trip completed', 'success');
      closeCompleteModal();
      await loadData();
    } catch (err) {
      toast(err.message, 'error');
    }
    });

    tableBody.addEventListener('click', async (e) => {
    const btn = e.target.closest('button');
    if (!btn) return;
    const id = btn.dataset.id;
    const trip = trips.find(t => t.id === id);

    if (btn.classList.contains('btn-edit')) {
      openTripModal(trip);
    }

    if (btn.classList.contains('btn-dispatch')) {
      try {
        await API.dispatchTrip(id);
        toast('Trip dispatched', 'success');
        await loadData();
      } catch (err) {
        toast(err.message, 'error');
      }
    }

    if (btn.classList.contains('btn-complete')) {
      $('#completeTripId').value = id;
      completeModal.classList.add('open');
    }

    if (btn.classList.contains('btn-cancel')) {
      if (confirmAction('Cancel this dispatched trip? Vehicle and driver will be freed.')) {
        try {
          await API.cancelTrip(id);
          toast('Trip cancelled', 'success');
          await loadData();
        } catch (err) {
          toast(err.message, 'error');
        }
      }
    }

    if (btn.classList.contains('btn-delete')) {
      if (confirmAction('Delete this trip?')) {
        try {
          await API.deleteTrip(id);
          toast('Trip deleted', 'success');
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
    [vehicles, drivers, trips] = await Promise.all([
      API.getVehicles(),
      API.getDrivers(),
      API.getTrips()
    ]);
    render();
  }

  function availableVehicles() {
    return vehicles.filter(v => v.status === 'Available');
  }

  function availableDrivers() {
    return drivers.filter(d => d.status === 'Available' && new Date(d.licenseExpiry) >= new Date());
  }

  function populateSelects(selectedVehicleId = '', selectedDriverId = '') {
    tripVehicleSelect.innerHTML = `<option value="">Select available vehicle</option>` +
      availableVehicles().map(v => `<option value="${v.id}" ${v.id === selectedVehicleId ? 'selected' : ''}>${escapeHtml(v.name)} (${escapeHtml(v.regNo)}) — ${v.capacity} kg</option>`).join('');

    tripDriverSelect.innerHTML = `<option value="">Select available driver</option>` +
      availableDrivers().map(d => `<option value="${d.id}" ${d.id === selectedDriverId ? 'selected' : ''}>${escapeHtml(d.name)} — ${d.licenseCategory}</option>`).join('');
  }

  function updateCapacityHint() {
    const vehicleId = tripVehicleSelect.value;
    const vehicle = vehicles.find(v => v.id === vehicleId);
    const weight = Number(tripCargoWeight.value);
    if (!vehicle) {
      capacityHint.textContent = '';
      return;
    }
    const remaining = vehicle.capacity - weight;
    if (weight > vehicle.capacity) {
      capacityHint.innerHTML = `<span style="color: var(--danger);">⚠ Cargo exceeds vehicle capacity of ${vehicle.capacity} kg</span>`;
    } else {
      capacityHint.innerHTML = `<span style="color: var(--success);">✓ Capacity used: ${weight || 0} / ${vehicle.capacity} kg (${remaining >= 0 ? remaining : 0} kg remaining)</span>`;
    }
  }

  function render() {
    const q = searchInput.value.trim();
    let data = trips.filter(t => {
      const vehicle = vehicles.find(v => v.id === t.vehicleId);
      const driver = drivers.find(d => d.id === t.driverId);
      const text = `${t.source} ${t.destination} ${vehicle?.name || ''} ${vehicle?.regNo || ''} ${driver?.name || ''}`.toLowerCase();
      return !q || text.includes(q.toLowerCase());
    });
    if (filterStatus.value) data = data.filter(t => t.status === filterStatus.value);

    if (!data.length) {
      tableBody.innerHTML = `<tr><td colspan="8" class="text-center text-muted">No trips found.</td></tr>`;
      return;
    }

    tableBody.innerHTML = data.map(t => {
      const vehicle = vehicles.find(v => v.id === t.vehicleId);
      const driver = drivers.find(d => d.id === t.driverId);
      let actions = '';
      if (t.status === 'Draft') {
        actions = `
          <button class="btn btn-primary btn-sm btn-dispatch" data-id="${t.id}">Dispatch</button>
          <button class="btn btn-ghost btn-sm btn-edit" data-id="${t.id}">✏️</button>
          <button class="btn btn-danger btn-sm btn-delete" data-id="${t.id}">🗑️</button>`;
      } else if (t.status === 'Dispatched') {
        actions = `
          <button class="btn btn-success btn-sm btn-complete" data-id="${t.id}">Complete</button>
          <button class="btn btn-warning btn-sm btn-cancel" data-id="${t.id}">Cancel</button>`;
      } else {
        actions = `<span class="text-muted text-sm">No actions</span>`;
      }

      return `
        <tr>
          <td><strong>${escapeHtml(t.source)} → ${escapeHtml(t.destination)}</strong></td>
          <td>${escapeHtml(vehicle?.name || '-')}</td>
          <td>${escapeHtml(driver?.name || '-')}</td>
          <td>${formatNumber(t.cargoWeight)}</td>
          <td>${formatNumber(t.plannedDistance)}${t.actualDistance ? ` <span class="text-muted">(actual: ${t.actualDistance})</span>` : ''}</td>
          <td>${renderBadge(getStatusClass(t.status), { label: t.status })}</td>
          <td>${formatDate(t.createdAt)}</td>
          ${canModify ? `<td style="text-align: right;">${actions}</td>` : ''}
        </tr>
      `;
    }).join('');
  }

  function openTripModal(trip = null) {
    tripForm.reset();
    populateSelects(trip?.vehicleId, trip?.driverId);
    if (trip) {
      $('#modalTitle').textContent = 'Edit Trip';
      populateForm(tripForm, trip);
    } else {
      $('#modalTitle').textContent = 'Create Trip';
      $('#tripId').value = '';
    }
    updateCapacityHint();
    tripModal.classList.add('open');
  }

  function closeTripModal() {
    tripModal.classList.remove('open');
  }

  function closeCompleteModal() {
    completeModal.classList.remove('open');
  }
});
