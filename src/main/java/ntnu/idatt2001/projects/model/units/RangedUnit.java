package ntnu.idatt2001.projects.model.units;

/**
 * Ranged units are low-mobility long range units.
 * Most effective from long range where they are less
 * exposed to enemy close combat units.
 *
 * @author Kristian Vaula Jensen
 * //@version 2022.02.09
 */
public class RangedUnit extends Unit{

    //Tracks how many times the ranged unit has been attacked
    private int timesAttacked = 0;

    //Constant for attack bonus.
    protected static final int RANGED_ATTACK_BONUS = 3;
    //Constant for the resistance bonus at first attack
    protected static final int RANGED_MAXIMUM_RANGE_BONUS = 6;
    //Constant for the reduction in bonus as the unit gets attacked
    protected static final int RANGE_BONUS_REDUCTION = 2;

    //Constants passed to default attack and armor if
    //not stated in initiation.
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
    public RangedUnit(String name, int health, int attack, int armor){
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
    public RangedUnit(String name, int health){
        super(name, health, DEFAULT_RANGED_ATTACK, DEFAULT_RANGED_ARMOR);
    }

    /**
     * Calls for superclass takeDamage and increments
     * timesAttacked field to track times attacked.
     *
     * @param health The value in which the unit takes damage
     */
    @Override
    public void takeDamage(int health){
        super.takeDamage(health);
        timesAttacked++;
    }

    @Override
    public int getAttackBonus() {
        return RANGED_ATTACK_BONUS;
    }

    /**
     * Calculates the ranged units resistance bonus. Ranged units
     * resistance is based on how far away he is from his opponent.
     * Resistance bonus is therefore the max bonus subtracted times
     * attacked multiplied with a reduction constant. Resistance bonus
     * is never below the reduction constant itself.
     *
     * @return The resistance bonus
     */
    @Override
    public int getResistBonus() {
        int resistanceBonus = RANGED_MAXIMUM_RANGE_BONUS - (RANGE_BONUS_REDUCTION * timesAttacked);
        //If resistanceBonus bonus can never be below reduction constant.
        return Math.max(resistanceBonus, RANGE_BONUS_REDUCTION);
    }

    @Override
    public String getType(){
        return "Ranged Unit";
    }
}