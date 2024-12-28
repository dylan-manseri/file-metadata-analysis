package core.gui;

import javax.swing.*;
import java.awt.*;

public class ImageWindow extends JFrame {

    public ImageWindow(ImageIcon image){
        super("Affichage");
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        this.setSize(900,600);
        this.setLocationRelativeTo(null);
        JPanel contentPane = (JPanel) this.getContentPane();
        contentPane.setLayout(new BorderLayout());
        JLabel imageLabel = new JLabel(image);
        JPanel imageContainer = new JPanel();
        imageContainer.add(imageLabel);
        JScrollPane window = new JScrollPane(imageContainer,JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
                JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        contentPane.add(window);

    }

}
