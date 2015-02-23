package com.tofurkishrobocracy.changerace.races.impl;

import com.tofurkishrobocracy.changerace.Util;
import com.tofurkishrobocracy.changerace.races.DefaultBehaviorRace;
import org.bukkit.Material;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class Krakenite extends DefaultBehaviorRace {

    private static final double HM_IN_WATER = 0.0;
    private static final double HM_ON_LAND = 3.0;

    private double _hungerRateMultiplier = HM_IN_WATER;

    public Krakenite() {
        super.RACE_NAME = "krakenite";
    }

    @Override
    public void onPlayerIsNotInWater(Player player) {
        player.setFlying(false);
        player.setAllowFlight(false);
        player.removePotionEffect(PotionEffectType.NIGHT_VISION);
        player.removePotionEffect(PotionEffectType.WATER_BREATHING);
        player.removePotionEffect(PotionEffectType.FAST_DIGGING);
        _hungerRateMultiplier = HM_ON_LAND;
    }

    @Override
    public void onPlayerIsInWater(Player player) {
        player.setAllowFlight(true);
        player.setFlying(true);
        player.addPotionEffect(new PotionEffect(PotionEffectType.NIGHT_VISION, 999999999, 1), true);
        player.addPotionEffect(new PotionEffect(PotionEffectType.WATER_BREATHING, 999999999, 1), true);
        player.setSaturation(20);
        Material blockStandingOn = player.getLocation().getBlock().getRelative(BlockFace.DOWN).getType();
        if ((blockStandingOn == Material.STATIONARY_WATER || blockStandingOn == Material.WATER)) {
            player.addPotionEffect(new PotionEffect(PotionEffectType.FAST_DIGGING, 999999999, 15), true);
        } else {
            player.addPotionEffect(new PotionEffect(PotionEffectType.FAST_DIGGING, 999999999, 5), true);
        }
        _hungerRateMultiplier = HM_IN_WATER;
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
    public void onPlayerItemConsume(PlayerItemConsumeEvent event) {
        Util.dontEatFood(event);
    }

    @Override
    public double getHungerMultiplier() {
        return _hungerRateMultiplier;
    }
}
