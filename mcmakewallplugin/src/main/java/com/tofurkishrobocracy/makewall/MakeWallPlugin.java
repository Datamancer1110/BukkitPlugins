/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tofurkishrobocracy.makewall;

import com.tofurkishrobocracy.makewall.asynch.DrawDimensionSet2D;
import com.tofurkishrobocracy.makewall.asynch.DrawDimensionSet3D;
import java.util.HashMap;
import java.util.Map;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

/**
 *
 * @author Cody
 */
public class MakeWallPlugin extends JavaPlugin {

    public static MakeWallPlugin instance;
    Map<Player, DimensionSet> dimensions = new HashMap<Player, DimensionSet>();

    @Override
    public void onEnable() {
        instance = this;
        getCommand("makewall").setExecutor(new CommandExecutor(this));
        getServer().getPluginManager().registerEvents(new PlayerListener(this), this);
    }

    void startMakeWall(Player player) {
        DimensionSet existingDimensionSet = dimensions.get(player);
        if (existingDimensionSet == null) {
            dimensions.put(player, new DimensionSet(player.getWorld()));
            return;
        } else {
            if (existingDimensionSet.status == DimensionSet.STARTED) {

            }
        }
    }

    void updateDimensionSet(Player p, DimensionSet ds) {
        dimensions.put(p, ds);
    }

    void cancelMakeWall(Player player) {
        dimensions.remove(player);
    }

    boolean makingWall(Player p) {
        return dimensions.containsKey(p);
    }

    DimensionSet getDimensionSet(Player p) {
        return dimensions.get(p);
    }

    void drawDimensions(Player p) {
        getServer().getScheduler().runTaskAsynchronously(this, new DrawDimensionSet2D(getDimensionSet(p), getLogger()));
    }

    void makeWall(Player p, int depth) {
        DimensionSet ds = getDimensionSet(p);
        if (ds != null) {
            ds.height = depth;
            getServer().getScheduler().runTaskAsynchronously(this, new DrawDimensionSet3D(getDimensionSet(p), getLogger()));
        }
        cancelMakeWall(p);
    }
}
