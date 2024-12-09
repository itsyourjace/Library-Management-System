

package mylibrary;

import java.awt.Color;
import java.awt.Font;
import java.awt.Image;
import javax.swing.*;
import java.awt.event.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.JTableHeader;
import javax.swing.JLabel;



/**
 *
 * @author ADMIN
 */
public class LibView extends javax.swing.JFrame {
    private static LibView instance;
    private static String DEFAULT_ICON_PATH = "src/icons/defaulticon.png";   
    private int userId;
    private final String username;
    private JLabel navuserinfoo;
    
    private static final String DB_URL = "jdbc:mysql://localhost:3306/library_db";
    private static final String USER = "root";
    private static final String PASSWORD = "";
    private JTextField searchTextField1;

    
    // Constructor
 
    private void customizeTableHeader() {
        JTableHeader tableHeader = bookstable.getTableHeader();
        tableHeader.setBackground(new Color(210, 232, 237));

        DefaultTableCellRenderer headerRenderer = new DefaultTableCellRenderer();
        headerRenderer.setHorizontalAlignment(DefaultTableCellRenderer.CENTER);
        headerRenderer.setBackground(new Color(210, 232, 237)); // Set the same background color
        headerRenderer.setForeground(Color.BLACK); // Set the text color
        headerRenderer.setFont(new Font("Century Gothic", Font.BOLD, 12)); // Set a bold font if needed

      
        tableHeader.setDefaultRenderer(headerRenderer);
        tableHeader.setBorder(BorderFactory.createLineBorder(new Color(150, 150, 255)));
    }
    
    
    private void customizeTableColumns() {
    DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
    centerRenderer.setHorizontalAlignment(DefaultTableCellRenderer.CENTER);
    bookstable.getColumnModel().getColumn(0).setCellRenderer(centerRenderer);
    bookstable.getColumnModel().getColumn(4).setCellRenderer(centerRenderer);
    
    
}


   
    
