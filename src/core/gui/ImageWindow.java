package core.gui;

import javax.swing.*;
import java.awt.*;

/**
 * Cette classe gère la fenêtre s'ouvrant lorsque l'utilisateur veut voir une image
 *
 * @author Dylan Manseri
 * @version 1.0
 */
public class ImageWindow extends JFrame {

    /**
     * Instancie une ImageWindow et l'affiche à l'ecran avec l'image
     * @param image l'image à afficher
     */
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
