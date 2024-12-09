/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package mylibrary;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.print.PageFormat;
import java.awt.print.Printable;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import java.text.SimpleDateFormat; 
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.Font;
import java.awt.Image;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import javax.swing.ImageIcon;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author ADMIN
 */
public class LibBorrow extends javax.swing.JFrame {
    private static LibBorrow instance;
    final int userId;
    final String username;
    private javax.swing.JTextField duetext1; 
    private static String DEFAULT_ICON_PATH = "src/icons/defaulticon.png";   
 
    
    
    private Connection connect() {
        Connection conn = null;
        try {
            conn = DriverManager.getConnection("jdbc:mysql://localhost/library_db", "root", "");
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Failed to connect to database: " + e.getMessage());
        }
        return conn;
    }

    private void customizeTableColumns() {
    // Create a renderer that centers the text
    DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
    centerRenderer.setHorizontalAlignment(SwingConstants.CENTER); // Use SwingConstants.CENTER for clarity

    // Apply the renderer to each column of borrowedtable
    for (int i = 0; i < borrowedtable.getColumnCount(); i++) {
        borrowedtable.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
    }

    // Apply the renderer to each column of selecttable
    for (int i = 0; i < selecttable.getColumnCount(); i++) {
        selecttable.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
    }
        borrowedtable.revalidate();
        borrowedtable.repaint();

        selecttable.revalidate();
        selecttable.repaint();
}
    private void loadBookInfoByID(int id) {
        Connection conn = connect();
        if (conn != null) {
            try {
                String query = "SELECT title, genre, author, quantity FROM books WHERE book_id = ?";
                PreparedStatement ps = conn.prepareStatement(query);
                ps.setInt(1, id); 

                ResultSet rs = ps.executeQuery();

                if (rs.next()) {
                    // Fill in the text fields with the book info
                    titletext.setText(rs.getString("title"));
                    genretext.setText(rs.getString("genre"));
                    authortext.setText(rs.getString("author"));
                    qtytext.setText(rs.getString("quantity"));
                  

                    // Make titletext non-editable
                    titletext.setEditable(false);
                    genretext.setEditable(false);
                    authortext.setEditable(false);
                    qtytext.setEditable(false);

                } else {
                    JOptionPane.showMessageDialog(null, "No book found with that ID.");
                }

            } catch (SQLException e) {
                JOptionPane.showMessageDialog(null, "Error fetching book details: " + e.getMessage());
            } finally {
                try {
                    conn.close();
                } catch (SQLException ex) {
                }
            }
        }
    }
    
    
    

    
    

