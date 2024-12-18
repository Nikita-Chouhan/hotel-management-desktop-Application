package in.hm;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import de.wannawork.jcalendar.JCalendarPanel;
import java.awt.*;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Date;

public class BanquetBooking extends JFrame {

	private JCalendarPanel calendar;
	private DefaultTableModel bookingTableModel;
	private JTable bookingTable;

	public BanquetBooking() {
		setTitle("Banquet Booking");
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setSize(900, 600);
		setLocationRelativeTo(null);
		setLayout(new BorderLayout(15, 15));

		String iconPath = "D:\\Spring Projects\\HotelManagement\\src\\Images\\hsfs_logo.png";
		ImageIcon icon = new ImageIcon(iconPath);
		setIconImage(icon.getImage());

		Font headerFont = new Font("Arial", Font.BOLD, 16);
		Font tableFont = new Font("Arial", Font.PLAIN, 14);
		Color primaryColor = new Color(0, 123, 255);
		Color secondaryColor = new Color(240, 240, 240);
		Color buttonColor = new Color(0, 123, 255);

		JPanel calendarPanel = new JPanel(new BorderLayout(10, 10));
		calendarPanel.setBackground(secondaryColor);
		calendarPanel.setBorder(new EmptyBorder(15, 15, 15, 15));
		JLabel calendarLabel = new JLabel("Select a Date:");
		calendarLabel.setFont(headerFont);
		calendarLabel.setForeground(Color.DARK_GRAY);
		calendar = new JCalendarPanel();

		calendar.addPropertyChangeListener("calendar", evt -> loadBookings(calendar.getDate()));
		calendar.addMouseListener(new java.awt.event.MouseAdapter() {
			@Override
			public void mouseClicked(java.awt.event.MouseEvent e) {
				Date selectedDate = calendar.getDate();
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
				String formattedDate = sdf.format(selectedDate);

				String[] options = { "Hall", "Lounge", "Both" };
				String type = (String) JOptionPane.showInputDialog(BanquetBooking.this, "Select booking type:",
						"Select Room Type", JOptionPane.PLAIN_MESSAGE, null, options, options[0]

				);

				if (type == null)
					return;

				System.out.println("Selected date: " + selectedDate);
				System.out.println("Formatted date: " + formattedDate);
				System.out.println("Room type: " + type);

				checkRoomAvailability(type, formattedDate);
			}
		});

		calendarPanel.add(calendarLabel, BorderLayout.NORTH);
		calendarPanel.add(calendar, BorderLayout.CENTER);

		String[] columnNames = { "Booking ID", "Type", "Party Type", "Customer Name", "Contact Number", "Booking Date",
				"End Time" };
		bookingTableModel = new DefaultTableModel(columnNames, 0);
		bookingTable = new JTable(bookingTableModel);
		bookingTable.setFont(tableFont);
		bookingTable.getTableHeader().setFont(headerFont);
		bookingTable.setRowHeight(30);
		bookingTable.setSelectionBackground(primaryColor);
		bookingTable.setGridColor(new Color(220, 220, 220));
		bookingTable.setIntercellSpacing(new Dimension(5, 5));
		JScrollPane bookingScrollPane = new JScrollPane(bookingTable);
		bookingScrollPane.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200)));

		JButton addBookingButton = new JButton("Add Booking");
		addBookingButton.setFont(headerFont);
		addBookingButton.setBackground(buttonColor);
		addBookingButton.setForeground(Color.WHITE);
		addBookingButton.setFocusPainted(false);
		addBookingButton.setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY));
		addBookingButton.addActionListener(e -> addBooking());

		JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
		buttonPanel.setBackground(secondaryColor);
		buttonPanel.add(addBookingButton);

		add(calendarPanel, BorderLayout.WEST);
		add(bookingScrollPane, BorderLayout.CENTER);
		add(buttonPanel, BorderLayout.SOUTH);

		setVisible(true);
	}

	private void loadBookings(Date selectedDate) {
		bookingTableModel.setRowCount(0);
		try (Connection conn = databaseCode.getConnection(); Statement stmt = conn.createStatement()) {

			String query = "SELECT * FROM banquet_bookings WHERE booking_date = '"
					+ new java.sql.Date(selectedDate.getTime()) + "'";
			ResultSet rs = stmt.executeQuery(query);

			while (rs.next()) {
				int bookingId = rs.getInt("booking_id");
				String type = rs.getString("type");
				String partyType = rs.getString("party_type");
				String customerName = rs.getString("customer_name");
				String contactNumber = rs.getString("contact_number");
				String bookingDate = rs.getString("booking_date");
				String endTime = rs.getString("end_time");

				bookingTableModel.addRow(
						new Object[] { bookingId, type, partyType, customerName, contactNumber, bookingDate, endTime });
			}

		} catch (Exception ex) {
			JOptionPane.showMessageDialog(this, "Error loading bookings: " + ex.getMessage());
		}
	}

	private boolean checkRoomAvailability(String type, String formattedDate) {
		try (Connection conn = databaseCode.getConnection(); Statement stmt = conn.createStatement()) {

			String query = "SELECT type FROM banquet_bookings WHERE booking_date = '" + formattedDate + "'";
			ResultSet rs = stmt.executeQuery(query);

			while (rs.next()) {
				String bookedType = rs.getString("type");
				if ("Both".equals(bookedType) || "Both".equals(type)) {
					return false;
				}
				if (("Hall".equals(type) && "Hall".equals(bookedType))
						|| ("Lounge".equals(type) && "Lounge".equals(bookedType))) {
					return false;
				}
			}

			return true;
		} catch (Exception ex) {
			JOptionPane.showMessageDialog(this, "Error checking room availability: " + ex.getMessage());
			return false;
		}
	}

	private void addBooking() {

		JPanel formPanel = new JPanel(new GridLayout(0, 2, 10, 10));

		String[] options = { "Hall", "Lounge", "Both" };
		JComboBox<String> typeComboBox = new JComboBox<>(options);
		formPanel.add(new JLabel("Select Room Type:"));
		formPanel.add(typeComboBox);

		String[] partyOptions = { "Wedding", "Ring Ceremony", "Birthday Party", "Conference Meeting", "Farewell Party",
				"Other" };
		JComboBox<String> partyTypeComboBox = new JComboBox<>(partyOptions);
		formPanel.add(new JLabel("Select Party Type:"));
		formPanel.add(partyTypeComboBox);

		JTextField customerNameField = new JTextField();
		formPanel.add(new JLabel("Customer Name:"));
		formPanel.add(customerNameField);

		JTextField contactField = new JTextField();
		formPanel.add(new JLabel("Contact Number:"));
		formPanel.add(contactField);

		JTextField endTimeField = new JTextField("yyyy-mm-dd");
		endTimeField.setForeground(Color.GRAY);
		formPanel.add(new JLabel("Booking Duration (End Time):"));
		formPanel.add(endTimeField);

		endTimeField.addFocusListener(new java.awt.event.FocusAdapter() {
			public void focusGained(java.awt.event.FocusEvent evt) {
				if (endTimeField.getText().equals("yyyy-mm-dd")) {
					endTimeField.setText("");
					endTimeField.setForeground(Color.BLACK);
				}
			}

			public void focusLost(java.awt.event.FocusEvent evt) {
				if (endTimeField.getText().isEmpty()) {
					endTimeField.setText("yyyy-mm-dd");
					endTimeField.setForeground(Color.GRAY);
				}
			}
		});

		String[] paymentModes = { "Cash", "Card", "Online" };
		JComboBox<String> paymentModeComboBox = new JComboBox<>(paymentModes);
		formPanel.add(new JLabel("Select Payment Mode:"));
		formPanel.add(paymentModeComboBox);

		JTextField amountField = new JTextField();
		formPanel.add(new JLabel("Amount Paid:"));
		formPanel.add(amountField);

		int option = JOptionPane.showConfirmDialog(this, formPanel, "Add Booking Details", JOptionPane.OK_CANCEL_OPTION,
				JOptionPane.PLAIN_MESSAGE);

		if (option == JOptionPane.OK_OPTION) {
			String type = (String) typeComboBox.getSelectedItem();
			String partyType = (String) partyTypeComboBox.getSelectedItem();
			String customerName = customerNameField.getText().trim();
			String contact = contactField.getText().trim();
			String endTime = endTimeField.getText().trim();
			String paymentMode = (String) paymentModeComboBox.getSelectedItem();
			String amountStr = amountField.getText().trim();

			if (customerName.isEmpty() || !customerName.matches("[a-zA-Z ]")) {
				JOptionPane.showMessageDialog(this, "Customer name cannot be empty.");
				return;
			}

			if (contact.isEmpty() || !contact.matches("\\d{10}")) {
				JOptionPane.showMessageDialog(this, "Please enter a valid 10-digit contact number.");
				return;
			}

			if (endTime.isEmpty()) {
				JOptionPane.showMessageDialog(this, "Please enter booking time.");
				return;
			}

			if (amountStr.isEmpty() || !amountStr.matches("\\d+(\\.\\d{1,2})?")) {
				JOptionPane.showMessageDialog(this, "Please enter a valid amount.");
				return;
			}

			BigDecimal amount = new BigDecimal(amountStr);
			LocalDate selectedDate = new java.sql.Date(calendar.getDate().getTime()).toLocalDate();
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			String formattedDate = sdf.format(calendar.getDate());

			if (!checkRoomAvailability(type, formattedDate)) {
				JOptionPane.showMessageDialog(this, "The selected type is already booked for this date.");
				return;
			}
			try (Connection conn = databaseCode.getConnection(); Statement stmt = conn.createStatement()) {

				String insertQuery = "INSERT INTO banquet_bookings (type, party_type, customer_name, contact_number, booking_date, end_time, payment_mode, amount_paid) "
						+ "VALUES ('" + type + "', '" + partyType + "', '" + customerName + "', '" + contact + "', '"
						+ selectedDate + "', '" + endTime + "', '" + paymentMode + "', " + amount + ");";
				stmt.executeUpdate(insertQuery);

				JOptionPane.showMessageDialog(this, "Booking added successfully with payment details!");
				loadBookings(calendar.getDate());
			} catch (Exception ex) {
				JOptionPane.showMessageDialog(this, "Error adding booking: " + ex.getMessage());
			}
		}
	}

	public static void main(String[] args) {
		SwingUtilities.invokeLater(BanquetBooking::new);
	}
}
