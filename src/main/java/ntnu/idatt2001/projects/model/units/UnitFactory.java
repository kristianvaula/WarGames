package ntnu.idatt2001.projects.model.units;

import java.util.ArrayList;
import java.util.List;

/**
 * Unit factory that creates units
 * All unit creation in the application happens through
 * UnitFactory. The advantage of this is that we can take care
 * of all value checks and handle exceptions at a higher level.
 */
public class UnitFactory {

    /**
     * Returns a unit.
     * Takes specific attributes and type, then
     * returns a new Unit based on these.
     *
     * @param unitType UnitType type of unit
     * @param name name of the unit
     * @param health health of the unit
     * @param attack attack of the unit
     * @param armor armor of the unit
     * @return The unit
     * @throws IllegalArgumentException if values are empty or below zero
     */
    public static Unit getUnit(UnitType unitType, String name, int health, int attack, int armor) throws IllegalArgumentException{
        if(health <= 0 || attack <= 0 || armor <= 0) throw new IllegalArgumentException("Inputs cannot be negative or zero");
        if(name.isBlank() || name.isEmpty()) throw new IllegalArgumentException("Name cannot be empty");
        switch (unitType){
            case INFANTRY -> {
                return new InfantryUnit(name,health,attack,armor);
            }
            case RANGED -> {
                return new RangedUnit(name,health,attack,armor);
            }
            case CAVALRY -> {
                return new CavalryUnit(name,health,attack,armor);
            }
            case COMMANDER -> {
                return new CommanderUnit(name,health,attack,armor);
            }
            default -> {return null;}
        }
    }

    /**
     * Returns a unit.
     * Takes specific attributes and type, then
     * returns a new Unit based on these.
     *
     * @param unitType UnitType type of unit
     * @param name name of the unit
     * @param health health of the unit
     * @return The unit
     * @throws IllegalArgumentException if values are empty or below zero
     */
    public static Unit getUnit(UnitType unitType, String name, int health) throws IllegalArgumentException{
        if(health <= 0) throw new IllegalArgumentException("Inputs cannot be negative or zero");
        if(name.isBlank() || name.isEmpty()) throw new IllegalArgumentException("Name cannot be empty");

        switch (unitType){
            case INFANTRY -> {
                return new InfantryUnit(name,health);
            }
            case RANGED -> {
                return new RangedUnit(name,health);
            }
            case CAVALRY -> {
                return new CavalryUnit(name,health);
            }
            case COMMANDER -> {
                return new CommanderUnit(name,health);
            }
            default -> {return null;}
        }
    }

    /**
     * Returns a given number of units.
     * Takes specific attributes of a unit and
     * creates a templateUnit which we use to call
     * the specific get methods.
     *
     * @param unitType UnitType type of unit
     * @param name name of the unit
     * @param health health of the unit
     * @param numberOfUnits number of units
     * @return A list of the created units
     * @throws IllegalArgumentException if values are empty or below zero
     */
    public static List<Unit> getMultipleUnits(UnitType unitType, String name, int health, int numberOfUnits) throws IllegalArgumentException{
        if(health <= 0) throw new IllegalArgumentException("Inputs cannot be negative or zero");
        if(name.isBlank() || name.isEmpty()) throw new IllegalArgumentException("Name cannot be empty");

        switch (unitType){
            case INFANTRY -> {
                return getInfantry(new InfantryUnit(name,health),numberOfUnits);
            }
            case RANGED -> {
                return getRanged(new RangedUnit(name,health),numberOfUnits);
            }
            case CAVALRY -> {
                return getCavalry(new CavalryUnit(name,health),numberOfUnits);
            }
            case COMMANDER -> {
                return getCommander(new CommanderUnit(name,health),numberOfUnits);
            }
            default -> {return null;}
        }
    }

    /**
     * Returns a given number of units.
     * Takes specific attributes of a unit and
     * creates a templateUnit which we use to call
     * the specific get methods.
     *
     * @param unitType UnitType type of unit
     * @param name name of the unit
     * @param health health of the unit
     * @param attack attack of the unit
     * @param armor armor of the unit
     * @param numberOfUnits number of units
     * @return A list of the created units
     * @throws IllegalArgumentException if values are empty or below zero
     */
    public static List<Unit> getMultipleUnits(UnitType unitType, String name, int health, int attack, int armor, int numberOfUnits) throws IllegalArgumentException{
        if(health <= 0 || attack <= 0 || armor <= 0) throw new IllegalArgumentException("Inputs cannot be negative or zero");
        if(name.isBlank() || name.isEmpty()) throw new IllegalArgumentException("Name cannot be empty");

        switch (unitType){
            case INFANTRY -> {
                return getInfantry(new InfantryUnit(name,health,attack,armor),numberOfUnits);
            }
            case RANGED -> {
                return getRanged(new RangedUnit(name,health,attack,armor),numberOfUnits);
            }
            case CAVALRY -> {
                return getCavalry(new CavalryUnit(name,health,attack,armor),numberOfUnits);
            }
            case COMMANDER -> {
                return getCommander(new CommanderUnit(name,health,attack,armor),numberOfUnits);
            }
            default -> {return null;}
        }
    }

    /**
     * Gets a quantity of a given infantry unit
     * and returns them as a list
     *
     * @param templateUnit Unit model we are creating
     * @param quantity How many units we are creating
     * @return All units we have created
     */
    private static List<Unit> getInfantry(InfantryUnit templateUnit, int quantity){
        List<Unit> infantry = new ArrayList<>();
        for (int i = 0; i < quantity; i++) {
            infantry.add(new InfantryUnit(templateUnit.getName(),
                        templateUnit.getHealth(),
                        templateUnit.getAttack(),
                        templateUnit.getArmor()));
        }
        return infantry;
    }

    /**
     * Gets a quantity of a given cavalry unit
     * and returns them as a list
     *
     * @param templateUnit Unit model we are creating
     * @param quantity How many units we are creating
     * @return All units we have created
     */
    private static List<Unit> getCavalry(CavalryUnit templateUnit, int quantity){
        List<Unit> cavalry = new ArrayList<>();
        for (int i = 0; i < quantity; i++) {
            cavalry.add(new CavalryUnit(templateUnit.getName(),
                        templateUnit.getHealth(),
                        templateUnit.getAttack(),
                        templateUnit.getArmor()));
        }
        return cavalry;
    }

    /**
     * Gets a quantity of a given ranged unit
     * and returns them as a list
     *
     * @param templateUnit Unit model we are creating
     * @param quantity How many units we are creating
     * @return All units we have created
     */
    private static List<Unit> getRanged(RangedUnit templateUnit, int quantity){
        List<Unit> ranged = new ArrayList<>();
        for (int i = 0; i < quantity; i++) {
            ranged.add(new RangedUnit(templateUnit.getName(),
                        templateUnit.getHealth(),
                        templateUnit.getAttack(),
                        templateUnit.getArmor()));
        }
        return ranged;
    }

    /**
     * Gets a quantity of a given commander unit
     * and returns them as a list
     *
     * @param templateUnit Unit model we are creating
     * @param quantity How many units we are creating
     * @return All units we have created
     */
    private static List<Unit> getCommander(CommanderUnit templateUnit, int quantity){
        List<Unit> commander = new ArrayList<>();
        for (int i = 0; i < quantity; i++) {
            commander.add(new CommanderUnit(templateUnit.getName(),
                        templateUnit.getHealth(),
                        templateUnit.getAttack(),
                        templateUnit.getArmor()));
        }
        return commander;
    }
}
