package ntnu.idatt2001.projects.simulation;

import ntnu.idatt2001.projects.units.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Assembles units
 */
public class UnitAssembler {

    /**
     * Request a number of a given unit. Checks which
     * unit type and calls for correct method with the
     * quantity and unit as argument. Returns the list
     * from generator method call.
     *
     * @param templateUnit Unit model we are creating
     * @param quantity How many units we are creating
     * @return The units we have requested
     * @throws IllegalArgumentException if Unit is of unfamiliar unit class
     */
    public static List<Unit> requestUnits(Unit templateUnit, int quantity) throws IllegalArgumentException{
        List<Unit> createdUnits;

        if (templateUnit instanceof InfantryUnit){
            createdUnits = new ArrayList<>(generateInfantry(templateUnit,quantity));
        }
        else if (templateUnit instanceof CavalryUnit){
            createdUnits = new ArrayList<>(generateCavalry(templateUnit,quantity));
        }
        else if (templateUnit instanceof RangedUnit){
            createdUnits = new ArrayList<>(generateRanged(templateUnit,quantity));
        }
        else if (templateUnit instanceof CommanderUnit){
            createdUnits = new ArrayList<>(generateCommander(templateUnit,quantity));
        }
        else{
            throw new IllegalArgumentException("templateUnit unfamiliar subclass");
        }

        return createdUnits;
    }

    /**
     * Generates a quantity of a given infantry unit
     * and returns them as a list
     *
     * @param templateUnit Unit model we are creating
     * @param quantity How many units we are creating
     * @return All units we have created
     */
    public static List<Unit> generateInfantry(Unit templateUnit, int quantity){
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
     * Generates a quantity of a given cavalry unit
     * and returns them as a list
     *
     * @param templateUnit Unit model we are creating
     * @param quantity How many units we are creating
     * @return All units we have created
     */
    public static List<Unit> generateCavalry(Unit templateUnit, int quantity){
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
     * Generates a quantity of a given ranged unit
     * and returns them as a list
     *
     * @param templateUnit Unit model we are creating
     * @param quantity How many units we are creating
     * @return All units we have created
     */
    public static List<Unit> generateRanged(Unit templateUnit, int quantity){
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
     * Generates a quantity of a given commander unit
     * and returns them as a list
     *
     * @param templateUnit Unit model we are creating
     * @param quantity How many units we are creating
     * @return All units we have created
     */
    public static List<Unit> generateCommander(Unit templateUnit, int quantity){
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
