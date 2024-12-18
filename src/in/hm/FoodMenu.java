package in.hm;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import java.awt.*;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FoodMenu extends JFrame {

    private DefaultTableModel tableModel;
    private JTable foodTable;
    private JComboBox<String> categoryComboBox;
    private DefaultTableModel orderTableModel;
    private JTable orderTable;
    private List<OrderItem> selectedItems = new ArrayList<>();
    private String customerName;
    private long customerNumber;
    private Map<String, List<OrderItem>> tableOrders = new HashMap<>();
    

    public FoodMenu() {
        setTitle("Food Menu - Order System");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        setSize(1000, 700);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));

        String iconPath = "D:\\Spring Projects\\HotelManagement\\src\\Images\\hsfs_logo.png";
		ImageIcon icon = new ImageIcon(iconPath);
		setIconImage(icon.getImage());

        JPanel categoryPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        categoryPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        JLabel categoryLabel = new JLabel("Select Category: ");
        categoryLabel.setFont(new Font("Arial", Font.BOLD, 14));
        
        categoryComboBox = new JComboBox<>(new String[]{
                "All", "Starter", "Soups", "Tandoori Starter", "Papad", "Salad", "Shake", "Faluda", "Ice Cream",
                "Sweets", "Mocktails", "Pizza", "Sandwich", "Papdi", "Tea/Coffee", "Juice", "Raita", "Chhach",
                "Exotic Paneer", "Taste of Kaju", "Kofta Ka Kamal", "Veg Curry", "Chef Special", "Rice",
                "Tandoori Roti/Tawa Roti", "Dal", "Chinese"
        });
        categoryComboBox.setFont(new Font("Arial", Font.PLAIN, 14));
        categoryComboBox.setBackground(new Color(224, 224, 224));
        categoryComboBox.addActionListener(e -> loadFoodData((String) categoryComboBox.getSelectedItem()));

        categoryPanel.add(categoryLabel);
        categoryPanel.add(categoryComboBox);

        String[] columnNames = {"Food ID", "Name", "Category", "Price", "Availability"};
        tableModel = new DefaultTableModel(columnNames, 0);
        foodTable = new JTable(tableModel) {
            @Override
            public Component prepareRenderer(TableCellRenderer renderer, int row, int column) {
                Component c = super.prepareRenderer(renderer, row, column);
                String availability = (String) getValueAt(row, 4);
                if ("Not Available".equals(availability)) {
                    c.setBackground(Color.PINK);
                } else {
                    c.setBackground(Color.WHITE);
                }
                return c;
            }
        };
        foodTable.setRowHeight(25);
        foodTable.setFont(new Font("Arial", Font.PLAIN, 14));
        foodTable.getTableHeader().setFont(new Font("Arial", Font.BOLD, 14));
        JScrollPane foodScrollPane = new JScrollPane(foodTable);

        String[] orderColumnNames = {"Table Number", "Food ID", "Name", "Price", "Quantity", "Total"};
        orderTableModel = new DefaultTableModel(orderColumnNames, 0);

        orderTable = new JTable(orderTableModel);
        orderTable.setRowHeight(25);
        orderTable.setFont(new Font("Arial", Font.PLAIN, 14));
        orderTable.getTableHeader().setFont(new Font("Arial", Font.BOLD, 14));
        JScrollPane orderScrollPane = new JScrollPane(orderTable);

        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, foodScrollPane, orderScrollPane);
        splitPane.setDividerLocation(600);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        JButton addToOrderButton = new JButton("Add to Order");
        JButton generateBillButton = new JButton("Generate Bill");

        
        styleButton(addToOrderButton, new Color(0, 128, 255), Color.WHITE);
        styleButton(generateBillButton, new Color(34, 139, 34), Color.WHITE);

        addToOrderButton.addActionListener(e -> addToOrder());
        generateBillButton.addActionListener(e -> generateBill());

        buttonPanel.add(addToOrderButton);
        buttonPanel.add(generateBillButton);

        add(categoryPanel, BorderLayout.NORTH);
        add(splitPane, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);

        loadFoodData("All");
        setVisible(true);
    }

    private void styleButton(JButton button, Color background, Color foreground) {
        button.setFont(new Font("Arial", Font.BOLD, 30));
        button.setBackground(background);
        button.setForeground(foreground);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY));
    }

    private void loadFoodData(String category) {
        tableModel.setRowCount(0);
        try (Connection conn = databaseCode.getConnection();
             Statement stmt = conn.createStatement()) {

            String query = "SELECT * FROM food";

            if (!"All".equalsIgnoreCase(category)) {
                query += " WHERE category = '" + category + "'";
            }
            ResultSet rs = stmt.executeQuery(query);

            while (rs.next()) {
                int foodId = rs.getInt("food_id");
                String name = rs.getString("name");
                String foodCategory = rs.getString("category");
                double price = rs.getDouble("price");
                boolean availability = rs.getBoolean("availability");

                String availabilityText = availability ? "Available" : "Not Available";

                tableModel.addRow(new Object[]{foodId, name, foodCategory, price, availabilityText});
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
        String tableNumber = JOptionPane.showInputDialog(this, "Enter Table Number:").trim();
        if (tableNumber == null || tableNumber.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Table number is required.");
            return;
        }
        int foodId = (int) tableModel.getValueAt(selectedRow, 0);
        String name = (String) tableModel.getValueAt(selectedRow, 1);
        double price = (double) tableModel.getValueAt(selectedRow, 3);

        String quantityStr = JOptionPane.showInputDialog(this, "Enter quantity for " + name + ":");
        if (quantityStr != null && !quantityStr.isEmpty()) {
            try {
                int quantity = Integer.parseInt(quantityStr);
                double total = price * quantity;

                OrderItem orderItem = new OrderItem(tableNumber, foodId, name, price, quantity);
                tableOrders.computeIfAbsent(tableNumber, k -> new ArrayList<>()).add(orderItem);

                orderTableModel.addRow(new Object[]{tableNumber, foodId, name, price, quantity, total});
                JOptionPane.showMessageDialog(this, "Item added to order successfully!");
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Invalid quantity. Please enter a valid number.");
            }
        }
    }

    private void generateBill() {
        if (tableOrders.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No items in the order list. Please add items to generate a bill.");
            return;
        }

        String tableNumber = JOptionPane.showInputDialog(this, "Enter Table Number to generate bill:").trim();
        if (tableNumber == null || tableNumber.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Table number is required to generate a bill.");
            return;
        }

        List<OrderItem> tableSpecificItems = tableOrders.get(tableNumber);
        if (tableSpecificItems == null || tableSpecificItems.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No items found for table number: " + tableNumber);
            return;
        }

        // Proceed with bill generation
        JDialog dialog = new JDialog(this, "Customer Details", true);
        dialog.setSize(400, 300);
        dialog.setLayout(new GridBagLayout());
        dialog.setLocationRelativeTo(this);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel nameLabel = new JLabel("Customer Name:");
        nameLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        gbc.gridx = 0;
        gbc.gridy = 0;
        dialog.add(nameLabel, gbc);

        JTextField nameField = new JTextField();
        nameField.setFont(new Font("Arial", Font.PLAIN, 14));
        gbc.gridx = 1;
        gbc.gridy = 0;
        dialog.add(nameField, gbc);

        JLabel numberLabel = new JLabel("Mobile Number:");
        numberLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        gbc.gridx = 0;
        gbc.gridy = 1;
        dialog.add(numberLabel, gbc);

        JTextField numberField = new JTextField();
        numberField.setFont(new Font("Arial", Font.PLAIN, 14));
        gbc.gridx = 1;
        gbc.gridy = 1;
        dialog.add(numberField, gbc);

        JButton submitButton = new JButton("Generate Bill");
        submitButton.setFont(new Font("Arial", Font.BOLD, 14));
        gbc.gridx = 1;
        gbc.gridy = 2;
        dialog.add(submitButton, gbc);

        submitButton.addActionListener(e -> {
            customerName = nameField.getText().trim();
            String customerNumberStr = numberField.getText().trim();

            if (customerName.isEmpty() || customerNumberStr.isEmpty()) {
                JOptionPane.showMessageDialog(dialog, "All fields are required.");
                return;
            }

            if (!customerName.matches("[a-zA-Z ]+")) {
                JOptionPane.showMessageDialog(dialog, "Invalid name. Please enter alphabetic words.");
                return;
            }

            if (!customerNumberStr.matches("\\d{10}")) {
                JOptionPane.showMessageDialog(dialog, "Invalid mobile number. Please enter a 10-digit number.");
                return;
            }

            try {
                customerNumber = Long.parseLong(customerNumberStr);

                dialog.dispose();

                BillGenerate billGenerator = new BillGenerate(tableSpecificItems, customerName, customerNumber, tableNumber);
                billGenerator.generateBill();
                JOptionPane.showMessageDialog(this, "Bill generated successfully for table number: " + tableNumber);
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(dialog, "Invalid mobile number format.");
            }
        });

        dialog.setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(FoodMenu::new);
    }
}
