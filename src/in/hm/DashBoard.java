package in.hm;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

public class DashBoard extends JFrame {

	public DashBoard() {
		setTitle("Hotel Management System - Dashboard");
		setExtendedState(JFrame.MAXIMIZED_BOTH);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		String iconPath = "D:\\Spring Projects\\HotelManagement\\src\\Images\\hotel.png";
		ImageIcon icon = new ImageIcon(iconPath);

		setIconImage(icon.getImage());
		setExtendedState(JFrame.MAXIMIZED_BOTH);
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		int screenWidth = screenSize.width;
		int screenHeight = screenSize.height;

		String imagePath = "D:\\Spring Projects\\HotelManagement\\src\\Images\\Dashboard.jpg";
		ImageIcon originalIcon = new ImageIcon(imagePath);
		Image scaledImage = originalIcon.getImage().getScaledInstance(screenWidth, screenHeight, Image.SCALE_SMOOTH);
		ImageIcon scaledIcon = new ImageIcon(scaledImage);

		JLabel background = new JLabel(scaledIcon);
		background.setLayout(new BorderLayout());
		add(background);

		JPanel topPanel = new JPanel(new BorderLayout());
		topPanel.setOpaque(false);

		JPanel navbar = new JPanel(new GridBagLayout());
		navbar.setOpaque(false);
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.insets = new Insets(10, 10, 10, 10);
		gbc.anchor = GridBagConstraints.CENTER;
		gbc.fill = GridBagConstraints.HORIZONTAL;

		navbar.add(createFeatureButton("Food Management", e -> openFoodManagement()), gbc);
		navbar.add(createFeatureButton("Food Menu", e -> openFoodMenu()), gbc);
		navbar.add(createFeatureButton("Room Book", e -> roombook()), gbc);
		navbar.add(createFeatureButton("Billing", e -> openBilling()), gbc);
		navbar.add(createFeatureButton("Banquet Details", e -> banquet_Details()), gbc);
		navbar.add(createFeatureButton("Banquets Booking", e -> openBanquetsBooking()), gbc);
		navbar.add(createFeatureButton("Logout", e -> logout()), gbc);

		topPanel.add(navbar, BorderLayout.CENTER);

		String role = Session.getUserRole();
		String username = Session.getUsername();
		JLabel welcomeLabel = new JLabel("Welcome, " + username + "!", SwingConstants.LEFT);
		welcomeLabel.setFont(new Font("Verdana", Font.BOLD, 36));
		welcomeLabel.setForeground(Color.WHITE);
		welcomeLabel.setBorder(BorderFactory.createEmptyBorder(20, 10, 20, 10));
		topPanel.add(welcomeLabel, BorderLayout.SOUTH);

		background.add(topPanel, BorderLayout.NORTH);
	}
	


	private JButton createFeatureButton(String text, ActionListener action) {
		JButton button = new JButton(text);
		button.setFont(new Font("Arial", Font.BOLD, 14));
		button.setBackground(new Color(100, 149, 237));
		button.setForeground(Color.WHITE);
		button.setFocusPainted(false);
		button.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

		button.addMouseListener(new java.awt.event.MouseAdapter() {
			public void mouseEntered(java.awt.event.MouseEvent evt) {
				button.setBackground(new Color(176, 224, 230)); 
			}

			public void mouseExited(java.awt.event.MouseEvent evt) {
				button.setBackground(new Color(100, 149, 237));
			}
		});

		button.addActionListener(action);
		return button;
	}

	private void openFoodManagement() {
		SwingUtilities.invokeLater(() -> {
			JFrame foodManagementFrame = new JFrame("Food Management");
			foodManagementFrame.setSize(700, 550);
			foodManagementFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

			Image icon = Toolkit.getDefaultToolkit()
					.getImage("D:\\Spring Projects\\HotelManagement\\src\\Images\\hsfs_logo.png");
			foodManagementFrame.setIconImage(icon);

			JPanel mainPanel = new JPanel();
			mainPanel.setLayout(new BorderLayout(20, 20));
			mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

			JPanel buttonPanel = new JPanel();
			buttonPanel.setLayout(new GridBagLayout());
			buttonPanel.setOpaque(false);
			GridBagConstraints gbc = new GridBagConstraints();
			gbc.insets = new Insets(10, 10, 10, 10);

			JButton addFoodButton = createProfessionalButton("Add Food", e -> new AddFoodForm().setVisible(true));
			JButton updateFoodButton = createProfessionalButton("Update Food",
					e -> new UpdateFoodForm().setVisible(true));
			JButton deleteFoodButton = createProfessionalButton("Delete Food",
					e -> new DeleteFoodForm().setVisible(true));

			gbc.gridx = 0;
			gbc.gridy = 0;
			buttonPanel.add(addFoodButton, gbc);

			gbc.gridx = 1;
			gbc.gridy = 0;
			buttonPanel.add(updateFoodButton, gbc);

			gbc.gridx = 0;
			gbc.gridy = 1;
			buttonPanel.add(deleteFoodButton, gbc);

			JLabel foodImageLabel = new JLabel();
			String imagePath = "D:\\Spring Projects\\HotelManagement\\src\\Images\\food-image.jpg"; 
																									
			ImageIcon foodImageIcon = new ImageIcon(imagePath);
			Image scaledFoodImage = foodImageIcon.getImage().getScaledInstance(400, 400, Image.SCALE_SMOOTH); 
																												
			foodImageLabel.setIcon(new ImageIcon(scaledFoodImage));

			foodManagementFrame.addComponentListener(new ComponentAdapter() {
				@Override
				public void componentResized(ComponentEvent e) {
					int width = foodManagementFrame.getWidth() / 2;
					int height = (int) (width * 0.75);
					Image newImage = foodImageIcon.getImage().getScaledInstance(width, height, Image.SCALE_SMOOTH);
					foodImageLabel.setIcon(new ImageIcon(newImage));
				}
			});

			mainPanel.add(buttonPanel, BorderLayout.WEST);
			mainPanel.add(foodImageLabel, BorderLayout.CENTER);

			foodManagementFrame.setContentPane(mainPanel);
			foodManagementFrame.setLocationRelativeTo(null);
			foodManagementFrame.setVisible(true);
		});
	}

	private JButton createProfessionalButton(String text, ActionListener action) {
		JButton button = new JButton(text);

		button.setFont(new Font("Segoe UI", Font.BOLD, 16));
		button.setBackground(new Color(70, 130, 180));
		button.setForeground(Color.WHITE);
		button.setFocusPainted(false);
		button.setPreferredSize(new Dimension(150, 60));
		button.setAlignmentX(Component.CENTER_ALIGNMENT);
		button.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(new Color(50, 100, 150)),
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

	private void roombook() {
		SwingUtilities.invokeLater(()-> new RoomBooking().setVisible(true));;
		
	}

	private void openAddNewUserForm() {
		SwingUtilities.invokeLater(() -> new AddNewUser().setVisible(true));
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
		JOptionPane.showMessageDialog(this, "Logged out successfully!");
		new Login().setVisible(true);
		dispose();
	}

	public static void main(String[] args) {
		SwingUtilities.invokeLater(() -> new DashBoard().setVisible(true));
	}
}
