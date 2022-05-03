package me.stephanosbad.edublocks.mcletternumberblocks.utility;

/**
 * @param <T>
 * @param <U>
 */
class SimpleTuple<T, U>
{
    public T first;
    public U second;

    /**
     * @param first
     * @param second
     */
    SimpleTuple(T first, U second) {
        this.first = first;
        this.second = second;
    }
}

