package ntnu.idatt2001.projects.model.simulation;

import ntnu.idatt2001.projects.io.TerrainFileHandler;
import ntnu.idatt2001.projects.model.units.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class MapTest {
    private Map map = null;
    private int depth = 85;
    private int width = 135;
    private String terrainFileName = "mixed_terrain";

    @BeforeEach
    void setUp(){

    }

    @Nested
    @DisplayName("File handling and initiation")
    class InitiationAndFileHandling{

        @Test
        @DisplayName("Loading terrain file and initiating map")
        public void loadingTerrainFileAndInitiatingMap(){
            TerrainFileHandler fileHandler = new TerrainFileHandler();

            try{
                Terrain[][] terrains = fileHandler.getTerrainFromFile(terrainFileName,depth,width);
                map = new Map(terrains,depth,width);
            }
            catch(Exception e){
                e.printStackTrace();
                fail();
            }

            assertEquals(map.getTerrainAt(depth-1,width-1),Terrain.PLAINS);
        }
    }
}
