/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package mylibrary;

import com.mysql.cj.jdbc.Blob;
import java.awt.Image;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.DriverManager;
import java.sql.ResultSet;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.JOptionPane;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 *
 * @author ADMIN
 */
public class LibUsersInfo extends javax.swing.JFrame {
    private String username;
    private int userId;
    
    
    
    
    /**
     * Creates new form LibUsersInfo
     * @param userId
     * @param username
     */
    public LibUsersInfo(int userId, String username) {
        this.userId = userId;
        this.username = username;
        initComponents();
        userusername.setText(username.toUpperCase());
        loadUserProfileIcon();
        updateBookCounts(userId);
        setLocationRelativeTo(null);
        loadUserProfileIcon();
        
        
        try {
            fetchUserInfo(userId);
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error fetching user information", "Error", JOptionPane.ERROR_MESSAGE);
        }
    
    userdash.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                new LibDashboard(userId, username).setVisible(true); // Open LibView JFrame
                dispose(); // Close the dashboard
            }
        });
    
    userview.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                new LibView(userId,username).setVisible(true); // Open LibView JFrame
                dispose(); // Close the dashboard
            }
        });
    
        userborrow.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                new LibBorrow(userId,username).setVisible(true); // Open LibView JFrame
                dispose(); // Close the dashboard
            }
        });
        
        userreturn.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                new LibReturn(userId,username).setVisible(true); // Open LibView JFrame
                dispose(); // Close the dashboard
            }
        });
        
        userhistory.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                new LibHistory(userId, username).setVisible(true); // Open LibView JFrame
                dispose(); // Close the dashboard
            }
        });
        
        userlogout.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int response = JOptionPane.showConfirmDialog(
                        LibUsersInfo.this,
                        "Are you sure you want to log out?",
                        "Logout Confirmation",
                        JOptionPane.YES_NO_OPTION
                );

                if (response == JOptionPane.YES_OPTION) {
                    // Proceed with logout
                    LibLogin loginScreen = new LibLogin(); // Create a new instance of your login screen
                    loginScreen.setVisible(true); // Show the login screen
                    dispose(); // Close the current dashboard window
                }
            }
        });
     
    


 }
    private Connection connect() {
        Connection conn = null;
        try {
            // Load the MySQL JDBC Driver
            Class.forName("com.mysql.cj.jdbc.Driver");
            // Establish the connection
            String url = "jdbc:mysql://localhost:3306/library_db";
            String user = "root"; // Your database username
            String password = ""; // Your database password
            conn = DriverManager.getConnection(url, user, password);
            System.out.println("Database connected successfully!");
        } catch (ClassNotFoundException e) {
            System.out.println("MySQL JDBC Driver not found. Include it in your library path.");
        } catch (SQLException e) {
            System.out.println("Connection failed! Check output console");
        }
        return conn;

    }

    private void fetchUserInfo(int userId) throws SQLException {
        Connection conn = connect(); // Use your connect method to get a connection
        if (conn != null) {
            String query = "SELECT * FROM users WHERE user_id = ?";
            try {
                PreparedStatement pstmt = conn.prepareStatement(query); // Use the prepareStatement method on the Connection object
                pstmt.setInt(1, userId);
                ResultSet rs = pstmt.executeQuery();

                if (rs.next()) {
                    String username = rs.getString("username");
                    String fullName = rs.getString("full_name");
                    String email = rs.getString("email");
                    String password = rs.getString("password");

                    // Set retrieved data to fields or labels in the UI
                    usernamefield.setText(username);
                    fullnamefld.setText(fullName);
                    emailfield.setText(email);
                    
                    int fetchedUserId = rs.getInt("user_id"); // Ensure this matches your database column name
                    userid_fld.setText(String.valueOf(fetchedUserId)); // Display user ID
                    userid_fld.setEditable(false); //
                    oldpassfield.setText("password"); // Display old password
                    oldpassfield.setEditable(false);
                }
            } catch (SQLException e) {
                e.printStackTrace();
            } finally {
                try {
                    conn.close(); // Close the connection
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
 
    }
    
    public int getTotalBorrowedBooks(int userId) {
        int count = 0;
        String query = "SELECT COUNT(*) FROM transactions WHERE user_id = ? AND return_date IS NULL";

        try (Connection conn = DbConnection.getConnection(); PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setInt(1, userId);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                count = rs.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return count;
    }

    public int getTotalReturnedBooks(int userId) {
        int count = 0;
        String query = "SELECT COUNT(*) FROM transactions WHERE user_id = ? AND return_date IS NOT NULL";

        try (Connection conn = DbConnection.getConnection(); PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setInt(1, userId);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                count = rs.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return count;
    }
    
    public void updateBookCounts(int userId) {
    int borrowedCount = getTotalBorrowedBooks(userId);
    int returnedCount = getTotalReturnedBooks(userId);

    numberborrowed.setText(String.valueOf(borrowedCount));
    numberreturned.setText(String.valueOf(returnedCount));
    
}

    
  



    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel9 = new javax.swing.JPanel();
        iconuser = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();
        usericon = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        userdash = new javax.swing.JLabel();
        jPanel4 = new javax.swing.JPanel();
        userview = new javax.swing.JLabel();
        jPanel5 = new javax.swing.JPanel();
        userborrow = new javax.swing.JLabel();
        jPanel6 = new javax.swing.JPanel();
        userusername = new javax.swing.JLabel();
        jPanel3 = new javax.swing.JPanel();
        userhistory = new javax.swing.JLabel();
        jPanel7 = new javax.swing.JPanel();
        userlogout = new javax.swing.JLabel();
        userreturn = new javax.swing.JLabel();
        jPanel8 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        userreturn2 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        closeuserbtn = new javax.swing.JButton();
        changeiconbtn = new javax.swing.JButton();
        jPanel10 = new javax.swing.JPanel();
        jLabel5 = new javax.swing.JLabel();
        jPanel11 = new javax.swing.JPanel();
        jLabel9 = new javax.swing.JLabel();
        jPanel13 = new javax.swing.JPanel();
        jLabel6 = new javax.swing.JLabel();
        jPanel14 = new javax.swing.JPanel();
        jLabel10 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jPanel12 = new javax.swing.JPanel();
        jPanel15 = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        jPanel17 = new javax.swing.JPanel();
        jLabel16 = new javax.swing.JLabel();
        usernamefield = new javax.swing.JTextField();
        userid_fld = new javax.swing.JTextField();
        emailfield = new javax.swing.JTextField();
        oldpassfield = new javax.swing.JPasswordField();
        newpassfield = new javax.swing.JPasswordField();
        confirmpassfield = new javax.swing.JPasswordField();
        deactbtn = new javax.swing.JButton();
        updatebtn = new javax.swing.JButton();
        jLabel14 = new javax.swing.JLabel();
        jPanel16 = new javax.swing.JPanel();
        fullnamefld = new javax.swing.JTextField();
        jPanel18 = new javax.swing.JPanel();
        jLabel13 = new javax.swing.JLabel();
        jPanel19 = new javax.swing.JPanel();
        jLabel15 = new javax.swing.JLabel();
        jPanel20 = new javax.swing.JPanel();
        numberborrowed = new javax.swing.JLabel();
        jPanel21 = new javax.swing.JPanel();
        numberreturned = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("User's  Information");
        setPreferredSize(new java.awt.Dimension(900, 540));
        getContentPane().setLayout(null);

        jPanel9.setBackground(new java.awt.Color(255, 255, 255));

        iconuser.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        iconuser.setIcon(new javax.swing.ImageIcon("C:\\Users\\ADMIN\\Documents\\NetBeansProjects\\MyLibrary\\src\\icons\\defaulticon.png")); // NOI18N
        iconuser.setPreferredSize(new java.awt.Dimension(120, 90));

        javax.swing.GroupLayout jPanel9Layout = new javax.swing.GroupLayout(jPanel9);
        jPanel9.setLayout(jPanel9Layout);
        jPanel9Layout.setHorizontalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(iconuser, javax.swing.GroupLayout.DEFAULT_SIZE, 130, Short.MAX_VALUE)
        );
        jPanel9Layout.setVerticalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel9Layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(iconuser, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        getContentPane().add(jPanel9);
        jPanel9.setBounds(390, 50, 130, 90);

        jPanel1.setBackground(new java.awt.Color(210, 232, 237));
        jPanel1.setPreferredSize(new java.awt.Dimension(120, 500));
        jPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        usericon.setIcon(new javax.swing.ImageIcon("C:\\Users\\ADMIN\\Documents\\NetBeansProjects\\MyLibrary\\src\\icons\\defaulticon.png")); // NOI18N
        jPanel1.add(usericon, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 120, 80));

        jPanel2.setBackground(new java.awt.Color(204, 204, 204));
        jPanel2.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        userdash.setFont(new java.awt.Font("Century Gothic", 0, 14)); // NOI18N
        userdash.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        userdash.setText("DASHBOARD");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(userdash, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(userdash, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 26, Short.MAX_VALUE)
        );

        jPanel1.add(jPanel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 160, 120, 30));

        jPanel4.setBackground(new java.awt.Color(204, 204, 204));
        jPanel4.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        userview.setFont(new java.awt.Font("Century Gothic", 0, 14)); // NOI18N
        userview.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        userview.setText("VIEW BOOKS");

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addComponent(userview, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(userview, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 26, Short.MAX_VALUE)
        );

        jPanel1.add(jPanel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 190, 120, 30));

        jPanel5.setBackground(new java.awt.Color(204, 204, 204));
        jPanel5.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        userborrow.setFont(new java.awt.Font("Century Gothic", 0, 14)); // NOI18N
        userborrow.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        userborrow.setText("BORROW");

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(userborrow, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 116, Short.MAX_VALUE)
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(userborrow, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 26, Short.MAX_VALUE)
        );

        jPanel1.add(jPanel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 220, 120, 30));

        jPanel6.setBackground(new java.awt.Color(0, 51, 102));
        jPanel6.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        userusername.setFont(new java.awt.Font("Century Gothic", 1, 16)); // NOI18N
        userusername.setForeground(new java.awt.Color(255, 255, 255));
        userusername.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        userusername.setText("USERNAME");

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel6Layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(userusername, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel6Layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(userusername, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        jPanel1.add(jPanel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 80, 120, 30));

        jPanel3.setBackground(new java.awt.Color(204, 204, 204));
        jPanel3.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        userhistory.setFont(new java.awt.Font("Century Gothic", 0, 14)); // NOI18N
        userhistory.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        userhistory.setText("HISTORY");

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(userhistory, javax.swing.GroupLayout.PREFERRED_SIZE, 116, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(userhistory, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        jPanel1.add(jPanel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 280, 120, -1));

        jPanel7.setBackground(new java.awt.Color(204, 51, 0));
        jPanel7.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        userlogout.setFont(new java.awt.Font("Century Gothic", 1, 12)); // NOI18N
        userlogout.setForeground(new java.awt.Color(255, 255, 255));
        userlogout.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        userlogout.setText("LOGOUT");

        javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel7Layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(userlogout, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addComponent(userlogout, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );

        jPanel1.add(jPanel7, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 470, 120, 30));

        userreturn.setFont(new java.awt.Font("Century Gothic", 0, 14)); // NOI18N
        userreturn.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        userreturn.setText("RETURN");
        jPanel1.add(userreturn, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 250, 116, 30));

        jPanel8.setBackground(new java.awt.Color(204, 204, 204));
        jPanel8.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        javax.swing.GroupLayout jPanel8Layout = new javax.swing.GroupLayout(jPanel8);
        jPanel8.setLayout(jPanel8Layout);
        jPanel8Layout.setHorizontalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 116, Short.MAX_VALUE)
        );
        jPanel8Layout.setVerticalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 26, Short.MAX_VALUE)
        );

        jPanel1.add(jPanel8, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 250, 120, 30));

        jLabel1.setIcon(new javax.swing.ImageIcon("C:\\Users\\ADMIN\\Documents\\NetBeansProjects\\MyLibrary\\src\\das (1).png")); // NOI18N
        jLabel1.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jPanel1.add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(-10, 0, 130, -1));

        userreturn2.setFont(new java.awt.Font("Century Gothic", 0, 14)); // NOI18N
        userreturn2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        userreturn2.setText("RETURN");
        jPanel1.add(userreturn2, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 330, 100, 26));

        getContentPane().add(jPanel1);
        jPanel1.setBounds(0, 0, 120, 500);

        jLabel2.setFont(new java.awt.Font("Century Gothic", 1, 24)); // NOI18N
        jLabel2.setText("User Information");
        getContentPane().add(jLabel2);
        jLabel2.setBounds(360, 10, 200, 30);

        closeuserbtn.setBackground(new java.awt.Color(204, 51, 0));
        closeuserbtn.setFont(new java.awt.Font("Century Gothic", 1, 14)); // NOI18N
        closeuserbtn.setForeground(new java.awt.Color(255, 255, 255));
        closeuserbtn.setText("Close");
        closeuserbtn.setBorder(null);
        closeuserbtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                closeuserbtnActionPerformed(evt);
            }
        });
        getContentPane().add(closeuserbtn);
        closeuserbtn.setBounds(690, 460, 90, 25);

        changeiconbtn.setBackground(new java.awt.Color(204, 204, 255));
        changeiconbtn.setFont(new java.awt.Font("Century Gothic", 1, 11)); // NOI18N
        changeiconbtn.setText("Change Icon");
        changeiconbtn.setBorder(null);
        changeiconbtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                changeiconbtnActionPerformed(evt);
            }
        });
        getContentPane().add(changeiconbtn);
        changeiconbtn.setBounds(390, 150, 130, 20);

        jPanel10.setBackground(new java.awt.Color(210, 232, 237));

        jLabel5.setFont(new java.awt.Font("Century Gothic", 0, 12)); // NOI18N
        jLabel5.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel5.setText("Email");

        javax.swing.GroupLayout jPanel10Layout = new javax.swing.GroupLayout(jPanel10);
        jPanel10.setLayout(jPanel10Layout);
        jPanel10Layout.setHorizontalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel10Layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        jPanel10Layout.setVerticalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel10Layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        getContentPane().add(jPanel10);
        jPanel10.setBounds(190, 260, 120, 30);

        jPanel11.setBackground(new java.awt.Color(210, 232, 237));

        jLabel9.setFont(new java.awt.Font("Century Gothic", 0, 12)); // NOI18N
        jLabel9.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel9.setText("Old Password");

        javax.swing.GroupLayout jPanel11Layout = new javax.swing.GroupLayout(jPanel11);
        jPanel11.setLayout(jPanel11Layout);
        jPanel11Layout.setHorizontalGroup(
            jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel11Layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(jLabel9, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        jPanel11Layout.setVerticalGroup(
            jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel11Layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(jLabel9, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        getContentPane().add(jPanel11);
        jPanel11.setBounds(190, 340, 120, 30);

        jPanel13.setBackground(new java.awt.Color(210, 232, 237));

        jLabel6.setFont(new java.awt.Font("Century Gothic", 0, 12)); // NOI18N
        jLabel6.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel6.setText("Username ");

        javax.swing.GroupLayout jPanel13Layout = new javax.swing.GroupLayout(jPanel13);
        jPanel13.setLayout(jPanel13Layout);
        jPanel13Layout.setHorizontalGroup(
            jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel13Layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        jPanel13Layout.setVerticalGroup(
            jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel13Layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        getContentPane().add(jPanel13);
        jPanel13.setBounds(190, 300, 120, 30);

        jPanel14.setBackground(new java.awt.Color(210, 232, 237));

        jLabel10.setFont(new java.awt.Font("Century Gothic", 0, 12)); // NOI18N
        jLabel10.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel10.setText("New Password");

        javax.swing.GroupLayout jPanel14Layout = new javax.swing.GroupLayout(jPanel14);
        jPanel14.setLayout(jPanel14Layout);
        jPanel14Layout.setHorizontalGroup(
            jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel14Layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(jLabel10, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        jPanel14Layout.setVerticalGroup(
            jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel14Layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(jLabel10, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        getContentPane().add(jPanel14);
        jPanel14.setBounds(190, 380, 120, 30);

        jLabel4.setFont(new java.awt.Font("Century Gothic", 0, 12)); // NOI18N
        jLabel4.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel4.setText("Full Name");
        getContentPane().add(jLabel4);
        jLabel4.setBounds(190, 220, 120, 30);

        jPanel12.setBackground(new java.awt.Color(210, 232, 237));

        javax.swing.GroupLayout jPanel12Layout = new javax.swing.GroupLayout(jPanel12);
        jPanel12.setLayout(jPanel12Layout);
        jPanel12Layout.setHorizontalGroup(
            jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 120, Short.MAX_VALUE)
        );
        jPanel12Layout.setVerticalGroup(
            jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 30, Short.MAX_VALUE)
        );

        getContentPane().add(jPanel12);
        jPanel12.setBounds(190, 220, 120, 30);

        jPanel15.setBackground(new java.awt.Color(210, 232, 237));

        jLabel3.setFont(new java.awt.Font("Century Gothic", 0, 12)); // NOI18N
        jLabel3.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel3.setText("Confirm Password");

        javax.swing.GroupLayout jPanel15Layout = new javax.swing.GroupLayout(jPanel15);
        jPanel15.setLayout(jPanel15Layout);
        jPanel15Layout.setHorizontalGroup(
            jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel15Layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        jPanel15Layout.setVerticalGroup(
            jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel15Layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        getContentPane().add(jPanel15);
        jPanel15.setBounds(190, 420, 120, 30);

        jPanel17.setBackground(new java.awt.Color(207, 218, 246));

        jLabel16.setFont(new java.awt.Font("Century Gothic", 0, 14)); // NOI18N
        jLabel16.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel16.setText("Borrowed Books:");

        javax.swing.GroupLayout jPanel17Layout = new javax.swing.GroupLayout(jPanel17);
        jPanel17.setLayout(jPanel17Layout);
        jPanel17Layout.setHorizontalGroup(
            jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel17Layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(jLabel16, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        jPanel17Layout.setVerticalGroup(
            jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel17Layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(jLabel16, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        getContentPane().add(jPanel17);
        jPanel17.setBounds(650, 260, 120, 30);

        usernamefield.setEditable(false);
        usernamefield.setBackground(new java.awt.Color(218, 233, 236));
        usernamefield.setFont(new java.awt.Font("Century Gothic", 0, 12)); // NOI18N
        usernamefield.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                usernamefieldActionPerformed(evt);
            }
        });
        getContentPane().add(usernamefield);
        usernamefield.setBounds(310, 300, 250, 30);

        userid_fld.setBackground(new java.awt.Color(204, 204, 255));
        userid_fld.setFont(new java.awt.Font("Century Gothic", 0, 12)); // NOI18N
        userid_fld.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        userid_fld.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                userid_fldActionPerformed(evt);
            }
        });
        getContentPane().add(userid_fld);
        userid_fld.setBounds(310, 180, 30, 30);

        emailfield.setBackground(new java.awt.Color(218, 233, 236));
        emailfield.setFont(new java.awt.Font("Century Gothic", 0, 12)); // NOI18N
        emailfield.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                emailfieldActionPerformed(evt);
            }
        });
        getContentPane().add(emailfield);
        emailfield.setBounds(310, 260, 250, 30);

        oldpassfield.setBackground(new java.awt.Color(218, 233, 236));
        oldpassfield.setFont(new java.awt.Font("Century Gothic", 0, 12)); // NOI18N
        oldpassfield.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                oldpassfieldActionPerformed(evt);
            }
        });
        getContentPane().add(oldpassfield);
        oldpassfield.setBounds(310, 340, 250, 30);

        newpassfield.setBackground(new java.awt.Color(218, 233, 236));
        newpassfield.setFont(new java.awt.Font("Century Gothic", 0, 12)); // NOI18N
        newpassfield.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                newpassfieldActionPerformed(evt);
            }
        });
        getContentPane().add(newpassfield);
        newpassfield.setBounds(310, 380, 250, 30);

        confirmpassfield.setBackground(new java.awt.Color(218, 233, 236));
        confirmpassfield.setFont(new java.awt.Font("Century Gothic", 0, 12)); // NOI18N
        confirmpassfield.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                confirmpassfieldActionPerformed(evt);
            }
        });
        getContentPane().add(confirmpassfield);
        confirmpassfield.setBounds(310, 420, 250, 30);

        deactbtn.setBackground(new java.awt.Color(204, 204, 204));
        deactbtn.setFont(new java.awt.Font("Century Gothic", 1, 14)); // NOI18N
        deactbtn.setText("Deactivate");
        deactbtn.setBorder(null);
        deactbtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                deactbtnActionPerformed(evt);
            }
        });
        getContentPane().add(deactbtn);
        deactbtn.setBounds(360, 460, 90, 25);

        updatebtn.setBackground(new java.awt.Color(204, 204, 255));
        updatebtn.setFont(new java.awt.Font("Century Gothic", 1, 14)); // NOI18N
        updatebtn.setText("Update");
        updatebtn.setBorder(null);
        updatebtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                updatebtnActionPerformed(evt);
            }
        });
        getContentPane().add(updatebtn);
        updatebtn.setBounds(270, 460, 90, 25);

        jLabel14.setFont(new java.awt.Font("Century Gothic", 0, 14)); // NOI18N
        jLabel14.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel14.setText("USER ID");
        getContentPane().add(jLabel14);
        jLabel14.setBounds(190, 180, 120, 30);

        jPanel16.setBackground(new java.awt.Color(210, 232, 237));

        javax.swing.GroupLayout jPanel16Layout = new javax.swing.GroupLayout(jPanel16);
        jPanel16.setLayout(jPanel16Layout);
        jPanel16Layout.setHorizontalGroup(
            jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 120, Short.MAX_VALUE)
        );
        jPanel16Layout.setVerticalGroup(
            jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 30, Short.MAX_VALUE)
        );

        getContentPane().add(jPanel16);
        jPanel16.setBounds(190, 180, 120, 30);

        fullnamefld.setBackground(new java.awt.Color(218, 233, 236));
        fullnamefld.setFont(new java.awt.Font("Century Gothic", 0, 12)); // NOI18N
        fullnamefld.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                fullnamefldActionPerformed(evt);
            }
        });
        getContentPane().add(fullnamefld);
        fullnamefld.setBounds(310, 220, 250, 30);

        jPanel18.setBackground(new java.awt.Color(207, 218, 246));

        jLabel13.setFont(new java.awt.Font("Century Gothic", 0, 14)); // NOI18N
        jLabel13.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel13.setText("Return Books:");

        javax.swing.GroupLayout jPanel18Layout = new javax.swing.GroupLayout(jPanel18);
        jPanel18.setLayout(jPanel18Layout);
        jPanel18Layout.setHorizontalGroup(
            jPanel18Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel18Layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(jLabel13, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        jPanel18Layout.setVerticalGroup(
            jPanel18Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel18Layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(jLabel13, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        getContentPane().add(jPanel18);
        jPanel18.setBounds(650, 290, 120, 30);

        jPanel19.setBackground(new java.awt.Color(51, 51, 51));

        jLabel15.setFont(new java.awt.Font("Century Gothic", 1, 14)); // NOI18N
        jLabel15.setForeground(new java.awt.Color(255, 255, 255));
        jLabel15.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel15.setText("USER STATUS");

        javax.swing.GroupLayout jPanel19Layout = new javax.swing.GroupLayout(jPanel19);
        jPanel19.setLayout(jPanel19Layout);
        jPanel19Layout.setHorizontalGroup(
            jPanel19Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel15, javax.swing.GroupLayout.DEFAULT_SIZE, 120, Short.MAX_VALUE)
        );
        jPanel19Layout.setVerticalGroup(
            jPanel19Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel19Layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(jLabel15, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        getContentPane().add(jPanel19);
        jPanel19.setBounds(650, 220, 120, 30);

        jPanel20.setBackground(new java.awt.Color(218, 233, 236));

        numberborrowed.setFont(new java.awt.Font("Century Gothic", 1, 18)); // NOI18N
        numberborrowed.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        numberborrowed.setText("0");

        javax.swing.GroupLayout jPanel20Layout = new javax.swing.GroupLayout(jPanel20);
        jPanel20.setLayout(jPanel20Layout);
        jPanel20Layout.setHorizontalGroup(
            jPanel20Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel20Layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(numberborrowed, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        jPanel20Layout.setVerticalGroup(
            jPanel20Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel20Layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(numberborrowed, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        getContentPane().add(jPanel20);
        jPanel20.setBounds(770, 260, 50, 30);

        jPanel21.setBackground(new java.awt.Color(218, 233, 236));

        numberreturned.setBackground(new java.awt.Color(218, 233, 236));
        numberreturned.setFont(new java.awt.Font("Century Gothic", 1, 18)); // NOI18N
        numberreturned.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        numberreturned.setText("0");

        javax.swing.GroupLayout jPanel21Layout = new javax.swing.GroupLayout(jPanel21);
        jPanel21.setLayout(jPanel21Layout);
        jPanel21Layout.setHorizontalGroup(
            jPanel21Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel21Layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(numberreturned, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        jPanel21Layout.setVerticalGroup(
            jPanel21Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel21Layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(numberreturned, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        getContentPane().add(jPanel21);
        jPanel21.setBounds(770, 290, 50, 30);

        jLabel11.setIcon(new javax.swing.ImageIcon("C:\\Users\\ADMIN\\Documents\\NetBeansProjects\\MyLibrary\\src\\1.png")); // NOI18N
        jLabel11.setText("jLabel11");
        getContentPane().add(jLabel11);
        jLabel11.setBounds(120, 0, 1400, 500);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    
    private void closeuserbtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_closeuserbtnActionPerformed
        // TODO add your handling code here:
        new LibDashboard(userId, username).setVisible(true); 
        dispose();
        
    }//GEN-LAST:event_closeuserbtnActionPerformed

    private void changeiconbtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_changeiconbtnActionPerformed
          // TODO add your handling code here
        
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Select a Profile Picture");

        // Set the initial directory to src/icons
        fileChooser.setCurrentDirectory(new File("src/icons"));

        // File filter for image files
        fileChooser.setFileFilter(new FileNameExtensionFilter("Image Files", "png", "jpg", "jpeg", "gif"));

        // Create an instance of ImagePreview and set it as the accessory
        ImagePreview preview = new ImagePreview(fileChooser);
        fileChooser.setAccessory(preview);

        // Show the file chooser dialog
        int result = fileChooser.showOpenDialog(this);  // Use 'this' to refer to the current frame

        if (result == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();

            // Check file size (limit to 5 MB)
            if (selectedFile.length() > 5 * 1024 * 1024) {
                JOptionPane.showMessageDialog(null, "Image file is too large. Please select a file smaller than 5MB.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Create the ImageIcon from the selected file
            ImageIcon icon = new ImageIcon(selectedFile.getAbsolutePath());

            // Scale the image to fit both JLabels
            Image scaledImage = icon.getImage().getScaledInstance(iconuser.getWidth(), iconuser.getHeight(), Image.SCALE_SMOOTH);
            Image scaledImage2 = icon.getImage().getScaledInstance(usericon.getWidth(), usericon.getHeight(), Image.SCALE_SMOOTH);

            // Set the scaled image to both JLabels
            iconuser.setIcon(new ImageIcon(scaledImage));
            usericon.setIcon(new ImageIcon(scaledImage2));

            // Save the selected icon to the database as a Blob
            saveIconPathToDatabase(selectedFile);

            // Reload the profile icon to update across frames
            loadUserProfileIcon();
        }
    }//GEN-LAST:event_changeiconbtnActionPerformed

    private void saveIconPathToDatabase(File selectedFile) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        FileInputStream fis = null;

        try {
            conn = connect();  // Your method to connect to the database
            String query = "UPDATE users SET icon=? WHERE user_id=?";  // Adjust column name as necessary
            pstmt = conn.prepareStatement(query);

            // Read the image file and convert it to a Blob
            fis = new FileInputStream(selectedFile);
            pstmt.setBlob(1, fis);  // Set the Blob
            pstmt.setInt(2, userId);  // Assuming 'userId' is the current logged-in user's ID

            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected > 0) {
                JOptionPane.showMessageDialog(null, "Icon updated successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(null, "No user found with the given user ID.", "Warning", JOptionPane.WARNING_MESSAGE);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Database error while saving icon: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        } catch (IOException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error reading selected file: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        } finally {
            // Clean up resources
            try {
                if (fis != null) {
                    fis.close(); // Close the FileInputStream
                }
                if (pstmt != null) {
                    pstmt.close();
                }
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException | IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void loadUserProfileIcon() {
        Connection conn = null;
        try {
            // Use your DbConnection class to get a connection
            conn = DbConnection.getConnection();  // Adjust this according to your method to get a Connection
            String query = "SELECT icon FROM users WHERE user_id=?";
            PreparedStatement pstmt = conn.prepareStatement(query);
            pstmt.setInt(1, userId);  // Assuming 'userId' is the current logged-in user's ID
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                // Assuming the icon is stored as a Blob in the database
                java.sql.Blob blob = rs.getBlob("icon");
                if (blob != null) {
                    ImageIcon icon = new ImageIcon(blob.getBytes(1, (int) blob.length()));

                    // Scale the image to fit both JLabels
                    Image scaledImage = icon.getImage().getScaledInstance(iconuser.getWidth(), iconuser.getHeight(), Image.SCALE_SMOOTH);
                    iconuser.setIcon(new ImageIcon(scaledImage));

                    // Scale for usericon to the same size
                    Image scaledIconUserImage = icon.getImage().getScaledInstance(usericon.getWidth(), usericon.getHeight(), Image.SCALE_SMOOTH);
                    usericon.setIcon(new ImageIcon(scaledIconUserImage));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error loading profile icon", "Error", JOptionPane.ERROR_MESSAGE);
        } finally {
            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void updateAllFramesIcons(String iconPath) {
        // Update icon in LibDashboard
        if (LibDashboard.getInstance(userId, username) != null) {
            LibDashboard.getInstance(userId, username).updateIconInLibDashboard(iconPath);
        }

        // Update icon in LibView
        if (LibView.getInstance(userId, username) != null) {
            LibView.getInstance(userId, username).updateIconInLibView(iconPath);
        }

        // Update icon in LibBorrow
        if (LibBorrow.getInstance(userId, username) != null) {
            LibBorrow.getInstance(userId, username).updateIconInLibBorrow(iconPath);
        }

        // Update icon in LibReturn
        if (LibReturn.getInstance(userId, username) != null) {
            LibReturn.getInstance(userId, username).updateIconInLibReturn(iconPath);
        }

        // Update icon in LibHistory
        if (LibHistory.getInstance(userId, username) != null) {
            LibHistory.getInstance(userId, username).updateIconInLibHistory(iconPath);
        }
    }




    
 
 
    
    private void confirmpassfieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_confirmpassfieldActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_confirmpassfieldActionPerformed

    private void newpassfieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_newpassfieldActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_newpassfieldActionPerformed

    private void userid_fldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_userid_fldActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_userid_fldActionPerformed

    private void deactbtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_deactbtnActionPerformed
        // TODO add your handling code here:
        int response = JOptionPane.showConfirmDialog(this, "Are you sure you want to deactivate your account?", "Deactivate Account", JOptionPane.YES_NO_OPTION);
        if (response == JOptionPane.YES_OPTION) {
            // Check for pending borrowed books
            int borrowedCount = getPendingBorrowedBooks(userId); // This method should return the count of pending borrowed books
            if (borrowedCount > 0) {
                JOptionPane.showMessageDialog(this, "You cannot deactivate your account while you have pending borrowed books.", "Deactivation Failed", JOptionPane.WARNING_MESSAGE);
                return; // Exit the method if there are pending books
            }

            // Connect to the database and deactivate the account
            Connection conn = null;
            try {
                conn = connect(); // Get the connection
                if (conn != null) {
                    String query = "UPDATE users SET active=0 WHERE user_id=?"; // Ensure the column name matches your database
                    PreparedStatement pstmt = conn.prepareStatement(query);
                    pstmt.setInt(1, userId);
                    pstmt.executeUpdate();
                    JOptionPane.showMessageDialog(this, "Account deactivated successfully. You will be logged out.");

                    // Log out the user
                    new LibLogin().setVisible(true);
                    dispose();
                } else {
                    JOptionPane.showMessageDialog(this, "Failed to connect to the database.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } catch (SQLException e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(this, "Error deactivating account", "Error", JOptionPane.ERROR_MESSAGE);
            } finally {
                // Close the connection if it was opened
                if (conn != null) {
                    try {
                        conn.close();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }//GEN-LAST:event_deactbtnActionPerformed

    private int getPendingBorrowedBooks(int userId) {
        int pendingCount = 0; // Initialize the count
        Connection conn = null;
        try {
            conn = connect(); // Get the database connection
            if (conn != null) {
                // SQL query to count pending borrowed books
                String query = "SELECT COUNT(*) FROM transactions WHERE user_id = ? AND return_date IS NULL";
                PreparedStatement pstmt = conn.prepareStatement(query);
                pstmt.setInt(1, userId);
                ResultSet rs = pstmt.executeQuery();

                if (rs.next()) {
                    pendingCount = rs.getInt(1); // Get the count from the result set
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            // Close the connection if it was opened
            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        return pendingCount; // Return the count of pending borrowed books
    }
    
    private void updatebtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_updatebtnActionPerformed
        // TODO add your handling code here:
        String newUsername = usernamefield.getText();
        String newFullName = fullnamefld.getText();
        String newEmail = emailfield.getText();
        String newPassword = new String(newpassfield.getPassword()); // Assuming you have a JPasswordField named newpassfield
        String confirmPassword = new String(confirmpassfield.getPassword()); // Assuming you have a JPasswordField named confirmpassfield

        // Check if password fields are empty
        boolean isPasswordChange = !newPassword.isEmpty() && !confirmPassword.isEmpty();

        // If password fields are not empty, check if passwords match
        if (isPasswordChange && !newPassword.equals(confirmPassword)) {
            JOptionPane.showMessageDialog(this, "Passwords do not match! Please try again.", "Error", JOptionPane.ERROR_MESSAGE);
            return; // Stop further execution if passwords don't match
        }

        // Confirm update action
        int response = JOptionPane.showConfirmDialog(this, "Are you sure you want to update your account information?", "Update Account", JOptionPane.YES_NO_OPTION);
        if (response == JOptionPane.YES_OPTION) {
            Connection conn = null;
            try {
                conn = connect(); // Get the connection
                if (conn != null) {
                    String query;
                    PreparedStatement pstmt;

                    // Prepare SQL statement based on whether password is being changed
                    if (isPasswordChange) {
                        // If the password is being changed
                        query = "UPDATE users SET username=?, full_name=?, email=?, password=? WHERE user_id=?";
                        pstmt = conn.prepareStatement(query);

                        // Set parameters for the prepared statement
                        pstmt.setString(1, newUsername);
                        pstmt.setString(2, newFullName);
                        pstmt.setString(3, newEmail);
                        pstmt.setString(4, newPassword); // Add the new password (ensure hashing if needed)
                        pstmt.setInt(5, userId); // Assuming userId is the ID of the logged-in user
                    } else {
                        // If the password is not being changed
                        query = "UPDATE users SET username=?, full_name=?, email=? WHERE user_id=?";
                        pstmt = conn.prepareStatement(query);

                        // Set parameters for the prepared statement (excluding password)
                        pstmt.setString(1, newUsername);
                        pstmt.setString(2, newFullName);
                        pstmt.setString(3, newEmail);
                        pstmt.setInt(4, userId); // Assuming userId is the ID of the logged-in user
                    }

                    // Execute the update
                    int rowsUpdated = pstmt.executeUpdate();
                    if (rowsUpdated > 0) {
                        JOptionPane.showMessageDialog(this, "Account information updated successfully.");
                    } else {
                        JOptionPane.showMessageDialog(this, "No changes were made to the account.");
                    }

                } else {
                    JOptionPane.showMessageDialog(this, "Failed to connect to the database.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } catch (SQLException e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(this, "Error updating account information", "Error", JOptionPane.ERROR_MESSAGE);
            } finally {
                // Close the connection if it was opened
                if (conn != null) {
                    try {
                        conn.close();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    
    
    }//GEN-LAST:event_updatebtnActionPerformed

    private void oldpassfieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_oldpassfieldActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_oldpassfieldActionPerformed

    private void fullnamefldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_fullnamefldActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_fullnamefldActionPerformed

    private void emailfieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_emailfieldActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_emailfieldActionPerformed

    private void usernamefieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_usernamefieldActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_usernamefieldActionPerformed

    
  
    
    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(LibUsersInfo.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(LibUsersInfo.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(LibUsersInfo.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(LibUsersInfo.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                 
                int userId = 0;
                String username = "Default User";
                new LibUsersInfo(userId, username).setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton changeiconbtn;
    private javax.swing.JButton closeuserbtn;
    private javax.swing.JPasswordField confirmpassfield;
    private javax.swing.JButton deactbtn;
    private javax.swing.JTextField emailfield;
    private javax.swing.JTextField fullnamefld;
    private javax.swing.JLabel iconuser;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel10;
    private javax.swing.JPanel jPanel11;
    private javax.swing.JPanel jPanel12;
    private javax.swing.JPanel jPanel13;
    private javax.swing.JPanel jPanel14;
    private javax.swing.JPanel jPanel15;
    private javax.swing.JPanel jPanel16;
    private javax.swing.JPanel jPanel17;
    private javax.swing.JPanel jPanel18;
    private javax.swing.JPanel jPanel19;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel20;
    private javax.swing.JPanel jPanel21;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JPasswordField newpassfield;
    private javax.swing.JLabel numberborrowed;
    private javax.swing.JLabel numberreturned;
    private javax.swing.JPasswordField oldpassfield;
    private javax.swing.JButton updatebtn;
    private javax.swing.JLabel userborrow;
    private javax.swing.JLabel userdash;
    private javax.swing.JLabel userhistory;
    private javax.swing.JLabel usericon;
    private javax.swing.JTextField userid_fld;
    private javax.swing.JLabel userlogout;
    private javax.swing.JTextField usernamefield;
    private javax.swing.JLabel userreturn;
    private javax.swing.JLabel userreturn2;
    private javax.swing.JLabel userusername;
    private javax.swing.JLabel userview;
    // End of variables declaration//GEN-END:variables
}
