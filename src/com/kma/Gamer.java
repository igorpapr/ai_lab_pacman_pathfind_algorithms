package com.kma;

import java.awt.*;
import java.util.ArrayDeque;
import java.util.List;
import java.util.Queue;

// will emulate pressing keyboard buttons with supplied key codes list
public class Gamer extends Thread {
    private final List<Integer> path;
    private final Queue<Integer> pathControlledKeys;

    public Gamer(List<Integer> path) {
        this.path = path;
        this.pathControlledKeys = new ArrayDeque<>();
        fillPathControlledKeys();
    }

    @Override
    public void run() {
        try {
            Robot robot = new Robot();
            Thread.sleep(500);

            for (int key : pathControlledKeys) {
                robot.keyPress(key);
                robot.keyRelease(key);
                Thread.sleep(123);
            }
        } catch (AWTException | InterruptedException e) {
            e.printStackTrace();
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
