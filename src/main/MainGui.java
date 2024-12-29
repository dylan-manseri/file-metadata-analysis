package main;

import core.gui.MainWindow;
import javax.swing.*;
import javax.swing.plaf.nimbus.NimbusLookAndFeel;

/**
 * Classe principale gérant plusieurs fonctionnalité en fonction des arguments cliqués par l'utilisateur
 * Fonctionnalité :
 * - Extraction des statistiques d'un fichier/repertoire
 * - Extraction des metadonnées d'une images
 * - Recherche d'un fichier selon plusieurs critères dans une arborescence
 * - Realisation d'une sauvegarde d'un repertoire
 * - Comparaison d'une sauvegarde avec un autre repertoire
 * - Affichage d'une arborescence que l'on peut parcourir
 * - Affichage d'une image
 *
 * @author Dylan Manseri
 * @version 1.0
 */
public class MainGui {

    /**
     * Instancie MainGui et crée une fenêtre principale
     * @param args arguments entrée par l'utilisateur
     * @throws UnsupportedLookAndFeelException fomat non supporté
     */
    public static void main(String[] args) throws UnsupportedLookAndFeelException {
        UIManager.setLookAndFeel(new NimbusLookAndFeel());
        MainWindow f = new MainWindow();
        f.setVisible(true);
    }
}
