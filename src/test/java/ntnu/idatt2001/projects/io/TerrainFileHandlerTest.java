package ntnu.idatt2001.projects.io;


import ntnu.idatt2001.projects.model.simulation.Terrain;
import org.junit.jupiter.api.*;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for terrainFileHandler class
 */
public class TerrainFileHandlerTest {

    TerrainFileHandler terrainFileHandler;
    String mixedTerrain,corruptValue,corruptWidth, corruptDepth;

    //DEFAULT WIDTH AND DEPTH
    private static final int WIDTH = 135;
    private static final int DEPTH = 85;

    // FILE DIRECTORY
    private String FILE_DIRECTORY = "src" + DLM + "main" + DLM + "resources" + DLM +
            "ntnu" + DLM + "idatt2001" + DLM + "projects" + DLM + "saveFiles" + DLM + "mapTestfiles";
    // DELIMITER
    private static final String DLM = File.separator;


    //Pre initiated csv files used in test classes:
    //      corruptdepth.csv
    //      corruptvalue.csv
    //      mixed_terrain.csv
    //      corruptwidth.csv
    //Please check resources -> mapTestfiles before running tests

    @BeforeEach
    void setUp() {
        corruptDepth = "CorruptDepth";
        corruptValue = "CorruptValue";
        corruptWidth = "CorruptWidth";
        mixedTerrain = "Mixed Terrain";

        terrainFileHandler = new TerrainFileHandler();
        terrainFileHandler.setFileDirectory(FILE_DIRECTORY);
    }

    @Nested
    @DisplayName("Reading from file")
    public class readingFromFileTest {

        @Test
        @DisplayName("Reading mixed_terrain.csv")
        public void readingMixedTerrainCSV(){
            Terrain[][] readTerrain = null;

            try {
                readTerrain = terrainFileHandler.getTerrainFromFile(mixedTerrain,DEPTH,WIDTH);
            }catch (Exception e){
                e.printStackTrace();
                fail();
            }

        assertTrue(readTerrain[0][0] != null && readTerrain[DEPTH-1][WIDTH-1] != null);
        }

        @Test
        @DisplayName("Reading corruptdepth.csv throws exception")
        public void readingCorruptDepthCSV(){
            assertThrows(IOException.class, () ->{
                try {
                    Terrain[][] terrain = terrainFileHandler.getTerrainFromFile(corruptDepth,DEPTH,WIDTH);
                }
                catch(Exception e){
                    e.printStackTrace();
                    throw e;
                }
            });
        }

        @Test
        @DisplayName("Reading corruptvalue.csv throws exception")
        public void readingCorruptValueCSV(){
            assertThrows(IOException.class, () ->{
                try {
                    Terrain[][] terrain = terrainFileHandler.getTerrainFromFile(corruptValue,DEPTH,WIDTH);
                }
                catch(Exception e){
                    e.printStackTrace();
                    throw e;
                }
            });
        }

        @Test
        @DisplayName("Reading corruptwidth.csv throws exception")
        public void readingCorruptWidthCSV(){
            assertThrows(IOException.class, () ->{
                try {
                    Terrain[][] terrain = terrainFileHandler.getTerrainFromFile(corruptWidth,DEPTH,WIDTH);
                }catch(Exception e){
                    e.printStackTrace();
                    throw e;
                }
            });
        }

        @Test
        @DisplayName("Reading all default savefiles")
        public void readingAllDefaultSavefiles(){
            ArrayList<Terrain[][]> terrains = null;
            TerrainFileHandler defaultTerrainFileHandler = new TerrainFileHandler();

            try {
                terrains = defaultTerrainFileHandler.getTerrainSavefiles(DEPTH,WIDTH);
            }
            catch (Exception e){
                e.printStackTrace();
                fail();
            }

            assertTrue(terrains.size() > 1 && terrains.get(0)[0][0] != null);
        }
    }

    @Nested
    @DisplayName("Other methods")
    public class methodTesting {

        @Test
        @DisplayName("getFilePath() gets correct file path")
        public void getsCorrectFilePath() {
            String correctFilePath = FILE_DIRECTORY + DLM + "mixed_terrain.csv";

            assertEquals(terrainFileHandler.getFilePath(mixedTerrain), correctFilePath);
        }

        @Test
        @DisplayName("fileExists() returns true if terrain file exists")
        public void fileExistReturnTrue() {
            String extistingTerrainName = mixedTerrain;

            assertTrue(terrainFileHandler.fileExists(terrainFileHandler.getFilePath(extistingTerrainName)));
        }

        @Test
        @DisplayName("fileExists() returns false if terrain file not exists")
        public void fileExistReturnFalse() {
            String nonExistingTerrainName = "RandomTerrainName";

            assertFalse(terrainFileHandler.fileExists(terrainFileHandler.getFilePath(nonExistingTerrainName)));
        }
    }
}