# TransitOps Backend

Spring Boot + PostgreSQL backend for the Smart Transport Operations Platform.

## Tech Stack

- **Framework:** Spring Boot 3.5.16
- **Language:** Java 17
- **Database:** PostgreSQL
- **Build Tool:** Maven
- **ORM:** Spring Data JPA / Hibernate
- **Other:** Lombok, Validation

## Database Configuration

Edit `src/main/resources/application.properties`:

```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/transitops
spring.datasource.username=postgres
spring.datasource.password=${DB_PASSWORD}

spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.format_sql=true
spring.jpa.open-in-view=false
```

Create the database before running:

```sql
CREATE DATABASE transitops;
```

## How to Run

### Option 1: Using Maven Wrapper

```bash
./mvnw spring-boot:run
```

### Option 2: Using System Maven

```bash
mvn spring-boot:run
```

### Option 3: Build and Run JAR

```bash
./mvnw clean package
java -jar target/backend-0.0.1-SNAPSHOT.jar
```

The server starts on port `8080` by default.

Base URL: `http://localhost:8080`

## Project Structure

```
backend/
├── src/main/java/com/odoo/
│   ├── BackendApplication.java
│   ├── common/          # Exceptions, responses, utilities
│   ├── config/          # Configuration classes
│   ├── entities/        # User, FuelLog, enums
│   ├── security/        # Security config (placeholder)
│   └── features/
│       ├── auth/        # Authentication (placeholder)
│       ├── driver/
│       ├── expense/
│       ├── fuel/        # Entity only — no controller yet
│       ├── maintenance/
│       ├── trip/
│       ├── user/        # Entity only — no controller yet
│       └── vehicle/
└── src/main/resources/
    └── application.properties
```

## API Endpoints

All responses are wrapped in `ApiResponse<T>`:

```json
{
  "success": true,
  "message": "...",
  "data": { ... },
  "timestamp": "..."
}
```

### Vehicles

Base: `/api/v1/vehicles`

| Method | Endpoint | Description |
|--------|----------|-------------|
| GET    | `/api/v1/vehicles` | List all vehicles |
| GET    | `/api/v1/vehicles/{id}` | Get vehicle by ID |
| POST   | `/api/v1/vehicles` | Create vehicle |
| PUT    | `/api/v1/vehicles/{id}` | Update vehicle |
| DELETE | `/api/v1/vehicles/{id}` | Delete vehicle |

#### Create Vehicle Request

```json
{
  "registrationNumber": "MH12AB1234",
  "model": "Tata Ace",
  "vehicleType": "MINI_TRUCK",
  "maxLoadCapacity": 750,
  "acquisitionCost": 650000,
  "odometerReading": 12400,
  "region": "Pune"
}
```

#### Vehicle Response

```json
{
  "id": 1,
  "registrationNumber": "MH12AB1234",
  "model": "Tata Ace",
  "vehicleType": "MINI_TRUCK",
  "maxLoadCapacity": 750,
  "acquisitionCost": 650000,
  "odometerReading": 12400,
  "region": "Pune",
  "status": "AVAILABLE"
}
```

Vehicle types: `TRUCK`, `MINI_TRUCK`, `PICKUP`, `TRAILER`, `VAN`  
Vehicle statuses: `AVAILABLE`, `ON_TRIP`, `IN_SHOP`, `RETIRED`

### Drivers

Base: `/api/drivers`

| Method | Endpoint | Description |
|--------|----------|-------------|
| POST   | `/api/drivers` | Register driver |
| GET    | `/api/drivers/available` | List available drivers |

#### Create Driver Request

```json
{
  "userId": 1,
  "licenseNumber": "MH0120210001234",
  "licenseCategory": "HMV",
  "licenseExpiry": "2026-08-15"
}
```

License categories: `LMV`, `HMV`, `HPMV`, `HTV`  
Driver statuses: `AVAILABLE`, `ON_TRIP`, `OFF_DUTY`, `SUSPENDED`

### Trips

Base: `/api/v1/trips`

