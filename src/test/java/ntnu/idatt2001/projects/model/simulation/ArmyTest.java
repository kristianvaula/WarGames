package ntnu.idatt2001.projects.model.simulation;

import ntnu.idatt2001.projects.model.units.*;

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

            assertEquals(testList,testArmy.getAllUnits());
        }

        @Test
        @DisplayName("Initiate a new army using empty name")
        public void InitiateArmyWithBlankName(){
            String name = " ";
            List<Unit> testList = new ArrayList<>();
            testList.add(new InfantryUnit("Name",10));

            assertThrows(IllegalArgumentException.class, () -> {
                Army testArmy = new Army(name,testList);
            });
        }

        @Test
        @DisplayName("Initiate a new army using illegal characters")
        public void InitiateArmyWithIllegalCharacters(){
            String name = "BadA$$ Army";
            List<Unit> testList = new ArrayList<>();
            testList.add(new InfantryUnit("Name",10));

            assertThrows(IllegalArgumentException.class, () -> {
                Army testArmy = new Army(name,testList);
            });
        }

        @Test
        @DisplayName("Initiate a new army using illegal characters")
        public void InitiateArmyWithIllegalCharactersTwo(){
            String name = "Army Nam=";

            assertThrows(IllegalArgumentException.class, () -> {
                Army testArmy = new Army(name);
            });
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

            testArmy.remove(testUnit);

            assertThrows(IndexOutOfBoundsException.class, () -> {
                Unit unit = testArmy.getAllUnits().get(0);
            });
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

        @Nested
        @DisplayName("getInfantryUnits()")
        public class getInfantryUnitsTests{

            @Test
            @DisplayName("getInfantryUnits() gets infantry units")
            public void getInfantryUnits(){
                Army testArmy = new Army("Test army");
                testArmy.add(new CavalryUnit("CavalryUnit",20));
                testArmy.add(new RangedUnit("RangedUnit",20));
                testArmy.add(new CommanderUnit("CommanderUnit",20));
                InfantryUnit infantryUnit1 = (new InfantryUnit("InfantryUnit1",20));
                testArmy.add(infantryUnit1);
                InfantryUnit infantryUnit2 = (new InfantryUnit("InfantryUnit2",20));
                testArmy.add(infantryUnit2);


                ArrayList<Unit> infantries = new ArrayList<>(testArmy.getInfantryUnits());

                assertTrue(infantries.contains(infantryUnit1)
                        && infantries.contains(infantryUnit2));
            }

            @Test
            @DisplayName("getInfantryUnits() excludes other types")
            public void getInfantryUnitsExcludesOthers(){
                Army testArmy = new Army("Test army");
                testArmy.add(new InfantryUnit("InfantryUnit1",20));
                RangedUnit rangedUnit = new RangedUnit("RangedUnit",20);
                testArmy.add(rangedUnit);

                ArrayList<Unit> infantries = new ArrayList<>(testArmy.getInfantryUnits());

                assertFalse(infantries.contains(rangedUnit));
            }
        }

        @Nested
        @DisplayName("getCavalryUnits()")
        public class getCavalryUnitsTests{

            @Test
            @DisplayName("getCavalryUnits() gets cavalry units")
            public void getCavalryUnits(){
                Army testArmy = new Army("Test army");
                testArmy.add(new InfantryUnit("InfantryUnit",20));
                testArmy.add(new RangedUnit("RangedUnit",20));
                testArmy.add(new CommanderUnit("CommanderUnit",20));
                CavalryUnit cavalryUnit1 = (new CavalryUnit("CavalryUnit1",20));
                testArmy.add(cavalryUnit1);
                CavalryUnit cavalryUnit2 = (new CavalryUnit("CavalryUnit2",20));
                testArmy.add(cavalryUnit2);


                ArrayList<Unit> cavalries = new ArrayList<>(testArmy.getCavalryUnits());

                assertTrue(cavalries.contains(cavalryUnit1)
                        && cavalries.contains(cavalryUnit2));
            }

            @Test
            @DisplayName("getCavalryUnits() excludes other types")
            public void getCavalryUnitsExcludesOthers(){
                Army testArmy = new Army("Test army");
                testArmy.add(new CavalryUnit("CavalryUnit1",20));
                InfantryUnit infantryUnit = new InfantryUnit("InfantryUnit",20);
                testArmy.add(infantryUnit);

                ArrayList<Unit> cavalries = new ArrayList<>(testArmy.getCavalryUnits());

                assertFalse(cavalries.contains(infantryUnit));
            }

            @Test
            @DisplayName("getCavalryUnits() excludes CommanderUnits")
            public void getCavalryUnitsExcludeCommanderUnit(){
                Army testArmy = new Army("Test army");
                testArmy.add(new CavalryUnit("CavalryUnit1",20));
                testArmy.add(new CavalryUnit("CavalryUnit2",20));
                CommanderUnit commanderUnit = new CommanderUnit("CommanderUnit",20);
                testArmy.add(commanderUnit);

                ArrayList<Unit> cavalries = new ArrayList<>(testArmy.getCavalryUnits());

                assertFalse(cavalries.contains(commanderUnit));
            }
        }

        @Nested
        @DisplayName("getRangedUnits()")
        public class getRangedUnitsTests{

            @Test
            @DisplayName("getRangedUnits() gets infantry units")
            public void getRangedUnits(){
                Army testArmy = new Army("Test army");
                testArmy.add(new CavalryUnit("CavalryUnit",20));
                testArmy.add(new InfantryUnit("InfantryUnit",20));
                testArmy.add(new CommanderUnit("CommanderUnit",20));
                RangedUnit rangedUnit1 = (new RangedUnit("RangedUnit1",20));
                testArmy.add(rangedUnit1);
                RangedUnit rangedUnit2 = (new RangedUnit("RangedUnit2",20));
                testArmy.add(rangedUnit2);


                ArrayList<Unit> ranged = new ArrayList<>(testArmy.getRangedUnits());

                assertTrue(ranged.contains(rangedUnit1)
                        && ranged.contains(rangedUnit2));
            }

            @Test
            @DisplayName("getRangedUnits() excludes other types")
            public void getRangedUnitsExcludesOthers(){
                Army testArmy = new Army("Test army");
                testArmy.add(new RangedUnit("InfantryUnit1",20));
                InfantryUnit infantryUnit = new InfantryUnit("InfantryUnit",20);
                testArmy.add(infantryUnit);

                ArrayList<Unit> ranged = new ArrayList<>(testArmy.getRangedUnits());

                assertFalse(ranged.contains(infantryUnit));
            }
        }

        @Nested
        @DisplayName("getCommanderUnits()")
        public class getCommanderUnitsTests{

            @Test
            @DisplayName("getCommanderUnits() gets cavalry units")
            public void getCommanderUnits(){
                Army testArmy = new Army("Test army");
                testArmy.add(new InfantryUnit("InfantryUnit",20));
                testArmy.add(new RangedUnit("RangedUnit",20));
                testArmy.add(new CavalryUnit("CommanderUnit",20));
                CommanderUnit commanderUnit1 = (new CommanderUnit("CommanderUnit1",20));
                testArmy.add(commanderUnit1);
                CommanderUnit commanderUnit2 = (new CommanderUnit("CommanderUnit2",20));
                testArmy.add(commanderUnit2);


                ArrayList<Unit> commanders = new ArrayList<>(testArmy.getCommanderUnits());

                assertTrue(commanders.contains(commanderUnit1)
                        && commanders.contains(commanderUnit2));
            }

            @Test
            @DisplayName("getCommanderUnits() excludes other types")
            public void getCommanderUnitsExcludesOthers(){
                Army testArmy = new Army("Test army");
                testArmy.add(new CommanderUnit("CommanderUnit",20));
                InfantryUnit infantryUnit = new InfantryUnit("InfantryUnit",20);
                testArmy.add(infantryUnit);

                ArrayList<Unit> commanders = new ArrayList<>(testArmy.getCommanderUnits());

                assertFalse(commanders.contains(infantryUnit));
            }

            @Test
            @DisplayName("getCommanderUnits() excludes CavalryUnits")
            public void getCommanderUnitsExcludeCavalryUnit(){
                Army testArmy = new Army("Test army");
                testArmy.add(new CommanderUnit("CommanderUnit1",20));
                testArmy.add(new CommanderUnit("CommanderUnit2",20));
                CavalryUnit cavalryUnit = new CavalryUnit("CavalryUnit",20);
                testArmy.add(cavalryUnit);

                ArrayList<Unit> commanders = new ArrayList<>(testArmy.getCommanderUnits());

                assertFalse(commanders.contains(cavalryUnit));
            }
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
        @DisplayName("Equals and hashcode")
        class EqualsAndHashCode{

            @Test
            @DisplayName("Equals returns true for two equal armies")
            public void TwoArmiesEqualEachOther(){
                Unit testUnit = new InfantryUnit("Axeman",20);
                Army testArmy = new Army("Test army");
                Army testArmy2 = new Army("Test army");
                testArmy.add(testUnit);
                testArmy2.add(testUnit);

                assertEquals(testArmy, testArmy2);
            }

            @Test
            @DisplayName("Equals returns false for two unequal armies")
            public void TwoArmiesDoesNotEqualEachOther(){
                Unit testUnit = new InfantryUnit("Axeman",20);
                Army testArmy = new Army("Test army");
                Army testArmy2 = new Army("Test army with different name");
                testArmy.add(testUnit);
                testArmy2.add(testUnit);

                assertNotEquals(testArmy, testArmy2);
            }

            @Test
            @DisplayName("Equal hashCode for two equal armies")
            public void TwoEqualArmiesHaveTheSameHashcode(){
                Unit testUnit = new InfantryUnit("Axeman",20);
                Army testArmy = new Army("Test army");
                Army testArmy2 = new Army("Test army");
                testArmy.add(testUnit);
                testArmy2.add(testUnit);

                if(testArmy.equals(testArmy2)){
                    assertEquals(testArmy.hashCode(),testArmy2.hashCode());
                }
                else fail();
            }
        }
    }
}