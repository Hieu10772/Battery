package com.battery.render;

import java.util.ArrayDeque;
import java.util.Deque;

public class RenderWorkScheduler {
    private final Deque<Runnable> nonCriticalQueue = new ArrayDeque<>();

    public void schedule(Runnable task) {
        if (nonCriticalQueue.size() < 200) {
            nonCriticalQueue.addLast(task);
        }
    }

    public void executeBatch(int maxTasks) {
        int executed = 0;
        while (!nonCriticalQueue.isEmpty() && executed < maxTasks) {
            Runnable task = nonCriticalQueue.pollFirst();
            if (task != null) {
                try {
                    task.run();
                } catch (Exception e) {
                    System.err.println("[Battery] Error executing scheduled render task: " + e.getMessage());
                }
            }
            executed++;
        }
    }

    public void clear() {
        nonCriticalQueue.clear();
    }
}
