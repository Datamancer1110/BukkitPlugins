/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tofurkishrobocracy.changerace;

import com.tofurkishrobocracy.changerace.asynch.scheduled.CheckPlayersInLavaOrNotTask;
import com.tofurkishrobocracy.changerace.asynch.scheduled.CheckPlayersInSunOrNotTask;
import com.tofurkishrobocracy.changerace.asynch.scheduled.CheckPlayersInWaterOrNotTask;
import com.tofurkishrobocracy.changerace.persistance.ChangeRacePersistance;
import com.tofurkishrobocracy.changerace.persistance.PlayerPersistance;
import com.tofurkishrobocracy.changerace.persistance.exception.FailedToPersistException;
import com.tofurkishrobocracy.changerace.persistance.impl.CraftBukkitPersistanceImpl;
import com.tofurkishrobocracy.changerace.races.Race;
import com.tofurkishrobocracy.changerace.races.ReturnNoRaceOnNullHashMap;
import com.tofurkishrobocracy.changerace.races.impl.Infernite;
import com.tofurkishrobocracy.changerace.races.impl.Krakenite;
import com.tofurkishrobocracy.changerace.races.impl.NoneRace;
import com.tofurkishrobocracy.changerace.races.impl.Snowman;
import com.tofurkishrobocracy.changerace.races.impl.UberMench;
import com.tofurkishrobocracy.changerace.races.impl.Undead;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import javax.persistence.PersistenceException;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Recipe;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.metadata.MetadataValue;
import org.bukkit.plugin.java.JavaPlugin;

/**
 *
 * @author Cody
 */
public class ChangeRacePlugin extends JavaPlugin {

    public static final Race RACE_NONE = new NoneRace();
    public static final Race RACE_KRAKENITE = new Krakenite();
    public static final Race RACE_UNDEAD = new Undead();
    public static final Race RACE_INFERNITE = new Infernite();
    public static final Race RACE_SNOWMAN = new Snowman();
    public static final Race RACE_UBERMENCH = new UberMench();
    public static final Map<String, Race> stringToRaceMap = new ReturnNoRaceOnNullHashMap<String, Race>(RACE_NONE);
    public static boolean isWarTime = false;
    public Map<String, Location> clanBaseLocations = new HashMap<String, Location>();

    static {
        stringToRaceMap.put(RACE_KRAKENITE.getName(), RACE_KRAKENITE);
        stringToRaceMap.put(RACE_NONE.getName(), RACE_NONE);
        stringToRaceMap.put(RACE_UNDEAD.getName(), RACE_UNDEAD);
        stringToRaceMap.put(RACE_INFERNITE.getName(), RACE_INFERNITE);
        stringToRaceMap.put(RACE_SNOWMAN.getName(), RACE_SNOWMAN);
        stringToRaceMap.put(RACE_UBERMENCH.getName(), RACE_UBERMENCH);
    }
    public ChangeRacePersistance persistance;
    public static ChangeRacePlugin instance;

    @Override
    public void onEnable() {
        instance = this;
        //persistance = new NoPersistanceImpl(this);
        persistance = new CraftBukkitPersistanceImpl(this);
        setupDatabase();
        Player[] players = getServer().getOnlinePlayers();
        for (Player player : players) {
            setRace(player, getRace(player));
        }

        Map<String, Location> locs = persistance.getClanLocations();
        clanBaseLocations.putAll(locs);

        getCommand("race").setExecutor(new CommandExecutor(this));
        getServer().getPluginManager().registerEvents(new PlayerListener(this), this);

        getServer().getScheduler().runTaskTimerAsynchronously(this, new CheckPlayersInSunOrNotTask(this), 0L, 20L);
        getServer().getScheduler().runTaskTimerAsynchronously(this, new CheckPlayersInWaterOrNotTask(this), 10L, 20L);
        getServer().getScheduler().runTaskTimerAsynchronously(this, new CheckPlayersInLavaOrNotTask(this), 5L, 20L);

        Iterator<Recipe> iter = getServer().recipeIterator();
        while (iter.hasNext()) {
            Recipe r = iter.next();
            if (r.getResult().getType().equals(Material.DIAMOND_BLOCK)) {
                iter.remove();
            }
        }
    }

    private void setupDatabase() {
        try {
            getDatabase().find(PlayerPersistance.class).findRowCount();
        } catch (PersistenceException ex) {
            System.out.println("Installing database for " + getDescription().getName() + " due to first time usage");
            installDDL();
        }
    }

    @Override
    public List<Class<?>> getDatabaseClasses() {
        List<Class<?>> list = new ArrayList<Class<?>>();
        list.add(PlayerPersistance.class);
        return list;
    }

    @Override
    public void onDisable() {
        saveRaceToPersistanceForAllPlayers();
    }

    public void setRace(Player player, Race race) {
        saveRaceToMetadata(player, race);
        try {
            saveRaceToPersistance(player, race);
        } catch (FailedToPersistException ex) {
            getLogger().log(Level.WARNING,
                    "Failed to save Race to persistance for player "
                    + player.getName(), ex);
        }
        race.init(player);
    }

    public Race getRace(Player player) {
        Race r = getRaceFromMetadata(player);
        if (r != null) {
            return r;
        }
        r = persistance.getRace(player);
        if (r != null) {
            return r;
        }
        return RACE_NONE;
    }

    public void unsetRace(Player targetPlayer) {
        Race r = getRace(targetPlayer);
        r.unset(targetPlayer);
        setRace(targetPlayer, ChangeRacePlugin.RACE_NONE);
    }

    private Race getRaceFromMetadata(Player player) {
        try {
            Race r = (Race) player.getMetadata("ChangeRacePlugin.race").get(0).value();
            return r;
        } catch (Throwable ex) {
            return null;
        }
    }

    private void saveRaceToMetadata(Player player, Race race) {
        player.setMetadata("ChangeRacePlugin.race", new FixedMetadataValue(this, race));
        List<MetadataValue> metas = player.getMetadata("ChangeRacePlugin.race");
        Race r = (Race) metas.get(0).value();
        if (!r.getName().equalsIgnoreCase(race.getName())) {
            player.sendMessage("Something went wrong: ChangeRacePlugin.race=" + r.getName() + " (expected " + race.getName() + ")");
        }
    }

    private void saveRaceToPersistance(Player player, Race race) throws FailedToPersistException {
        try {
            persistance.saveRace(player, race);
        } catch (FailedToPersistException ex) {
            throw ex;
        }
    }

    private void saveRaceToPersistanceForAllPlayers() {
        Player[] players = getServer().getOnlinePlayers();
        for (Player player : players) {
            try {
                saveRaceToPersistance(player, getRace(player));
            } catch (FailedToPersistException ex) {
                getLogger().log(Level.WARNING,
                        "Failed to save Race to persistance for player "
                        + player.getName()
                        + " on server shutdown", ex);
            }
        }
    }
}
