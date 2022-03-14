package ntnu.idatt2001.projects.simulation;


import ntnu.idatt2001.projects.units.Unit;

import java.util.Random;

/**
 * Battle simulates a battle between two armies.
 * The armies fight each other until one of them
 * are defeated.
 *
 * @author Kristian Vaula Jensen
 */
public class Battle {
    //The two armies fighting each other
    private Army armyOne;
    private Army armyTwo;

    /**
     * Initiates a new Battle object. Takes each
     * army as argument.
     *
     * @param armyOne The first army going into battle
     * @param armyTwo The second army going into battle
     * @throws IllegalArgumentException If the armies har equal
     */
    public Battle(Army armyOne, Army armyTwo) throws IllegalArgumentException{
        if(armyOne.equals(armyTwo)) throw new IllegalArgumentException("Battle cannot contain the same army twice");
        this.armyOne = armyOne;
        this.armyTwo = armyTwo;
    }

    /**
     * Gets army one
     *
     * @return The army
     */
    public Army getArmyOne() {
        return armyOne;
    }

    /**
     * Gets army two
     *
     * @return The army
     */
    public Army getArmyTwo() {
        return armyTwo;
    }

    /**
     * Simulates the battle. While both of the armies have
     * units we choose one from each army. One of these are
     * chosen randomly to attack the other. We then check to
     * see if unit is dead. Continues until one of the armies
     * are defeated.
     *
     * @throws IllegalStateException If one of the armies are empty
     * @return The winning army
     */
    public Army simulate() throws IllegalStateException{
        if(!armyOne.hasUnits() || !armyTwo.hasUnits()){
            throw new IllegalStateException("Both armies must contain units to simulate a battle");
        }
        Random rand = new Random();

        while(armyOne.hasUnits() && armyTwo.hasUnits()){
            Unit armyOneUnit = armyOne.getRandom();
            Unit armyTwoUnit = armyTwo.getRandom();

            //The nextBoolean() gives a random true or false value which
            //decides who attacks who
            if(rand.nextBoolean()){
                armyOneUnit.attack(armyTwoUnit);
                if(armyTwoUnit.getHealth() == 0) armyTwo.remove(armyTwoUnit);
            }
            else{
                armyTwoUnit.attack(armyOneUnit);
                if(armyOneUnit.getHealth() == 0) armyOne.remove(armyOneUnit);
            }
        }

        if(armyTwo.hasUnits()) return armyTwo;
        return armyOne;
    }


    /**
     * Creates a String representation of the battles
     * armies.
     *
     * @return The String representation of the armies
     */
    @Override
    public String toString() {
        String output = "Army status: \n";
        output += armyOne.toString() + "\n\n\n";
        output += armyTwo.toString() + "\n";
        return output;
    }
}