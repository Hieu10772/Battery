package com.battery.config;

import com.battery.power.PowerProfile;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.fabricmc.loader.api.FabricLoader;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class BatteryConfig {
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static final File CONFIG_FILE = FabricLoader.getInstance().getConfigDir().resolve("battery.json").toFile();

    public PowerProfile powerProfile = PowerProfile.BALANCED;
    public boolean reduceFarParticles = true;
    public boolean reduceCosmeticUpdates = true;
    public boolean reduceRedundantRenderWork = true;
    public boolean deferNonCriticalWork = true;
    public boolean thermalProtection = true;
    public boolean mobileOptimizations = true;

    public static BatteryConfig load() {
        if (CONFIG_FILE.exists()) {
            try (FileReader reader = new FileReader(CONFIG_FILE)) {
                BatteryConfig loaded = GSON.fromJson(reader, BatteryConfig.class);
                if (loaded != null) {
                    return loaded;
                }
            } catch (IOException e) {
                System.err.println("[Battery] Failed to load config, fallback to default: " + e.getMessage());
            }
        }
        BatteryConfig defaultConfig = new BatteryConfig();
        defaultConfig.save();
        return defaultConfig;
    }

    public void save() {
        try (FileWriter writer = new FileWriter(CONFIG_FILE)) {
            GSON.toJson(this, writer);
        } catch (IOException e) {
            System.err.println("[Battery] Failed to save config: " + e.getMessage());
        }
    }
}
