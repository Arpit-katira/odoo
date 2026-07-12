package com.odoo.security;

import com.odoo.common.security.Permissions;
import com.odoo.entities.enums.RoleName;

import java.util.List;
import java.util.Map;

public class RolePermissions {

    public static final Map<RoleName, List<String>> ROLE_PERMISSIONS = Map.of(

            RoleName.ADMIN,
            List.of(
                    Permissions.DASHBOARD_VIEW,

                    Permissions.USER_VIEW,
                    Permissions.USER_CREATE,
                    Permissions.USER_UPDATE,
                    Permissions.USER_DELETE,

                    Permissions.VEHICLE_VIEW,
                    Permissions.VEHICLE_CREATE,
                    Permissions.VEHICLE_UPDATE,
                    Permissions.VEHICLE_DELETE,

                    Permissions.DRIVER_VIEW,
                    Permissions.DRIVER_CREATE,
                    Permissions.DRIVER_UPDATE,
                    Permissions.DRIVER_DELETE,

                    Permissions.TRIP_VIEW,
                    Permissions.TRIP_CREATE,
                    Permissions.TRIP_UPDATE,
                    Permissions.TRIP_DELETE,
                    Permissions.TRIP_DISPATCH,
                    Permissions.TRIP_COMPLETE,

                    Permissions.MAINTENANCE_VIEW,
                    Permissions.MAINTENANCE_CREATE,
                    Permissions.MAINTENANCE_UPDATE,
                    Permissions.MAINTENANCE_DELETE,

                    Permissions.FUEL_VIEW,
                    Permissions.FUEL_CREATE,
                    Permissions.FUEL_UPDATE,
                    Permissions.FUEL_DELETE,

                    Permissions.EXPENSE_VIEW,
                    Permissions.EXPENSE_CREATE,
                    Permissions.EXPENSE_UPDATE,
                    Permissions.EXPENSE_DELETE,

                    Permissions.REPORT_VIEW
            ),

            RoleName.DISPATCHER,
            List.of(
                    Permissions.DASHBOARD_VIEW,
                    Permissions.VEHICLE_VIEW,
                    Permissions.DRIVER_VIEW,
                    Permissions.TRIP_VIEW,
                    Permissions.TRIP_CREATE,
                    Permissions.TRIP_UPDATE,
                    Permissions.TRIP_DISPATCH,
                    Permissions.TRIP_COMPLETE
            ),

            RoleName.SAFETY_OFFICER,
            List.of(
                    Permissions.DASHBOARD_VIEW,
                    Permissions.VEHICLE_VIEW,
                    Permissions.DRIVER_VIEW,
                    Permissions.DRIVER_UPDATE,
                    Permissions.MAINTENANCE_VIEW,
                    Permissions.MAINTENANCE_CREATE,
                    Permissions.MAINTENANCE_UPDATE,
                    Permissions.REPORT_VIEW
            ),

            RoleName.FINANCIAL_ANALYST,
            List.of(
                    Permissions.DASHBOARD_VIEW,
                    Permissions.FUEL_VIEW,
                    Permissions.FUEL_CREATE,
                    Permissions.FUEL_UPDATE,
                    Permissions.EXPENSE_VIEW,
                    Permissions.EXPENSE_CREATE,
                    Permissions.EXPENSE_UPDATE,
                    Permissions.REPORT_VIEW
            )
    );

    private RolePermissions() {
    }
}