    /**
     * Creates new form LibBorrow
     */
    public LibBorrow(int userId, String username) {
        this.userId = userId;
        this.username = username;
        this.borrowedtable = new JTable();
         loadBorrowedBooks(userId);
        loadTransactionData();
        initComponents();
        setUsername(username);
        setLocationRelativeTo(null);
        duedatechooser.setDate(null);
        String userIconPath = getUserIconPath(userId); 
        updateIconInLibBorrow(userIconPath);
        setUserIcon(userId);
         loadUserIcon(userId);
        
   
        
       
        
        
        bordash.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                new LibDashboard(userId, username).setVisible(true); // Open LibView JFrame
                dispose(); // Close the dashboard
            }
        });
        
        
        DefaultTableCellRenderer headerRenderer = new DefaultTableCellRenderer();
        headerRenderer.setHorizontalAlignment(JLabel.CENTER); // Center align the headers
        for (int i = 0; i < selecttable.getModel().getColumnCount(); i++) {
              selecttable.getColumnModel().getColumn(i).setHeaderRenderer(headerRenderer);
              headerRenderer.setBackground(new Color(210,232,237));
              selecttable.setShowGrid(true);
              selecttable.setGridColor(Color.LIGHT_GRAY);
        }
        DefaultTableCellRenderer borrowedHeaderRenderer = new DefaultTableCellRenderer();
        borrowedHeaderRenderer.setHorizontalAlignment(JLabel.CENTER); // Center align the headers
        borrowedHeaderRenderer.setBackground(new Color(210, 232, 237)); // Set background color for borrowedtable header


        for (int i = 0; i < borrowedtable.getModel().getColumnCount(); i++) {
            borrowedtable.getColumnModel().getColumn(i).setHeaderRenderer(borrowedHeaderRenderer);
            borrowedtable.setShowGrid(true);
            borrowedtable.setGridColor(Color.LIGHT_GRAY); 

        }
        boruser.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                new LibUsersInfo(userId, username).setVisible(true);
                dispose();
            }
        });
        
        borview.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
            new LibView(userId,username).setVisible(true); 
                dispose();
            }
        });


        
        borreturn.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                new LibReturn(userId,username).setVisible(true);
                dispose(); 
            }
        });
        
        borhistory.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                new LibHistory(userId, username).setVisible(true); // Open LibView JFrame
                dispose(); // Close the dashboard
            }
        });
        
        borlogout.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int response = JOptionPane.showConfirmDialog(
                        LibBorrow.this,
                        "Are you sure you want to log out?",
                        "Logout Confirmation",
                        JOptionPane.YES_NO_OPTION
                );

                if (response == JOptionPane.YES_OPTION) {
                    // Proceed with logout
                    LibLogin loginScreen = new LibLogin(); 
                    loginScreen.setVisible(true); 
                    dispose();
                }
            }
        });
         
    }
    public static LibBorrow getInstance() {
        return instance; // Return the existing instance
    }

    public static LibBorrow getInstance(int userId, String username) {
        if (instance == null) {
            instance = new LibBorrow(userId, username);
        }
        return instance;
    }
    
       public void updateIconInLibBorrow(String iconPath) {
        if (iconPath != null && !iconPath.isEmpty()) {
            ImageIcon icon = new ImageIcon(iconPath);
            Image scaledImage = icon.getImage().getScaledInstance(boricon.getWidth(), boricon.getHeight(), Image.SCALE_SMOOTH);
            boricon.setIcon(new ImageIcon(scaledImage)); // Assuming you have a JLabel named iconLabel
        }
    }
       
       public void loadUserIcon(int userId) {
        // Attempt to retrieve the user's icon
        ImageIcon icon = UserIconUtility.retrieveUserIcon(userId);

        // Check if a custom icon was found
        if (icon != null) {
            // Scale and set the custom icon
            Image scaledImage = icon.getImage().getScaledInstance(boricon.getWidth(), boricon.getHeight(), Image.SCALE_SMOOTH);
            boricon.setIcon(new ImageIcon(scaledImage));
        } else {
            // If no custom icon is found, set the default icon
            String DEFAULT_ICON_PATH = "src/icons/defaulticon.png"; // Adjust the path accordingly
            ImageIcon defaultIcon = new ImageIcon(DEFAULT_ICON_PATH);
            Image scaledDefaultImage = defaultIcon.getImage().getScaledInstance(boricon.getWidth(), boricon.getHeight(), Image.SCALE_SMOOTH);
            boricon.setIcon(new ImageIcon(scaledDefaultImage));
        }
    }

    public void setUserIcon(int userId) {
        String iconPath = getUserIconPath(userId); // Fetch icon path from the database
        updateIconInLibBorrow(iconPath); // Update the icon
    }

    public String getUserIconPath(int userId) {
        DbConnection db = new DbConnection();
        String iconPath = db.getUserIconPath(userId); // Fetch icon path from the database
        return iconPath; // Return fetched path
    }

    
        private void setUsername(String username) {
         boruser.setText(username.toUpperCase());       
    }
    
        private void addBookToTable() {
        int id = Integer.parseInt(idtext.getText().trim());
        String title = titletext.getText();
        String genre = genretext.getText();
        String author = authortext.getText();
        java.util.Date dueDate = duedatechooser.getDate(); // Get the selected date from JDateChooser

        // Check if any fields are empty
            if (id == 0 || title.isEmpty() || genre.isEmpty() || author.isEmpty() || dueDate == null) {
                JOptionPane.showMessageDialog(null, "Please fill in all fields and select a due date.", "Missing Information", JOptionPane.WARNING_MESSAGE);
                return;
            }

        // Debug print to check selected due date
        System.out.println("Selected Due Date: " + dueDate);

        // Format the date for display (e.g., using SimpleDateFormat)
        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy"); // Change to preferred format
        String formattedDueDate = sdf.format(dueDate);

        // Add to the table
        DefaultTableModel model = (DefaultTableModel) selecttable.getModel();
        model.addRow(new Object[]{false, id, title, formattedDueDate}); // Add the formatted due date to the table

        // Clear fields after adding
        clearFields();
}

    private void clearFields() {
        idtext.setText("");
        titletext.setText("");
        genretext.setText("");
        authortext.setText("");
        qtytext.setText("");
        
    } 

    
    public int getUserId() {
        return userId;
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
        boruser = new javax.swing.JLabel();
        jPanel17 = new javax.swing.JPanel();
        boricon = new javax.swing.JLabel();
        jPanel6 = new javax.swing.JPanel();
        bordash = new javax.swing.JLabel();
        jPanel3 = new javax.swing.JPanel();
        borview = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        borhistory = new javax.swing.JLabel();
        jPanel15 = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        jPanel4 = new javax.swing.JPanel();
        borreturn = new javax.swing.JLabel();
        jPanel7 = new javax.swing.JPanel();
        borlogout = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jPanel8 = new javax.swing.JPanel();
        jLabel11 = new javax.swing.JLabel();
        idtext = new javax.swing.JTextField();
        titletext = new javax.swing.JTextField();
        genretext = new javax.swing.JTextField();
        jPanel9 = new javax.swing.JPanel();
        jLabel14 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        jPanel10 = new javax.swing.JPanel();
        jLabel15 = new javax.swing.JLabel();
        jLabel16 = new javax.swing.JLabel();
        jPanel11 = new javax.swing.JPanel();
        jPanel13 = new javax.swing.JPanel();
        jLabel13 = new javax.swing.JLabel();
        jPanel14 = new javax.swing.JPanel();
        borrowtbtn = new javax.swing.JButton();
        addbookbtn = new javax.swing.JButton();
        allselectbtn = new javax.swing.JCheckBox();
        jScrollPane1 = new javax.swing.JScrollPane();
        selecttable = new javax.swing.JTable();
        authortext = new javax.swing.JTextField();
        clearbookbtn = new javax.swing.JButton();
        deletbttn = new javax.swing.JButton();
        jScrollPane2 = new javax.swing.JScrollPane();
        borrowedtable = new javax.swing.JTable();
        jPanel12 = new javax.swing.JPanel();
        jLabel17 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jPanel5 = new javax.swing.JPanel();
        borrowhistory = new javax.swing.JButton();
        undoborrowbtn = new javax.swing.JButton();
        printbtn = new javax.swing.JButton();
        returnbtn = new javax.swing.JButton();
        duedatechooser = new com.toedter.calendar.JDateChooser();
        qtytext = new javax.swing.JTextField();
        jPanel16 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        bg = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Borrow Books");
        setPreferredSize(new java.awt.Dimension(900, 540));
        getContentPane().setLayout(null);

        jPanel1.setBackground(new java.awt.Color(210, 232, 237));
        jPanel1.setPreferredSize(new java.awt.Dimension(120, 520));
        jPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        boruser.setFont(new java.awt.Font("Century Gothic", 0, 14)); // NOI18N
        boruser.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        boruser.setText("USERNAME");
        jPanel1.add(boruser, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 80, 120, 20));

        jPanel17.setBackground(new java.awt.Color(134, 174, 183));

        boricon.setIcon(new javax.swing.ImageIcon("C:\\Users\\ADMIN\\Documents\\NetBeansProjects\\MyLibrary\\src\\icons\\defaulticon.png")); // NOI18N

        javax.swing.GroupLayout jPanel17Layout = new javax.swing.GroupLayout(jPanel17);
        jPanel17.setLayout(jPanel17Layout);
        jPanel17Layout.setHorizontalGroup(
            jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel17Layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(boricon))
        );
        jPanel17Layout.setVerticalGroup(
            jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel17Layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(boricon))
        );

        jPanel1.add(jPanel17, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 120, 80));

        jPanel6.setBackground(new java.awt.Color(204, 204, 204));
        jPanel6.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        bordash.setFont(new java.awt.Font("Century Gothic", 0, 14)); // NOI18N
        bordash.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        bordash.setText("DASHBOARD");

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel6Layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(bordash, javax.swing.GroupLayout.PREFERRED_SIZE, 116, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(bordash, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 26, Short.MAX_VALUE)
        );

        jPanel1.add(jPanel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 140, 120, 30));

        jPanel3.setBackground(new java.awt.Color(204, 204, 204));
        jPanel3.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        borview.setFont(new java.awt.Font("Century Gothic", 0, 14)); // NOI18N
        borview.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        borview.setText("VIEW BOOKS");

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(borview, javax.swing.GroupLayout.DEFAULT_SIZE, 116, Short.MAX_VALUE)
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(borview, javax.swing.GroupLayout.DEFAULT_SIZE, 26, Short.MAX_VALUE)
        );

        jPanel1.add(jPanel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 170, 120, -1));

        jLabel2.setFont(new java.awt.Font("Century Gothic", 1, 16)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(255, 255, 255));
        jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel2.setText("BORROW");
        jPanel1.add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 200, 120, 30));

        borhistory.setFont(new java.awt.Font("Century Gothic", 0, 14)); // NOI18N
        borhistory.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        borhistory.setText("HISTORY");
        jPanel1.add(borhistory, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 260, 116, 26));

        jPanel15.setBackground(new java.awt.Color(204, 204, 204));
        jPanel15.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        javax.swing.GroupLayout jPanel15Layout = new javax.swing.GroupLayout(jPanel15);
        jPanel15.setLayout(jPanel15Layout);
        jPanel15Layout.setHorizontalGroup(
            jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 116, Short.MAX_VALUE)
        );
        jPanel15Layout.setVerticalGroup(
            jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 26, Short.MAX_VALUE)
        );

        jPanel1.add(jPanel15, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 260, 120, 30));

        jPanel2.setBackground(new java.awt.Color(0, 51, 102));
        jPanel2.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 116, Short.MAX_VALUE)
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 26, Short.MAX_VALUE)
        );

        jPanel1.add(jPanel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 200, 120, 30));

        jPanel4.setBackground(new java.awt.Color(204, 204, 204));
        jPanel4.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        borreturn.setFont(new java.awt.Font("Century Gothic", 0, 14)); // NOI18N
        borreturn.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        borreturn.setText("RETURN");

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(borreturn, javax.swing.GroupLayout.PREFERRED_SIZE, 116, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(borreturn, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        jPanel1.add(jPanel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 230, 120, 30));

        jPanel7.setBackground(new java.awt.Color(204, 51, 0));
        jPanel7.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        borlogout.setFont(new java.awt.Font("Century Gothic", 1, 12)); // NOI18N
        borlogout.setForeground(new java.awt.Color(255, 255, 255));
        borlogout.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        borlogout.setText("LOGOUT");

        javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel7Layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(borlogout, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel7Layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(borlogout, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        jPanel1.add(jPanel7, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 480, 120, 30));

        jLabel6.setFont(new java.awt.Font("Century Gothic", 0, 14)); // NOI18N
        jLabel6.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel6.setIcon(new javax.swing.ImageIcon("C:\\Users\\ADMIN\\Documents\\NetBeansProjects\\MyLibrary\\src\\das (1).png")); // NOI18N
        jLabel6.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jLabel6.setPreferredSize(new java.awt.Dimension(140, 480));
        jPanel1.add(jLabel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 120, 530));

        getContentPane().add(jPanel1);
        jPanel1.setBounds(0, 0, 120, 510);

        jPanel8.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel11.setFont(new java.awt.Font("Century Gothic", 0, 24)); // NOI18N
        jLabel11.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel11.setText("BORROW BOOKS");
        jPanel8.add(jLabel11, new org.netbeans.lib.awtextra.AbsoluteConstraints(280, 0, 210, 40));

        idtext.setBackground(new java.awt.Color(218, 233, 236));
        idtext.setFont(new java.awt.Font("Century Gothic", 0, 11)); // NOI18N
        idtext.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        idtext.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                idtextActionPerformed(evt);
            }
        });
        jPanel8.add(idtext, new org.netbeans.lib.awtextra.AbsoluteConstraints(120, 60, 60, 25));

        titletext.setBackground(new java.awt.Color(218, 233, 236));
        titletext.setFont(new java.awt.Font("Century Gothic", 0, 11)); // NOI18N
        titletext.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                titletextActionPerformed(evt);
            }
        });
        jPanel8.add(titletext, new org.netbeans.lib.awtextra.AbsoluteConstraints(120, 100, 240, 25));

        genretext.setBackground(new java.awt.Color(218, 233, 236));
        genretext.setFont(new java.awt.Font("Century Gothic", 0, 11)); // NOI18N
        genretext.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                genretextActionPerformed(evt);
            }
        });
        jPanel8.add(genretext, new org.netbeans.lib.awtextra.AbsoluteConstraints(120, 140, 240, 25));

        jPanel9.setBackground(new java.awt.Color(210, 232, 237));
        jPanel9.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));

        jLabel14.setBackground(new java.awt.Color(0, 204, 204));
        jLabel14.setFont(new java.awt.Font("Century Gothic", 0, 12)); // NOI18N
        jLabel14.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel14.setText("BOOK ID:");

        javax.swing.GroupLayout jPanel9Layout = new javax.swing.GroupLayout(jPanel9);
        jPanel9.setLayout(jPanel9Layout);
        jPanel9Layout.setHorizontalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel14, javax.swing.GroupLayout.DEFAULT_SIZE, 88, Short.MAX_VALUE)
        );
        jPanel9Layout.setVerticalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel14, javax.swing.GroupLayout.DEFAULT_SIZE, 23, Short.MAX_VALUE)
        );

        jPanel8.add(jPanel9, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 60, 90, 25));

        jLabel12.setFont(new java.awt.Font("Century Gothic", 0, 12)); // NOI18N
        jLabel12.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel12.setText("TITLE:");
        jPanel8.add(jLabel12, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 100, 90, 25));

        jPanel10.setBackground(new java.awt.Color(210, 232, 237));

        javax.swing.GroupLayout jPanel10Layout = new javax.swing.GroupLayout(jPanel10);
        jPanel10.setLayout(jPanel10Layout);
        jPanel10Layout.setHorizontalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 90, Short.MAX_VALUE)
        );
        jPanel10Layout.setVerticalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 25, Short.MAX_VALUE)
        );

        jPanel8.add(jPanel10, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 100, 90, 25));

        jLabel15.setFont(new java.awt.Font("Century Gothic", 0, 12)); // NOI18N
        jLabel15.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel15.setText("GENRE:");
        jPanel8.add(jLabel15, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 140, 90, 25));

        jLabel16.setFont(new java.awt.Font("Century Gothic", 0, 12)); // NOI18N
        jLabel16.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel16.setText("AUTHOR:");
        jPanel8.add(jLabel16, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 180, 90, 25));

        jPanel11.setBackground(new java.awt.Color(210, 232, 237));

        javax.swing.GroupLayout jPanel11Layout = new javax.swing.GroupLayout(jPanel11);
        jPanel11.setLayout(jPanel11Layout);
        jPanel11Layout.setHorizontalGroup(
            jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 90, Short.MAX_VALUE)
        );
        jPanel11Layout.setVerticalGroup(
            jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 25, Short.MAX_VALUE)
        );

        jPanel8.add(jPanel11, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 140, 90, 25));

        jPanel13.setBackground(new java.awt.Color(210, 232, 237));

        jLabel13.setFont(new java.awt.Font("Century Gothic", 0, 12)); // NOI18N
        jLabel13.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel13.setText("DUE DATE:");

        javax.swing.GroupLayout jPanel13Layout = new javax.swing.GroupLayout(jPanel13);
        jPanel13.setLayout(jPanel13Layout);
        jPanel13Layout.setHorizontalGroup(
            jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel13Layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(jLabel13, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        jPanel13Layout.setVerticalGroup(
            jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel13Layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(jLabel13, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        jPanel8.add(jPanel13, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 220, 90, 25));

        jPanel14.setBackground(new java.awt.Color(210, 232, 237));

        javax.swing.GroupLayout jPanel14Layout = new javax.swing.GroupLayout(jPanel14);
        jPanel14.setLayout(jPanel14Layout);
        jPanel14Layout.setHorizontalGroup(
            jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 90, Short.MAX_VALUE)
        );
        jPanel14Layout.setVerticalGroup(
            jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 25, Short.MAX_VALUE)
        );

        jPanel8.add(jPanel14, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 180, 90, 25));

        borrowtbtn.setBackground(new java.awt.Color(210, 232, 237));
        borrowtbtn.setFont(new java.awt.Font("Century Gothic", 1, 14)); // NOI18N
        borrowtbtn.setText("Borrow");
        borrowtbtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                borrowtbtnActionPerformed(evt);
            }
        });
        jPanel8.add(borrowtbtn, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 470, 120, 25));

        addbookbtn.setBackground(new java.awt.Color(210, 232, 237));
        addbookbtn.setFont(new java.awt.Font("Century Gothic", 1, 14)); // NOI18N
        addbookbtn.setText("Add");
        addbookbtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addbookbtnActionPerformed(evt);
            }
        });
        jPanel8.add(addbookbtn, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 250, 100, 25));

        allselectbtn.setBackground(new java.awt.Color(204, 204, 255));
        allselectbtn.setToolTipText("");
        allselectbtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                allselectbtnActionPerformed(evt);
            }
        });
        jPanel8.add(allselectbtn, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 320, -1, 10));

        jScrollPane1.setBackground(new java.awt.Color(203, 211, 224));

        selecttable.setBackground(new java.awt.Color(204, 204, 255));
        selecttable.setFont(new java.awt.Font("Century Gothic", 0, 12)); // NOI18N
        selecttable.setForeground(new java.awt.Color(51, 51, 51));
        selecttable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "", "ID", "TITLE", "DUE DATE"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Boolean.class, java.lang.Integer.class, java.lang.Object.class, java.lang.Object.class
            };
            boolean[] canEdit = new boolean [] {
                true, false, true, true
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        selecttable.setColumnSelectionAllowed(true);
        selecttable.setGridColor(new java.awt.Color(204, 204, 204));
        selecttable.setName("Selected Books"); // NOI18N
        selecttable.setRowHeight(25);
        selecttable.setRowMargin(1);
        selecttable.setSelectionBackground(new java.awt.Color(102, 102, 102));
        selecttable.setShowGrid(true);
        selecttable.getTableHeader().setReorderingAllowed(false);
        jScrollPane1.setViewportView(selecttable);
        selecttable.getColumnModel().getSelectionModel().setSelectionMode(javax.swing.ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        if (selecttable.getColumnModel().getColumnCount() > 0) {
            selecttable.getColumnModel().getColumn(0).setMinWidth(40);
            selecttable.getColumnModel().getColumn(0).setPreferredWidth(40);
            selecttable.getColumnModel().getColumn(0).setMaxWidth(40);
            selecttable.getColumnModel().getColumn(1).setMinWidth(40);
            selecttable.getColumnModel().getColumn(1).setPreferredWidth(40);
            selecttable.getColumnModel().getColumn(1).setMaxWidth(40);
            selecttable.getColumnModel().getColumn(2).setMinWidth(180);
            selecttable.getColumnModel().getColumn(2).setPreferredWidth(180);
            selecttable.getColumnModel().getColumn(2).setMaxWidth(180);
        }
        selecttable.getAccessibleContext().setAccessibleName("Borrow Books");

        jPanel8.add(jScrollPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 310, 360, 160));

        authortext.setBackground(new java.awt.Color(218, 233, 236));
        authortext.setFont(new java.awt.Font("Century Gothic", 0, 11)); // NOI18N
        authortext.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                authortextActionPerformed(evt);
            }
        });
        jPanel8.add(authortext, new org.netbeans.lib.awtextra.AbsoluteConstraints(120, 180, 240, 25));

        clearbookbtn.setBackground(new java.awt.Color(204, 0, 0));
        clearbookbtn.setFont(new java.awt.Font("Century Gothic", 1, 14)); // NOI18N
        clearbookbtn.setForeground(new java.awt.Color(255, 255, 255));
        clearbookbtn.setText("Clear");
        clearbookbtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                clearbookbtnActionPerformed(evt);
            }
        });
        jPanel8.add(clearbookbtn, new org.netbeans.lib.awtextra.AbsoluteConstraints(230, 250, 110, 25));

        deletbttn.setBackground(new java.awt.Color(204, 0, 0));
        deletbttn.setFont(new java.awt.Font("Century Gothic", 1, 14)); // NOI18N
        deletbttn.setForeground(new java.awt.Color(255, 255, 255));
        deletbttn.setText("Delete");
        deletbttn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                deletbttnActionPerformed(evt);
            }
        });
        jPanel8.add(deletbttn, new org.netbeans.lib.awtextra.AbsoluteConstraints(210, 470, 120, 25));

        borrowedtable.setBackground(new java.awt.Color(218, 233, 236));
        borrowedtable.setFont(new java.awt.Font("Century Gothic", 0, 11)); // NOI18N
        borrowedtable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "TR#", "ID", "Title", "Checkout", "Due Date"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Integer.class, java.lang.Integer.class, java.lang.String.class, java.lang.String.class, java.lang.String.class
            };
            boolean[] canEdit = new boolean [] {
                false, true, true, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        borrowedtable.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        borrowedtable.setGridColor(new java.awt.Color(102, 102, 102));
        borrowedtable.setName("Borrowed Books"); // NOI18N
        borrowedtable.setRowHeight(25);
        borrowedtable.setSelectionBackground(new java.awt.Color(102, 102, 102));
        borrowedtable.setShowGrid(true);
        borrowedtable.setShowVerticalLines(false);
        borrowedtable.getTableHeader().setReorderingAllowed(false);
        borrowedtable.addAncestorListener(new javax.swing.event.AncestorListener() {
            public void ancestorAdded(javax.swing.event.AncestorEvent evt) {
            }
            public void ancestorMoved(javax.swing.event.AncestorEvent evt) {
                borrowedtableAncestorMoved(evt);
            }
            public void ancestorRemoved(javax.swing.event.AncestorEvent evt) {
            }
        });
        jScrollPane2.setViewportView(borrowedtable);
        if (borrowedtable.getColumnModel().getColumnCount() > 0) {
            borrowedtable.getColumnModel().getColumn(0).setMinWidth(50);
            borrowedtable.getColumnModel().getColumn(0).setPreferredWidth(50);
            borrowedtable.getColumnModel().getColumn(0).setMaxWidth(50);
            borrowedtable.getColumnModel().getColumn(1).setMinWidth(40);
            borrowedtable.getColumnModel().getColumn(1).setPreferredWidth(40);
            borrowedtable.getColumnModel().getColumn(1).setMaxWidth(40);
            borrowedtable.getColumnModel().getColumn(3).setMinWidth(75);
            borrowedtable.getColumnModel().getColumn(3).setPreferredWidth(75);
            borrowedtable.getColumnModel().getColumn(3).setMaxWidth(75);
            borrowedtable.getColumnModel().getColumn(4).setMinWidth(75);
            borrowedtable.getColumnModel().getColumn(4).setPreferredWidth(75);
            borrowedtable.getColumnModel().getColumn(4).setMaxWidth(75);
        }

        jPanel8.add(jScrollPane2, new org.netbeans.lib.awtextra.AbsoluteConstraints(380, 100, 380, 280));

        jPanel12.setBackground(new java.awt.Color(204, 204, 255));

        jLabel17.setBackground(new java.awt.Color(102, 102, 255));
        jLabel17.setFont(new java.awt.Font("Century Gothic", 1, 11)); // NOI18N
        jLabel17.setForeground(new java.awt.Color(51, 51, 51));
        jLabel17.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel17.setText("BORROWED BOOKS");

        javax.swing.GroupLayout jPanel12Layout = new javax.swing.GroupLayout(jPanel12);
        jPanel12.setLayout(jPanel12Layout);
        jPanel12Layout.setHorizontalGroup(
            jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel12Layout.createSequentialGroup()
                .addComponent(jLabel17, javax.swing.GroupLayout.PREFERRED_SIZE, 359, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 1, Short.MAX_VALUE))
        );
        jPanel12Layout.setVerticalGroup(
            jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel12Layout.createSequentialGroup()
                .addComponent(jLabel17, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 6, Short.MAX_VALUE))
        );

        jPanel8.add(jPanel12, new org.netbeans.lib.awtextra.AbsoluteConstraints(390, 80, 360, 30));

        jLabel8.setBackground(new java.awt.Color(102, 102, 255));
        jLabel8.setFont(new java.awt.Font("Century Gothic", 1, 11)); // NOI18N
        jLabel8.setForeground(new java.awt.Color(51, 51, 51));
        jLabel8.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel8.setText("SELECTED BOOKS");
        jPanel8.add(jLabel8, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 290, 320, 20));

        jPanel5.setBackground(new java.awt.Color(183, 204, 224));

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 320, Short.MAX_VALUE)
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 30, Short.MAX_VALUE)
        );

        jPanel8.add(jPanel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 290, 320, 30));

        borrowhistory.setBackground(new java.awt.Color(0, 0, 0));
        borrowhistory.setFont(new java.awt.Font("Century Gothic", 1, 14)); // NOI18N
        borrowhistory.setForeground(new java.awt.Color(255, 255, 255));
        borrowhistory.setText("History");
        borrowhistory.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                borrowhistoryActionPerformed(evt);
            }
        });
        jPanel8.add(borrowhistory, new org.netbeans.lib.awtextra.AbsoluteConstraints(450, 420, 120, 25));

        undoborrowbtn.setBackground(new java.awt.Color(204, 0, 0));
        undoborrowbtn.setFont(new java.awt.Font("Century Gothic", 1, 14)); // NOI18N
        undoborrowbtn.setForeground(new java.awt.Color(255, 255, 255));
        undoborrowbtn.setText("Undo");
        undoborrowbtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                undoborrowbtnActionPerformed(evt);
            }
        });
        jPanel8.add(undoborrowbtn, new org.netbeans.lib.awtextra.AbsoluteConstraints(450, 390, 120, 25));

        printbtn.setFont(new java.awt.Font("Century Gothic", 1, 14)); // NOI18N
        printbtn.setForeground(new java.awt.Color(51, 51, 51));
        printbtn.setText("Print");
        printbtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                printbtnActionPerformed(evt);
            }
        });
        jPanel8.add(printbtn, new org.netbeans.lib.awtextra.AbsoluteConstraints(570, 420, 120, 25));

        returnbtn.setBackground(new java.awt.Color(210, 232, 237));
        returnbtn.setFont(new java.awt.Font("Century Gothic", 1, 14)); // NOI18N
        returnbtn.setText("Return Book ");
        returnbtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                returnbtnActionPerformed(evt);
            }
        });
        jPanel8.add(returnbtn, new org.netbeans.lib.awtextra.AbsoluteConstraints(570, 390, 120, 25));

        duedatechooser.setBackground(new java.awt.Color(218, 233, 236));
        duedatechooser.setDateFormatString("MMMM dd, yyyy"); // NOI18N
        duedatechooser.setFont(new java.awt.Font("Century Gothic", 0, 12)); // NOI18N
        duedatechooser.setMaxSelectableDate(new java.util.Date(253370739689000L));
        duedatechooser.setMinSelectableDate(new java.util.Date(-62135794711000L));
        jPanel8.add(duedatechooser, new org.netbeans.lib.awtextra.AbsoluteConstraints(120, 220, 240, -1));

        qtytext.setBackground(new java.awt.Color(218, 233, 236));
        qtytext.setFont(new java.awt.Font("Century Gothic", 0, 11)); // NOI18N
        qtytext.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        qtytext.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                qtytextActionPerformed(evt);
            }
        });
        jPanel8.add(qtytext, new org.netbeans.lib.awtextra.AbsoluteConstraints(280, 60, 80, 25));

        jPanel16.setBackground(new java.awt.Color(210, 232, 237));

        jLabel1.setFont(new java.awt.Font("Century Gothic", 0, 12)); // NOI18N
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText("QUANTITY:");
        jLabel1.setToolTipText("");

        javax.swing.GroupLayout jPanel16Layout = new javax.swing.GroupLayout(jPanel16);
        jPanel16.setLayout(jPanel16Layout);
        jPanel16Layout.setHorizontalGroup(
            jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, 80, Short.MAX_VALUE)
        );
        jPanel16Layout.setVerticalGroup(
            jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel16Layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        jPanel8.add(jPanel16, new org.netbeans.lib.awtextra.AbsoluteConstraints(190, 60, 80, 25));

        bg.setBackground(new java.awt.Color(51, 51, 51));
        bg.setIcon(new javax.swing.ImageIcon("C:\\Users\\ADMIN\\Documents\\NetBeansProjects\\MyLibrary\\src\\1.png")); // NOI18N
        jPanel8.add(bg, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, -20, 800, -1));

        getContentPane().add(jPanel8);
        jPanel8.setBounds(120, 0, 790, 510);

        getAccessibleContext().setAccessibleName("BORROW");

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void idtextActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_idtextActionPerformed
        // TODO add your handling code here:
        try {
            // Directly parse the input as an int
            int id = Integer.parseInt(idtext.getText().trim());

            // Check if the id is valid (greater than 0)
            if (id > 0) {
                loadBookInfoByID(id);  // Pass the int directly
            } else {
                JOptionPane.showMessageDialog(null, "Please enter a valid numeric book ID greater than 0.");
                clearFields(); 
            }
        } catch (NumberFormatException e) {
            // Handle the case where the input is not a valid integer
            JOptionPane.showMessageDialog(null, "Invalid ID: Please enter a numeric book ID.");
            clearFields(); 
        } catch (Exception e) {
            // Catch any other exceptions that may arise
            JOptionPane.showMessageDialog(null, "An error occurred: " + e.getMessage());
            clearFields(); 
        }
    }//GEN-LAST:event_idtextActionPerformed

    private void titletextActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_titletextActionPerformed
        // TODO add your handling code here:
        
             
        
    }//GEN-LAST:event_titletextActionPerformed

    private void borrowtbtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_borrowtbtnActionPerformed
        // TODO add your handling code here:
         addBorrowedBooksToTransactions();
        DefaultTableModel selectTableModel = (DefaultTableModel) selecttable.getModel();
        DefaultTableModel borrowedTableModel = (DefaultTableModel) borrowedtable.getModel();

        int transactionId = 1; // Default value in case there are no previous transactions

        try {
            // Fetch the connection using the DbConnection class
            Connection conn = DbConnection.getConnection();

            // Prepare the SQL query to get the highest transaction_id
            String query = "SELECT MAX(transaction_id) AS max_id FROM transactions";
             java.sql.Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(query);

          
            if (rs.next()) {
                int maxTransactionId = rs.getInt("max_id");
                if (maxTransactionId > 0) {
                    transactionId = maxTransactionId; // Use the max transaction ID without incrementing
                }
            }

            
            rs.close();
            stmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
// Get the current date as the checkout date
        Date checkoutDate = new Date();  // Current date
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String formattedCheckoutDate = sdf.format(checkoutDate);

  

// List to store selected row indices
        List<Integer> selectedRows = new ArrayList<>();

// Iterate over each row in the selecttable to transfer the data
        for (int i = 0; i < selectTableModel.getRowCount(); i++) {
            Boolean isSelected = (Boolean) selectTableModel.getValueAt(i, 0); // Check if the row is selected

            if (isSelected != null && isSelected) {
                // Get the data from the selecttable (assuming columns for ID, Title, Due Date exist)
                int bookId = Integer.parseInt(selectTableModel.getValueAt(i, 1).toString());  // Assuming 1 is the ID column index
                String title = selectTableModel.getValueAt(i, 2).toString();   // Assuming 2 is the Title column index
                String dueDate = selectTableModel.getValueAt(i, 3).toString(); // Assuming 3 is the Due Date column index

                // Add a new row to the borrowedtable with the new column order
                // Column 0: Transaction ID, Column 1: Book ID, Column 2: Title, Column 3: Checkout Date, Column 4: Due Date
                borrowedTableModel.addRow(new Object[]{transactionId, bookId, title, formattedCheckoutDate, dueDate});

             

                // Add this row index to the list of rows to be removed later
                selectedRows.add(i);
            }
        }

// Optionally clear the selected rows in selecttable after checkout
// Remove the rows from bottom to top to avoid index shifting issues
        for (int i = selectedRows.size() - 1; i >= 0; i--) {
            int row = selectedRows.get(i);
            selectTableModel.removeRow(row);
        }
    }
        private void addBorrowedBooksToTransactions() {
        try {
            // Establish connection
            Connection conn = DriverManager.getConnection("jdbc:mysql://localhost/library_db", "root", "");

            // Get current user ID
            int userId = getCurrentUserId();

            String insertTransactionSQL = "INSERT INTO transactions (user_id, book_id, title, checkout_date, due_date) VALUES (?, ?, ?, CURDATE(), ?)";
            String updateBookQuantitySQL = "UPDATE books SET quantity = quantity - 1 WHERE book_id = ?";

            // Prepare a date formatter for SQL date format (yyyy-MM-dd)
            SimpleDateFormat sqlDateFormat = new SimpleDateFormat("yyyy-MM-dd");

            for (int i = 0; i < selecttable.getRowCount(); i++) {
                Boolean isSelected = (Boolean) selecttable.getValueAt(i, 0); // Check if the book is selected
                if (isSelected != null && isSelected) {
                    // Retrieve book ID as String and convert to int
                    String bookIdStr = selecttable.getValueAt(i, 1).toString();
                    int bookId = Integer.parseInt(bookIdStr);

                    // Retrieve the title (assuming it's in column index 2)
                    String title = selecttable.getValueAt(i, 2).toString();

                    String dueDateStr = selecttable.getValueAt(i, 3).toString(); // Get Due Date

                    // Parse the due date to ensure correct format for SQL
                    Date dueDate = new SimpleDateFormat("MM/dd/yyyy").parse(dueDateStr);
                    String formattedDueDate = sqlDateFormat.format(dueDate); // Convert to yyyy-MM-dd

                    // Insert transaction into the database
                    PreparedStatement insertStmt = conn.prepareStatement(insertTransactionSQL, new String[]{"transaction_id"});
                    insertStmt.setInt(1, userId);
                    insertStmt.setInt(2, bookId);
                    insertStmt.setString(3, title);
                    insertStmt.setString(4, formattedDueDate);
                    insertStmt.executeUpdate();

                    // Get the generated transaction_id (optional)
                    ResultSet generatedKeys = insertStmt.getGeneratedKeys();
                    if (generatedKeys.next()) {
                        int transactionId = generatedKeys.getInt(1);
                        System.out.println("Transaction ID: " + transactionId);
                    }

                    // Update book quantity
                    PreparedStatement updateStmt = conn.prepareStatement(updateBookQuantitySQL);
                    updateStmt.setInt(1, bookId);
                    updateStmt.executeUpdate();
                }
            }
            conn.close();

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "SQL Error: " + e.getMessage());
            e.printStackTrace();
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "Invalid book ID format: " + e.getMessage());
            e.printStackTrace();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "An error occurred: " + e.getMessage());
            e.printStackTrace();
        }
   
    }//GEN-LAST:event_borrowtbtnActionPerformed

        
     
        
    private void addbookbtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addbookbtnActionPerformed
        // TODO add your handling code here:
        
        int id;
        try {
            String idInput = idtext.getText().trim(); // Trim any accidental whitespace
            if (idInput.isEmpty()) {  // Check if input is empty
                JOptionPane.showMessageDialog(null, "Book ID cannot be empty.", "Invalid ID", JOptionPane.WARNING_MESSAGE);
                clearFields();
                return;
            }

            id = Integer.parseInt(idInput); // Try to parse the integer from the input
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "Please input valid book ID.", "Invalid ID", JOptionPane.WARNING_MESSAGE);
            clearFields();
            return;
        }

