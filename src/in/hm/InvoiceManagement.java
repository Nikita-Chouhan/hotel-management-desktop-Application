package in.hm;


import javax.swing.*;
import java.awt.*;

public class InvoiceManagement extends JFrame {
    public InvoiceManagement() {
        setTitle("Invoice Management");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        add(new JLabel("Invoice Management Module", SwingConstants.CENTER), BorderLayout.CENTER);
    }
}
