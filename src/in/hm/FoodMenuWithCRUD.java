package in.hm;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionListener;
import java.sql.*;

public class FoodMenuWithCRUD extends JFrame {

    public FoodMenuWithCRUD () {
        setTitle("Food Management");
        setSize(1200, 800);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        Image icon = Toolkit.getDefaultToolkit()
                .getImage("D:\\Spring Projects\\HotelManagement\\src\\Images\\hsfs_logo.png");
        setIconImage(icon);

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout(20, 20));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        mainPanel.setBackground(new Color(240, 248, 255));

        String[] columnNames = {"ID", "Name", "Category", "Price", "Availability"};
        DefaultTableModel tableModel = new DefaultTableModel(columnNames, 0);
        JTable foodTable = new JTable(tableModel);
        styleFoodTable(foodTable);

        JScrollPane tableScrollPane = new JScrollPane(foodTable);
        tableScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        tableScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
        fetchFoodData(tableModel);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.X_AXIS));
        buttonPanel.setBackground(new Color(240, 248, 255));
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JButton updateFoodButton = createProfessionalButton("Update Food", e -> {
            int selectedRow = foodTable.getSelectedRow();
            if (selectedRow != -1) {
                int foodId = (int) tableModel.getValueAt(selectedRow, 0);
                new UpdateFoodForm().setVisible(true);
            } else {
                JOptionPane.showMessageDialog(this, "Please select a food item to update.");
            }
        });

		JButton deleteFoodButton = createProfessionalButton("Delete Food", e -> {
            int selectedRow = foodTable.getSelectedRow();
            if (selectedRow != -1) {
                int foodId = (int) tableModel.getValueAt(selectedRow, 0);
                int confirm = JOptionPane.showConfirmDialog(this,
                        "Are you sure you want to delete this food item?", "Confirm Delete",
                        JOptionPane.YES_NO_OPTION);
                if (confirm == JOptionPane.YES_OPTION) {
                    deleteFoodFromDatabase(foodId);
                    fetchFoodData(tableModel); 
                }
            } else {
                JOptionPane.showMessageDialog(this, "Please select a food item to delete.");
            }
        });

        JButton backButton = createProfessionalButton("Back", e -> {
            dispose();
            openMainMenu();
        });

        buttonPanel.add(updateFoodButton);
        buttonPanel.add(Box.createVerticalStrut(50)); 
        buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 20, 10));
        buttonPanel.add(deleteFoodButton);
        buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 20, 10));
        buttonPanel.add(Box.createVerticalStrut(50)); 
        buttonPanel.add(backButton);

      
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);
        mainPanel.add(tableScrollPane, BorderLayout.CENTER);

        setContentPane(mainPanel);
        setLocationRelativeTo(null);
    }

    private void styleFoodTable(JTable foodTable) {
        foodTable.setRowHeight(30);
        foodTable.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        foodTable.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 16));
        foodTable.getTableHeader().setBackground(new Color(70, 130, 180));
        foodTable.getTableHeader().setForeground(Color.WHITE);

        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(SwingConstants.CENTER);
        for (int i = 0; i < foodTable.getColumnCount(); i++) {
            foodTable.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
        }

        foodTable.setSelectionBackground(new Color(100, 149, 237));
        foodTable.setSelectionForeground(Color.WHITE);
        foodTable.setGridColor(new Color(220, 220, 220));
        foodTable.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS); // Enable auto resizing
    }

    private void fetchFoodData(DefaultTableModel tableModel) {
        tableModel.setRowCount(0);
        try (Connection connection = databaseCode.getConnection();
             Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT food_id, name, category, price, availability FROM food")) {

            while (rs.next()) {
                int id = rs.getInt("food_id");
                String name = rs.getString("name");
                String category = rs.getString("category");
                double price = rs.getDouble("price");
                boolean availability = rs.getBoolean("availability");
                tableModel.addRow(new Object[]{id, name, category, price, availability});
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void deleteFoodFromDatabase(int foodId) {
        try (Connection connection = databaseCode.getConnection();
             PreparedStatement pstmt = connection.prepareStatement("DELETE FROM food WHERE food_id = ?")) {

            pstmt.setInt(1, foodId);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private JButton createProfessionalButton(String text, ActionListener action) {
        JButton button = new JButton(text);
        button.setFont(new Font("Segoe UI", Font.BOLD, 16));
        button.setBackground(new Color(70, 130, 180));
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setPreferredSize(new Dimension(150, 50));
        button.setAlignmentX(Component.CENTER_ALIGNMENT);
        button.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(50, 100, 150)),
                BorderFactory.createEmptyBorder(12, 20, 12, 20)));

        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(new Color(100, 149, 237));
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(new Color(70, 130, 180));
            }
        });

        button.addActionListener(action);
        return button;
    }

    private void openMainMenu() {
        System.out.println("Back to Main Menu");
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            // Create and show the window
            FoodMenuWithCRUD app = new FoodMenuWithCRUD();
            app.setVisible(true);
        });
    }
}
