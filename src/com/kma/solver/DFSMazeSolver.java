package com.kma.solver;

import com.zetcode.Board;

import java.util.*;

public class DFSMazeSolver extends MazeSolver {

    private final Set<Integer> visited;
    private final Stack<Integer> stack;

    public DFSMazeSolver(Board map, int pacmanPosition) {
        this.map = map;
        this.pacmanPosition = pacmanPosition;

        this.visited = new HashSet<>(map.getN_BLOCKS() * map.getN_BLOCKS());
        this.stack = new Stack<>();
    }

    private Optional<Integer> chooseNext(int pos) {
        var up = map.canMoveUp(pos);
        var right = map.canMoveRight(pos);
        var down = map.canMoveDown(pos);
        var left = map.canMoveLeft(pos);

        if (up.isPresent() && !visited.contains(up.get())) return up;
        if (right.isPresent() && !visited.contains(right.get())) return right;
        if (down.isPresent() && !visited.contains(down.get())) return down;
        if (left.isPresent() && !visited.contains(left.get())) return left;
        return Optional.empty();
    }

    @Override
    public List<Integer> solve() {
        return null;
    }
}
