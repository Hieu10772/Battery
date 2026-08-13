package com.battery.mixin;

import com.battery.Battery;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.render.RenderTickCounter;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = GameRenderer.class, priority = 500)
public class GameRendererMixin {

    @Inject(method = "render", at = @At("RETURN"), require = 0)
    private void battery$onRenderTail(RenderTickCounter tickCounter, boolean renderLevel, CallbackInfo ci) {
        if (Battery.getInstance() != null && Battery.getInstance().getPowerManager() != null) {
            if (Battery.getInstance().getPowerManager().getRedundantWorkDetector().isStateUnchanged()) {
            }
        }
    }
}
