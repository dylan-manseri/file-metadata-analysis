package core.gui;

import core.cli.Commande;
import core.cli.Fichier;
import core.cli.Recherche;
import core.cli.Repertoire;
import core.cli.save.Compare;
import core.cli.save.Sauvegarde;
import exception.WrongArgumentException;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * Cette classe gère l'affichage du panneau gauche concernant des analyses
 *
 * @author Dylan Manseri
 * @version 1.0
 */
public class LeftPanel extends JPanel {
    /**
     * contenaire parent
     */
    JPanel base;
    /**
     * type d'information à affiché sur ce panneau
     */
    String type;
    /**
     * nom du fichier cliqué de l'arborescence
     */
    String nameFile;

    /**
     * Instancie un LeftPanel et appelle la methode associé à l'analyse à effectuer et à afficher sur ce panneau
     * @param base contenaire parent
     * @param type type d'analyse
     */
    public LeftPanel(JPanel base, String type) {
        this.base=base;
        this.type=type;
        switch(type){
            case "stat" -> createStatPanel();
            case "info" -> createInfoPanel();
            case "save" -> createSavePanel();
            case "compare" -> createComparePanel();
            case "search" -> createSearchPanel();
            case "help" -> createHelp();
        }
    }

    /**
     * Instancie un LeftPanel et appelle la methode associé à l'analyse à effectuer et à afficher sur ce panneau
     * Ce deuxième constructeur sert au cas où l'utilisateur clique sur l'arborescence.
     * @param base contenaire parent
     * @param type type d'analyse
     * @param name nom du fichier/dossier où l'utilisateur a cliqué
     * @throws Exception si l'extraction des données du fichiers/dossier a échoué
     */
    public LeftPanel(JPanel base, String type, String name) throws Exception {
        this.base=base;
        this.type=type;
        this.nameFile=name;
        if(type.equals("analyze")){
            createAnalyzePanel();
        }
    }

    /**
     * Instancie un LeftPanel
     * Ce 3e constructeur sert a faciliter la suppression du ce panneau dans la classe MainWindow
     */
    public LeftPanel(){
        this.setVisible(false);
    }

    /**
     * Affiche sur le panneau les commandes disponnible
     */
    private void createHelp() {
        this.setLayout(new BorderLayout());
        JTextArea helpTxt = new JTextArea();
        String[] args = {"helpGui"};
        try{
            Commande c = new Commande(args);
            helpTxt.setText(c.getConsoleMessage());
        } catch (Exception e) {
            //La commande help ne peut pas provoquer d'erreur
        }
        this.add(helpTxt);
        base.revalidate();
        base.repaint();
    }

