/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tofurkishrobocracy.changerace.persistance;

import com.avaje.ebean.validation.NotEmpty;
import com.avaje.ebean.validation.NotNull;
import com.tofurkishrobocracy.changerace.ChangeRacePlugin;
import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import org.bukkit.Location;
import org.bukkit.entity.Player;

@Entity()
@Table(
        name = "cr_claninfo",
        uniqueConstraints
        = @UniqueConstraint(columnNames = {"playerName"})
)
public class ClanPersistance implements Serializable {

    public static final int NO_CLAN = 0;
    public static final int CLAN_NO_BASE_SET = 1;
    public static final int CLAN_BASE_SET = 2;

    @Id
    private int id;

    @NotNull
    @NotEmpty
    private String playerName;

    private int clanStatus;
    private double clanX;
    private double clanY;
    private double clanZ;
    private String clanWorld;

    public ClanPersistance() {

    }

    public ClanPersistance(Player p, int clanStatus) {
        playerName = p.getName();
        this.clanStatus = clanStatus;
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

    public int getClanStatus() {
        return clanStatus;
    }

    public void setClanStatus(int clanStatus) {
        this.clanStatus = clanStatus;
    }

    public void setClanLocation(Location location) {
        clanX = location.getX();
        clanY = location.getY();
        clanZ = location.getZ();
        clanWorld = location.getWorld().getName();
        this.clanStatus = CLAN_BASE_SET;
    }

    public Location getClanLocation() {
        if (clanStatus < 2) {
            return null;
        }
        return new Location(ChangeRacePlugin.instance.getServer().getWorld(clanWorld), clanX, clanY, clanZ);
    }
}
