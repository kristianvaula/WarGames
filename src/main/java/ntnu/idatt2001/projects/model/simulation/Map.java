package ntnu.idatt2001.projects.model.simulation;

import ntnu.idatt2001.projects.model.units.Unit;
import org.apache.commons.collections4.BidiMap;
import org.apache.commons.collections4.bidimap.DualHashBidiMap;

import java.util.*;

/**
 * Represents the two dimensional map consisting of location objects.
 *
 * A map has a given depth and width, and for every pixel a location.
 * Implements a bi-directional map which keeps two maps, with a key-value
 * pair for each unit on the map. This means that we can perform operations
 * where we look for units using locations, or vise versa without having to
 * iterate through the whole map each time.
 */
public class Map {
    //Depth and width of the field
    private final int depth;
    private final int width;
    //Building blocks of the map
    private final Location[][] map;
    //Keeps track of units placement on location
    private BidiMap<Location,Unit> unitLocationTracker;

    /**
     * Initiates a new map.
     *
     * @param terrain Terrain array:
     *                Tells us which type of terrain should
     *                be on the different locations
     * @param depth int Depth of the map in locations
     * @param width int Width of the map in locations
     * @throws IllegalArgumentException if the terrain argument is missing data
     */
    public Map(Terrain[][] terrain,int depth, int width) throws IllegalArgumentException{
        //Check that the terrain matches the map size
        if(terrain.length != depth || terrain[new Random().nextInt(depth)-1].length != width){
            throw new IllegalArgumentException("Terrain is missing data");
        }
        this.depth = depth;
        this.width = width;
        this.unitLocationTracker = new DualHashBidiMap<>();
        this.map = new Location[depth][width];

        //Fill all locations with terrain type
        for (int i = 0; i < map.length; i++) {
            for (int j = 0; j < map[i].length; j++) {
                if(terrain[i][j] == null) {
                    throw new IllegalArgumentException("Terrain is missing data");
                }
                map[i][j] = new Location(i,j,terrain[i][j]);
            }
        }
    }

    /**
     * Gets depth
     *
     * @return the depth
     */
    public int getDepth() {
        return depth;
    }

    /**
     * Gets width
     *
     * @return the width
     */
    public int getWidth() {
        return width;
    }

    /**
     * Gets unit location tracker.
     *
     * @return the HashMap unit location tracker
     */
    public BidiMap<Location, Unit> getUnitLocationTracker() {
        return unitLocationTracker;
    }

    /**
     * Checks if location has a unit or not.
     * Gets location by row and column and then
     * checks if we have a unit bound to the location
     * in UnitLocationTracker.
     *
     * @param location Location we are checking
     * @return True if the location has a unit.
     */
    public boolean isLocationOccupied(Location location){
        //Check if we have bound unit to this location
        if(unitLocationTracker.containsKey(location)){
            return unitLocationTracker.get(location) != null;
        }
        return false;
    }

    /**
     * Checks if location object is valid.
     * Does multiple checks to see if location
     * is inside of the map and that the location
     * corresponds with its placement on the map.
     *
     * @param location The location we are checking
     * @return True if location is inside map
     * @throws IllegalArgumentException if location object is outside of map
     */
    protected boolean isLocationValid(Location location){
        if(location == null){
            return false;
        }
        else if(location.getRow() <= 0 || location.getRow() > getDepth()-1){
            return false;
        }
        else if(location.getCol() <= 0 || location.getCol() > getWidth()-1){
            return false;
        }
        else return map[location.getRow()][location.getCol()].equals(location);
    }

    /**
     * Return a list of locations adjacent to the one we passed.
     * Checks all locations surrounding a location by looping through
     * from -1 to 1 both vertically and horizontally. The list is
     * shuffled to make unit movement and attack more random.
     *
     * @param location Location we are getting adjacent from
     * @return A list of locations adjacent to that given.
     * @throws IllegalArgumentException If location is invalid
     */
    public List<Location> getAdjacentLocations(Location location) throws IllegalArgumentException {
        if(!isLocationValid(location)) throw new IllegalArgumentException("Location passed is invalid");
        //The list of locations to be returned.
        List<Location> locations = new ArrayList<>();
        //Get all adjacent by looping through row and col from -1 to 1
        for (int rOffset = -1; rOffset <= 1; rOffset++) {
            for (int cOffset = -1; cOffset <= 1; cOffset++) {
                //Get adjacent location
                Location nextLocation = getLocationAt(location.getRow() + rOffset,location.getCol() + cOffset);
                //Check if location is valid
                if(isLocationValid(nextLocation) && !nextLocation.equals(location)){
                    //Add to list
                    locations.add(nextLocation);
                }
            }
        }
        // Shuffle the list. Adjacent locations should not have fixed order
        Collections.shuffle(locations);

        return locations;
    }

