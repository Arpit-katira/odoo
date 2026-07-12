/**
 * TransitOps — Mock API Service
 *
 * This file simulates a Spring Boot REST backend using localStorage.
 * When your backend team is ready, replace the function bodies with real fetch() calls
 * to endpoints like:
 *   GET    /api/vehicles
 *   POST   /api/vehicles
 *   GET    /api/vehicles/{id}
 *   PUT    /api/vehicles/{id}
 *   DELETE /api/vehicles/{id}
 *
 * All functions return Promises so real fetch integration is a drop-in replacement.
 */

const STORAGE_KEYS = {
  roles: 'transitops-roles',
  users: 'transitops-users',
  vehicles: 'transitops-vehicles',
  drivers: 'transitops-drivers',
  trips: 'transitops-trips',
  maintenance: 'transitops-maintenance',
  fuel: 'transitops-fuel',
  expenses: 'transitops-expenses'
};

const DEFAULT_ROLES = [
  { id: 'r1', name: 'Admin' },
  { id: 'r2', name: 'Dispatcher' },
  { id: 'r3', name: 'Safety Officer' },
  { id: 'r4', name: 'Financial Analyst' }
];

const DEFAULT_USERS = [
  { id: 'u1', name: 'Admin User', email: 'admin@transitops.com', password: 'admin123', role: 'Admin', phoneNumber: '9000000001', isActive: true },
  { id: 'u2', name: 'Dispatcher User', email: 'dispatcher@transitops.com', password: 'dispatcher123', role: 'Dispatcher', phoneNumber: '9000000002', isActive: true },
  { id: 'u3', name: 'Safety Officer', email: 'safety@transitops.com', password: 'safety123', role: 'Safety Officer', phoneNumber: '9000000003', isActive: true },
  { id: 'u4', name: 'Financial Analyst', email: 'finance@transitops.com', password: 'finance123', role: 'Financial Analyst', phoneNumber: '9000000004', isActive: true }
];

const DEFAULT_VEHICLES = [
  { id: 'v1', regNo: 'MH12AB1234', name: 'Tata Ace', type: 'Mini Truck', capacity: 750, odometer: 12400, acquisitionCost: 650000, status: 'Available', region: 'Pune' },
  { id: 'v2', regNo: 'MH14CD5678', name: 'Eicher Pro', type: 'Truck', capacity: 3500, odometer: 45200, acquisitionCost: 1850000, status: 'On Trip', region: 'Mumbai' },
  { id: 'v3', regNo: 'MH02EF9012', name: 'Ashok Leyland', type: 'Trailer', capacity: 5000, odometer: 78900, acquisitionCost: 2200000, status: 'In Shop', region: 'Nashik' },
  { id: 'v4', regNo: 'MH11GH3456', name: 'Mahindra Bolero', type: 'Pickup', capacity: 500, odometer: 5600, acquisitionCost: 950000, status: 'Available', region: 'Pune' }
];

const DEFAULT_DRIVERS = [
  { id: 'd1', name: 'Alex Fernandes', licenseNo: 'MH0120210001234', licenseCategory: 'HMV', licenseExpiry: '2026-08-15', contact: '9876543210', safetyScore: 92, status: 'On Trip' },
  { id: 'd2', name: 'Ravi Sharma', licenseNo: 'MH0120200005678', licenseCategory: 'LMV', licenseExpiry: '2027-02-28', contact: '9123456789', safetyScore: 88, status: 'Available' },
  { id: 'd3', name: 'Priya Nair', licenseNo: 'MH0120190009012', licenseCategory: 'HMV', licenseExpiry: '2025-09-10', contact: '9988776655', safetyScore: 95, status: 'Available' },
  { id: 'd4', name: 'Amit Patel', licenseNo: 'MH0120180003456', licenseCategory: 'LMV', licenseExpiry: '2024-12-01', contact: '8899001122', safetyScore: 72, status: 'Suspended' }
];

const DEFAULT_TRIPS = [
  { id: 't1', source: 'Pune', destination: 'Mumbai', vehicleId: 'v2', driverId: 'd1', cargoWeight: 1200, plannedDistance: 150, actualDistance: 152, fuelConsumed: 18, status: 'Dispatched', createdAt: '2026-07-10' }
];

const DEFAULT_MAINTENANCE = [
  { id: 'm1', vehicleId: 'v3', type: 'Oil Change', description: 'Scheduled oil and filter change', startDate: '2026-07-11', endDate: null, cost: 4500, status: 'Open' }
];

const DEFAULT_FUEL = [
  { id: 'f1', vehicleId: 'v2', liters: 45, cost: 4500, date: '2026-07-10', odometer: 45200 }
];

