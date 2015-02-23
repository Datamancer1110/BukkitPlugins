package com.tofurkishrobocracy.changerace.races.impl;

import com.tofurkishrobocracy.changerace.races.DefaultBehaviorRace;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class Snowman extends DefaultBehaviorRace {

    public Snowman() {
        super.RACE_NAME = "snowman";
    }

    @Override
    public void unset(Player player) {
        player.removePotionEffect(PotionEffectType.JUMP);
        player.removePotionEffect(PotionEffectType.INCREASE_DAMAGE);
    }

    @Override
    public void onPlayerIsNotInWater(Player player) {
        player.addPotionEffect(new PotionEffect(PotionEffectType.JUMP, 999999999, 1), true);
        player.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, 999999999, 1), true);
    }

    @Override
    public double getHungerMultiplier() {
        return 2.0;
    }
}
