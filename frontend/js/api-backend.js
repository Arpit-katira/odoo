
const BASE_URL = 'http://localhost:8080';

function getToken() {
  return localStorage.getItem('transitops-token');
}

function authHeaders() {
  const headers = { 'Content-Type': 'application/json' };
  const token = getToken();
  if (token) headers['Authorization'] = `Bearer ${token}`;
  return headers;
}

async function request(method, path, body = null) {
  const options = { method, headers: authHeaders() };
  if (body !== null) options.body = JSON.stringify(body);

  const res = await fetch(`${BASE_URL}${path}`, options);
  const json = await res.json().catch(() => null);

  if (!res.ok) {
    throw new Error(json?.message || json?.error || `HTTP ${res.status}`);
  }
  return json?.data ?? json;
}

function getAll(key) {
  try {
    return JSON.parse(localStorage.getItem(`transitops-${key}`)) || [];
  } catch {
    return [];
  }
}

function saveAll(key, data) {
  localStorage.setItem(`transitops-${key}`, JSON.stringify(data));
}

// ------------------------------------------------------------------
// Field mapping helpers
// ------------------------------------------------------------------
function toFrontendVehicle(v) {
  return v ? {
    id: String(v.id),
    regNo: v.registrationNumber,
    name: v.model,
    type: v.vehicleType,
    capacity: v.maxLoadCapacity,
    odometer: v.odometerReading,
    acquisitionCost: v.acquisitionCost,
    region: v.region,
    status: v.status,
    createdAt: v.createdAt
  } : null;
}

function toBackendVehicle(payload) {
  return {
    registrationNumber: payload.regNo,
    model: payload.name,
    vehicleType: payload.type,
    maxLoadCapacity: payload.capacity,
    acquisitionCost: payload.acquisitionCost,
    odometerReading: payload.odometer,
    region: payload.region,
    status: payload.status
  };
}

function toFrontendDriver(d) {
  return d ? {
    id: String(d.id),
    name: d.user?.fullName || `Driver #${d.id}`,
    userId: d.user?.id ? String(d.user.id) : null,
    licenseNo: d.licenseNumber,
    licenseCategory: d.licenseCategory,
    licenseExpiry: d.licenseExpiry,
    contact: d.user?.phoneNumber || d.phoneNumber,
    phoneNumber: d.user?.phoneNumber || d.phoneNumber,
    safetyScore: d.safetyScore,
    status: d.status,
    createdAt: d.createdAt
  } : null;
}

function toBackendDriver(payload) {
  return {
    userId: payload.userId ? Number(payload.userId) : null,
    licenseNumber: payload.licenseNo,
    licenseCategory: payload.licenseCategory,
    licenseExpiry: payload.licenseExpiry
  };
}

function toFrontendTrip(t) {
  return t ? {
    id: String(t.id),
    tripNumber: t.tripNumber,
    vehicleId: String(t.vehicleId),
    driverId: String(t.driverId),
    source: t.source,
    destination: t.destination,
    cargoWeight: t.cargoWeight,
    plannedDistance: t.plannedDistance,
    startOdometer: t.startOdometer,
    endOdometer: t.endOdometer,
    revenue: t.revenue,
    status: t.status,
    dispatchedAt: t.dispatchedAt,
    completedAt: t.completedAt,
    createdAt: t.createdAt || t.dispatchedAt
  } : null;
}

function toBackendTrip(payload) {
  return {
    vehicleId: Number(payload.vehicleId),
    driverId: Number(payload.driverId),
    source: payload.source,
    destination: payload.destination,
    cargoWeight: payload.cargoWeight,
    plannedDistance: payload.plannedDistance,
    revenue: payload.revenue
  };
}

function toBackendUpdateTrip(payload) {
  return {
    source: payload.source,
    destination: payload.destination,
    cargoWeight: payload.cargoWeight,
    plannedDistance: payload.plannedDistance,
    revenue: payload.revenue
  };
}

