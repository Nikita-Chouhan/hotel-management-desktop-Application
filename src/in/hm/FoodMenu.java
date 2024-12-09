package in.hm;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class FoodMenu extends JFrame {

    private DefaultTableModel tableModel;
    private JTable foodTable;
    private List<OrderItem> selectedItems = new ArrayList<>();

    public FoodMenu() {
        setTitle("Food Menu - Order System");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        String iconPath = "D:\\Spring Projects\\HotelManagement\\src\\Images\\food-icon.png";
        ImageIcon icon = new ImageIcon(iconPath);
        setIconImage(icon.getImage());

        setSize(800, 600);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        JPanel tablePanel = new JPanel(new BorderLayout());
        tablePanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        tablePanel.setBackground(new Color(255, 255, 255));  

        String[] columnNames = { "Food ID", "Name", "Category", "Price", "Availability" };
        tableModel = new DefaultTableModel(columnNames, 0);
        foodTable = new JTable(tableModel);

        foodTable.getTableHeader().setBackground(new Color(135, 206, 235));
        foodTable.getTableHeader().setForeground(Color.WHITE);
        foodTable.setBackground(new Color(239, 239, 239));  

        loadFoodData();

        JScrollPane scrollPane = new JScrollPane(foodTable);
        tablePanel.add(scrollPane, BorderLayout.CENTER);

        add(tablePanel, BorderLayout.CENTER);

        JPanel footerPanel = new JPanel(new GridLayout(1, 2, 10, 10));
        footerPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        footerPanel.setBackground(new Color(255, 255, 255));  

        JButton addToOrderButton = new JButton("Add to Order");
        addToOrderButton.setFont(new Font("Arial", Font.BOLD, 14));
        addToOrderButton.setBackground(new Color(60, 179, 113));  
        addToOrderButton.setForeground(Color.WHITE);
        addToOrderButton.setFocusPainted(false);
        addToOrderButton.addActionListener(e -> addToOrder());

        JButton generateBillButton = new JButton("Generate Bill");
        generateBillButton.setFont(new Font("Arial", Font.BOLD, 14));
        generateBillButton.setBackground(new Color(100, 149, 237)); 
        generateBillButton.setForeground(Color.WHITE);
        generateBillButton.setFocusPainted(false);
        generateBillButton.addActionListener(e -> generateBill());

        footerPanel.add(addToOrderButton);
        footerPanel.add(generateBillButton);

        add(footerPanel, BorderLayout.SOUTH);

        setVisible(true);
    }

    private void loadFoodData() {
        try (Connection conn = databaseCode.getConnection(); Statement stmt = conn.createStatement()) {
            String query = "SELECT * FROM food";
            ResultSet rs = stmt.executeQuery(query);

            while (rs.next()) {
                int foodId = rs.getInt("food_id");
                String name = rs.getString("name");
                String category = rs.getString("category");
                double price = rs.getDouble("price");
                boolean availability = rs.getBoolean("availability");

                String availabilityText = availability ? "Available" : "Not Available";

                tableModel.addRow(new Object[] { foodId, name, category, price, availabilityText });
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error fetching food data: " + ex.getMessage());
        }
    }

    private void addToOrder() {
        int selectedRow = foodTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a food item to add to the order.");
            return;
        }

        int foodId = (int) tableModel.getValueAt(selectedRow, 0);
        String name = (String) tableModel.getValueAt(selectedRow, 1);
        double price = (double) tableModel.getValueAt(selectedRow, 3);

        String quantityStr = JOptionPane.showInputDialog(this, "Enter quantity for " + name + ":");
        if (quantityStr != null && !quantityStr.isEmpty()) {
            try {
                int quantity = Integer.parseInt(quantityStr);
                selectedItems.add(new OrderItem(foodId, name, price, quantity));
                JOptionPane.showMessageDialog(this, "Item added to order successfully!");
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Invalid quantity. Please enter a valid number.");
            }
        }
    }

    private void generateBill() {
        if (selectedItems.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No items in the order. Please add items to generate a bill.");
            return;
        }

        double totalAmount = 0.0;
        StringBuilder billDetails = new StringBuilder();

        billDetails.append("<html><body style='font-family:Arial; font-size:12px;'>");
        billDetails.append("<h2 style='text-align:center;'>Hotel Management System</h2>");
        billDetails.append("<h3 style='text-align:center;'>Bill Details</h3>");
        billDetails.append("<table style='width:100%; border:1px solid black;'>");
        billDetails.append("<tr><th style='text-align:left;'>Item</th><th>Quantity</th><th>Price</th><th>Total</th></tr>");

        for (OrderItem item : selectedItems) {
            double itemTotal = item.getPrice() * item.getQuantity();
            totalAmount += itemTotal;
            billDetails.append("<tr>")
                    .append("<td>").append(item.getName()).append("</td>")
                    .append("<td>").append(item.getQuantity()).append("</td>")
                    .append("<td>₹").append(item.getPrice()).append("</td>")
                    .append("<td>₹").append(itemTotal).append("</td>")
                    .append("</tr>");
        }

        double discountRate = 0.10; 
        double discountAmount = totalAmount * discountRate;
        double amountAfterDiscount = totalAmount - discountAmount;

        double gstRate = 0.18; 
        double gstAmount = amountAfterDiscount * gstRate;
        double totalWithGst = amountAfterDiscount + gstAmount;

        billDetails.append("<tr><td colspan='3' style='text-align:right;'><strong>Subtotal:</strong></td>")
                .append("<td>₹").append(totalAmount).append("</td></tr>");
        billDetails.append("<tr><td colspan='3' style='text-align:right;'><strong>Discount (10%):</strong></td>")
                .append("<td>₹").append(discountAmount).append("</td></tr>");
        billDetails.append("<tr><td colspan='3' style='text-align:right;'><strong>Amount after Discount:</strong></td>")
                .append("<td>₹").append(amountAfterDiscount).append("</td></tr>");
        billDetails.append("<tr><td colspan='3' style='text-align:right;'><strong>GST (18%):</strong></td>")
                .append("<td>₹").append(gstAmount).append("</td></tr>");
        billDetails.append("<tr><td colspan='3' style='text-align:right;'><strong>Total Amount:</strong></td>")
                .append("<td>₹").append(totalWithGst).append("</td></tr>");

        billDetails.append("</table>");
        billDetails.append("</body></html>");

        JEditorPane billPane = new JEditorPane("text/html", billDetails.toString());
        billPane.setEditable(false);

        JScrollPane scrollPane = new JScrollPane(billPane);
        scrollPane.setPreferredSize(new Dimension(500, 400));

        JOptionPane.showMessageDialog(this, scrollPane, "Generated Bill", JOptionPane.INFORMATION_MESSAGE);

        saveBillToDatabase(totalWithGst);
    }

    private void saveBillToDatabase(double totalAmount) {
        try (Connection conn = databaseCode.getConnection(); Statement stmt = conn.createStatement()) {

            String insertBillQuery = "INSERT INTO bills (order_id, total_amount, bill_date) " + "VALUES (NULL, "
                    + totalAmount + ", CURRENT_TIMESTAMP)";
            stmt.executeUpdate(insertBillQuery);

            JOptionPane.showMessageDialog(this, "Bill saved to database successfully!");
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error saving bill to database: " + ex.getMessage());
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(FoodMenu::new);
    }
}


