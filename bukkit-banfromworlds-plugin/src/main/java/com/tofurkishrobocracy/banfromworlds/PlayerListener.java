/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tofurkishrobocracy.banfromworlds;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.logging.Level;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerTeleportEvent;

/**
 * Handle events for all Player related events
 *
 * @author Dinnerbone
 */
public class PlayerListener implements Listener {

    private BanFromWorlds plugin;
    private String worldToTeleportTo;
    private String[] worldsToTeleportFrom;
    private long banLengthInSeconds = 0;
    private Map<UUID, Date> bans = new HashMap<UUID, Date>();

    public PlayerListener(BanFromWorlds p) {
        plugin = p;
        worldToTeleportTo = p.getConfig().getString("world_to_teleport_to_in_event_of_death");
        banLengthInSeconds = p.getConfig().getLong("length_of_time_in_seconds_to_ban_dead_players_from_worlds");
        String worldsToTeleportFromStr = p.getConfig().getString("worlds_in_which_death_causes_teleport_csv");
        if (worldsToTeleportFromStr != null && !worldsToTeleportFromStr.isEmpty()) {
            worldsToTeleportFrom = worldsToTeleportFromStr.split(",");
        }
    }

    private PlayerListener() {
    }

    @EventHandler
    public void deathEvent(PlayerDeathEvent e) {
        if (worldToTeleportTo != null && !worldToTeleportTo.isEmpty() && banLengthInSeconds > 0 && worldsToTeleportFrom != null
                && worldsToTeleportFrom.length > 0 && (e.getEntity() instanceof Player)) {
            final Player player = e.getEntity();
            boolean doit = false;
            for (String world : worldsToTeleportFrom) {
                if (world.equalsIgnoreCase(player.getWorld().getName())) {
                    doit = true;
                    break;
                }
            }
            if (doit) {
                plugin.getLogger().log(Level.INFO, "Player " + player.getName() + " died! Teleporting them to " + worldToTeleportTo + " and banning them from world for " + banLengthInSeconds + " seconds");
                Date bannedTill = new Date(new Date().getTime() + (banLengthInSeconds * 1000));
                bans.put(player.getUniqueId(), bannedTill);
                player.setHealth(player.getMaxHealth());
                player.teleport(plugin.getServer().getWorld(worldToTeleportTo).getSpawnLocation());
            }
        }
    }

    @EventHandler
    public void onPlayerTeleportEvent(PlayerTeleportEvent e) {
        Player player = e.getPlayer();
        Date bannedTill = bans.get(player.getUniqueId());
        long timeleft = (bannedTill == null ? -1 : bannedTill.getTime() - new Date().getTime());
        if (timeleft > 1000) {
            boolean doit = false;
            for (String world : worldsToTeleportFrom) {
                if (world.equalsIgnoreCase(e.getTo().getWorld().getName())) {
                    String unit = "seconds";
                    long measure = timeleft / 1000;
                    if (measure > 60) {
                        unit = "minutes";
                        measure = measure / 60;
                    }
                    if (measure > 60) {
                        unit = "hours";
                        measure = measure / 60;
                    }
                    if (measure > 24) {
                        unit = "days";
                        measure = measure / 24;
                    }
                    player.sendMessage("You cannot teleport to " + e.getTo().getWorld().getName() + " for another " + measure + " " + unit);
                    e.setCancelled(true);
                    return;
                }
            }
        } else {
            plugin.getServer().getScheduler().runTaskAsynchronously(plugin, new Runnable() {

                public void run() {
                    removeFinishedBans();
                }
            });
        }
    }

    public final void removeFinishedBans() {
        Set<UUID> keys = bans.keySet();
        for (UUID key : keys) {
            Date d = bans.get(key);
            if (new Date().getTime() > d.getTime()) {
                bans.remove(key);
            }
        }
    }

    public long secondsToTicks(long seconds) {
        return 20 * seconds;
    }
}
