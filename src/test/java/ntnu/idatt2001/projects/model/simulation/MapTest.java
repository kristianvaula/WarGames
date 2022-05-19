package ntnu.idatt2001.projects.model.simulation;

import ntnu.idatt2001.projects.io.TerrainFileHandler;

import ntnu.idatt2001.projects.model.units.InfantryUnit;
import ntnu.idatt2001.projects.model.units.Unit;
import org.junit.jupiter.api.*;

import java.io.IOException;
import java.util.List;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

public class MapTest {
    private static TerrainFileHandler fileHandler;
    private static Map map = null;
    private static final InfantryUnit randUnit = new InfantryUnit("TestUnit",15);
    private static final int depth = 85;
    private static final int width = 135;
    private static final String mixedTerrainName = "mixed_terrain";

    @BeforeAll
    static void init(){
        fileHandler = new TerrainFileHandler();
        Terrain[][] terrains = new Terrain[0][];
        try {
            terrains = fileHandler.getTerrainFromFile(mixedTerrainName,depth,width);
        } catch (IOException e) {
            e.printStackTrace();
            fail();
        }
        map = new Map(terrains,depth,width);
    }

    @BeforeEach
    void setUp() {
        map.clear();
    }

    @Nested
    @DisplayName("File handling and initiation")
    class InitiationAndFileHandling{

        @Test
        @DisplayName("Loading terrain file and initiating map")
        public void loadingTerrainFileAndInitiatingMap(){
            Map testMap = null;

            try{
                Terrain[][] terrains = fileHandler.getTerrainFromFile(mixedTerrainName,depth,width);
                testMap = new Map(terrains,depth,width);
            }
            catch(Exception e){
                e.printStackTrace();
                fail();
            }

            assertTrue(Terrain.getTerrains().contains(testMap.getLocationAt(depth-1,width-1).getTerrain()));
        }

        @Test
        @DisplayName("Initiating map with missing data")
        public void initiatingMapWithMissingData(){
            assertThrows(IllegalArgumentException.class, () ->{
                try{
                    Terrain[][]terrains = fileHandler.getTerrainFromFile(mixedTerrainName,depth,width);
                    int randnumb = new Random().nextInt(depth-1);
                    terrains[randnumb][randnumb] = null;
                    Map testMap = new Map(terrains,depth,width);
                }
                catch(IOException e){
                    e.printStackTrace();
                    fail();
                }
                catch (IllegalArgumentException e){
                    throw e;
                }
            });
        }

        @Test
        @DisplayName("Initiating map with bad Terrains array")
        public void initiatingMapWithBadTerrains(){
            assertThrows(IllegalArgumentException.class, () ->{
                try{
                    Terrain[][]terrains = fileHandler.getTerrainFromFile(mixedTerrainName,depth,width);
                    terrains[0] = new Terrain[6];
                    Map testMap = new Map(terrains,depth,width);
                }
                catch(IOException e){
                    e.printStackTrace();
                    fail();
                }
                catch (IllegalArgumentException e){
                    throw e;
                }
            });
        }

        @Test
        @DisplayName("Initiating map with bad width")
        public void initiatingMapWithBadWidth(){
            assertThrows(IllegalArgumentException.class, () ->{
                try{
                    Terrain[][]terrains = fileHandler.getTerrainFromFile(mixedTerrainName,depth,width);
                    Map testMap = new Map(terrains,depth,126);
                }
                catch(IOException e){
                    e.printStackTrace();
                    fail();
                }
                catch (IllegalArgumentException e){
                    throw e;
                }
            });
        }
    }

    @Nested
    @DisplayName("Method testing")
    class MethodTesting{

        @Test
        @DisplayName("Move unit and location is occupied")
        public void moveUnitAndLocationIsOccupied(){
            Location loc = map.getLocationAt(5,5);
            map.moveUnit(randUnit,loc);

            assertTrue(map.isLocationOccupied(loc));
        }

        @Test
        @DisplayName("Remove Unit and location is not occupied")
        public void loadingTerrainFileAndInitiatingMap(){
            Location loc = map.getLocationAt(5,5);
            map.moveUnit(randUnit,loc);

            if(map.isLocationOccupied(loc)){
                map.removeUnit(randUnit);
                assertFalse(map.isLocationOccupied(loc));
            }
            else{
                fail();
            }
        }

        @Test
        @DisplayName("Clear map")
        public void clearMap(){
            Location loc = map.getLocationAt(5,5);
            map.moveUnit(randUnit,loc);

            if(map.isLocationOccupied(loc)){
                map.clear();
                assertFalse(map.isLocationOccupied(loc));
            }
            else{
                fail();
            }
        }

        @Test
        @DisplayName("Get location by unit")
        public void getLocationByUnit(){
            int rowCell = 5;
            int columnCell = 5;
            map.moveUnit(randUnit,rowCell,columnCell);
            Location location = map.getLocationAt(rowCell,columnCell);

            Location locByUnit = map.getLocationByUnit(randUnit);

            assertEquals(location,locByUnit);
        }

        @Test
        @DisplayName("Get unit by location")
        public void getUnitByLocation(){
            int rowCell = 5;
            int columnCell = 5;
            map.moveUnit(randUnit,rowCell,columnCell);
            Location location = map.getLocationAt(rowCell,columnCell);

            Unit unitByLoc = map.getUnitByLocation(location);

            assertEquals(randUnit,unitByLoc);
        }

        @Test
        @DisplayName("Get adjacent locations")
        public void getAdjacentLocations(){
            int rowCell = 5;
            int columnCell = 5;
            Location loc = map.getLocationAt(rowCell,columnCell);
            List<Location> adjLoc = map.getAdjacentLocations(loc);

            assertTrue(adjLoc.contains(map.getLocationAt(rowCell-1,columnCell-1))
                        && adjLoc.contains(map.getLocationAt(rowCell+1,columnCell+1)));
        }

        @Test
        @DisplayName("Get free adjacent locations")
        public void getFreeAdjacentLocations(){
            int rowCell = 5;
            int columnCell = 5;
            Location loc = map.getLocationAt(rowCell,columnCell);
            map.moveUnit(randUnit,rowCell-1,columnCell-1);
            Location occupiedLoc = map.getLocationByUnit(randUnit);

            List<Location> adjLoc = map.getFreeAdjacentLocations(loc);

            assertFalse(adjLoc.contains(occupiedLoc));
        }
    }
}
