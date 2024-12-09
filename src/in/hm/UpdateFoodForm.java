
	package in.hm;

	import javax.swing.*;
	import java.awt.*;
	import java.awt.event.ActionEvent;
	import java.awt.event.ActionListener;
	import java.sql.Connection;
	import java.sql.PreparedStatement;

	public class UpdateFoodForm extends JFrame {

	    public UpdateFoodForm() {
	       
	        setTitle("Update Food");
	        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
	        
	        String iconPath = "D:\\Spring Projects\\HotelManagement\\src\\Images\\food-icon.png"; 
	        ImageIcon icon = new ImageIcon(iconPath);
	        setIconImage(icon.getImage());
	        
	        setSize(600, 500);
	        setLocationRelativeTo(null);
	        setLayout(new GridBagLayout());
	        GridBagConstraints gbc = new GridBagConstraints();

	        getContentPane().setBackground(new Color(245, 245, 245));

	        // Input fields
	        JLabel foodIdLabel = createLabel("Food ID:");
	        JTextField foodIdField = createTextField();

	        JLabel nameLabel = createLabel("Name:");
	        JTextField nameField = createTextField();

	        JLabel categoryLabel = createLabel("Category:");
	        JTextField categoryField = createTextField();

	        JLabel priceLabel = createLabel("Price:");
	        JTextField priceField = createTextField();

	        JLabel availabilityLabel = createLabel("Availability:");
	        JCheckBox availabilityCheckBox = new JCheckBox("Available");
	        availabilityCheckBox.setFont(new Font("Arial", Font.PLAIN, 14));

	        // Update button
	        JButton updateButton = createButton("Update");

	        gbc.insets = new Insets(10, 10, 10, 10); // Padding for components
	        gbc.anchor = GridBagConstraints.WEST;
	        gbc.fill = GridBagConstraints.HORIZONTAL;

	        addComponentToGrid(foodIdLabel, gbc, 0, 0);
	        addComponentToGrid(foodIdField, gbc, 1, 0);

	        addComponentToGrid(nameLabel, gbc, 0, 1);
	        addComponentToGrid(nameField, gbc, 1, 1);

	        addComponentToGrid(categoryLabel, gbc, 0, 2);
	        addComponentToGrid(categoryField, gbc, 1, 2);

	        addComponentToGrid(priceLabel, gbc, 0, 3);
	        addComponentToGrid(priceField, gbc, 1, 3);

	        addComponentToGrid(availabilityLabel, gbc, 0, 4);
	        addComponentToGrid(availabilityCheckBox, gbc, 1, 4);

	        gbc.gridwidth = 2;
	        gbc.anchor = GridBagConstraints.CENTER;

	        gbc.gridy = 5;
	        add(updateButton, gbc);

	        updateButton.addActionListener(e -> {
	            try {
	                int foodId = Integer.parseInt(foodIdField.getText());
	                String name = nameField.getText();
	                String category = categoryField.getText();
	                double price = Double.parseDouble(priceField.getText());
	                boolean availability = availabilityCheckBox.isSelected();

	                try (Connection conn = databaseCode.getConnection()) {
	                    String query = "UPDATE food SET name = ?, category = ?, price = ?, availability = ? WHERE food_id = ?";
	                    PreparedStatement stmt = conn.prepareStatement(query);

	                    stmt.setString(1, name);
	                    stmt.setString(2, category);
	                    stmt.setDouble(3, price);
	                    stmt.setBoolean(4, availability);
	                    stmt.setInt(5, foodId);

	                    int rowsAffected = stmt.executeUpdate();

	                    if (rowsAffected > 0) {
	                        JOptionPane.showMessageDialog(this, "Food updated successfully!");
	                    } else {
	                        JOptionPane.showMessageDialog(this, "Food ID not found.");
	                    }
	                } catch (Exception ex) {
	                    JOptionPane.showMessageDialog(this, "Error updating food: " + ex.getMessage());
	                }
	            } catch (Exception ex) {
	                JOptionPane.showMessageDialog(this, "Invalid input: " + ex.getMessage());
	            }
	        });

	        setVisible(true);
	    }

	    // Helper method to create labels with a consistent style
	    private JLabel createLabel(String text) {
	        JLabel label = new JLabel(text);
	        label.setFont(new Font("Arial", Font.PLAIN, 14));
	        label.setForeground(new Color(50, 50, 50)); // Dark gray text
	        return label;
	    }

	    // Helper method to create text fields with a consistent style
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
		SwingUtilities.invokeLater(() -> new UpdateFoodForm()); 
	}

}
