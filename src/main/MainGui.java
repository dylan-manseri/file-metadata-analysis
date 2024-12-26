package main;

import core.gui.Fenetre;

import javax.swing.*;
import javax.swing.plaf.nimbus.NimbusLookAndFeel;

public class MainGui {

    public static void main(String[] args) throws UnsupportedLookAndFeelException {
        UIManager.setLookAndFeel(new NimbusLookAndFeel());
        Fenetre f = new Fenetre();
        f.setVisible(true);
    }
}
