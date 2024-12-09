package mylibrary;


import java.awt.Color;
import java.awt.Component;
import java.awt.Image;
import java.awt.MediaTracker;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.net.URL;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.swing.ImageIcon;

import javax.swing.JLabel;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;


/**
 *
 * @author ADMIN
 */
public class LibHistory extends javax.swing.JFrame {
    private static LibHistory instance;
    private static String DEFAULT_ICON_PATH = "src/icons/defaulticon.png";   
    
    private int userId;
    private String username;
    private DefaultTableModel tableModel;

    /**
     * Creates new form LibHistory
  
     */
    public LibHistory(int userId,String username) {
        this.userId =userId;
        this.username = username;
        initComponents();
        hisuser.setText(username.toUpperCase());
        tableModel = new DefaultTableModel(new Object[]{"TRID", "BID", "TITLE", "CHECKOUT", "DUE DATE", "RETURN", "STATUS"}, 0);
        historytable.setModel(tableModel);
        fetchTransactions();
        adjustTableColumns();
        centerTableHeaders();
        historytable.setGridColor(Color.LIGHT_GRAY);
        DefaultTableModel model = (DefaultTableModel) historytable.getModel();
        customizeTableHeaders();
        setLocationRelativeTo(null);
        setDefaultIcon(); 
        updateIconInLibHistory(getUserIconPath(userId)); 
        loadUserIcon(userId);
        getUserIconPath(userId);
        
        
     
       
       

      
        
        
        hislogout.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int response = JOptionPane.showConfirmDialog(
                        LibHistory.this,
                        "Are you sure you want to log out?",
                        "Logout Confirmation",
                        JOptionPane.YES_NO_OPTION
                );

                if (response == JOptionPane.YES_OPTION) {

                    LibLogin loginScreen = new LibLogin(); 
                    loginScreen.setVisible(true); 
                    dispose();
                }
            } 
        });
        
        hisview.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                new LibView(userId, username).setVisible(true); 
                dispose(); 
            }
        });
        
        hisdash.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                new LibDashboard(userId, username).setVisible(true); 
                dispose(); 
            }
        });
        
        hisuser.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                new LibUsersInfo(userId,username).setVisible(true); 
                dispose(); 
            }
        });
        
        hisreturn.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                new LibReturn(userId, username).setVisible(true); 
                dispose();
            }
        });
        
        hisborrow.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                new LibBorrow(userId, username).setVisible(true);
                dispose(); 
            }
        });
        
    }
    
  
    private void fetchTransactions() {
        try (Connection connection = DbConnection.getConnection(); java.sql.Statement statement = connection.createStatement()) {

            String query = "SELECT t.transaction_id, t.user_id, t.book_id, b.title, t.checkout_date, t.due_date, t.return_date, t.status "
                    + "FROM transactions t "
                    + "JOIN books b ON t.book_id = b.book_id "
                    + "WHERE t.user_id = " + userId;
            ResultSet resultSet = statement.executeQuery(query);

            while (resultSet.next()) {
                Object[] row = new Object[]{
                    resultSet.getInt("transaction_id"),
                    resultSet.getInt("book_id"),
                    resultSet.getString("title") != null ? resultSet.getString("title") : "N/A",
                    resultSet.getDate("checkout_date"),
                    resultSet.getDate("due_date"),
                    resultSet.getDate("return_date"),
                    resultSet.getString("status") != null ? resultSet.getString("status") : "Unknown"
                };
                tableModel.addRow(row);
            }
        } catch (SQLException e) {
            // Log the exception (this can be to a file, console, etc.)
            System.err.println("SQL Error Code: " + e.getErrorCode());
            System.err.println("SQL State: " + e.getSQLState());
            e.printStackTrace(); // Log full stack trace for debugging

            // Show user-friendly message
            JOptionPane.showMessageDialog(this,
                    "An error occurred while fetching transaction history. Please try again later.",
                    "Database Error",
                    JOptionPane.ERROR_MESSAGE);
        } catch (Exception e) {
            // Handle any other exceptions
            JOptionPane.showMessageDialog(this,
                    "An unexpected error occurred: " + e.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
         historytable.revalidate();
         historytable.repaint();
    }
    
    public static LibHistory getInstance() {
        return instance; // Return the existing instance
    }

    public static LibHistory getInstance(int userId, String username) {
        if (instance == null) {
            instance = new LibHistory(userId, username);
        }
        return instance;
    }
    
    public void updateIconInLibHistory(String iconPath) {
        ImageIcon icon = new ImageIcon(iconPath);
        Image scaledImage = icon.getImage().getScaledInstance(hisicon.getWidth(), hisicon.getHeight(), Image.SCALE_SMOOTH);
        hisicon.setIcon(new ImageIcon(scaledImage));
        hisicon.revalidate();
        hisicon.repaint();
    }
    
    private void setDefaultIcon() {
        URL iconUrl = getClass().getResource("src/icons/defaulticon.png");
        if (iconUrl != null) {
            ImageIcon defaultIcon = new ImageIcon(iconUrl);
            Image scaledDefaultImage = defaultIcon.getImage().getScaledInstance(120, 90, Image.SCALE_SMOOTH);
            hisicon.setIcon(new ImageIcon(scaledDefaultImage));
        } else {
            System.err.println("Default icon not found at src/icons/defaulticon.png");
        }
    } 

    // Example method to get user icon path from the database
    public String getUserIconPath(int userId) {
        DbConnection db = new DbConnection();
        return db.getUserIconPath(userId); // Fetch icon path from the database
    }

    

    public void loadUserIcon(int userId) {
        // Attempt to retrieve the user's icon
        ImageIcon icon = UserIconUtility.retrieveUserIcon(userId);

        // Check if a custom icon was found
        if (icon != null) {
            // Scale and set the custom icon
            Image scaledImage = icon.getImage().getScaledInstance(hisicon.getWidth(), hisicon.getHeight(), Image.SCALE_SMOOTH);
            hisicon.setIcon(new ImageIcon(scaledImage));
        } else {
            // If no custom icon is found, set the default icon
            String DEFAULT_ICON_PATH = "src/icons/defaulticon.png"; // Adjust the path accordingly
            ImageIcon defaultIcon = new ImageIcon(DEFAULT_ICON_PATH);
            Image scaledDefaultImage = defaultIcon.getImage().getScaledInstance(hisicon.getWidth(), hisicon.getHeight(), Image.SCALE_SMOOTH);
            hisicon.setIcon(new ImageIcon(scaledDefaultImage));
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

        jPanel2 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        historytable = new javax.swing.JTable();
        sortby = new javax.swing.JComboBox<>();
        jLabel3 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();
        hisicon = new javax.swing.JLabel();
        hisuser = new javax.swing.JLabel();
        hisdash = new javax.swing.JLabel();
        jPanel4 = new javax.swing.JPanel();
        jPanel6 = new javax.swing.JPanel();
        hisview = new javax.swing.JLabel();
        jPanel5 = new javax.swing.JPanel();
        hisborrow = new javax.swing.JLabel();
        jPanel7 = new javax.swing.JPanel();
        hisreturn = new javax.swing.JLabel();
        jPanel8 = new javax.swing.JPanel();
        hislogout = new javax.swing.JLabel();
        jPanel9 = new javax.swing.JPanel();
        jLabel9 = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setPreferredSize(new java.awt.Dimension(900, 530));
        getContentPane().setLayout(null);

        jPanel2.setPreferredSize(new java.awt.Dimension(900, 600));
        jPanel2.setLayout(null);

        historytable.setBackground(new java.awt.Color(207, 218, 246));
        historytable.setFont(new java.awt.Font("Century Gothic", 0, 12)); // NOI18N
        historytable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null}
            },
            new String [] {
                "TRID", "BID", "TITLE", "CHECKOUT", "DUE DATE", "RETURN", "STATUS"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.Integer.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }
        });
        historytable.setAlignmentX(1.0F);
        historytable.setAlignmentY(1.0F);
        historytable.setGridColor(new java.awt.Color(102, 102, 102));
        historytable.setName("TRANSACTION HISTORY"); // NOI18N
        historytable.setRowHeight(25);
        historytable.setRowMargin(1);
        historytable.setShowGrid(true);
        jScrollPane1.setViewportView(historytable);
        if (historytable.getColumnModel().getColumnCount() > 0) {
            historytable.getColumnModel().getColumn(0).setMinWidth(70);
            historytable.getColumnModel().getColumn(0).setPreferredWidth(70);
            historytable.getColumnModel().getColumn(0).setMaxWidth(5070);
            historytable.getColumnModel().getColumn(1).setMinWidth(70);
            historytable.getColumnModel().getColumn(1).setPreferredWidth(70);
            historytable.getColumnModel().getColumn(1).setMaxWidth(70);
            historytable.getColumnModel().getColumn(2).setMinWidth(250);
            historytable.getColumnModel().getColumn(2).setPreferredWidth(250);
            historytable.getColumnModel().getColumn(2).setMaxWidth(250);
            historytable.getColumnModel().getColumn(3).setMinWidth(90);
            historytable.getColumnModel().getColumn(3).setPreferredWidth(90);
            historytable.getColumnModel().getColumn(3).setMaxWidth(90);
            historytable.getColumnModel().getColumn(4).setMinWidth(100);
            historytable.getColumnModel().getColumn(4).setPreferredWidth(100);
            historytable.getColumnModel().getColumn(4).setMaxWidth(100);
            historytable.getColumnModel().getColumn(5).setMinWidth(90);
            historytable.getColumnModel().getColumn(5).setPreferredWidth(90);
            historytable.getColumnModel().getColumn(5).setMaxWidth(90);
            historytable.getColumnModel().getColumn(6).setMinWidth(100);
            historytable.getColumnModel().getColumn(6).setPreferredWidth(100);
            historytable.getColumnModel().getColumn(6).setMaxWidth(100);
        }

        jPanel2.add(jScrollPane1);
        jScrollPane1.setBounds(10, 80, 750, 410);

        sortby.setFont(new java.awt.Font("Century Gothic", 0, 12)); // NOI18N
        sortby.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "All", "Borrowed", "Returned", "Overdue" }));
        sortby.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                sortbyActionPerformed(evt);
            }
        });
        jPanel2.add(sortby);
        sortby.setBounds(660, 50, 100, 22);

        jLabel3.setFont(new java.awt.Font("Century Gothic", 1, 24)); // NOI18N
        jLabel3.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel3.setText("TRANSACTION HISTORY");
        jPanel2.add(jLabel3);
        jLabel3.setBounds(0, 0, 810, 70);

        jLabel10.setIcon(new javax.swing.ImageIcon("C:\\Users\\ADMIN\\Documents\\NetBeansProjects\\MyLibrary\\src\\1.png")); // NOI18N
        jLabel10.setText("jLabel10");
        jPanel2.add(jLabel10);
        jLabel10.setBounds(0, 0, 810, 490);

        getContentPane().add(jPanel2);
        jPanel2.setBounds(120, 0, 840, 600);

        jPanel1.setPreferredSize(new java.awt.Dimension(120, 500));
        jPanel1.setLayout(null);

        hisicon.setIcon(new javax.swing.ImageIcon("C:\\Users\\ADMIN\\Documents\\NetBeansProjects\\MyLibrary\\src\\icons\\defaulticon.png")); // NOI18N
        jPanel1.add(hisicon);
        hisicon.setBounds(0, 0, 120, 80);

        hisuser.setFont(new java.awt.Font("Century Gothic", 0, 14)); // NOI18N
        hisuser.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        hisuser.setText("USERNAME");
        jPanel1.add(hisuser);
        hisuser.setBounds(0, 80, 120, 19);

        hisdash.setFont(new java.awt.Font("Century Gothic", 0, 14)); // NOI18N
        hisdash.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        hisdash.setText("DASHBOARD");
        jPanel1.add(hisdash);
        hisdash.setBounds(0, 149, 120, 30);

        jPanel4.setBackground(new java.awt.Color(204, 204, 204));
        jPanel4.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 116, Short.MAX_VALUE)
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 21, Short.MAX_VALUE)
        );

        jPanel1.add(jPanel4);
        jPanel4.setBounds(0, 150, 120, 25);

        jPanel6.setBackground(new java.awt.Color(204, 204, 204));
        jPanel6.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        hisview.setFont(new java.awt.Font("Century Gothic", 0, 14)); // NOI18N
        hisview.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        hisview.setText("VIEW");

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(hisview, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 116, Short.MAX_VALUE)
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(hisview, javax.swing.GroupLayout.DEFAULT_SIZE, 21, Short.MAX_VALUE)
        );

        jPanel1.add(jPanel6);
        jPanel6.setBounds(0, 175, 120, 25);

        jPanel5.setBackground(new java.awt.Color(204, 204, 204));
        jPanel5.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        hisborrow.setFont(new java.awt.Font("Century Gothic", 0, 14)); // NOI18N
        hisborrow.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        hisborrow.setText("BORROW");

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(hisborrow, javax.swing.GroupLayout.DEFAULT_SIZE, 116, Short.MAX_VALUE)
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(hisborrow, javax.swing.GroupLayout.DEFAULT_SIZE, 21, Short.MAX_VALUE)
        );

        jPanel1.add(jPanel5);
        jPanel5.setBounds(0, 200, 120, 25);

        jPanel7.setBackground(new java.awt.Color(204, 204, 204));
        jPanel7.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        hisreturn.setFont(new java.awt.Font("Century Gothic", 0, 14)); // NOI18N
        hisreturn.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        hisreturn.setText("RETURN");

        javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel7Layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(hisreturn, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel7Layout.createSequentialGroup()
                .addGap(0, 2, Short.MAX_VALUE)
                .addComponent(hisreturn))
        );

        jPanel1.add(jPanel7);
        jPanel7.setBounds(0, 220, 120, 25);

        jPanel8.setBackground(new java.awt.Color(204, 51, 0));

        hislogout.setFont(new java.awt.Font("Century Gothic", 1, 12)); // NOI18N
        hislogout.setForeground(new java.awt.Color(255, 255, 255));
        hislogout.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        hislogout.setText("LOGOUT");

        javax.swing.GroupLayout jPanel8Layout = new javax.swing.GroupLayout(jPanel8);
        jPanel8.setLayout(jPanel8Layout);
        jPanel8Layout.setHorizontalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(hislogout, javax.swing.GroupLayout.DEFAULT_SIZE, 120, Short.MAX_VALUE)
        );
        jPanel8Layout.setVerticalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(hislogout, javax.swing.GroupLayout.DEFAULT_SIZE, 30, Short.MAX_VALUE)
        );

        jPanel1.add(jPanel8);
        jPanel8.setBounds(0, 460, 120, 30);

        jPanel9.setBackground(new java.awt.Color(0, 51, 102));
        jPanel9.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jPanel9.setForeground(new java.awt.Color(51, 51, 51));
        jPanel9.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel9.setFont(new java.awt.Font("Century Gothic", 1, 16)); // NOI18N
        jLabel9.setForeground(new java.awt.Color(255, 255, 255));
        jLabel9.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel9.setText("HISTORY");
        jPanel9.add(jLabel9, new org.netbeans.lib.awtextra.AbsoluteConstraints(2, 7, 116, -1));

        jPanel1.add(jPanel9);
        jPanel9.setBounds(0, 240, 120, 30);

        jLabel1.setIcon(new javax.swing.ImageIcon("C:\\Users\\ADMIN\\Documents\\NetBeansProjects\\MyLibrary\\src\\das (3).png")); // NOI18N
        jLabel1.setText("jLabel1");
        jLabel1.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jPanel1.add(jLabel1);
        jLabel1.setBounds(0, -110, 120, 601);

        getContentPane().add(jPanel1);
        jPanel1.setBounds(0, 0, 120, 601);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void sortbyActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_sortbyActionPerformed
        // TODO add your handling code here:
         String selectedStatus = (String) sortby.getSelectedItem(); 
         updateTable(selectedStatus);
    }//GEN-LAST:event_sortbyActionPerformed

    private void centerTableHeaders() {
        JTableHeader tableHeader = historytable.getTableHeader();
        DefaultTableCellRenderer renderer = (DefaultTableCellRenderer) tableHeader.getDefaultRenderer();
        renderer.setHorizontalAlignment(JLabel.CENTER); // Center the header text
        tableHeader.setDefaultRenderer(renderer);
    }

    private void customizeTableHeaders() {
        JTableHeader header = historytable.getTableHeader();

        // Define colors for each column
        // Define the two alternating colors
        Color color1 = new Color(204, 204, 255); // Light purple
        Color color2 = new Color(210, 232, 237); // Light blue

        // Apply alternating colors to the columns
        TableColumnModel columnModel = historytable.getColumnModel();
        for (int i = 0; i < columnModel.getColumnCount(); i++) {
            Color alternatingColor = (i % 2 == 0) ? color1 : color2; // Alternate between the two colors
            
            TableColumn column = columnModel.getColumn(i);
            column.setHeaderRenderer(new DefaultTableCellRenderer() {
                @Override
                public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                    JLabel label = (JLabel) super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                    label.setHorizontalAlignment(SwingConstants.CENTER); // Center header text
                    label.setOpaque(true);
                    label.setBackground(alternatingColor); // Apply alternating background color
                    label.setForeground(Color.BLACK); // Set text color to black
                    return label;
                }
            });
        }

        header.repaint(); // Repaint to apply the changes
    }
    
    private void adjustTableColumns() {
        if (historytable.getColumnModel().getColumnCount() > 0) {
            historytable.getColumnModel().getColumn(0).setMinWidth(70);
            historytable.getColumnModel().getColumn(0).setPreferredWidth(70);
            historytable.getColumnModel().getColumn(0).setMaxWidth(5070);
            historytable.getColumnModel().getColumn(1).setMinWidth(70);
            historytable.getColumnModel().getColumn(1).setPreferredWidth(70);
            historytable.getColumnModel().getColumn(1).setMaxWidth(70);
            historytable.getColumnModel().getColumn(2).setMinWidth(250);
            historytable.getColumnModel().getColumn(2).setPreferredWidth(250);
            historytable.getColumnModel().getColumn(2).setMaxWidth(250);
            historytable.getColumnModel().getColumn(3).setMinWidth(90);
            historytable.getColumnModel().getColumn(3).setPreferredWidth(90);
            historytable.getColumnModel().getColumn(3).setMaxWidth(90);
            historytable.getColumnModel().getColumn(4).setMinWidth(100);
            historytable.getColumnModel().getColumn(4).setPreferredWidth(100);
            historytable.getColumnModel().getColumn(4).setMaxWidth(100);
            historytable.getColumnModel().getColumn(5).setMinWidth(90);
            historytable.getColumnModel().getColumn(5).setPreferredWidth(90);
            historytable.getColumnModel().getColumn(5).setMaxWidth(90);
            historytable.getColumnModel().getColumn(6).setMinWidth(100);
            historytable.getColumnModel().getColumn(6).setPreferredWidth(100);
            historytable.getColumnModel().getColumn(6).setMaxWidth(100);
        }
    }

    public int getCurrentUserId() {
        return userId;
    }

    
    private void updateTable(String selectedStatus) {
        // SQL query variable
        String sql;

        try {
            // Get the current user's ID
            int currentUserId = getCurrentUserId(); // Replace this with your method to get the logged-in user ID

            // Check if the user ID is valid
            if (currentUserId == -1) { // Assuming -1 indicates no user is logged in
                JOptionPane.showMessageDialog(null, "No user ID found. Please log in.", "Error", JOptionPane.ERROR_MESSAGE);
                return; // Exit the method early if no user is logged in
            }

            // Determine the SQL query based on the selected status
            if ("Borrowed".equals(selectedStatus)) {
                sql = "SELECT t.transaction_id, t.book_id, b.title, t.checkout_date, t.due_date, t.return_date, "
                        + "CASE WHEN t.return_date IS NULL THEN 'Borrowed' ELSE 'Returned' END AS status "
                        + "FROM transactions t JOIN books b ON t.book_id = b.book_id "
                        + "WHERE t.return_date IS NULL AND t.user_id = ?";
            } else if ("Returned".equals(selectedStatus)) {
                sql = "SELECT t.transaction_id, t.book_id, b.title, t.checkout_date, t.due_date, t.return_date, "
                        + "CASE WHEN t.return_date IS NOT NULL THEN 'Returned' ELSE 'Borrowed' END AS status "
                        + "FROM transactions t JOIN books b ON t.book_id = b.book_id "
                        + "WHERE t.return_date IS NOT NULL AND t.user_id = ?";
               } else if ("Overdue".equals(selectedStatus)) {
            // Only show returned items where the return_date is after the due_date
            sql = "SELECT t.transaction_id, t.book_id, b.title, t.checkout_date, t.due_date, t.return_date, 'Overdue' AS status "
                    + "FROM transactions t JOIN books b ON t.book_id = b.book_id "
                    + "WHERE t.return_date > t.due_date AND t.user_id = ?";
            } else {
                // Default to show all records if "All" is selected
                sql = "SELECT t.transaction_id, t.book_id, b.title, t.checkout_date, t.due_date, t.return_date, "
                        + "CASE "
                        + "WHEN t.return_date > t.due_date THEN 'Overdue' " // Overdue: return_date > due_date
                        + "WHEN t.return_date IS NULL THEN 'Borrowed' " // Borrowed: no return_date
                        + "ELSE 'Returned' END AS status " // Returned: return_date <= due_date
                        + "FROM transactions t JOIN books b ON t.book_id = b.book_id "
                        + "WHERE t.user_id = ?";
            }

            // Clear existing rows in the JTable
            DefaultTableModel model = (DefaultTableModel) historytable.getModel();
            model.setRowCount(0); // Clear existing rows

            // Execute the query with user_id filter
            try (Connection conn = DbConnection.getConnection(); // Ensure the connection is established
            PreparedStatement pstmt = conn.prepareStatement(sql)) {


                pstmt.setInt(1, currentUserId); // Set the user_id parameter in the SQL query

                try (ResultSet rs = pstmt.executeQuery()) {
                    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd"); // Adjust the date format as needed

                    while (rs.next()) {
                        int transactionId = rs.getInt("transaction_id");
                        int bookId = rs.getInt("book_id");
                        String title = rs.getString("title");
                        Date checkoutDate = rs.getDate("checkout_date");
                        Date dueDate = rs.getDate("due_date");
                        Date returnDate = rs.getDate("return_date");
                        String status = rs.getString("status");

                        // Format dates
                        String formattedCheckoutDate = (checkoutDate != null) ? dateFormat.format(checkoutDate) : "";
                        String formattedDueDate = (dueDate != null) ? dateFormat.format(dueDate) : "";
                        String formattedReturnDate = (returnDate != null) ? dateFormat.format(returnDate) : "";

                        // Add new rows to the table model
                        model.addRow(new Object[]{transactionId, bookId, title, formattedCheckoutDate, formattedDueDate, formattedReturnDate, status});
                    }
                }
            } catch (SQLException ex) {
                ex.printStackTrace(); // Handle SQL exceptions
            }

            adjustTableColumns();
            centerTableHeaders();
            historytable.setGridColor(Color.LIGHT_GRAY);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "An error occurred. Please try again.", "Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace(); // Handle unexpected exceptions
        }
    }
    

    
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
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(LibHistory.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(() -> {
            int userId1 = 0;
            String username1 = "Default User";
            new LibHistory(userId1, username1).setVisible(true);
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel hisborrow;
    private javax.swing.JLabel hisdash;
    private javax.swing.JLabel hisicon;
    private javax.swing.JLabel hislogout;
    private javax.swing.JLabel hisreturn;
    private javax.swing.JTable historytable;
    private javax.swing.JLabel hisuser;
    private javax.swing.JLabel hisview;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JComboBox<String> sortby;
    // End of variables declaration//GEN-END:variables
}
