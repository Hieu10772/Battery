package com.battery.thermal;

public record ThermalResponse(
        ThermalState state,
        double distanceMultiplier,
        int strideAdditive,
        boolean pauseDistantAnimations
) {
    public static final ThermalResponse NORMAL_RESPONSE = new ThermalResponse(ThermalState.NORMAL, 1.0, 0, false);
    public static final ThermalResponse WARM_RESPONSE = new ThermalResponse(ThermalState.WARM, 0.75, 1, false);
    public static final ThermalResponse HOT_RESPONSE = new ThermalResponse(ThermalState.HOT, 0.50, 2, true);
    public static final ThermalResponse CRITICAL_RESPONSE = new ThermalResponse(ThermalState.CRITICAL, 0.30, 3, true);
}
