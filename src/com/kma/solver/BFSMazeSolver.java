package com.kma.solver;

import com.zetcode.Board;

import java.util.*;

public class BFSMazeSolver extends MazeSolver {

    private final Set<Integer> visited;
    private final Queue<Integer> queue;

    //used in order to remember the previous step of each step
    private final HashMap<Integer, Integer> pathMap;

    public BFSMazeSolver(Board map, int pacmanPosition) {
        this.map = map;
        this.pacmanPosition = pacmanPosition;

        this.visited = new HashSet<>(map.getN_BLOCKS() * map.getN_BLOCKS());
        this.queue = new ArrayDeque<>();
        this.pathMap = new HashMap<>();
    }

    //function of finding the next step
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

    //BFS algorithm
    @Override
    protected List<Integer> solve(int initPos) {
        boolean solved = false;
        int pos = initPos;
        while (!solved) {
            queue.add(pos);
            visited.add(pos);
            //initial step
            pathMap.put(pos, -1);
            fillQueue(pos);
            while (!queue.isEmpty()) {
                pos = queue.remove();
                count++;
                if (visited.contains(pos))
                    continue;
                visited.add(pos);
                map.addPathAfter(pathMap.get(pos), pos);
                sleep();

                //found candy
                if (map.candyIsUnderneath(pos)) {
                    System.out.println("solved!");
                    solved = true;
                    break;
                }

                fillQueue(pos);
            }
        }
        return recreatePath(pathMap, pos);
    }

    //recreating path by pathMap
    private List<Integer> recreatePath(Map<Integer, Integer> pathMap, int lastPosition){
        List<Integer> res = new LinkedList<>();
        res.add(lastPosition);
        int current = lastPosition;
        int temp;
        while(current != -1){
            temp = pathMap.get(current);
            if(temp != -1)
                res.add(temp);
            current = temp;
        }
        Collections.reverse(res);
        return res;
    }

    public int getCount() {
        return count;
    }

    @Override
    public String toString() {
        return "BFSMazeSolver";
    }
}
