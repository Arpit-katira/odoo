# TransitOps — Frontend Documentation

This document describes the frontend built for the **TransitOps Smart Transport Operations Platform** hackathon project. It is a fully static, responsive web UI built with **HTML, CSS, and vanilla JavaScript**, designed to connect to a Spring Boot + PostgreSQL backend.

---

## 1. What Was Built

A complete single-page-application-style frontend with the following modules:

| Page | File | Purpose |
|------|------|---------|
| Login | `index.html` + `js/auth.js` | Email/password login with role-based redirect |
| Register | `register.html` + `js/register.js` | New user registration with role selection |
| Dashboard | `dashboard.html` + `js/dashboard.js` | KPIs, fleet filters, active trips |
| Vehicles | `pages/vehicles.html` + `js/vehicles.js` | Vehicle registry CRUD |
| Drivers | `pages/drivers.html` + `js/drivers.js` | Driver management + license expiry alerts |
| Trips | `pages/trips.html` + `js/trips.js` | Trip lifecycle: Draft → Dispatch → Complete/Cancel |
| Maintenance | `pages/maintenance.html` + `js/maintenance.js` | Maintenance logs with auto vehicle status |
| Fuel & Expenses | `pages/expenses.html` + `js/expenses.js` | Fuel logs and other expenses |
| Reports | `pages/reports.html` + `js/reports.js` | Cost, efficiency, ROI + CSV export |
| Users | `pages/users.html` + `js/users.js` | Admin-only user management |

---

## 2. Tech Stack

- **HTML5** — semantic markup
- **CSS3** — custom design system with CSS variables, Flexbox, Grid
- **Vanilla JavaScript (ES6+)** — no frameworks or build tools
- **localStorage** — mock database for standalone development
- **Google Fonts (Inter)** — typography

No build step is required. The project runs directly in a browser.

---

## 3. How to Run

### Option 1: Open Directly

Open `index.html` in any modern browser.

### Option 2: Use a Local Server (Recommended)

```bash
# Python
python -m http.server 5500 --bind 127.0.0.1

# Node (if installed)
npx serve .
```

Then open:

- Login: `http://127.0.0.1:5500/index.html`
- Register: `http://127.0.0.1:5500/register.html`

---

## 4. Demo Accounts

Stored in `js/api.js` under `DEFAULT_USERS`.

| Email | Password | Role |
|---|---|---|
| admin@transitops.com | admin123 | Admin |
| dispatcher@transitops.com | dispatcher123 | Dispatcher |
| safety@transitops.com | safety123 | Safety Officer |
| finance@transitops.com | finance123 | Financial Analyst |

New accounts can also be created via the **Register** page.

---

## 5. Project Structure

```
frontend/
├── index.html                 # Login page
├── register.html              # Registration page
├── dashboard.html             # Dashboard / home
├── pages/
│   ├── vehicles.html          # Vehicle registry
│   ├── drivers.html           # Driver management
│   ├── trips.html             # Trip management
│   ├── maintenance.html       # Maintenance logs
│   ├── expenses.html          # Fuel & expenses
│   ├── reports.html           # Reports & analytics
│   └── users.html             # Admin user management
├── css/
│   └── style.css              # All styles + design system
├── js/
│   ├── utils.js               # Shared helpers, auth, theme, role access
│   ├── api.js                 # Mock API service (replace with real backend)
│   ├── auth.js                # Login page logic
│   ├── register.js            # Registration page logic
│   ├── dashboard.js           # Dashboard logic
│   ├── vehicles.js            # Vehicle CRUD logic
│   ├── drivers.js             # Driver CRUD logic
│   ├── trips.js               # Trip lifecycle logic
│   ├── maintenance.js         # Maintenance workflow logic
│   ├── expenses.js            # Fuel & expense logic
│   ├── reports.js             # Reports & CSV export logic
│   └── users.js               # Admin user management logic
├── backend-readme.md          # Documentation of the backend repo
└── README.md                  # This file
```

---

## 6. Key Design Decisions

### 6.1 Mock API Layer

All backend interaction goes through `js/api.js`. Every function returns a `Promise`, so switching to real `fetch()` calls is a drop-in replacement.

The mock stores data in `localStorage` under keys like:

- `transitops-users`
- `transitops-roles`
- `transitops-vehicles`
- `transitops-drivers`
- `transitops-trips`
- `transitops-maintenance`
- `transitops-fuel`
- `transitops-expenses`

### 6.2 Business Rules Enforced in Mock

- Vehicle registration number must be unique.
- Retired or In-Shop vehicles cannot be selected for trips.
- Suspended drivers or drivers with expired licenses cannot be assigned.
- Drivers/vehicles already On Trip cannot be double-booked.
- Cargo weight must not exceed vehicle capacity.
- Dispatching sets vehicle + driver to `On Trip`.
- Completing/cancelling restores vehicle + driver to `Available`.
- Creating maintenance sets vehicle to `In Shop`.
- Closing maintenance restores vehicle to `Available` (unless retired).

### 6.3 Shared Utilities (`js/utils.js`)

Common helpers used across pages:

- `$`, `$$` — DOM selectors
- `formatDate()`, `formatCurrency()`, `formatNumber()`
- `uuid()` — generate IDs
- `escapeHtml()` — XSS protection
- `debounce()` — input delay
- `toast()` — notification messages
- `renderBadge()` — status badges
- `filterData()`, `sortData()`
- `parseForm()`, `populateForm()`
- `initTheme()`, `initSidebar()`, `initLogout()`
- `renderUserPill()`
- **Role access:** `getAccess()`, `canEdit()`, `canView()`, `applyRoleAccess()`, `guardPageAccess()`

