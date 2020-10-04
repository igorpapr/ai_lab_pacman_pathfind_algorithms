package com.zetcode;

import javax.swing.*;
import java.awt.*;

public class Pacman extends JFrame {

    private final boolean withoutIntroScreen;

    public Pacman(boolean withoutIntroScreen) {
        this.withoutIntroScreen = withoutIntroScreen;

        initUI();
    }

    private void initUI() {

        add(new Board(withoutIntroScreen));

        setTitle("Pacman Best Algorithm Find Path to KONFETA");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(380, 420);
        setLocationRelativeTo(null);
    }

    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {

            var ex = new Pacman(false);
            ex.setVisible(true);
        });
    }
}
