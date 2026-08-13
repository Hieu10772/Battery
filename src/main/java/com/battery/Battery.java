package com.battery;

import com.battery.client.MobileEnvironmentDetector;
import com.battery.command.BatteryCommand;
import com.battery.config.BatteryConfig;
import com.battery.power.PowerManager;
import com.battery.thermal.ThermalMonitor;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.loader.api.FabricLoader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Battery implements ClientModInitializer {
    public static final String MOD_ID = "battery";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    private static Battery instance;
    private BatteryConfig config;
    private PowerManager powerManager;
    private ThermalMonitor thermalMonitor;
    private MobileEnvironmentDetector environmentDetector;

    @Override
    public void onInitializeClient() {
        instance = this;
        LOGGER.info("[Battery] Initializing Mobile Power & Thermal Engine for Minecraft 1.21.11...");

        this.config = BatteryConfig.load();
        this.environmentDetector = new MobileEnvironmentDetector();
        this.thermalMonitor = new ThermalMonitor();
        this.powerManager = new PowerManager(config, thermalMonitor, environmentDetector);

        if (FabricLoader.getInstance().isModLoaded("distanthorizons")) {
            LOGGER.warn("[Battery] Distant Horizons detected! Adjusting aggressive render throttling to prevent native crashes.");
        }

        this.environmentDetector.logEnvironmentDetails();

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            if (client.world != null && !client.isPaused()) {
                this.thermalMonitor.tick();
                this.powerManager.tick(client);
            }
        });

        ClientCommandRegistrationCallback.EVENT.register((dispatcher, registryAccess) -> 
            BatteryCommand.register(dispatcher, powerManager, thermalMonitor)
        );

        LOGGER.info("[Battery] Engine initialized successfully under profile: {}", config.powerProfile);
    }

    public static Battery getInstance() {
        return instance;
    }

    public BatteryConfig getConfig() {
        return config;
    }

    public PowerManager getPowerManager() {
        return powerManager;
    }

    public ThermalMonitor getThermalMonitor() {
        return thermalMonitor;
    }

    public MobileEnvironmentDetector getEnvironmentDetector() {
        return environmentDetector;
    }
}
