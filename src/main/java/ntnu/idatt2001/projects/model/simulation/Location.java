package ntnu.idatt2001.projects.model.simulation;

/**
 * Represents a single location on a map.
 * A location is of a single terrain type.
 */
public class Location {

    //Rows and column positions
    private final int row;
    private final int col;

    //Terrain
    private final Terrain terrain;

    /**
     * Initiates a location.
     * Sets row, col and a certain type of terrain.
     * @param row row index
     * @param col col index
     * @param terrain terrain type
     */
    public Location(int row, int col, Terrain terrain) {
        this.row = row;
        this.col = col;
        this.terrain = terrain;
    }

    /**
     * Gets row
     * @return the row
     */
    public int getRow() {
        return row;
    }

    /**
     * Gets column
     * @return the column
     */
    public int getCol() {
        return col;
    }

    /**
     * Gets the terrain
     * @return terrain type
     */
    public Terrain getTerrain() {
        return terrain;
    }
}
