/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tofurkishrobocracy.dighole;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

/**
 * Handle events for all Player related events
 *
 * @author Dinnerbone
 */
public class PlayerListener implements Listener {

    private DigHolePlugin plugin;

    public PlayerListener(DigHolePlugin p) {
        plugin = p;
    }

    private PlayerListener() {
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        Player p = (Player) event.getPlayer();
        if (!plugin.diggingHole(p)) {
            return;
        } else {
            DimensionSet ds = plugin.getDimensionSet(p);
            if (ds == null) {
                return;
            }
            if (ds.a == null) {
                ds.a = event.getBlock().getLocation();
                plugin.getLogger().info("a=(" + ds.a.getBlockX() + ", " + ds.a.getBlockY() + ", " + ds.a.getBlockZ() + ")");
                plugin.updateDimensionSet(p, ds);
                event.getBlock().setType(Material.GLASS);
                p.sendMessage("First corner set. Break another block to select the second (and final) corner");
                event.setCancelled(true);
            } else if (ds.b == null) {
                ds.b = event.getBlock().getLocation();
                plugin.getLogger().info("b=(" + ds.b.getBlockX() + ", " + ds.b.getBlockY() + ", " + ds.b.getBlockZ() + ")");
                plugin.updateDimensionSet(p, ds);
                event.getBlock().setType(Material.GLASS);
                plugin.drawDimensions(p);
                p.sendMessage("Second corner set. Drawing. Do '/dighole depth <depth>' to dig the hole to a certain depth");
                p.sendMessage("i.e. '/dighole depth 10' Will dig a rectangular hole from corner 1 to corner 2, 10 blocks deep");
                event.setCancelled(true);
            } else {
                return;
            }
        }
    }
}
