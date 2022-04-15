package me.stephanosbad.edublocks.mcletternumberblocks.Items;

import io.th0rgal.oraxen.items.OraxenItems;
import org.bukkit.Bukkit;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;

import static java.lang.Character.toLowerCase;


public enum LetterFactors {

    //Concise Oxford Dictionary (9th edition, 1995)
    E(11.1607, 56.88),
    M(3.0129, 15.36),
    A(8.4966, 43.31),
    H(3.0034, 15.31),
    R(7.5809, 38.64),
    G(2.4705, 12.59),
    I(7.5448, 38.45),
    B(2.0720, 10.56),
    O(7.1635, 36.51),
    F(1.8121, 9.24),
    T(6.9509, 35.43),
    Y(1.7779, 9.06),
    N(6.6544, 33.92),
    W(1.2899, 6.57),
    S(5.7351, 29.23),
    K(1.1016, 5.61),
    L(5.4893, 27.98),
    V(1.0074, 5.13),
    C(4.5388, 23.13),
    X(0.2902, 1.48),
    U(3.6308, 18.51),
    Z(0.2722, 1.39),
    D(3.3844, 17.25),
    J(0.1965, 1.00),
    P(3.1671, 16.14),
    Q(0.1962, 1);

    public final char character;
    private final double frequencyPercent;
    private final double frequencyFactor;
    private double hitLow;
    private double hitHigh;
    final String id;

    LetterFactors(double frequencyPercent, double frequencyFactor)
    {
        this.frequencyPercent = frequencyPercent;
        this.frequencyFactor = frequencyFactor;
        this.character = this.name().charAt(0);
        this.id = toLowerCase(this.character) + "_block";
    }

    private boolean isHit(double testValue)
    {
        return (testValue >= hitLow) && (testValue < hitHigh);
    }

    private static final double hitMax = sumAll();
    private static double sumAll()
    {
        double ret = 0;
        for(var ch: LetterFactors.values())
        {
            ch.hitLow = ret;
            ret += ch.frequencyPercent;
            ch.hitHigh = ret;
        }
        return ret;
    }

    public static LetterFactors randomPick()
    {
        var randVal = Math.random()*hitMax;
        return Arrays.stream(LetterFactors.values()).filter(it -> it.isHit(randVal)).findFirst().orElse(null);
    }

    public static ItemStack randomPickOraxenBlock()
    {
        var item = LetterFactors.randomPick().id;
        Bukkit.getLogger().info(item);

        return OraxenItems.getItemById(item).build();
    }

}
