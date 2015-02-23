/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tofurkishrobocracy.rightclickgivesitem.persistance;

import java.util.List;
import org.bukkit.entity.Player;

/**
 *
 * @author Cody
 */
public interface RightClickGivesItemPersistence {

    boolean deletePlayer(Player player);

    List<String> listPlayers();

    void savePlayer(Player player, int delay);

    Integer getDelayForPlayer(Player player);

}
