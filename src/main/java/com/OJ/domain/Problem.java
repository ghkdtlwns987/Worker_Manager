package com.OJ.domain;

public class Problem {

    private int id;
    private int timeLimit;
    private int memoryLimit;
    private boolean spaceCheck;

    public Problem(int id, int timeLimit, int memoryLimit, boolean spaceCheck) {
        this.id = id;
        this.timeLimit = timeLimit;
        this.memoryLimit = memoryLimit;
        this.spaceCheck = spaceCheck;
    }

    public int getId() {
        return id;
    }

    public int getTimeLimit() {
        return timeLimit;
    }

    public int getMemoryLimit() {
        return memoryLimit;
    }

    public boolean isSpaceCheck() {
        return spaceCheck;
    }
}
