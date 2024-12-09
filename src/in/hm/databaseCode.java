package in.hm;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

public class databaseCode {
    private static final String JDBC_URL = "jdbc:sqlite:data/hotel_management.db";
//    private static final String USER = "sa";
//    private static final String PASSWORD = "";

    public static Connection getConnection() throws SQLException {
        try {
            Class.forName("org.sqlite.JDBC");
            Connection conn = DriverManager.getConnection(JDBC_URL);
            
            return conn;
        } catch (ClassNotFoundException e) {
            throw new SQLException("sqlite JDBC Driver not found", e);
        }
    }
}
