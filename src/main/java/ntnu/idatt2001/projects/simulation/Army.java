package ntnu.idatt2001.projects.simulation;

import ntnu.idatt2001.projects.units.CavalryUnit;
import ntnu.idatt2001.projects.units.InfantryUnit;
import ntnu.idatt2001.projects.units.RangedUnit;
import ntnu.idatt2001.projects.units.Unit;

import java.util.*;

/**
 * Army is a collection of units gathered under a
 * common army name. An army can attack a different
 * army.
 *
 * @author Kristian Vaula Jensen
 */
public class Army{
    //The army name
    private String name;
    //The list that keeps all the units
    private List<Unit> units;

    /**
     * Initiates a new Army object. Takes name and a list
     * of units as arguments.
     *
     * @param name The name of the army
     * @param units
     */
    public Army(String name, List<Unit> units) {
        this.name = name;
        this.units = units;
    }

    /**
     * Initiates a new Army object. Takes name as
     * argument and sets units as an empty ArrayList.
     *
     * @param name The name of the army
     */
    public Army(String name) {
        this.name = name;
        this.units = new ArrayList<>();
    }

    /**
     * Gets the name of the army
     * @return The String name
     */
    public String getName() {
        return name;
    }

    /**
     * Adds a unit to the army. If List doesnt alreadey
     * contain the object, we call for List method
     * add() with the argument unit.
     *
     * @param unit The unit we are adding to the army
     * @return True if unit is added to the army
     */
    public boolean add(Unit unit){
        if(this.getAllUnits().contains(unit)) return false;
        this.units.add(unit);
        return true;
    }

    /**
     * Adds a list of units to an army. Iterates through
     * the list and calls the Army.add() methods.
     *
     * @param units The units we are adding to the army
     */
    public void addAll(List<Unit> units){
        Iterator<Unit> it = units.iterator();
        while(it.hasNext()){
            this.add(it.next());
        }
    }

    /**
     * Removes a unit from the army. Calls the
     * remove method on the units list with the
     * unit we are removing as argument.
     *
     * @param unit The unit we are removing
     */
    public void remove(Unit unit){
        this.units.remove(unit);
    }

    /**
     * Checks if the army has units. Calls the
     * isEmpty() method on units list. If list is
     * empty hasUnits returns false.
     *
     * @return True if there are units in the army
     */
    public boolean hasUnits(){
        return !units.isEmpty();
    }

    /**
     * Gets the list of all units.
     * @return The List<Unit> units
     */
    public List<Unit> getAllUnits(){
        return this.units;
    }

    public int getArmySize(){
        return this.units.size();
    }

    /**
     * Gets a random unit from the army.
     * Creates a random object and calls the
     * nextInt method with the army size as
     * argument. This creates a random index
     * that we use to return the unit.
     *
     * @return The random unit.
     */
    public Unit getRandom(){
        Random rand = new Random();
        int index = rand.nextInt(this.getArmySize());

        return this.units.get(index);
    }


    /**
     * Creates a String representation of a
     * army. Displays name of the army and calls
     * the toString method on all the units.
     *
     * @return The String representation of army
     */
    @Override
    public String toString(){
        StringBuilder infantry = new StringBuilder();
        StringBuilder cavalry = new StringBuilder();
        StringBuilder ranged = new StringBuilder();

        for (Unit unit : getAllUnits()){
            if(unit instanceof InfantryUnit){
                infantry.append(unit.toString());
            }
            else if(unit instanceof CavalryUnit){
                cavalry.append(unit.toString());
            }
            else if(unit instanceof RangedUnit){
                ranged.append(unit.toString());
            }
        }
        StringBuilder output = new StringBuilder(name);
        output.append("Infantry: \n").append(infantry);
        output.append("Cavalry: \n").append(cavalry);
        output.append("Ranged: \n").append(ranged);
        return output.toString();
    }

    /**
     * Checks if two armies are equal. For two armies
     * to be equal they need to have the same name
     * and the same exact units. We check this by
     * creating sorted lists with stream and comparing
     * the content.
     *
     * @param o The army we are comparing to
     * @return True if armies are equal.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Army)) return false;
        Army army = (Army) o;

        // Creates a stream to ensure that we never change the data, only use
        // it to do our comparison.
        List<Unit> oSortedArmy = army.units.stream().sorted(Unit::compareTo).toList();
        List<Unit> thisSortedArmy = this.units.stream().sorted(Unit::compareTo).toList();
        return this.getName().equals(army.getName()) && thisSortedArmy.equals(oSortedArmy);
    }

    /**
     * Hashes the army by name and the sorted
     * army list we compared in
     *
     * @return The hashcode.
     */
    @Override
    public int hashCode() {
        List<Unit> sortedArmy = this.units.stream().sorted(Unit::compareTo).toList();
        return Objects.hash(name,sortedArmy.hashCode());
    }
}










