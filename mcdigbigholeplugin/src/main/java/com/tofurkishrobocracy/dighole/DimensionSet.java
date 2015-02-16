package com.tofurkishrobocracy.dighole;

import org.bukkit.Location;
import org.bukkit.World;

/**
 *
 * @author Cody
 */
public class DimensionSet {

    public static final int STARTED = 0;
    public static final int PAUSED = 1;

    public Location a = null;
    public Location b = null;
    public World world;
    public Integer depth = null;
    int status = 0;
    //getServer().getWorlds().get(0).getBlockAt(null).setType(Material.AIR);

    public DimensionSet(World w) {
        world = w;
    }

    public void setA(Location a) {
        this.a = a;
    }

    public void setB(Location b) throws IllegalArgumentException {
        if (this.a == null) {
            throw new IllegalArgumentException("Set A first");
        }
        this.b = b;
    }

    public void setDepth(long depth) throws IllegalArgumentException {
        if (this.a == null) {
            throw new IllegalArgumentException("Set A first");
        }
        if (this.b == null) {
            throw new IllegalArgumentException("Set B first");
        }
        if (depth <= 0) {
            throw new IllegalArgumentException("Depth cannot be 0 or negative");
        }
    }
}