| Method | Endpoint | Description |
|--------|----------|-------------|
| GET    | `/api/v1/trips` | List all trips |
| GET    | `/api/v1/trips/{id}` | Get trip by ID |
| POST   | `/api/v1/trips` | Create trip |
| PUT    | `/api/v1/trips/{id}` | Update trip |
| DELETE | `/api/v1/trips/{id}` | Delete trip |
| POST   | `/api/v1/trips/{id}/dispatch` | Dispatch trip |
| POST   | `/api/v1/trips/{id}/complete?endOdometer=45352` | Complete trip |
| POST   | `/api/v1/trips/{id}/cancel` | Cancel trip |

#### Create Trip Request

```json
{
  "vehicleId": 1,
  "driverId": 1,
  "source": "Pune",
  "destination": "Mumbai",
  "cargoWeight": 1200,
  "plannedDistance": 150,
  "revenue": 5000
}
```

#### Trip Response

```json
{
  "id": 1,
  "tripNumber": "TRP-0001",
  "vehicleId": 1,
  "vehicleRegistration": "MH12AB1234",
  "driverId": 1,
  "driverName": "Alex Fernandes",
  "source": "Pune",
  "destination": "Mumbai",
  "cargoWeight": 1200,
  "plannedDistance": 150,
  "startOdometer": 45200,
  "endOdometer": null,
  "revenue": 5000,
  "status": "DRAFT",
  "dispatchedAt": null,
  "completedAt": null
}
```

Trip statuses: `DRAFT`, `DISPATCHED`, `COMPLETED`, `CANCELLED`

### Maintenance

Base: `/api/maintenance`

| Method | Endpoint | Description |
|--------|----------|-------------|
| GET    | `/api/maintenance` | List all maintenance records |
| POST   | `/api/maintenance` | Log maintenance |

#### Create Maintenance Request

```json
{
  "vehicleId": 1,
  "issue": "Oil Change",
  "description": "Scheduled oil and filter change",
  "cost": 4500,
  "status": "OPEN"
}
```

Maintenance statuses: `OPEN`, `CLOSED`

### Expenses

Base: `/api/expenses`

| Method | Endpoint | Description |
|--------|----------|-------------|
| GET    | `/api/expenses` | List all expenses |
| POST   | `/api/expenses` | Log expense |

#### Create Expense Request

```json
{
  "tripId": 1,
  "expenseType": "TOLL",
  "amount": 850,
  "description": "Mumbai-Pune expressway toll"
}
```

Expense types: `TOLL`, `PARKING`, `REPAIR`, `INSURANCE`, `OTHER`

### Fuel Logs

Fuel log entity exists but there is **no controller yet**. Entity fields:

- `id`
- `trip` (Many-to-One)
- `liters`
- `cost`
- `odometerReading`
- `loggedAt`

## Roles

Stored in `users` table as `RoleName` enum:

- `ADMIN`
- `DISPATCHER`
- `SAFETY_OFFICER`
- `FINANCIAL_ANALYST`

## Users

The `User` entity exists but there is **no auth controller yet**. Fields:

- `id`
- `fullName`
- `email` (unique)
- `password`
- `role`
- `phoneNumber`
- `isActive`
- `createdAt`
- `updatedAt`

## Known Gaps / TODO

1. **Authentication & Authorization** — No login/register endpoints. No JWT or Spring Security config.
2. **Fuel Logs API** — Entity exists, but no controller/service/repository.
3. **Users API** — Entity exists, but no controller/service/repository.
4. **Driver API** — Only create and `GET /available`. Missing full CRUD and list-all.
5. **Maintenance API** — Missing update, close, delete endpoints.
6. **Expense API** — Missing update and delete endpoints.
7. **CORS** — Add `@CrossOrigin` or global CORS config for frontend integration.
8. **Role-based access** — Controllers do not enforce role permissions yet.
9. **Validation** — Some DTOs missing validation annotations.

## Suggested Frontend Base URL

```js
const BASE_URL = 'http://localhost:8080';
```

Enable CORS in Spring Boot before connecting the frontend:

```java
@Configuration
public class WebConfig implements WebMvcConfigurer {
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/api/**")
                .allowedOrigins("http://127.0.0.1:5500")
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS");
    }
}
```
