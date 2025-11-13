public class Expense {
    private double amount;
    private String category;
    private String description;
    private String username;

    public Expense(double amount, String category, String description, String username) {
        this.amount = amount;
        this.category = category;
        this.description = description;
        this.username = username;
    }

    // Геттеры
    public double getAmount() {
        return amount;
    }

    public String getCategory() {
        return category;
    }

    public String getDescription() {
        return description;
    }

    public String getUsername() {
        return username;
    }

    // Метод для отображения у USER
    public String getUserDisplay() {
        return String.format("Категория: %-15s | Сумма: %-10.2f | Описание: %s",
                category, amount, description);
    }

    // Метод для отображения у ADMIN
    public String getAdminDisplay() {
        return String.format("[%-10s] | Категория: %-15s | Сумма: %-10.2f | Описание: %s",
                username, category, amount, description);
    }
}