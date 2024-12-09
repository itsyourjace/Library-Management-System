/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package mylibrary;

import javax.swing.*;
import java.awt.*;
import java.io.File;

public class ImagePreview extends JComponent implements java.beans.PropertyChangeListener {

    private ImageIcon thumbnail = null;
    private File file = null;

    public ImagePreview(JFileChooser fc) {
        setPreferredSize(new Dimension(200, 200)); // Set size of preview panel
        fc.addPropertyChangeListener(this); // Listen for changes in the file chooser
    }

    // Create a thumbnail when a file is selected
    public void loadImage() {
        if (file == null) {
            thumbnail = null;
            return;
        }

        // Create the thumbnail image
        ImageIcon tmpIcon = new ImageIcon(file.getAbsolutePath());
        if (tmpIcon != null) {
            Image img = tmpIcon.getImage(); // Get the image from the file
            Image scaledImg = img.getScaledInstance(140, 80, Image.SCALE_SMOOTH); // Resize for preview
            thumbnail = new ImageIcon(scaledImg); // Assign the scaled image to the thumbnail
        }
    }

    // Update when a new file is selected
    @Override
    public void propertyChange(java.beans.PropertyChangeEvent e) {
        String prop = e.getPropertyName();

        if (JFileChooser.SELECTED_FILE_CHANGED_PROPERTY.equals(prop)) {
            file = (File) e.getNewValue();
            if (isShowing()) {
                loadImage();
                repaint();
            }
        }
    }

    // Paint the thumbnail in the preview panel
    @Override
    protected void paintComponent(Graphics g) {
        if (thumbnail != null) {
            int x = getWidth() / 2 - thumbnail.getIconWidth() / 2;
            int y = getHeight() / 2 - thumbnail.getIconHeight() / 2;

            if (y < 0) {
                y = 0;
            }

            if (x < 5) {
                x = 5;
            }

            // Draw the image on the preview panel
            thumbnail.paintIcon(this, g, x, y);
        }
    }
}
