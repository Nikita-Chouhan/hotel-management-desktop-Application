package in.hm;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;

public class AddFoodForm extends JFrame {

	public AddFoodForm() {
		setTitle("Food Management");
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

		String iconPath = "D:\\Spring Projects\\HotelManagement\\src\\Images\\food-icon.png";
		ImageIcon icon = new ImageIcon(iconPath);
		setIconImage(icon.getImage());

		setSize(600, 450);
		setLocationRelativeTo(null);
		setLayout(new GridBagLayout());
		GridBagConstraints gbc = new GridBagConstraints();

		getContentPane().setBackground(new Color(245, 245, 245));

		JLabel nameLabel = createLabel("Name:");
		JTextField nameField = createTextField();

		JLabel categoryLabel = createLabel("Category:");
		JTextField categoryField = createTextField();

		JLabel priceLabel = createLabel("Price:");
		JTextField priceField = createTextField();

		JLabel availabilityLabel = createLabel("Availability:");
		JCheckBox availabilityCheckBox = new JCheckBox("Available");
		availabilityCheckBox.setFont(new Font("Arial", Font.PLAIN, 14));

		JButton addButton = createButton("Add");

		gbc.insets = new Insets(10, 10, 10, 10); // Padding for components
		gbc.anchor = GridBagConstraints.WEST;
		gbc.fill = GridBagConstraints.HORIZONTAL;

		addComponentToGrid(nameField, gbc, 1, 1);
		addComponentToGrid(categoryLabel, gbc, 0, 2);
		addComponentToGrid(categoryField, gbc, 1, 2);

		addComponentToGrid(priceLabel, gbc, 0, 3);
		addComponentToGrid(priceField, gbc, 1, 3);

		addComponentToGrid(availabilityLabel, gbc, 0, 4);
		addComponentToGrid(availabilityCheckBox, gbc, 1, 4);

		gbc.gridwidth = 2;
		gbc.anchor = GridBagConstraints.CENTER;

		JPanel buttonPanel = new JPanel(new GridLayout(1, 4, 20, 10));
		buttonPanel.setOpaque(false);
		buttonPanel.add(addButton);

		gbc.gridy = 5;
		add(buttonPanel, gbc);

		addButton.addActionListener(e -> {
			try {
				String name = nameField.getText();
				String category = categoryField.getText();
				double price = Double.parseDouble(priceField.getText());
				boolean availability = availabilityCheckBox.isSelected();

				try (Connection conn = databaseCode.getConnection()) {
					String query = "INSERT INTO food (name, category, price, availability) VALUES (?, ?, ?, ?)";
					PreparedStatement stmt = conn.prepareStatement(query);

					stmt.setString(1, name);
					stmt.setString(2, category);
					stmt.setDouble(3, price);
					stmt.setBoolean(4, availability);

					stmt.executeUpdate();

					JOptionPane.showMessageDialog(this, "Food Added Successfully!");
				} catch (Exception ex) {
					JOptionPane.showMessageDialog(this, "Error adding food: " + ex.getMessage());
				}
			} catch (Exception ex) {
				JOptionPane.showMessageDialog(this, "Invalid input: " + ex.getMessage());
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
		button.setBackground(new Color(100, 149, 237));
		button.setForeground(Color.WHITE);
		button.setFocusPainted(false);
		button.setPreferredSize(new Dimension(120, 40));
		button.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

		button.addMouseListener(new java.awt.event.MouseAdapter() {
			public void mouseEntered(java.awt.event.MouseEvent evt) {
				button.setBackground(new Color(176, 224, 230));
			}

			public void mouseExited(java.awt.event.MouseEvent evt) {
				button.setBackground(new Color(100, 149, 237));
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
		SwingUtilities.invokeLater(() -> new AddFoodForm());
	}
}
