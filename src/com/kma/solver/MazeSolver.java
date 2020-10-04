package com.kma.solver;

import java.util.List;

public abstract class MazeSolver {

    protected int[] map;

    protected int pacmanPosition;

    //.... function of finding the next step

    protected int candyPosition;

    protected int cost;

    public abstract List<Integer> solve();

    public int getCost() {
        return cost;
    }
}
