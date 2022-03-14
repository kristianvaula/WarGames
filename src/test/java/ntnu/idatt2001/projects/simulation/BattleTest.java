package ntnu.idatt2001.projects.simulation;

import ntnu.idatt2001.projects.units.InfantryUnit;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class BattleTest {

    @Nested
    @DisplayName("Testing initiation of a new army")
    class InitiationOfArmy {

        @Test
        @DisplayName("Initiate a new Battle with two armies")
        public void InitiateBattleWithArmies() {
            Army testArmyOne = new Army("testarmy1");
            Army testArmyTwo = new Army("testarmy2");

            Battle testBattle = new Battle(testArmyOne,testArmyTwo);

            assertSame(testArmyOne,testBattle.getArmyOne());
        }

        @Test
        @DisplayName("Initiate a new Battle with the same army")
        public void InitiateBattleWithSameArmy() {
            Army testArmy = new Army("TestArmy");
            testArmy.add(new InfantryUnit("Testunit",10));

            try{
                Battle testBattle = new Battle(testArmy,testArmy);
                fail("Method did not throw IllegalArgumentException");
            }catch(IllegalArgumentException e){

                assertEquals(e.getMessage(),"Battle cannot contain the same army twice");
            }
        }
    }

    @Nested
    @DisplayName("Get methods")
    class GetMethodTests{

        @Test
        @DisplayName("Gets army one")
        public void GetsArmyOne() {
            Army testArmyOne = new Army("TestArmyOne");
            Army testArmyTwo = new Army("TestArmyTwo");
            testArmyOne.add(new InfantryUnit("TestUnit",10));
            Battle testBattle = new Battle(testArmyOne,testArmyTwo);

            Army returnArmy = testBattle.getArmyOne();

            assertEquals(returnArmy,testArmyOne);
        }

        @Test
        @DisplayName("Gets army two")
        public void GetsArmytwo() {
            Army testArmyOne = new Army("TestArmyOne");
            Army testArmyTwo = new Army("TestArmyTwo");
            testArmyTwo.add(new InfantryUnit("TestUnit",10));
            Battle testBattle = new Battle(testArmyOne,testArmyTwo);

            Army returnArmy = testBattle.getArmyTwo();

            assertEquals(returnArmy,testArmyTwo);
        }
    }

    @Nested
    @DisplayName("Simulation tests")
    class SimulationTests{

        @Test
        @DisplayName("Getting result from simulate")
        public void SimulateReturnsResult(){
            Army testArmyOne = new Army("TestArmyOne");
            Army testArmyTwo = new Army("TestArmyTwo");
            testArmyOne.add(new InfantryUnit("TestUnit",10));
            testArmyTwo.add(new InfantryUnit("TestUnit",10));
            Battle testBattle = new Battle(testArmyOne,testArmyTwo);

            Army winner = testBattle.simulate();

            assertTrue(winner.getName().equals(testArmyOne.getName())
                        || winner.getName().equals(testArmyTwo.getName()));
        }

        @Test
        @DisplayName("One of the armies are empty after simulation")
        public void OneOfArmiesAreEmptyAfterSimulation(){
            Army testArmyOne = new Army("TestArmyOne");
            Army testArmyTwo = new Army("TestArmyTwo");
            testArmyOne.add(new InfantryUnit("TestUnit",10));
            testArmyTwo.add(new InfantryUnit("TestUnit",10));
            Battle testBattle = new Battle(testArmyOne,testArmyTwo);

            Army winner = testBattle.simulate();
            assertFalse(testBattle.getArmyOne().hasUnits() && testBattle.getArmyTwo().hasUnits());
        }

        @Test
        @DisplayName("Simulation with one empty army")
        public void SimulationWithOneEmptyArmy(){
            Army testArmyOne = new Army("TestArmyOne");
            Army testArmyTwo = new Army("TestArmyTwo");
            testArmyOne.add(new InfantryUnit("TestUnit",10));
            Battle testBattle = new Battle(testArmyOne,testArmyTwo);

            try{
                Army winner = testBattle.simulate();
                fail("Simulate did not return IllegalStateException");
            }catch (IllegalStateException e){
                assertEquals(e.getMessage(),"Both armies must contain units to simulate a battle");
            }
        }
    }
}
