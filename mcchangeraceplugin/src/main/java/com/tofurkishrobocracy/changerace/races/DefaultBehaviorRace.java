/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tofurkishrobocracy.changerace.races;

import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerItemConsumeEvent;

/**
 *
 * @author Cody
 */
public abstract class DefaultBehaviorRace implements Race {

    public String RACE_NAME;

    public String getName() {
        return RACE_NAME;
    }

    public double getHungerMultiplier() {
        return 1.0;
    }

    public boolean requiresOp() {
        return false;
    }

    public void init(Player player) {
    }

    public void unset(Player player) {
    }

    public void onPlayerItemConsume(PlayerItemConsumeEvent event) {
    }

    public void onPlayerIsInSun(Player player) {
    }

    public void onPlayerIsNotInWater(Player player) {
    }

    public void onPlayerIsInWater(Player player) {
    }

    public void onPlayerIsNotInSun(Player player) {
    }

    public void onPlayerIsInLava(Player player) {
    }

    public void onPlayerIsNotInLava(Player player) {
    }
}
