package me.stephanosbad.edublocks.mcletternumberblocks.Items;

import io.th0rgal.oraxen.items.OraxenItems;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;

import static java.lang.Character.toLowerCase;


/**
 * Determine rarity score and Oraxen Noteblock variation via an enum
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
     * letter block character
     */
    public final char character;

    /**
     * Oraxen Noteblock variation
     */
    public final int customVariation;

    /**
     * Frequency of letter found in real life via info received by Oxford publication.
     */
    private final double frequencyPercent;

    /**
     * Score facter of letter
     */
    public final double frequencyFactor;

    /**
     * Hit range low (randomizer)
     */
    private double hitLow;

    /**
     * Hit range high (randomizer)
     */
    private double hitHigh;

    /**
     * Oraxen ID
     */
    final String id;

    /**
     * Constructor
     * @param frequencyPercent - Frequency of letter found in real life via info received by Oxford publication.
     * @param frequencyFactor - Score facter of letter
     * @param customVariation - Oraxen noteblock variation
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
     * Determine if letter is hit by randomizer
     * @param testValue - hit value
     * @return - Veracity of hit
     */
    private boolean isHit(double testValue)
    {
        return (testValue >= hitLow) && (testValue < hitHigh);
    }

    /**
     * Maximum hit value.
     */
    private static final double hitMax = sumAll();

    /**
     * Maximum hit value.
     * @return - Maximum hit value.
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
     * rarity weighted random letter picker
     * @return - picked letter
     */
    public static LetterFactors randomPick()
    {
        var randVal = Math.random()*hitMax;
        return Arrays.stream(LetterFactors.values()).filter(it -> it.isHit(randVal)).findFirst().orElse(null);
    }

    /**
     * Letter block random picker. Weighted by rarity.
     * @return Item stack of single letter block
     */
    public static ItemStack randomPickOraxenBlock()
    {
        var item = LetterFactors.randomPick().id;
        return OraxenItems.getItemById(item).build();
    }
}
