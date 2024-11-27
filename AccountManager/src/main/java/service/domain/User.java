package service.domain;

/**
 * Entity object. Represents a user, with proper account and credits
 */
public class User {
    private final String username;

    private final String password;

    private int credits;

    public User(String username, String password, int credits) {
        this.username = username;
        this.password = password;
        this.credits = credits;
    }
    public String getUsername() {
        return username;
    }
    public String getPassword() {
        return password;
    }

    public int getCredits() {
        return credits;
    }

    public void setCredits(int credits) {
        this.credits = credits;
    }
}
