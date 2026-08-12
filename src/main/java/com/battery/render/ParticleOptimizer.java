package com.battery.render;

import com.battery.power.PowerManager;
import com.battery.power.WorkloadController;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.particle.Particle;

public class ParticleOptimizer {
    private final PowerManager powerManager;
    private long tickCounter = 0;

    public ParticleOptimizer(PowerManager powerManager) {
        this.powerManager = powerManager;
    }

    public boolean shouldSkipParticleTick(Particle particle, double x, double y, double z) {
        if (!powerManager.getConfig().reduceFarParticles) {
            return false;
        }

        MinecraftClient client = MinecraftClient.getInstance();
        if (client.player == null) {
            return false;
        }

        WorkloadController controller = powerManager.getWorkloadController();
        double dx = client.player.getX() - x;
        double dy = client.player.getY() - y;
        double dz = client.player.getZ() - z;
        double distSq = dx * dx + dy * dy + dz * dz;

        if (distSq > controller.getEffectiveParticleMaxDistanceSq()) {
            return true;
        }

        int stride = controller.getParticleStride();
        if (stride > 1 && distSq > controller.getEffectiveCosmeticDistanceSq()) {
            tickCounter++;
            return (tickCounter % stride) != 0;
        }

        return false;
    }
}
