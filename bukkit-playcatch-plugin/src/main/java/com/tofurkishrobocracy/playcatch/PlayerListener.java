/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tofurkishrobocracy.playcatch;

import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;

/**
 * Handle events for all Player related events
 *
 * @author Dinnerbone
 */
public class PlayerListener implements Listener {

    private PlayCatchPlugin plugin;

    public PlayerListener(PlayCatchPlugin p) {
        plugin = p;
    }

    private PlayerListener() {
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerCatch(EntityDamageByEntityEvent event) {
        if (event.getDamager() instanceof Player && event.getEntity() instanceof Player) {
            if (event.getEntityType() == EntityType.SNOWBALL) {
                Player p = (Player) event.getEntity();
                p.getInventory().addItem(new ItemStack(Material.SNOW_BALL, 1));
                p.updateInventory();
                p.sendMessage("You caught a snowball from " + ((Player) event.getDamager()).getDisplayName());
            } else if (event.getEntityType() == EntityType.EGG) {
                Player p = (Player) event.getEntity();
                p.getInventory().addItem(new ItemStack(Material.EGG, 1));
                p.updateInventory();
                p.sendMessage("You caught an egg from " + ((Player) event.getDamager()).getDisplayName());
            }
        }
    }
}
