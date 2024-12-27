package core.gui;

import core.cli.Fichier;
import core.cli.Repertoire;
import exception.WrongArgumentException;
import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.io.File;
import java.io.IOException;

public class Analyse extends JFrame {
    JPanel base;
    String type;

    public Analyse(JPanel base, String type) {
        this.base=base;
        this.type=type;
        switch(type){
            case "stat" -> {
                createStatPanel();
            }
            case "info" -> {

            }
            case "save" -> {

            }
        }
    }

    public void createStatPanel(){
        JPanel stat = new JPanel(new BorderLayout());
        stat.setBorder(new EmptyBorder(10,10,10,10));

        JLabel chemin = new JLabel("Chemin :");
        JTextField cheminTxt = new JTextField();
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

        JTextArea reponse = new JTextArea(); //CHANGEMENT
        reponse.setEditable(false); //CHANGEMENT
        reponse.setWrapStyleWord(true); //CHANGEMENT
        Border border = BorderFactory.createLineBorder(Color.BLACK, 2);
        reponse.setBorder(border);
        reponse.setPreferredSize(new Dimension(0,325));

        JLabel stati = new JLabel("Statistique -> indiquer un chemin");
        stati.setPreferredSize(new Dimension(0,130));

        stat.add(stati, BorderLayout.NORTH);
        stat.add(boxY,BorderLayout.CENTER);
        stat.add(reponse, BorderLayout.SOUTH);
        stat.setPreferredSize(new Dimension(250,0));
        base.add(stat, BorderLayout.WEST);
        base.revalidate();
        base.repaint();
        entree.addActionListener(e-> {
            try {
                doStatAnalyze(cheminTxt,reponse);
            } catch (WrongArgumentException | IOException ex) {
                reponse.setText("Erreur lors de l'extraction des statistiques");
            }
        });
    }

    public void doStatAnalyze(JTextField chemin, JTextArea reponse) throws WrongArgumentException, IOException {
        String s=chemin.getText();
        File f = new File(s);
        String stat;
        if(f.isFile()){
            Fichier fic = new Fichier(s);
            stat=fic.printStatGui(); //CHANGEMENT
        }
        else{
            Repertoire rep = new Repertoire(s);
            stat= rep.printStatGui(); //CHANGEMENT
        }
        reponse.setText(stat);
        base.revalidate();
        base.repaint();
    }
}
