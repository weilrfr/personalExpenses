// Файл: ExpenseTracker.java
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
        return new ArrayList<>(expenses); // Возвращаем копию, чтобы нельзя было изменить список извне
    }

    public Map<String, Double> getCategoryReport() {
        Map<String, Double> categoryTotals = new HashMap<>();

        // Проходим по каждому расходу в нашем списке
        for (Expense expense : expenses) {
            String category = expense.getCategory();
            double amount = expense.getAmount();

            // getOrDefault - очень удобный метод:
            // 1. Пытается взять текущее значение по ключу (category)
            // 2. Если ключа нет (т.е. категория встретилась впервые), берет 0.0
            // 3. Прибавляет к этому значению 'amount' и кладет обратно в Map
            double currentTotal = categoryTotals.getOrDefault(category, 0.0);
            categoryTotals.put(category, currentTotal + amount);
        }

        return categoryTotals;
    }
}