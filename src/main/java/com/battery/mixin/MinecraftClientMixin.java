package com.battery.mixin;

import com.battery.Battery;
import com.battery.thermal.ThermalMonitor;
import net.minecraft.client.MinecraftClient;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MinecraftClient.class)
public class MinecraftClientMixin {

    @Unique
    private long battery$frameStartTime = 0;

    @Inject(method = "render", at = @At("HEAD"))
    private void battery$onRenderStart(boolean tick, CallbackInfo ci) {
        this.battery$frameStartTime = System.nanoTime();
    }

    @Inject(method = "render", at = @At("RETURN"))
    private void battery$onRenderEnd(boolean tick, CallbackInfo ci) {
        long duration = System.nanoTime() - this.battery$frameStartTime;
        if (Battery.getInstance() != null) {
            ThermalMonitor monitor = Battery.getInstance().getThermalMonitor();
            if (monitor != null) {
                monitor.recordFrameTime(duration);
            }
        }
    }
}