    /**
     * Affiche sur le panneau les resultats de l'analyse du fichier cliqué par l'utilisateur
     * Permet à l'utilisateur si il le souhaite d'afficher l'image correspondant à l'analyse
     * @throws Exception si l'extraction des données du fichier/dossier a échoué
     */
    private void createAnalyzePanel() throws Exception {
        this.setBorder(new EmptyBorder(10,10,10,10));
        this.setLayout(new BorderLayout());
        ImageIcon imageIcon=null;
        JLabel image = new JLabel("---Image---");
        image.setPreferredSize(new Dimension(90,60));
        JButton afficher = new JButton("Afficher");
        afficher.setPreferredSize(new Dimension(90,60));
        JLabel imageLabel =new JLabel();
        imageLabel.setPreferredSize(new Dimension(100,100));
        image.setVisible(false);
        imageLabel.setVisible(false);
        afficher.setVisible(false);
        JLabel stat = new JLabel("---Statistiques---");
        stat.setPreferredSize(new Dimension(90,60));
        JLabel meta = new JLabel("---Metadonnees---");
        meta.setPreferredSize(new Dimension(90,60));

        JTextArea statConsole = new JTextArea();
        statConsole.setLineWrap(true);
        statConsole.setWrapStyleWord(true);
        JScrollPane statScroll = new JScrollPane(statConsole, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
                JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        statScroll.setPreferredSize(new Dimension(200,100));

        JTextArea metaConsole = new JTextArea();
        metaConsole.setLineWrap(true);
        metaConsole.setWrapStyleWord(true);
        JScrollPane metaScroll = new JScrollPane(metaConsole, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
                JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        metaScroll.setPreferredSize(new Dimension(140,200));

        JPanel box = new JPanel();
        box.setLayout(new BoxLayout(box,BoxLayout.Y_AXIS));
        JScrollPane theBox = new JScrollPane(box, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
                JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        Recherche r = new Recherche("gui", nameFile);
        String chemin = r.getFileFound().getAbsolutePath();
        doStatAnalyze(chemin, statConsole);
        if(r.isImage()){
            BufferedImage b = ImageIO.read(new File(chemin));
            Image scaledImage = b.getScaledInstance(200, 200, Image.SCALE_SMOOTH);
            ImageIcon icon = new ImageIcon(scaledImage);
            imageLabel = new JLabel(icon);
            image.setVisible(true);
            imageLabel.setVisible(true);
            afficher.setVisible(true);
            doInfoAnalyze(chemin, metaConsole);
            afficher.addActionListener(e -> openImageWindow(new ImageIcon(b)));
        }
        else{
            metaConsole.setText("Les metadonnees ne s'affichent que pour les images");
        }
        box.add(image); box.add(imageLabel); box.add(afficher);
        box.add(stat); box.add(statScroll); box.add(meta); box.add(metaScroll);

        this.setPreferredSize(new Dimension(400,0));
        this.setLayout(new BorderLayout());
        this.add(theBox);
        base.revalidate();
        base.repaint();

    }

    /**
     * Ouvre une nouvelle fenêtre affichant une image
     * @param imageIcon l'image à afficher
     */
    private void openImageWindow(ImageIcon imageIcon) {
        ImageWindow im = new ImageWindow(imageIcon);
        im.setVisible(true);
    }

    /**
     * Affiche sur le panneau une bare de recherche et offre la possibilité de choisir un critère
     * Cette methode appelle d'autre methode pour effectuer la recherche et afficher le resultat
     */
    private void createSearchPanel() {
        this.setBorder(new EmptyBorder(10,10,10,10));

        String[] critere = {"Nom", "Annee", "Date", "Dimension", "Annee/Dimension"};
        JComboBox<String> critereBox = new JComboBox<>(critere);
        JLabel donneeTxt = new JLabel("Donnee :");

        JTextField nomTxt = new JTextField();
        nomTxt.setPreferredSize(new Dimension(200,30));

        JPanel askNom = new JPanel(new FlowLayout());
        askNom.add(donneeTxt); askNom.add(nomTxt);

        JButton recherche = new JButton("RECHERCHER");
        recherche.setPreferredSize(new Dimension(100,50));

        JPanel boxNom = new JPanel();
        boxNom.setLayout(new BoxLayout(boxNom,BoxLayout.X_AXIS));
        boxNom.add(donneeTxt); boxNom.add(nomTxt);

        JPanel boxY = new JPanel();
        boxY.setLayout(new BoxLayout(boxY,BoxLayout.Y_AXIS));
        boxY.add(boxNom); boxY.add(critereBox); boxY.add(recherche);

        JTextArea reponseConsole = new JTextArea();
        reponseConsole.setEditable(false);
        reponseConsole.setLineWrap(true);
        reponseConsole.setWrapStyleWord(true);
        JScrollPane rep = new JScrollPane(reponseConsole, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
                JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        rep.setPreferredSize(new Dimension(300,100));

        JLabel txt = new JLabel("Recherche, indiquer une donnee selon le critere");
        txt.setPreferredSize(new Dimension(250,50));

        this.add(txt, BorderLayout.NORTH);
        this.add(boxY,BorderLayout.CENTER);
        this.add(rep, BorderLayout.SOUTH);
        this.setPreferredSize(new Dimension(300,0));
        base.revalidate();
        base.repaint();
       recherche.addActionListener(e-> {
            try {
                String critereActuel = (String) critereBox.getSelectedItem();
                if(critereActuel==null) throw new WrongArgumentException("");
                doSearch(nomTxt, critereActuel,reponseConsole);
            } catch (WrongArgumentException | IOException ex) {
                reponseConsole.setText("Erreur lors de la recherche");
            }
        });
    }

    /**
     * Effectue une recherche selon un critère et affiche le resultat de celle ci
     * @param donneeTxt donnee de la recherche
     * @param critereActuel critere de la recherche
     * @param reponseConsole le resultat à écrire
     * @throws WrongArgumentException si la donnée de la recherche est mauvaise
     * @throws IOException si l'extraction des données pendant le parcours de l'arborescence échoue
     */
    private void doSearch(JTextField donneeTxt, String critereActuel, JTextArea reponseConsole) throws WrongArgumentException, IOException {
        switch (critereActuel){
            case "Nom" -> {
                Recherche r = new Recherche("--name", donneeTxt.getText());
                reponseConsole.setText(r.getConsoleMessage());
            }
            case "Annee" -> {
                Recherche r = new Recherche("--year", donneeTxt.getText());
                reponseConsole.setText(r.getConsoleMessage());
            }
            case "Date" -> {
                System.out.println(donneeTxt.getText());
                Recherche r = new Recherche("--date", donneeTxt.getText());
                reponseConsole.setText(r.getConsoleMessage());

            }
            case "Dimension" -> {
                Recherche r = new Recherche("--dim", donneeTxt.getText());
                reponseConsole.setText(r.getConsoleMessage());
            }
            case "Annee/Dimension" -> {
                String donnee = donneeTxt.getText();
                verifSyntaxe(donnee);
                String annee = donnee.substring(0,donnee.indexOf(" "));
                String dim = donnee.substring(donnee.indexOf(" ")+1);
                Recherche r = new Recherche("--dim", "--year", dim, annee);
                reponseConsole.setText(r.getConsoleMessage());
            }
        }
    }

    /**
     * Verifie la syntaxe entrée par l'utilisateur
     * Cette methode sert lorsque l'utilisateur tente d'effectuer une recherche sur deux critère
     * @param donnee donnée entrée par l'utilisateur
     * @throws WrongArgumentException si la donnée entrée est mauvaise
     */
    private void verifSyntaxe(String donnee) throws WrongArgumentException {
        int space =0;
        int i=0;
        while(space !=2 && i<donnee.length()){
            if(donnee.charAt(i)==' '){
                space++;
            }
            i++;
        }
        if(space!=1){
            throw new WrongArgumentException("");
        }
    }

    /**
     * Affiche sur le panneau deux bloc de texte permettant à l'utilisateur d'entrée un chemin et une capture
     * Cette methode appelle d'autres methodes qui font la comparaison entre les deux données
     */
    private void createComparePanel() {
        this.setBorder(new EmptyBorder(10,10,10,10));

        JLabel chemin = new JLabel("Chemin :");
        JLabel captureName = new JLabel("Capture name :");
        JTextField cheminTxt = new JTextField();
        JTextField cheminCapture = new JTextField();
        cheminTxt.setPreferredSize(new Dimension(200,30));
        cheminCapture.setPreferredSize(new Dimension(200,30));
        JPanel ask2 = new JPanel(new FlowLayout());
        ask2.add(captureName); ask2.add(cheminCapture);

        JButton compare = new JButton("COMPARER");
        compare.setPreferredSize(new Dimension(100,50));
        JPanel boxX = new JPanel();
        boxX.setLayout(new BoxLayout(boxX,BoxLayout.X_AXIS));
        boxX.add(chemin); boxX.add(cheminTxt);
        JPanel boxY = new JPanel();
        boxY.setLayout(new BoxLayout(boxY,BoxLayout.Y_AXIS));
        boxY.add(boxX); boxY.add(ask2); boxY.add(compare);

        JTextArea reponseConsole = new JTextArea();
        reponseConsole.setEditable(false);
        reponseConsole.setLineWrap(true);
        reponseConsole.setWrapStyleWord(true);
        JScrollPane rep = new JScrollPane(reponseConsole, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
                JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        rep.setPreferredSize(new Dimension(300,100));

        JLabel stati = new JLabel("Comparaison, indiquer un chemin");
        stati.setPreferredSize(new Dimension(180,50));

        this.add(stati, BorderLayout.NORTH);
        this.add(boxY,BorderLayout.CENTER);
        this.add(rep, BorderLayout.SOUTH);
        this.setPreferredSize(new Dimension(300,0));
        base.revalidate();
        base.repaint();
        compare.addActionListener(e-> {
            try {
                doCompare(cheminTxt, cheminCapture, reponseConsole);
            } catch (WrongArgumentException | IOException ex) {
                reponseConsole.setText("Erreur lors de la comparaison");
            }
        });
    }

    /**
     * Effectue la comparaison du chemin d'un dossier et du nom de la capture
     * Cette methode appelle d'autre methode et affiche le resultat de la comparaison
     * @param cheminTxt bloc de texte où l'utilisateur à entrée le chemin
     * @param captureName bloc de texte où l'utilisateur a entrée le nom de la capture
     * @param reponse le bloc de texte où la reponse doit être écrite
     * @throws WrongArgumentException si l'utilisateur entre une donnée incorrect
     * @throws IOException si l'extraction des données d'un fichier a échoué
     */
    private void doCompare(JTextField cheminTxt,JTextField captureName, JTextArea reponse) throws WrongArgumentException, IOException {
        File fic = new File(cheminTxt.getText());
        if(!fic.isDirectory()){
            reponse.setText("Merci d'indiquer un dossier valide");
        }
        else{
            Compare c = new Compare(fic, captureName);
            reponse.setText(c.getReponseConsole());
        }
    }

    /**
     * Affiche sur le panneau un bloc de texte où l'utilisateur entre le chemin d'un dossier à sauvegarder
     * Cette methode appelle d'autre methode effectuant la sauvegarde
     */
    private void createSavePanel() {
        this.setBorder(new EmptyBorder(10,10,10,10));

        JLabel chemin = new JLabel("Chemin :");
        JTextField cheminTxt = new JTextField();
        cheminTxt.setPreferredSize(new Dimension(200,30));
        JPanel ask = new JPanel(new FlowLayout());
        ask.add(chemin); ask.add(cheminTxt);

        JButton save = new JButton("SAUVEGARDER");
        save.setPreferredSize(new Dimension(100,50));
        JPanel boxX = new JPanel();
        boxX.setLayout(new BoxLayout(boxX,BoxLayout.X_AXIS));
        boxX.add(chemin); boxX.add(cheminTxt);
        JPanel boxY = new JPanel();
        boxY.setLayout(new BoxLayout(boxY,BoxLayout.Y_AXIS));
        boxY.add(boxX); boxY.add(save);

        JTextArea reponseConsole = new JTextArea();
        reponseConsole.setEditable(false);
        reponseConsole.setLineWrap(true);
        reponseConsole.setWrapStyleWord(true);
        JScrollPane rep = new JScrollPane(reponseConsole, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
                JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        rep.setPreferredSize(new Dimension(300,100));

        JLabel stati = new JLabel("Sauvegarde, indiquer un chemin");
        stati.setPreferredSize(new Dimension(170,50));

        this.add(stati, BorderLayout.NORTH);
        this.add(boxY,BorderLayout.CENTER);
        this.add(rep, BorderLayout.SOUTH);
        this.setPreferredSize(new Dimension(300,0));
        base.revalidate();
        base.repaint();
        save.addActionListener(e-> {
            try {
                doSave(cheminTxt, reponseConsole);
            } catch (WrongArgumentException | IOException ex) {
                reponseConsole.setText("Erreur lors de la sauvegarde");
            }
        });
    }

    /**
     * Effectue la sauvegarde d'un fichier en instanciant la classe concerné
     * @param cheminTxt bloc de texte où l'utilisateur à entrée le chemin du dossier à sauvegarder
     * @param reponse bloc de texte où la reponse doit être écrite
     * @throws WrongArgumentException si l'utilisateur entre une donnée incorrect
     * @throws IOException si l'extraction des données d'un fichier a échoué
     */
    private void doSave(JTextField cheminTxt, JTextArea reponse) throws WrongArgumentException, IOException {
        Sauvegarde save = new Sauvegarde(cheminTxt);
        reponse.setText(save.getMessage());
    }

    /**
     * Affiche sur le panneau un bloc de texte où l'utilisateur entre le chemin d'un ficher pour voir ses statistiques basiques.
     * Cette methode appelle d'autres methodes effectuant l'extraction des statistiques et l'affichage.
     */
    private void createStatPanel(){
        this.setBorder(new EmptyBorder(10,10,10,10));

        JLabel chemin = new JLabel("Chemin :");
        JTextField cheminTxt = new JTextField();
        cheminTxt.setPreferredSize(new Dimension(200,30));
        JPanel ask = new JPanel(new FlowLayout());
        ask.add(chemin); ask.add(cheminTxt);

        JButton entree = new JButton("ENTREE");
        entree.setPreferredSize(new Dimension(100,50));
        JPanel boxX = new JPanel();
        boxX.setLayout(new BoxLayout(boxX,BoxLayout.X_AXIS));
        boxX.add(chemin); boxX.add(cheminTxt);
        JPanel boxY = new JPanel();
        boxY.setLayout(new BoxLayout(boxY,BoxLayout.Y_AXIS));
        boxY.add(boxX); boxY.add(entree);

        JTextArea reponseConsole = new JTextArea();
        reponseConsole.setEditable(false);
        reponseConsole.setLineWrap(true);
        reponseConsole.setWrapStyleWord(true);
        JScrollPane rep = new JScrollPane(reponseConsole, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
                JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        rep.setPreferredSize(new Dimension(300,100));

        JLabel stati = new JLabel("Statistique, indiquer un chemin");
        stati.setPreferredSize(new Dimension(170,50));

        this.add(stati, BorderLayout.NORTH);
        this.add(boxY,BorderLayout.CENTER);
        this.add(rep, BorderLayout.SOUTH);
        this.setPreferredSize(new Dimension(300,0));
        base.revalidate();
        base.repaint();
        entree.addActionListener(e-> {
            try {
                doStatAnalyze(cheminTxt.getText(), reponseConsole);
            } catch (WrongArgumentException | IOException ex) {
                reponseConsole.setText("Erreur lors de l'extraction des statistiques");
            }
        });
    }

    /**
     * Affiche sur le panneau un bloc de texte où l'utilisateur entre le chemin d'un ficher pour voir ses metadonnées.
     * Cette methode appelle d'autres methodes effectuant l'extraction des statistiques et l'affichage.
     */
    private void createInfoPanel(){
        this.setBorder(new EmptyBorder(10,10,10,10));

        JLabel chemin = new JLabel("Chemin :");
        JTextField cheminTxt = new JTextField();
        cheminTxt.setPreferredSize(new Dimension(200,30));
        JPanel ask = new JPanel(new FlowLayout());
        ask.add(chemin); ask.add(cheminTxt);

        JButton entree = new JButton("ENTREE");
        entree.setPreferredSize(new Dimension(100,50));
        JPanel boxX = new JPanel();
        boxX.setLayout(new BoxLayout(boxX,BoxLayout.X_AXIS));
        boxX.add(chemin); boxX.add(cheminTxt);
        JPanel boxY = new JPanel();
        boxY.setLayout(new BoxLayout(boxY,BoxLayout.Y_AXIS));
        boxY.add(boxX); boxY.add(entree);

        JTextArea reponseConsole = new JTextArea();
        reponseConsole.setEditable(false);
        reponseConsole.setLineWrap(true);
        reponseConsole.setWrapStyleWord(true);
        JScrollPane rep = new JScrollPane(reponseConsole, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
                JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        rep.setPreferredSize(new Dimension(300,100));

        JLabel stati = new JLabel("Metadonnee, indiquer un chemin");
        stati.setPreferredSize(new Dimension(170,50));

        this.add(stati, BorderLayout.NORTH);
        this.add(boxY,BorderLayout.CENTER);
        this.add(rep, BorderLayout.SOUTH);
        this.setPreferredSize(new Dimension(300,0));
        base.revalidate();
        base.repaint();
        entree.addActionListener(e-> {
            try {
                doInfoAnalyze(cheminTxt.getText(), reponseConsole);
            } catch (Exception ex) {
                reponseConsole.setText("Erreur lors de l'extraction des metadonnees");
            }
        });
    }

    /**
     * Effectue l'extraction des metadonnée et l'affiche sur le bloc de texte dédié.
     * Cette classe instancie Fichier et affiche les metadonnées du fichier
     * @param chemin chemin entrée par l'utilisateur
     * @param reponse bloc de texte où la reponse sera écrite
     * @throws Exception si l'extraction des métadonnées échoue
     */
    private void doInfoAnalyze(String chemin, JTextArea reponse) throws Exception {
        File f = new File(chemin);
        String meta;
        if(f.isFile()){
            Fichier fic = new Fichier(chemin);
            fic.printInfo();
            meta=fic.getConsoleMessage();
        }
        else{
            meta="Merci d'indiquer un REPERTOIRE valide, faites help pour de l'aide";
        }
        reponse.setText(meta);
        base.revalidate();
        base.repaint();
    }

    /**
     * Effectue l'extraction des statistiques en instanciant la classe Fichier ou Repertoire
     * Affiche le resultat de l'extraction sur le bloc de texte dédié
     * @param chemin chemin du fichier/repertoire entrée par l'utilisateur
     * @param reponse bloc de texte où la reponse doit être écrite
     * @throws WrongArgumentException si l'utilisateur entre une donnée incorrect
     * @throws IOException si l'extraction des données d'un fichier/repertoire a échoué
     */
    private void doStatAnalyze(String chemin, JTextArea reponse) throws WrongArgumentException, IOException {
        File f = new File(chemin);
        String stat;
        if(f.isFile()){
            Fichier fic = new Fichier(chemin);
            stat=fic.printStatGui();
        }
        else{
            Repertoire rep = new Repertoire(chemin);
            stat= rep.printStatGui();
        }
        reponse.setText(stat);
        base.revalidate();
        base.repaint();
    }



}
