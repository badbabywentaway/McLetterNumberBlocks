package me.stephanosbad.edublocks.mcletternumberblocks.Items;

import io.th0rgal.oraxen.items.OraxenItems;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;

import static java.lang.Character.toLowerCase;


/**
 *
 */
public enum LetterFactors {

    //Concise Oxford Dictionary (9th edition, 1995)
    E(11.1607, 56.88, 9),
    M(3.0129, 15.36, 17),
    A(8.4966, 43.31, 5),
    H(3.0034, 15.31, 12),
    R(7.5809, 38.64, 22),
    G(2.4705, 12.59, 11),
    I(7.5448, 38.45, 13),
    B(2.0720, 10.56, 6),
    O(7.1635, 36.51, 19),
    F(1.8121, 9.24, 10),
    T(6.9509, 35.43, 24),
    Y(1.7779, 9.06, 29),
    N(6.6544, 33.92, 18),
    W(1.2899, 6.57, 27),
    S(5.7351, 29.23, 23),
    K(1.1016, 5.61, 15),
    L(5.4893, 27.98, 16),
    V(1.0074, 5.13, 26),
    C(4.5388, 23.13, 7),
    X(0.2902, 1.48, 28),
    U(3.6308, 18.51, 25),
    Z(0.2722, 1.39, 30),
    D(3.3844, 17.25, 8),
    J(0.1965, 1.00, 14),
    P(3.1671, 16.14, 20),
    Q(0.1962, 1, 21);


    /**
     *
     */
    public final char character;

    /**
     *
     */
    public final int customVariation;

    /**
     *
     */
    private final double frequencyPercent;

    /**
     *
     */
    public final double frequencyFactor;

    /**
     *
     */
    private double hitLow;

    /**
     *
     */
    private double hitHigh;

    /**
     *
     */
    final String id;

    /**
     * @param frequencyPercent
     * @param frequencyFactor
     * @param customVariation
     */
    LetterFactors(double frequencyPercent, double frequencyFactor, int customVariation)
    {
        this.frequencyPercent = frequencyPercent;
        this.frequencyFactor = frequencyFactor;
        this.character = this.name().charAt(0);
        this.customVariation = customVariation;
        this.id = toLowerCase(this.character) + "_block";
    }

    /**
     * @param testValue
     * @return
     */
    private boolean isHit(double testValue)
    {
        return (testValue >= hitLow) && (testValue < hitHigh);
    }

    /**
     *
     */
    private static final double hitMax = sumAll();

    /**
     * @return
     */
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

    /**
     * @return
     */
    public static LetterFactors randomPick()
    {
        var randVal = Math.random()*hitMax;
        return Arrays.stream(LetterFactors.values()).filter(it -> it.isHit(randVal)).findFirst().orElse(null);
    }

    /**
     * @return
     */
    public static ItemStack randomPickOraxenBlock()
    {
        var item = LetterFactors.randomPick().id;
        return OraxenItems.getItemById(item).build();
    }
}
