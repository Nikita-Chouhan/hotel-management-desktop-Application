package in.hm;

import javax.swing.*;
import java.awt.*;
import java.awt.print.PrinterException;
import java.util.List;

public class BillGenerate {

    private List<OrderItem> selectedItems;
    private String customerName;
    private long customerNumber;

    public BillGenerate(List<OrderItem> selectedItems, String customerName, long customerNumber2) {
        this.selectedItems = selectedItems;
        this.customerName = customerName;
        this.customerNumber = customerNumber2;
    }

    public void generateBill() {
        double totalAmount = calculateAndDisplayBill();
        int option = JOptionPane.showOptionDialog(null, "Select an option to proceed:",
                "Save/Print Bill", JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE,
                null, new String[]{"Print Bill", "Cancel"}, "Print Bill");

        if (option == 0) {
            saveBillAsSimplePdf(totalAmount);
        }
    }

    private double calculateAndDisplayBill() {
        double totalAmount = 0.0;

        StringBuilder billDetails = new StringBuilder();
        billDetails.append("<html><body>");
        billDetails.append("<h2>Hotel Management System - Invoice</h2>");
        billDetails.append("<p><strong>Customer Name:</strong> ").append(customerName).append("</p>");
        billDetails.append("<p><strong>Customer Number:</strong> ").append(customerNumber).append("</p>");
        billDetails.append("<table border='1' style='width: 100%; text-align: left;'>");
        billDetails.append("<tr><th>Item</th><th>Quantity</th><th>Price</th><th>Total</th></tr>");

        for (OrderItem item : selectedItems) {
            double itemTotal = item.getPrice() * item.getQuantity();
            totalAmount =totalAmount + itemTotal;
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
        
        double gstRate = 0.5; 
        double gstAmount = amountAfterDiscount * gstRate;
        double totalWithGst = amountAfterDiscount + gstAmount;

        billDetails.append("</table>");
        billDetails.append("<p><strong>Subtotal:</strong> ₹").append(totalAmount).append("</p>");
        billDetails.append("<p><strong>Discount (10%):</strong> ₹").append(discountAmount).append("</p>");
        billDetails.append("<p><strong>Amount after Discount:</strong> ₹").append(amountAfterDiscount).append("</p>");
        billDetails.append("<p><strong>GST (18%):</strong> ₹").append(gstAmount).append("</p>");
        billDetails.append("<p><strong>Total Amount:</strong> ₹").append(totalWithGst).append("</p>");
        billDetails.append("</body></html>");

        JEditorPane billPane = new JEditorPane("text/html", billDetails.toString());
        billPane.setEditable(false);

        JScrollPane scrollPane = new JScrollPane(billPane);
        scrollPane.setPreferredSize(new Dimension(600, 500));
        JOptionPane.showMessageDialog(null, scrollPane, "Generated Bill", JOptionPane.INFORMATION_MESSAGE);

        saveBillToDatabase(totalAmount);

        return totalWithGst;
    }

    private void saveBillAsSimplePdf(double totalAmount) {
        JTextPane textPane = new JTextPane();
        textPane.setContentType("text/html");

        StringBuilder billDetails = new StringBuilder();
        billDetails.append("<html><body>");
        billDetails.append("<h2>Hotel Management System - Invoice</h2>");
        billDetails.append("<p><strong>Customer Name:</strong> ").append(customerName).append("</p>");
        billDetails.append("<p><strong>Customer Number:</strong> ").append(customerNumber).append("</p>");
        billDetails.append("<table border='1' style='width: 100%; text-align: left;'>");
        billDetails.append("<tr><th>Item</th><th>Quantity</th><th>Price</th><th>Total</th></tr>");

        for (OrderItem item : selectedItems) {
            double itemTotal = item.getPrice() * item.getQuantity();
            billDetails.append("<tr>")
                       .append("<td>").append(item.getName()).append("</td>")
                       .append("<td>").append(item.getQuantity()).append("</td>")
                       .append("<td>₹").append(item.getPrice()).append("</td>")
                       .append("<td>₹").append(itemTotal).append("</td>")
                       .append("</tr>");
        }

        billDetails.append("</table>");
        billDetails.append("<p><strong>Total Amount:</strong> ₹").append(totalAmount).append("</p>");
        billDetails.append("</body></html>");

        textPane.setText(billDetails.toString());

        try {
            textPane.print();
            JOptionPane.showMessageDialog(null, "Bill printed as PDF successfully!");
        } catch (PrinterException e) {
            JOptionPane.showMessageDialog(null, "Error printing bill: " + e.getMessage());
        }
    }

    private void saveBillToDatabase(double totalAmount) {
        try (var conn = databaseCode.getConnection(); var stmt = conn.createStatement()) {
            String insertBillQuery = "INSERT INTO bills (order_id, customer_name, customer_number, total_amount, bill_date) " +
                    "VALUES (NULL, '" + customerName + "','" + customerNumber + "', " + totalAmount + ", CURRENT_TIMESTAMP)";
            stmt.executeUpdate(insertBillQuery);
            JOptionPane.showMessageDialog(null, "Bill saved to database successfully!");
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, "Error saving bill to database: " + ex.getMessage());
        }
    }
}
