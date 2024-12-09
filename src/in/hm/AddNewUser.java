package in.hm;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.sql.Connection;
import java.sql.PreparedStatement;

public class AddNewUser extends JFrame {
    private JTextField userNameField, userEmailField, userPhoneField, userCountry, userDeposit;
    private JRadioButton maleRadioButton, femaleRadioButton, otherRadioButton;

    public AddNewUser() {
        setTitle("Add New User");
        setSize(500, 400);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        
        String iconPath = "D:\\Spring Projects\\HotelManagement\\src\\Images\\hotel.png"; 
        ImageIcon icon = new ImageIcon(iconPath);
        setIconImage(icon.getImage());

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(new Color(240, 248, 255));
        mainPanel.setBorder(new EmptyBorder(20, 20, 20, 20));

        JPanel formPanel = new JPanel(new GridLayout(6, 2, 10, 10));
        formPanel.setBackground(new Color(255, 255, 255)); 
        formPanel.setBorder(BorderFactory.createTitledBorder("User Details"));

        formPanel.add(createStyledLabel("Name:"));
        userNameField = createStyledTextField();
        formPanel.add(userNameField);

        formPanel.add(createStyledLabel("Email:"));
        userEmailField = createStyledTextField();
        formPanel.add(userEmailField);

        formPanel.add(createStyledLabel("Phone:"));
        userPhoneField = createStyledTextField();
        formPanel.add(userPhoneField);

        formPanel.add(createStyledLabel("Gender:"));
        formPanel.add(createGenderPanel());

        formPanel.add(createStyledLabel("Country:"));
        userCountry = createStyledTextField();
        formPanel.add(userCountry);

        formPanel.add(createStyledLabel("Deposit:"));
        userDeposit = createStyledTextField();
        formPanel.add(userDeposit);

        mainPanel.add(formPanel, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.setBackground(new Color(240, 248, 255));

        JButton saveButton = createStyledButton("Save");
        saveButton.addActionListener(this::saveNewUser);
        buttonPanel.add(saveButton);

        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        add(mainPanel);
    }

    private JPanel createGenderPanel() {
        JPanel genderPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        genderPanel.setBackground(new Color(255, 255, 255));

        maleRadioButton = new JRadioButton("Male");
        femaleRadioButton = new JRadioButton("Female");
        otherRadioButton = new JRadioButton("Other");

        ButtonGroup genderGroup = new ButtonGroup();
        genderGroup.add(maleRadioButton);
        genderGroup.add(femaleRadioButton);
        genderGroup.add(otherRadioButton);

        maleRadioButton.setBackground(new Color(255, 255, 255));
        femaleRadioButton.setBackground(new Color(255, 255, 255));
        otherRadioButton.setBackground(new Color(255, 255, 255));

        genderPanel.add(maleRadioButton);
        genderPanel.add(femaleRadioButton);
        genderPanel.add(otherRadioButton);

        return genderPanel;
    }

    private JLabel createStyledLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Arial", Font.BOLD, 14));
        label.setForeground(new Color(50, 50, 150)); 
        return label;
    }

    private JTextField createStyledTextField() {
        JTextField textField = new JTextField();
        textField.setFont(new Font("Arial", Font.PLAIN, 14));
        textField.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200), 1));
        return textField;
    }

    private JButton createStyledButton(String text) {
        JButton button = new JButton(text);
        button.setFont(new Font("Arial", Font.BOLD, 14));
        button.setBackground(new Color(60, 179, 113)); 
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(new Color(46, 139, 87)); 
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(new Color(60, 179, 113)); 
            }
        });

        return button;
    }

    private void saveNewUser(ActionEvent e) {
        String name = userNameField.getText();
        String email = userEmailField.getText();
        String phone = userPhoneField.getText();
        String gender = maleRadioButton.isSelected() ? "Male" : femaleRadioButton.isSelected() ? "Female" : "Other";
        String country = userCountry.getText();
        String deposit = userDeposit.getText();

        try (Connection conn = databaseCode.getConnection()) {
            String query = "INSERT INTO userdetails (name, email, phone, gender, country, deposit) VALUES (?, ?, ?, ?, ?, ?)";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setString(1, name);
            stmt.setString(2, email);
            stmt.setString(3, phone);
            stmt.setString(4, gender);
            stmt.setString(5, country);
            stmt.setString(6, deposit);

            stmt.executeUpdate();
            JOptionPane.showMessageDialog(this, "User added successfully!");
            dispose();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new AddNewUser().setVisible(true));
    }
}
