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
public class CheckPlayersInLavaOrNotTask implements Runnable {

    private final ChangeRacePlugin _plugin;

    private CheckPlayersInLavaOrNotTask() {
        _plugin = null;
    }

    public CheckPlayersInLavaOrNotTask(ChangeRacePlugin plugin) {
        _plugin = plugin;
    }

    @Override
    public void run() {
        Player[] players = _plugin.getServer().getOnlinePlayers();
        for (Player player : players) {
            Material m = player.getLocation().getBlock().getType();
            if ((m == Material.STATIONARY_LAVA || m == Material.LAVA)) {
                _plugin.getRace(player).onPlayerIsInLava(player);
            } else {
                _plugin.getRace(player).onPlayerIsNotInLava(player);
            }
        }
    }

}
