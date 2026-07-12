document.addEventListener('DOMContentLoaded', async () => {
  initTheme();
  initSidebar();
  initLogout();

  const user = guardPageAccess('fuel');
  if (!user) return;
  renderUserPill(user);
  applyRoleAccess(user.role);

  const fuelTableBody = $('#fuelTableBody');
  const expensesTableBody = $('#expensesTableBody');
  const searchFuel = $('#searchFuel');
  const searchExpenses = $('#searchExpenses');

  const fuelModal = $('#fuelModal');
  const fuelForm = $('#fuelForm');
  const expenseModal = $('#expenseModal');
  const expenseForm = $('#expenseForm');

  let vehicles = [];
  let fuelLogs = [];
  let expenses = [];

  await loadData();

  const canModify = canEdit('fuel', user.role);

  const btnAddFuel = $('#btnAddFuel');
  const btnAddExpense = $('#btnAddExpense');
  if (btnAddFuel) btnAddFuel.addEventListener('click', () => openFuelModal());
  if (btnAddExpense) btnAddExpense.addEventListener('click', () => openExpenseModal());

  searchFuel.addEventListener('input', debounce(render, 200));
  searchExpenses.addEventListener('input', debounce(render, 200));

  $$('[data-close-modal]').forEach(btn => btn.addEventListener('click', closeFuelModal));
  $$('[data-close-expense]').forEach(btn => btn.addEventListener('click', closeExpenseModal));
  fuelModal.addEventListener('click', (e) => { if (e.target === fuelModal) closeFuelModal(); });
  expenseModal.addEventListener('click', (e) => { if (e.target === expenseModal) closeExpenseModal(); });

  if (canModify) {
    fuelForm.addEventListener('submit', async (e) => {
    e.preventDefault();
    try {
      await API.createFuelLog(parseForm(fuelForm));
      toast('Fuel log added', 'success');
      closeFuelModal();
      await loadData();
    } catch (err) {
      toast(err.message, 'error');
    }
  });

    expenseForm.addEventListener('submit', async (e) => {
    e.preventDefault();
    try {
      await API.createExpense(parseForm(expenseForm));
      toast('Expense recorded', 'success');
      closeExpenseModal();
      await loadData();
    } catch (err) {
      toast(err.message, 'error');
    }
  });

    fuelTableBody.addEventListener('click', async (e) => {
    const btn = e.target.closest('button');
    if (!btn) return;
    const id = btn.dataset.id;
    if (btn.classList.contains('btn-delete') && confirmAction('Delete this fuel log?')) {
      try {
        await API.deleteFuelLog(id);
        toast('Fuel log deleted', 'success');
        await loadData();
      } catch (err) {
        toast(err.message, 'error');
      }
    }
  });

    expensesTableBody.addEventListener('click', async (e) => {
    const btn = e.target.closest('button');
    if (!btn) return;
    const id = btn.dataset.id;
    if (btn.classList.contains('btn-delete') && confirmAction('Delete this expense?')) {
      try {
        await API.deleteExpense(id);
        toast('Expense deleted', 'success');
        await loadData();
      } catch (err) {
        toast(err.message, 'error');
      }
    }
  });

  }

  async function loadData() {
    await API.seed();
    [vehicles, fuelLogs, expenses] = await Promise.all([
      API.getVehicles(),
      API.getFuelLogs(),
      API.getExpenses()
    ]);
    render();
  }

  function render() {
    const fq = searchFuel.value.trim();
    let fData = fuelLogs.filter(f => {
      const vehicle = vehicles.find(v => v.id === f.vehicleId);
      const text = `${vehicle?.name || ''} ${vehicle?.regNo || ''} ${f.date}`.toLowerCase();
      return !fq || text.includes(fq.toLowerCase());
    });

    if (!fData.length) {
      fuelTableBody.innerHTML = `<tr><td colspan="5" class="text-center text-muted">No fuel logs yet.</td></tr>`;
    } else {
      fuelTableBody.innerHTML = fData.map(f => {
        const vehicle = vehicles.find(v => v.id === f.vehicleId);
        return `
          <tr>
            <td><strong>${escapeHtml(vehicle?.name || '-')}</strong><div class="text-xs text-muted">${escapeHtml(vehicle?.regNo || '-')}</div></td>
            <td>${formatDate(f.date)}</td>
            <td>${formatNumber(f.liters, 2)} L</td>
            <td>${formatCurrency(f.cost)}</td>
            ${canModify ? `
            <td style="text-align: right;">
              <button class="btn btn-danger btn-sm btn-delete" data-id="${f.id}">🗑️</button>
            </td>` : ''}
          </tr>
        `;
      }).join('');
    }

    const eq = searchExpenses.value.trim();
    let eData = expenses.filter(ex => {
      const vehicle = vehicles.find(v => v.id === ex.vehicleId);
      const text = `${vehicle?.name || ''} ${vehicle?.regNo || ''} ${ex.type} ${ex.description}`.toLowerCase();
      return !eq || text.includes(eq.toLowerCase());
    });

    if (!eData.length) {
      expensesTableBody.innerHTML = `<tr><td colspan="5" class="text-center text-muted">No expenses yet.</td></tr>`;
    } else {
      expensesTableBody.innerHTML = eData.map(ex => {
        const vehicle = vehicles.find(v => v.id === ex.vehicleId);
        return `
          <tr>
            <td><strong>${escapeHtml(vehicle?.name || '-')}</strong><div class="text-xs text-muted">${escapeHtml(vehicle?.regNo || '-')}</div></td>
            <td>${escapeHtml(ex.type)}</td>
            <td>${formatDate(ex.date)}</td>
            <td>${formatCurrency(ex.amount)}</td>
            ${canModify ? `
            <td style="text-align: right;">
              <button class="btn btn-danger btn-sm btn-delete" data-id="${ex.id}">🗑️</button>
            </td>` : ''}
          </tr>
        `;
      }).join('');
    }
  }

  function populateVehicleSelect(selectId, selected = '') {
    const select = $(selectId);
    select.innerHTML = `<option value="">Select vehicle</option>` +
      vehicles.map(v => `<option value="${v.id}" ${v.id === selected ? 'selected' : ''}>${escapeHtml(v.name)} (${escapeHtml(v.regNo)})</option>`).join('');
  }

  function openFuelModal() {
    fuelForm.reset();
    populateVehicleSelect('#fuelVehicleId');
    fuelModal.classList.add('open');
  }

  function closeFuelModal() {
    fuelModal.classList.remove('open');
  }

  function openExpenseModal() {
    expenseForm.reset();
    populateVehicleSelect('#expenseVehicleId');
    expenseModal.classList.add('open');
  }

  function closeExpenseModal() {
    expenseModal.classList.remove('open');
  }
});
