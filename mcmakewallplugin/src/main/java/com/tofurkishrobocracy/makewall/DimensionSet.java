package com.tofurkishrobocracy.makewall;

import org.bukkit.Location;
import org.bukkit.Material;
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
    public Integer height = null;
    int status = 0;
    //getServer().getWorlds().get(0).getBlockAt(null).setType(Material.AIR);
    public Material type;

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

    public void setDepth(long height) throws IllegalArgumentException {
        if (this.a == null) {
            throw new IllegalArgumentException("Set A first");
        }
        if (this.b == null) {
            throw new IllegalArgumentException("Set B first");
        }
        if (height <= 0) {
            throw new IllegalArgumentException("Height cannot be 0 or negative");
        }
    }
}
