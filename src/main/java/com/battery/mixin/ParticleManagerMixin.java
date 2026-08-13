package com.battery.mixin;

import com.battery.Battery;
import com.battery.render.ParticleOptimizer;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = ParticleManager.class, priority = 500)
public class ParticleManagerMixin {

    @Inject(method = "tickParticle", at = @At("HEAD"), cancellable = true, require = 0)
    private void battery$onTickParticle(Particle particle, CallbackInfo ci) {
        if (particle == null) return;

        if (Battery.getInstance() != null && Battery.getInstance().getPowerManager() != null) {
            ParticleOptimizer optimizer = Battery.getInstance().getPowerManager().getParticleOptimizer();
            if (optimizer != null) {
                try {
                    double x = particle.getBoundingBox() != null ? particle.getBoundingBox().getCenter().x : 0;
                    double y = particle.getBoundingBox() != null ? particle.getBoundingBox().getCenter().y : 0;
                    double z = particle.getBoundingBox() != null ? particle.getBoundingBox().getCenter().z : 0;

                    if (optimizer.shouldSkipParticleTick(particle, x, y, z)) {
                        ci.cancel();
                    }
                } catch (Throwable ignored) {
                }
            }
        }
    }
}
