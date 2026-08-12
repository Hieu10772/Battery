package com.battery.thermal;

public class ThermalMonitor {
    private static final int WINDOW_SIZE = 120;
    private final long[] frameTimesNs = new long[WINDOW_SIZE];
    private int windowIndex = 0;
    private boolean windowFull = false;

    private ThermalState currentState = ThermalState.NORMAL;
    private ThermalResponse currentResponse = ThermalResponse.NORMAL_RESPONSE;

    private long lastFrameTimeTimestamp = System.nanoTime();

    public void recordFrameTime(long frameDurationNs) {
        frameTimesNs[windowIndex] = frameDurationNs;
        windowIndex = (windowIndex + 1) % WINDOW_SIZE;
        if (windowIndex == 0) {
            windowFull = true;
        }
    }

    public void tick() {
        if (!windowFull && windowIndex < 30) {
            return;
        }

        int count = windowFull ? WINDOW_SIZE : windowIndex;
        double sumMs = 0;
        for (int i = 0; i < count; i++) {
            sumMs += frameTimesNs[i] / 1_000_000.0;
        }
        double avgMs = sumMs / count;

        double varianceSum = 0;
        int spikeCount = 0;
        for (int i = 0; i < count; i++) {
            double ms = frameTimesNs[i] / 1_000_000.0;
            double diff = ms - avgMs;
            varianceSum += diff * diff;
            if (ms > avgMs * 2.2 && ms > 35.0) {
                spikeCount++;
            }
        }
        double stdDevMs = Math.sqrt(varianceSum / count);

        evaluateThermalState(avgMs, stdDevMs, spikeCount);
    }

    private void evaluateThermalState(double avgMs, double stdDevMs, int spikeCount) {
        if (stdDevMs > 18.0 || spikeCount >= 12 || avgMs > 45.0) {
            currentState = ThermalState.CRITICAL;
            currentResponse = ThermalResponse.CRITICAL_RESPONSE;
        } else if (stdDevMs > 10.0 || spikeCount >= 6 || avgMs > 28.0) {
            currentState = ThermalState.HOT;
            currentResponse = ThermalResponse.HOT_RESPONSE;
        } else if (stdDevMs > 5.0 || spikeCount >= 2 || avgMs > 20.0) {
            currentState = ThermalState.WARM;
            currentResponse = ThermalResponse.WARM_RESPONSE;
        } else {
            currentState = ThermalState.NORMAL;
            currentResponse = ThermalResponse.NORMAL_RESPONSE;
        }
    }

    public ThermalState getCurrentState() {
        return currentState;
    }

    public ThermalResponse getCurrentResponse() {
        return currentResponse;
    }
}
