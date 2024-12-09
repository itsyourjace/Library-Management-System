
package mylibrary;


import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.print.PageFormat;
import java.awt.print.Printable;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;
import javax.swing.JLabel;
import javax.swing.table.DefaultTableCellRenderer;
import java.sql.Connection;
import javax.swing.JOptionPane; // For showing message dialogs
import javax.swing.table.DefaultTableModel; // For working with table models
import java.sql.PreparedStatement; // For executing parameterized SQL statements
import java.sql.ResultSet; // For handling the results of a SQL query
import java.sql.SQLException; 
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.ImageIcon;
import javax.swing.JCheckBox;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.table.JTableHeader;

/**
 *
 * @author ADMIN
 */
public class LibReturn extends javax.swing.JFrame {
    private static LibReturn instance;
    private final int userId;
    private final String username;
    String DEFAULT_ICON_PATH = "src/icons/defaulticon.png";

   

   

    /**
     * Creates new form LibReturn
     * @param userId
     * @param username
     */
    public LibReturn(int userId, String username) {
        this.userId = userId;
        this.username = username;
        initComponents();
        setLocationRelativeTo(null);
        retuser.setText(username.toUpperCase());
        loadReturnedBooks(userId);
        setupTables(); 
        String userIconPath = getUserIconPath(userId);  
        updateIconInLibReturn(userIconPath);
        loadUserIcon(userId);
        
    
        

        
        
        retuser.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                new LibUsersInfo(userId, username).setVisible(true); // Open LibView JFrame
                dispose(); // Close the dashboard
            }
        });
        
        rethistory.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                new LibHistory(userId, username).setVisible(true); 
                dispose(); 
            }
        });
        
        
        retdash.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                new LibDashboard(userId, username).setVisible(true); // Open LibView JFrame
                dispose(); // Close the dashboard
            }
        });
        
        retview.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                new LibView(userId, username).setVisible(true); // Open LibView JFrame
                dispose(); // Close the dashboard
            }
        });
        
        retborrow.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                new LibBorrow(userId, username).setVisible(true); // Open LibView JFrame
                dispose(); // Close the dashboard
            }
        });
        
        retlogout.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int response = JOptionPane.showConfirmDialog(
                        LibReturn.this,
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
    
    public static LibReturn getInstance() {
        return instance; // Return the existing instance
    }

    public static LibReturn getInstance(int userId, String username) {
        if (instance == null) {
            instance = new LibReturn(userId, username);
        }
        return instance;
    }
    
    public void updateIconInLibReturn(String iconPath) {
        // Load and scale the image
        ImageIcon icon = new ImageIcon(iconPath);
        Image scaledImageReturn = icon.getImage().getScaledInstance(reticon.getWidth(), reticon.getHeight(), Image.SCALE_SMOOTH);
        reticon.setIcon(new ImageIcon(scaledImageReturn));
    }

    public void loadUserIcon(int userId) {
        ImageIcon icon = UserIconUtility.retrieveUserIcon(userId);

        // Check if a custom icon was found
        if (icon != null) {
            // Scale and set the custom icon
            Image scaledImage = icon.getImage().getScaledInstance(reticon.getWidth(), reticon.getHeight(), Image.SCALE_SMOOTH);
            reticon.setIcon(new ImageIcon(scaledImage));
        } else {
            // If no custom icon is found, set the default icon
            String DEFAULT_ICON_PATH = "src/icons/defaulticon.png"; // Adjust the path accordingly
            ImageIcon defaultIcon = new ImageIcon(DEFAULT_ICON_PATH);
            Image scaledDefaultImage = defaultIcon.getImage().getScaledInstance(reticon.getWidth(), reticon.getHeight(), Image.SCALE_SMOOTH);
            reticon.setIcon(new ImageIcon(scaledDefaultImage));
        }
    }
    
    public String getUserIconPath(int userId) {
        DbConnection db = new DbConnection();
        return db.getUserIconPath(userId);  
    }

    
    
    private class CenteredTableCellRenderer extends DefaultTableCellRenderer {

        public CenteredTableCellRenderer() {
            setHorizontalAlignment(JLabel.CENTER); // Center align text
        }
    }

    private void setupTables() {
        // Header Renderer for viewborrowtable
        DefaultTableCellRenderer headerRenderer = new DefaultTableCellRenderer();
        headerRenderer.setHorizontalAlignment(JLabel.CENTER); // Center align the headers
        headerRenderer.setBackground(new Color(204, 204, 255)); // Set background color for headers

        for (int i = 0; i < viewborrowtable.getModel().getColumnCount(); i++) {
            viewborrowtable.getColumnModel().getColumn(i).setHeaderRenderer(headerRenderer);
        }

        // Set grid properties for viewborrowtable
        viewborrowtable.setShowGrid(true);
        viewborrowtable.setGridColor(Color.LIGHT_GRAY);
        viewborrowtable.setIntercellSpacing(new Dimension(1, 1));

        // Center specific columns for viewborrowtable (example: column 1 and 2)
        CenteredTableCellRenderer cellRenderer = new CenteredTableCellRenderer();
        viewborrowtable.getColumnModel().getColumn(1).setCellRenderer(cellRenderer); // Center column 1 (Book ID)
        viewborrowtable.getColumnModel().getColumn(2).setCellRenderer(cellRenderer);
        viewborrowtable.getColumnModel().getColumn(3).setCellRenderer(cellRenderer);// Center column 2 (Title)
        viewborrowtable.getColumnModel().getColumn(4).setCellRenderer(cellRenderer);
        viewborrowtable.getColumnModel().getColumn(5).setCellRenderer(cellRenderer);
        
        // Header Renderer for returnedtable
        DefaultTableCellRenderer borrowedHeaderRenderer = new DefaultTableCellRenderer();
        borrowedHeaderRenderer.setHorizontalAlignment(JLabel.CENTER); // Center align the headers
        borrowedHeaderRenderer.setBackground(new Color(210, 232, 237)); // Set background color for returnedtable header

        for (int i = 0; i < returnedtable.getModel().getColumnCount(); i++) {
            returnedtable.getColumnModel().getColumn(i).setHeaderRenderer(borrowedHeaderRenderer);
        }

        // Set grid properties for returnedtable
        returnedtable.setShowGrid(true);
        returnedtable.setGridColor(Color.LIGHT_GRAY);

        // Center specific columns for returnedtable (example: column 1 and 2)
        returnedtable.getColumnModel().getColumn(0).setCellRenderer(cellRenderer);
        returnedtable.getColumnModel().getColumn(1).setCellRenderer(cellRenderer);
        returnedtable.getColumnModel().getColumn(2).setCellRenderer(cellRenderer);// Center column 1 (Book ID)
        returnedtable.getColumnModel().getColumn(3).setCellRenderer(cellRenderer); // Center column 2 (Title)
        returnedtable.getColumnModel().getColumn(4).setCellRenderer(cellRenderer);
        returnedtable.getColumnModel().getColumn(5).setCellRenderer(cellRenderer);
        returnedtable.getColumnModel().getColumn(6).setCellRenderer(cellRenderer);
        
    }
    
    
    private void initializeTable() {
        // Create a DefaultTableModel with columns
        DefaultTableModel model = new DefaultTableModel();
        model.setColumnIdentifiers(new Object[]{"Select", "Transaction ID", "Book Title", "Checkout Date", "Due Date", "Return Date"});

        viewborrowtable.setModel(model);

        // Set font size for the header
        JTableHeader header = viewborrowtable.getTableHeader();
        header.setFont(new Font("Century Gothic", Font.BOLD, 12)); // Set the desired font and size
        header.setForeground(Color.BLACK); // Example text color for header

        // Set font for the table rows
       
        viewborrowtable.setRowHeight(15); // Set row height

        // Make gridlines visible
        viewborrowtable.setShowGrid(true); // Show gridlines
        
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
        retuser = new javax.swing.JLabel();
        jPanel8 = new javax.swing.JPanel();
        rethistory = new javax.swing.JLabel();
        jPanel4 = new javax.swing.JPanel();
        retdash = new javax.swing.JLabel();
        jPanel3 = new javax.swing.JPanel();
        retview = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        retborrow = new javax.swing.JLabel();
        jPanel6 = new javax.swing.JPanel();
        jLabel4 = new javax.swing.JLabel();
        reticon = new javax.swing.JLabel();
        jPanel5 = new javax.swing.JPanel();
        retlogout = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        jPanel7 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        returnedtable = new javax.swing.JTable();
        undoreturn = new javax.swing.JButton();
        viewcheck = new javax.swing.JCheckBox();
        jLabel3 = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        viewborrowtable = new javax.swing.JTable();
        returnbtn1 = new javax.swing.JButton();
        jLabel2 = new javax.swing.JLabel();
        returnhistorybtn = new javax.swing.JButton();
        printreturbbtn = new javax.swing.JButton();
        jLabel6 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setPreferredSize(new java.awt.Dimension(900, 540));
        getContentPane().setLayout(null);

        jPanel1.setBackground(new java.awt.Color(131, 148, 172));
        jPanel1.setPreferredSize(new java.awt.Dimension(120, 520));
        jPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        retuser.setFont(new java.awt.Font("Century Gothic", 0, 14)); // NOI18N
        retuser.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        retuser.setText("USERNAME");
        jPanel1.add(retuser, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 80, 120, 20));

        jPanel8.setBackground(new java.awt.Color(204, 204, 204));
        jPanel8.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        rethistory.setFont(new java.awt.Font("Century Gothic", 0, 14)); // NOI18N
        rethistory.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        rethistory.setText("HISTORY");

        javax.swing.GroupLayout jPanel8Layout = new javax.swing.GroupLayout(jPanel8);
        jPanel8.setLayout(jPanel8Layout);
        jPanel8Layout.setHorizontalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel8Layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(rethistory, javax.swing.GroupLayout.PREFERRED_SIZE, 116, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        jPanel8Layout.setVerticalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(rethistory, javax.swing.GroupLayout.DEFAULT_SIZE, 21, Short.MAX_VALUE)
        );

        jPanel1.add(jPanel8, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 260, 120, 25));

        jPanel4.setBackground(new java.awt.Color(204, 204, 204));
        jPanel4.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        retdash.setFont(new java.awt.Font("Century Gothic", 0, 14)); // NOI18N
        retdash.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        retdash.setText("DASHBOARD");

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addComponent(retdash, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addComponent(retdash, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );

        jPanel1.add(jPanel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 140, 120, 30));

        jPanel3.setBackground(new java.awt.Color(204, 204, 204));
        jPanel3.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        retview.setFont(new java.awt.Font("Century Gothic", 0, 14)); // NOI18N
        retview.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        retview.setText("VIEW BOOKS");

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(retview, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 116, Short.MAX_VALUE)
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(retview, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 31, Short.MAX_VALUE)
        );

        jPanel1.add(jPanel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 170, 120, -1));

        jPanel2.setBackground(new java.awt.Color(204, 204, 204));
        jPanel2.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        retborrow.setFont(new java.awt.Font("Century Gothic", 0, 14)); // NOI18N
        retborrow.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        retborrow.setText("BORROW");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(retborrow, javax.swing.GroupLayout.DEFAULT_SIZE, 116, Short.MAX_VALUE)
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(retborrow, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        jPanel1.add(jPanel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 200, 120, 30));

        jPanel6.setBackground(new java.awt.Color(0, 51, 102));
        jPanel6.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jLabel4.setFont(new java.awt.Font("Century Gothic", 1, 18)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(255, 255, 255));
        jLabel4.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel4.setText("RETURN");

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel4, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 116, Short.MAX_VALUE)
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel6Layout.createSequentialGroup()
                .addGap(0, 1, Short.MAX_VALUE)
                .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        jPanel1.add(jPanel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 230, 120, 30));

        reticon.setIcon(new javax.swing.ImageIcon("C:\\Users\\ADMIN\\Documents\\NetBeansProjects\\MyLibrary\\src\\icons\\defaulticon.png")); // NOI18N
        jPanel1.add(reticon, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 120, 80));

        jPanel5.setBackground(new java.awt.Color(204, 51, 0));
        jPanel5.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jPanel5.setForeground(new java.awt.Color(255, 255, 255));

        retlogout.setFont(new java.awt.Font("Century Gothic", 1, 12)); // NOI18N
        retlogout.setForeground(new java.awt.Color(255, 255, 255));
        retlogout.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        retlogout.setText("LOGOUT");

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addComponent(retlogout, javax.swing.GroupLayout.PREFERRED_SIZE, 116, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel5Layout.createSequentialGroup()
                .addComponent(retlogout, javax.swing.GroupLayout.DEFAULT_SIZE, 20, Short.MAX_VALUE)
                .addContainerGap())
        );

        jPanel1.add(jPanel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 480, 120, 30));

        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        jLabel1.setIcon(new javax.swing.ImageIcon("C:\\Users\\ADMIN\\Documents\\NetBeansProjects\\MyLibrary\\src\\das (1).png")); // NOI18N
        jLabel1.setVerticalAlignment(javax.swing.SwingConstants.BOTTOM);
        jLabel1.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jPanel1.add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 120, 530));

        jPanel7.setBackground(new java.awt.Color(84, 136, 136));
        jPanel7.setPreferredSize(new java.awt.Dimension(900, 540));

        javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 120, Short.MAX_VALUE)
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 80, Short.MAX_VALUE)
        );

        jPanel1.add(jPanel7, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 120, 80));

        getContentPane().add(jPanel1);
        jPanel1.setBounds(0, 0, 120, 530);

        returnedtable.setBackground(new java.awt.Color(204, 204, 255));
        returnedtable.setFont(new java.awt.Font("Century Gothic", 0, 11)); // NOI18N
        returnedtable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "TRID", "ID", "TITLE", "CHECKOUT", "DUE DATE", "RETURN ", "STATUS"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Integer.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        returnedtable.setGridColor(new java.awt.Color(51, 51, 51));
        returnedtable.setRowHeight(25);
        returnedtable.setSelectionBackground(new java.awt.Color(204, 204, 204));
        returnedtable.setSelectionForeground(new java.awt.Color(51, 51, 51));
        returnedtable.setShowVerticalLines(true);
        jScrollPane1.setViewportView(returnedtable);
        if (returnedtable.getColumnModel().getColumnCount() > 0) {
            returnedtable.getColumnModel().getColumn(0).setMinWidth(50);
            returnedtable.getColumnModel().getColumn(0).setPreferredWidth(50);
            returnedtable.getColumnModel().getColumn(0).setMaxWidth(50);
            returnedtable.getColumnModel().getColumn(1).setMinWidth(50);
            returnedtable.getColumnModel().getColumn(1).setPreferredWidth(50);
            returnedtable.getColumnModel().getColumn(1).setMaxWidth(50);
            returnedtable.getColumnModel().getColumn(2).setPreferredWidth(150);
            returnedtable.getColumnModel().getColumn(3).setMinWidth(90);
            returnedtable.getColumnModel().getColumn(3).setPreferredWidth(90);
            returnedtable.getColumnModel().getColumn(3).setMaxWidth(90);
            returnedtable.getColumnModel().getColumn(4).setMinWidth(90);
            returnedtable.getColumnModel().getColumn(4).setPreferredWidth(90);
            returnedtable.getColumnModel().getColumn(4).setMaxWidth(90);
            returnedtable.getColumnModel().getColumn(5).setMinWidth(90);
            returnedtable.getColumnModel().getColumn(5).setPreferredWidth(90);
            returnedtable.getColumnModel().getColumn(5).setMaxWidth(90);
            returnedtable.getColumnModel().getColumn(6).setMinWidth(120);
            returnedtable.getColumnModel().getColumn(6).setPreferredWidth(120);
            returnedtable.getColumnModel().getColumn(6).setMaxWidth(120);
        }

        getContentPane().add(jScrollPane1);
        jScrollPane1.setBounds(140, 280, 730, 190);

        undoreturn.setBackground(new java.awt.Color(204, 0, 0));
        undoreturn.setFont(new java.awt.Font("Century Gothic", 1, 12)); // NOI18N
        undoreturn.setForeground(new java.awt.Color(255, 255, 255));
        undoreturn.setText("UNDO");
        undoreturn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                undoreturnActionPerformed(evt);
            }
        });
        getContentPane().add(undoreturn);
        undoreturn.setBounds(300, 470, 120, 25);

        viewcheck.setFont(new java.awt.Font("Segoe UI", 0, 10)); // NOI18N
        viewcheck.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        viewcheck.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        viewcheck.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        viewcheck.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                viewcheckActionPerformed(evt);
            }
        });
        getContentPane().add(viewcheck);
        viewcheck.setBounds(190, 80, 20, 10);

        jLabel3.setBackground(new java.awt.Color(204, 204, 255));
        jLabel3.setFont(new java.awt.Font("Century Gothic", 1, 13)); // NOI18N
        jLabel3.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel3.setText("RETURNED BOOKS");
        getContentPane().add(jLabel3);
        jLabel3.setBounds(100, 250, 200, 40);

        viewborrowtable.setBackground(new java.awt.Color(210, 232, 237));
        viewborrowtable.setFont(new java.awt.Font("Century Gothic", 0, 11)); // NOI18N
        viewborrowtable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "", "TRID", "ID", "TITLE", "CHECKOUT", "DUE DATE"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Boolean.class, java.lang.Integer.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class
            };
            boolean[] canEdit = new boolean [] {
                true, false, false, false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        viewborrowtable.setAlignmentX(1.0F);
        viewborrowtable.setAlignmentY(1.0F);
        viewborrowtable.setGridColor(new java.awt.Color(51, 51, 51));
        viewborrowtable.setRowHeight(25);
        viewborrowtable.setSelectionBackground(new java.awt.Color(204, 204, 204));
        viewborrowtable.setSelectionForeground(new java.awt.Color(51, 51, 51));
        jScrollPane2.setViewportView(viewborrowtable);
        if (viewborrowtable.getColumnModel().getColumnCount() > 0) {
            viewborrowtable.getColumnModel().getColumn(0).setMinWidth(50);
            viewborrowtable.getColumnModel().getColumn(0).setPreferredWidth(50);
            viewborrowtable.getColumnModel().getColumn(0).setMaxWidth(50);
            viewborrowtable.getColumnModel().getColumn(1).setMinWidth(80);
            viewborrowtable.getColumnModel().getColumn(1).setPreferredWidth(80);
            viewborrowtable.getColumnModel().getColumn(1).setMaxWidth(80);
            viewborrowtable.getColumnModel().getColumn(2).setMinWidth(50);
            viewborrowtable.getColumnModel().getColumn(2).setPreferredWidth(50);
            viewborrowtable.getColumnModel().getColumn(2).setMaxWidth(50);
            viewborrowtable.getColumnModel().getColumn(3).setMinWidth(270);
            viewborrowtable.getColumnModel().getColumn(3).setPreferredWidth(270);
            viewborrowtable.getColumnModel().getColumn(3).setMaxWidth(270);
            viewborrowtable.getColumnModel().getColumn(4).setMinWidth(90);
            viewborrowtable.getColumnModel().getColumn(4).setPreferredWidth(90);
            viewborrowtable.getColumnModel().getColumn(4).setMaxWidth(90);
            viewborrowtable.getColumnModel().getColumn(5).setMinWidth(90);
            viewborrowtable.getColumnModel().getColumn(5).setPreferredWidth(90);
            viewborrowtable.getColumnModel().getColumn(5).setMaxWidth(90);
        }

        getContentPane().add(jScrollPane2);
        jScrollPane2.setBounds(180, 70, 630, 170);

        returnbtn1.setBackground(new java.awt.Color(210, 232, 237));
        returnbtn1.setFont(new java.awt.Font("Century Gothic", 1, 12)); // NOI18N
        returnbtn1.setText("RETURN");
        returnbtn1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                returnbtn1ActionPerformed(evt);
            }
        });
        getContentPane().add(returnbtn1);
        returnbtn1.setBounds(440, 240, 120, 25);

        jLabel2.setFont(new java.awt.Font("Century Gothic", 1, 24)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(51, 51, 51));
        jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel2.setText("RETURN BOOKS");
        getContentPane().add(jLabel2);
        jLabel2.setBounds(120, 0, 770, 50);

        returnhistorybtn.setBackground(new java.awt.Color(51, 51, 51));
        returnhistorybtn.setFont(new java.awt.Font("Century Gothic", 1, 12)); // NOI18N
        returnhistorybtn.setForeground(new java.awt.Color(255, 255, 255));
        returnhistorybtn.setText("HISTORY");
        returnhistorybtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                returnhistorybtnActionPerformed(evt);
            }
        });
        getContentPane().add(returnhistorybtn);
        returnhistorybtn.setBounds(540, 470, 120, 25);

        printreturbbtn.setFont(new java.awt.Font("Century Gothic", 1, 12)); // NOI18N
        printreturbbtn.setText("PRINT");
        printreturbbtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                printreturbbtnActionPerformed(evt);
            }
        });
        getContentPane().add(printreturbbtn);
        printreturbbtn.setBounds(420, 470, 120, 25);

        jLabel6.setBackground(new java.awt.Color(204, 204, 255));
        jLabel6.setFont(new java.awt.Font("Century Gothic", 1, 13)); // NOI18N
        jLabel6.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel6.setText("BORROWED BOOKS");
        getContentPane().add(jLabel6);
        jLabel6.setBounds(150, 40, 190, 40);

        jLabel5.setFont(new java.awt.Font("Century Gothic", 0, 11)); // NOI18N
        jLabel5.setIcon(new javax.swing.ImageIcon("C:\\Users\\ADMIN\\Documents\\NetBeansProjects\\MyLibrary\\src\\1.png")); // NOI18N
        jLabel5.setText("jLabel5");
        getContentPane().add(jLabel5);
        jLabel5.setBounds(120, -70, 950, 670);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void undoreturnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_undoreturnActionPerformed
        // TODO add your handling code here:
        int selectedRow = returnedtable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a returned book to undo.", "No Book Selected", JOptionPane.WARNING_MESSAGE);
            return;
        }

        DefaultTableModel returnedModel = (DefaultTableModel) returnedtable.getModel();
        int transactionId = (int) returnedModel.getValueAt(selectedRow, 0); // Transaction ID
        int bookId = (int) returnedModel.getValueAt(selectedRow, 1); // Book ID (assuming it's in the second column)
        String title = returnedModel.getValueAt(selectedRow, 2).toString(); // Title of the book

