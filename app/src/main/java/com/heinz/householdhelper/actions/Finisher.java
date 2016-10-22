package com.heinz.householdhelper.actions;

/**
 * Created by Heinz on 21.10.2016.
 */

public class Finisher<O extends Object> {

    private boolean finished;

    private O result;

    public void finished(O res) {
        result = res;
        finished = true;
    }

    public boolean isFinished() {
        return finished;
    }

    public O getResult() {
        return result;
    }
}
