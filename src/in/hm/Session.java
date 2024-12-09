package in.hm;

public class Session {
    private static String loggedInUserRole;
    private static String loggedInUsername;

    public static void setUserRole(String role) {
        loggedInUserRole = role;
    }

    public static String getUserRole() {
        return loggedInUserRole;
    }

    public static void setUsername(String username) {
        loggedInUsername = username;
    }

    public static String getUsername() {
        return loggedInUsername;
    }
}
