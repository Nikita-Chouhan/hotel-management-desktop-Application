package in.hm;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class Billing extends JFrame {
    private JTextField customerNameField,Order_id, amountField,bill_date;

    public Billing() {
//        setTitle("Billing");
//        setSize(600, 400);
//        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
//        setLocationRelativeTo(null);

        try (Connection conn = databaseCode.getConnection()) {
            if (conn == null) {
                JOptionPane.showMessageDialog(this, "Database connection failed.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            String query = "SELECT order_id, total_amount, bill_date FROM bills";
            PreparedStatement stmt = conn.prepareStatement(query);
            ResultSet rs = stmt.executeQuery();

            String[] columnNames = {"Order ID", "Total Amount", "Bill Date"};
            DefaultTableModel tableModel = new DefaultTableModel(columnNames, 0);

            while (rs.next()) {
                Object[] row = {
                    rs.getInt("order_id"),
                    rs.getDouble("total_amount"),
                    rs.getString("bill_date")
                };
                tableModel.addRow(row);
            }

            JTable table = new JTable(tableModel);
            table.setFont(new Font("Arial", Font.PLAIN, 14));
            table.setRowHeight(25);

            JScrollPane scrollPane = new JScrollPane(table);
            scrollPane.setPreferredSize(new Dimension(500, 300));

            JFrame tableFrame = new JFrame("Billing Records");
            tableFrame.setSize(600, 400);
            tableFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            tableFrame.add(scrollPane, BorderLayout.CENTER);
            tableFrame.setLocationRelativeTo(this);
            tableFrame.setVisible(true);
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    
    }