    /**
     * Returns the adjacent locations that does
     * not contain any units. Calls for the adjacent
     * locations and uses isLocationOccupied method to
     * check each adjacent location.
     *
     * @param location Location to get adjacent from
     * @return Free adjacent locations to argument location
     */
    public List<Location> getFreeAdjacentLocations(Location location){
        List<Location> freeAdjacentLocations = new ArrayList<>();
        List<Location> adjacentLocations = getAdjacentLocations(location);
        //Iterate through adjacent locations
        for(Location adjLoc : adjacentLocations){
            //Check if location has unit
            if(!isLocationOccupied(adjLoc)){
                freeAdjacentLocations.add(adjLoc);
            }
        }
        return freeAdjacentLocations;
    }

    /**
     * Gets location at a specified point in the map
     *
     * @param row row number
     * @param col column number
     * @return the location
     * @throws IllegalArgumentException if location is invalid
     */
    public Location getLocationAt(int row,int col){
        if(row >= 0 && row <= depth-1 && col >= 0 && col <= width-1){
            return map[row][col];
        }
        return null;
    }

    /**
     * Gets location by unit.
     * We call for the inverse BidiMap
     * unitLocationTracker with Unit as a key
     *
     * @param unit The unit we are getting location from
     * @return Location of unit we passed
     * @throws IllegalArgumentException If the unit does not have location
     */
    public Location getLocationByUnit(Unit unit) throws IllegalArgumentException{
        if(!unitLocationTracker.containsValue(unit)){
            throw new IllegalArgumentException(unit.getName() + " does not have location");
        }
        Location tempPointer = unitLocationTracker.inverseBidiMap().get(unit);
        return map[tempPointer.getRow()][tempPointer.getCol()];
    }

    /**
     * Gets unit by location.
     * Calls the unitLocationTracker with location
     * as key
     *
     * @param location Location we are getting unit from
     * @return The unit at the location
     * @throws IllegalArgumentException if the location does not have a unit
     */
    public Unit getUnitByLocation(Location location) throws IllegalArgumentException{
        if(!isLocationOccupied(location)){
            throw new IllegalArgumentException("Location does not contain unit");
        }
        return unitLocationTracker.get(location);
    }

    /**
     * Clear all locations of units
     */
    public void clear(){
        unitLocationTracker = new DualHashBidiMap<>();
    }

    /**
     * Place unit on location in map.
     * Can only place unit on location if it
     * is available.
     *
     * @param unit The unit to place
     * @param location the location in map
     * @throws IllegalArgumentException if location is false or occupied.
     */
    public void moveUnit(Unit unit, Location location) throws IllegalArgumentException{
        if(!isLocationValid(location)){
            throw new IllegalArgumentException("Location is invalid");
        }
        else if(isLocationOccupied(location)){
            throw new IllegalArgumentException("Location is occupied");
        }
        if(unitLocationTracker.inverseBidiMap().containsKey(unit)){
            removeUnit(unit);
        }

        try{
            unitLocationTracker.put(location,unit);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    /**
     * Place unit on location in map.
     * Can only place unit on location if it
     * is available. Also assigns the unit
     * the location itself.
     *
     * @param unit The unit to place
     * @param row row pos
     * @param col col pos
     * @throws IllegalArgumentException if location is occupied.
     */
    public void moveUnit(Unit unit, int row, int col)throws IllegalArgumentException{
        moveUnit(unit,getLocationAt(row,col));
    }

    /**
     * Removes unit location from
     * the tracker.
     *
     * @param unit the unit to remove
     */
    public void removeUnit(Unit unit){
        if(unitLocationTracker.inverseBidiMap().containsKey(unit)){
            unitLocationTracker.removeValue(unit);
        }
    }
}
