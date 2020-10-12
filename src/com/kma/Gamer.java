package com.kma;

import java.awt.*;

// will emulate pressing keyboard buttons with supplied key codes list
public class Gamer extends Thread {
    private final int[] path;

    public Gamer(int[] path) {
        this.path = path;
    }

    @Override
    public void run() {
//        super.run();
        try {
            Robot robot = new Robot();
            Thread.sleep(500);

            for (int key : path) {
                robot.keyPress(key);
                robot.keyRelease(key);
                Thread.sleep(100);
            }
        } catch (AWTException | InterruptedException e) {
            e.printStackTrace();
        }
    }

}
