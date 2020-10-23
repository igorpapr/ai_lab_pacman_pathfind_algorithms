package com.kma;

import com.kma.solver.ARBFSMazeSolver;
import com.kma.solver.BFSMazeSolver;
import com.kma.solver.DFSMazeSolver;
import com.kma.solver.GreedyMazeSolver;
import com.zetcode.Board;
import com.zetcode.Pacman;

import java.awt.*;
import java.util.List;

public class Main {

	public static void main(String[] args) {
		var board = new Board(true, 13); //1255
		EventQueue.invokeLater(() -> {

			var ex = new Pacman(board);
			ex.setVisible(true);
		});

		//to show all the board first
		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		//var solver = new BFSMazeSolver(board, 172);
		//TRY ONE OF THESE
		//var solver = new DFSMazeSolver(board, 172);
		//var solver = new GreedyMazeSolver(board, 172);
		var solver = new ARBFSMazeSolver(board, 172);
		List<Integer> path = solver.solve();

		Gamer gamer = new Gamer(path, board);
		gamer.start();

	}
}
