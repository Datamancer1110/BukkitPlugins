/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tofurkishrobocracy.dighole.asynch;

import com.tofurkishrobocracy.dighole.DimensionSet;
import java.util.logging.Logger;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.scheduler.BukkitRunnable;

/**
 *
 * @author Cody
 */
public class DrawDimensionSet2D extends BukkitRunnable {

    private static final Material dm = Material.GLASS;
    private final DimensionSet ds;
    Logger _log;

    public DrawDimensionSet2D(DimensionSet ds, Logger log) {
        this.ds = ds;
        _log = log;
    }

    public void run() {
        if (ds == null || ds.a == null || ds.b == null) {
            return;
        }
        Location a = ds.a;
        Location b = ds.b;
        a.setY(Math.max(a.getBlockY(), b.getBlockY()));
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
        for (int i = a.getBlockX(); i <= b.getBlockX(); i++) {
            ds.world.getBlockAt(i, a.getBlockY(), a.getBlockZ()).setType(dm);
        }
        for (int i = a.getBlockZ(); i <= b.getBlockZ(); i++) {
            ds.world.getBlockAt(b.getBlockX(), a.getBlockY(), i).setType(dm);
        }
        for (int i = b.getBlockX(); i >= a.getBlockX(); i--) {
            ds.world.getBlockAt(i, a.getBlockY(), b.getBlockZ()).setType(dm);
        }
        for (int i = b.getBlockZ(); i >= a.getBlockZ(); i--) {
            ds.world.getBlockAt(a.getBlockX(), a.getBlockY(), i).setType(dm);
        }
    }
}
