package com.tofurkishrobocracy.changerace.races.impl;

import com.tofurkishrobocracy.changerace.Util;
import com.tofurkishrobocracy.changerace.races.DefaultBehaviorRace;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class Undead extends DefaultBehaviorRace {

    public Undead() {
        super.RACE_NAME = "undead";
    }

    @Override
    public void unset(Player player) {
        player.setFireTicks(0);
    }

    @Override
    public void onPlayerItemConsume(PlayerItemConsumeEvent event) {
        Util.dontEatFood(event);
    }

    @Override
    public void onPlayerIsInSun(Player player) {
        player.setFireTicks(100);
    }

    @Override
    public void onPlayerIsNotInSun(Player player) {
        player.addPotionEffect(new PotionEffect(PotionEffectType.NIGHT_VISION, 999999999, 1), true);
    }

    @Override
    public double getHungerMultiplier() {
        return 0.0;
    }
}
