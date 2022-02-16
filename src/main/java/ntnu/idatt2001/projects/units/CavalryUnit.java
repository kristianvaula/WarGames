package ntnu.idatt2001.projects.units;

/**
 * Cavalry unit is a high-mobility close combat
 * unit. Most effective upon first impact and in
 * close combat
 *
 * @author Kristian Vaula Jensen
 * //@version 2022.02.09
 */
public class CavalryUnit extends Unit{

    //Tracks if the cavalry unit has been attacked
    private boolean hasBeenAttacked = false;

    //Constant representing the cavalry attack bonus at first attack
    protected static final int CAVALRY_CHARGE_ATTACK_BONUS = 6;
    //Constant representing cavalry attack once it has been attacked
    protected static final int CAVALRY_COMBAT_ATTACK_BONUS = 2;
    //Constant representing cavalry unit resistance bonus
    protected static final int CAVALRY_RESISTANCE_BONUS = 1;


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
     * Calls for superclass takeDamage and changes
     * hasBeenAttacked field to true;
     *
     * @param health The value in which the unit takes damage
     */
    @Override
    public void takeDamage(int health){
        super.takeDamage(health);
        hasBeenAttacked = true;
    }

    /**
     * Because the cavalry is stronger the first time
     * it charges, we return a higher constant if
     * hasBeenAttacked is false. Else we return the
     * combat attack constant which is set lower.
     *
     * @return The attack bonus
     */
    @Override
    public int getAttackBonus() {
        if(hasBeenAttacked) return CAVALRY_COMBAT_ATTACK_BONUS;
        return CAVALRY_CHARGE_ATTACK_BONUS;
    }

    @Override
    public int getResistBonus() {
        return CAVALRY_RESISTANCE_BONUS;
    }
}