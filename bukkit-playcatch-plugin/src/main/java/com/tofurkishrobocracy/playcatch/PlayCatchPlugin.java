/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tofurkishrobocracy.playcatch;

import org.bukkit.plugin.java.JavaPlugin;

/**
 *
 * @author Cody
 */
public class PlayCatchPlugin extends JavaPlugin {

    public static PlayCatchPlugin instance;
    public boolean enabled = true;

    @Override
    public void onEnable() {
        instance = this;
        getCommand("playcatch").setExecutor(new CommandExecutor(this));
        getServer().getPluginManager().registerEvents(new PlayerListener(this), this);
    }
}
