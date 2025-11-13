import java.util.InputMismatchException;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class Main {
    private static ExpenseTracker tracker = new ExpenseTracker();
    private static AuthService authService = new AuthService();
    private static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        // Главный цикл программы: "Цикл Логина/Регистрации"
        while (true) {

            // Показываем стартовый экран (Логин, Регистрация, Выход)
            String action = showLoginRegisterMenu();
            User loggedInUser = null;

            switch (action) {
                case "login":
                    loggedInUser = handleLogin(); // Вызываем логику входа
                    break;
                case "register":
                    handleRegister(); // Вызываем логику регистрации
                    // После регистрации мы не входим автоматом, а возвращаем в гл. меню
                    break;
                case "exit":
                    System.out.println("Выход из программы.");
                    scanner.close();
                    return; // Полный выход из main
            }

            // Если пользователь УСПЕШНО вошел (loggedInUser != null)
            if (loggedInUser != null) {
                // Запускаем "Цикл Приложения" (внутреннее меню)
                runApplicationLoop(loggedInUser);

                // Когда пользователь выберет "Выйти" в runApplicationLoop,
                // тот цикл закончится, и мы вернемся сюда (в начало цикла while(true)),
                // снова показывая меню Логин/Регистрация.
            }
        }
    }


     // Показывает стартовое меню (Логин / Регистрация / Выход).
    private static String showLoginRegisterMenu() {
        while (true) {
            System.out.println("\n=== Главное Меню Учета Расходов ===");
            System.out.println("1. Войти (Login)");
            System.out.println("2. Зарегистрироваться (Register)");
            System.out.println("3. Выйти из программы (Exit)");
            System.out.print("Ваш выбор: ");

            int choice = getUserChoice();

            switch (choice) {
                case 1:
                    return "login";
                case 2:
                    return "register";
                case 3:
                    return "exit";
                default:
                    System.out.println("! Ошибка: Введите 1, 2 или 3.");
            }
        }
    }


     // Обрабатывает ТОЛЬКО логику ВХОДА.
    private static User handleLogin() {
        System.out.println("\n--- Вход в Систему ---");
        System.out.println("Тестовые данные (Админ): admin / admin123");

        System.out.print("Введите имя пользователя (или 'назад'): ");
        String username = scanner.nextLine();

        if (username.equalsIgnoreCase("назад")) {
            return null; // Возврат в главное меню
        }

        System.out.print("Введите пароль: ");
        String password = scanner.nextLine();

        User user = authService.login(username, password);
        if (user != null) {
            System.out.println("Добро пожаловать, " + user.getUsername() + " (Роль: " + user.getRole() + ")");
            return user; // Успешный вход
        } else {
            System.out.println("! Ошибка: Неверное имя пользователя или пароль.");
            return null; // Неуспешный вход, возврат в главное меню
        }
    }

    private static void handleRegister() {
        System.out.println("\n--- Регистрация Нового Пользователя ---");

        String username = "";
        while (username.isEmpty()) {
            System.out.print("Введите новое имя пользователя (или 'назад'): ");
            username = scanner.nextLine();
            if (username.equalsIgnoreCase("назад")) return; // Возврат в гл. меню

            // Простые проверки
            if (username.isEmpty() || username.contains(" ") || username.equalsIgnoreCase("admin")) {
                System.out.println("! Ошибка: Имя не должно быть пустым, содержать пробелы или быть 'admin'.");
                username = ""; // Сброс для повтора цикла
            }
        }

        String password = "";
        while (password.isEmpty()) {
            System.out.print("Введите пароль (минимум 3 символа): ");
            password = scanner.nextLine();
            if (password.length() < 3) {
                System.out.println("! Ошибка: Пароль слишком короткий.");
                password = ""; // Сброс
            }
        }

        // Пытаемся зарегистрировать
        boolean success = authService.registerUser(username, password);

        if (success) {
            System.out.println("✓ Регистрация прошла успешно!");
            System.out.println("Теперь вы можете войти, используя '" + username + "'.");
        } else {
            System.out.println("! Ошибка: Имя пользователя '" + username + "' уже занято.");
        }
        // В любом случае, возвращаемся в главное меню (автоматически)
    }

    private static void runApplicationLoop(User user) {
        boolean running = true;
        while (running) {
            if (user.getRole() == Role.ADMIN) {
                running = showAdminMenu();
            } else {
                running = showUserMenu();
            }
        }
    }

    private static boolean showUserMenu() {
        System.out.println("\n=== Меню Пользователя ("+ authService.getCurrentUser().getUsername() +") ===");
        System.out.println("1. Добавить мой расход");
        System.out.println("2. Показать мои расходы");
        System.out.println("3. Мой отчет по категориям");
        System.out.println("4. Выйти (на экран логина)");
        System.out.print("Ваш выбор: ");

        int choice = getUserChoice();
        switch (choice) {
            case 1:
                handleAddExpense();
                break;
            case 2:
                handleShowUserExpenses();
                break;
            case 3:
                handleShowUserCategoryReport();
                break;
            case 4:
                authService.logout();
                System.out.println("Вы вышли из системы.");
                return false; // Завершаем цикл (возврат к логину)
            default:
                System.out.println("Неверный выбор.");
        }
        return true; // Продолжаем цикл
    }

    private static void handleShowUserExpenses() {
        System.out.println("\n--- Мои расходы ---");
        List<Expense> expenses = tracker.getExpensesForUser(authService.getCurrentUser());
        if (expenses.isEmpty()) {
            System.out.println("Расходов пока нет.");
            return;
        }
        for (Expense expense : expenses) {
            System.out.println(expense.getUserDisplay());
        }
    }

    private static void handleShowUserCategoryReport() {
        System.out.println("\n--- Мой отчет по категориям ---");
        Map<String, Double> report = tracker.getCategoryReportForUser(authService.getCurrentUser());
        if (report.isEmpty()) {
            System.out.println("Расходов пока нет.");
            return;
        }
        double total = 0;
        for (Map.Entry<String, Double> entry : report.entrySet()) {
            System.out.printf("Категория: %-15s | Итого: %.2f\n", entry.getKey(), entry.getValue());
            total += entry.getValue();
        }
        System.out.println("------------------------------------");
        System.out.printf("ОБЩАЯ СУММА: %.2f\n", total);
    }

    private static boolean showAdminMenu() {
        System.out.println("\n=== Меню Администратора ===");
        System.out.println("1. Показать ВСЕ расходы (всех пользователей)");
        System.out.println("2. Общий отчет по категориям (все пользователи)");
        System.out.println("3. Выйти (на экран логина)");
        System.out.print("Ваш выбор: ");

        int choice = getUserChoice();
        switch (choice) {
            case 1:
                handleShowAllAdminExpenses();
                break;
            case 2:
                handleShowAllAdminCategoryReport();
                break;
            case 3:
                authService.logout();
                System.out.println("Вы вышли из системы.");
                return false;
            default:
                System.out.println("Неверный выбор.");
        }
        return true;
    }

    private static void handleShowAllAdminExpenses() {
        System.out.println("\n--- Все расходы (Админ) ---");
        List<Expense> expenses = tracker.getAllExpensesAsAdmin();
        if (expenses.isEmpty()) {
            System.out.println("Расходов в системе нет.");
            return;
        }
        for (Expense expense : expenses) {
            System.out.println(expense.getAdminDisplay());
        }
    }

    private static void handleShowAllAdminCategoryReport() {
        System.out.println("\n--- Общий отчет по категориям (Админ) ---");
        Map<String, Double> report = tracker.getCategoryReportAsAdmin();
        if (report.isEmpty()) {
            System.out.println("Расходов в системе нет.");
            return;
        }
        double total = 0;
        for (Map.Entry<String, Double> entry : report.entrySet()) {
            System.out.printf("Категория: %-15s | Итого: %.2f\n", entry.getKey(), entry.getValue());
            total += entry.getValue();
        }
        System.out.println("------------------------------------");
        System.out.printf("ОБЩАЯ СУММА (ВСЕ ПОЛЬЗОВАТЕЛИ): %.2f\n", total);
    }

    private static void handleAddExpense() {
        User currentUser = authService.getCurrentUser();
        System.out.println("\n--- Добавление расхода (от имени: " + currentUser.getUsername() + ") ---");

        double amount = 0;
        boolean validAmount = false;
        while (!validAmount) {
            System.out.print("Введите сумму (например, 150.50): ");
            try {
                amount = scanner.nextDouble();
                if (amount <= 0) {
                    System.out.println("Сумма должна быть положительной.");
                } else {
                    validAmount = true;
                }
            } catch (InputMismatchException e) {
                System.out.println("Неверный формат. Используйте ',' или '.' в качестве разделителя.");
            } finally {
                scanner.nextLine();
            }
        }

        System.out.print("Введите категорию (например, Еда, Транспорт): ");
        String category = scanner.nextLine();

        System.out.print("Введите краткое описание (например, Обед): ");
        String description = scanner.nextLine();

        Expense expense = new Expense(amount, category, description, currentUser.getUsername());
        tracker.addExpense(expense);
    }

    private static int getUserChoice() {
        try {
            int choice = scanner.nextInt();
            scanner.nextLine();
            return choice;
        } catch (InputMismatchException e) {
            scanner.nextLine();
            return -1;
        }
    }
}