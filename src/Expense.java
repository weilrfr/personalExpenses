// Файл: Expense.java
public class Expense {
    private double amount;
    private String category;
    private String description;

    public Expense(double amount, String category, String description) {
        this.amount = amount;
        this.category = category;
        this.description = description;
    }

    // Геттеры (нужны для доступа к полям из других классов)
    public double getAmount() {
        return amount;
    }

    public String getCategory() {
        return category;
    }

    public String getDescription() {
        return description;
    }

    @Override
    public String toString() {
        // Удобный вывод для списка всех расходов
        return String.format("Категория: %-15s | Сумма: %-10.2f | Описание: %s",
                category, amount, description);
    }
}