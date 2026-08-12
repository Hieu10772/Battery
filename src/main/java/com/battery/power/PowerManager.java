package com.battery.power;

import com.battery.client.MobileEnvironmentDetector;
import com.battery.config.BatteryConfig;
import com.battery.render.ParticleOptimizer;
import com.battery.render.RedundantWorkDetector;
import com.battery.render.RenderWorkScheduler;
import com.battery.render.VisualUpdateOptimizer;
import com.battery.thermal.ThermalMonitor;
import com.battery.thermal.ThermalResponse;
import net.minecraft.client.MinecraftClient;

public class PowerManager {
    private final BatteryConfig config;
    private final ThermalMonitor thermalMonitor;
    private final MobileEnvironmentDetector environmentDetector;

    private final WorkloadController workloadController;
    private final ParticleOptimizer particleOptimizer;
    private final VisualUpdateOptimizer visualUpdateOptimizer;
    private final RenderWorkScheduler renderWorkScheduler;
    private final RedundantWorkDetector redundantWorkDetector;

    public PowerManager(BatteryConfig config, ThermalMonitor thermalMonitor, MobileEnvironmentDetector environmentDetector) {
        this.config = config;
        this.thermalMonitor = thermalMonitor;
        this.environmentDetector = environmentDetector;

        this.workloadController = new WorkloadController();
        this.particleOptimizer = new ParticleOptimizer(this);
        this.visualUpdateOptimizer = new VisualUpdateOptimizer(this);
        this.renderWorkScheduler = new RenderWorkScheduler();
        this.redundantWorkDetector = new RedundantWorkDetector();
    }

    public void tick(MinecraftClient client) {
        ThermalResponse thermalResponse = thermalMonitor.getCurrentResponse();
        workloadController.updateState(config.powerProfile, thermalResponse, client);
        redundantWorkDetector.tick(client);
        renderWorkScheduler.executeBatch(workloadController.getScheduledBatchSize());
    }

    public void setProfile(PowerProfile profile) {
        this.config.powerProfile = profile;
        this.config.save();
    }

    public PowerProfile getProfile() {
        return config.powerProfile;
    }

    public BatteryConfig getConfig() {
        return config;
    }

    public WorkloadController getWorkloadController() {
        return workloadController;
    }

    public ParticleOptimizer getParticleOptimizer() {
        return particleOptimizer;
    }

    public VisualUpdateOptimizer getVisualUpdateOptimizer() {
        return visualUpdateOptimizer;
    }

    public RenderWorkScheduler getRenderWorkScheduler() {
        return renderWorkScheduler;
    }

    public RedundantWorkDetector getRedundantWorkDetector() {
        return redundantWorkDetector;
    }

    public MobileEnvironmentDetector getEnvironmentDetector() {
        return environmentDetector;
    }
}
