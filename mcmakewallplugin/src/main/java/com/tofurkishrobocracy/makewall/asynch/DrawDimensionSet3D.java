/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tofurkishrobocracy.makewall.asynch;

import com.tofurkishrobocracy.makewall.DimensionSet;
import java.util.logging.Logger;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.scheduler.BukkitRunnable;

/**
 *
 * @author Cody
 */
public class DrawDimensionSet3D extends BukkitRunnable {

    private Material dm;
    private final DimensionSet ds;
    Logger _log;

    public DrawDimensionSet3D(DimensionSet ds, Logger log) {
        this.ds = ds;
        this.dm = ds.type;
        _log = log;
    }

    public void run() {
        if (ds == null || ds.a == null || ds.b == null) {
            return;
        }
        Location a = ds.a;
        Location b = ds.b;
        a.setY(Math.min(a.getBlockY(), b.getBlockY()));
        b.setY(a.getBlockY());

        if (a.getBlockX() > b.getBlockX()) {
            if (a.getBlockZ() > b.getBlockZ()) {
                Location tmp = a;
                a = b;
                b = tmp;
            } else {
                int tmp = a.getBlockX();
                a.setX(b.getBlockX());
                b.setX(tmp);
            }
        } else {
            if (a.getBlockZ() > b.getBlockZ()) {
                int tmp = a.getBlockZ();
                a.setZ(b.getBlockZ());
                b.setZ(tmp);
            }
        }
        for (int z = 0; z < ds.height; z++) {
            int by = a.getBlockY() + z;
            for (int j = a.getBlockZ(); j <= b.getBlockZ(); j++) {
                for (int i = a.getBlockX(); i <= b.getBlockX(); i++) {
                    ds.world.getBlockAt(i, by, j).setType(dm);
                }
            }
        }
    }
}
