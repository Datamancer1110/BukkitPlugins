/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tofurkishrobocracy.rightclickgivesitem.persistance;

import com.avaje.ebean.validation.NotEmpty;
import com.avaje.ebean.validation.NotNull;
import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import org.bukkit.entity.Player;

@Entity()
@Table(
        name = "rcgi_players"
)
public class PlayerPersistence implements Serializable {

    @Id
    private int id;

    @NotNull
    @NotEmpty
    private String playerName;

    private int delay;

    public PlayerPersistence() {

    }

    public PlayerPersistence(Player p, int delay) {
        playerName = p.getName();
        this.delay = delay;
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

    public int getDelay() {
        return delay;
    }

    public void setDelay(int delay) {
        this.delay = delay;
    }

}
