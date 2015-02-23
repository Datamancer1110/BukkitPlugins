/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tofurkishrobocracy.changerace;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerItemConsumeEvent;

/**
 *
 * @author Cody
 */
public class Util {

    public static boolean materialIsFood(Material type) {
        return type == Material.APPLE
                || type == Material.BAKED_POTATO
                || type == Material.BREAD
                || type == Material.BROWN_MUSHROOM
                || type == Material.CAKE
                || type == Material.CAKE_BLOCK
                || type == Material.CARROT
                || type == Material.CARROT_ITEM
                || type == Material.COCOA
                || type == Material.COOKED_BEEF
                || type == Material.COOKED_CHICKEN
                || type == Material.COOKED_FISH
                || type == Material.COOKIE
                || type == Material.GOLDEN_APPLE
                || type == Material.GOLDEN_CARROT
                || type == Material.GRILLED_PORK
                || type == Material.MELON
                || type == Material.MUSHROOM_SOUP
                || type == Material.POISONOUS_POTATO
                || type == Material.PORK
                || type == Material.POTATO
                || type == Material.POTATO_ITEM
                || type == Material.RAW_BEEF
                || type == Material.RAW_CHICKEN
                || type == Material.RAW_FISH
                || type == Material.ROTTEN_FLESH;
    }

    public static void dontEatFood(PlayerItemConsumeEvent event) {
        Player player = event.getPlayer();
        if (Util.materialIsFood(player.getItemInHand().getType())) {
            event.setCancelled(true);
        }
    }

}
