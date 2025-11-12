// Файл: AuthService.java
import java.util.HashMap;
import java.util.Map;

public class AuthService {

    private Map<String, User> users = new HashMap<>();
    private User currentUser = null;

    public AuthService() {
        // Админ создается при запуске
        users.put("admin", new User("admin", "admin123", Role.ADMIN));
        // Обычный юзер для тестов
        users.put("user", new User("user", "user123", Role.USER));
    }

    public User login(String username, String password) {
        User user = users.get(username);

        if (user != null && user.getPassword().equals(password)) {
            this.currentUser = user;
            return user;
        }

        this.currentUser = null;
        return null;
    }

    /**
     * НОВЫЙ МЕТОД
     * Регистрирует нового пользователя.
     * @param username Имя пользователя.
     * @param password Пароль.
     * @return true, если регистрация прошла успешно, false - если пользователь уже существует.
     */
    public boolean registerUser(String username, String password) {
        // Проверяем, не занято ли имя
        if (users.containsKey(username)) {
            return false; // Пользователь уже существует
        }

        // Важно: новые пользователи регистриются ТОЛЬКО с ролью USER
        User newUser = new User(username, password, Role.USER);
        users.put(username, newUser);
        return true; // Успешная регистрация
    }

    public void logout() {
        this.currentUser = null;
    }

    public User getCurrentUser() {
        return currentUser;
    }
}