package se.bastagruppen.todo_appen.security;

public class CustomPrincipal {
    private final Long userId;
    private final String username;

    public CustomPrincipal(Long userId, String username) {
        this.userId = userId;
        this.username = username;
    }

    public Long getUserId() {
        return userId;
    }

    public String getUsername() {
        return username;
    }
}
