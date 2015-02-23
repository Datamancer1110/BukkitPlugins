/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tofurkishrobocracy.rightclickgivesitem;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

/**
 * Handle events for all Player related events
 *
 * @author Dinnerbone
 */
public class PlayerListener implements Listener {

    private RightClickGivesItemPlugin plugin;
    private Map<UUID, Date> cooldowns = new HashMap<UUID, Date>();

    public PlayerListener(RightClickGivesItemPlugin p) {
        plugin = p;
    }

    private PlayerListener() {
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        if (event.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
            if (event.getClickedBlock().getType() == plugin.block) {
                Date bannedTill = cooldowns.get(player.getUniqueId());
                long timeleft = (bannedTill == null ? -1 : bannedTill.getTime() - new Date().getTime());
                if (timeleft > 1) {
                    String unit = "seconds";
                    long measure = timeleft / 1000;
                    if (measure > 60) {
                        unit = "minutes";
                        measure = measure / 60;
                    }
                    if (measure > 60) {
                        unit = "hours";
                        measure = measure / 60;
                    }
                    if (measure > 24) {
                        unit = "days";
                        measure = measure / 24;
                    }
                    player.sendMessage("You cannot collect for another " + measure + " " + unit);
                    event.setCancelled(true);
                    return;
                } else {
                    PlayerInventory pi = player.getInventory();
                    pi.addItem(new ItemStack(plugin.drop, 1));
                    Integer delay = plugin.persistence.getDelayForPlayer(player);
                    if (delay == null) {
                        delay = plugin.delayInSeconds;
                    }
                    bannedTill = new Date(new Date().getTime() + (delay * 1000));
                    cooldowns.put(player.getUniqueId(), bannedTill);
                    player.updateInventory();
                    if (plugin.destroy) {
                        event.getClickedBlock().setType(Material.AIR);
                    }
                    event.setCancelled(plugin.cancel);
                    plugin.getServer().getScheduler().runTaskAsynchronously(plugin, new Runnable() {
                        public void run() {
                            removeFinishedCooldowns();
                        }
                    });
                }
            }
        }
    }

    public final void removeFinishedCooldowns() {
        Set<UUID> keys = cooldowns.keySet();
        for (UUID key : keys) {
            Date d = cooldowns.get(key);
            if (new Date().getTime() > d.getTime()) {
                cooldowns.remove(key);
            }
        }
    }
}
