package com.kma;

import com.zetcode.Board;

import java.awt.*;
import java.util.ArrayDeque;
import java.util.List;
import java.util.Queue;

// will emulate pressing keyboard buttons with supplied key codes list
public class Gamer extends Thread {
    private final List<Integer> path;
    private final Queue<Integer> pathControlledKeys;
    private final Board board;

    public Gamer(List<Integer> path, Board board) {
        this.path = path;
        this.board = board;
        this.pathControlledKeys = new ArrayDeque<>();
        fillPathControlledKeys();
    }

    @Override
    public void run() {
        for (int key : pathControlledKeys) {
            try {
                Robot robot = new Robot();
                int pos = path.remove(0);

                robot.keyPress(key);
                robot.keyRelease(key);
                while (board.getPacmanPos() != pos) {}
            } catch (AWTException e) {
                e.printStackTrace();
            }
        }
    }

    private void fillPathControlledKeys(){
        int prev = path.remove(0);
        for(int step : path){
            pathControlledKeys.add(getDirection(prev, step));
            prev = step;
        }
    }

    private int getDirection(int prevPos, int nextPos){
        int delta = nextPos - prevPos;
        switch (delta){
            case 1:
                return RIGHT_ARROW;
            case -1:
                return LEFT_ARROW;
            case -15:
                return UP_ARROW;
            case 15:
                return DOWN_ARROW;
            default:
                return 0;
        }
    }

    private static final int LEFT_ARROW = 37;
    private static final int UP_ARROW = 38;
    private static final int RIGHT_ARROW = 39;
    private static final int DOWN_ARROW = 40;
}
