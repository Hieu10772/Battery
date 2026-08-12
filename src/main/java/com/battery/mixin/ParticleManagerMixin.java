package com.battery.mixin;

import com.battery.Battery;
import com.battery.render.ParticleOptimizer;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ParticleManager.class)
public class ParticleManagerMixin {

    @Inject(method = "tickParticle", at = @At("HEAD"), cancellable = true)
    private void battery$onTickParticle(Particle particle, CallbackInfo ci) {
        if (particle == null) return;

        if (Battery.getInstance() != null && Battery.getInstance().getPowerManager() != null) {
            ParticleOptimizer optimizer = Battery.getInstance().getPowerManager().getParticleOptimizer();
            if (optimizer != null) {
                // Accessing particle coordinates directly
                double x = particle.getBoundingBox().getCenter().x;
                double y = particle.getBoundingBox().getCenter().y;
                double z = particle.getBoundingBox().getCenter().z;

                if (optimizer.shouldSkipParticleTick(particle, x, y, z)) {
                    ci.cancel();
                }
            }
        }
    }
}
