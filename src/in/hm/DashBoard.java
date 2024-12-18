package in.hm;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

public class DashBoard extends JFrame {

	public DashBoard() {
	    setTitle("Hotel Sanwariya & Family Restaurant");
	    setExtendedState(JFrame.MAXIMIZED_BOTH);
	    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

	    String iconPath = "D:\\Spring Projects\\HotelManagement\\src\\Images\\hsfs_logo.png";
	    ImageIcon icon = new ImageIcon(iconPath);
	    setIconImage(icon.getImage());

	    Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
	    int screenWidth = screenSize.width;
	    int screenHeight = screenSize.height;

	    String imagePath = "D:\\Spring Projects\\HotelManagement\\src\\Images\\sawariyahotel.jpg";
	    ImageIcon originalIcon = new ImageIcon(imagePath);
	    Image scaledImage = originalIcon.getImage().getScaledInstance(screenWidth, screenHeight, Image.SCALE_SMOOTH);
	    ImageIcon scaledIcon = new ImageIcon(scaledImage);

	    JLabel background = new JLabel(scaledIcon);
	    background.setLayout(new BorderLayout());
	    add(background);

	    JPanel topPanel = new JPanel(new BorderLayout());
	    topPanel.setOpaque(false);

	    JLabel label = new JLabel("Hotel Sanwariya & Family Restaurant", SwingConstants.CENTER);
	    label.setFont(new Font("Roboto", Font.BOLD, 24));
	    label.setForeground(Color.BLACK);
	    topPanel.add(label, BorderLayout.NORTH); 

	    JPanel navbar = new JPanel(new GridBagLayout());
	    navbar.setOpaque(false);
	    GridBagConstraints gbc = new GridBagConstraints();
	    gbc.insets = new Insets(10, 10, 10, 10);
	    gbc.anchor = GridBagConstraints.CENTER;
	    gbc.fill = GridBagConstraints.HORIZONTAL;

	    navbar.add(createFeatureButton("Food Menu & Booking", e -> openFoodMenu()), gbc);
	    navbar.add(createFeatureButton("Room Booking", e -> roombook()), gbc);
	    navbar.add(createFeatureButton("Banquets Booking", e -> openBanquetsBooking()), gbc);
	    navbar.add(createFeatureButton("Add New Food", e -> addFood()), gbc);
	    navbar.add(createFeatureButton("Update/Delete Food", e -> update_delete()), gbc);
	    navbar.add(createFeatureButton("Food_Billing", e -> openBilling()), gbc);
	    navbar.add(createFeatureButton("Room Details", e -> room_Details()), gbc);
	    navbar.add(createFeatureButton("Banquet Details", e -> banquet_Details()), gbc);
	    navbar.add(createFeatureButton("Logout", e -> logout()), gbc);

	    topPanel.add(navbar, BorderLayout.CENTER);

	    String role = Session.getUserRole();
	    String username = Session.getUsername();
	    JLabel welcomeLabel = new JLabel("Welcome, " + username + "!", SwingConstants.LEFT);
	    welcomeLabel.setFont(new Font("Roboto", Font.BOLD, 36));
	    welcomeLabel.setForeground(Color.BLACK);
	    welcomeLabel.setBorder(BorderFactory.createEmptyBorder(20, 10, 20, 10));
	    topPanel.add(welcomeLabel, BorderLayout.SOUTH);  

	    background.add(topPanel, BorderLayout.NORTH);
	}


    private JButton createFeatureButton(String text, ActionListener action) {
        JButton button = new JButton(text);
        button.setFont(new Font("Segoe UI", Font.BOLD, 14));
        button.setBackground(new Color(70, 130, 180));  
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder(15, 25, 15, 25));
        button.setPreferredSize(new Dimension(200, 60));
        button.setOpaque(true);
        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        // Hover effect
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

    private void update_delete() {
        SwingUtilities.invokeLater(() -> new FoodMenuWithCRUD().setVisible(true));
    }

    private void roombook() {
        SwingUtilities.invokeLater(() -> new RoomBooking().setVisible(true));
    }

    private void room_Details() {
        SwingUtilities.invokeLater(() -> new RoomManagementCRUD().setVisible(true));
    }

    private void addFood() {
        SwingUtilities.invokeLater(() -> new AddFoodForm().setVisible(true));
    }

    private void openBilling() {
        SwingUtilities.invokeLater(() -> new Billing().setVisible(true));
    }

    private void banquet_Details() {
        SwingUtilities.invokeLater(() -> new BanquetDetails().setVisible(true));
    }

    private void openBanquetsBooking() {
        SwingUtilities.invokeLater(() -> new BanquetBooking().setVisible(true));
    }

    private void openFoodMenu() {
        SwingUtilities.invokeLater(() -> new FoodMenu().setVisible(true));
    }

   
    private void logout() {
        int confirm = JOptionPane.showConfirmDialog(this, 
                "Are you sure you want to log out?", 
                "Confirm Logout", 
                JOptionPane.YES_NO_OPTION, 
                JOptionPane.QUESTION_MESSAGE);
        
        if (confirm == JOptionPane.YES_OPTION) {
            JOptionPane.showMessageDialog(this, "Logged out successfully!");
            new Login().setVisible(true);
            dispose();  
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new DashBoard().setVisible(true));
    }
}
