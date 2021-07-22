package thederpgamer.superstructures.manager;

import api.mod.config.FileConfiguration;
import thederpgamer.superstructures.SuperStructures;


/**
 * Manages mod config files and values.
 *
 * @author TheDerpGamer
 * @since 06/07/2021
 */
public class ConfigManager {

    //Main Config
    private static FileConfiguration mainConfig;
    private static final String[] defaultMainConfig = {
            "debug-mode: false",
            "auto-save-frequency: 5000",
            "max-dyson-sphere-station-distance: 7500"
    };

    public static void initialize(SuperStructures instance) {
        mainConfig = instance.getConfig("config");
        mainConfig.saveDefault(defaultMainConfig);
    }

    public static FileConfiguration getMainConfig() {
        return mainConfig;
    }
}