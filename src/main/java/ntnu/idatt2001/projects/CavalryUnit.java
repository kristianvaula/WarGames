package ntnu.idatt2001.projects;

/**
 * Cavalry unit is a high-mobility close combat
 * unit. Most effective upon first impact and in
 * close combat
 *
 * @author Kristian Vaula Jensen
 * @version 2022.02.09
 */
public class CavalryUnit extends Unit{

    //Constants that makes returning cavalry attack and
    //resistance bonuses more perceptible.
    private static final int CAVALRY_ATTACK_BONUS = 2;
    private static final int CAVALRY_RESISTANCE_BONUS = 1;

    //Constants used as default values if no attack and health
    // values are passed to the constructor under initiation.
    private static final int DEFAULT_CAVALRY_ATTACK = 20;
    private static final int DEFAULT_CAVALRY_ARMOR = 12;

    /**
     * Initiates a new cavalry unit. Passes arguments for
     * all fields to superclass constructor.
     *
     * @param name   The unit descriptive name
     * @param health The value of the units health
     * @param attack The attack value of the unit
     * @param armor  The defensive resistance of the unit
     */
    public CavalryUnit(String name, int health, int attack, int armor) {
        super(name, health, attack, armor);
    }

    /**
     * Initiates a new cavalry unit. Only takes the arguments
     * name and health, therefore attack and armor parameters
     * are passed with their default value to superclass constructor
     *
     * @param name   The unit descriptive name
     * @param health The value of the units health
     */
    public CavalryUnit(String name, int health) {
        super(name, health, DEFAULT_CAVALRY_ATTACK, DEFAULT_CAVALRY_ARMOR);
    }

    /**
     * Gets the cavalry default attack bonus
     *
     * @return The attack bonus
     */
    @Override
    public int getAttackBonus() {
        //TODO make bonuses dynamic
        return CAVALRY_ATTACK_BONUS;
    }

    /**
     * Gets the cavalry default resistance bonus
     *
     * @return The resistance bonus
     */
    @Override
    public int getResistBonus() {
        //TODO make bonuses dynamic
        return CAVALRY_RESISTANCE_BONUS;
    }
}