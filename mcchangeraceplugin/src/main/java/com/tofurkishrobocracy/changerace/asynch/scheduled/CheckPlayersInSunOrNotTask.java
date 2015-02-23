package com.tofurkishrobocracy.changerace.asynch.scheduled;

import com.tofurkishrobocracy.changerace.ChangeRacePlugin;
import org.bukkit.entity.Player;

public class CheckPlayersInSunOrNotTask implements Runnable {

    private final ChangeRacePlugin _plugin;

    private CheckPlayersInSunOrNotTask() {
        _plugin = null;
    }

    public CheckPlayersInSunOrNotTask(ChangeRacePlugin plugin) {
        _plugin = plugin;
    }

    @Override
    public void run() {
        Player[] players = _plugin.getServer().getOnlinePlayers();
        for (Player player : players) {
            if (player.getWorld().getBlockAt(player.getLocation()).getLightFromSky() >= 13
                    && player.getWorld().getTime() >= 0
                    && player.getWorld().getTime() <= 12500) {
                _plugin.getRace(player).onPlayerIsInSun(player);
            } else {
                _plugin.getRace(player).onPlayerIsNotInSun(player);
            }
        }
    }

}
