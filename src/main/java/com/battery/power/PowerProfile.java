package com.battery.power;

public enum PowerProfile {
    BATTERY_SAVER(32.0, 16.0, 4, 3, true),
    BALANCED(64.0, 32.0, 2, 2, true),
    PERFORMANCE(128.0, 64.0, 1, 1, false);

    private final double maxParticleDistance;
    private final double cosmeticCullingDistance;
    private final int particleTickStride;
    private final int visualUpdateStride;
    private final boolean aggressiveRedundantWorkCulling;

    PowerProfile(double maxParticleDistance, double cosmeticCullingDistance, int particleTickStride, int visualUpdateStride, boolean aggressiveRedundantWorkCulling) {
        this.maxParticleDistance = maxParticleDistance;
        this.cosmeticCullingDistance = cosmeticCullingDistance;
        this.particleTickStride = particleTickStride;
        this.visualUpdateStride = visualUpdateStride;
        this.aggressiveRedundantWorkCulling = aggressiveRedundantWorkCulling;
    }

    public double getMaxParticleDistance() {
        return maxParticleDistance;
    }

    public double getCosmeticCullingDistance() {
        return cosmeticCullingDistance;
    }

    public int getParticleTickStride() {
        return particleTickStride;
    }

    public int getVisualUpdateStride() {
        return visualUpdateStride;
    }

    public boolean isAggressiveRedundantWorkCulling() {
        return aggressiveRedundantWorkCulling;
    }
}
