
package mylibrary;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class DbConnection {
    private static final String URL = "jdbc:mysql://localhost:3306/library_db"; 
    private static final String USER = "root"; 
    private static final String PASSWORD = ""; 

    // Method to establish a database connection
    public static Connection getConnection() throws SQLException {
        Connection connection = null;
        try {
            connection = DriverManager.getConnection(URL, USER, PASSWORD);
            System.out.println("Connection established successfully.");
        } catch (SQLException e) {
            System.err.println("Failed to establish connection: " + e.getMessage());
            throw e;
        }
        return connection;
    }
    
   public static void main(String[] args) {
         System.out.println("Testing database connection...");

      
        DbConnection.testConnection();
        
    }

    static void testConnection() {
       
    }
    
    // Method to get the user's icon path from the database
    public String getUserIconPath(int userId) {
    String iconPath = null;
    Connection conn = null;
    try {
        conn = getConnection(); // Call the static method to get the connection
        String query = "SELECT icon FROM users WHERE user_id=?"; // Query using the 'icon' column
        PreparedStatement pstmt = conn.prepareStatement(query);
        pstmt.setInt(1, userId);
        ResultSet rs = pstmt.executeQuery();

        if (rs.next()) {
            iconPath = "src/icons/" + rs.getString("icon"); // Construct the full path using the icon column
        }
    } catch (SQLException e) {
        e.printStackTrace();
    } finally {
        if (conn != null) {
            try {
                conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
    return iconPath; // Return the icon path
}
}
