import java.util.InputMismatchException;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class Main {
    private static ExpenseTracker tracker = new ExpenseTracker();
    private static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        // Бесконечный цикл для меню
        while (true) {
            showMenu();
            int choice = getUserChoice();

            switch (choice) {
                case 1:
                    handleAddExpense();
                    break;
                case 2:
                    handleShowAllExpenses();
                    break;
                case 3:
                    handleShowCategoryReport();
                    break;
                case 4:
                    System.out.println("До свидания!");
                    scanner.close();
                    return; // Выход из main -> завершение программы
                default:
                    System.out.println("Неверный выбор. Пожалуйста, введите число от 1 до 4.");
            }
            System.out.println(); // Пустая строка для читаемости
        }
    }

    // Показывает главное меню
    private static void showMenu() {
        System.out.println("=== Учет личных расходов ===");
        System.out.println("1. Добавить расход");
        System.out.println("2. Показать все расходы");
        System.out.println("3. Показать отчет по категориям");
        System.out.println("4. Выход");
        System.out.print("Ваш выбор: ");
    }

    // Получает выбор пользователя с проверкой на число
    private static int getUserChoice() {
        try {
            int choice = scanner.nextInt();
            scanner.nextLine(); // "Съедаем" символ новой строки (Enter)
            return choice;
        } catch (InputMismatchException e) {
            scanner.nextLine(); // Очищаем буфер сканера
            return -1; // Возвращаем невалидный выбор
        }
    }

    // Логика добавления расхода
    private static void handleAddExpense() {
        System.out.println("\n--- Добавление расхода ---");

        // Получение суммы с проверкой
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
                scanner.nextLine(); // Очистка буфера в любом случае
            }
        }

        System.out.print("Введите категорию (например, Еда, Транспорт): ");
        String category = scanner.nextLine();

        System.out.print("Введите краткое описание (например, Обед): ");
        String description = scanner.nextLine();

        // Создаем и добавляем расход
        Expense expense = new Expense(amount, category, description);
        tracker.addExpense(expense);
    }

    // Логика показа всех расходов
    private static void handleShowAllExpenses() {
        System.out.println("\n--- Все расходы ---");
        List<Expense> expenses = tracker.getAllExpenses();
        if (expenses.isEmpty()) {
            System.out.println("Расходов пока нет.");
            return;
        }

        for (Expense expense : expenses) {
            System.out.println(expense); // Используется .toString() из класса Expense
        }
    }

    // Логика показа отчета по категориям
    private static void handleShowCategoryReport() {
        System.out.println("\n--- Отчет по категориям ---");
        Map<String, Double> report = tracker.getCategoryReport();

        if (report.isEmpty()) {
            System.out.println("Расходов пока нет.");
            return;
        }

        double total = 0;
        // Проходим по каждой паре (Категория, Сумма) в Map
        for (Map.Entry<String, Double> entry : report.entrySet()) {
            System.out.printf("Категория: %-15s | Итого: %.2f\n",
                    entry.getKey(), entry.getValue());
            total += entry.getValue();
        }
        System.out.println("------------------------------------");
        System.out.printf("ОБЩАЯ СУММА: %.2f\n", total);
    }
}