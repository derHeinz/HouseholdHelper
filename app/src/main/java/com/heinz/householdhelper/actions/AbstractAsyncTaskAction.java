package com.heinz.householdhelper.actions;

/**
 * Created by Heinz on 21.10.2016.
 */

public abstract class AbstractAsyncTaskAction<O extends Object> extends AbstractAction {

    protected Finisher<O> finisher;

    protected O waitForFinisher() {
        while (!finisher.isFinished()) {
            try {
                Thread.sleep(300);
            } catch (InterruptedException e) {
                // TODO
                e.printStackTrace();
            }
        }
        return finisher.getResult();
    }
}
