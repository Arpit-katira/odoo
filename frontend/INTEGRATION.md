# Backend Integration Guide (Spring Boot)

This frontend currently uses `js/api.js` as a mock API service backed by `localStorage`.
When the Spring Boot + PostgreSQL backend is ready, replace the function bodies in `js/api.js`
with real HTTP calls to your REST endpoints.

## Base URL

Configure once at the top of `js/api.js`:

```js
const BASE_URL = 'http://localhost:8080/api'; // change to your backend URL
```

## Authentication

The frontend stores the logged-in user object in `localStorage` under `transitops-user`.
For a real backend, you should store a JWT token instead and send it in the
`Authorization: Bearer <token>` header of every request.

### `POST /auth/login`

Request:
```json
{
  "email": "manager@transitops.com",
  "password": "manager123"
}
```

Expected response:
```json
{
  "id": "u1",
  "name": "Fleet Manager",
  "email": "manager@transitops.com",
  "role": "Fleet Manager",
  "token": "<jwt>"
}
```

## Vehicles

| Method | Endpoint              | Description            |
|--------|-----------------------|------------------------|
| GET    | `/vehicles`           | List all vehicles      |
| GET    | `/vehicles/{id}`      | Get one vehicle        |
| POST   | `/vehicles`           | Create vehicle         |
| PUT    | `/vehicles/{id}`      | Update vehicle         |
| DELETE | `/vehicles/{id}`      | Delete vehicle         |

Vehicle JSON shape:
```json
{
  "id": "v1",
  "regNo": "MH12AB1234",
  "name": "Tata Ace",
  "type": "Mini Truck",
  "capacity": 750,
  "odometer": 12400,
  "acquisitionCost": 650000,
  "status": "Available",
  "region": "Pune"
}
```

Status values: `Available`, `On Trip`, `In Shop`, `Retired`.

**Backend must enforce:** registration number uniqueness.

## Drivers

| Method | Endpoint           | Description         |
|--------|--------------------|---------------------|
| GET    | `/drivers`         | List all drivers    |
| GET    | `/drivers/{id}`    | Get one driver      |
| POST   | `/drivers`         | Create driver       |
| PUT    | `/drivers/{id}`    | Update driver       |
| DELETE | `/drivers/{id}`    | Delete driver       |

Driver JSON shape:
```json
{
  "id": "d1",
  "name": "Alex Fernandes",
  "licenseNo": "MH0120210001234",
  "licenseCategory": "HMV",
  "licenseExpiry": "2026-08-15",
  "contact": "9876543210",
  "safetyScore": 92,
  "status": "Available"
}
```

Status values: `Available`, `On Trip`, `Off Duty`, `Suspended`.

## Trips

| Method | Endpoint                        | Description                                  |
|--------|---------------------------------|----------------------------------------------|
| GET    | `/trips`                        | List all trips                               |
| GET    | `/trips/{id}`                   | Get one trip                                 |
| POST   | `/trips`                        | Create draft trip                            |
| PUT    | `/trips/{id}`                   | Update draft trip                            |
| POST   | `/trips/{id}/dispatch`          | Dispatch trip (sets vehicle/driver On Trip)  |
| POST   | `/trips/{id}/complete`          | Complete trip (frees vehicle/driver)         |
| POST   | `/trips/{id}/cancel`            | Cancel dispatched trip                       |
| DELETE | `/trips/{id}`                   | Delete trip                                  |

Trip JSON shape:
```json
{
  "id": "t1",
  "source": "Pune",
  "destination": "Mumbai",
  "vehicleId": "v2",
  "driverId": "d1",
  "cargoWeight": 1200,
  "plannedDistance": 150,
  "actualDistance": 152,
  "fuelConsumed": 18,
  "status": "Dispatched",
  "createdAt": "2026-07-10"
}
```

Status values: `Draft`, `Dispatched`, `Completed`, `Cancelled`.

### Trip creation validations (MUST enforce on backend)

