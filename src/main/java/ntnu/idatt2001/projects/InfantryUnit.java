package ntnu.idatt2001.projects;

/**
 * Infantry unit is a low-mobility close combat unit.
 * Most effective in close vicinity.
 *
 * @author Kristian Vaula Jensen
 * @version 2022.02.09
 */
public class InfantryUnit extends Unit{

    //Constants that makes returning infantry attack and
    //resistance bonuses more perceptible.
    private static final int INFANTRY_ATTACK_BONUS = 2;
    private static final int INFANTRY_RESISTANCE_BONUS = 1;

    //Constants passed to superclass constructor if attack
    //and armor is not stated in initiation.
    private static final int DEFAULT_INFANTRY_ATTACK = 15;
    private static final int DEFAULT_INFANTRY_ARMOR = 10;

    /**
     * Initiates a new infantry unit. Passes arguments for
     * all fields to superclass constructor.
     *
     * @param name   The unit descriptive name
     * @param health The value of the units health
     * @param attack The attack value of the unit
     * @param armor  The defensive resistance of the unit
     */
    public InfantryUnit(String name, int health, int attack, int armor) {
        super(name, health, attack, armor);
    }

    /**
     * Initiates a new infantry unit. Only takes the arguments
     * name and health, therefore attack and armor parameters
     * are passed with their default value to superclass constructor
     *
     * @param name   The unit descriptive name
     * @param health The value of the units health
     */
    public InfantryUnit(String name, int health) {
        super(name, health, DEFAULT_INFANTRY_ATTACK, DEFAULT_INFANTRY_ARMOR);
    }

    /**
     * Gets the infantry default attack bonus
     *
     * @return The attack bonus
     */
    @Override
    public int getAttackBonus() {
        return INFANTRY_ATTACK_BONUS;
    }

    /**
     * Gets the infantry default resistance bonus
     *
     * @return The resistance bonus
     */
    @Override
    public int getResistBonus() {
        return INFANTRY_RESISTANCE_BONUS;
    }
}
