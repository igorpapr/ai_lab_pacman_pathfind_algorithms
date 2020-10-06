package com.kma.solver;

import com.zetcode.Board;

import java.util.List;

public abstract class MazeSolver {

    protected Board map;

    protected int pacmanPosition;

    //.... function of finding the next step

    protected int cost;

    public abstract List<Integer> solve();

    public int getCost() {
        return cost;
    }
}
