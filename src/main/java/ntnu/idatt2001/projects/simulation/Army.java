package ntnu.idatt2001.projects.simulation;

import ntnu.idatt2001.projects.units.*;

import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

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

    //Pattern used to check if name contains any characters except letters, digits and "-" "."
    private static final Pattern namePattern = Pattern.compile("[^a-zA-Z0-9-.\s]");

    /**
     * Initiates a new Army object. Takes name and a list
     * of units as arguments.
     *
     * @param name The name of the army
     * @param units The list of units to add to the army
     */
    public Army(String name, List<Unit> units)throws IllegalArgumentException {
        if(name.isBlank()) throw new IllegalArgumentException("Name cannot be blank");
        if(namePattern.matcher(name).find()) throw new IllegalArgumentException("Name contains illegal characters");
        this.name = name;
        this.units = units;
    }

    /**
     * Initiates a new Army object. Takes name as
     * argument and sets units as an empty ArrayList.
     *
     * @param name The name of the army
     */
    public Army(String name) throws IllegalArgumentException{
        if(name.isBlank()) throw new IllegalArgumentException("Name cannot be blank");
        if(namePattern.matcher(name).find()) throw new IllegalArgumentException("Name contains illegal characters");
        this.name = name;
        this.units = new ArrayList<>();
    }

    /**
     * Gets the name of the army
     *
     * @return The String name
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the name of the army
     *
     * @param name The String name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Adds a unit to the army. We call for List method
     * add() with the argument unit.
     *
     * @param unit The unit we are adding to the army
     */
    public void add(Unit unit){
        this.units.add(unit);
    }

    /**
     * Adds a list of units to an army.
     * Uses Java Collection addAll()
     * to add all units to list
     *
     * @param units The units we are adding to the army
     */
    public void addAll(List<Unit> units){
        this.units.addAll(units);
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

    /**
     * Gets all InfantryUnits
     * <br> Uses stream operations on
     * the units list to get all InfantryUnits
     * @return List of InfantryUnits
     */
    public List<Unit> getInfantryUnits(){
        return units.stream()
                    .filter(u -> u instanceof InfantryUnit)
                    .toList();
    }

    /**
     * Gets all CavalryUnits
     * <br> Uses stream operations on
     * the units list to get all cavalries.
     * Uses getClass() instead of instanceof
     * to exclude subclasses.
     * @return List of CavalryUnits
     */
    public List<Unit> getCavalryUnits(){
        return units.stream()
                    .filter(u -> u.getClass().equals(CavalryUnit.class))
                    .toList();
    }

    /**
     * Gets all RangedUnits
     * <br> Uses stream operations on the
     * units list to get all RangedUnits
     * @return List of RangedUnits
     */
    public List<Unit> getRangedUnits(){
        return units.stream()
                    .filter(u -> u instanceof RangedUnit)
                    .toList();
    }

    /**
     * Gets all CommanderUnits
     * <br> Uses stream operations on the
     * units list to get all CommanderUnits
     * @return List of CommanderUnits
     */
    public List<Unit> getCommanderUnits(){
        return units.stream()
                    .filter(u -> u instanceof CommanderUnit)
                    .toList();
    }

    /**
     * Gets Army Size
     * @return Int army size
     */
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
        return this.units.get(rand.nextInt(this.getArmySize()));
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
        StringBuilder output = new StringBuilder(name);

        output.append("\nCommanders: \n");
        output.append(getCommanderUnits().stream()
                .sorted((u1,u2) -> -u1.getHealth() + u2.getHealth())
                .map(u -> u + "\n").collect(Collectors.joining()));

        output.append("\nInfantry: \n");
        output.append(getInfantryUnits().stream()
                    .sorted((u1,u2) -> -u1.getHealth() + u2.getHealth())
                    .map(u -> u + "\n").collect(Collectors.joining()));

        output.append("\nCavalry: \n");
        output.append(getCavalryUnits().stream()
                    .sorted((u1,u2) -> -u1.getHealth() + u2.getHealth())
                    .map(u -> u + "\n").collect(Collectors.joining()));

        output.append("\nRanged: \n");
        output.append(getRangedUnits().stream()
                    .sorted((u1,u2) -> -u1.getHealth() + u2.getHealth())
                    .map(u -> u + "\n").collect(Collectors.joining()));

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










