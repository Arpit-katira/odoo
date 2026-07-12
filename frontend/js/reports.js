
document.addEventListener('DOMContentLoaded', async () => {
  initTheme();
  initSidebar();
  initLogout();

  const user = guardPageAccess('reports');
  if (!user) return;
  renderUserPill(user);
  applyRoleAccess(user.role);

  const reportTableBody = $('#reportTableBody');
  const reportKpis = $('#reportKpis');
  const searchReport = $('#searchReport');
  const btnExportCsv = $('#btnExportCsv');

  let reportData = [];

  await loadReport();

  searchReport.addEventListener('input', debounce(render, 200));

  const canModify = canEdit('reports', user.role);

  if (canModify) {
    btnExportCsv.addEventListener('click', () => {
    if (!reportData.length) return;
    const headers = ['Vehicle', 'Reg No', 'Type', 'Distance km', 'Fuel Cost', 'Maintenance', 'Other Expenses', 'Total Cost', 'Efficiency km/L', 'ROI %'];
    const rows = reportData.map(r => [
      r.name, r.regNo, r.type, r.distance, r.fuelCost, r.maintenanceCost, r.otherCost, r.totalCost, r.efficiency, r.roi
    ]);
    const csv = [headers, ...rows].map(row => row.map(cell => `"${cell}"`).join(',')).join('\n');
    const blob = new Blob([csv], { type: 'text/csv' });
    const url = URL.createObjectURL(blob);
    const a = document.createElement('a');
    a.href = url;
    a.download = `transitops-report-${new Date().toISOString().slice(0, 10)}.csv`;
    a.click();
      URL.revokeObjectURL(url);
      toast('CSV exported successfully', 'success');
    });
  }

  async function loadReport() {
    await API.seed();
    reportData = await API.getVehicleCostReport();
    render();
  }

  function render() {
    const q = searchReport.value.trim();
    let data = filterData(reportData, q, ['name', 'regNo', 'type']);

    const totalDistance = data.reduce((s, r) => s + r.distance, 0);
    const totalFuelCost = data.reduce((s, r) => s + r.fuelCost, 0);
    const totalMaintenance = data.reduce((s, r) => s + r.maintenanceCost, 0);
    const totalOther = data.reduce((s, r) => s + r.otherCost, 0);
    const totalCost = data.reduce((s, r) => s + r.totalCost, 0);
    const avgEfficiency = data.length ? (data.reduce((s, r) => s + Number(r.efficiency), 0) / data.length).toFixed(2) : 0;

    reportKpis.innerHTML = [
      { label: 'Total Distance', value: `${formatNumber(totalDistance)} km`, icon: '🛣️', color: 'var(--primary)' },
      { label: 'Fuel Cost', value: formatCurrency(totalFuelCost), icon: '⛽', color: 'var(--warning)' },
      { label: 'Maintenance Cost', value: formatCurrency(totalMaintenance), icon: '🔧', color: 'var(--danger)' },
      { label: 'Other Expenses', value: formatCurrency(totalOther), icon: '💸', color: 'var(--info)' },
      { label: 'Total Operational Cost', value: formatCurrency(totalCost), icon: '💰', color: 'var(--secondary)' },
      { label: 'Avg Efficiency', value: `${avgEfficiency} km/L`, icon: '📈', color: 'var(--success)' }
    ].map(k => `
      <div class="kpi-card">
        <div class="flex justify-between items-center">
          <span class="kpi-label">${k.label}</span>
          <div class="kpi-icon" style="background: ${k.color}20; color: ${k.color};">${k.icon}</div>
        </div>
        <div class="kpi-value" style="color: ${k.color};">${k.value}</div>
      </div>
    `).join('');

    if (!data.length) {
      reportTableBody.innerHTML = `<tr><td colspan="9" class="text-center text-muted">No data found.</td></tr>`;
      return;
    }

    reportTableBody.innerHTML = data.map(r => `
      <tr>
        <td><strong>${escapeHtml(r.name)}</strong></td>
        <td>${escapeHtml(r.regNo)}</td>
        <td>${formatNumber(r.distance)}</td>
        <td>${formatCurrency(r.fuelCost)}</td>
        <td>${formatCurrency(r.maintenanceCost)}</td>
        <td>${formatCurrency(r.otherCost)}</td>
        <td><strong>${formatCurrency(r.totalCost)}</strong></td>
        <td>${r.efficiency}</td>
        <td>${r.roi}%</td>
      </tr>
    `).join('');
  }
});
