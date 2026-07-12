package com.odoo.common.security;

public final class Permissions {

    private Permissions() {
    }

    // Dashboard
    public static final String DASHBOARD_VIEW = "dashboard:view";

    // Users
    public static final String USER_VIEW = "user:view";
    public static final String USER_CREATE = "user:create";
    public static final String USER_UPDATE = "user:update";
    public static final String USER_DELETE = "user:delete";

    // Vehicles
    public static final String VEHICLE_VIEW = "vehicle:view";
    public static final String VEHICLE_CREATE = "vehicle:create";
    public static final String VEHICLE_UPDATE = "vehicle:update";
    public static final String VEHICLE_DELETE = "vehicle:delete";

    // Drivers
    public static final String DRIVER_VIEW = "driver:view";
    public static final String DRIVER_CREATE = "driver:create";
    public static final String DRIVER_UPDATE = "driver:update";
    public static final String DRIVER_DELETE = "driver:delete";

    // Trips
    public static final String TRIP_VIEW = "trip:view";
    public static final String TRIP_CREATE = "trip:create";
    public static final String TRIP_UPDATE = "trip:update";
    public static final String TRIP_DELETE = "trip:delete";
    public static final String TRIP_DISPATCH = "trip:dispatch";
    public static final String TRIP_COMPLETE = "trip:complete";

    // Maintenance
    public static final String MAINTENANCE_VIEW = "maintenance:view";
    public static final String MAINTENANCE_CREATE = "maintenance:create";
    public static final String MAINTENANCE_UPDATE = "maintenance:update";
    public static final String MAINTENANCE_DELETE = "maintenance:delete";

    // Fuel
    public static final String FUEL_VIEW = "fuel:view";
    public static final String FUEL_CREATE = "fuel:create";
    public static final String FUEL_UPDATE = "fuel:update";
    public static final String FUEL_DELETE = "fuel:delete";

    // Expenses
    public static final String EXPENSE_VIEW = "expense:view";
    public static final String EXPENSE_CREATE = "expense:create";
    public static final String EXPENSE_UPDATE = "expense:update";
    public static final String EXPENSE_DELETE = "expense:delete";

    // Reports
    public static final String REPORT_VIEW = "report:view";
}