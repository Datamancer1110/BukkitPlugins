/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tofurkishrobocracy.changerace.asynch.scheduled;

import com.tofurkishrobocracy.changerace.ChangeRacePlugin;
import org.bukkit.Material;
import org.bukkit.entity.Player;

/**
 *
 * @author Cody
 */
public class CheckPlayersInWaterOrNotTask implements Runnable {

    private final ChangeRacePlugin _plugin;

    private CheckPlayersInWaterOrNotTask() {
        _plugin = null;
    }

    public CheckPlayersInWaterOrNotTask(ChangeRacePlugin plugin) {
        _plugin = plugin;
    }

    @Override
    public void run() {
        Player[] players = _plugin.getServer().getOnlinePlayers();
        for (Player player : players) {
            Material m = player.getLocation().getBlock().getType();
            if ((m == Material.STATIONARY_WATER || m == Material.WATER)) {
                _plugin.getRace(player).onPlayerIsInWater(player);
            } else {
                _plugin.getRace(player).onPlayerIsNotInWater(player);
            }
        }
    }

}