    public LibView(int userId, String username) {
        
        this.userId = userId;
        this.username = username;
        initComponents();
        setLocationRelativeTo(null);
        loadBooksData();
        setPlaceholde();
        customizeTableHeader();
        customizeTableColumns();
         setUsername(username);
         String userIconPath = getUserIconPath(userId);
         updateIconInLibView(userIconPath);
         loadUserIcon(userId);
         
         
        

        navlogout.addMouseListener(new MouseAdapter() {
            @Override
             public void mouseClicked(MouseEvent e) {
            int response = JOptionPane.showConfirmDialog(
            LibView.this, 
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
        navborrow.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                new LibBorrow(userId, username).setVisible(true); // Open LibBorrow JFrame
                dispose(); // Close the dashboard
            }
        });
    
        navhistory.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                new LibHistory(userId, username).setVisible(true); // Open LibBorrow JFrame
                dispose(); // Close the dashboard
            }
            
        });
        
        navreturn.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                new LibReturn(userId, username).setVisible(true); // Open LibBorrow JFrame
                dispose(); // Close the dashboard
            }
        });
        
        navuserinfo.addMouseListener(new MouseAdapter() {
        @Override
        public void mouseClicked(MouseEvent e) {
            new LibUsersInfo (userId, username).setVisible(true); // Open LibDashboard JFrame
            dispose(); // Close the current LibBorrow JFrame
        }
    });
        navdash.addMouseListener(new MouseAdapter() {
        @Override
        public void mouseClicked(MouseEvent e) {
            new LibDashboard(userId, username).setVisible(true); // Open LibDashboard JFrame
            dispose(); // Close the current LibBorrow JFrame
        }
    });
    }
    
     public void loadBooksData() {
    DefaultTableModel model = (DefaultTableModel) bookstable.getModel();
    model.setRowCount(0); // Clear existing data

    String url = "jdbc:mysql://localhost:3306/library_db"; 
    String user = "root"; 
    String password = ""; 

    try (Connection conn = DriverManager.getConnection(url, user, password);
         Statement stmt = conn.createStatement();
         ResultSet rs = stmt.executeQuery("SELECT book_id, title, author, genre, quantity FROM books")) {

        while (rs.next()) {
            int ID = rs.getInt("book_id"); // Get the book ID
            String Title = rs.getString("title");
            String Author = rs.getString("author");
            String Genre = rs.getString("genre");
            int Qty = rs.getInt("quantity");

            model.addRow(new Object[]{ID, Title, Author, Genre, Qty}); // Add book ID to the row
        }
    } catch (Exception e) {
        // Handle exceptions appropriately
        
    }
}
     public static LibView getInstance() {
        return instance; // Return the existing instance
    }

    public static LibView getInstance(int userId, String username) {
        if (instance == null) {
            instance = new LibView(userId, username);
        }
        return instance;
    }
    
     public void updateIconInLibView(String iconPath) {
        // Load and scale the image
        ImageIcon icon = new ImageIcon(iconPath);
        Image scaledImageView = icon.getImage().getScaledInstance(viewicon.getWidth(), viewicon.getHeight(), Image.SCALE_SMOOTH);
        viewicon.setIcon(new ImageIcon(scaledImageView));
    }
     
    public void loadUserIcon(int userId) {
        ImageIcon icon = UserIconUtility.retrieveUserIcon(userId);

        // Check if a custom icon was found
        if (icon != null) {
            // Scale and set the custom icon
            Image scaledImage = icon.getImage().getScaledInstance(viewicon.getWidth(), viewicon.getHeight(), Image.SCALE_SMOOTH);
            viewicon.setIcon(new ImageIcon(scaledImage));
        } else {
            // If no custom icon is found, set the default icon
            String DEFAULT_ICON_PATH = "src/icons/defaulticon.png"; // Adjust the path accordingly
            ImageIcon defaultIcon = new ImageIcon(DEFAULT_ICON_PATH);
            Image scaledDefaultImage = defaultIcon.getImage().getScaledInstance(viewicon.getWidth(), viewicon.getHeight(), Image.SCALE_SMOOTH);
            viewicon.setIcon(new ImageIcon(scaledDefaultImage));
        }
    }

    // Example method to get user icon path from database
    public String getUserIconPath(int userId) {
        DbConnection db = new DbConnection();
        return db.getUserIconPath(userId);  // Fetch icon path from the database
    }

    
     private void searchBooks() {
        String searchTerm = searchTextField.getText().trim();
        DefaultTableModel model = (DefaultTableModel) bookstable.getModel();
        model.setRowCount(0); // Clear existing rows
        try {
            Connection connection = DriverManager.getConnection(DB_URL, USER, PASSWORD);
            Statement statement = connection.createStatement();
            String query = "SELECT * FROM books WHERE book_id LIKE '%" + searchTerm + "%' "
                    + "OR title LIKE '%" + searchTerm + "%' "
                    + "OR author LIKE '%" + searchTerm + "%'"
                    + "OR genre LIKE '%" + searchTerm + "%'";; // Search by ID, title, or author
            ResultSet resultSet = statement.executeQuery(query);
            while (resultSet.next()) {
                int id = resultSet.getInt("book_id");
                String title = resultSet.getString("title");
                String author = resultSet.getString("author");
                String genre = resultSet.getString("genre");
                int qty = resultSet.getInt("quantity");
                model.addRow(new Object[]{id, title, author, genre, qty});
            }
            connection.close();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error searching books: " + e.getMessage());
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
        jPanel7 = new javax.swing.JPanel();
        navlogout = new javax.swing.JLabel();
        navuserinfo = new javax.swing.JLabel();
        viewicon = new javax.swing.JLabel();
        jPanel4 = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        navdash = new javax.swing.JLabel();
        jPanel8 = new javax.swing.JPanel();
        navview = new javax.swing.JLabel();
        jPanel6 = new javax.swing.JPanel();
        navborrow = new javax.swing.JLabel();
        jPanel10 = new javax.swing.JPanel();
        navreturn = new javax.swing.JLabel();
        jPanel3 = new javax.swing.JPanel();
        navhistory = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        bookstable = new javax.swing.JTable();
        searchTextField = new javax.swing.JTextField();
        jButton1 = new javax.swing.JButton();
        jLabel10 = new javax.swing.JLabel();
        refreshbtn = new javax.swing.JButton();
        jLabel2 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("View Books");
        setBackground(new java.awt.Color(255, 51, 51));
        setMinimumSize(new java.awt.Dimension(900, 540));
        setPreferredSize(new java.awt.Dimension(900, 540));
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel1.setBackground(new java.awt.Color(210, 232, 237));
        jPanel1.setPreferredSize(new java.awt.Dimension(120, 520));
        jPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel7.setBackground(new java.awt.Color(204, 51, 0));
        jPanel7.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jPanel7.setPreferredSize(new java.awt.Dimension(120, 30));

        navlogout.setFont(new java.awt.Font("Century Gothic", 1, 12)); // NOI18N
        navlogout.setForeground(new java.awt.Color(255, 255, 255));
        navlogout.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        navlogout.setText("LOGOUT");

        javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 116, Short.MAX_VALUE)
            .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel7Layout.createSequentialGroup()
                    .addComponent(navlogout, javax.swing.GroupLayout.PREFERRED_SIZE, 116, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(0, 0, Short.MAX_VALUE)))
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 26, Short.MAX_VALUE)
            .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel7Layout.createSequentialGroup()
                    .addComponent(navlogout, javax.swing.GroupLayout.DEFAULT_SIZE, 20, Short.MAX_VALUE)
                    .addContainerGap()))
        );

        jPanel1.add(jPanel7, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 480, 120, 30));

        navuserinfo.setFont(new java.awt.Font("Century Gothic", 0, 14)); // NOI18N
        navuserinfo.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        navuserinfo.setText("USERNAME");
        jPanel1.add(navuserinfo, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 80, 120, 30));

        viewicon.setIcon(new javax.swing.ImageIcon("C:\\Users\\ADMIN\\Documents\\NetBeansProjects\\MyLibrary\\src\\icons\\defaulticon.png")); // NOI18N
        jPanel1.add(viewicon, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 120, 80));

        jPanel4.setBackground(new java.awt.Color(172, 188, 199));
        jPanel4.setLayout(new java.awt.BorderLayout());
        jPanel1.add(jPanel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 120, 80));

        jPanel2.setBackground(new java.awt.Color(204, 204, 204));
        jPanel2.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jPanel2.setPreferredSize(new java.awt.Dimension(120, 30));

        navdash.setFont(new java.awt.Font("Century Gothic", 0, 14)); // NOI18N
        navdash.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        navdash.setText("DASHBOARD");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(navdash, javax.swing.GroupLayout.DEFAULT_SIZE, 116, Short.MAX_VALUE)
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(navdash, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 26, Short.MAX_VALUE)
        );

        jPanel1.add(jPanel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 140, 120, 30));

        jPanel8.setBackground(new java.awt.Color(0, 51, 102));
        jPanel8.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jPanel8.setPreferredSize(new java.awt.Dimension(120, 30));

        navview.setFont(new java.awt.Font("Century Gothic", 1, 16)); // NOI18N
        navview.setForeground(new java.awt.Color(255, 255, 255));
        navview.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        navview.setText("VIEW BOOKS");
        navview.setPreferredSize(new java.awt.Dimension(120, 30));

        javax.swing.GroupLayout jPanel8Layout = new javax.swing.GroupLayout(jPanel8);
        jPanel8.setLayout(jPanel8Layout);
        jPanel8Layout.setHorizontalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(navview, javax.swing.GroupLayout.DEFAULT_SIZE, 120, Short.MAX_VALUE)
        );
        jPanel8Layout.setVerticalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(navview, javax.swing.GroupLayout.DEFAULT_SIZE, 26, Short.MAX_VALUE)
        );

        jPanel1.add(jPanel8, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 170, 120, 30));

        jPanel6.setBackground(new java.awt.Color(204, 204, 204));
        jPanel6.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jPanel6.setPreferredSize(new java.awt.Dimension(120, 30));

        navborrow.setFont(new java.awt.Font("Century Gothic", 0, 14)); // NOI18N
        navborrow.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        navborrow.setText("BORROW");

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel6Layout.createSequentialGroup()
                .addComponent(navborrow, javax.swing.GroupLayout.DEFAULT_SIZE, 110, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(navborrow, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 26, Short.MAX_VALUE)
        );

        jPanel1.add(jPanel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 200, 120, -1));

        jPanel10.setBackground(new java.awt.Color(204, 204, 204));
        jPanel10.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jPanel10.setPreferredSize(new java.awt.Dimension(120, 30));

        navreturn.setFont(new java.awt.Font("Century Gothic", 0, 14)); // NOI18N
        navreturn.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        navreturn.setText("RETURN");

        javax.swing.GroupLayout jPanel10Layout = new javax.swing.GroupLayout(jPanel10);
        jPanel10.setLayout(jPanel10Layout);
        jPanel10Layout.setHorizontalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(navreturn, javax.swing.GroupLayout.DEFAULT_SIZE, 116, Short.MAX_VALUE)
        );
        jPanel10Layout.setVerticalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel10Layout.createSequentialGroup()
                .addComponent(navreturn, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(25, 25, 25))
        );

        jPanel1.add(jPanel10, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 230, 120, -1));

        jPanel3.setBackground(new java.awt.Color(204, 204, 204));
        jPanel3.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        navhistory.setFont(new java.awt.Font("Century Gothic", 0, 14)); // NOI18N
        navhistory.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        navhistory.setText("HISTORY");

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(navhistory, javax.swing.GroupLayout.DEFAULT_SIZE, 116, Short.MAX_VALUE)
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(navhistory, javax.swing.GroupLayout.DEFAULT_SIZE, 26, Short.MAX_VALUE)
        );

        jPanel1.add(jPanel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 260, 120, 30));

        jLabel1.setIcon(new javax.swing.ImageIcon("C:\\Users\\ADMIN\\Documents\\NetBeansProjects\\MyLibrary\\src\\das (1).png")); // NOI18N
        jLabel1.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jPanel1.add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 120, 500));

        getContentPane().add(jPanel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, -1, 510));

        bookstable.setBackground(new java.awt.Color(207, 218, 246));
        bookstable.setFont(new java.awt.Font("Century Gothic", 0, 11)); // NOI18N
        bookstable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null}
            },
            new String [] {
                "ID", "Title", "Author", "Genre", "Qty"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Integer.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.Integer.class
            };
            boolean[] canEdit = new boolean [] {
                false, true, false, true, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        bookstable.setGridColor(new java.awt.Color(102, 102, 102));
        bookstable.setRowHeight(25);
        bookstable.setRowMargin(1);
        bookstable.setSelectionBackground(new java.awt.Color(139, 174, 210));
        bookstable.setShowGrid(true);
        bookstable.getTableHeader().setReorderingAllowed(false);
        jScrollPane1.setViewportView(bookstable);
        if (bookstable.getColumnModel().getColumnCount() > 0) {
            bookstable.getColumnModel().getColumn(0).setMinWidth(40);
            bookstable.getColumnModel().getColumn(0).setPreferredWidth(40);
            bookstable.getColumnModel().getColumn(0).setMaxWidth(40);
            bookstable.getColumnModel().getColumn(1).setPreferredWidth(200);
            bookstable.getColumnModel().getColumn(2).setResizable(false);
            bookstable.getColumnModel().getColumn(2).setPreferredWidth(100);
            bookstable.getColumnModel().getColumn(3).setResizable(false);
            bookstable.getColumnModel().getColumn(3).setPreferredWidth(50);
            bookstable.getColumnModel().getColumn(4).setMinWidth(50);
            bookstable.getColumnModel().getColumn(4).setPreferredWidth(50);
            bookstable.getColumnModel().getColumn(4).setMaxWidth(50);
        }

        getContentPane().add(jScrollPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 60, 750, 430));

        searchTextField.setBackground(new java.awt.Color(207, 222, 225));
        searchTextField.setFont(new java.awt.Font("Century Gothic", 0, 12)); // NOI18N
        searchTextField.setForeground(new java.awt.Color(102, 102, 102));
        searchTextField.setText("Search for title, genre,  or author of the book");
        searchTextField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                searchTextFieldActionPerformed(evt);
            }
        });
        getContentPane().add(searchTextField, new org.netbeans.lib.awtextra.AbsoluteConstraints(230, 10, 446, 41));

        jButton1.setBackground(new java.awt.Color(210, 232, 237));
        jButton1.setFont(new java.awt.Font("Century Gothic", 1, 12)); // NOI18N
        jButton1.setText("Search");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });
        getContentPane().add(jButton1, new org.netbeans.lib.awtextra.AbsoluteConstraints(680, 20, 85, 25));

        jLabel10.setIcon(new javax.swing.ImageIcon("C:\\Users\\ADMIN\\Documents\\NetBeansProjects\\MyLibrary\\src\\searchico.png")); // NOI18N
        getContentPane().add(jLabel10, new org.netbeans.lib.awtextra.AbsoluteConstraints(190, 10, -1, -1));

        refreshbtn.setBackground(new java.awt.Color(207, 218, 246));
        refreshbtn.setFont(new java.awt.Font("Century Gothic", 1, 12)); // NOI18N
        refreshbtn.setText("Refresh");
        refreshbtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                refreshbtnActionPerformed(evt);
            }
        });
        getContentPane().add(refreshbtn, new org.netbeans.lib.awtextra.AbsoluteConstraints(760, 20, 80, 25));

        jLabel2.setIcon(new javax.swing.ImageIcon("C:\\Users\\ADMIN\\Documents\\NetBeansProjects\\MyLibrary\\src\\admin1.png")); // NOI18N
        jLabel2.setText("jLabel2");
        getContentPane().add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(120, 0, 800, 550));

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void searchTextFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_searchTextFieldActionPerformed
        // TODO add your handling code here:
        searchBooks();
        
       
    }//GEN-LAST:event_searchTextFieldActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // TODO add your handling code here:
        searchBooks();
        
        
    }//GEN-LAST:event_jButton1ActionPerformed

    private void refreshbtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_refreshbtnActionPerformed
        // TODO add your handling code here:
        loadBooksData();
        setPlaceholde();  
    }//GEN-LAST:event_refreshbtnActionPerformed

    
    
   
    
    

        
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
            java.util.logging.Logger.getLogger(LibView.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(LibView.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(LibView.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(LibView.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                int userId = 0;
                String username = "Default User";
                new LibView(userId, username).setVisible(true);
                
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTable bookstable;
    private javax.swing.JButton jButton1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel10;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel navborrow;
    private javax.swing.JLabel navdash;
    private javax.swing.JLabel navhistory;
    private javax.swing.JLabel navlogout;
    private javax.swing.JLabel navreturn;
    private javax.swing.JLabel navuserinfo;
    private javax.swing.JLabel navview;
    private javax.swing.JButton refreshbtn;
    private javax.swing.JTextField searchTextField;
    private javax.swing.JLabel viewicon;
    // End of variables declaration//GEN-END:variables

    private void setPlaceholde() {
        searchTextField.setText("Search for ID, title, author, or genre of the book");
        searchTextField.setForeground(Color.GRAY); // Set the placeholder color to gray

    // Add focus listener to manage focus gained/lost behavior
    searchTextField.addFocusListener(new java.awt.event.FocusAdapter() {
        @Override
        public void focusGained(java.awt.event.FocusEvent evt) {
            // Clear placeholder on focus if it matches the placeholder text
            if (searchTextField.getText().equals("Search for ID, title, author, or genre of the book")) {
                searchTextField.setText(""); // Clear placeholder
                searchTextField.setForeground(Color.BLACK); // Set the text color to black
            }
        }

        @Override
        public void focusLost(java.awt.event.FocusEvent evt) {
            // If the user leaves the field empty, restore the placeholder
            if (searchTextField.getText().isEmpty()) {
                searchTextField.setText("Search for ID, title, author, or genre of the book");
                searchTextField.setForeground(Color.GRAY); // Set color back to gray
            }
        }
    });
}

    private void setUsername(String username) {
         navuserinfo.setText(username.toUpperCase());       
    }
        
    }

