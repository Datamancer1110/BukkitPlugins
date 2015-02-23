/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tofurkishrobocracy.changerace.persistance.impl;

import com.tofurkishrobocracy.changerace.ChangeRacePlugin;
import com.tofurkishrobocracy.changerace.persistance.ChangeRacePersistance;
import com.tofurkishrobocracy.changerace.persistance.ClanPersistance;
import com.tofurkishrobocracy.changerace.persistance.PlayerPersistance;
import com.tofurkishrobocracy.changerace.persistance.exception.AlreadyInAClanException;
import com.tofurkishrobocracy.changerace.persistance.exception.FailedToPersistException;
import com.tofurkishrobocracy.changerace.persistance.exception.NotInAClanException;
import com.tofurkishrobocracy.changerace.races.Race;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import org.bukkit.Location;
import org.bukkit.entity.Player;

/**
 *
 * @author Cody
 */
public class CraftBukkitPersistanceImpl implements ChangeRacePersistance {

    private static final Race defaultRace = ChangeRacePlugin.RACE_NONE;
    private final ChangeRacePlugin plugin;

    public CraftBukkitPersistanceImpl(ChangeRacePlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public void saveRace(Player player, Race race) throws FailedToPersistException {
        PlayerPersistance pr = getPlayerRaceInternal(player);
        if (pr == null) {
            pr = new PlayerPersistance(player, race);
            plugin.getDatabase().save(pr);
        } else {
            pr.setRaceName(race.getName());
            pr.setRaceLevel(1);
            plugin.getDatabase().update(pr);
        }

    }

    @Override
    public Race getRace(Player player) {
        Race r = getRaceInternal(player);
        if (r == null) {
            return defaultRace;
        }
        return r;
    }

    public int getClanStatus(Player player) {
        ClanPersistance clanClass = getPlayerClanInternal(player);
        if (clanClass == null) {
            return ClanPersistance.NO_CLAN;
        } else {
            return clanClass.getClanStatus();
        }
    }

    public void setupClan(Player player) throws AlreadyInAClanException {
        ClanPersistance clanClass = getPlayerClanInternal(player);
        if (clanClass == null) {
            clanClass = new ClanPersistance(player, ClanPersistance.CLAN_NO_BASE_SET);
            plugin.getDatabase().save(clanClass);
        } else {
            throw new AlreadyInAClanException("Player is already in a clan!");
        }
    }

    private Race getRaceInternal(Player player) {
        PlayerPersistance raceClass = getPlayerRaceInternal(player);
        if (raceClass == null) {
            return null;
        } else {
            return ChangeRacePlugin.stringToRaceMap.get(raceClass.getRaceName());
        }
    }

    private PlayerPersistance getPlayerRaceInternal(Player player) {
        return plugin.getDatabase().find(PlayerPersistance.class)
                .where().ieq("playerName", player.getName()).findUnique();
    }

    private ClanPersistance getPlayerClanInternal(Player player) {
        return plugin.getDatabase().find(ClanPersistance.class)
                .where().ieq("playerName", player.getName()).findUnique();
    }

    @Override
    public void deletePlayer(Player player) {
        deleteClan(player);
        Set<PlayerPersistance> prs = plugin.getDatabase().find(PlayerPersistance.class)
                .where().ieq("playerName", player.getName()).findSet();
        plugin.getLogger().log(Level.INFO, "Deleting {0} entries for player {1}", new Object[]{prs.size(), player.getName()});
        for (PlayerPersistance pr : prs) {
            plugin.getDatabase().delete(pr);
        }
        prs = plugin.getDatabase().find(PlayerPersistance.class)
                .where().ieq("playerName", player.getName()).findSet();
        if (!prs.isEmpty()) {
            plugin.getLogger().log(Level.WARNING, "Failed to delete {0} entries", prs.size());
        }
    }

    public void deleteClan(Player player) {
        try {
            Location l = getClanLocation(player);
            l.getBlock().breakNaturally();
        } catch (NotInAClanException ex) {
        }
        Set<ClanPersistance> prs = plugin.getDatabase().find(ClanPersistance.class)
                .where().ieq("playerName", player.getName()).findSet();
        plugin.getLogger().log(Level.INFO, "Deleting {0} entries for player {1}", new Object[]{prs.size(), player.getName()});
        for (ClanPersistance pr : prs) {
            plugin.getDatabase().delete(pr);
        }
        prs = plugin.getDatabase().find(ClanPersistance.class)
                .where().ieq("playerName", player.getName()).findSet();
        if (!prs.isEmpty()) {
            plugin.getLogger().log(Level.WARNING, "Failed to delete {0} entries", prs.size());
        }
    }

    @Override
    public List<String> showAllPlayersAndRaces() {
        List<String> toReturn = new ArrayList<String>();
        Set<PlayerPersistance> prs = plugin.getDatabase().find(PlayerPersistance.class).findSet();
        for (PlayerPersistance pr : prs) {
            toReturn.add(pr.getPlayerName() + " : " + pr.getRaceName());
        }
        return toReturn;
    }

    public void setClanLocation(Player p, Location location) throws NotInAClanException {
        ClanPersistance clanClass = getPlayerClanInternal(p);
        if (clanClass == null) {
            throw new NotInAClanException("Can't set location. not in a clan");
        } else {
            clanClass.setClanLocation(location);
        }
    }

    public Location getClanLocation(Player p) throws NotInAClanException {
        ClanPersistance clanClass = getPlayerClanInternal(p);
        if (clanClass == null) {
            throw new NotInAClanException("Can't get location. not in a clan");
        } else {
            return clanClass.getClanLocation();
        }
    }

    public void unsetClanLocation(Player p) {
        ClanPersistance clanClass = getPlayerClanInternal(p);
        if (clanClass != null) {
            clanClass.setClanLocation(null);
            clanClass.setClanStatus(ClanPersistance.CLAN_NO_BASE_SET);
        }
    }

    public Map<String, Location> getClanLocations() {
        Map<String, Location> toReturn = new HashMap<String, Location>();
        List<ClanPersistance> clans = plugin.getDatabase().find(ClanPersistance.class)
                .where().gt("clanStatus", ClanPersistance.CLAN_NO_BASE_SET).findList();
        for (ClanPersistance clan : clans) {
            toReturn.put(clan.getPlayerName(), clan.getClanLocation());
        }
        return toReturn;
    }
}
