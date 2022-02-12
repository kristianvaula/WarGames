package ntnu.idatt2001.projects;

/**
 * Ranged units are low-mobility long range units.
 * Most effective from long range where they are less
 * exposed to enemy close combat units.
 *
 * @author Kristian Vaula Jensen
 * @version 2022.02.09
 */
public class RangedUnit extends Unit{

    //Constants that makes returning ranged unit attack
    //and resistance bonuses more perceptible.
    private static final int RANGED_ATTACK_BONUS = 3;
    private static final int RANGED_RESISTANCE_BONUS = 1;

    //Constants passed to superclass constructor if attack
    //and armor is not stated in initiation.
    private static final int DEFAULT_RANGED_ATTACK = 15;
    private static final int DEFAULT_RANGED_ARMOR = 8;

    /**
     * Initiates a new ranged unit. Passes arguments for
     * all fields to superclass constructor.
     *
     * @param name   The unit descriptive name
     * @param health The value of the units health
     * @param attack The attack value of the unit
     * @param armor  The defensive resistance of the unit
     */
    public RangedUnit(String name, int health, int attack, int armor) {
        super(name, health, attack, armor);
    }

    /**
     * Initiates a new ranged unit. Only takes the arguments
     * name and health, therefore attack and armor parameters
     * are passed with their default value to superclass constructor
     *
     * @param name   The unit descriptive name
     * @param health The value of the units health
     */
    public RangedUnit(String name, int health) {
        super(name, health, DEFAULT_RANGED_ATTACK, DEFAULT_RANGED_ARMOR);
    }

    /**
     * Gets the ranged default attack bonus
     *
     * @return The attack bonus
     */
    @Override
    public int getAttackBonus() {
        //TODO make bonuses dynamic
        return RANGED_ATTACK_BONUS;
    }

    /**
     * Gets the ranged default resistance bonus
     *
     * @return The resistance bonus
     */
    @Override
    public int getResistBonus() {
        //TODO make bonuses dynamic
        return RANGED_RESISTANCE_BONUS;
    }
}