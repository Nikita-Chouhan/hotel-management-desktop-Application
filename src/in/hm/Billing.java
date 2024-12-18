package in.hm;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;

public class Billing extends JFrame {

    public Billing() {

    	 String iconPath = "D:\\Spring Projects\\HotelManagement\\src\\Images\\hsfs_logo.png";
     	ImageIcon icon = new ImageIcon(iconPath);
     	setIconImage(icon.getImage());
     	
        try (Connection conn = databaseCode.getConnection()) {
            if (conn == null) {
                JOptionPane.showMessageDialog(this, "Database connection failed.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            String query = "SELECT * FROM bills";
            PreparedStatement stmt = conn.prepareStatement(query);
            ResultSet rs = stmt.executeQuery();

            String[] columnNames = {"Order ID", "Customer_Name", "Customer_Number", "Total Amount", "Bill Date"};
            DefaultTableModel tableModel = new DefaultTableModel(columnNames, 0);

            SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");

            while (rs.next()) {
                java.sql.Timestamp billDate = rs.getTimestamp("bill_date");

                String formattedDate = (billDate != null) ? dateFormat.format(billDate) : "No Date";

                Object[] row = {
                    rs.getInt("order_id"),
                    rs.getString("customer_name"),
                    rs.getString("customer_number"),
                    String.format("₹%.2f", rs.getDouble("total_amount")),
                    formattedDate
                };
                tableModel.addRow(row);
            }

            JTable table = new JTable(tableModel);
            table.setFont(new Font("Arial", Font.PLAIN, 14));
            table.setRowHeight(30);
            table.setSelectionBackground(new Color(135, 206, 235));

            JTableHeader header = table.getTableHeader();
            header.setBackground(new Color(60, 179, 113));
            header.setForeground(Color.WHITE);
            header.setFont(new Font("Arial", Font.BOLD, 16));

            table.setBackground(new Color(245, 245, 245));
            table.setGridColor(new Color(200, 200, 200));

            table.getColumnModel().getColumn(0).setPreferredWidth(100);
            table.getColumnModel().getColumn(1).setPreferredWidth(150);
            table.getColumnModel().getColumn(2).setPreferredWidth(200);

            table.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);

            JScrollPane scrollPane = new JScrollPane(table);
            scrollPane.setPreferredSize(new Dimension(500, 300));
            scrollPane.getViewport().setBackground(new Color(245, 245, 245));
            scrollPane.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
            this.setTitle("Billing Records");
            this.setLayout(new BorderLayout());
            this.setSize(1000, 400);
            this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            this.setLocationRelativeTo(null);

            JLabel titleLabel = new JLabel("Billing Records", JLabel.CENTER);
            titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
            titleLabel.setForeground(new Color(60, 179, 113));
            titleLabel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
            this.add(titleLabel, BorderLayout.NORTH);

            this.add(scrollPane, BorderLayout.CENTER);

            this.setResizable(true);

            this.setVisible(true);
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(Billing::new);
    }
}
