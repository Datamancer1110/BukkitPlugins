/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tofurkishrobocracy.rightclickgivesitem.persistance.impl;

import com.tofurkishrobocracy.rightclickgivesitem.RightClickGivesItemPlugin;
import com.tofurkishrobocracy.rightclickgivesitem.persistance.PlayerPersistence;
import com.tofurkishrobocracy.rightclickgivesitem.persistance.RightClickGivesItemPersistence;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import org.bukkit.entity.Player;

/**
 *
 * @author Cody
 */
public class RightClickGivesItemPersistenceImpl implements RightClickGivesItemPersistence {

    private final RightClickGivesItemPlugin plugin;

    public RightClickGivesItemPersistenceImpl(RightClickGivesItemPlugin plugin) {
        this.plugin = plugin;
    }

    public List<String> listPlayers() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public void savePlayer(Player player, int delay) {
        PlayerPersistence pr = getPlayerInternal(player);
        if (pr == null) {
            pr = new PlayerPersistence(player, delay);
            plugin.getDatabase().save(pr);
        } else {
            pr.setDelay(delay);
            plugin.getDatabase().update(pr);
        }
    }

    private PlayerPersistence getPlayerInternal(Player player) {
        return plugin.getDatabase().find(PlayerPersistence.class)
                .where().ieq("playerName", player.getName()).findUnique();
    }

    @Override
    public boolean deletePlayer(Player player) {
        Set<PlayerPersistence> prs = plugin.getDatabase().find(PlayerPersistence.class)
                .where().ieq("playerName", player.getName()).findSet();
        plugin.getLogger().log(Level.INFO, "Deleting {0} entries for player {1}", new Object[]{prs.size(), player.getName()});
        if (prs.isEmpty()) {
            return false;
        }
        for (PlayerPersistence pr : prs) {
            plugin.getDatabase().delete(pr);
        }
        prs = plugin.getDatabase().find(PlayerPersistence.class)
                .where().ieq("playerName", player.getName()).findSet();
        if (!prs.isEmpty()) {
            plugin.getLogger().log(Level.WARNING, "Failed to delete {0} entries", prs.size());
            return false;
        }
        return true;
    }

    public Integer getDelayForPlayer(Player player) {
        PlayerPersistence pp = getPlayerInternal(player);
        return pp == null ? null : pp.getDelay();
    }
}
