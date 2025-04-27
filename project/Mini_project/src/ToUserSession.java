public class ToUserSession {
    private static ToUserSession instance;
    private String username;

    private ToUserSession() {

    }

    public static ToUserSession getInstance() {
        if (instance == null) {
            instance = new ToUserSession();
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
