package com.kma;

import com.zetcode.Pacman;

import java.awt.*;

public class Main {

	public static void main(String[] args) {
		EventQueue.invokeLater(() -> {

			var ex = new Pacman(true);
			ex.setVisible(true);
		});
	}
}
