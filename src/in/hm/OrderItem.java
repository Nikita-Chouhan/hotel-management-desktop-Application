package in.hm;

class OrderItem {
    private int foodId;
    private String name;
    private double price;
    private int quantity;

    public OrderItem(String tableNumber, int foodId, String name, double price, int quantity) {
        this.foodId = foodId;
        this.name = name;
        this.price = price;
        this.quantity = quantity;
    }

    public int getFoodId() {
        return foodId;
    } 

    public String getName() {
        return name;
    }

    public double getPrice() {
        return price;
    }

    public int getQuantity() {
        return quantity;
    }
}