package ntnu.idatt2001.projects.model.units;

import ntnu.idatt2001.projects.model.simulation.Terrain;

/**
 * Infantry unit is a low-mobility close combat unit.
 * Most effective in close vicinity. Preferred terrain
 * is forests where it is better protected from the high
 * speeds of cavalries and ranged attacks.
 */
public class InfantryUnit extends Unit{

    //Constants that makes returning infantry attack and
    //resistance bonuses more perceptible.
    protected static final int INFANTRY_ATTACK_BONUS = 2;
    //Constant for terrainBonus if infantry fights in forest
    protected static final int INFANTRY_FOREST_BONUS = 2;
    protected static final int INFANTRY_RESISTANCE_BONUS = 1;

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
    public InfantryUnit(String name, int health, int attack, int armor){
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
    public InfantryUnit(String name, int health){
        super(name, health, DEFAULT_INFANTRY_ATTACK, DEFAULT_INFANTRY_ARMOR);
    }

    /**
     * Returns the Infantry attack bonus.
     * Has an extra bonus if the Infantry fights
     * in forest.
     *
     * @param terrain The terrain in which the opponent is standing on
     * @return The attack bonus
     */
    @Override
    public int getAttackBonus(Terrain terrain) {
        if(terrain == Terrain.FOREST){
            return INFANTRY_ATTACK_BONUS + INFANTRY_FOREST_BONUS;
        }
        return INFANTRY_ATTACK_BONUS;
    }

    /**
     * Returns the Infantry resistance bonus.
     * Has an extra bonus if the Infantry fights in forest.
     *
     * @param terrain The terrain in which the opponent is standing on
     * @return The resistance bonus
     */
    @Override
    public int getResistBonus(Terrain terrain) {
        if(terrain == Terrain.FOREST){
            return INFANTRY_RESISTANCE_BONUS + INFANTRY_FOREST_BONUS;
        }
        return INFANTRY_RESISTANCE_BONUS;
    }

    @Override
    public String getType(){
        return UnitType.INFANTRY.toString();
    }
}
