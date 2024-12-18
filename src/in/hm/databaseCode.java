package in.hm;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class databaseCode {
	private static final String JDBC_URL = "jdbc:sqlite:data/hotel_management.db";

	public static Connection getConnection() throws SQLException {
		try {
			Class.forName("org.sqlite.JDBC");
			return DriverManager.getConnection(JDBC_URL);
		} catch (ClassNotFoundException e) {
			throw new SQLException("SQLite JDBC Driver not found", e);
		}
	}

	public static boolean insertBooking(int bookingId, String customerName, int customer_Number, String bookingDate,
			int numGuests, String eventType, String foodType, int duration, String hallName, String booking_Status,
			String special_Request, int total_Cost, String payment_Status) {
		String query = "INSERT INTO banquet_booking (bookingId, bookingDate, numGuests, eventType, foodType, duration, hallName) "
				+ "VALUES (?, ?, ?, ?, ?, ?, ?)";
		try (Connection conn = getConnection(); PreparedStatement stmt = conn.prepareStatement(query)) {

			stmt.setInt(1, bookingId);
			stmt.setString(2, bookingDate);
			stmt.setInt(3, numGuests);
			stmt.setString(4, eventType);
			stmt.setString(5, foodType);
			stmt.setInt(6, duration);
			stmt.setString(7, hallName);

			int rowsAffected = stmt.executeUpdate();
			return rowsAffected > 0;
		} catch (SQLException e) {
			System.err.println("Error inserting booking: " + e.getMessage());
			return false;
		}
	}

	public static double generateBill(int bookingId) {
		return 0.0;
	}

}
