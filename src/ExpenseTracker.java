import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ExpenseTracker {
    private List<Expense> expenses;

    public ExpenseTracker() {
        // Инициализируем список при создании трекера
        this.expenses = new ArrayList<>();
    }

    // Метод для добавления нового расхода
    public void addExpense(Expense expense) {
        this.expenses.add(expense);
        System.out.println("Расход успешно добавлен.");
    }

    // Метод для получения списка всех расходов
    public List<Expense> getAllExpenses() {
        return new ArrayList<>(expenses);
    }

    public Map<String, Double> getCategoryReport() {
        Map<String, Double> categoryTotals = new HashMap<>();

        // Проходим по каждому расходу в нашем списке
        for (Expense expense : expenses) {
            String category = expense.getCategory();
            double amount = expense.getAmount();

            double currentTotal = categoryTotals.getOrDefault(category, 0.0);
            categoryTotals.put(category, currentTotal + amount);
        }

        return categoryTotals;
    }
}