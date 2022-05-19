package ntnu.idatt2001.projects.model.simulation;

import ntnu.idatt2001.projects.io.TerrainFileHandler;
import ntnu.idatt2001.projects.model.units.UnitFactory;
import ntnu.idatt2001.projects.model.units.UnitType;
import ntnu.idatt2001.projects.view.BattleView;
import org.junit.jupiter.api.*;

import java.io.IOException;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

public class BattleTest {
    private static Battle battle;
    private static Random random = new Random();
    private static Army armyOne;
    private static Army armyTwo;

    //Default Width
    private static final int WIDTH = 135;
    //Default Depth
    private static final int DEPTH = 85;
    //Constant that sets delay time in simulation
    private static final int SIMULATION_DELAY_MILLIS = 200;
    //The amount of runs we do in stress test
    private static final int STRESS_TEST_RUNS = 800;
    //Amounts of units per army (increase with caution)
    private static final int UNITS_PER_ARMY = 50;
    private static final String ARMY_NAME_ONE = "armyOne";
    private static final String ARMY_NAME_TWO = "armyTwo";
    private static final String MAP_NAME = "Mixed Terrain";

    private void stressTestSetUp(){
        armyOne = new Army(ARMY_NAME_ONE);
        armyTwo = new Army(ARMY_NAME_TWO);
        for (int i = 0; i < UNITS_PER_ARMY; i++) {
            UnitType randType = UnitType.getUnitTypes().get(random.nextInt(UnitType.getUnitTypes().size()));
            armyOne.add(UnitFactory.getUnit(randType,"TestUnit",20));
            armyTwo.add(UnitFactory.getUnit(randType,"TestUnit",20));
        }
        if(battle == null){ // Initiates first time
            battle = new Battle(armyOne,armyTwo);
            //Add a MapView because we want to graphically display the battle
            battle.setView(new BattleView(DEPTH,WIDTH,battle.getArmyOne().getName(),battle.getArmyTwo().getName()));
        }
        else{ // Resets after last battle
            battle.getMap().clear();
            battle.setArmyOne(armyOne);
            battle.setArmyTwo(armyTwo);
            battle.placeArmies();
        }
    }

    @BeforeEach
    void setUp() {
        armyOne = new Army(ARMY_NAME_ONE);
        armyTwo = new Army(ARMY_NAME_TWO);
        for (int i = 0; i < UNITS_PER_ARMY; i++) {
            UnitType randType = UnitType.getUnitTypes().get(random.nextInt(UnitType.getUnitTypes().size()));
            armyOne.add(UnitFactory.getUnit(randType,"TestUnit",20));
            armyTwo.add(UnitFactory.getUnit(randType,"TestUnit1",20));
        }
        battle = new Battle(armyOne,armyTwo);
    }

    @Nested
    @DisplayName("Testing initiation of a new army")
    class InitiationOfArmy {

        @Test
        @DisplayName("Initiate a new Battle with two armies")
        public void initiateBattleWithArmies() {
            Battle testBattle = new Battle(armyOne,armyTwo);

            assertEquals(armyOne,testBattle.getArmyOne());
        }

        @Test
        @DisplayName("Initiate a new Battle with the same army")
        public void initiateBattleWithSameArmy() {
            assertThrows(IllegalArgumentException.class, () -> {
                Battle testBattle = new Battle(armyOne,armyOne);
            });
        }
    }

    @Nested
    @DisplayName("Get methods")
    class GetMethodTests{

        @Test
        @DisplayName("Gets army one")
        public void getsArmyOne() {
            Army returnArmy = battle.getArmyOne();

            assertEquals(returnArmy,armyOne);
        }

        @Test
        @DisplayName("Gets army two")
        public void getsArmyTwo() {
            Army returnArmy = battle.getArmyTwo();

            assertEquals(returnArmy,armyTwo);
        }
    }

    @Nested
    @DisplayName("Simulation tests")
    class SimulationTests{

        @Test
        @DisplayName("Getting result from simplified simulate")
        public void simplifiedSimulateReturnsResult(){

            Army winner = battle.simulate(SIMULATION_DELAY_MILLIS);

            assertTrue(winner.getName().equals(armyOne.getName())
                        || winner.getName().equals(armyTwo.getName()));
        }

        @Test
        @DisplayName("One of the armies are empty after simulation")
        public void OneOfArmiesAreEmptyAfterSimulation(){
            Army winner = battle.simulate(SIMULATION_DELAY_MILLIS);

            assertFalse(battle.getArmyOne().hasUnits() && battle.getArmyTwo().hasUnits());
        }

        @Test
        @DisplayName("Simulation with one empty army")
        public void SimulationWithOneEmptyArmy(){
            Army emptyArmyOne = new Army("TestArmyOne");

            Battle testBattle = new Battle(emptyArmyOne,armyTwo);

            assertThrows(IllegalStateException.class, () -> {
                Army winner = testBattle.simulate(SIMULATION_DELAY_MILLIS);
            });
        }

        @Test
        @DisplayName("Getting result from fast simulation")
        public void fastSimulateReturnsResult(){

            Army winner = battle.simulate(0);

            assertTrue(winner.getName().equals(armyOne.getName())
                    || winner.getName().equals(armyTwo.getName()));
        }

        @Disabled("Stress testing does not need to be run unless changes have been made")
        @Test
        @DisplayName("Stress testing simulate does not throw exceptions")
        public void stressTestSimulate(){
            int armyOneWins = 0;
            int armyTwoWins = 0;

            try {
                battle.setMap(new Map(new TerrainFileHandler(DEPTH,WIDTH).getTerrainFromFile(MAP_NAME),DEPTH,WIDTH));

                for (int i = 0; i < STRESS_TEST_RUNS; i++) {
                    stressTestSetUp();
                    Army winner = battle.simulate(0);
                    if(winner.equals(armyOne)) armyOneWins++;
                    else armyTwoWins++;
                }
            } catch (IOException e) {
                e.printStackTrace();
                fail();
            }

            System.out.println("Out of " + STRESS_TEST_RUNS + " runs");
            System.out.println("armyOne won " + armyOneWins + " times, and");
            System.out.println("armyTwo won " + armyTwoWins + " times");
        }
    }
}
