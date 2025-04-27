package dev.thiagooliveira.poc_autofill_processor.domain.user;

public class User {
    private String username;
    private String password;
    private String role;
    private int limit;
    private int version;

    public User(String username, String password, String role, int limit, int version) {
        this.username = username;
        this.password = password;
        this.role = role;
        this.limit = limit;
        this.version = version;
    }

    public boolean hasLimit() {
        return limit > 0;
    }

    public void decreaseLimit() {
        if("ADMIN".equalsIgnoreCase(role)){
            return;
        }
        this.limit = this.limit -1;
        this.limit = Math.max(this.limit, 0);
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public int getLimit() {
        return limit;
    }

    public void setLimit(int limit) {
        this.limit = limit;
    }

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }
}
