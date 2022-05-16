package me.stephanosbad.edublocks.mcletternumberblocks.Items;

/**
 * Orthogonal Direction indicator (+/-1 x or z)
 */
public class LateralDirection {
    public int xOffset;
    public int zOffset;

    /**
     * Constructor
     * @param xOffset x axis direction.
     * @param zOffset z axis direction
     */
    public LateralDirection(int xOffset, int zOffset)
    {
        this.xOffset = xOffset;
        this.zOffset = zOffset;
    }

    /**
     * Check if this direction is orthogonal
     * @return Validity
     */
    public Boolean isValid()
    {
        return xOffset != 0 || zOffset != 0;
    }
}
