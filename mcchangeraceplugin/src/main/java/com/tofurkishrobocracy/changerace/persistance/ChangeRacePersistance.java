/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tofurkishrobocracy.changerace.persistance;

import com.tofurkishrobocracy.changerace.persistance.exception.AlreadyInAClanException;
import com.tofurkishrobocracy.changerace.persistance.exception.FailedToPersistException;
import com.tofurkishrobocracy.changerace.persistance.exception.NotInAClanException;
import com.tofurkishrobocracy.changerace.races.Race;
import java.util.List;
import java.util.Map;
import org.bukkit.Location;
import org.bukkit.entity.Player;

/**
 *
 * @author Cody
 */
public interface ChangeRacePersistance {

    void deletePlayer(Player player);

    void deleteClan(Player player);

    Race getRace(Player player);

    void saveRace(Player player, Race race) throws FailedToPersistException;

    List<String> showAllPlayersAndRaces();

    int getClanStatus(Player player);

    void setupClan(Player player) throws AlreadyInAClanException;

    void setClanLocation(Player p, Location location) throws NotInAClanException;

    Location getClanLocation(Player p) throws NotInAClanException;

    void unsetClanLocation(Player p);

    public Map<String, Location> getClanLocations();

}
