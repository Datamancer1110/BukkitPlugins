/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tofurkishrobocracy.changerace.asynch.scheduled;

import com.tofurkishrobocracy.changerace.ChangeRacePlugin;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

/**
 *
 * @author Cody
 */
public class GiveSnowmenSnowballTask implements Runnable {

    private final ChangeRacePlugin _plugin;

    private GiveSnowmenSnowballTask() {
        _plugin = null;
    }

    public GiveSnowmenSnowballTask(ChangeRacePlugin plugin) {
        _plugin = plugin;
    }

    @Override
    public void run() {
        Player[] players = _plugin.getServer().getOnlinePlayers();
        for (Player player : players) {
            if (_plugin.getRace(player) == ChangeRacePlugin.RACE_SNOWMAN) {
                int snowballCount = 0;
                ItemStack lastItemStack = new ItemStack(Material.SNOW_BALL, 1);
                for (ItemStack is : player.getInventory().getContents()) {
                    if (is.getType() == Material.SNOW_BALL) {
                        snowballCount = snowballCount + is.getAmount();
                        lastItemStack = is;
                    }
                }
                if (snowballCount < 10) {
                    lastItemStack.setAmount(lastItemStack.getAmount() + 1);
                }
            }
        }
    }

}
