package com.battery.render;

import com.battery.power.PowerManager;
import com.battery.power.WorkloadController;
import net.minecraft.client.MinecraftClient;

public class VisualUpdateOptimizer {
    private final PowerManager powerManager;
    private long visualCounter = 0;

    public VisualUpdateOptimizer(PowerManager powerManager) {
        this.powerManager = powerManager;
    }

    public boolean shouldSkipCosmeticAnimation(double blockX, double blockY, double blockZ) {
        if (!powerManager.getConfig().reduceCosmeticUpdates) {
            return false;
        }

        MinecraftClient client = MinecraftClient.getInstance();
        if (client.player == null) {
            return false;
        }

        WorkloadController controller = powerManager.getWorkloadController();
        double dx = client.player.getX() - blockX;
        double dy = client.player.getY() - blockY;
        double dz = client.player.getZ() - blockZ;
        double distSq = dx * dx + dy * dy + dz * dz;

        if (distSq > controller.getEffectiveCosmeticDistanceSq()) {
            visualCounter++;
            return (visualCounter % controller.getVisualStride()) != 0;
        }

        return false;
    }
}
