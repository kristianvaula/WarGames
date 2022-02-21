package ntnu.idatt2001.projects.units;

/**
 * A commander is a stronger version of
 * a cavalry unit.
 */
public class CommanderUnit extends CavalryUnit{

    //Constants passed to superclass constructor if attack
    //and armor is not stated in initiation.
    private static final int DEFAULT_COMMANDER_ATTACK = 25;
    private static final int DEFAULT_COMMANDER_ARMOR = 15;

    /**
     * Initiates a new commander unit. Passes arguments for
     * all fields to superclass constructor.
     *
     * @param name   The unit descriptive name
     * @param health The value of the units health
     * @param attack The attack value of the unit
     * @param armor  The defensive resistance of the unit
     */
    public CommanderUnit(String name, int health, int attack, int armor) throws IllegalArgumentException{
        super(name, health, attack, armor);
    }

    /**
     * Initiates a new commander unit. Only takes the arguments
     * name and health, therefore attack and armor parameters
     * are passed with their default value to superclass constructor
     *
     * @param name   The unit descriptive name
     * @param health The value of the units health
     */
    public CommanderUnit(String name, int health) {
        super(name, health,DEFAULT_COMMANDER_ATTACK,DEFAULT_COMMANDER_ARMOR);
    }
}
