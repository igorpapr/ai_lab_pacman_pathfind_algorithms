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

    //function of finding the next step
    private Optional<Integer> chooseNext(int pos) {
        var up = map.canMoveUp(pos);
        var right = map.canMoveRight(pos);
        var down = map.canMoveDown(pos);
        var left = map.canMoveLeft(pos);

        if (up.isPresent() && !visited.contains(up.get())) {
            map.addPathAfter(pos, up.get());
            return up;
        }

        if (right.isPresent() && !visited.contains(right.get())) {
            map.addPathAfter(pos, right.get());
            return right;
        }

        if (down.isPresent() && !visited.contains(down.get())) {
            map.addPathAfter(pos, down.get());
            return down;
        }

        if (left.isPresent() && !visited.contains(left.get())) {
            map.addPathAfter(pos, left.get());
            return left;
        }
        return Optional.empty();
    }

    //DFS algorithm
    @Override
    protected List<Integer> solve(int initPos) {
        var solved = false;
        var pos = initPos;
        while (!solved) {
            stack.push(pos);
            visited.add(pos);
            var next = chooseNext(pos);
            while (next.isPresent()) {
                count++;
                pos = next.get();
                stack.push(pos);
                visited.add(pos);

                if (map.candyIsUnderneath(pos)) {
                    System.out.println("solved!");
                    solved = true;
//                    map.addPathAfter(prev, pos);
                    break;
                }
                next = chooseNext(pos);
                sleep();
            }

            if (!solved) {
                var prev = stack.pop();
                var nextPrev = chooseNext(prev);
                while (nextPrev.isEmpty()) {
                    count++;
                    sleep();
                    prev = stack.pop();
                    nextPrev = chooseNext(prev);
                    map.removePathAfter(prev);
                }
                pos = prev;
            }
        }
        return new LinkedList<>(stack);
    }

    @Override
    public String toString() {
        return "DFSMazeSolver";
    }
}
