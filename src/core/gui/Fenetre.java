package core.gui;

import javax.swing.*;
import javax.swing.border.Border;
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

        actionButton();
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

    public void createStatPanel(){
        JPanel stat = new JPanel(new BorderLayout());

        JLabel chemin = new JLabel("Chemin :");
        JTextArea cheminTxt = new JTextArea();
        cheminTxt.setPreferredSize(new Dimension(5,25));
        JPanel ask = new JPanel(new FlowLayout());
        ask.add(chemin); ask.add(cheminTxt);

        JButton entree = new JButton("ENTREE");
        entree.setPreferredSize(new Dimension(800,50));
        JPanel boxX = new JPanel();
        boxX.setLayout(new BoxLayout(boxX,BoxLayout.X_AXIS));
        boxX.add(chemin); boxX.add(cheminTxt);
        boxX.setPreferredSize(new Dimension(100,100));
        JPanel boxY = new JPanel();
        boxY.setLayout(new BoxLayout(boxY,BoxLayout.Y_AXIS));
        boxY.add(boxX); boxY.add(entree);

        JLabel reponse = new JLabel("reponse ici");
        Border border = BorderFactory.createLineBorder(Color.BLACK, 2);
        reponse.setBorder(border);
        reponse.setPreferredSize(new Dimension(0,350));

        JLabel stati = new JLabel("Statistique -> indiquer un chemin");
        stati.setPreferredSize(new Dimension(0,130));

        stat.add(stati, BorderLayout.NORTH);
        stat.add(boxY,BorderLayout.CENTER);
        stat.add(reponse, BorderLayout.SOUTH);
        stat.setPreferredSize(new Dimension(250,0));
        contentPane.add(stat, BorderLayout.WEST);
        contentPane.revalidate();
        contentPane.repaint();
    }

    public Path actualPath(){
        return Paths.get(".").normalize().toAbsolutePath();
    }

    public void actionButton(){
        statButton.addActionListener( (event) -> createStatPanel());
    }
}
