package ntnu.idatt2001.projects.model.simulation;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Represents a terrain on which a unit stands on.
 * Can affect their behaviour and effectiveness.
 */
public enum Terrain {
    HILL,
    PLAINS,
    FOREST;

    /**
     * Gets a list of all the terrains
     *
     * @return List of all Terrain values
     */
    public static List<Terrain> getTerrains(){
        return new ArrayList<>(Arrays.asList(Terrain.values()));
    }
}
