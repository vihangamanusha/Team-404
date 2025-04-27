public class UserSession {
    private static UserSession instance;
    private String username;

    private UserSession() {
        // Private constructor to prevent instantiation from other classes
    }

    public static UserSession getInstance() {
        if (instance == null) {
            instance = new UserSession();
        }
        return instance;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
