package ntnu.idatt2001.projects.model.simulation;

import ntnu.idatt2001.projects.model.units.Unit;

import java.util.*;

/**
 * Represents the map in which
 */
public class Map {
    //Depth and width of the field
    private int depth;
    private int width;
    //Building blocks of the map
    private Location[][] map;
    //Keeps track of locations with units on them
    //TODO look at implementing BiMap to avoid many iterations.
    private HashMap<Location,Unit> unitLocationTracker;

    /**
     * Initiates a new map.
     *
     * @param terrain Terrain array:
     *                Tells us which type of terrain should
     *                be on the different locations
     */
    public Map(Terrain[][] terrain,int depth, int width) throws IllegalArgumentException{
        this.depth = depth;
        this.width = width;
        this.unitLocationTracker = new HashMap<>();
        this.map = new Location[depth][width];

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
     * Clear the given location of unit.
     *
     * @param location The location to clear.
     */
    public void clear(Location location) {
        unitLocationTracker.remove(location);
    }

    /**
     * Clear all locations of units
     */
    public void clear(){
        unitLocationTracker = new HashMap<>();
    }

    /**
     * Return a list of locations adjacent to the one we passed.
     *
     * @param row row of location
     * @param col column of location
     * @return A list of locations adjacent to that given.
     */
    public List<Location> getAdjacentLocations(int row, int col) {
        if(row > getDepth() || row < 0) {
            throw new IllegalArgumentException("Row not on map");
        }
        if(col > getWidth() || col < 0){
            throw new IllegalArgumentException("Column not on map");
        }
        // The list of locations to be returned.
        List<Location> locations = new LinkedList<>();
        if(map[row][col] != null) {
            for(int roffset = -1; roffset <= 1; roffset++) {
                int nextRow = row + roffset;
                if(nextRow >= 0 && nextRow < depth) {
                    for(int coffset = -1; coffset <= 1; coffset++) {
                        int nextCol = col + coffset;
                        // Exclude invalid locations and the original location.
                        if(nextCol >= 0 && nextCol < width && (roffset != 0 || coffset != 0)) {
                            locations.add(map[nextRow][nextCol]);
                        }
                    }
                }
            }

            // Shuffle the list. Several other methods rely on the list
            // being in a random order.
            Collections.shuffle(locations);
        }
        return locations;
    }

    /**
     * Returns the adjacent locations that doesnt
     * contain any units.
     *
     * @param row row of location
     * @param col column of location
     * @return Free adjacent locations
     */
    public List<Location> getFreeAdjacentLocations(int row, int col){
        List<Location> adjacentLocations = getAdjacentLocations(row,col);
        List<Location> freeAdjacentLocations = new ArrayList<>();
        for(Location location : adjacentLocations){
            if(!unitLocationTracker.containsKey(location)){
                freeAdjacentLocations.add(location);
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
     */
    public Location getLocationAt(int row,int col){
        return map[row][col];
    }

    /**
     * Gets unit at position
     */
    public Unit getUnitAt(int row, int col){
        return unitLocationTracker.get(getLocationAt(row,col));
    }

    /**
     * Gets terrain type at location
     *
     * @param location the location
     * @return The terrain at location
     */
    public Terrain getTerrainAt(Location location){
        return map[location.getRow()][location.getCol()].getTerrain();
    }

    /**
     * Gets terrain type at location
     *
     * @param row the row of location
     * @param col the column of location
     * @return The terrain at location
     */
    public Terrain getTerrainAt(int row, int col){
        return map[row][col].getTerrain();
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
     * @return
     */
    public int getWidth() {
        return width;
    }

    /**
     * Moves a unit to a location
     * Checks if location is occupied.
     * Removes old location if unit has one
     * and then places them on the new ine
     *
     * @param unit The unit to move
     * @param location location it gets moved to
     */
    public void moveUnit(Unit unit,Location location) throws Exception{
        if(unitLocationTracker.containsKey(location)){
            throw new Exception("Location is occupied");
        }
        removeUnit(unit);
        placeUnit(unit,location);
    }

    /**
     * Moves a unit to a location
     * Checks if location is occupied.
     * Removes old location if unit has one
     * and then places them on the new ine
     *
     * @param unit The unit to move
     * @param row row of location
     * @param col col of location
     */
    public void moveUnit(Unit unit, int row, int col) throws Exception{
        moveUnit(unit,getLocationAt(row,col));
    }

    /**
     * Place unit on location in map.
     * Can only place unit on location if it
     * is available.
     *
     * @param unit The unit to place
     * @param location the location in map
     * @throws Exception if location is false or occupied.
     */
    public void placeUnit(Unit unit, Location location) throws Exception{
        if(map[location.getRow()][location.getCol()].equals(location)){
            throw new Exception("Location is invalid");
        }
        else if(unitLocationTracker.containsKey(location)){
            throw new Exception("Location is occupied");
        }
        unitLocationTracker.put(location,unit);
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
     * @throws Exception if location is occupied.
     */
    public void placeUnit(Unit unit, int row, int col) throws Exception{
        placeUnit(unit,getLocationAt(row,col));
    }

    /**
     * Removes unit location from
     * the tracker.
     *
     * @param unit the unit to remove
     */
    public void removeUnit(Unit unit){
        if(unitLocationTracker.containsValue(unit)){
            unitLocationTracker.values().remove(unit);
        }
    }
}
