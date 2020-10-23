package com.kma.solver.comparators;

import com.zetcode.Board;

import java.util.Comparator;
import static java.lang.Math.abs;

public class DistanceComparator implements Comparator<Integer> {

	private final Board board;

	public DistanceComparator(Board board) {
		this.board = board;
	}

	@Override
	public int compare(Integer o1, Integer o2) {
		int candyX = board.getXByPosition(board.getCandyPos());
		int candyY = board.getYByPosition(board.getCandyPos());
		int d1 = abs(candyX - board.getXByPosition(o1)) + abs(candyY - board.getYByPosition(o1));
		int d2 = abs(candyX - board.getXByPosition(o2)) + abs(candyY - board.getYByPosition(o2));
		return d1 - d2;
	}
}
