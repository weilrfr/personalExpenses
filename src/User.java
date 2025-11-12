// Файл: User.java
public class User {
    private String username;
    private String password; // В реальном приложении здесь должен быть хэш
    private Role role;

    public User(String username, String password, Role role) {
        this.username = username;
        this.password = password;
        this.role = role;
    }

    // Геттеры
    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public Role getRole() {
        return role;
    }
}