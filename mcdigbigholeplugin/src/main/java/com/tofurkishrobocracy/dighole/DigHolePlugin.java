/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tofurkishrobocracy.dighole;

import com.tofurkishrobocracy.dighole.asynch.DrawDimensionSet2D;
import com.tofurkishrobocracy.dighole.asynch.DrawDimensionSet3D;
import java.util.HashMap;
import java.util.Map;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

/**
 *
 * @author Cody
 */
public class DigHolePlugin extends JavaPlugin {

    public static DigHolePlugin instance;
    Map<Player, DimensionSet> dimensions = new HashMap<Player, DimensionSet>();

    @Override
    public void onEnable() {
        instance = this;
        getCommand("dighole").setExecutor(new CommandExecutor(this));
        getServer().getPluginManager().registerEvents(new PlayerListener(this), this);
    }

    void startDigHole(Player player) {
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

    void cancelDigHole(Player player) {
        dimensions.remove(player);
    }

    boolean diggingHole(Player p) {
        return dimensions.containsKey(p);
    }

    DimensionSet getDimensionSet(Player p) {
        return dimensions.get(p);
    }

    void drawDimensions(Player p) {
        getServer().getScheduler().runTaskAsynchronously(this, new DrawDimensionSet2D(getDimensionSet(p), getLogger()));
    }

    void digHole(Player p, int depth) {
        DimensionSet ds = getDimensionSet(p);
        if (ds != null) {
            ds.depth = depth;
            getServer().getScheduler().runTaskAsynchronously(this, new DrawDimensionSet3D(getDimensionSet(p), getLogger()));
        }
        cancelDigHole(p);
    }
}
