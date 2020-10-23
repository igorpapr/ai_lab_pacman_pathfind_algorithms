package com.kma.solver;

import com.kma.solver.comparators.DistanceComparator;
import com.zetcode.Board;

import java.util.PriorityQueue;

public class ARBFSMazeSolver extends BFSMazeSolver{

	public ARBFSMazeSolver(Board map, int pacmanPosition) {
		super(map, pacmanPosition);

		DistanceComparator comparator = new DistanceComparator(map);
		this.queue = new PriorityQueue<>(comparator);
	}

	@Override
	public String toString() {
		return "RBFSMazeSolver (A*)";
	}
}