// Get Checkout and Due dates (assuming they are in the correct columns)
        String checkoutDateStr = returnedModel.getValueAt(selectedRow, 3).toString(); // Checkout date
        String dueDateStr = returnedModel.getValueAt(selectedRow, 4).toString();      // Due date

// Log the values for debugging purposes
        System.out.println("Transaction ID: " + transactionId);
        System.out.println("Book ID: " + bookId);
        System.out.println("Title: " + title);
        System.out.println("Checkout Date: " + checkoutDateStr);
        System.out.println("Due Date: " + dueDateStr);

// Update the transaction in the database
        String sql = "UPDATE transactions SET status = 'borrowed', return_date = NULL WHERE transaction_id = ?";
        try (Connection conn = DbConnection.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, transactionId);  // Set the transaction ID

            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected > 0) {
                // Successfully undone the return, now update the tables
                returnedModel.removeRow(selectedRow); // Remove from returned table

                // Add the book back to the borrowed list (viewborrowedtable)
                DefaultTableModel borrowModel = (DefaultTableModel) viewborrowtable.getModel();
                borrowModel.addRow(new Object[]{false,  transactionId,bookId, title, checkoutDateStr, dueDateStr}); // Add Book ID and Transaction ID

                JOptionPane.showMessageDialog(this, "Book return undone successfully!", "Undo Success", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this, "Failed to undo the return.", "Undo Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error undoing return: " + e.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_undoreturnActionPerformed

    private void viewcheckActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_viewcheckActionPerformed
        // TODO add your handling code here:
        JCheckBox selectAllCheckbox = (JCheckBox) evt.getSource(); // Get the source of the event

        // Check if the checkbox is selected
        boolean isSelected = selectAllCheckbox.isSelected();

        // Loop through the viewborrowtable to select/deselect rows
        DefaultTableModel model = (DefaultTableModel) viewborrowtable.getModel();

        for (int i = 0; i < model.getRowCount(); i++) {
            // Assuming the first column (index 0) is a checkbox
            viewborrowtable.setValueAt(isSelected, i, 0); // Set the checkbox state
        }
    
    }//GEN-LAST:event_viewcheckActionPerformed

    private void returnbtn1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_returnbtn1ActionPerformed
        // TODO add your handling code here:
        DefaultTableModel borrowModel = (DefaultTableModel) viewborrowtable.getModel();
        DefaultTableModel returnModel = (DefaultTableModel) returnedtable.getModel();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String returnDate = sdf.format(new Date());

        Connection conn = null;
        PreparedStatement pstmt = null;
        PreparedStatement updateQuantityStmt = null;

        try {
            // Establish a connection to the database
            conn = DbConnection.getConnection();

            // Loop through the viewborrowtable to check which rows are selected (checked)
            for (int i = borrowModel.getRowCount() - 1; i >= 0; i--) {
                Boolean isSelected = (Boolean) borrowModel.getValueAt(i, 0); // First column is the checkbox

                if (isSelected != null && isSelected) {
                    // Retrieve the data from the selected row
                    int transactionId = Integer.parseInt(borrowModel.getValueAt(i, 1).toString()); // Parse TR# as integer
                    int bookId = Integer.parseInt(borrowModel.getValueAt(i, 2).toString()); // Parse Book ID as integer
                    String title = borrowModel.getValueAt(i, 3).toString(); // Book Title
                    String checkoutDate = borrowModel.getValueAt(i, 4).toString(); // Checkout Date
                    String dueDate = borrowModel.getValueAt(i, 5).toString(); // Due Date

                    // Parse the due date to compare with the current date
                    Date dueDateParsed = null;
                    try {
                        dueDateParsed = sdf.parse(dueDate);
                    } catch (ParseException e) {
                        JOptionPane.showMessageDialog(null, "Error parsing due date: " + e.getMessage());
                        continue; // Skip to the next iteration if parsing fails
                    }

                    // Determine the status (Overdue if returnDate is after dueDate)
                    String status;
                    Date currentDate = new Date();
                    if (currentDate.after(dueDateParsed)) {
                        status = "Overdue";
                    } else {
                        status = "Returned";
                    }

                    // Prepare the row to be added to the returnedtable
                    Object[] returnRow = new Object[]{
                        transactionId, // TR#
                        bookId, // Book ID
                        title, // Title
                        checkoutDate, // Checkout Date
                        dueDate, // Due Date
                        returnDate, // Return Date (current date)
                        status // Status (Overdue or Returned)
                    };

                    // Add the row to the returnedtable
                    returnModel.addRow(returnRow);

                    // Remove the row from the viewborrowtable after it's added to the returnedtable
                    borrowModel.removeRow(i);

                    // Update the transactions table in the database
                    String updateQuery = "UPDATE transactions SET return_date = ?, status = ? WHERE transaction_id = ?";
                    pstmt = conn.prepareStatement(updateQuery);
                    pstmt.setString(1, returnDate); // Set the return date to the current date
                    pstmt.setString(2, status);     // Set the status (Overdue/Returned)
                    pstmt.setInt(3, transactionId); // Set the transaction ID

                    // Execute the update query
                    int transactionUpdateResult = pstmt.executeUpdate();
                    if (transactionUpdateResult > 0) {
                        System.out.println("Transaction updated successfully for TR#: " + transactionId);
                    } else {
                        System.out.println("Transaction update failed for TR#: " + transactionId);
                    }

                    // Step to increment the book quantity in the books table
                    String updateQuantityQuery = "UPDATE books SET quantity = quantity + 1 WHERE book_id = ?";
                    updateQuantityStmt = conn.prepareStatement(updateQuantityQuery);
                    updateQuantityStmt.setInt(1, bookId); // Set the book ID

                    // Execute the query to update the book quantity
                    int quantityUpdateResult = updateQuantityStmt.executeUpdate();
                    if (quantityUpdateResult > 0) {
                        System.out.println("Book quantity updated successfully for Book ID: " + bookId);
                    } else {
                        System.out.println("Failed to update book quantity for Book ID: " + bookId);
                    }
                }
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error updating transaction in database: " + e.getMessage());
        } finally {
            try {
                if (pstmt != null) {
                    pstmt.close();
                }
                if (updateQuantityStmt != null) {
                    updateQuantityStmt.close();
                }
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(null, "Error closing database connection: " + e.getMessage());
            }
        }
    
    }//GEN-LAST:event_returnbtn1ActionPerformed

    private void returnhistorybtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_returnhistorybtnActionPerformed
        // TODO add your handling code here:
         LibHistory libHistoryFrame = new LibHistory(userId, username);
    
    // Set the frame to be visible
    libHistoryFrame.setVisible(true);
    this.dispose(); 
    
  
    }//GEN-LAST:event_returnhistorybtnActionPerformed
   
    private String getFullNameFromDatabase(String username) {
        String fullName = null;
        String sql = "SELECT full_name FROM users WHERE username = ?";
        try (Connection conn = DbConnection.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, username);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                fullName = rs.getString("full_name"); // Assuming your column is named 'full_name'
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return fullName;
    }
    
    private JTextArea txtReceipt;
    private void generateReceipt(String fullName) {
        SimpleDateFormat sdf = new SimpleDateFormat("MMMM dd, yyyy HH:mm:ss");
        StringBuilder receiptText = new StringBuilder();
        receiptText.append("====== BOOKWORM CENTRAL ======\n")
                .append("Date: ").append(sdf.format(new Date())).append("\n")
                .append("Borrower: ").append(fullName).append("\n")
                .append("User ID: ").append(userId).append("\n")
                .append("Username: ").append(username).append("\n\n")
                .append("Returned Books:\n");

        DefaultTableModel model = (DefaultTableModel) returnedtable.getModel();
        int[] selectedRows = returnedtable.getSelectedRows(); // Get selected rows

        if (selectedRows.length == 0) {
            JOptionPane.showMessageDialog(this, "No books selected for receipt.", "Warning", JOptionPane.WARNING_MESSAGE);
            return; // Exit the method if no rows are selected
        }

        // Loop through the selected rows and build the receipt
        for (int selectedIndex : selectedRows) {
            int transactionId = (int) model.getValueAt(selectedIndex, 0);
            int id = (int) model.getValueAt(selectedIndex, 1);
            String title = model.getValueAt(selectedIndex, 2).toString();
            String checkout = model.getValueAt(selectedIndex, 3).toString();
            String returnedDate = model.getValueAt(selectedIndex, 5).toString(); // Assuming returned date is in column 5
            String status = model.getValueAt(selectedIndex, 6).toString(); // Assuming status is in column 6

            receiptText.append("Transaction ID: ").append(transactionId).append("\n")
                    .append("Book ID: ").append(id).append("\n")
                    .append("Title: ").append(title).append("\n")
                    .append("Checkout Date: ").append(checkout).append("\n")
                    .append("Returned Date: ").append(returnedDate).append("\n") // Add returned date
                    .append("Status: ").append(status).append("\n\n"); // Add status
        }

        receiptText.append("Happy Reading, ").append(username != null && !username.isEmpty()
                ? Character.toUpperCase(username.charAt(0)) + username.substring(1).toLowerCase()
                : username).append("!\n"); // Capitalize only here

        receiptText.append("=====================================");

        ImageIcon icon = new ImageIcon("C:\\Users\\ADMIN\\Documents\\NetBeansProjects\\MyLibrary\\src\\iconop.png");

        txtReceipt = new JTextArea(receiptText.toString());
        txtReceipt.setEditable(false);
        txtReceipt.setFont(new Font("Monospaced", Font.PLAIN, 12)); // Monospaced font for better alignment

        JScrollPane scrollPane = new JScrollPane(txtReceipt);

        // Show the receipt with the new custom icon
        JOptionPane.showMessageDialog(this, scrollPane, "Borrowed Books Receipt", JOptionPane.INFORMATION_MESSAGE, icon);
    }
    private void printreturbbtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_printreturbbtnActionPerformed
        // TODO add your handling code here:
       
        String fullName = getFullNameFromDatabase(username); // Fetch the full name based on the username
        generateReceipt(fullName); // Generate the receipt

        // Proceed to print as already defined in printbtnActionPerformed
        PrinterJob job = PrinterJob.getPrinterJob();
        job.setJobName("Print Returned Books Receipt");

        job.setPrintable(new Printable() {
            @Override
            public int print(Graphics g, PageFormat pf, int pageIndex) throws PrinterException {
                if (pageIndex > 0) {
                    return Printable.NO_SUCH_PAGE;
                }

                Graphics2D g2d = (Graphics2D) g;
                g2d.translate(pf.getImageableX(), pf.getImageableY());

                g2d.setFont(new Font("Courier New", Font.PLAIN, 12));
                int y = 0;
                for (String line : txtReceipt.getText().split("\n")) {
                    y += g2d.getFontMetrics().getHeight();
                    g2d.drawString(line, 0, y);
                }

                return Printable.PAGE_EXISTS;
            }
        });

        if (job.printDialog()) {
            try {
                job.print();
            } catch (PrinterException e) {
                System.err.println(e.getMessage());
            }
        }
    
    }//GEN-LAST:event_printreturbbtnActionPerformed

    private void loadBorrowedBooks(int userId) {
        Connection conn = null; 
        PreparedStatement ps = null; // Declare PreparedStatement variable
        ResultSet rs = null; // Declare ResultSet variable

        try {
            // Get the database connection
            conn = DbConnection.getConnection(); // Change this line to call your connection method

            // SQL query to retrieve borrowed books for the specific user
            String query = "SELECT t.transaction_id, b.book_id, b.title, t.checkout_date, t.due_date "
                         + "FROM transactions t "
                         + "JOIN books b ON t.book_id = b.book_id "
                         + "WHERE t.user_id = ? AND t.status = 'borrowed'";

            // Prepare the SQL statement
            ps = conn.prepareStatement(query);
            ps.setInt(1, userId); // Set the user ID parameter in the query

            // Execute the query and get the results
            rs = ps.executeQuery();

            // Clear existing rows from the viewborrowtable before adding new data
            DefaultTableModel model = (DefaultTableModel) viewborrowtable.getModel();
            model.setRowCount(0); // Clear existing rows

            // Check if ResultSet has data
            if (!rs.isBeforeFirst()) {
            
            } else {
                // Loop through the result set and populate the table model
                while (rs.next()) {
                    Object[] row = new Object[]{
                        false, // First column: boolean value (default to false for checkbox)
                        rs.getInt("transaction_id"), // TR#
                        rs.getInt("book_id"), // Book ID
                        rs.getString("title"), // Book Title
                        rs.getDate("checkout_date"), // Checkout Date
                        rs.getDate("due_date") // Due Date
                    };

                    // Add the borrowed book to the table model
                    model.addRow(row);
                }
            }

        } catch (SQLException e) {
            // Show error message if there is a problem with database operations
            JOptionPane.showMessageDialog(null, "Error loading borrowed books: " + e.getMessage());
        } finally {
            // Ensure the database resources are closed properly
            try {
                if (rs != null) rs.close(); // Close ResultSet if it's not null
                if (ps != null) ps.close(); // Close PreparedStatement if it's not null
                if (conn != null) conn.close(); // Close Connection if it's not null
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(null, "Error closing resources: " + e.getMessage());
            }
        }
    }
    
    @Override
    public void setVisible(boolean isVisible) {
        super.setVisible(isVisible);
        if (isVisible) {
            loadBorrowedBooks(userId); // Reload data when frame becomes visible
        }
    }
    
    
    private void loadTransactionData(int userId) {
        // SQL query to fetch transaction data for the given user
        String sql = "SELECT t.transaction_id, t.user_id, b.title, t.checkout_date, t.due_date, t.return_date "
                + "FROM transactions t "
                + "INNER JOIN books b ON t.book_id = b.book_id "
                + "WHERE t.user_id = ?";

        // Establishing the connection and preparing the statement
        try (Connection conn = DbConnection.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {

            // Set the user_id parameter in the query
            pstmt.setInt(1, userId);

            ResultSet rs = pstmt.executeQuery();

            // Assuming you have a DefaultTableModel for your viewborrowtable
            DefaultTableModel model = (DefaultTableModel) viewborrowtable.getModel();
            model.setRowCount(0);  // Clear existing rows in the table

            // Loop through the ResultSet to populate the table
            while (rs.next()) {
                // Retrieve data from the ResultSet
                int transactionId = rs.getInt("transaction_id");
                String title = rs.getString("title");
                Date checkoutDate = rs.getDate("checkout_date");
                Date dueDate = rs.getDate("due_date");
                Date returnDate = rs.getDate("return_date");

                // Add the data to the viewborrowtable with a boolean in the first column (default to false)
                model.addRow(new Object[]{
                    false, // First column: boolean value (default to false for checkbox)
                    transactionId, // TR#
                    title, // Book Title
                    checkoutDate, // Checkout Date
                    dueDate, // Due Date
                    returnDate // Return Date
                });
            }

        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error fetching transaction data: " + e.getMessage());
        }
    }
    
    private void loadReturnedBooks(int userId) {
        String sql = "SELECT t.transaction_id, t.book_id, b.title, t.checkout_date, t.due_date, t.return_date, t.status "
                + "FROM transactions t "
                + "INNER JOIN books b ON t.book_id = b.book_id "
                + "WHERE t.return_date IS NOT NULL AND t.user_id = ?"; // Filter by user ID

        try (Connection conn = DbConnection.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, userId); // Set the user_id filter

            try (ResultSet rs = pstmt.executeQuery()) {
                DefaultTableModel returnModel = (DefaultTableModel) returnedtable.getModel();
                returnModel.setRowCount(0); // Clear existing rows

                while (rs.next()) {
                    // Retrieve data from the result set
                    int transactionId = rs.getInt("transaction_id");
                    int bookId = rs.getInt("book_id");
                    String title = rs.getString("title");
                    Date checkoutDate = rs.getDate("checkout_date");
                    Date dueDate = rs.getDate("due_date");
                    Date returnDate = rs.getDate("return_date");
                    String status = rs.getString("status");

                    // Add the returned book data to the returnedtable
                    returnModel.addRow(new Object[]{
                        transactionId, // TR#
                        bookId, // Book ID
                        title, // Title
                        checkoutDate, // Checkout Date
                        dueDate, // Due Date
                        returnDate, // Return Date
                        status // Status (Overdue/Returned)
                    });
                }
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error loading returned books: " + e.getMessage());
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
            java.util.logging.Logger.getLogger(LibReturn.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(() -> {
            int userId1 = 0;
            String username1 = "Default User";
            new LibReturn(userId1, username1).setVisible(true);
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JButton printreturbbtn;
    private javax.swing.JLabel retborrow;
    private javax.swing.JLabel retdash;
    private javax.swing.JLabel rethistory;
    private javax.swing.JLabel reticon;
    private javax.swing.JLabel retlogout;
    private javax.swing.JButton returnbtn1;
    private javax.swing.JTable returnedtable;
    private javax.swing.JButton returnhistorybtn;
    private javax.swing.JLabel retuser;
    private javax.swing.JLabel retview;
    private javax.swing.JButton undoreturn;
    private javax.swing.JTable viewborrowtable;
    private javax.swing.JCheckBox viewcheck;
    // End of variables declaration//GEN-END:variables
}
