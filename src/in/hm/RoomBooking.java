package in.hm;

import javax.swing.*;
import java.awt.*;
import java.math.BigDecimal;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.Date;

public class RoomBooking extends JFrame {

	private JComboBox<String> roomNumberComboBox, roomTypeComboBox, acOptionComboBox;
	private JTextField customerNameField, contactField, amountField;
	private JSpinner checkInDateSpinner, checkOutDateSpinner;
	private JComboBox<String> paymentModeComboBox;

	public RoomBooking() {
		setTitle("Room Booking");
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setSize(600, 450);
		setLocationRelativeTo(null);
		setLayout(new BorderLayout(10, 10));

		ImageIcon appIcon = new ImageIcon("D:\\Spring Projects\\HotelManagement\\src\\\\Images\\hsfs_logo.png");
		setIconImage(appIcon.getImage());

		Font headerFont = new Font("Arial", Font.BOLD, 16);
		Font labelFont = new Font("Arial", Font.PLAIN, 14);
		Color buttonColor = new Color(0, 123, 255);
		Color secondaryColor = new Color(240, 240, 240);
		Color formBackgroundColor = new Color(255, 255, 255); // White background for form fields
		Color labelColor = new Color(33, 33, 33); // Dark gray for labels

		JPanel formPanel = new JPanel(new GridLayout(0, 2, 10, 10));
		formPanel.setBackground(formBackgroundColor);
		formPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20)); // Added padding to the form

		customerNameField = new JTextField();
		formPanel.add(new JLabel("Customer Name:"));
		formPanel.add(customerNameField);

		contactField = new JTextField();
		formPanel.add(new JLabel("Contact Number:"));
		formPanel.add(contactField);

		String[] roomNumber = { "Select Room", "room 1", "room 2", "room 3", "room 4", "room 5", "room 6", "room 7",
				"room 8", "room 9", "room 10", "room 11", "room 12" };

		roomNumberComboBox = new JComboBox<>(roomNumber);
		roomNumberComboBox.setSelectedIndex(0); // Set "Select Room" as the default selected item

		formPanel.add(new JLabel("Select Room Type:"));
		formPanel.add(roomNumberComboBox);

		String[] roomTypes = { "Single", "Double", "Suite", "Family" };
		roomTypeComboBox = new JComboBox<>(roomTypes);
		formPanel.add(new JLabel("Select Room Type:"));
		formPanel.add(roomTypeComboBox);

		String[] acOptions = { "AC", "Non-AC" };
		acOptionComboBox = new JComboBox<>(acOptions);
		formPanel.add(new JLabel("Select AC/Non-AC:"));
		formPanel.add(acOptionComboBox);

		SpinnerDateModel checkInDateModel = new SpinnerDateModel();
		checkInDateSpinner = new JSpinner(checkInDateModel);
		JSpinner.DateEditor checkInEditor = new JSpinner.DateEditor(checkInDateSpinner, "yyyy-MM-dd");
		checkInDateSpinner.setEditor(checkInEditor);
		formPanel.add(new JLabel("Check-in Date:"));
		formPanel.add(checkInDateSpinner);

		SpinnerDateModel checkOutDateModel = new SpinnerDateModel();
		checkOutDateSpinner = new JSpinner(checkOutDateModel);
		JSpinner.DateEditor checkOutEditor = new JSpinner.DateEditor(checkOutDateSpinner, "yyyy-MM-dd");
		checkOutDateSpinner.setEditor(checkOutEditor);
		formPanel.add(new JLabel("Check-out Date:"));
		formPanel.add(checkOutDateSpinner);

		String[] paymentModes = { "Cash", "Card", "Online" };
		paymentModeComboBox = new JComboBox<>(paymentModes);
		formPanel.add(new JLabel("Select Payment Mode:"));
		formPanel.add(paymentModeComboBox);

		amountField = new JTextField();
		formPanel.add(new JLabel("Amount Paid:"));
		formPanel.add(amountField);

		JButton submitButton = new JButton("Book Room");
		ImageIcon submitIcon = new ImageIcon("path_to_icon.png");
		submitButton.setIcon(submitIcon);
		submitButton.setFont(headerFont);
		submitButton.setBackground(buttonColor);
		submitButton.setForeground(Color.WHITE);
		submitButton.setFocusPainted(false);
		submitButton.setBorderPainted(false);
		submitButton.setPreferredSize(new Dimension(150, 40));
		submitButton.addActionListener(e -> bookRoom());

		JPanel buttonPanel = new JPanel();
		buttonPanel.setBackground(secondaryColor);
		buttonPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 20, 0));
		buttonPanel.add(submitButton);

		add(formPanel, BorderLayout.CENTER);
		add(buttonPanel, BorderLayout.SOUTH);

		setResizable(true);

		setVisible(true);
	}

	private void bookRoom() {
		String roomNumber = (String) roomNumberComboBox.getSelectedItem();
		String roomType = (String) roomTypeComboBox.getSelectedItem();
		String acOption = (String) acOptionComboBox.getSelectedItem();
		String customerName = customerNameField.getText().trim();
		String contact = contactField.getText().trim();
		Date checkInDate = (Date) checkInDateSpinner.getValue();
		Date checkOutDate = (Date) checkOutDateSpinner.getValue();
		String paymentMode = (String) paymentModeComboBox.getSelectedItem();
		String amountStr = amountField.getText().trim();

		if (customerName.isEmpty()) {
			JOptionPane.showMessageDialog(this, "Customer name cannot be empty.");
			return;
		}

		String room = (String) roomNumberComboBox.getSelectedItem();
		if (room.equals("Select Room")) {
			JOptionPane.showMessageDialog(this, "Please select a valid room.");
			return;
		}

		if (contact.isEmpty() || !contact.matches("\\d{10}")) {
			JOptionPane.showMessageDialog(this, "Please enter a valid 10-digit contact number.");
			return;
		}

		if (checkInDate == null || checkOutDate == null) {
			JOptionPane.showMessageDialog(this, "Please select valid check-in and check-out dates.");
			return;
		}

		if (amountStr.isEmpty() || !amountStr.matches("\\d+(\\.\\d{1,2})?")) {
			JOptionPane.showMessageDialog(this, "Please enter a valid amount.");
			return;
		}

		BigDecimal amount = new BigDecimal(amountStr);

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		String formattedCheckInDate = sdf.format(checkInDate);
		String formattedCheckOutDate = sdf.format(checkOutDate);

		if (!isRoomAvailable(roomNumber, roomType, acOption, formattedCheckInDate, formattedCheckOutDate)) {
			JOptionPane.showMessageDialog(this, "The selected room is not available for the chosen dates.");
			return;
		}

		try (Connection conn = databaseCode.getConnection(); Statement stmt = conn.createStatement()) {
			String insertQuery = "INSERT INTO room_bookings (room_number,room_type, ac_option, customer_name, contact_number, check_in_date, check_out_date, payment_mode, amount_paid) "
					+ "VALUES ('" + roomNumber + "','" + roomType + "', '" + acOption + "', '" + customerName + "', '"
					+ contact + "', '" + formattedCheckInDate + "', '" + formattedCheckOutDate + "', '" + paymentMode
					+ "', " + amount + ")";
			stmt.executeUpdate(insertQuery);

			JOptionPane.showMessageDialog(this, "Room booked successfully!");
		} catch (Exception ex) {
			JOptionPane.showMessageDialog(this, "Error booking room: " + ex.getMessage());
		}
	}

	private boolean isRoomAvailable(String roomNumber, String roomType, String acOption, String checkInDate,
			String checkOutDate) {
		try (Connection conn = databaseCode.getConnection(); Statement stmt = conn.createStatement()) {
			String query = "SELECT * FROM room_bookings WHERE room_number = '" + roomNumber + "' " + "AND room_type = '"
					+ roomType + "' AND ac_option = '" + acOption + "' " + "AND (check_in_date < '" + checkOutDate
					+ "' AND check_out_date > '" + checkInDate + "')";

			ResultSet rs = stmt.executeQuery(query);

			if (rs.next()) {
				JOptionPane.showMessageDialog(this, "The selected room is already booked for the chosen dates.");
				return false;
			}
			return true;
		} catch (Exception ex) {
			JOptionPane.showMessageDialog(this, "Error checking room availability: " + ex.getMessage());
			return false;
		}
	}

	public static void main(String[] args) {
		SwingUtilities.invokeLater(RoomBooking::new);
	}
}
