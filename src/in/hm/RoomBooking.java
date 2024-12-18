package in.hm;

import javax.swing.*;
import java.awt.*;
import java.awt.print.Printable;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;
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

		ImageIcon appIcon = new ImageIcon("D:\\Spring Projects\\HotelManagement\\src\\Images\\hsfs_logo.png");
		setIconImage(appIcon.getImage());

		Font headerFont = new Font("Arial", Font.BOLD, 16);
		Font labelFont = new Font("Arial", Font.PLAIN, 14);
		Color buttonColor = new Color(0, 123, 255);
		Color secondaryColor = new Color(240, 240, 240);
		Color formBackgroundColor = new Color(255, 255, 255);
		Color labelColor = new Color(33, 33, 33);

		JPanel formPanel = new JPanel(new GridLayout(0, 2, 10, 10));
		formPanel.setBackground(formBackgroundColor);
		formPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

		customerNameField = new JTextField();
		formPanel.add(new JLabel("Customer Name:"));
		formPanel.add(customerNameField);

		contactField = new JTextField();
		formPanel.add(new JLabel("Contact Number:"));
		formPanel.add(contactField);

		String[] roomNumber = { "Select Room", "room 1", "room 2", "room 3", "room 4", "room 5", "room 6", "room 7",
				"room 8", "room 9", "room 10", "room 11", "room 12", "room 13", "room 14", "room 15", "room 16",
				"room 17", "room 18", "room 19", "room 20" };
		roomNumberComboBox = new JComboBox<>(roomNumber);
		roomNumberComboBox.setSelectedIndex(0);

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

		if (roomNumber.equals("Select Room")) {
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

		if (!checkRoomAvailability(roomNumber, roomType, formattedCheckInDate, formattedCheckOutDate)) {
			JOptionPane.showMessageDialog(this, "The selected room is not available for the chosen dates.");
			return;
		}

		try (Connection conn = databaseCode.getConnection(); Statement stmt = conn.createStatement()) {
			String insertQuery = "INSERT INTO room_bookings (room_number, room_type, ac_option, customer_name, contact_number, check_in_date, check_out_date, payment_mode, amount_paid) "
					+ "VALUES ('" + roomNumber + "','" + roomType + "', '" + acOption + "', '" + customerName + "', '"
					+ contact + "', '" + formattedCheckInDate + "', '" + formattedCheckOutDate + "', '" + paymentMode
					+ "', " + amount + ")";
			stmt.executeUpdate(insertQuery);

			JOptionPane.showMessageDialog(this, "Room booked successfully!");

			generateBill(roomNumber, customerName, contact, formattedCheckInDate, formattedCheckOutDate, amount);
		} catch (Exception ex) {
			JOptionPane.showMessageDialog(this, "Error booking room: " + ex.getMessage());
		}
	}

	private boolean checkRoomAvailability(String roomNumber, String roomType, String checkInDate, String checkOutDate) {
		try (Connection conn = databaseCode.getConnection(); Statement stmt = conn.createStatement()) {
			String query = "SELECT * FROM room_bookings WHERE room_number = '" + roomNumber + "' AND room_type = '"
					+ roomType + "' AND (check_in_date BETWEEN '" + checkInDate + "' AND '" + checkOutDate + "'"
					+ " OR check_out_date BETWEEN '" + checkInDate + "' AND '" + checkOutDate + "')";
			ResultSet rs = stmt.executeQuery(query);

			return !rs.next();
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}

	private void generateBill(String roomNumber, String customerName, String contact, String formattedCheckInDate,
			String formattedCheckOutDate, BigDecimal amount) {

		StringBuilder billContent = new StringBuilder();
		billContent.append("******************** HOTEL BILL ********************\n\n");
		billContent.append("Customer Name: ").append(customerName).append("\n\n");
		billContent.append("Contact Number: ").append(contact).append("\n\n");
		billContent.append("Room Number: ").append(roomNumber).append("\n\n");
		billContent.append("Room Type: ").append(roomTypeComboBox.getSelectedItem()).append("\n\n");
		billContent.append("AC/Non-AC: ").append(acOptionComboBox.getSelectedItem()).append("\n\n");
		billContent.append("Check-in Date: ").append(formattedCheckInDate).append("\n\n");
		billContent.append("Check-out Date: ").append(formattedCheckOutDate).append("\n\n");
		billContent.append("Payment Mode: ").append(paymentModeComboBox.getSelectedItem()).append("\n\n");
		billContent.append("Amount Paid: ₹").append(amount).append("\n\n");
		billContent.append("******************************************************\n\n");

		JTextArea billTextArea = new JTextArea(20, 60);
		billTextArea.setText(billContent.toString());
		billTextArea.setFont(new Font("Monospaced", Font.PLAIN, 14));
		billTextArea.setEditable(false);
		JScrollPane scrollPane = new JScrollPane(billTextArea);

		JPanel billPanel = new JPanel(new BorderLayout(10, 10));
		billPanel.add(scrollPane, BorderLayout.CENTER);

		JDialog dialog = new JDialog((JFrame) SwingUtilities.getWindowAncestor(this), "Room Booking Bill", true);
		dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);

		JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
		JButton printButton = new JButton("Print Bill");
		printButton.setPreferredSize(new Dimension(120, 30));
		printButton.setToolTipText("Click to print the bill");

		JButton closeButton = new JButton("Close");
		closeButton.setPreferredSize(new Dimension(120, 30));
		closeButton.setToolTipText("Click to close the dialog");

		printButton.addActionListener(e -> printBill(billTextArea));
		closeButton.addActionListener(e -> dialog.dispose()); 

		buttonPanel.add(printButton);
		buttonPanel.add(closeButton);

		billPanel.add(buttonPanel, BorderLayout.SOUTH);

		dialog.getContentPane().add(billPanel);
		dialog.pack();
		dialog.setLocationRelativeTo(this); 
		dialog.setVisible(true);
	}

	private void printBill(JTextArea billTextArea) {
	    PrinterJob printerJob = PrinterJob.getPrinterJob();
	    printerJob.setPrintable((graphics, pageFormat, pageIndex) -> {
	        if (pageIndex >= 1) {
	            return Printable.NO_SUCH_PAGE;
	        }

	        graphics.setFont(new Font("Monospaced", Font.PLAIN, 14));

	        String text = billTextArea.getText();
	        int yPosition = 100; 

	        String[] lines = text.split("\n");

	        for (String line : lines) {
	            graphics.drawString(line, 100, yPosition);
	            yPosition += 20; 
	        }

	        return Printable.PAGE_EXISTS;
	    });

	    if (printerJob.printDialog()) {
	        try {
	            printerJob.print();
	        } catch (PrinterException e) {
	            JOptionPane.showMessageDialog(this, "Error printing the bill: " + e.getMessage());
	        }
	    }
	}

	public static void main(String[] args) {
		SwingUtilities.invokeLater(RoomBooking::new);
	}
}
