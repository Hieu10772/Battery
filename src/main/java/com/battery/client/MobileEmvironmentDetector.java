package com.battery.client;

import com.battery.Battery;

public class MobileEnvironmentDetector {
    private final boolean isPojavLauncher;
    private final boolean isMobileGLues;
    private final boolean isAndroid;
    private final boolean isIOS;

    public MobileEnvironmentDetector() {
        String osName = System.getProperty("os.name", "").toLowerCase();
        String javaVendor = System.getProperty("java.vendor", "").toLowerCase();
        String pojavVer = System.getProperty("pojav.version", "");

        this.isAndroid = osName.contains("android") || osName.contains("linux") && javaVendor.contains("android");
        this.isIOS = osName.contains("ios") || osName.contains("mac") && System.getProperty("os.arch", "").contains("aarch64");
        this.isPojavLauncher = !pojavVer.isEmpty() || System.getenv("POJAV_RENDERER") != null;
        this.isMobileGLues = System.getenv("MOBILEGLUES_VERSION") != null || System.getProperty("mobileglues.active") != null;
    }

    public void logEnvironmentDetails() {
        Battery.LOGGER.info("[Battery] Environment Detection Summary:");
        Battery.LOGGER.info("  - OS: Android={}, iOS={}", isAndroid, isIOS);
        Battery.LOGGER.info("  - Launcher: PojavLauncher={}, MobileGLues={}", isPojavLauncher, isMobileGLues);
    }

    public boolean isMobile() {
        return isAndroid || isIOS || isPojavLauncher || isMobileGLues;
    }

    public boolean isPojavLauncher() {
        return isPojavLauncher;
    }

    public boolean isMobileGLues() {
        return isMobileGLues;
    }
}
