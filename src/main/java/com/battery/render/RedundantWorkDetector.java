package com.battery.render;

import net.minecraft.client.MinecraftClient;
import net.minecraft.util.math.Vec3d;

public class RedundantWorkDetector {
    private Vec3d lastPlayerPos = Vec3d.ZERO;
    private float lastYaw = 0.0f;
    private float lastPitch = 0.0f;
    private boolean stateUnchanged = false;
    private int unchangedTicks = 0;

    public void tick(MinecraftClient client) {
        if (client.player == null) {
            stateUnchanged = false;
            unchangedTicks = 0;
            return;
        }

        Vec3d currentPos = new Vec3d(client.player.getX(), client.player.getY(), client.player.getZ());
        float currentYaw = client.player.getYaw();
        float currentPitch = client.player.getPitch();

        if (currentPos.squaredDistanceTo(lastPlayerPos) < 0.0001 &&
            Math.abs(currentYaw - lastYaw) < 0.01f &&
            Math.abs(currentPitch - lastPitch) < 0.01f) {
            unchangedTicks++;
            if (unchangedTicks > 5) {
                stateUnchanged = true;
            }
        } else {
            unchangedTicks = 0;
            stateUnchanged = false;
        }

        lastPlayerPos = currentPos;
        lastYaw = currentYaw;
        lastPitch = currentPitch;
    }

    public boolean isStateUnchanged() {
        return stateUnchanged;
    }

    public int getUnchangedTicks() {
        return unchangedTicks;
    }
}
