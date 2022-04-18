package ntnu.idatt2001.projects.model.units;

/**
 * Cavalry unit is a high-mobility close combat
 * unit. Most effective upon first impact and in
 * close combat
 *
 * @author Kristian Vaula Jensen
 */
public class CavalryUnit extends Unit{

    //Tracks if the cavalry unit has attacked
    private boolean hasAttacked = false;

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
    public CavalryUnit(String name, int health, int attack, int armor){
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
     * Attack an opponent. Calls the Unit attack method
     * and then changes its hasAttacked field to true as
     * it now has attacked an opponent.
     *
     * @param opponent The opponent unit that gets attacked
     */
    @Override
    public void attack(Unit opponent){
        super.attack(opponent);
        this.hasAttacked = true;
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
        if(hasAttacked) return CAVALRY_COMBAT_ATTACK_BONUS;
        return CAVALRY_CHARGE_ATTACK_BONUS;
    }

    @Override
    public int getResistBonus() {
        return CAVALRY_RESISTANCE_BONUS;
    }
}