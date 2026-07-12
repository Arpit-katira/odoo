/**
 * TransitOps — Shared Utilities
 */

const $ = (selector, root = document) => root.querySelector(selector);
const $$ = (selector, root = document) => Array.from(root.querySelectorAll(selector));

const ROLES = {
  ADMIN: 'Admin',
  DISPATCHER: 'Dispatcher',
  SAFETY: 'Safety Officer',
  FINANCE: 'Financial Analyst'
};

function getAccess(module, role) {
  const matrix = {
    users:       { [ROLES.ADMIN]: 'full' },
    drivers:     { [ROLES.ADMIN]: 'full', [ROLES.DISPATCHER]: 'full', [ROLES.SAFETY]: 'full', [ROLES.FINANCE]: 'view' },
    vehicles:    { [ROLES.ADMIN]: 'full', [ROLES.DISPATCHER]: 'full', [ROLES.SAFETY]: 'view', [ROLES.FINANCE]: 'view' },
    trips:       { [ROLES.ADMIN]: 'full', [ROLES.DISPATCHER]: 'full', [ROLES.SAFETY]: 'view', [ROLES.FINANCE]: 'view' },
    maintenance: { [ROLES.ADMIN]: 'full', [ROLES.SAFETY]: 'full', [ROLES.FINANCE]: 'view' },
    fuel:        { [ROLES.ADMIN]: 'full', [ROLES.FINANCE]: 'full' },
    expenses:    { [ROLES.ADMIN]: 'full', [ROLES.FINANCE]: 'full' },
    reports:     { [ROLES.ADMIN]: 'full', [ROLES.FINANCE]: 'full', [ROLES.SAFETY]: 'view' }
  };
  return matrix[module]?.[role] || 'none';
}

function canEdit(module, role) {
  return getAccess(module, role) === 'full';
}

function canView(module, role) {
  return ['full', 'view'].includes(getAccess(module, role));
}

function applyRoleAccess(role) {
  $$('.nav-item').forEach(item => {
    const module = item.dataset.module;
    if (module && !canView(module, role)) item.classList.add('hidden');
  });

  $$('[data-require-view]').forEach(el => {
    const module = el.dataset.requireView;
    if (!canView(module, role)) el.classList.add('hidden');
  });

  $$('[data-require-edit]').forEach(el => {
    const module = el.dataset.requireEdit;
    if (!canEdit(module, role)) el.classList.add('hidden');
  });
}

function guardPageAccess(module) {
  const user = loadFromStorage('transitops-user');
  if (!user) {
    window.location.href = '../index.html';
    return null;
  }
  if (!canView(module, user.role)) {
    toast('You do not have access to this module.', 'error');
    window.location.href = '../dashboard.html';
    return null;
  }
  return user;
}

function formatDate(dateStr) {
  if (!dateStr) return '-';
  const d = new Date(dateStr);
  return d.toLocaleDateString('en-GB', { day: '2-digit', month: 'short', year: 'numeric' });
}

function formatCurrency(value) {
  const n = Number(value);
  return isNaN(n) ? '-' : '₹' + n.toLocaleString('en-IN', { maximumFractionDigits: 2 });
}

function formatNumber(value, decimals = 0) {
  const n = Number(value);
  return isNaN(n) ? '-' : n.toLocaleString('en-IN', { maximumFractionDigits: decimals });
}

function uuid() {
  return 'xxxxxxxx-xxxx-4xxx-yxxx-xxxxxxxxxxxx'.replace(/[xy]/g, c => {
    const r = Math.random() * 16 | 0;
    const v = c === 'x' ? r : (r & 0x3 | 0x8);
    return v.toString(16);
  });
}

