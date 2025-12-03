import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ExpenseTracker {
    // Это ОБЩИЙ список всех расходов ВСЕХ пользователей
    private List<Expense> allExpenses;

    public ExpenseTracker() {
        this.allExpenses = new ArrayList<>();
    }

    // Добавление расхода
    public void addExpense(Expense expense) {
        this.allExpenses.add(expense);
        System.out.println("Расход успешно добавлен.");
    }

    // --- МЕТОДЫ ДЛЯ USER ---

    /**
     * Получить расходы ТОЛЬКО для конкретного пользователя.
     */
    public List<Expense> getExpensesForUser(User user) {
        // Используем Stream API для фильтрации
        return allExpenses.stream()
                .filter(expense -> expense.getUsername().equals(user.getUsername()))
                .collect(Collectors.toList());
    }

    /**
     * Построить отчет ТОЛЬКО для конкретного пользователя.
     */
    public Map<String, Double> getCategoryReportForUser(User user) {
        Map<String, Double> categoryTotals = new HashMap<>();
        List<Expense> userExpenses = getExpensesForUser(user); // Получаем только его расходы

        for (Expense expense : userExpenses) {
            String category = expense.getCategory();
            double amount = expense.getAmount();
            categoryTotals.put(category, categoryTotals.getOrDefault(category, 0.0) + amount);
        }
        return categoryTotals;
    }

    // --- МЕТОДЫ ДЛЯ ADMIN ---

    /**
     * Получить ВСЕ расходы (для админа).
     */
    public List<Expense> getAllExpensesAsAdmin() {
        return new ArrayList<>(allExpenses);
    }

    /**
     * Построить ОБЩИЙ отчет (для админа).
     */
    public Map<String, Double> getCategoryReportAsAdmin() {
        Map<String, Double> categoryTotals = new HashMap<>();

        for (Expense expense : allExpenses) { // Используем allExpenses
            String category = expense.getCategory();
            double amount = expense.getAmount();
            categoryTotals.put(category, categoryTotals.getOrDefault(category, 0.0) + amount);
        }
        return categoryTotals;
    }
}