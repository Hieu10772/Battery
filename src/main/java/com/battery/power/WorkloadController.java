package com.battery.power;

import com.battery.thermal.ThermalResponse;
import com.battery.thermal.ThermalState;
import net.minecraft.client.MinecraftClient;

public class WorkloadController {
    private double effectiveParticleMaxDistanceSq = 1024.0;
    private double effectiveCosmeticDistanceSq = 256.0;
    private int particleStride = 1;
    private int visualStride = 1;
    private int scheduledBatchSize = 10;
    private boolean isPlayerStationary = false;

    public void updateState(PowerProfile profile, ThermalResponse thermalResponse, MinecraftClient client) {
        double distMultiplier = thermalResponse.distanceMultiplier();
        int strideModifier = thermalResponse.strideAdditive();

        double baseMaxDist = profile.getMaxParticleDistance() * distMultiplier;
        this.effectiveParticleMaxDistanceSq = baseMaxDist * baseMaxDist;

        double baseCosmeticDist = profile.getCosmeticCullingDistance() * distMultiplier;
        this.effectiveCosmeticDistanceSq = baseCosmeticDist * baseCosmeticDist;

        this.particleStride = Math.max(1, profile.getParticleTickStride() + strideModifier);
        this.visualStride = Math.max(1, profile.getVisualUpdateStride() + strideModifier);

        if (thermalResponse.state() == ThermalState.CRITICAL) {
            this.scheduledBatchSize = 2;
        } else if (thermalResponse.state() == ThermalState.HOT) {
            this.scheduledBatchSize = 5;
        } else {
            this.scheduledBatchSize = 15;
        }

        if (client.player != null) {
            this.isPlayerStationary = client.player.getVelocity().lengthSquared() < 0.001 
                    && client.player.lastRenderYaw == client.player.getYaw() 
                    && client.player.lastRenderPitch == client.player.getPitch();
        } else {
            this.isPlayerStationary = false;
        }
    }

    public double getEffectiveParticleMaxDistanceSq() {
        return effectiveParticleMaxDistanceSq;
    }

    public double getEffectiveCosmeticDistanceSq() {
        return effectiveCosmeticDistanceSq;
    }

    public int getParticleStride() {
        return particleStride;
    }

    public int getVisualStride() {
        return visualStride;
    }

    public int getScheduledBatchSize() {
        return scheduledBatchSize;
    }

    public boolean isPlayerStationary() {
        return isPlayerStationary;
    }
}
