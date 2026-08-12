package com.battery.render;

import net.minecraft.client.Minecraft;
import net.minecraft.world.phys.Vec3;

public class RedundantWorkDetector {
    private Vec3 lastPlayerPos = Vec3.ZERO;
    private float lastYaw = 0.0f;
    private float lastPitch = 0.0f;
    private boolean stateUnchanged = false;
    private int unchangedTicks = 0;

    public void tick(Minecraft client) {
        if (client.player == null) {
            stateUnchanged = false;
            unchangedTicks = 0;
            return;
        }

        Vec3 currentPos = client.player.position();
        float currentYaw = client.player.getYRot();
        float currentPitch = client.player.getXRot();

        if (currentPos.distanceToSqr(lastPlayerPos) < 0.0001 &&
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
