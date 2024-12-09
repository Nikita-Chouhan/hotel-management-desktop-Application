package in.hm;


import javax.swing.*;
import java.awt.*;

public class Menus extends JFrame {
    public Menus() {
        setTitle("Menus");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        add(new JLabel("Menus Module", SwingConstants.CENTER), BorderLayout.CENTER);
    }
}
