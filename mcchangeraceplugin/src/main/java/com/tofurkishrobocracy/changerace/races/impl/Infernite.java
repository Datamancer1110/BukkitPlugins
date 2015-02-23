package com.tofurkishrobocracy.changerace.races.impl;

import com.tofurkishrobocracy.changerace.races.DefaultBehaviorRace;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class Infernite extends DefaultBehaviorRace {

    private static final double damageInWaterAmt = 1.0;

    public Infernite() {
        super.RACE_NAME = "infernite";
    }

    @Override
    public void unset(Player player) {
        player.removePotionEffect(PotionEffectType.FIRE_RESISTANCE);
        player.removePotionEffect(PotionEffectType.NIGHT_VISION);
    }

    @Override
    public void onPlayerIsInWater(Player player) {
        player.damage(damageInWaterAmt);
    }

    @Override
    public void onPlayerIsNotInWater(Player player) {
        player.addPotionEffect(new PotionEffect(PotionEffectType.FIRE_RESISTANCE, 999999999, 100), true);
    }

    @Override
    public void onPlayerIsInLava(Player player) {
        player.addPotionEffect(new PotionEffect(PotionEffectType.NIGHT_VISION, 999999999, 1), true);
        player.setAllowFlight(true);
        player.setFlying(true);
    }

    @Override
    public void onPlayerIsNotInLava(Player player) {
        player.removePotionEffect(PotionEffectType.NIGHT_VISION);
        player.setAllowFlight(false);
        player.setFlying(false);
    }
}
