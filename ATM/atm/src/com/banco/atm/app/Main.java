package com.banco.atm.app;

import javax.swing.SwingUtilities;
import com.banco.atm.presentation.MainFrame;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            MainFrame frame = new MainFrame();
            frame.setVisible(true);
        });
    }
}
