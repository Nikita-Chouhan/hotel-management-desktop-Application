package in.hm;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.print.Printable;
import java.awt.print.PrinterJob;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

public class BanquetDetails extends JFrame {

	private JTable banquetTable;
	private DefaultTableModel tableModel;
	private JTextField searchField;

	public BanquetDetails() {
		setTitle("Banquet Details");
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setSize(800, 600);
		setLocationRelativeTo(null);
		setLayout(new BorderLayout(15, 15));

		String iconPath = "D:\\Spring Projects\\HotelManagement\\src\\Images\\hsfs_logo.png";
		ImageIcon icon = new ImageIcon(iconPath);
		setIconImage(icon.getImage());

		Font headerFont = new Font("Arial", Font.BOLD, 14);
		Font tableFont = new Font("Arial", Font.PLAIN, 14);

		String[] columnNames = { "Booking ID", "Customer Name", "Contact Number", "Booking Type", "Party Type",
				"Booking Date", "End Date", "Payment Mode", "Amount paid" };
		tableModel = new DefaultTableModel(columnNames, 0);
		banquetTable = new JTable(tableModel);
		banquetTable.setFont(tableFont);
		banquetTable.getTableHeader().setFont(headerFont);
		banquetTable.setRowHeight(30);

		JScrollPane scrollPane = new JScrollPane(banquetTable);
		add(scrollPane, BorderLayout.CENTER);

		JPanel topPanel = new JPanel(new BorderLayout(10, 10));
		topPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
		JLabel lblSearch = new JLabel("Search by Name:");
		lblSearch.setFont(new Font("Arial", Font.BOLD, 14));
		searchField = new JTextField();
		searchField.setFont(new Font("Arial", Font.PLAIN, 14));
		JButton btnSearch = new JButton("Search");
		btnSearch.setFont(new Font("Arial", Font.BOLD, 14));
		btnSearch.addActionListener(e -> searchByName());

		topPanel.add(lblSearch, BorderLayout.WEST);
		topPanel.add(searchField, BorderLayout.CENTER);
		topPanel.add(btnSearch, BorderLayout.EAST);

		add(topPanel, BorderLayout.NORTH);

		JButton btnGenerateBill = new JButton("Generate Bill");
		btnGenerateBill.setFont(new Font("Arial", Font.BOLD, 14));
		btnGenerateBill.setBackground(new Color(0, 123, 255));
		btnGenerateBill.setForeground(Color.WHITE);
		btnGenerateBill.addActionListener(e -> generateBill());

		JButton btnDelete = new JButton("Delete");
		btnDelete.setFont(new Font("Arial", Font.BOLD, 14));
		btnDelete.setBackground(new Color(220, 53, 69));
		btnDelete.setForeground(Color.WHITE);
		btnDelete.addActionListener(e -> deleteBooking());

		JButton btnUpdate = new JButton("Update");
		btnUpdate.setFont(new Font("Arial", Font.BOLD, 14));
		btnUpdate.setBackground(new Color(255, 193, 7));
		btnUpdate.setForeground(Color.WHITE);
		btnUpdate.addActionListener(e -> updateBooking());

		JButton btnBack = new JButton("Back");
		btnBack.setFont(new Font("Arial", Font.BOLD, 14));
		btnBack.setBackground(new Color(108, 117, 125));
		btnBack.setForeground(Color.WHITE);
		btnBack.addActionListener(e -> {
			new BanquetDetails();
			dispose();
		});

		JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
		buttonPanel.setBackground(Color.WHITE);
		buttonPanel.add(btnGenerateBill);
		buttonPanel.add(btnDelete);
		buttonPanel.add(btnUpdate);
		buttonPanel.add(btnBack);
		add(buttonPanel, BorderLayout.SOUTH);

		loadBanquetDetails();
		setVisible(true);
	}

	private void loadBanquetDetails() {
		try (Connection conn = databaseCode.getConnection(); Statement stmt = conn.createStatement()) {
			String query = "SELECT * FROM banquet_bookings";
			ResultSet rs = stmt.executeQuery(query);

			tableModel.setRowCount(0);

			while (rs.next()) {
				int bookingId = rs.getInt("booking_id");
				String customerName = rs.getString("customer_name");
				String contactNumber = rs.getString("contact_number");
				String bookingType = rs.getString("type");
				String partyType = rs.getString("party_type");
				String bookingDate = rs.getString("booking_date");
				String endDate = rs.getString("end_time");
				String PaymentMode = rs.getString("payment_mode");
				String AmountPaid = rs.getString("amount_paid");

				tableModel.addRow(new Object[] { bookingId, customerName, contactNumber, bookingType, partyType,
						bookingDate, endDate, PaymentMode, AmountPaid });
			}

		} catch (Exception ex) {
			JOptionPane.showMessageDialog(this, "Error loading banquet details: " + ex.getMessage());
		}
	}

