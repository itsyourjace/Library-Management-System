/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package mylibrary;

import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import java.awt.Image;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;



public class UserIconUtility {

    public static ImageIcon retrieveUserIcon(int userId) {
        ImageIcon userIcon = null;
        try (Connection conn = DbConnection.getConnection()) {
            String query = "SELECT icon FROM users WHERE user_id = ?";
            PreparedStatement pstmt = conn.prepareStatement(query);
            pstmt.setInt(1, userId);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                java.sql.Blob blob = rs.getBlob("icon");
                if (blob != null) {
                    userIcon = new ImageIcon(blob.getBytes(1, (int) blob.length()));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error retrieving user icon", "Error", JOptionPane.ERROR_MESSAGE);
        }
        return userIcon;
    }
}
