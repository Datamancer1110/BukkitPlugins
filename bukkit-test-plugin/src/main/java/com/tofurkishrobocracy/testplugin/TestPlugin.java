/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tofurkishrobocracy.testplugin;

import java.util.logging.Level;
import org.bukkit.plugin.java.JavaPlugin;

/**
 *
 * @author Cody
 */
public class TestPlugin extends JavaPlugin {

    public static TestPlugin instance;

    @Override
    public void onEnable() {
        instance = this;
        getConfig().options().copyDefaults(true);
        saveConfig();
        try {
            validateConfigYml();
        } catch (Exception ex) {
            throw new RuntimeException("Failed to Enable BanFromWorlds", ex);
        }
        getServer().getPluginManager().registerEvents(new PlayerListener(this), this);
    }

    private void validateConfigYml() throws Exception {
        String[] worldsToTeleportFrom = null;
        String worldToTeleportTo = this.getConfig().getString("world_to_teleport_to_in_event_of_death");
        long banLengthInSeconds = this.getConfig().getLong("length_of_time_in_seconds_to_ban_dead_players_from_worlds");
        String worldsToTeleportFromStr = this.getConfig().getString("worlds_in_which_death_causes_teleport_csv");
        if (worldsToTeleportFromStr != null && !worldsToTeleportFromStr.isEmpty()) {
            worldsToTeleportFrom = worldsToTeleportFromStr.split(",");
        }
        if (worldsToTeleportFrom == null || worldsToTeleportFrom.length == 0) {
            this.getLogger().log(Level.SEVERE, "'worlds_in_which_death_causes_teleport_csv' could not be parsed; should be of the form: world1,world2,worldn");
            throw new Exception("'worlds_in_which_death_causes_teleport_csv' could not be parsed; should be of the form: world1,world2,worldn");
        }
        if (worldToTeleportTo == null || worldToTeleportTo.isEmpty()) {
            this.getLogger().log(Level.SEVERE, "'world_to_teleport_to_in_event_of_death' could not be parsed; is null or blank");
            throw new Exception("'world_to_teleport_to_in_event_of_death' could not be parsed; is null or blank");
        }
        if (banLengthInSeconds <= 0) {
            this.getLogger().log(Level.SEVERE, "'length_of_time_in_seconds_to_ban_dead_players_from_worlds' needs to be a positive number");
        }
        if (this.getServer().getWorld(worldToTeleportTo) == null) {
            this.getLogger().log(Level.SEVERE, "'world_to_teleport_to_in_event_of_death' world named '" + worldToTeleportTo + "' does not exist!");
            throw new Exception("'world_to_teleport_to_in_event_of_death' world named '" + worldToTeleportTo + "' does not exist!");
        }
        for (String world : worldsToTeleportFrom) {
            if (this.getServer().getWorld(world) == null) {
                this.getLogger().log(Level.SEVERE, "'worlds_in_which_death_causes_teleport_csv' world named '" + world + "' does not exist!");
                throw new Exception("'worlds_in_which_death_causes_teleport_csv' world named '" + world + "' does not exist!");
            }
        }
    }
}