### 6.4 Role-Based Access Control

The role matrix is defined in `js/utils.js` inside `getAccess()`.

| Module | Admin | Dispatcher | Safety Officer | Financial Analyst |
|--------|:-----:|:----------:|:--------------:|:-----------------:|
| Users | Full | None | None | None |
| Drivers | Full | Full | Full | View |
| Vehicles | Full | Full | View | View |
| Trips | Full | Full | View | View |
| Maintenance | Full | None | Full | View |
| Fuel & Expenses | Full | None | None | Full |
| Reports | Full | None | View | Full |

Implementation:

- Sidebar links have `data-module` attributes and are hidden if the role cannot view.
- Page sections have `data-require-view="module"` and are hidden if access is denied.
- Add buttons and action columns have `data-require-edit="module"` and are hidden for view-only roles.
- `guardPageAccess('module')` redirects unauthorized users to the dashboard.

---

## 7. Page-by-Page Details

### 7.1 Login (`index.html`)

- Form collects email and password.
- Calls `API.login()`.
- Stores user object in `localStorage` as `transitops-user`.
- Redirects to `dashboard.html`.
- Auto-redirects already logged-in users.

### 7.2 Register (`register.html`)

- Form collects full name, email, phone, role, password, confirm password.
- Passwords must match.
- Calls `API.register()`.
- On success, redirects to login.

### 7.3 Dashboard (`dashboard.html`)

- Shows KPI cards: Active Vehicles, Available Vehicles, In Maintenance, Active Trips, Pending Trips, Drivers On Duty, Fleet Utilization.
- Filters by vehicle type, status, and region.
- Lists fleet status and active trips.

### 7.4 Vehicles (`pages/vehicles.html`)

- CRUD for vehicles.
- Fields: registration number, model/type, capacity, odometer, acquisition cost, region, status.
- Search + filters.
- Validation: unique registration number.

### 7.5 Drivers (`pages/drivers.html`)

- CRUD for drivers.
- Fields: name, license number/category, expiry, phone, safety score, status.
- License expiry badges: Valid / Expiring / Expired.
- Filters by status and license expiry.

### 7.6 Trips (`pages/trips.html`)

- Create/edit draft trips.
- Dropdowns show only available vehicles and drivers.
- Live capacity hint while entering cargo weight.
- Dispatch, Complete, Cancel actions.
- Complete modal asks for actual distance, fuel consumed, final odometer.

### 7.7 Maintenance (`pages/maintenance.html`)

- Log maintenance for any vehicle.
- Creates record and sets vehicle status to `In Shop`.
- Close action restores vehicle to `Available`.

### 7.8 Fuel & Expenses (`pages/expenses.html`)

- Two side-by-side tables.
- Fuel logs: vehicle, date, liters, cost, odometer.
- Expenses: vehicle, type, date, amount, description.

### 7.9 Reports (`pages/reports.html`)

- Shows total distance, fuel cost, maintenance cost, other expenses, total cost, average efficiency.
- Per-vehicle table: distance, fuel, maintenance, other, total cost, efficiency, ROI.
- CSV export button.

### 7.10 Users (`pages/users.html`)

- Admin-only page.
- Create/edit/delete users.
- Activate/deactivate users.
- Role dropdown populated from `API.getRoles()`.

---

## 8. Theming

- Light/dark mode toggle on every page.
- Theme preference saved in `localStorage` as `transitops-theme`.
- CSS variables in `:root` switch with `data-theme="dark"`.

---

## 9. Backend Integration

When the Spring Boot backend is ready, replace `js/api.js` with real `fetch()` calls.

Current backend endpoints (from the repo at `Arpit-katira/odoo`):

- Vehicles: `GET/POST/PUT/DELETE /api/v1/vehicles`
- Drivers: `POST /api/drivers`, `GET /api/drivers/available`
- Trips: `GET/POST/PUT/DELETE /api/v1/trips`, `POST /api/v1/trips/{id}/dispatch`, `POST /api/v1/trips/{id}/complete?endOdometer=...`, `POST /api/v1/trips/{id}/cancel`
- Maintenance: `GET/POST /api/maintenance`
- Expenses: `GET/POST /api/expenses`

Note: Fuel logs and user auth controllers are not implemented in the backend yet. See `backend-readme.md` for the full backend analysis.

Enable CORS on the backend before connecting the frontend.

---

## 10. Files to Modify for Backend Hookup

1. `js/api.js` — replace mock functions with `fetch()` calls.
2. `js/utils.js` — update `guardPageAccess()` if JWT tokens are used instead of user objects.
3. `index.html` / `register.html` — update form fields if backend uses different names.

---

## 11. Known Limitations

- The frontend mock uses camelCase field names; backend uses `snake_case` in the database. Mapping may be needed at the API layer.
- Fuel logs in the frontend are linked to `vehicleId`; the backend links them to `tripId`. This will need alignment.
- Expenses in the frontend are linked to `vehicleId`; the backend links them to `tripId`. This will need alignment.
- Reports compute ROI using a placeholder revenue model.

---

## 12. Quick Commands

```bash
# Start frontend server
python -m http.server 5500 --bind 127.0.0.1

# Open in browser
http://127.0.0.1:5500/index.html
```

---

## 13. Authors

Frontend built by you with help from Kimi Code CLI for the TransitOps hackathon project.
