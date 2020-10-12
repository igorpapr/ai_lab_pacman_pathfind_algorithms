package com.kma;

import com.kma.solver.BFSMazeSolver;
import com.kma.solver.DFSMazeSolver;
import com.zetcode.Board;
import com.zetcode.Pacman;

import java.awt.*;

public class Main {

	public static void main(String[] args) {
		var board = new Board(true);
		EventQueue.invokeLater(() -> {

			var ex = new Pacman(board);
			ex.setVisible(true);
		});
		var solver = new BFSMazeSolver(board, 172);
		var path = solver.solve();
		for (var p : path) {
			System.out.print(p + " ");
		}
	}
}
