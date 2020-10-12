package com.kma.solver;

import com.zetcode.Board;

import java.util.*;

public class BFSMazeSolver extends MazeSolver {

    private final Set<Integer> visited;
    private final Queue<Integer> queue;
    private final HashMap<Integer, Integer> pathMap;

    public BFSMazeSolver(Board map, int pacmanPosition) {
        this.map = map;
        this.pacmanPosition = pacmanPosition;

        this.visited = new HashSet<>(map.getN_BLOCKS() * map.getN_BLOCKS());
        this.queue = new ArrayDeque<>();
        this.pathMap = new HashMap<>();
    }

    private void fillQueue(int pos) {
        List<Optional<Integer>> waysList = new ArrayList<>();
        waysList.add(map.canMoveUp(pos));
        waysList.add(map.canMoveRight(pos));
        waysList.add(map.canMoveDown(pos));
        waysList.add(map.canMoveLeft(pos));

        checkPresenceAndRememberPath(waysList, pos);
    }

    private void checkPresenceAndRememberPath(List<Optional<Integer>> posAroundList, int currentPos){
        for(Optional<Integer> posToAdd : posAroundList){
            if (posToAdd.isPresent() && !visited.contains(posToAdd.get())) {
                queue.add(posToAdd.get());
                pathMap.put(posToAdd.get(), currentPos);
            }
        }
    }

    @Override
    public List<Integer> solve() {
        return solve(pacmanPosition);
    }

    private List<Integer> solve(int initPos) {
        boolean solved = false;
        int pos = initPos;
        while (!solved) {
            queue.add(pos);
            visited.add(pos);
            pathMap.put(pos, -1);
            fillQueue(pos);
            while (!queue.isEmpty()) {
                pos = queue.remove();
                if (visited.contains(pos))
                    continue;
                visited.add(pos);
                map.addPathAfter(pathMap.get(pos), pos);
                try {
                    Thread.sleep(50);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                fillQueue(pos);
                //FOUND CANDY
                if (map.candyIsUnderneath(pos)) {
                    System.out.println("solved!");
                    solved = true;
                    break;
                }
            }
        }
        return recreatePath(pathMap, pos);
    }

    private List<Integer> recreatePath(Map<Integer, Integer> pathArr, int lastPosition){
        List<Integer> res = new LinkedList<>();
        int current = lastPosition;
        int temp;
        while(current != -1){
            temp = pathArr.get(current);
            if(temp != -1)
                res.add(temp);
            current = temp;
        }
        Collections.reverse(res);
        return res;
    }
}