// Retrieve the quantity from the qtytext JTextField (Assuming you set it earlier)
        int quantity = Integer.parseInt(qtytext.getText().trim());

        if (quantity == 0) {
            // If the book quantity is 0, show a warning and return without proceeding
            JOptionPane.showMessageDialog(null, "This book is not available for borrowing. No copies left.",
                    "Book Not Available", JOptionPane.WARNING_MESSAGE);
            clearFields();
            return; // Exit the method if no books are available
        }

        String title = titletext.getText().trim();
        String genre = genretext.getText().trim();
        String author = authortext.getText().trim();
        java.util.Date dueDate = duedatechooser.getDate();

        System.out.println("Selected Due Date: " + dueDate);

// Check if the selected due date is valid (not today or in the past)
        if (dueDate == null || dueDate.before(new java.util.Date())) {
            JOptionPane.showMessageDialog(null, "Please select a valid due date (not today or in the past).",
                    "Invalid Due Date", JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (id == 0 || title.isEmpty() || genre.isEmpty() || author.isEmpty() || dueDate == null) {
            JOptionPane.showMessageDialog(null, "Please fill in all fields and select a due date.",
                    "Missing Information", JOptionPane.WARNING_MESSAGE);
            return;
        }

        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
        String formattedDueDate = sdf.format(dueDate);
        System.out.println("Formatted Due Date: " + formattedDueDate);

// Add a new row to the selecttable
        DefaultTableModel model = (DefaultTableModel) selecttable.getModel();
        model.addRow(new Object[]{false, id, title, formattedDueDate}); // Add the formatted due date to the table

        clearFields(); // Clear the text fields after adding
    }//GEN-LAST:event_addbookbtnActionPerformed

    
    private void genretextActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_genretextActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_genretextActionPerformed

    private void authortextActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_authortextActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_authortextActionPerformed

    private void clearbookbtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_clearbookbtnActionPerformed
        // TODO add your handling code here:
        
        int response = JOptionPane.showConfirmDialog(null, "Are you sure you want to clear all fields?", "Confirm Clear", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
        idtext.setText("");
        titletext.setText("");
        genretext.setText("");
        authortext.setText("");
         duedatechooser.setDate(null);
    }//GEN-LAST:event_clearbookbtnActionPerformed

    private void deletbttnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_deletbttnActionPerformed
        // TODO add your handling code here:
        
         int selectedRow = selecttable.getSelectedRow();
        if (selectedRow != -1) { // Check if a row is selected
            int response = JOptionPane.showConfirmDialog(null, "Are you sure you want to delete this book?", "Confirm Delete", JOptionPane.YES_NO_OPTION);
            if (response == JOptionPane.YES_OPTION) {
                DefaultTableModel model = (DefaultTableModel) selecttable.getModel();
                model.removeRow(selectedRow);
                // Optionally, remove from the database as well if necessary
            }
        } else {
            JOptionPane.showMessageDialog(null, "Please select a book to delete.");
        
    }               
        
    }//GEN-LAST:event_deletbttnActionPerformed

    private void borrowedtableAncestorMoved(javax.swing.event.AncestorEvent evt) {//GEN-FIRST:event_borrowedtableAncestorMoved
        // TODO add your handling code here:
    }//GEN-LAST:event_borrowedtableAncestorMoved

    private void loadTransactionData() {
        String sql = "SELECT t.transaction_id, t.user_id, b.title, t.checkout_date, t.due_date, t.return_date "
                + "FROM transactions t "
                + "INNER JOIN books b ON t.book_id = b.book_id "
                + "WHERE t.user_id = ? AND t.status = 'Borrowed'";

        try (Connection conn = DbConnection.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, userId);  // Set the user_id to filter by the current logged-in user

            ResultSet rs = pstmt.executeQuery();

            DefaultTableModel model = (DefaultTableModel) borrowedtable.getModel();
            model.setRowCount(0);  // Clear existing rows before loading new data

            while (rs.next()) {
                // Get the data from the ResultSet
                int transactionId = rs.getInt("transaction_id");
                String title = rs.getString("title");
                Date checkoutDate = rs.getDate("checkout_date");
                Date dueDate = rs.getDate("due_date");

                // Add the data to the JTable; omit return_date as it's NULL for still borrowed books
                model.addRow(new Object[]{transactionId, title, checkoutDate, dueDate});
            }

        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error fetching transaction data: " + e.getMessage());
        }
    }
    
    private void undoborrowbtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_undoborrowbtnActionPerformed
        // TODO add your handling code here:
        int selectedRow = borrowedtable.getSelectedRow();

        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(null, "Please select a book to undo borrowing.");
            return;
        }

        // Get the transaction ID from the selected row (assuming the first column is transaction_id)
        int transactionId = (int) borrowedtable.getValueAt(selectedRow, 0);

        // Database connection
        Connection conn = null;
        PreparedStatement stmt = null;

        try {
            // Assume conn is your active DB connection
            conn = DriverManager.getConnection("jdbc:mysql://localhost/library_db", "root", "");

            // Delete or mark the transaction as returned
            String deleteSQL = "DELETE FROM transactions WHERE transaction_id = ?";
            stmt = conn.prepareStatement(deleteSQL);
            stmt.setInt(1, transactionId);
            stmt.executeUpdate();

            // Remove the row from the JTable
            DefaultTableModel model = (DefaultTableModel) borrowedtable.getModel();
            model.removeRow(selectedRow);

            JOptionPane.showMessageDialog(null, "Borrowing successfully undone.");

        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error while undoing borrowing.");
        } finally {
            try {
                if (stmt != null) {
                    stmt.close();
                }
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
        
    }//GEN-LAST:event_undoborrowbtnActionPerformed

    private JTextArea txtReceipt;

    private void generateReceipt(String fullName) {
        SimpleDateFormat sdf = new SimpleDateFormat("MMMM dd, yyyy HH:mm:ss");
        StringBuilder receiptText = new StringBuilder();
        receiptText.append("====== BOOKWORM CENTRAL ======\n")
                .append("Date: ").append(sdf.format(new Date())).append("\n")
                .append("Borrower: ").append(fullName).append("\n")
                .append("User ID: ").append(userId).append("\n")
                .append("Username: ").append(username).append("\n\n")
                .append("Borrowed Books:\n");

        DefaultTableModel model = (DefaultTableModel) borrowedtable.getModel();
        int[] selectedRows = borrowedtable.getSelectedRows(); // Get selected rows

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
            String dueDate = model.getValueAt(selectedIndex, 4).toString();

            receiptText.append("Transaction ID: ").append(transactionId).append("\n")
                    .append("Book ID: ").append(id).append("\n")
                    .append("Title: ").append(title).append("\n")
                    .append("Checkout Date: ").append(checkout).append("\n")
                    .append("Due Date: ").append(dueDate).append("\n\n");
        }

        receiptText.append("Happy Reading, ").append(username).append("!\n")
                .append("=====================================");

        ImageIcon icon = new ImageIcon("C:\\Users\\ADMIN\\Documents\\NetBeansProjects\\MyLibrary\\src\\iconop.png");

        txtReceipt = new JTextArea(receiptText.toString());
        txtReceipt.setEditable(false);
        txtReceipt.setFont(new Font("Monospaced", Font.PLAIN, 12)); // Monospaced font for better alignment

        JScrollPane scrollPane = new JScrollPane(txtReceipt);

        // Show the receipt with the new custom icon
        JOptionPane.showMessageDialog(this, scrollPane, "Borrowed Books Receipt", JOptionPane.INFORMATION_MESSAGE, icon);
    }
    private void printbtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_printbtnActionPerformed
        // TODO add your handling code here:
        String fullName = getFullNameFromDatabase(username);
        generateReceipt(fullName);
        int userId = getCurrentUserId();
        

        PrinterJob job = PrinterJob.getPrinterJob();
        job.setJobName("Print Borrowed Books Receipt");

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
    }

    private String getFullNameFromDatabase(String username) {
        String fullName = "";
        try (Connection conn = connect()) {
            String query = "SELECT full_name FROM users WHERE username = ?";
            PreparedStatement pstmt = conn.prepareStatement(query);
            pstmt.setString(1, username);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                fullName = rs.getString("full_name");
            }
        } catch (SQLException e) {
        }
        return fullName;
    
    }//GEN-LAST:event_printbtnActionPerformed

    private void returnbtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_returnbtnActionPerformed
        // TODO add your handling code here:
        LibReturn libReturnFrame = new LibReturn(userId,username);
        libReturnFrame.setVisible(true);
        libReturnFrame.setLocationRelativeTo(null);
    }//GEN-LAST:event_returnbtnActionPerformed

    private boolean allSelected = false;
  
    private void allselectbtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_allselectbtnActionPerformed
        // TODO add your handling code here:
        DefaultTableModel selectTableModel = (DefaultTableModel) selecttable.getModel();

        // Check if we are selecting or deselecting
        if (!allSelected) {
            // Select all rows
            for (int i = 0; i < selectTableModel.getRowCount(); i++) {
                selectTableModel.setValueAt(true, i, 0);  // Assuming column 0 is the checkbox column
            }
            allSelected = true;  // Set the flag to indicate all rows are selected
        } else {
            // Deselect all rows
            for (int i = 0; i < selectTableModel.getRowCount(); i++) {
                selectTableModel.setValueAt(false, i, 0);  // Deselect the rows
            }
            allSelected = false;  // Set the flag to indicate no rows are selected
        }
    
    }//GEN-LAST:event_allselectbtnActionPerformed

    private void borrowhistoryActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_borrowhistoryActionPerformed
        // TODO add your handling code here:
        LibHistory historyFrame = new LibHistory(userId, username);
        historyFrame.setVisible(true);  // Show the history frame
        this.dispose();
        
    }//GEN-LAST:event_borrowhistoryActionPerformed

    private void qtytextActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_qtytextActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_qtytextActionPerformed

    private boolean showConfirmationDialog(String message) {
        int response = JOptionPane.showConfirmDialog(null, message, "Confirm Clear", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
        return response == JOptionPane.YES_OPTION; // Return true if user clicked "Yes"
    }

    
    
// Method to show a message dialog with a specific background color
    private void showMessage(String message) {
        // Create a custom JDialog
        JDialog dialog = new JDialog();
        dialog.setTitle("Information");
        dialog.setModal(true); // Modal dialog

        // Create a JPanel for content
        JPanel panel = new JPanel();
        panel.setBackground(new Color(0, 204, 204)); // Set background color

        // Add message label
        JLabel messageLabel = new JLabel(message);
        panel.add(messageLabel);

        // Create an OK button
        JButton okButton = new JButton("OK");
        okButton.setBackground(Color.BLUE);
        okButton.setForeground(Color.WHITE);
         okButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dialog.dispose(); // Close the dialog
            }
        });

        // Add buttons to the panel
        panel.add(okButton);

        // Set the dialog's content pane
        dialog.setContentPane(panel);
        dialog.pack(); // Adjust size to fit contents
        dialog.setLocationRelativeTo(null); // Center on screen
        dialog.setVisible(true); // Show dialog
    }
  
   
    private void returnBook(int transactionId) {
        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost/library_db", "root", "")) {
            String updateSQL = "UPDATE transactions SET return_date = CURDATE() WHERE transaction_id = ?";
            PreparedStatement stmt = conn.prepareStatement(updateSQL);
            stmt.setInt(1, transactionId);
            stmt.executeUpdate();

            JOptionPane.showMessageDialog(null, "Book returned successfully.");

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error returning book.");
        }
    }
    private void borrowBook(int bookId, int userId, Date dueDate) {
        Connection conn = connect();
        if (conn != null) {
            try {
                String query = "INSERT INTO transactions (user_id, book_id, title, checkout_date, due_date) VALUES (?, ?, ?, ?, ?)";
                PreparedStatement ps = conn.prepareStatement(query);
                ps.setInt(1, userId);
                ps.setInt(2, bookId);
                ps.setDate(3, new java.sql.Date(System.currentTimeMillis())); // Checkout date
                ps.setDate(4, new java.sql.Date(dueDate.getTime())); // Due date

                ps.executeUpdate(); // Execute the insert

                // Optionally, update the borrowedtable to show the newly borrowed book
                loadBorrowedBooks(userId); // Refresh the table view

            } catch (SQLException e) {
                JOptionPane.showMessageDialog(null, "Error borrowing the book: " + e.getMessage());
            } finally {
                try {
                    conn.close();
                } catch (SQLException ex) {
                }
            }
        }
    }
    
    
    private void loadBorrowedBooks(int userId) {
        Connection conn = connect();
        if (conn != null) {
            try {
                String query = "SELECT t.transaction_id, b.book_id, b.title, t.checkout_date, t.due_date "
                        + "FROM transactions t "
                        + "JOIN books b ON t.book_id = b.book_id "
                        + "WHERE t.user_id = ? AND t.status = 'borrowed'";
                PreparedStatement ps = conn.prepareStatement(query);
                ps.setInt(1, userId);

                ResultSet rs = ps.executeQuery();

                // Clear existing rows from the borrowedtable before adding new data
                DefaultTableModel model = (DefaultTableModel) borrowedtable.getModel();
                model.setRowCount(0);

                while (rs.next()) {
            Object[] row = new Object[]{
                rs.getInt("transaction_id"), // TR#
                rs.getInt("book_id"),       // Book ID
                rs.getString("title"),       // Book Title
                rs.getDate("checkout_date"), // Checkout Date
                rs.getDate("due_date")      // Due Date
            };

                    // Add the borrowed book to the table
                    model.addRow(row);
                }

            } catch (SQLException e) {
                JOptionPane.showMessageDialog(null, "Error loading borrowed books: " + e.getMessage());
            } finally {
                try {
                    conn.close();
                } catch (SQLException ex) {
                    
                }
                
                    
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
            java.util.logging.Logger.getLogger(LibBorrow.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                int userId = 0;
                String username1 = "Default User";
                new LibBorrow(userId, username1).setVisible(true);
            }
        });
        
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton addbookbtn;
    private javax.swing.JCheckBox allselectbtn;
    private javax.swing.JTextField authortext;
    private javax.swing.JLabel bg;
    private javax.swing.JLabel bordash;
    private javax.swing.JLabel borhistory;
    private javax.swing.JLabel boricon;
    private javax.swing.JLabel borlogout;
    private javax.swing.JLabel borreturn;
    private javax.swing.JTable borrowedtable;
    private javax.swing.JButton borrowhistory;
    private javax.swing.JButton borrowtbtn;
    private javax.swing.JLabel boruser;
    private javax.swing.JLabel borview;
    private javax.swing.JButton clearbookbtn;
    private javax.swing.JButton deletbttn;
    private com.toedter.calendar.JDateChooser duedatechooser;
    private javax.swing.JTextField genretext;
    private javax.swing.JTextField idtext;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel10;
    private javax.swing.JPanel jPanel11;
    private javax.swing.JPanel jPanel12;
    private javax.swing.JPanel jPanel13;
    private javax.swing.JPanel jPanel14;
    private javax.swing.JPanel jPanel15;
    private javax.swing.JPanel jPanel16;
    private javax.swing.JPanel jPanel17;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JButton printbtn;
    private javax.swing.JTextField qtytext;
    private javax.swing.JButton returnbtn;
    private javax.swing.JTable selecttable;
    private javax.swing.JTextField titletext;
    private javax.swing.JButton undoborrowbtn;
    // End of variables declaration//GEN-END:variables

    private int getCurrentUserId() {
        return userId;
    }
}
