/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tofurkishrobocracy.rightclickgivesitem;

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
                PlayerInventory pi = player.getInventory();
                pi.addItem(new ItemStack(plugin.drop, 1));
                player.updateInventory();
                if (plugin.destroy) {
                    event.getClickedBlock().setType(Material.AIR);
                }
                event.setCancelled(plugin.cancel);
                /*int idx = pi.first(itemToGive);
                 if(idx < 0) {
                 idx = pi.firstEmpty();
                 } else {
                 ItemStack is = pi.getItem(idx);
                 if(is.getAmount() >= is.getMaxStackSize()) {
                 }
                 if(idx < 0) {
                 player.sendMessage("Can't collect item; inventory full");
                 return;
                 }
                 ItemStack is = pi.getItem(idx);
                 if(is.getAmount() == 0) {
                 pi.addItem(new ItemStack(itemToGive, 1));
                 } else {
                 if(is.getAmount() < is.getMaxStackSize()) {
                 is.setAmount(is.getAmount() + 1);
                 } else {

                 }
                 }*/
            }
        }
    }
}