const DEFAULT_EXPENSES = [
  { id: 'e1', vehicleId: 'v2', type: 'Toll', amount: 850, date: '2026-07-10', description: 'Mumbai-Pune expressway toll' }
];

function seedIfEmpty() {
  if (!loadFromStorage(STORAGE_KEYS.roles)) saveToStorage(STORAGE_KEYS.roles, DEFAULT_ROLES);
  if (!loadFromStorage(STORAGE_KEYS.users)) saveToStorage(STORAGE_KEYS.users, DEFAULT_USERS);
  if (!loadFromStorage(STORAGE_KEYS.vehicles)) saveToStorage(STORAGE_KEYS.vehicles, DEFAULT_VEHICLES);
  if (!loadFromStorage(STORAGE_KEYS.drivers)) saveToStorage(STORAGE_KEYS.drivers, DEFAULT_DRIVERS);
  if (!loadFromStorage(STORAGE_KEYS.trips)) saveToStorage(STORAGE_KEYS.trips, DEFAULT_TRIPS);
  if (!loadFromStorage(STORAGE_KEYS.maintenance)) saveToStorage(STORAGE_KEYS.maintenance, DEFAULT_MAINTENANCE);
  if (!loadFromStorage(STORAGE_KEYS.fuel)) saveToStorage(STORAGE_KEYS.fuel, DEFAULT_FUEL);
  if (!loadFromStorage(STORAGE_KEYS.expenses)) saveToStorage(STORAGE_KEYS.expenses, DEFAULT_EXPENSES);
}

function getAll(key) {
  return loadFromStorage(STORAGE_KEYS[key], []);
}

function getById(key, id) {
  return getAll(key).find(x => x.id === id);
}

function saveAll(key, data) {
  saveToStorage(STORAGE_KEYS[key], data);
}

function create(key, payload) {
  const data = getAll(key);
  const item = { ...payload, id: payload.id || uuid() };
  data.push(item);
  saveAll(key, data);
  return item;
}

function update(key, id, payload) {
  const data = getAll(key);
  const idx = data.findIndex(x => x.id === id);
  if (idx === -1) throw new Error('Not found');
  data[idx] = { ...data[idx], ...payload };
  saveAll(key, data);
  return data[idx];
}

function remove(key, id) {
  const data = getAll(key).filter(x => x.id !== id);
  saveAll(key, data);
}

