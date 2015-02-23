/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tofurkishrobocracy.changerace.persistance.impl;

import com.tofurkishrobocracy.changerace.ChangeRacePlugin;
import com.tofurkishrobocracy.changerace.persistance.ChangeRacePersistance;
import com.tofurkishrobocracy.changerace.persistance.exception.AlreadyInAClanException;
import com.tofurkishrobocracy.changerace.persistance.exception.FailedToPersistException;
import com.tofurkishrobocracy.changerace.persistance.exception.NotInAClanException;
import com.tofurkishrobocracy.changerace.races.Race;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.bukkit.Location;
import org.bukkit.entity.Player;

/**
 *
 * @author Cody
 */
public class NoPersistanceImpl implements ChangeRacePersistance {

    private static final Race defaultRace = ChangeRacePlugin.RACE_NONE;

    public NoPersistanceImpl(ChangeRacePlugin plugin) {
    }

    @Override
    public void saveRace(Player player, Race race) throws FailedToPersistException {
    }

    @Override
    public Race getRace(Player player) {
        return defaultRace;
    }

    @Override
    public void deletePlayer(Player player) {
    }

    @Override
    public List<String> showAllPlayersAndRaces() {
        List<String> toReturn = new ArrayList<String>();
        return toReturn;
    }

    public int getClanStatus(Player player) {
        return -1;
    }

    public void setupClan(Player player) throws AlreadyInAClanException {
    }

    public void setClanLocation(Player p, Location location) throws NotInAClanException {
    }

    public Location getClanLocation(Player p) throws NotInAClanException {
        return null;
    }

    public void deleteClan(Player player) {
    }

    public void unsetClanLocation(Player p) {
    }

    public Map<String, Location> getClanLocations() {
        return new HashMap<String, Location>();
    }
}
