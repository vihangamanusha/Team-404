public class stUserSession {
    private static stUserSession instance;
    private String username;

    private stUserSession() {
        // private constructor to prevent external instantiation
    }

    public static stUserSession getInstance() {
        if (instance == null) {
            instance = new stUserSession();
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
