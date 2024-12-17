package in.hm;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;

class BillPanel extends JFrame {

    public BillPanel(String[][] billData, String[] columnNames, double totalAmount) {
        setTitle("Generated Bill");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(600, 400);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());
        
        JTable billTable = new JTable(billData, columnNames);
        JScrollPane scrollPane = new JScrollPane(billTable);
        add(scrollPane, BorderLayout.CENTER);

        JPanel footerPanel = new JPanel(new BorderLayout());
        footerPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JLabel totalLabel = new JLabel("Total Amount: ₹" + totalAmount, JLabel.RIGHT);
        totalLabel.setFont(new Font("Arial", Font.BOLD, 16));
        totalLabel.setForeground(new Color(60, 179, 113));
        footerPanel.add(totalLabel, BorderLayout.CENTER);

        JButton printButton = new JButton("Print");
        printButton.setFont(new Font("Arial", Font.BOLD, 14));
        printButton.setBackground(new Color(100, 149, 237));
        printButton.setForeground(Color.WHITE);
        printButton.setFocusPainted(false);
        printButton.addActionListener(e -> printBill(billData, totalAmount));
        footerPanel.add(printButton, BorderLayout.EAST);

        add(footerPanel, BorderLayout.SOUTH);

        setVisible(true);
    }

    private void printBill(String[][] billData, double totalAmount) {
        JOptionPane.showMessageDialog(this, "Printing the bill... (this can be replaced with real print logic)");
    }
}
