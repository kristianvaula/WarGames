package ntnu.idatt2001.projects.model.units;

import ntnu.idatt2001.projects.model.simulation.Terrain;

/**
 * Ranged units are low-mobility long range units.
 * Most effective from long range where they are less
 * exposed to enemy close combat units. Has an advantage on
 * hills and a disadvantage in forests.
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
    protected static final int RANGED_MAXIMUM_RESISTANCE_BONUS = 6;
    //Constants for bonus and reductions based on terrain type
    protected static final int RANGED_HILL_ATTACK_BONUS = 2;
    protected static final int RANGED_FOREST_ATTACK_REDUCTION = 2;
    //Constant for the reduction in bonus as the unit gets attacked
    protected static final int RANGED_RESISTANCE_BONUS_REDUCTION = 2;

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

    /**
     * Returns the ranged attack bonus. The ranged unit
     * has an advantage if it is fighting on a hill, and
     * a disadvantage if fighting in a forest. For other
     * instances the basic attack bonus is returned.
     *
     * @param terrain The terrain in which the opponent is standing on
     * @return The attack bonus
     */
    @Override
    public int getAttackBonus(Terrain terrain) {
        if(terrain == Terrain.HILL){
            return RANGED_ATTACK_BONUS + RANGED_HILL_ATTACK_BONUS;
        }
        else if(terrain == Terrain.FOREST){
            //Added math.max when using subtraction to ensure that we never return
            //negative values, even if constants are changed.
            return Math.max(0,RANGED_ATTACK_BONUS - RANGED_FOREST_ATTACK_REDUCTION);
        }
        return RANGED_ATTACK_BONUS;
    }

    /**
     * Returns the ranged units resistance bonus. Ranged units
     * resistance is based on how far away he is from his opponent.
     * Resistance bonus is therefore the max bonus subtracted times
     * attacked multiplied with a reduction constant. Resistance bonus
     * is never below the reduction constant itself.
     *
     * @return The resistance bonus
     */
    @Override
    public int getResistBonus(Terrain terrain) {
        int resistanceBonus = RANGED_MAXIMUM_RESISTANCE_BONUS - (RANGED_RESISTANCE_BONUS_REDUCTION * timesAttacked);
        //If resistanceBonus bonus can never be below reduction constant.
        return Math.max(resistanceBonus, RANGED_RESISTANCE_BONUS_REDUCTION);
    }

    @Override
    public String getType(){
        return "Ranged Unit";
    }
}