package com.solvd.navigator.util;

public class DriverManager {
    private ConnectionPool pool;

    public ThreadManager(ConnectionPool pool) {
        this.pool = pool;
    }

    public void startThreads(int numberOfThreads) {
        for (int i = 0; i < numberOfThreads; i++) {
            Runnable task = new ThreadRunnable(pool);
            Thread thread = new Thread(task, "Driver " + (i + 1));
            thread.start();
        }
    }
}
