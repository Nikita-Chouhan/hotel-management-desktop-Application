package in.hm;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;

public class RoomManagementCRUD extends JFrame {

	private JTable roomTable;
	private DefaultTableModel tableModel;

	public RoomManagementCRUD() {
		setTitle("Room Management");
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setSize(800, 600);
		setLocationRelativeTo(null);
		setLayout(new BorderLayout(10, 10));

		JPanel tablePanel = new JPanel(new BorderLayout());

		tableModel = new DefaultTableModel(new String[] { "ID", "Room Number", "Room Type", "AC Option",
				"Customer Name", "Contact", "Check-in Date", "Check-out Date", "Payment Mode", "Amount" }, 0);
		roomTable = new JTable(tableModel);
		roomTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		JScrollPane scrollPane = new JScrollPane(roomTable);
		tablePanel.add(scrollPane, BorderLayout.CENTER);

		JButton updateButton = new JButton("Update");
		updateButton.addActionListener(e -> updateRoom());

		updateButton.setBackground(new Color(0, 123, 255)); 
		updateButton.setForeground(Color.WHITE); 
		updateButton.setFont(new Font("Arial", Font.BOLD, 14));
		updateButton.setPreferredSize(new Dimension(150, 50)); 
		updateButton.setBorder(BorderFactory.createRaisedBevelBorder()); 

		JButton deleteButton = new JButton("Delete");
		deleteButton.addActionListener(e -> deleteRoom());

		deleteButton.setBackground(new Color(220, 53, 69)); 
		deleteButton.setForeground(Color.WHITE); 
		deleteButton.setFont(new Font("Arial", Font.BOLD, 14)); 
		deleteButton.setPreferredSize(new Dimension(150, 50)); 
		deleteButton.setBorder(BorderFactory.createRaisedBevelBorder()); 

		JPanel buttonPanel = new JPanel();
		buttonPanel.add(updateButton);
		buttonPanel.add(deleteButton);

		add(tablePanel, BorderLayout.CENTER);
		add(buttonPanel, BorderLayout.SOUTH);

		loadData();

		setVisible(true);
	}

	private void loadData() {
		tableModel.setRowCount(0);
		try (Connection conn = databaseCode.getConnection(); 
			Statement stmt = conn.createStatement()) {
			String query = "SELECT * FROM room_bookings";
			ResultSet rs = stmt.executeQuery(query);
			while (rs.next()) {
				tableModel.addRow(new Object[] { 
						rs.getInt("booking_id"), 
						rs.getString("room_number"),
						rs.getString("room_type"), 
						rs.getString("ac_option"), 
						rs.getString("customer_name"),
						rs.getString("contact_number"), 
						rs.getString("check_in_date"), 
						rs.getString("check_out_date"),
						rs.getString("payment_mode"), 
						rs.getDouble("amount_paid") });
			}
		} catch (SQLException e) {
			JOptionPane.showMessageDialog(this, "Error loading data: " + e.getMessage());
			e.printStackTrace();
		}
	}

	private void updateRoom() {
		int selectedRow = roomTable.getSelectedRow();
		if (selectedRow == -1) {
			JOptionPane.showMessageDialog(this, "Please select a row to update.");
			return;
		}

		int id = (int) tableModel.getValueAt(selectedRow, 0);
		String roomNumber = (String) tableModel.getValueAt(selectedRow, 1);
		String roomType = (String) tableModel.getValueAt(selectedRow, 2);
		String acOption = (String) tableModel.getValueAt(selectedRow, 3);
		String customerName = (String) tableModel.getValueAt(selectedRow, 4);
		String contact = (String) tableModel.getValueAt(selectedRow, 5);
		String checkInDate = (String) tableModel.getValueAt(selectedRow, 6);
		String checkOutDate = (String) tableModel.getValueAt(selectedRow, 7);
		String paymentMode = (String) tableModel.getValueAt(selectedRow, 8);
		Double amount = (Double) tableModel.getValueAt(selectedRow, 9);

		try (Connection conn = databaseCode.getConnection();
				PreparedStatement pstmt = conn.prepareStatement(
						"UPDATE room_bookings SET room_number=?, room_type=?, ac_option=?, customer_name=?, contact_number=?, check_in_date=?, check_out_date=?, payment_mode=?, amount_paid=? WHERE booking_id=?")) {
			pstmt.setString(1, roomNumber);
			pstmt.setString(2, roomType);
			pstmt.setString(3, acOption);
			pstmt.setString(4, customerName);
			pstmt.setString(5, contact);
			pstmt.setString(6, checkInDate);
			pstmt.setString(7, checkOutDate);
			pstmt.setString(8, paymentMode);
			pstmt.setDouble(9, amount);
			pstmt.setInt(10, id);

			pstmt.executeUpdate();

			JOptionPane.showMessageDialog(this, "Record updated successfully.");
			loadData();
		} catch (SQLException e) {
			JOptionPane.showMessageDialog(this, "Error updating record: " + e.getMessage());
		}
	}

	private void deleteRoom() {
		int selectedRow = roomTable.getSelectedRow();
		if (selectedRow == -1) {
			JOptionPane.showMessageDialog(this, "Please select a row to delete.");
			return;
		}

		int id = (int) tableModel.getValueAt(selectedRow, 0);

		int confirm = JOptionPane.showConfirmDialog(this, "Are you sure you want to delete this record?",
				"Confirm Delete", JOptionPane.YES_NO_OPTION);
		if (confirm == JOptionPane.YES_OPTION) {
			try (Connection conn = databaseCode.getConnection();
					PreparedStatement pstmt = conn.prepareStatement("DELETE FROM room_bookings WHERE booking_id=?")) {
				pstmt.setInt(1, id);
				pstmt.executeUpdate();

				JOptionPane.showMessageDialog(this, "Record deleted successfully.");
				loadData();
			} catch (SQLException e) {
				JOptionPane.showMessageDialog(this, "Error deleting record: " + e.getMessage());
			}
		}
	}

	public static void main(String[] args) {
		SwingUtilities.invokeLater(RoomManagementCRUD::new);
	}
}