// ------------------------------------------------------------------
// Auth
// ------------------------------------------------------------------
const API = {
  seed: () => Promise.resolve(seedIfEmpty()),

  getRoles: () => Promise.resolve(getAll('roles')),

  login: async (email, password) => {
    await API.seed();
    const user = getAll('users').find(u => u.email === email && u.password === password && u.isActive !== false);
    if (!user) throw new Error('Invalid email or password');
    const { password: _, ...safe } = user;
    return safe;
  },

  register: async ({ fullName, email, password, phoneNumber, roleId }) => {
    await API.seed();
    const users = getAll('users');
    if (users.some(u => u.email.toLowerCase() === email.toLowerCase())) {
      throw new Error('An account with this email already exists');
    }
    const role = getById('roles', roleId);
    if (!role) throw new Error('Please select a valid role');

    const newUser = {
      id: uuid(),
      name: fullName,
      fullName,
      email,
      password,
      phoneNumber,
      roleId,
      role: role.name,
      isActive: true,
      createdAt: new Date().toISOString()
    };
    create('users', newUser);
    const { password: _, ...safe } = newUser;
    return safe;
  },

  getUsers: () => Promise.resolve(getAll('users').map(u => {
    const { password, ...safe } = u;
    return safe;
  })),

  updateUser: (id, payload) => {
    const users = getAll('users');
    const idx = users.findIndex(u => u.id === id);
    if (idx === -1) throw new Error('User not found');
    if (payload.email && users.some(u => u.id !== id && u.email.toLowerCase() === payload.email.toLowerCase())) {
      throw new Error('Email already exists');
    }
    if (payload.roleId) {
      const role = getById('roles', payload.roleId);
      if (role) payload.role = role.name;
    }
    if (payload.isActive !== undefined) {
      payload.isActive = payload.isActive === true || payload.isActive === 'true';
    }
    users[idx] = { ...users[idx], ...payload };
    saveAll('users', users);
    const { password, ...safe } = users[idx];
    return Promise.resolve(safe);
  },

  deleteUser: (id) => Promise.resolve(remove('users', id)),

  // ------------------------------------------------------------------
  // Vehicles
  // ------------------------------------------------------------------
  getVehicles: () => Promise.resolve(getAll('vehicles')),

  getVehicle: (id) => Promise.resolve(getById('vehicles', id)),

  createVehicle: async (payload) => {
    const vehicles = getAll('vehicles');
    if (vehicles.some(v => v.regNo.toLowerCase() === payload.regNo.toLowerCase())) {
      throw new Error('Registration number already exists');
    }
    return create('vehicles', payload);
  },

  updateVehicle: async (id, payload) => {
    const vehicles = getAll('vehicles');
    const existing = vehicles.find(v => v.id === id);
    if (!existing) throw new Error('Vehicle not found');
    if (payload.regNo && vehicles.some(v => v.id !== id && v.regNo.toLowerCase() === payload.regNo.toLowerCase())) {
      throw new Error('Registration number already exists');
    }
    return update('vehicles', id, payload);
  },

  deleteVehicle: (id) => Promise.resolve(remove('vehicles', id)),

  // ------------------------------------------------------------------
  // Drivers
  // ------------------------------------------------------------------
  getDrivers: () => Promise.resolve(getAll('drivers')),

  getDriver: (id) => Promise.resolve(getById('drivers', id)),

  createDriver: (payload) => Promise.resolve(create('drivers', payload)),

  updateDriver: (id, payload) => Promise.resolve(update('drivers', id, payload)),

  deleteDriver: (id) => Promise.resolve(remove('drivers', id)),

  // ------------------------------------------------------------------
  // Trips
  // ------------------------------------------------------------------
  getTrips: () => Promise.resolve(getAll('trips')),

  getTrip: (id) => Promise.resolve(getById('trips', id)),

  createTrip: async (payload) => {
    const vehicles = getAll('vehicles');
    const drivers = getAll('drivers');
    const vehicle = vehicles.find(v => v.id === payload.vehicleId);
    const driver = drivers.find(d => d.id === payload.driverId);

    if (!vehicle) throw new Error('Vehicle not found');
    if (!driver) throw new Error('Driver not found');
    if (['Retired', 'In Shop'].includes(vehicle.status)) throw new Error('Vehicle is not available for dispatch');
    if (vehicle.status === 'On Trip') throw new Error('Vehicle is already on a trip');
    if (driver.status === 'Suspended') throw new Error('Driver is suspended');
    if (driver.status === 'On Trip') throw new Error('Driver is already on a trip');
    if (new Date(driver.licenseExpiry) < new Date()) throw new Error('Driver license has expired');
    if (payload.cargoWeight > vehicle.capacity) throw new Error('Cargo weight exceeds vehicle capacity');

    return create('trips', { ...payload, status: 'Draft', createdAt: new Date().toISOString().slice(0, 10) });
  },

  updateTrip: (id, payload) => Promise.resolve(update('trips', id, payload)),

  dispatchTrip: async (id) => {
    const trip = getById('trips', id);
    if (!trip) throw new Error('Trip not found');
    if (trip.status !== 'Draft') throw new Error('Only draft trips can be dispatched');

    const vehicles = getAll('vehicles');
    const drivers = getAll('drivers');
    const vehicle = vehicles.find(v => v.id === trip.vehicleId);
    const driver = drivers.find(d => d.id === trip.driverId);
    if (!vehicle || !driver) throw new Error('Trip references missing vehicle or driver');
    if (['Retired', 'In Shop'].includes(vehicle.status)) throw new Error('Vehicle is not available for dispatch');
    if (vehicle.status === 'On Trip') throw new Error('Vehicle is already on a trip');
    if (driver.status === 'Suspended') throw new Error('Driver is suspended');
    if (driver.status === 'On Trip') throw new Error('Driver is already on a trip');
    if (new Date(driver.licenseExpiry) < new Date()) throw new Error('Driver license has expired');
    if (trip.cargoWeight > vehicle.capacity) throw new Error('Cargo weight exceeds vehicle capacity');

    update('trips', id, { status: 'Dispatched' });
    update('vehicles', trip.vehicleId, { status: 'On Trip' });
    update('drivers', trip.driverId, { status: 'On Trip' });
    return getById('trips', id);
  },

  completeTrip: async (id, { actualDistance, fuelConsumed, finalOdometer }) => {
    const trip = getById('trips', id);
    if (!trip || trip.status !== 'Dispatched') throw new Error('Trip not found or not dispatched');

    update('trips', id, { status: 'Completed', actualDistance, fuelConsumed });
    update('vehicles', trip.vehicleId, { status: 'Available', odometer: finalOdometer });
    update('drivers', trip.driverId, { status: 'Available' });
    return getById('trips', id);
  },

  cancelTrip: async (id) => {
    const trip = getById('trips', id);
    if (!trip || trip.status !== 'Dispatched') throw new Error('Trip not found or not dispatched');

    update('trips', id, { status: 'Cancelled' });
    update('vehicles', trip.vehicleId, { status: 'Available' });
    update('drivers', trip.driverId, { status: 'Available' });
    return getById('trips', id);
  },

  deleteTrip: (id) => Promise.resolve(remove('trips', id)),

  // ------------------------------------------------------------------
  // Maintenance
  // ------------------------------------------------------------------
  getMaintenance: () => Promise.resolve(getAll('maintenance')),

  createMaintenance: async (payload) => {
    const item = create('maintenance', { ...payload, status: 'Open' });
    update('vehicles', payload.vehicleId, { status: 'In Shop' });
    return item;
  },

  closeMaintenance: async (id) => {
    const m = getById('maintenance', id);
    if (!m) throw new Error('Maintenance record not found');
    update('maintenance', id, { status: 'Closed', endDate: new Date().toISOString().slice(0, 10) });
    const vehicle = getById('vehicles', m.vehicleId);
    if (vehicle && vehicle.status !== 'Retired') {
      update('vehicles', m.vehicleId, { status: 'Available' });
    }
    return getById('maintenance', id);
  },

  deleteMaintenance: (id) => Promise.resolve(remove('maintenance', id)),

  // ------------------------------------------------------------------
  // Fuel
  // ------------------------------------------------------------------
  getFuelLogs: () => Promise.resolve(getAll('fuel')),
  createFuelLog: (payload) => Promise.resolve(create('fuel', payload)),
  updateFuelLog: (id, payload) => Promise.resolve(update('fuel', id, payload)),
  deleteFuelLog: (id) => Promise.resolve(remove('fuel', id)),

  // ------------------------------------------------------------------
  // Expenses
  // ------------------------------------------------------------------
  getExpenses: () => Promise.resolve(getAll('expenses')),
  createExpense: (payload) => Promise.resolve(create('expenses', payload)),
  updateExpense: (id, payload) => Promise.resolve(update('expenses', id, payload)),
  deleteExpense: (id) => Promise.resolve(remove('expenses', id)),

  // ------------------------------------------------------------------
  // Dashboard / Reports helpers
  // ------------------------------------------------------------------
  getDashboardStats: async () => {
    const vehicles = await API.getVehicles();
    const drivers = await API.getDrivers();
    const trips = await API.getTrips();
    const maintenance = await API.getMaintenance();

    const activeVehicles = vehicles.filter(v => v.status === 'On Trip').length;
    const availableVehicles = vehicles.filter(v => v.status === 'Available').length;
    const inMaintenance = vehicles.filter(v => v.status === 'In Shop').length;
    const activeTrips = trips.filter(t => t.status === 'Dispatched').length;
    const pendingTrips = trips.filter(t => t.status === 'Draft').length;
    const driversOnDuty = drivers.filter(d => d.status === 'On Trip').length;
    const totalVehicles = vehicles.filter(v => v.status !== 'Retired').length;
    const fleetUtilization = totalVehicles ? Math.round((activeVehicles / totalVehicles) * 100) : 0;

    return {
      activeVehicles,
      availableVehicles,
      inMaintenance,
      activeTrips,
      pendingTrips,
      driversOnDuty,
      fleetUtilization
    };
  },

  getVehicleCostReport: async () => {
    const vehicles = await API.getVehicles();
    const fuel = await API.getFuelLogs();
    const expenses = await API.getExpenses();
    const maintenance = await API.getMaintenance();
    const trips = await API.getTrips();

    return vehicles.map(v => {
      const fuelCost = fuel.filter(f => f.vehicleId === v.id).reduce((s, f) => s + f.cost, 0);
      const fuelLiters = fuel.filter(f => f.vehicleId === v.id).reduce((s, f) => s + f.liters, 0);
      const otherCost = expenses.filter(e => e.vehicleId === v.id).reduce((s, e) => s + e.amount, 0);
      const maintCost = maintenance.filter(m => m.vehicleId === v.id).reduce((s, m) => s + (m.cost || 0), 0);
      const distance = trips.filter(t => t.vehicleId === v.id && t.status === 'Completed')
        .reduce((s, t) => s + (t.actualDistance || t.plannedDistance || 0), 0);

      return {
        ...v,
        fuelCost,
        fuelLiters,
        maintenanceCost: maintCost,
        otherCost,
        totalCost: fuelCost + maintCost + otherCost,
        distance,
        efficiency: fuelLiters ? (distance / fuelLiters).toFixed(2) : 0,
        roi: v.acquisitionCost ? (((distance * 25) - (fuelCost + maintCost)) / v.acquisitionCost * 100).toFixed(2) : 0
      };
    });
  }
};
