package com.kma.solver;

import com.zetcode.Board;

import javax.swing.*;
import java.awt.*;
import java.time.Duration;
import java.time.Instant;
import java.util.List;

public abstract class MazeSolver {

    protected Board map;

    protected int pacmanPosition;

    protected int count;

    protected static final long ANIMATION_PAUSE_MILLIS = 50;
    protected long ANIMATION_SLEEP_COUNTER = 0;

    public List<Integer> solve(){
        Instant start = Instant.now();
        List<Integer> res = solve(pacmanPosition);
        Instant finish = Instant.now();

        //counting time
        Duration between = Duration.between(start, finish).minusMillis(ANIMATION_PAUSE_MILLIS * ANIMATION_SLEEP_COUNTER);
        int seconds = between.toSecondsPart();
        int millis = between.toMillisPart();
        JOptionPane.showMessageDialog(new Frame(), this.toString() + "\nTotal steps: " + this.getCount() +
                "\n Elapsed time without animations: " + seconds + "s, " + millis + "ms");
        return res;

    }

    //Use this method for on each algorithm animation iteration in order for human to see how it works
    protected void sleep(){
        try {
            Thread.sleep(ANIMATION_PAUSE_MILLIS);
            ANIMATION_SLEEP_COUNTER++;
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    protected abstract List<Integer> solve(int initPosition);

    public int getCount() {
        return count;
    }

}
