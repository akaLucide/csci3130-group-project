package com.example.csci3130groupproject;

import static org.junit.Assert.*;

import org.junit.Test;

public class RoleRouterTest {

    @Test
    public void roleA_routesToEmployerDashboard() {
        RoleRouter.RouteResult r = RoleRouter.route("Employer");
        assertEquals(RoleRouter.Destination.EMPLOYER_DASHBOARD, r.destination);
        assertNull(r.message);
    }

    @Test
    public void roleB_routesToEmployeeDashboard() {
        RoleRouter.RouteResult r = RoleRouter.route("Employee");
        assertEquals(RoleRouter.Destination.EMPLOYEE_DASHBOARD, r.destination);
        assertNull(r.message);
    }

    @Test
    public void roleRouting_isCaseAndWhitespaceInsensitive() {
        assertEquals(RoleRouter.Destination.EMPLOYER_DASHBOARD, RoleRouter.route("  employer  ").destination);
        assertEquals(RoleRouter.Destination.EMPLOYEE_DASHBOARD, RoleRouter.route("EMPLOYEE").destination);
    }

    @Test
    public void invalidRole_fallsBackToLogin() {
        RoleRouter.RouteResult r = RoleRouter.route("Admin");
        assertEquals(RoleRouter.Destination.LOGIN_FALLBACK, r.destination);
        assertNotNull(r.message);
    }

    @Test
    public void missingRole_fallsBackToLogin() {
        assertEquals(RoleRouter.Destination.LOGIN_FALLBACK, RoleRouter.route(null).destination);
        assertEquals(RoleRouter.Destination.LOGIN_FALLBACK, RoleRouter.route("   ").destination);
    }

    @Test
    public void consistency_sameRoleSameDestination() {
        RoleRouter.Destination d1 = RoleRouter.route("Employer").destination;
        RoleRouter.Destination d2 = RoleRouter.route("Employer").destination;
        assertEquals(d1, d2);
    }
}
