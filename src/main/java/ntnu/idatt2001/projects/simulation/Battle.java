package ntnu.idatt2001.projects.simulation;


import ntnu.idatt2001.projects.units.Unit;

import java.util.Iterator;
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
     */
    public Battle(Army armyOne, Army armyTwo) {
        this.armyOne = armyOne;
        this.armyTwo = armyTwo;
    }

    /**
     * Simulates the battle. While both of the armies have
     * units we choose one from each army. One of these are
     * chosen randomly to attack the other. We then check to
     * see if unit is dead. Continues until one of the armies
     * are defeated.
     *
     */
    public void simulate(){
        Random rand = new Random();

        while(armyOne.hasUnits() && armyTwo.hasUnits()){
            Unit armyOneUnit = armyOne.getRandom();
            Unit armyTwoUnit = armyTwo.getRandom();

            if(rand.nextBoolean()){
                armyOneUnit.attack(armyTwoUnit);
                if(armyTwoUnit.getHealth() == 0) armyTwo.remove(armyTwoUnit);
            }
            else{
                armyTwoUnit.attack(armyOneUnit);
                if(armyOneUnit.getHealth() == 0) armyOne.remove(armyOneUnit);
            }
            System.out.println(this.toString());
        }
        Army winner = armyOne;
        if(armyTwo.hasUnits()) winner = armyTwo;

        System.out.println("The winner was " + winner.getName());
        System.out.println(winner.toString());
    }


    /**
     * Creates a String representation of the battles
     * armies.
     *
     * @return The String representation of the armies
     */
    @Override
    public String toString() {
        String output = "Army status: ";
        output += armyOne.toString();
        output += armyTwo.toString();
        return output;
    }
}