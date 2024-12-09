package in.hm;

import javax.swing.*;
import java.awt.*;

public class BanquetsBooking extends JFrame {
    public BanquetsBooking() {
        setTitle("Banquets Booking");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        add(new JLabel("Banquets Booking Module", SwingConstants.CENTER), BorderLayout.CENTER);
    }
}
