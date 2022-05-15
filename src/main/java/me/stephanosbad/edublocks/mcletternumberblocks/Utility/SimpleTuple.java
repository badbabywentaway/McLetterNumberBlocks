package me.stephanosbad.edublocks.mcletternumberblocks.Utility;

/**
 * @param <T>
 * @param <U>
 */
public class SimpleTuple<T, U> {
    public T first;
    public U second;

    /**
     * @param first
     * @param second
     */
    public SimpleTuple(T first, U second) {
        this.first = first;
        this.second = second;
    }
}

