package me.stephanosbad.edublocks.mcletternumberblocks.utility;

import org.bukkit.Location;

/**
 *
 */
public class LocationPair extends SimplerTuple<Location> {
    /**
     * @param first
     * @param second
     */
    public LocationPair(Location first, Location second) {
        super(first, second);
    }

    /**
     * @return
     */
    public boolean isValid() {
        return first != null && second != null &&
                first.getWorld() == second.getWorld();
    }

    /**
     * @param location
     * @return
     */
    public boolean check(Location location) {
        return location.getWorld() == first.getWorld() &&
                inMcRange(location.getX(), first.getX(), second.getX()) &&
                inMcRange(location.getZ(), first.getZ(), second.getZ());

    }

    /**
     * @param testValue
     * @param x1
     * @param x2
     * @return
     */
    private boolean inMcRange(double testValue, double x1, double x2) {
        if (x1 > x2) {
            return testValue <= x1 && testValue >= x2;
        }
        return testValue >= x1 && testValue <= x2;
    }
}