- Vehicle must exist and status must not be `Retired` or `In Shop`.
- Vehicle must not already be `On Trip`.
- Driver must exist, not `Suspended`, and not `On Trip`.
- Driver license expiry date must be in the future.
- `cargoWeight` must be `<= vehicle.capacity`.

## Maintenance

| Method | Endpoint                  | Description                                  |
|--------|---------------------------|----------------------------------------------|
| GET    | `/maintenance`            | List all maintenance records                 |
| POST   | `/maintenance`            | Create maintenance log                       |
| POST   | `/maintenance/{id}/close` | Close maintenance (restore vehicle status)   |
| DELETE | `/maintenance/{id}`       | Delete record                                |

Maintenance JSON shape:
```json
{
  "id": "m1",
  "vehicleId": "v3",
  "type": "Oil Change",
  "description": "Scheduled oil and filter change",
  "startDate": "2026-07-11",
  "endDate": null,
  "cost": 4500,
  "status": "Open"
}
```

Status values: `Open`, `Closed`.

**Backend must enforce:** creating maintenance sets the vehicle status to `In Shop`.
Closing maintenance sets vehicle to `Available` unless the vehicle is `Retired`.

## Fuel Logs

| Method | Endpoint         | Description        |
|--------|------------------|--------------------|
| GET    | `/fuel`          | List all fuel logs |
| POST   | `/fuel`          | Create fuel log    |
| PUT    | `/fuel/{id}`     | Update fuel log    |
| DELETE | `/fuel/{id}`     | Delete fuel log    |

Fuel JSON shape:
```json
{
  "id": "f1",
  "vehicleId": "v2",
  "liters": 45,
  "cost": 4500,
  "date": "2026-07-10",
  "odometer": 45200
}
```

## Expenses

| Method | Endpoint           | Description          |
|--------|--------------------|----------------------|
| GET    | `/expenses`        | List all expenses    |
| POST   | `/expenses`        | Create expense       |
| PUT    | `/expenses/{id}`   | Update expense       |
| DELETE | `/expenses/{id}`   | Delete expense       |

Expense JSON shape:
```json
{
  "id": "e1",
  "vehicleId": "v2",
  "type": "Toll",
  "amount": 850,
  "date": "2026-07-10",
  "description": "Mumbai-Pune expressway toll"
}
```

## Dashboard & Reports

The frontend can compute these client-side, or you can provide dedicated endpoints.

### `GET /dashboard/stats`

Expected response:
```json
{
  "activeVehicles": 1,
  "availableVehicles": 2,
  "inMaintenance": 1,
  "activeTrips": 1,
  "pendingTrips": 0,
  "driversOnDuty": 1,
  "fleetUtilization": 33
}
```

### `GET /reports/vehicle-cost`

Expected response: array of vehicle objects enriched with:
```json
{
  "id": "v2",
  "name": "Eicher Pro",
  "regNo": "MH14CD5678",
  "type": "Truck",
  "acquisitionCost": 1850000,
  "distance": 152,
  "fuelCost": 4500,
  "fuelLiters": 45,
  "maintenanceCost": 0,
  "otherCost": 850,
  "totalCost": 5350,
  "efficiency": "3.38",
  "roi": "1.86"
}
```

Formulas:
- `efficiency = distance / fuelLiters`
- `roi = (revenue - (maintenance + fuelCost)) / acquisitionCost * 100`
  - Use any revenue logic your backend prefers (frontend mock uses `distance * 25`).

## CORS

If the frontend and backend run on different origins, enable CORS on Spring Boot:

```java
@CrossOrigin(origins = "http://localhost:5500")
@RestController
@RequestMapping("/api")
public class ApiController { ... }
```

Or configure globally in `WebMvcConfigurer`.

## Notes

- All `api.js` functions return Promises, so replacing them with `fetch()` is straightforward.
- Handle 401 responses by redirecting to `index.html` and clearing `localStorage`.
- The frontend currently does not enforce role-based UI hiding; you may choose to hide menu items based on `user.role`.
