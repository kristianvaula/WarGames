package ntnu.idatt2001.projects.model.units;

import ntnu.idatt2001.projects.model.simulation.Terrain;

/**
 * Cavalry unit is a high-mobility close combat
 * unit. Most effective upon first impact and generally
 * on open flat surfaces like plains. Has a disadvantage in
 * close quarters like forests.
 *
 * @author Kristian Vaula Jensen
 */
public class CavalryUnit extends Unit{

    //Tracks if the cavalry unit has attacked
    private boolean hasAttacked = false;

    //Constant representing cavalry attack once it has been attacked
    protected static final int CAVALRY_DEFAULT_ATTACK_BONUS = 2;
    //Constant representing the cavalry additional charge attack bonus
    protected static final int CAVALRY_CHARGE_ATTACK_BONUS = 4;
    //Constant representing cavalry unit attack bonus on plains
    protected static final int CAVALRY_PLAINS_ATTACK_BONUS = 2;
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
    public void attack(Unit opponent,Terrain terrain){
        super.attack(opponent,terrain);
        this.hasAttacked = true;
    }

    /**
     * Returns the cavalry attack bonus. If the cavalry
     * has not yet attacked it receives a charge bonus.
     * Also has an advantage on plains.
     *
     * @param terrain The terrain in which the opponent is standing on
     * @return The attack bonus
     */
    @Override
    public int getAttackBonus(Terrain terrain) {
        int attackBonus = CAVALRY_DEFAULT_ATTACK_BONUS;
        //If cavalry is on plains we add the plains bonus
        if(terrain == Terrain.PLAINS) {
            attackBonus += CAVALRY_PLAINS_ATTACK_BONUS;
        }
        //If cavalry has not yet attacked we add charge bonus
        if(!hasAttacked) {
            attackBonus += CAVALRY_CHARGE_ATTACK_BONUS;
        }
        return attackBonus;
    }

    /**
     * Returns the cavalry resistance bonus.
     * Has a disadvantage if its being attacked
     * in a forest.
     *
     * @param terrain The terrain in which the opponent is standing on
     * @return
     */
    @Override
    public int getResistBonus(Terrain terrain) {
        if(terrain == Terrain.FOREST){
            return 0;
        }
        return CAVALRY_RESISTANCE_BONUS;
    }

    @Override
    public String getType(){
        return UnitType.CAVALRY.toString();
    }
}