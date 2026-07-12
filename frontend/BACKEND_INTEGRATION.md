# Frontend ↔ Backend Integration Guide

This folder contains the **frontend only**. The backend is in `../backend`.

To keep the existing frontend untouched, the backend-ready integration code is provided as **new files** that you can swap in.

---

## Files Added

| File | Purpose |
|------|---------|
| `js/api-backend.js` | Drop-in replacement for `js/api.js`. Uses `fetch()` to call the Spring Boot backend. |
| `BACKEND_INTEGRATION.md` | This guide. |

---

## How to Connect to Backend

### Step 1: Start the backend

```bash
cd ../backend
./mvnw spring-boot:run
```

Backend runs on `http://localhost:8080`.

### Step 2: Enable CORS on backend

Add this class in `../backend/src/main/java/com/odoo/config/WebConfig.java`:

```java
package com.odoo.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/api/**")
                .allowedOrigins("http://127.0.0.1:5500")
                .allowedMethods("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS");
    }
}
```

### Step 3: Swap the API file in every HTML page

In each `.html` file, change:

```html
<script src="js/api.js"></script>
```

to:

```html
<script src="js/api-backend.js"></script>
```

Files to update:

- `index.html`
- `register.html`
- `dashboard.html`
- `pages/vehicles.html`
- `pages/drivers.html`
- `pages/trips.html`
- `pages/maintenance.html`
- `pages/expenses.html`
- `pages/reports.html`
- `pages/users.html`

### Step 4: Run the frontend

```bash
cd frontend
python -m http.server 5500 --bind 127.0.0.1
```

Open `http://127.0.0.1:5500/index.html`.

---

## What `api-backend.js` Does

- Stores JWT token after login in `localStorage` (`transitops-token`).
- Sends `Authorization: Bearer <token>` header on every request.
- Maps backend field names to frontend field names automatically.
- Handles backend's `ApiResponse<T>` wrapper (`json.data`).

### Field mapping examples

| Frontend field | Backend field |
|---|---|
| `regNo` | `registrationNumber` |
| `name` (vehicle) | `model` |
| `type` | `vehicleType` |
| `capacity` | `maxLoadCapacity` |
| `odometer` | `odometerReading` |
| `licenseNo` | `licenseNumber` |
| `contact` (driver) | `phoneNumber` |

---

## Backend Endpoints Used

| Feature | Endpoint |
|---|---|
| Login | `POST /api/v1/auth/login` |
| Register | `POST /api/v1/auth/register` |
| Users CRUD | `/api/v1/users` |
| Vehicles CRUD | `/api/v1/vehicles` |
| Create Driver | `POST /api/drivers` |
| Available Drivers | `GET /api/drivers/available` |
| Trips CRUD | `/api/v1/trips` |
| Dispatch Trip | `POST /api/v1/trips/{id}/dispatch` |
| Complete Trip | `POST /api/v1/trips/{id}/complete?endOdometer=...` |
| Cancel Trip | `POST /api/v1/trips/{id}/cancel` |
| Maintenance | `/api/maintenance` |
| Fuel Logs | `/api/fuel` |
| Expenses | `/api/expenses` |

---

## Known Backend Gaps

These will limit some frontend features until backend is updated:

1. **Driver list/update/delete** — Backend only has `POST /api/drivers` and `GET /api/drivers/available`.
2. **Maintenance close** — Backend only has `GET/POST /api/maintenance`. `closeMaintenance` falls back to `PUT` with status `COMPLETED`.
3. **Roles endpoint** — No `GET /roles`. `api-backend.js` uses hardcoded roles with IDs 1-4.
4. **Fuel/expense are trip-based** — Frontend currently selects a vehicle. `api-backend.js` expects `tripId`. You may need to update the frontend forms later to select a trip instead of a vehicle.

---

## To Switch Back to Mock Mode

Restore the original script tags from `js/api.js` to `js/api-backend.js` in all HTML files.
