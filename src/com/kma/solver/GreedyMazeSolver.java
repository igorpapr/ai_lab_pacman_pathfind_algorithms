package com.kma.solver;

import com.zetcode.Board;

import java.util.*;

public class GreedyMazeSolver extends DFSMazeSolver{

	public GreedyMazeSolver(Board map, int pacmanPosition) {
		super(map, pacmanPosition);
	}

	@Override
	protected Optional<Integer> chooseNext(int pos) {
		Optional<Integer> up = map.canMoveUp(pos);
		Optional<Integer> right = map.canMoveRight(pos);
		Optional<Integer> down = map.canMoveDown(pos);
		Optional<Integer> left = map.canMoveLeft(pos);

		HashMap<Optional<Integer>, Double> directionsToDistancesMap = new HashMap<>();

		Arrays.asList(up, right, down, left).forEach(direction -> {
			if (direction.isPresent() && !this.visited.contains(direction.get())) {
				directionsToDistancesMap.put(direction, map.getDistanceFromBlockToCandy(direction.get()));
			}
		});

		//finding the shortest straight distance
		if (!directionsToDistancesMap.isEmpty()){
			Optional<Integer> direction = Collections.min(directionsToDistancesMap.entrySet(),Map.Entry.comparingByValue()).getKey();
			map.addPathAfter(pos, direction.get());
			return direction;
		}

		return Optional.empty();
	}

	@Override
	public String toString() {
		return "GreedyMazeSolver";
	}
}
