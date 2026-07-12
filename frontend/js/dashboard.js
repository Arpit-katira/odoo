
document.addEventListener('DOMContentLoaded', async () => {
  initTheme();
  initSidebar();
  initLogout();

  const user = guardPageAccess('dashboard');
  if (!user) return;
  renderUserPill(user);
  applyRoleAccess(user.role);

  const kpiGrid = $('#kpiGrid');
  const fleetTableBody = $('#fleetTableBody');
  const activeTripsList = $('#activeTripsList');

  const filterType = $('#filterType');
  const filterStatus = $('#filterStatus');
  const filterRegion = $('#filterRegion');

  let vehicles = [];
  let drivers = [];
  let trips = [];

  try {
    await API.seed();
    [vehicles, drivers, trips] = await Promise.all([
      API.getVehicles(),
      API.getDrivers(),
      API.getTrips()
    ]);
    render();
  } catch (err) {
    toast(err.message, 'error');
  }

  [filterType, filterStatus, filterRegion].forEach(el => {
    el.addEventListener('change', render);
  });

  function render() {
    const stats = computeStats(vehicles, trips, drivers);
    renderKPIs(stats);

    const filtered = vehicles.filter(v => {
      return (!filterType.value || v.type === filterType.value) &&
             (!filterStatus.value || v.status === filterStatus.value) &&
             (!filterRegion.value || v.region === filterRegion.value);
    });

    renderFleetTable(filtered);
    renderActiveTrips(trips.filter(t => t.status === 'Dispatched'));
  }

  function computeStats(vehicles, trips, drivers) {
    const activeVehicles = vehicles.filter(v => v.status === 'On Trip').length;
    const availableVehicles = vehicles.filter(v => v.status === 'Available').length;
    const inMaintenance = vehicles.filter(v => v.status === 'In Shop').length;
    const activeTrips = trips.filter(t => t.status === 'Dispatched').length;
    const pendingTrips = trips.filter(t => t.status === 'Draft').length;
    const driversOnDuty = drivers.filter(d => d.status === 'On Trip').length;
    const totalOperational = vehicles.filter(v => v.status !== 'Retired').length;
    const fleetUtilization = totalOperational ? Math.round((activeVehicles / totalOperational) * 100) : 0;

    return { activeVehicles, availableVehicles, inMaintenance, activeTrips, pendingTrips, driversOnDuty, fleetUtilization };
  }

  function renderKPIs(stats) {
    const kpis = [
      { label: 'Active Vehicles', value: stats.activeVehicles, icon: '🚛', color: 'var(--primary)' },
      { label: 'Available Vehicles', value: stats.availableVehicles, icon: '🅿️', color: 'var(--success)' },
      { label: 'In Maintenance', value: stats.inMaintenance, icon: '🔧', color: 'var(--warning)' },
      { label: 'Active Trips', value: stats.activeTrips, icon: '🛣️', color: 'var(--primary)' },
      { label: 'Pending Trips', value: stats.pendingTrips, icon: '⏳', color: 'var(--warning)' },
      { label: 'Drivers On Duty', value: stats.driversOnDuty, icon: '👤', color: 'var(--info)' },
      { label: 'Fleet Utilization', value: `${stats.fleetUtilization}%`, icon: '📈', color: 'var(--secondary)' }
    ];

    kpiGrid.innerHTML = kpis.map(k => `
      <div class="kpi-card">
        <div class="flex justify-between items-center">
          <span class="kpi-label">${k.label}</span>
          <div class="kpi-icon" style="background: ${k.color}20; color: ${k.color};">${k.icon}</div>
        </div>
        <div class="kpi-value" style="color: ${k.color};">${k.value}</div>
      </div>
    `).join('');
  }

  function renderFleetTable(data) {
    if (!data.length) {
      fleetTableBody.innerHTML = `<tr><td colspan="5" class="text-center text-muted">No vehicles match the filters.</td></tr>`;
      return;
    }
    fleetTableBody.innerHTML = data.map(v => `
      <tr>
        <td><strong>${escapeHtml(v.name)}</strong></td>
        <td>${escapeHtml(v.regNo)}</td>
        <td>${escapeHtml(v.type)}</td>
        <td>${escapeHtml(v.region || '-')}</td>
        <td>${renderBadge(getStatusClass(v.status), { label: v.status })}</td>
      </tr>
    `).join('');
  }

  function renderActiveTrips(data) {
    if (!data.length) {
      activeTripsList.innerHTML = `
        <div class="empty-state">
          <div class="icon">🛣️</div>
          <div>No active trips right now.</div>
        </div>`;
      return;
    }
    activeTripsList.innerHTML = data.map(t => {
      const vehicle = vehicles.find(v => v.id === t.vehicleId);
      const driver = drivers.find(d => d.id === t.driverId);
      return `
        <div class="card" style="margin-bottom: 0.75rem; padding: 1rem;">
          <div class="flex justify-between items-center mb-1">
            <strong>${escapeHtml(t.source)} → ${escapeHtml(t.destination)}</strong>
            ${renderBadge('on-trip', { label: t.status })}
          </div>
          <div class="text-sm text-secondary">${escapeHtml(vehicle?.name || '-')}</div>
          <div class="text-sm text-secondary">Driver: ${escapeHtml(driver?.name || '-')}</div>
          <div class="text-xs text-muted mt-1">Cargo: ${t.cargoWeight} kg • ${t.plannedDistance} km</div>
        </div>
      `;
    }).join('');
  }
});
