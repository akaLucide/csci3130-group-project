package com.example.csci3130groupproject;

public final class RoleRouter {

    public enum Destination {
        EMPLOYER_DASHBOARD,
        EMPLOYEE_DASHBOARD,
        LOGIN_FALLBACK
    }

    public static class RouteResult {
        public final Destination destination;
        public final String message; // optional error message for fallback

        public RouteResult(Destination destination, String message) {
            this.destination = destination;
            this.message = message;
        }
    }

    private RoleRouter() {}

    public static RouteResult route(String roleRaw) {
        if (roleRaw == null) return new RouteResult(Destination.LOGIN_FALLBACK, "Role missing");

        String role = roleRaw.trim().toLowerCase();
        if (role.isEmpty()) return new RouteResult(Destination.LOGIN_FALLBACK, "Role missing");

        // Adjust these strings to match what your DB returns
        if (role.equals("employer") || role.equals("role a") || role.equals("a")) {
            return new RouteResult(Destination.EMPLOYER_DASHBOARD, null);
        }
        if (role.equals("employee") || role.equals("role b") || role.equals("b")) {
            return new RouteResult(Destination.EMPLOYEE_DASHBOARD, null);
        }
        return new RouteResult(Destination.LOGIN_FALLBACK, "Invalid role: " + roleRaw);
    }
}
