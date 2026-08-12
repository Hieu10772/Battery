package com.battery.command;

import com.battery.power.PowerManager;
import com.battery.power.PowerProfile;
import com.battery.thermal.ThermalMonitor;
import com.mojang.brigadier.CommandDispatcher;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import net.minecraft.text.Text;

public class BatteryCommand {
    public static void register(CommandDispatcher<FabricClientCommandSource> dispatcher, PowerManager powerManager, ThermalMonitor thermalMonitor) {
        dispatcher.register(ClientCommandManager.literal("battery")
            .then(ClientCommandManager.literal("profile")
                .then(ClientCommandManager.literal("saver").executes(ctx -> {
                    powerManager.setProfile(PowerProfile.BATTERY_SAVER);
                    ctx.getSource().sendFeedback(Text.literal("§a[Battery] Power profile set to: BATTERY_SAVER"));
                    return 1;
                }))
                .then(ClientCommandManager.literal("balanced").executes(ctx -> {
                    powerManager.setProfile(PowerProfile.BALANCED);
                    ctx.getSource().sendFeedback(Text.literal("§a[Battery] Power profile set to: BALANCED"));
                    return 1;
                }))
                .then(ClientCommandManager.literal("performance").executes(ctx -> {
                    powerManager.setProfile(PowerProfile.PERFORMANCE);
                    ctx.getSource().sendFeedback(Text.literal("§a[Battery] Power profile set to: PERFORMANCE"));
                    return 1;
                }))
            )
            .then(ClientCommandManager.literal("status").executes(ctx -> {
                ctx.getSource().sendFeedback(Text.literal("§e=== BATTERY ENGINE STATUS ==="));
                ctx.getSource().sendFeedback(Text.literal("§fProfile: §a" + powerManager.getProfile().name()));
                ctx.getSource().sendFeedback(Text.literal("§fThermal State: §c" + thermalMonitor.getCurrentState().name()));
                ctx.getSource().sendFeedback(Text.literal("§fFar Particle Opt: §a" + powerManager.getConfig().reduceFarParticles));
                ctx.getSource().sendFeedback(Text.literal("§fCosmetic Opt: §a" + powerManager.getConfig().reduceCosmeticUpdates));
                ctx.getSource().sendFeedback(Text.literal("§fRedundant Work Reduction: §a" + powerManager.getConfig().reduceRedundantRenderWork));
                ctx.getSource().sendFeedback(Text.literal("§fMobile Environment: §a" + powerManager.getEnvironmentDetector().isMobile()));
                return 1;
            }))
        );
    }
}
