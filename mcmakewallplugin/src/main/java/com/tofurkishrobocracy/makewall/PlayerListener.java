/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tofurkishrobocracy.makewall;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;

/**
 * Handle events for all Player related events
 *
 * @author Dinnerbone
 */
public class PlayerListener implements Listener {

    private MakeWallPlugin plugin;

    public PlayerListener(MakeWallPlugin p) {
        plugin = p;
    }

    private PlayerListener() {
    }

    @EventHandler
    public void onBlockBreak(BlockPlaceEvent event) {
        Player p = (Player) event.getPlayer();
        if (!plugin.makingWall(p)) {
            return;
        } else {
            DimensionSet ds = plugin.getDimensionSet(p);
            if (ds == null) {
                return;
            }
            if (ds.a == null) {
                ds.a = event.getBlock().getLocation();
                ds.type = event.getBlock().getType();
                plugin.getLogger().info("a=(" + ds.a.getBlockX() + ", " + ds.a.getBlockY() + ", " + ds.a.getBlockZ() + ")");
                plugin.updateDimensionSet(p, ds);
                event.getBlock().setType(Material.GLASS);
                p.sendMessage("First corner set. Place another block of the same type to select the second (and final) corner");
                event.setCancelled(true);
            } else if (ds.b == null) {
                if (event.getBlock().getType() != ds.type) {
                    return;
                }
                ds.b = event.getBlock().getLocation();
                plugin.getLogger().info("b=(" + ds.b.getBlockX() + ", " + ds.b.getBlockY() + ", " + ds.b.getBlockZ() + ")");
                plugin.updateDimensionSet(p, ds);
                event.getBlock().setType(Material.GLASS);
                plugin.drawDimensions(p);
                p.sendMessage("Second corner set. Drawing. Do '/makewall height <height>' to make the wall to a certain height");
                p.sendMessage("i.e. '/makewall height 10' Will make a rectangular wall from corner 1 to corner 2, 10 blocks high");
                event.setCancelled(true);
            } else {
                return;
            }
        }
    }
}
