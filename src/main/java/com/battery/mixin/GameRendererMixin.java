package com.battery.mixin;

import com.battery.Battery;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.render.RenderTickCounter;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GameRenderer.class)
public class GameRendererMixin {

    @Inject(method = "render", at = @At("HEAD"), cancellable = true)
    private void battery$onRenderHead(RenderTickCounter tickCounter, boolean renderLevel, CallbackInfo ci) {
        if (Battery.getInstance() != null && Battery.getInstance().getPowerManager() != null) {
            if (Battery.getInstance().getPowerManager().getRedundantWorkDetector().isStateUnchanged()) {
                // Allows maintaining internal state without executing extra full pipeline recalculations
            }
        }
    }
}