	private void deleteBooking() {
		int selectedRow = banquetTable.getSelectedRow();
		if (selectedRow == -1) {
			JOptionPane.showMessageDialog(this, "Please select a booking to delete.");
			return;
		}

		int bookingId = (int) tableModel.getValueAt(selectedRow, 0);

		int confirmation = JOptionPane.showConfirmDialog(this, "Are you sure you want to delete this booking?",
				"Confirm Deletion", JOptionPane.YES_NO_OPTION);
		if (confirmation == JOptionPane.YES_OPTION) {
			try (Connection conn = databaseCode.getConnection();
					PreparedStatement pstmt = conn
							.prepareStatement("DELETE FROM banquet_bookings WHERE booking_id = ?")) {
				pstmt.setInt(1, bookingId);
				pstmt.executeUpdate();

				tableModel.removeRow(selectedRow);
				JOptionPane.showMessageDialog(this, "Booking deleted successfully.");
			} catch (Exception ex) {
				JOptionPane.showMessageDialog(this, "Error deleting booking: " + ex.getMessage());
			}
		}
	}

	private void searchByName() {
		String searchText = searchField.getText().trim();
		if (searchText.isEmpty()) {
			JOptionPane.showMessageDialog(this, "Please enter a name to search.");
			return;
		}

		try (Connection conn = databaseCode.getConnection(); Statement stmt = conn.createStatement()) {
			String query = "SELECT * FROM banquet_bookings WHERE customer_name LIKE '%" + searchText + "%'";
			ResultSet rs = stmt.executeQuery(query);

			tableModel.setRowCount(0);

			while (rs.next()) {
				int bookingId = rs.getInt("booking_id");
				String customerName = rs.getString("customer_name");
				String contactNumber = rs.getString("contact_number");
				String bookingType = rs.getString("type");
				String partyType = rs.getString("party_type");
				String bookingDate = rs.getString("booking_date");
				String endDate = rs.getString("end_time");
				String PaymentMode = rs.getString("payment_mode");
				String AmountPaid = rs.getString("amount_paid");

				tableModel.addRow(new Object[] { bookingId, customerName, contactNumber, bookingType, partyType,
						bookingDate, endDate, PaymentMode, AmountPaid });
			}

			if (tableModel.getRowCount() == 0) {
				JOptionPane.showMessageDialog(this, "No matching records found.");
			}

		} catch (Exception ex) {
			JOptionPane.showMessageDialog(this, "Error searching records: " + ex.getMessage());
		}
	}

