package core.gui;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreePath;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;

public class MainWindow extends JFrame {
    JButton statButton = new JButton("Statistiques");
    JButton metaButton = new JButton("Metadonnee");
    JButton searchButton = new JButton("Recherche");
    JButton saveButton = new JButton("Sauvegarde");
    JButton helpButton = new JButton("Help");
    JTree dossier;
    JPanel contentPane = (JPanel) this.getContentPane();
    LeftPanel LeftPanel = new LeftPanel();

    public MainWindow(){
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
            dossier.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    TreePath treeClique = dossier.getPathForLocation(e.getX(),e.getY());
                    if(treeClique !=null){
                        String noeudClique = treeClique.getLastPathComponent().toString();
                        try {
                            createAnalyzePanel(noeudClique);
                        } catch (Exception ex) {
                            showError(e);
                        }
                    }
                }

            });
            JScrollPane dossierScroll = new JScrollPane(dossier);

            JLabel nom = new JLabel(actualPath().toString());
            nom.setPreferredSize(new Dimension(100,30));
            arbo.add(nom, BorderLayout.NORTH);
            arbo.add(dossierScroll, BorderLayout.CENTER);
            contentPane.add(createToolBar(), BorderLayout.NORTH);
            contentPane.add(arbo,BorderLayout.CENTER);
            contentPane.add(LeftPanel, BorderLayout.WEST);

            toolBarButton();
        }

    }

    private void showError(MouseEvent e) {
        JLabel err = new JLabel("erreur lors de l'extraction");
        err.setBounds(e.getX(),e.getY(),150,30);
        Timer timer = new Timer(2000, event -> err.setVisible(false));
        timer.setRepeats(false);
        timer.start();
    }

    private void createAnalyzePanel(String clicName) throws Exception {
        contentPane.remove(LeftPanel);
        LeftPanel = new LeftPanel(contentPane,"analyze", clicName );
        contentPane.add(LeftPanel,BorderLayout.WEST);
    }

    private JToolBar createToolBar(){
        JToolBar toolBar = new JToolBar();
        toolBar.setFloatable(false);
        toolBar.addSeparator();
        toolBar.add(statButton); toolBar.addSeparator();
        toolBar.add(metaButton); toolBar.addSeparator();
        toolBar.add(saveButton); toolBar.addSeparator();
        toolBar.add(searchButton); toolBar.addSeparator();
        toolBar.add(helpButton); toolBar.addSeparator();
        return toolBar;
    }

    private void toolBarButton() {
        statButton.addActionListener((event) -> createLeftPanel("stat"));
        metaButton.addActionListener((event) -> createLeftPanel("info"));
        saveButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                popUpMenu(e);
            }
        });
        searchButton.addActionListener((event) -> createLeftPanel("search"));
        helpButton.addActionListener((event) -> createLeftPanel("help"));
    }

    private void createLeftPanel(String type) {
        contentPane.remove(LeftPanel);
        contentPane.add(LeftPanel = new LeftPanel(contentPane,type),BorderLayout.WEST);

    }

    private void popUpMenu(MouseEvent event){
        if(SwingUtilities.isLeftMouseButton(event)){
            JPopupMenu pop = new JPopupMenu("option");
            JButton save = new JButton("Sauvegarder");
            JButton compare = new JButton("Compare");
            pop.add(save); pop.add(compare);
            pop.show(event.getComponent(),event.getX(),event.getY());
            save.addActionListener( (e) -> createLeftPanel("save") );
            compare.addActionListener((ev) -> createLeftPanel("compare"));
        }
    }

    private DefaultMutableTreeNode createRacine(File[] fichiers, int i, DefaultMutableTreeNode racine){
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

    private DefaultMutableTreeNode createNoeud(String name){
        return new DefaultMutableTreeNode(name);
    }

    private DefaultMutableTreeNode createFeuille(String name){
        return new DefaultMutableTreeNode(name);
    }

    private Path actualPath(){
        return Paths.get(".").normalize().toAbsolutePath();
    }
}
