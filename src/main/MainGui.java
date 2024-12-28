package main;

import core.gui.MainWindow;

import javax.swing.*;
import javax.swing.plaf.nimbus.NimbusLookAndFeel;

public class MainGui {

    public static void main(String[] args) throws UnsupportedLookAndFeelException {
        UIManager.setLookAndFeel(new NimbusLookAndFeel());
        MainWindow f = new MainWindow();
        f.setVisible(true);
    }
}