function escapeHtml(str) {
  if (str == null) return '';
  return String(str)
    .replace(/&/g, '&amp;')
    .replace(/</g, '&lt;')
    .replace(/>/g, '&gt;')
    .replace(/"/g, '&quot;');
}

function debounce(fn, delay = 300) {
  let timer;
  return (...args) => {
    clearTimeout(timer);
    timer = setTimeout(() => fn(...args), delay);
  };
}

function toast(message, type = 'info') {
  let container = $('.toast-container');
  if (!container) {
    container = document.createElement('div');
    container.className = 'toast-container';
    document.body.appendChild(container);
  }
  const el = document.createElement('div');
  el.className = `toast ${type}`;
  el.innerHTML = `<div class="font-semibold">${escapeHtml(message)}</div>`;
  container.appendChild(el);
  setTimeout(() => el.remove(), 3500);
}

function renderBadge(status, options = {}) {
  const label = options.label || status || 'Unknown';
  const cls = 'badge-' + (status || 'unknown').toLowerCase().replace(/\s+/g, '-');
  return `<span class="badge ${cls}">${escapeHtml(label)}</span>`;
}

function getStatusClass(status) {
  return (status || 'unknown').toLowerCase().replace(/\s+/g, '-');
}

function saveToStorage(key, value) {
  try {
    localStorage.setItem(key, JSON.stringify(value));
  } catch (e) {
    console.error('Storage error:', e);
  }
}

function loadFromStorage(key, fallback = null) {
  try {
    const raw = localStorage.getItem(key);
    return raw ? JSON.parse(raw) : fallback;
  } catch (e) {
    return fallback;
  }
}

function confirmAction(message) {
  return window.confirm(message);
}

function initTheme() {
  const theme = loadFromStorage('transitops-theme', 'light');
  document.documentElement.setAttribute('data-theme', theme);
  const toggles = $$('[data-theme-toggle]');
  toggles.forEach(btn => {
    btn.addEventListener('click', () => {
      const current = document.documentElement.getAttribute('data-theme');
      const next = current === 'dark' ? 'light' : 'dark';
      document.documentElement.setAttribute('data-theme', next);
      saveToStorage('transitops-theme', next);
      btn.textContent = next === 'dark' ? '☀️' : '🌙';
    });
    btn.textContent = theme === 'dark' ? '☀️' : '🌙';
  });
}

function initSidebar() {
  const toggle = $('.menu-toggle');
  const sidebar = $('.sidebar');
  const current = location.pathname.split('/').pop() || 'dashboard.html';

  $$('.nav-item').forEach(item => {
    const href = item.getAttribute('href')?.split('/').pop();
    if (href === current) item.classList.add('active');
  });

  if (toggle && sidebar) {
    toggle.addEventListener('click', () => sidebar.classList.toggle('open'));
    document.addEventListener('click', (e) => {
      if (window.innerWidth > 1024) return;
      if (!sidebar.contains(e.target) && !toggle.contains(e.target)) {
        sidebar.classList.remove('open');
      }
    });
  }
}

function initLogout() {
  $$('[data-logout]').forEach(btn => {
    btn.addEventListener('click', () => {
      localStorage.removeItem('transitops-user');
      window.location.href = '../index.html';
    });
  });
}

function requireAuth() {
  const user = loadFromStorage('transitops-user');
  if (!user) {
    window.location.href = '../index.html';
    return null;
  }
  return user;
}

function renderUserPill(user) {
  const pill = $('.user-pill');
  if (!pill || !user) return;
  const initials = user.name?.split(' ').map(n => n[0]).join('').slice(0, 2).toUpperCase() || 'U';
  pill.querySelector('.user-avatar').textContent = initials;
  pill.querySelector('.user-name').textContent = user.name;
  pill.querySelector('.user-role').textContent = user.role;
}

function parseForm(form) {
  const data = {};
  const formData = new FormData(form);
  formData.forEach((value, key) => {
    const input = form.querySelector(`[name="${key}"]`);
    if (input?.type === 'number') {
      data[key] = value === '' ? null : Number(value);
    } else if (input?.type === 'date') {
      data[key] = value || null;
    } else {
      data[key] = value;
    }
  });
  return data;
}

function populateForm(form, data) {
  Object.entries(data).forEach(([key, value]) => {
    const input = form.querySelector(`[name="${key}"]`);
    if (input) input.value = value == null ? '' : value;
  });
}

function filterData(data, query, fields) {
  if (!query) return data;
  const q = query.toLowerCase();
  return data.filter(item => fields.some(f => String(item[f] || '').toLowerCase().includes(q)));
}

function sortData(data, field, dir = 'asc') {
  return [...data].sort((a, b) => {
    let x = a[field], y = b[field];
    if (typeof x === 'string') x = x.toLowerCase();
    if (typeof y === 'string') y = y.toLowerCase();
    if (x == null) return 1;
    if (y == null) return -1;
    if (x < y) return dir === 'asc' ? -1 : 1;
    if (x > y) return dir === 'asc' ? 1 : -1;
    return 0;
  });
}
