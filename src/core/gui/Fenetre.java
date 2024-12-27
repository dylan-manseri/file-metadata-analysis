package core.gui;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;

public class Fenetre extends JFrame {
    JButton statButton = new JButton("Statistiques");
    JButton metaButton = new JButton("Metadonnee");
    JButton searchButton = new JButton("Recherche");
    JButton saveButton = new JButton("Sauvegarde");
    JTree dossier;
    JPanel contentPane = (JPanel) this.getContentPane();
    Analyse AnalyzeWindow;

    public Fenetre(){
        super("Fichier et metadonnee");
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        this.setSize(900,600);
        this.setLocationRelativeTo(null);
        File actual = actualPath().toFile();
        File[] fichiers = actual.listFiles();
        if(fichiers==null || !actual.isDirectory()){
            JTextArea err = new JTextArea("Chemin invalide merci de mettre le .jar dans un repertoire valide");
            contentPane.add(err);
        }
        else{
            JPanel arbo = new JPanel();
            arbo.setLayout(new BorderLayout());

            DefaultMutableTreeNode racine = createNoeud(actual.getName());
            racine = createRacine(fichiers, 0, racine);
            this.dossier = new JTree(racine);
            JScrollPane dossierScroll = new JScrollPane(dossier);

            JLabel nom = new JLabel(actualPath().toString());
            nom.setPreferredSize(new Dimension(100,30));
            arbo.add(nom, BorderLayout.NORTH);
            arbo.add(dossierScroll, BorderLayout.CENTER);
            contentPane.add(createToolBar(), BorderLayout.NORTH);
            contentPane.add(arbo,BorderLayout.CENTER);
            toolBarButton();
        }

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

    public void toolBarButton() {
        statButton.addActionListener((event) -> AnalyzeWindow = new Analyse(contentPane, "stat"));
        dossier.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                popUpMenu(e);
            }
        });
    }
    public DefaultMutableTreeNode createRacine(File[] fichiers, int i, DefaultMutableTreeNode racine){
        if(i==fichiers.length){
            return racine;
        }
        File fic = fichiers[i];
        if(fic.isDirectory()){
            DefaultMutableTreeNode noeud = createNoeud(fic.getName());
            File[] fichiesrInterne = fic.listFiles();
            if(fichiesrInterne!=null){
                noeud=createRacine(fichiesrInterne,0,noeud);
            }
            else{
                noeud.setUserObject("Dossier inaccessible");
            }
            racine.add(noeud);
        }
        else if(fic.isFile()){
            DefaultMutableTreeNode feuille = createFeuille(fic.getName());
            racine.add(feuille);
        }
        else{
            DefaultMutableTreeNode feuille = createFeuille("Fichier inaccessible");
            racine.add(feuille);
        }
        return createRacine(fichiers,i+1,racine);
    }

    public DefaultMutableTreeNode createNoeud(String name){
        return new DefaultMutableTreeNode(name);
    }

    public DefaultMutableTreeNode createFeuille(String name){
        return new DefaultMutableTreeNode(name);
    }

    public void popUpMenu(MouseEvent e){
        if (SwingUtilities.isRightMouseButton(e)) {
            JPopupMenu bare = new JPopupMenu("Option :");
            JButton s = new JButton("Statistiques");
            s.addActionListener((event)->new Analyse(contentPane, "stat"));
            JButton m = new JButton("Metadonnee");
            JButton sa = new JButton("Sauvegarde");
            bare.add(s); bare.add(m); bare.add(sa);
            bare.show(e.getComponent(), e.getX(), e.getY());
        }
    }

    public void createAnalyzeWindow(String type){
        if(AnalyzeWindow==null){
            AnalyzeWindow=new Analyse(contentPane, type);
        }
        else{
            AnalyzeWindow.reset();
            AnalyzeWindow=new Analyse(contentPane, type);
        }
    }
}
