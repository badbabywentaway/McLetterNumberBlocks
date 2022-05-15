package me.stephanosbad.edublocks.mcletternumberblocks.Items;

/**
 *
 */
public class LateralDirection {
    public int xOffset;
    public int zOffset;

    /**
     * @param xOffset
     * @param zOffset
     */
    public LateralDirection(int xOffset, int zOffset)
    {
        this.xOffset = xOffset;
        this.zOffset = zOffset;
    }
    public Boolean isValid()
    {
        return xOffset != 0 || zOffset != 0;
    }
}