function toFrontendMaintenance(m) {
  return m ? {
    id: String(m.id),
    vehicleId: String(m.vehicle?.id || m.vehicleId),
    type: m.issue,
    issue: m.issue,
    description: m.description,
    cost: m.cost,
    status: m.status,
    startDate: m.startedAt?.slice(0, 10),
    startedAt: m.startedAt,
    completedAt: m.completedAt,
    endDate: m.completedAt?.slice(0, 10),
    createdAt: m.createdAt
  } : null;
}

function toBackendMaintenance(payload) {
  return {
    vehicleId: Number(payload.vehicleId),
    issue: payload.type || payload.issue,
    description: payload.description,
    cost: payload.cost,
    status: payload.status || 'OPEN'
  };
}

function toFrontendFuel(f) {
  return f ? {
    id: String(f.id),
    tripId: String(f.tripId),
    liters: f.liters,
    cost: f.cost,
    odometer: f.odometerReading,
    odometerReading: f.odometerReading,
    date: f.loggedAt?.slice(0, 10),
    loggedAt: f.loggedAt
  } : null;
}

function toBackendFuel(payload) {
  return {
    tripId: Number(payload.tripId),
    liters: payload.liters,
    cost: payload.cost,
    odometerReading: payload.odometer || payload.odometerReading
  };
}

function toFrontendExpense(e) {
  return e ? {
    id: String(e.id),
    tripId: String(e.trip?.id || e.tripId),
    type: e.expenseType,
    expenseType: e.expenseType,
    amount: e.amount,
    description: e.description,
    date: e.createdAt?.slice(0, 10),
    createdAt: e.createdAt
  } : null;
}

function toBackendExpense(payload) {
  return {
    tripId: Number(payload.tripId),
    expenseType: payload.type || payload.expenseType,
    amount: payload.amount,
    description: payload.description
  };
}

function toFrontendUser(u) {
  return u ? {
    id: String(u.id),
    name: u.fullName,
    fullName: u.fullName,
    email: u.email,
    phoneNumber: u.phoneNumber,
    roleId: String(u.roleId),
    role: u.roleName,
    isActive: u.isActive,
    createdAt: u.createdAt
  } : null;
}

function toBackendUser(payload) {
  return {
    fullName: payload.name || payload.fullName,
    email: payload.email,
    password: payload.password,
    phoneNumber: payload.phoneNumber,
    roleId: Number(payload.roleId)
  };
}

function toBackendUpdateUser(payload) {
  const data = {};
  if (payload.name || payload.fullName) data.fullName = payload.name || payload.fullName;
  if (payload.email) data.email = payload.email;
  if (payload.phoneNumber !== undefined) data.phoneNumber = payload.phoneNumber;
  if (payload.roleId) data.roleId = Number(payload.roleId);
  if (payload.isActive !== undefined) data.isActive = payload.isActive;
  if (payload.password) data.password = payload.password;
  return data;
}

