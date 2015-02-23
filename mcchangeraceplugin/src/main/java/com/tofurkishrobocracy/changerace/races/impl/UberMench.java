package com.tofurkishrobocracy.changerace.races.impl;

import com.tofurkishrobocracy.changerace.races.DefaultBehaviorRace;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class UberMench extends DefaultBehaviorRace {

    public UberMench() {
        super.RACE_NAME = "ubermench";
    }

    @Override
    public void onPlayerIsNotInWater(Player player) {
        goodStuff(player);
    }

    @Override
    public void onPlayerIsInWater(Player player) {
        goodStuff(player);
    }

    private void goodStuff(Player player) {
        player.addPotionEffect(new PotionEffect(PotionEffectType.NIGHT_VISION, 999999999, 1), false);
        player.addPotionEffect(new PotionEffect(PotionEffectType.WATER_BREATHING, 999999999, 1), false);
        player.addPotionEffect(new PotionEffect(PotionEffectType.FIRE_RESISTANCE, 999999999, 100), false);
        player.addPotionEffect(new PotionEffect(PotionEffectType.JUMP, 999999999, 1), false);
        player.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, 999999999, 1), false);
        player.addPotionEffect(new PotionEffect(PotionEffectType.FAST_DIGGING, 999999999, 15), false);
    }

    @Override
    public void unset(Player player) {
        player.setFlying(false);
        player.setAllowFlight(false);
        player.removePotionEffect(PotionEffectType.NIGHT_VISION);
        player.removePotionEffect(PotionEffectType.WATER_BREATHING);
        player.removePotionEffect(PotionEffectType.FAST_DIGGING);
    }

    @Override
    public double getHungerMultiplier() {
        return 0.0;
    }

    @Override
    public boolean requiresOp() {
        return true;
    }
}
