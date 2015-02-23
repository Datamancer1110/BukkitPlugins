/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tofurkishrobocracy.changerace.persistance;

import com.avaje.ebean.validation.NotEmpty;
import com.avaje.ebean.validation.NotNull;
import com.tofurkishrobocracy.changerace.races.Race;
import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import org.bukkit.entity.Player;

@Entity()
@Table(
        name = "cr_races",
        uniqueConstraints
        = @UniqueConstraint(columnNames = {"playerName"})
)
public class PlayerPersistance implements Serializable {

    @Id
    private int id;

    @NotNull
    @NotEmpty
    private String playerName;

    @NotNull
    @NotEmpty
    private String raceName;

    private int raceLevel;

    public PlayerPersistance() {

    }

    public PlayerPersistance(Player p, Race r) {
        playerName = p.getName();
        raceName = r.getName();
        raceLevel = 1;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPlayerName() {
        return playerName;
    }

    public void setPlayerName(String playerName) {
        this.playerName = playerName;
    }

    public String getRaceName() {
        return raceName;
    }

    public void setRaceName(String raceName) {
        this.raceName = raceName;
    }

    public int getRaceLevel() {
        return raceLevel;
    }

    public void setRaceLevel(int raceLevel) {
        this.raceLevel = raceLevel;
    }
}
