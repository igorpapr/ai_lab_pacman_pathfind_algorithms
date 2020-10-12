package com.kma.solver;

import com.zetcode.Board;

import java.util.*;

public class BFSMazeSolver extends MazeSolver {

    private final Set<Integer> visited;
    private final Queue<Integer> queue;

    public BFSMazeSolver(Board map, int pacmanPosition) {
        this.map = map;
        this.pacmanPosition = pacmanPosition;

        this.visited = new HashSet<>(map.getN_BLOCKS() * map.getN_BLOCKS());
        this.queue = new ArrayDeque<>() {
        };
    }

    private void fillQueue(int pos) {
        var up = map.canMoveUp(pos);
        var right = map.canMoveRight(pos);
        var down = map.canMoveDown(pos);
        var left = map.canMoveLeft(pos);

        if (up.isPresent() && !visited.contains(up.get()))
            queue.add(up.get());
        if (right.isPresent() && !visited.contains(right.get()))
            queue.add(right.get());
        if (down.isPresent() && !visited.contains(down.get()))
            queue.add(down.get());
        if (left.isPresent() && !visited.contains(left.get()))
            queue.add(left.get());
    }

    @Override
    public List<Integer> solve() {
        return solve(pacmanPosition);
    }

    private List<Integer> solve(int initPos) {
        var solved = false;
        var pos = initPos;
        while (!solved) {
            queue.add(pos);
            visited.add(pos);
            fillQueue(pos);
            while (!queue.isEmpty()) {
                map.path.add(pos);
                pos = queue.remove();
                fillQueue(pos);
                visited.add(pos);
                //FOUND CANDY
                if (map.candyIsUnderneath(pos)) {
                    System.out.println("solved!");
                    solved = true;
                    map.path.add(pos);
                    break;
                }

//                try {
//                    Thread.sleep(50);
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
            }
//            if (!solved) {
//                var prev = queue.pop();
//                map.path.pollLast();
//                var nextPrev = fillQueue(prev);
//                while (nextPrev.isEmpty()) {
//                    try {
//                        Thread.sleep(50);
//                    } catch (InterruptedException e) {
//                        e.printStackTrace();
//                    }
//                    prev = queue.pop();
//                    nextPrev = fillQueue(prev);
//                    map.path.pollLast();
//                }
//                pos = prev;
//            }
        }
        return new LinkedList<>(queue);
    }
}
