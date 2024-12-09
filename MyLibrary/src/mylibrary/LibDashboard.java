/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package mylibrary;

import java.awt.Image;
import java.sql.Connection; // Correct import for java.sql.Connection
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 *
 * @author ADMIN
 */
public class LibDashboard extends javax.swing.JFrame {
     private static LibDashboard instance;
     private static String DEFAULT_ICON_PATH = "src/icons/defaulticon.png";

  
    private String username;
    private final  int userId;
    
    

    // Constructor
    public LibDashboard(int userId, String username) {
        this.userId =userId;
        this.username = username;
        initComponents(); // Initialize components
        setLocationRelativeTo(null); // Center the frame
        welcome_lbl.setText("Welcome to Bookworm Central, " + username.substring(0, 1).toUpperCase() + username.substring(1).toLowerCase()); 
        usernameLabel.setText(username.toUpperCase()); // Set username label
      
        displayLetter();
        
        String userIconPath = getUserIconPath(userId);  // Get the user icon path from your database method

        // Update the dashicon in LibDashboard
        updateIconInLibDashboard(userIconPath);
        loadUserIcon(userId);
        
        
        
       
       
      

        // Add action listeners for navigation
        logoutlabel.addMouseListener(new MouseAdapter() {
            @Override
             public void mouseClicked(MouseEvent e) {
            int response = JOptionPane.showConfirmDialog(
            LibDashboard.this, 
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

        dashviewlabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                new LibView(userId,username).setVisible(true); // Open LibView JFrame
                dispose(); // Close the dashboard
            }
        });
        
        viewlabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                new LibView(userId,username).setVisible(true); // Open LibView JFrame
                dispose(); // Close the dashboard
            }
        });
        
        imgviewLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                new LibView(userId, username).setVisible(true); // Open LibView JFrame 
                dispose(); // Close the dashboard
            }
        });

        borrowlabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                new LibBorrow(userId, username).setVisible(true); // Open LibBorrow JFrame
                dispose(); // Close the dashboard
            }
        });

        dashhistory.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                new LibHistory(userId, username).setVisible(true);
                dispose();

            }
        });
        
        historylabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                new LibHistory(userId, username).setVisible(true); 
                dispose(); 
               
            }
        });
        imghistorylabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                new LibHistory(userId, username).setVisible(true);
                dispose();

            }
        });
                
        imgreturnL.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                new LibReturn(userId, username).setVisible(true); 
                dispose(); 
            }
        });

        imgborrowL.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                new LibBorrow(userId, username).setVisible(true); 
                dispose(); 
            }
        });
        
        dashborrow.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                new LibBorrow(userId,username).setVisible(true); 
                dispose();
            }
        });

        dashreturn.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                new LibReturn(userId, username).setVisible(true); 
                dispose(); 
            }
        });
        
        imguserprofileL.addMouseListener(new MouseAdapter(){
             @Override
            public void mouseClicked(MouseEvent e) {
                new LibUsersInfo(userId,username).setVisible(true); 
                dispose(); 
            }
        });
        
        dashuserprofile.addMouseListener(new MouseAdapter(){
             @Override
            public void mouseClicked(MouseEvent e) {
                new LibUsersInfo(userId,username).setVisible(true); // Open LibUserInfo JFrame
                dispose(); // Close the dashboard
            }
        });
        
        usernameLabel.addMouseListener(new MouseAdapter(){
             @Override
            public void mouseClicked(MouseEvent e) {
                new LibUsersInfo(userId,username).setVisible(true); // Open LibUserInfo JFrame
                dispose(); // Close the dashboard
            }
        });
        
        returnlabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                new LibReturn(userId, username).setVisible(true); // Open LibView JFrame
                dispose(); // Close the dashboard
            }
        });
    }
    
    public String getFullName(int userId) {
        String fullName = " Full Name";
        try (Connection connection = DbConnection.getConnection()) { // Handle exception here
            String query = "SELECT full_name FROM users WHERE user_id = ?"; // Adjust the query as needed

            try (PreparedStatement pstmt = connection.prepareStatement(query)) {
                pstmt.setInt(1, userId); // Set the user ID parameter
                ResultSet rs = pstmt.executeQuery();

                if (rs.next()) {
                    fullName = rs.getString("full_name"); // Retrieve the full name from the result set
                }
            }
        } catch (SQLException e) {
            e.printStackTrace(); // Handle exceptions
        }
        return fullName; // Return the retrieved full name
    }

    public void displayLetter() {
        String fullName = getFullName(userId); // Fetch the full name using the user ID

        // LETTER //
        llibrarianletter.setText("<html><body style='text-align: justify; margin-right: 70px; margin-left: 10px;'"
                + "<p>Dear " + fullName + ",</p>" // Use full_name instead of username
                + "<p style='text-indent: 20px;'>Welcome to Bookworm Central! I'm thrilled that you’ve chosen to explore our shelves. Here, you'll find knowledge, adventure, and worlds beyond imagination awaiting you in every book. Don't hesitate to ask for help if you need it – after all, a good book is best enjoyed when shared.</p>"
                + "<p style='text-indent: 20px;'>Happy reading, and remember, each page turned is a step toward a new discovery.</p>"
                + "<p><p>Warm regards,<br><b>Nico Robin</b><br>Librarian</p>"
                + "</body></html>");
    }
    
    public static LibDashboard getInstance(int userId, String username) {
        if (instance == null) {
            instance = new LibDashboard(userId, username);
        }
        return instance;
    }
    
    

    public void updateIconInLibDashboard(String iconPath) {
        ImageIcon icon = new ImageIcon(iconPath);
        Image scaledImage = icon.getImage().getScaledInstance(dashicon.getWidth(), dashicon.getHeight(), Image.SCALE_SMOOTH);
        dashicon.setIcon(new ImageIcon(scaledImage));
        dashicon.revalidate();
        dashicon.repaint();
    }

    public String getUserIconPath(int userId) {
        DbConnection db = new DbConnection();
        String iconPath = db.getUserIconPath(userId); // Fetch icon path from the database
        return (iconPath == null || iconPath.isEmpty()) ? DEFAULT_ICON_PATH : iconPath; // Default if no icon is found
    }
    
    public void loadUserIcon(int userId) {
        // Attempt to retrieve the user's icon
        ImageIcon icon = UserIconUtility.retrieveUserIcon(userId);

        // Check if a custom icon was found
        if (icon != null) {
            // Scale and set the custom icon
            Image scaledImage = icon.getImage().getScaledInstance(dashicon.getWidth(), dashicon.getHeight(), Image.SCALE_SMOOTH);
            dashicon.setIcon(new ImageIcon(scaledImage));
        } else {
            // If no custom icon is found, set the default icon
            String DEFAULT_ICON_PATH = "src/icons/defaulticon.png"; // Adjust the path accordingly
            ImageIcon defaultIcon = new ImageIcon(DEFAULT_ICON_PATH);
            Image scaledDefaultImage = defaultIcon.getImage().getScaledInstance(dashicon.getWidth(), dashicon.getHeight(), Image.SCALE_SMOOTH);
            dashicon.setIcon(new ImageIcon(scaledDefaultImage));
        }
    }
    
   


    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        usernameLabel = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        dashlabel = new javax.swing.JLabel();
        jPanel3 = new javax.swing.JPanel();
        dashicon = new javax.swing.JLabel();
        historylabel = new javax.swing.JLabel();
        jPanel8 = new javax.swing.JPanel();
        returnlabel = new javax.swing.JLabel();
        jPanel4 = new javax.swing.JPanel();
        viewlabel = new javax.swing.JLabel();
        jPanel5 = new javax.swing.JPanel();
        borrowlabel = new javax.swing.JLabel();
        jPanel6 = new javax.swing.JPanel();
        jPanel7 = new javax.swing.JPanel();
        logoutlabel = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        welcome_lbl = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        jPanel9 = new javax.swing.JPanel();
        llibrarianletter = new javax.swing.JLabel();
        imghistorylabel = new javax.swing.JLabel();
        imgviewLabel = new javax.swing.JLabel();
        imgreturnL = new javax.swing.JLabel();
        imgborrowL = new javax.swing.JLabel();
        jPanel10 = new javax.swing.JPanel();
        dashhistory = new javax.swing.JLabel();
        jPanel14 = new javax.swing.JPanel();
        dashuserprofile = new javax.swing.JLabel();
        jPanel11 = new javax.swing.JPanel();
        dashviewlabel = new javax.swing.JLabel();
        jPanel12 = new javax.swing.JPanel();
        dashreturn = new javax.swing.JLabel();
        jPanel13 = new javax.swing.JPanel();
        dashborrow = new javax.swing.JLabel();
        imguserprofileL = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Dashboard");
        setBackground(new java.awt.Color(204, 255, 255));
        setMinimumSize(new java.awt.Dimension(900, 540));
        setPreferredSize(new java.awt.Dimension(900, 540));
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel1.setBackground(new java.awt.Color(210, 232, 237));
        jPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        usernameLabel.setFont(new java.awt.Font("Century Gothic", 0, 12)); // NOI18N
        usernameLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        usernameLabel.setText("USERNAME");
        jPanel1.add(usernameLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 80, 120, 20));

        jPanel2.setBackground(new java.awt.Color(0, 51, 102));
        jPanel2.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jPanel2.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        dashlabel.setBackground(new java.awt.Color(255, 102, 0));
        dashlabel.setFont(new java.awt.Font("Century Gothic", 1, 16)); // NOI18N
        dashlabel.setForeground(new java.awt.Color(255, 255, 255));
        dashlabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        dashlabel.setText("DASHBOARD");
        jPanel2.add(dashlabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 120, 30));

        jPanel1.add(jPanel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 130, 120, 30));

        jPanel3.setLayout(new java.awt.BorderLayout());

        dashicon.setIcon(new javax.swing.ImageIcon("C:\\Users\\ADMIN\\Documents\\NetBeansProjects\\MyLibrary\\src\\icons\\defaulticon.png")); // NOI18N
        dashicon.setPreferredSize(new java.awt.Dimension(120, 90));
        jPanel3.add(dashicon, java.awt.BorderLayout.CENTER);

        jPanel1.add(jPanel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 120, 80));

        historylabel.setFont(new java.awt.Font("Century Gothic", 0, 14)); // NOI18N
        historylabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        historylabel.setText("HISTORY");
        jPanel1.add(historylabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 250, 120, 30));

        jPanel8.setBackground(new java.awt.Color(204, 204, 204));
        jPanel8.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jPanel1.add(jPanel8, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 250, 120, 30));

        returnlabel.setFont(new java.awt.Font("Century Gothic", 0, 14)); // NOI18N
        returnlabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        returnlabel.setText("RETURN");
        jPanel1.add(returnlabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 220, 120, 30));

        jPanel4.setBackground(new java.awt.Color(204, 204, 204));
        jPanel4.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        viewlabel.setFont(new java.awt.Font("Century Gothic", 0, 14)); // NOI18N
        viewlabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        viewlabel.setText("VIEW BOOKS");
        jPanel4.add(viewlabel);

        jPanel1.add(jPanel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 160, 120, 30));

        jPanel5.setBackground(new java.awt.Color(204, 204, 204));
        jPanel5.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        borrowlabel.setFont(new java.awt.Font("Century Gothic", 0, 14)); // NOI18N
        borrowlabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        borrowlabel.setText("BORROW");
        borrowlabel.setToolTipText("");
        jPanel5.add(borrowlabel);

        jPanel1.add(jPanel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 190, 120, 30));

        jPanel6.setBackground(new java.awt.Color(204, 204, 204));
        jPanel6.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jPanel1.add(jPanel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 220, 120, 30));

        jPanel7.setBackground(new java.awt.Color(204, 51, 0));
        jPanel7.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jPanel7.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        logoutlabel.setFont(new java.awt.Font("Century Gothic", 1, 12)); // NOI18N
        logoutlabel.setForeground(new java.awt.Color(255, 255, 255));
        logoutlabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        logoutlabel.setText("LOGOUT");
        jPanel7.add(logoutlabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, -4, 120, 30));

        jPanel1.add(jPanel7, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 480, 120, 40));

        jLabel8.setIcon(new javax.swing.ImageIcon("C:\\Users\\ADMIN\\Documents\\NetBeansProjects\\MyLibrary\\src\\das (1).png")); // NOI18N
        jLabel8.setText("jLabel8");
        jLabel8.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jPanel1.add(jLabel8, new org.netbeans.lib.awtextra.AbsoluteConstraints(-10, 10, 130, 530));

        getContentPane().add(jPanel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, -1, 510));

        welcome_lbl.setFont(new java.awt.Font("Century Gothic", 1, 20)); // NOI18N
        welcome_lbl.setForeground(new java.awt.Color(51, 51, 51));
        welcome_lbl.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        welcome_lbl.setText("Welcome to Bookworm Central, Readers!");
        getContentPane().add(welcome_lbl, new org.netbeans.lib.awtextra.AbsoluteConstraints(120, 0, 780, 40));

        jLabel1.setIcon(new javax.swing.ImageIcon("C:\\Users\\ADMIN\\Documents\\NetBeansProjects\\MyLibrary\\src\\robin.png")); // NOI18N
        getContentPane().add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(680, 40, 220, 510));

        jPanel9.setBackground(new java.awt.Color(185, 207, 222));
        jPanel9.setLayout(new java.awt.BorderLayout());

        llibrarianletter.setFont(new java.awt.Font("Monospaced", 2, 12)); // NOI18N
        llibrarianletter.setVerticalAlignment(javax.swing.SwingConstants.TOP);
        llibrarianletter.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        llibrarianletter.setVerticalTextPosition(javax.swing.SwingConstants.TOP);
        jPanel9.add(llibrarianletter, java.awt.BorderLayout.CENTER);

        getContentPane().add(jPanel9, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 50, 700, 190));

        imghistorylabel.setIcon(new javax.swing.ImageIcon("C:\\Users\\ADMIN\\Documents\\NetBeansProjects\\MyLibrary\\src\\historybkimg.png")); // NOI18N
        getContentPane().add(imghistorylabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(610, 250, -1, -1));

        imgviewLabel.setIcon(new javax.swing.ImageIcon("C:\\Users\\ADMIN\\Documents\\NetBeansProjects\\MyLibrary\\src\\viewbkimg.png")); // NOI18N
        getContentPane().add(imgviewLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(160, 250, -1, -1));

        imgreturnL.setIcon(new javax.swing.ImageIcon("C:\\Users\\ADMIN\\Documents\\NetBeansProjects\\MyLibrary\\src\\returnbkimg.png")); // NOI18N
        getContentPane().add(imgreturnL, new org.netbeans.lib.awtextra.AbsoluteConstraints(470, 250, -1, -1));

        imgborrowL.setIcon(new javax.swing.ImageIcon("C:\\Users\\ADMIN\\Documents\\NetBeansProjects\\MyLibrary\\src\\borrowbkimg.png")); // NOI18N
        getContentPane().add(imgborrowL, new org.netbeans.lib.awtextra.AbsoluteConstraints(330, 250, -1, -1));

        jPanel10.setBackground(new java.awt.Color(185, 207, 222));

        dashhistory.setFont(new java.awt.Font("Century Gothic", 1, 14)); // NOI18N
        dashhistory.setText("VIEW HISTORY");
        jPanel10.add(dashhistory);

        getContentPane().add(jPanel10, new org.netbeans.lib.awtextra.AbsoluteConstraints(610, 390, 120, 25));

        jPanel14.setBackground(new java.awt.Color(185, 207, 222));
        jPanel14.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        dashuserprofile.setFont(new java.awt.Font("Century Gothic", 1, 14)); // NOI18N
        dashuserprofile.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        dashuserprofile.setText("USER PROFILE");
        jPanel14.add(dashuserprofile, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 130, 30));

        getContentPane().add(jPanel14, new org.netbeans.lib.awtextra.AbsoluteConstraints(400, 450, 130, 30));

        jPanel11.setBackground(new java.awt.Color(185, 207, 222));

        dashviewlabel.setFont(new java.awt.Font("Century Gothic", 1, 14)); // NOI18N
        dashviewlabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        dashviewlabel.setText("VIEW AVAILABLE BOOK");
        jPanel11.add(dashviewlabel);

        getContentPane().add(jPanel11, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 390, 180, 25));

        jPanel12.setBackground(new java.awt.Color(185, 207, 222));

        dashreturn.setFont(new java.awt.Font("Century Gothic", 1, 14)); // NOI18N
        dashreturn.setText("RETURN BOOK");
        jPanel12.add(dashreturn);

        getContentPane().add(jPanel12, new org.netbeans.lib.awtextra.AbsoluteConstraints(460, 390, 130, 25));

        jPanel13.setBackground(new java.awt.Color(185, 207, 222));

        dashborrow.setFont(new java.awt.Font("Century Gothic", 1, 14)); // NOI18N
        dashborrow.setText("BORROW BOOK");
        jPanel13.add(dashborrow);

        getContentPane().add(jPanel13, new org.netbeans.lib.awtextra.AbsoluteConstraints(320, 390, 130, 25));

        imguserprofileL.setIcon(new javax.swing.ImageIcon("C:\\Users\\ADMIN\\Documents\\NetBeansProjects\\MyLibrary\\src\\userprofile.png")); // NOI18N
        getContentPane().add(imguserprofileL, new org.netbeans.lib.awtextra.AbsoluteConstraints(310, 420, -1, -1));

        jLabel7.setIcon(new javax.swing.ImageIcon("C:\\Users\\ADMIN\\Documents\\NetBeansProjects\\MyLibrary\\src\\1.png")); // NOI18N
        jLabel7.setText("jLabel7");
        jLabel7.setMaximumSize(new java.awt.Dimension(900, 540));
        jLabel7.setMinimumSize(new java.awt.Dimension(900, 540));
        getContentPane().add(jLabel7, new org.netbeans.lib.awtextra.AbsoluteConstraints(120, 0, 780, 520));

        pack();
    }// </editor-fold>//GEN-END:initComponents

   
    
   
   
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
            java.util.logging.Logger.getLogger(LibDashboard.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(LibDashboard.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(LibDashboard.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(LibDashboard.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                
                String username = "Default User"; 
                int userId = 0;
                new LibDashboard(userId, username).setVisible(true); // Create and show dashboard with default username
            }
        });
        
    }
        //</editor-fold>
        
    
        //</editor-fold>


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel borrowlabel;
    private javax.swing.JLabel dashborrow;
    private javax.swing.JLabel dashhistory;
    private javax.swing.JLabel dashicon;
    private javax.swing.JLabel dashlabel;
    private javax.swing.JLabel dashreturn;
    private javax.swing.JLabel dashuserprofile;
    private javax.swing.JLabel dashviewlabel;
    private javax.swing.JLabel historylabel;
    private javax.swing.JLabel imgborrowL;
    private javax.swing.JLabel imghistorylabel;
    private javax.swing.JLabel imgreturnL;
    private javax.swing.JLabel imguserprofileL;
    private javax.swing.JLabel imgviewLabel;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel10;
    private javax.swing.JPanel jPanel11;
    private javax.swing.JPanel jPanel12;
    private javax.swing.JPanel jPanel13;
    private javax.swing.JPanel jPanel14;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JLabel llibrarianletter;
    private javax.swing.JLabel logoutlabel;
    private javax.swing.JLabel returnlabel;
    private javax.swing.JLabel usernameLabel;
    private javax.swing.JLabel viewlabel;
    private javax.swing.JLabel welcome_lbl;
    // End of variables declaration//GEN-END:variables
}
