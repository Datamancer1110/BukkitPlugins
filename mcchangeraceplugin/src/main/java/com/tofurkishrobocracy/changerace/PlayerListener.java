/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tofurkishrobocracy.changerace;

import com.tofurkishrobocracy.changerace.persistance.ClanPersistance;
import com.tofurkishrobocracy.changerace.persistance.exception.AlreadyInAClanException;
import com.tofurkishrobocracy.changerace.persistance.exception.NotInAClanException;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.entity.Snowball;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

/**
 * Handle events for all Player related events
 *
 * @author Dinnerbone
 */
public class PlayerListener implements Listener {

    private ChangeRacePlugin plugin;

    public PlayerListener(ChangeRacePlugin p) {
        plugin = p;
    }

    private PlayerListener() {
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        plugin.setRace(event.getPlayer(), plugin.getRace(event.getPlayer()));
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        plugin.setRace(event.getPlayer(), plugin.getRace(event.getPlayer()));
    }

    @EventHandler
    public final void onPlayerItemConsume(PlayerItemConsumeEvent event) {
        plugin.getRace(event.getPlayer()).onPlayerItemConsume(event);
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = false)
    public void onEntityDamageByEntity(EntityDamageByEntityEvent e) {
        if (!(e.getDamager() instanceof Projectile)) {
            return;
        }
        if (e.getDamager() instanceof Snowball) {
            e.setDamage(1.0);
        }
    }

    @EventHandler
    public void ScaledHungerDepletion(FoodLevelChangeEvent event) {
        Player p = (Player) event.getEntity();
        double multiplier = plugin.getRace(p).getHungerMultiplier();
        p.setFoodLevel((int) (p.getFoodLevel() - (1 * multiplier)));
    }

    @EventHandler
    public void onPlayerCommandPreprocess(PlayerCommandPreprocessEvent event) {
        Player p = (Player) event.getPlayer();
        String rawCmd = event.getMessage().substring(1);
        if (rawCmd.toLowerCase().startsWith("clan create")) {
            try {
                plugin.persistance.setupClan(p);
            } catch (AlreadyInAClanException ex) {
                p.sendMessage("It looks like you are already in a clan");
                event.setCancelled(true);
                return;
            }
            plugin.getServer().dispatchCommand(plugin.getServer().getConsoleSender(), "/give " + p.getName() + " 1 57");
            p.sendMessage("You've been given a Diamond block");
            p.sendMessage("Place it to set the location of your Clan HQ");
        } else if (rawCmd.toLowerCase().startsWith("clan disband")) {
            plugin.persistance.deleteClan(p);
            p.sendMessage("Your clan is disbanded");
        }
    }

    @EventHandler
    public void onBlockPlaceEvent(BlockPlaceEvent event) {
        if (event.getBlock().getType() == Material.DIAMOND_BLOCK) {
            Player p = (Player) event.getPlayer();
            if (plugin.persistance.getClanStatus(p) == ClanPersistance.CLAN_NO_BASE_SET) {
                try {
                    plugin.persistance.setClanLocation(p, event.getBlock().getLocation());
                    plugin.clanBaseLocations.put(p.getName(), event.getBlock().getLocation());
                    p.sendMessage("Your clan is now set up!");
                } catch (NotInAClanException ex) {
                    return;
                }
            } else {
                p.sendMessage("Where'd you get that?");
                event.getBlock().setType(Material.AIR);
            }
        }
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        if (event.getBlock().getType() == Material.DIAMOND_BLOCK) {
            Player p = (Player) event.getPlayer();
            if (plugin.persistance.getClanStatus(p) == ClanPersistance.CLAN_BASE_SET) {
                try {
                    Location eventLoc = event.getBlock().getLocation();
                    Location clanLoc = plugin.persistance.getClanLocation(p);
                    if (clanLoc.getWorld().getName().equals(eventLoc.getWorld().getName())
                            && clanLoc.getBlockX() == eventLoc.getBlockX()
                            && clanLoc.getBlockY() == eventLoc.getBlockY()
                            && clanLoc.getBlockZ() == eventLoc.getBlockZ()) {
                        plugin.persistance.unsetClanLocation(p);
                        plugin.clanBaseLocations.remove(p.getName());
                        p.sendMessage("Clan HQ unset");
                    } else {
                        p.sendMessage("You can't destroy this block!");
                        event.setCancelled(true);
                    }
                } catch (NotInAClanException ex) {
                    //This *should* be impossible
                }
            } else {
                event.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void OnPlayerInteract(PlayerInteractEvent event) {
        if (ChangeRacePlugin.isWarTime) {
            if (plugin.clanBaseLocations.containsValue(event.getClickedBlock().getLocation())) {
                Player player = event.getPlayer();
                player.sendMessage("You attacked a clan HQ!");
            }
        }
    }
}