	private void updateBooking() {
		int selectedRow = banquetTable.getSelectedRow();
		if (selectedRow == -1) {
			JOptionPane.showMessageDialog(this, "Please select a booking to update.");
			return;
		}

		int bookingId = (int) tableModel.getValueAt(selectedRow, 0);
		String customerName = (String) tableModel.getValueAt(selectedRow, 1);
		String contactNumber = (String) tableModel.getValueAt(selectedRow, 2);
		String bookingType = (String) tableModel.getValueAt(selectedRow, 3);
		String partyType = (String) tableModel.getValueAt(selectedRow, 4);
		String bookingDate = (String) tableModel.getValueAt(selectedRow, 5);
		String endDate = (String) tableModel.getValueAt(selectedRow, 6);
		String PaymentMode = (String) tableModel.getValueAt(selectedRow, 7);
		String AmountPaid = (String) tableModel.getValueAt(selectedRow, 8);

		JTextField txtCustomerName = new JTextField(customerName);
		JTextField txtContactNumber = new JTextField(contactNumber);
		JTextField txtBookingType = new JTextField(bookingType);
		JTextField txtPartyType = new JTextField(partyType);
		JTextField txtBookingDate = new JTextField(bookingDate);
		JTextField txtEndDate = new JTextField(endDate);
		JTextField txtPaymentMode = new JTextField(PaymentMode);
		JTextField txtAmountPaid = new JTextField(AmountPaid);

		JPanel updatePanel = new JPanel(new GridLayout(6, 2, 10, 10));
		updatePanel.add(new JLabel("Customer Name:"));
		updatePanel.add(txtCustomerName);
		updatePanel.add(new JLabel("Contact Number:"));
		updatePanel.add(txtContactNumber);
		updatePanel.add(new JLabel("Booking Type:"));
		updatePanel.add(txtBookingType);
		updatePanel.add(new JLabel("Party Type:"));
		updatePanel.add(txtPartyType);
		updatePanel.add(new JLabel("Booking Date:"));
		updatePanel.add(txtBookingDate);
		updatePanel.add(new JLabel("End Time:"));
		updatePanel.add(txtEndDate);
		updatePanel.add(new JLabel("Payment Mode:"));
		updatePanel.add(txtPaymentMode);
		updatePanel.add(new JLabel("Amount Paid:"));
		updatePanel.add(txtAmountPaid);

		int result = JOptionPane.showConfirmDialog(this, updatePanel, "Update Booking Details",
				JOptionPane.OK_CANCEL_OPTION);
		if (result == JOptionPane.OK_OPTION) {
			try (Connection conn = databaseCode.getConnection();
					PreparedStatement pstmt = conn.prepareStatement(
							"UPDATE banquet_bookings SET customer_name = ?, contact_number = ?, type = ?, party_type = ?, booking_date = ?, end_time = ?, payment_mode= ?, amount_paid=? WHERE booking_id = ?")) {
				pstmt.setString(1, txtCustomerName.getText().trim());
				pstmt.setString(2, txtContactNumber.getText().trim());
				pstmt.setString(3, txtBookingType.getText().trim());
				pstmt.setString(4, txtPartyType.getText().trim());
				pstmt.setString(5, txtBookingDate.getText().trim());
				pstmt.setString(6, txtEndDate.getText().trim());
				pstmt.setString(7, txtPaymentMode.getText().trim());
				pstmt.setString(8, txtAmountPaid.getText().trim());
				pstmt.setInt(7, bookingId);
				pstmt.executeUpdate();

				tableModel.setValueAt(txtCustomerName.getText().trim(), selectedRow, 1);
				tableModel.setValueAt(txtContactNumber.getText().trim(), selectedRow, 2);
				tableModel.setValueAt(txtBookingType.getText().trim(), selectedRow, 3);
				tableModel.setValueAt(txtPartyType.getText().trim(), selectedRow, 4);
				tableModel.setValueAt(txtBookingDate.getText().trim(), selectedRow, 5);
				tableModel.setValueAt(txtEndDate.getText().trim(), selectedRow, 6);
				tableModel.setValueAt(txtPaymentMode.getText().trim(), selectedRow, 7);
				tableModel.setValueAt(txtAmountPaid.getText().trim(), selectedRow, 8);

				JOptionPane.showMessageDialog(this, "Booking updated successfully.");
			} catch (Exception ex) {
				JOptionPane.showMessageDialog(this, "Error updating booking: " + ex.getMessage());
			}
		}
	}

