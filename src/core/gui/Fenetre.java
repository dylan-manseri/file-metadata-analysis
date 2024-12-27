package core.gui;

import javax.swing.*;
import java.awt.*;
import java.nio.file.Path;
import java.nio.file.Paths;

public class Fenetre extends JFrame {
    JButton statButton = new JButton("Statistiques");
    JButton metaButton = new JButton("Metadonnee");
    JButton searchButton = new JButton("Recherche");
    JButton saveButton = new JButton("Sauvegarde");
    JPanel contentPane = (JPanel) this.getContentPane();

    public Fenetre(){
        super("Fichier et metadonnee");
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        this.setSize(900,600);
        this.setLocationRelativeTo(null);
        JPanel arbo = new JPanel();
        arbo.setLayout(new BorderLayout());
        JTree dossier = new JTree();
        JScrollPane dossierScroll = new JScrollPane(dossier);
        JLabel nom = new JLabel(actualPath().toString());
        nom.setPreferredSize(new Dimension(100,30));
        arbo.add(nom, BorderLayout.NORTH);
        arbo.add(dossierScroll, BorderLayout.CENTER);
        contentPane.add(createToolBar(), BorderLayout.NORTH);
        contentPane.add(arbo,BorderLayout.CENTER);

        toolBarButton();
    }

    public JToolBar createToolBar(){
        JToolBar toolBar = new JToolBar();
        toolBar.setFloatable(false);
        toolBar.addSeparator();
        toolBar.add(statButton); toolBar.addSeparator();
        toolBar.add(metaButton); toolBar.addSeparator();
        toolBar.add(saveButton); toolBar.addSeparator();
        toolBar.add(searchButton); toolBar.addSeparator();
        return toolBar;
    }



    public Path actualPath(){
        return Paths.get(".").normalize().toAbsolutePath();
    }

    public void toolBarButton(){
        statButton.addActionListener( (event) -> new Analyse(contentPane, "stat"));
    }


}
