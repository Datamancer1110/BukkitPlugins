/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tofurkishrobocracy.changerace.races;

import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerItemConsumeEvent;

/**
 *
 * @author Cody
 */
public interface Race {

    //public void onPlayerMove(Player player);
    public String getName();

    public void init(Player player);

    public void unset(Player player);

    public void onPlayerItemConsume(PlayerItemConsumeEvent event);

    public void onPlayerIsInSun(Player player);

    public void onPlayerIsNotInWater(Player player);

    public void onPlayerIsInWater(Player player);

    public void onPlayerIsNotInSun(Player player);

    public void onPlayerIsInLava(Player player);

    public void onPlayerIsNotInLava(Player player);

    public double getHungerMultiplier();

    public boolean requiresOp();
}