	private void generateBill() {
		int selectedRow = banquetTable.getSelectedRow();
		if (selectedRow == -1) {
			JOptionPane.showMessageDialog(this, "Please select a booking to generate the bill.");
			return;
		}

		int bookingId = (int) tableModel.getValueAt(selectedRow, 0);

		try (Connection conn = databaseCode.getConnection(); Statement stmt = conn.createStatement()) {

			String query = "SELECT * FROM banquet_bookings WHERE booking_id = " + bookingId;
			ResultSet rs = stmt.executeQuery(query);

			if (rs.next()) {
				String customerName = rs.getString("customer_name");
				String contactNumber = rs.getString("contact_number");
				String bookingDate = rs.getString("booking_date");
				String endDate = rs.getString("end_time");
				String banquetType = rs.getString("type");
				double paymentAmount = rs.getDouble("amount_paid");
				double gst = paymentAmount * 0.12;
				double totalAmount = paymentAmount + gst;

				JDialog billDialog = new JDialog(this, "Bill Details", true);
				billDialog.setSize(500, 600);
				billDialog.setLocationRelativeTo(this);
				billDialog.setLayout(new BorderLayout());

				JPanel billPanel = new JPanel();
				billPanel.setLayout(new BoxLayout(billPanel, BoxLayout.Y_AXIS));
				billPanel.setBorder(new EmptyBorder(20, 20, 20, 20));
				billPanel.setBackground(Color.WHITE);

				JLabel lblTitle = new JLabel("Banquet Booking Bill");
				lblTitle.setFont(new Font("Arial", Font.BOLD, 20));
				lblTitle.setAlignmentX(Component.CENTER_ALIGNMENT);

				JPanel detailsPanel = new JPanel();
				detailsPanel.setLayout(new GridLayout(0, 2, 10, 10)); // Grid for neat rows and columns
				detailsPanel.setBackground(Color.WHITE);

				detailsPanel.add(new JLabel("Booking ID:"));
				detailsPanel.add(new JLabel(String.valueOf(bookingId)));

				detailsPanel.add(new JLabel("Customer Name:"));
				detailsPanel.add(new JLabel(customerName));

				detailsPanel.add(new JLabel("Contact Number:"));
				detailsPanel.add(new JLabel(contactNumber));

				detailsPanel.add(new JLabel("Booking Date:"));
				detailsPanel.add(new JLabel(bookingDate));

				detailsPanel.add(new JLabel("End Date:"));
				detailsPanel.add(new JLabel(endDate));

				detailsPanel.add(new JLabel("Banquet Type:"));
				detailsPanel.add(new JLabel(banquetType));

				detailsPanel.add(new JLabel("Payment Amount:"));
				detailsPanel.add(new JLabel("₹" + String.format("%.2f", paymentAmount)));

				detailsPanel.add(new JLabel("GST (12%):"));
				detailsPanel.add(new JLabel("₹" + String.format("%.2f", gst)));

				detailsPanel.add(new JLabel("Total Amount:"));
				detailsPanel.add(new JLabel("₹" + String.format("%.2f", totalAmount)));

				billPanel.add(lblTitle);
				billPanel.add(Box.createVerticalStrut(20));
				billPanel.add(detailsPanel);

				JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
				JButton btnSaveAsPDF = new JButton("Save as PDF");
				btnSaveAsPDF.setFont(new Font("Arial", Font.BOLD, 14));
				btnSaveAsPDF.addActionListener(e -> saveBillAsPDF(bookingId, customerName, contactNumber, bookingDate,
						paymentAmount, gst, totalAmount));

				JButton btnClose = new JButton("Close");
				btnClose.setFont(new Font("Arial", Font.BOLD, 14));
				btnClose.addActionListener(e -> billDialog.dispose());

				buttonPanel.add(btnSaveAsPDF);
				buttonPanel.add(btnClose);

				billDialog.add(billPanel, BorderLayout.CENTER);
				billDialog.add(buttonPanel, BorderLayout.SOUTH);

				billDialog.setVisible(true);
			} else {
				JOptionPane.showMessageDialog(this, "No details found for the selected booking.");
			}

		} catch (Exception ex) {
			JOptionPane.showMessageDialog(this, "Error retrieving booking details: " + ex.getMessage());
		}
	}

	private void saveBillAsPDF(int bookingId, String customerName, String contactNumber, String bookingDate,
			double paymentAmount, double gst, double totalAmount) {
		Printable printable = (graphics, pageFormat, pageIndex) -> {
			if (pageIndex > 0)
				return Printable.NO_SUCH_PAGE;

			Graphics2D g2d = (Graphics2D) graphics;
			g2d.translate(pageFormat.getImageableX(), pageFormat.getImageableY());

			g2d.setFont(new Font("Arial", Font.BOLD, 18));
			g2d.drawString("Banquet Booking Bill", 100, 50);

			g2d.setFont(new Font("Arial", Font.PLAIN, 12));
			g2d.drawString("Booking ID: " + bookingId, 100, 100);
			g2d.drawString("Customer Name: " + customerName, 100, 120);
			g2d.drawString("Contact Number: " + contactNumber, 100, 140);
			g2d.drawString("Booking Date: " + bookingDate, 100, 160);
			g2d.drawString("Payment Amount: ₹" + paymentAmount, 100, 180);
			g2d.drawString("GST (12%): ₹" + gst, 100, 200);
			g2d.drawString("Total Amount: ₹" + totalAmount, 100, 220);

			return Printable.PAGE_EXISTS;
		};

		PrinterJob job = PrinterJob.getPrinterJob();
		job.setPrintable(printable);

		try {
			if (job.printDialog()) {
				job.print();
				JOptionPane.showMessageDialog(this, "Bill saved successfully.");
			}
		} catch (Exception e) {
			JOptionPane.showMessageDialog(this, "Failed to save bill: " + e.getMessage());
		}
	}

	public static void main(String[] args) {
		SwingUtilities.invokeLater(BanquetDetails::new);
	}
}
