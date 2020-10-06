package com.zetcode;

import javax.swing.*;
import java.awt.*;

public class Pacman extends JFrame {

    private final Board board;

    public Pacman(Board board) {
        this.board = board;

        initUI();
    }

    private void initUI() {

        add(board);

        setTitle("Pacman Best Algorithm Find Path to KONFETA");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(380, 420);
        setLocationRelativeTo(null);
    }

    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {

            var ex = new Pacman(new Board());
            ex.setVisible(true);
        });
    }
}
