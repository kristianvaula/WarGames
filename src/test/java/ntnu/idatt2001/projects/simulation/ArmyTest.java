package ntnu.idatt2001.projects.simulation;

import ntnu.idatt2001.projects.units.CavalryUnit;
import ntnu.idatt2001.projects.units.InfantryUnit;
import ntnu.idatt2001.projects.units.Unit;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class ArmyTest {

    @Nested
    @DisplayName("Testing initiation of a new army")
    class InitiationOfArmy{

        @Test
        @DisplayName("Initiate a new army using name")
        public void InitiateArmyWithName(){
            String name = "TestName";
            Army testArmy = new Army(name);
            assertSame(name,testArmy.getName());
        }

        @Test
        @DisplayName("Initiate a new army using name and list")
        public void InitiateArmyWithNameAndList(){
            String name = "TestName";
            List<Unit> testList = new ArrayList<>();
            testList.add(new InfantryUnit("Name",10));
            Army testArmy = new Army(name,testList);

            assertSame(name,testArmy.getName());
            assertSame(testList,testArmy.getAllUnits());
        }
    }

    @Nested
    @DisplayName("Individual methods")
    class MethodTesting{

        @Test
        @DisplayName("getName() method returns correct name")
        public void getNameReturnsCorrectName(){
            String name = "SomeName";
            Army testArmy = new Army(name);

            assertEquals(name,testArmy.getName());
        }

        @Test
        @DisplayName("add() adds a unit to the  ")
        public void addMethodAddsUnit(){
            InfantryUnit testUnit = new InfantryUnit("Axeman",20);
            Army testArmy = new Army("Test army");
            testArmy.add(testUnit);

            assertEquals(testArmy.getAllUnits().get(0),testUnit);
        }

        @Test
        @DisplayName("addAll() adds all units from a list")
        public void addAllAddsAllUnits(){
            List<Unit> unitList = new ArrayList<>();
            unitList.add(new InfantryUnit("Axeman",20));
            unitList.add(new CavalryUnit("Pigrider",20));

            Army testArmy = new Army("Test army");
            testArmy.addAll(unitList);
            assertEquals(testArmy.getAllUnits(),unitList);
        }

        @Test
        @DisplayName("remove() removes unit from list")
        public void removesUnitFromList() {
            InfantryUnit testUnit = new InfantryUnit("Axeman", 20);
            Army testArmy = new Army("Test army");
            testArmy.add(testUnit);

            if (testArmy.getAllUnits().size() > 0) {
                testArmy.remove(testUnit);
                assertThrows(IndexOutOfBoundsException.class, () -> testArmy.getAllUnits().get(0));
            }
        }

        @Test
        @DisplayName("hasUnits() return true if army has units")
        public void hasUnitsReturnsTrue(){
            InfantryUnit testUnit = new InfantryUnit("Axeman",20);
            Army testArmy = new Army("Test army");
            testArmy.add(testUnit);

            assertTrue(testArmy.hasUnits());
        }

        @Test
        @DisplayName("hasUnits() return false if army doesnt have units")
        public void hasUnitsReturnsFalseIfEmpty(){
            Army testArmy = new Army("Test army");
            assertFalse(testArmy.hasUnits());
        }

        @Test
        @DisplayName("getArmySize() returns correct army size")
        public void getArmySizeGivesCorrectSize(){
            List<Unit> unitList = new ArrayList<>();
            unitList.add(new InfantryUnit("Axeman",20));
            unitList.add(new CavalryUnit("Pigrider",20));

            Army testArmy = new Army("Test army");
            testArmy.addAll(unitList);

            assertEquals(unitList.size(),testArmy.getArmySize());
        }

        @Test
        @DisplayName("getRandom() returns ")
        public void getRandomUnitFromList(){
            Unit testUnit = new InfantryUnit("Axeman",20);
            Army testArmy = new Army("Test army");
            testArmy.add(testUnit);

            assertEquals(testArmy.getRandom(),testUnit);
        }

        @Nested
        class EqualsAndHashCode{

            @Test
            @DisplayName("Equals returns true for two equal armies")
            public void TwoArmiesEqualEachOther(){
                Unit testUnit = new InfantryUnit("Axeman",20);
                Army testArmy = new Army("Test army");
                Army testArmy2 = new Army("Test army");
                testArmy.add(testUnit);
                testArmy2.add(testUnit);

                assertTrue(testArmy.equals(testArmy2));
            }

            @Test
            @DisplayName("Equals returns false for two unequal armies")
            public void TwoArmiesDoesNotEqualEachOther(){
                Unit testUnit = new InfantryUnit("Axeman",20);
                Army testArmy = new Army("Test army");
                Army testArmy2 = new Army("Test army with different name");
                testArmy.add(testUnit);
                testArmy2.add(testUnit);

                assertFalse(testArmy.equals(testArmy2));
            }

            @Test
            @DisplayName("getRandom() returns ")
            public void getRandomUnitFromList(){
                Unit testUnit = new InfantryUnit("Axeman",20);
                Army testArmy = new Army("Test army");
                testArmy.add(testUnit);

                assertEquals(testArmy.getRandom(),testUnit);
            }
        }
    }
}