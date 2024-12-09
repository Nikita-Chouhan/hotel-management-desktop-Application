package in.hm;

import javax.swing.*;
import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;

public class DeleteFoodForm extends JFrame {

	public DeleteFoodForm() {

		setTitle("Delete Food");
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

		String iconPath = "D:\\Spring Projects\\HotelManagement\\src\\Images\\food-icon.png";
		ImageIcon icon = new ImageIcon(iconPath);
		setIconImage(icon.getImage());

		setSize(400, 200);
		setLocationRelativeTo(null);
		setLayout(new GridBagLayout());
		GridBagConstraints gbc = new GridBagConstraints();

		getContentPane().setBackground(new Color(245, 245, 245));

		JLabel foodIdLabel = createLabel("Food ID:");
		JTextField foodIdField = createTextField();

		JButton deleteButton = createButton("Delete");

		gbc.insets = new Insets(10, 10, 10, 10); 
		gbc.anchor = GridBagConstraints.WEST;
		gbc.fill = GridBagConstraints.HORIZONTAL;

		addComponentToGrid(foodIdLabel, gbc, 0, 0);
		addComponentToGrid(foodIdField, gbc, 1, 0);

		gbc.gridwidth = 2;
		gbc.anchor = GridBagConstraints.CENTER;

		gbc.gridy = 1;
		add(deleteButton, gbc);

		deleteButton.addActionListener(e -> {
			try {
				int foodId = Integer.parseInt(foodIdField.getText());

				try (Connection conn = databaseCode.getConnection()) {
					String query = "DELETE FROM food WHERE food_id = ?";
					PreparedStatement stmt = conn.prepareStatement(query);
					stmt.setInt(1, foodId);

					int rowsAffected = stmt.executeUpdate();

					if (rowsAffected > 0) {
						JOptionPane.showMessageDialog(this, "Food deleted successfully!");
					} else {
						JOptionPane.showMessageDialog(this, "Food ID not found.");
					}
				} catch (Exception ex) {
					JOptionPane.showMessageDialog(this, "Error deleting food: " + ex.getMessage());
				}
			} catch (NumberFormatException ex) {
				JOptionPane.showMessageDialog(this, "Invalid Food ID: " + ex.getMessage());
			}
		});

		setVisible(true);
	}

	private JLabel createLabel(String text) {
		JLabel label = new JLabel(text);
		label.setFont(new Font("Arial", Font.PLAIN, 14));
		label.setForeground(new Color(50, 50, 50)); // Dark gray text
		return label;
	}

	private JTextField createTextField() {
		JTextField textField = new JTextField();
		textField.setFont(new Font("Arial", Font.PLAIN, 14));
		textField.setPreferredSize(new Dimension(200, 30));
		return textField;
	}

	private JButton createButton(String text) {
		JButton button = new JButton(text);
		button.setFont(new Font("Arial", Font.BOLD, 14));
		button.setBackground(new Color(255, 69, 0)); 
		button.setForeground(Color.WHITE);
		button.setFocusPainted(false);
		button.setPreferredSize(new Dimension(120, 40));
		button.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

		button.addMouseListener(new java.awt.event.MouseAdapter() {
			public void mouseEntered(java.awt.event.MouseEvent evt) {
				button.setBackground(new Color(255, 99, 71)); 
			}

			public void mouseExited(java.awt.event.MouseEvent evt) {
				button.setBackground(new Color(255, 69, 0));
			}
		});

		return button;
	}

	private void addComponentToGrid(Component component, GridBagConstraints gbc, int gridx, int gridy) {
		gbc.gridx = gridx;
		gbc.gridy = gridy;
		add(component, gbc);
	}

	public static void main(String[] args) {
		SwingUtilities.invokeLater(() -> new DeleteFoodForm());
	}
}