// ------------------------------------------------------------------
// API
// ------------------------------------------------------------------
const API = {
  login: async (email, password) => {
    const data = await request('POST', '/api/v1/auth/login', { email, password });
    if (data.token) {
      localStorage.setItem('transitops-token', data.token);
      localStorage.setItem('transitops-user', JSON.stringify(toFrontendUser(data.user)));
    }
    return { token: data.token, user: toFrontendUser(data.user) };
  },

  register: async ({ fullName, email, password, phoneNumber, roleId }) => {
    return request('POST', '/api/v1/auth/register', {
      fullName, email, password, phoneNumber, roleId: Number(roleId)
    });
  },

  logout: () => {
    localStorage.removeItem('transitops-token');
    localStorage.removeItem('transitops-user');
  },

  getRoles: async () => {
    const roles = getAll('roles');
    if (roles.length) return roles;
    const defaultRoles = [
      { id: '1', name: 'ADMIN' },
      { id: '2', name: 'DISPATCHER' },
      { id: '3', name: 'SAFETY_OFFICER' },
      { id: '4', name: 'FINANCIAL_ANALYST' }
    ];
    saveAll('roles', defaultRoles);
    return defaultRoles;
  },

  getUsers: async () => {
    const data = await request('GET', '/api/v1/users');
    return data.map(toFrontendUser);
  },

  createUser: async (payload) => {
    return toFrontendUser(await request('POST', '/api/v1/users', toBackendUser(payload)));
  },

  updateUser: async (id, payload) => {
    return toFrontendUser(await request('PUT', `/api/v1/users/${id}`, toBackendUpdateUser(payload)));
  },

  deleteUser: async (id) => {
    return request('DELETE', `/api/v1/users/${id}`);
  },

  getVehicles: async () => {
    const data = await request('GET', '/api/v1/vehicles');
    return data.map(toFrontendVehicle);
  },

  getVehicle: async (id) => {
    return toFrontendVehicle(await request('GET', `/api/v1/vehicles/${id}`));
  },

  createVehicle: async (payload) => {
    return toFrontendVehicle(await request('POST', '/api/v1/vehicles', toBackendVehicle(payload)));
  },

  updateVehicle: async (id, payload) => {
    return toFrontendVehicle(await request('PUT', `/api/v1/vehicles/${id}`, toBackendVehicle(payload)));
  },

  deleteVehicle: async (id) => {
    return request('DELETE', `/api/v1/vehicles/${id}`);
  },

  getDrivers: async () => {
    const cached = getAll('drivers');
    if (cached.length) return cached;
    return [];
  },

  getAvailableDrivers: async () => {
    const data = await request('GET', '/api/drivers/available');
    return data.map(toFrontendDriver);
  },

  createDriver: async (payload) => {
    return toFrontendDriver(await request('POST', '/api/drivers', toBackendDriver(payload)));
  },

  updateDriver: async (id, payload) => {
    const drivers = getAll('drivers');
    const idx = drivers.findIndex(d => d.id === id);
    if (idx !== -1) {
      drivers[idx] = { ...drivers[idx], ...payload };
      saveAll('drivers', drivers);
      return drivers[idx];
    }
    throw new Error('Driver update not supported by backend yet');
  },

  deleteDriver: async (id) => {
    const drivers = getAll('drivers').filter(d => d.id !== id);
    saveAll('drivers', drivers);
  },

  getTrips: async () => {
    const data = await request('GET', '/api/v1/trips');
    return data.map(toFrontendTrip);
  },

  getTrip: async (id) => {
    return toFrontendTrip(await request('GET', `/api/v1/trips/${id}`));
  },

  createTrip: async (payload) => {
    return toFrontendTrip(await request('POST', '/api/v1/trips', toBackendTrip(payload)));
  },

  updateTrip: async (id, payload) => {
    return toFrontendTrip(await request('PUT', `/api/v1/trips/${id}`, toBackendUpdateTrip(payload)));
  },

  deleteTrip: async (id) => {
    return request('DELETE', `/api/v1/trips/${id}`);
  },

  dispatchTrip: async (id) => {
    return toFrontendTrip(await request('POST', `/api/v1/trips/${id}/dispatch`));
  },

  completeTrip: async (id, { finalOdometer }) => {
    return toFrontendTrip(await request('POST', `/api/v1/trips/${id}/complete?endOdometer=${finalOdometer}`));
  },

  cancelTrip: async (id) => {
    return toFrontendTrip(await request('POST', `/api/v1/trips/${id}/cancel`));
  },

  getMaintenance: async () => {
    const data = await request('GET', '/api/maintenance');
    return data.map(toFrontendMaintenance);
  },

  createMaintenance: async (payload) => {
    return toFrontendMaintenance(await request('POST', '/api/maintenance', toBackendMaintenance(payload)));
  },

  closeMaintenance: async (id) => {
    return toFrontendMaintenance(await request('PUT', `/api/maintenance/${id}`, { status: 'COMPLETED' }));
  },

  deleteMaintenance: async (id) => {
    return request('DELETE', `/api/maintenance/${id}`);
  },

  getFuelLogs: async () => {
    const data = await request('GET', '/api/fuel');
    return data.map(toFrontendFuel);
  },

  createFuelLog: async (payload) => {
    return toFrontendFuel(await request('POST', '/api/fuel', toBackendFuel(payload)));
  },

  updateFuelLog: async (id, payload) => {
    return toFrontendFuel(await request('PUT', `/api/fuel/${id}`, toBackendFuel(payload)));
  },

  deleteFuelLog: async (id) => {
    return request('DELETE', `/api/fuel/${id}`);
  },

  getExpenses: async () => {
    const data = await request('GET', '/api/expenses');
    return data.map(toFrontendExpense);
  },

  createExpense: async (payload) => {
    return toFrontendExpense(await request('POST', '/api/expenses', toBackendExpense(payload)));
  },

  updateExpense: async (id, payload) => {
    return toFrontendExpense(await request('PUT', `/api/expenses/${id}`, toBackendExpense(payload)));
  },

  deleteExpense: async (id) => {
    return request('DELETE', `/api/expenses/${id}`);
  },

  getDashboardStats: async () => {
    const vehicles = await API.getVehicles();
    const drivers = await API.getAvailableDrivers();
    const trips = await API.getTrips();

    const activeVehicles = vehicles.filter(v => v.status === 'ON_TRIP').length;
    const availableVehicles = vehicles.filter(v => v.status === 'AVAILABLE').length;
    const inMaintenance = vehicles.filter(v => v.status === 'IN_SHOP').length;
    const activeTrips = trips.filter(t => t.status === 'DISPATCHED').length;
    const pendingTrips = trips.filter(t => t.status === 'DRAFT').length;
    const driversOnDuty = drivers.filter(d => d.status === 'ON_TRIP').length;
    const totalVehicles = vehicles.filter(v => v.status !== 'RETIRED').length;
    const fleetUtilization = totalVehicles ? Math.round((activeVehicles / totalVehicles) * 100) : 0;

    return { activeVehicles, availableVehicles, inMaintenance, activeTrips, pendingTrips, driversOnDuty, fleetUtilization };
  },

  getVehicleCostReport: async () => {
    const vehicles = await API.getVehicles();
    const trips = await API.getTrips();
    const fuel = await API.getFuelLogs();
    const expenses = await API.getExpenses();
    const maintenance = await API.getMaintenance();

    return vehicles.map(v => {
      const vehicleTrips = trips.filter(t => t.vehicleId === v.id && t.status === 'COMPLETED');
      const distance = vehicleTrips.reduce((s, t) => s + (t.plannedDistance || 0), 0);
      const revenue = vehicleTrips.reduce((s, t) => s + (t.revenue || 0), 0);

      const fuelForVehicle = fuel.filter(f => {
        const trip = trips.find(t => t.id === f.tripId);
        return trip && trip.vehicleId === v.id;
      });
      const fuelCost = fuelForVehicle.reduce((s, f) => s + f.cost, 0);
      const fuelLiters = fuelForVehicle.reduce((s, f) => s + f.liters, 0);

      const otherCost = expenses.filter(e => {
        const trip = trips.find(t => t.id === e.tripId);
        return trip && trip.vehicleId === v.id;
      }).reduce((s, e) => s + e.amount, 0);

      const maintCost = maintenance.filter(m => m.vehicleId === v.id).reduce((s, m) => s + (m.cost || 0), 0);

      return {
        ...v,
        fuelCost,
        fuelLiters,
        maintenanceCost: maintCost,
        otherCost,
        totalCost: fuelCost + maintCost + otherCost,
        distance,
        revenue,
        efficiency: fuelLiters ? (distance / fuelLiters).toFixed(2) : 0,
        roi: v.acquisitionCost ? (((revenue) - (fuelCost + maintCost)) / v.acquisitionCost * 100).toFixed(2) : 0
      };
    });
  }
};
