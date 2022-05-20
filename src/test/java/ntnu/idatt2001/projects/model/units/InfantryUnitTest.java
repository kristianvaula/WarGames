package ntnu.idatt2001.projects.model.units;

import ntnu.idatt2001.projects.model.simulation.Terrain;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class InfantryUnitTest {
    private static final String NAME = "TestName";
    private static final Terrain FOREST = Terrain.FOREST;
    private static final Terrain PLAINS = Terrain.PLAINS;
    private static final Terrain HILL = Terrain.HILL;
    //Maximum value for attack,health,armor stats
    private static final int MAXIMUM_STAT_VALUE = 99;

    @Nested
    @DisplayName("Testing initiation of a new infantry unit")
    class initiationOfObject{

        @Test
        @DisplayName("Constructor initiates object with all parameters")
        public void initiatingWithAllParameters(){
            InfantryUnit testUnit = new InfantryUnit(NAME,20,15,10);

            assertEquals(NAME,testUnit.getName());
        }

        @Test
        @DisplayName("Constructor initiates object without all parameters")
        public void initiatingWithoutAllParameters(){
            int health = 20;

            InfantryUnit testUnit = new InfantryUnit(NAME,health);

            assertEquals(health,testUnit.getHealth());
        }

        @Test
        @DisplayName("Constructor throws IllegalArgumentException with negative values")
        public void initiatingWithNegativeHealth(){
            assertThrows(IllegalArgumentException.class, () -> {
                InfantryUnit testUnit = new InfantryUnit(NAME,-100);
            });
        }

        @Test
        @DisplayName("Constructor will change values that are too high")
        public void initiatingWithTooHighValueChangesValueToMax(){
            InfantryUnit testUnit = new InfantryUnit(NAME,150);

            assertEquals(testUnit.getHealth(),MAXIMUM_STAT_VALUE);
        }

        @Test
        @DisplayName("Constructor throws IllegalArgumentException with empty name")
        public void initiatingWithEmptyName(){
            assertThrows(IllegalArgumentException.class, () -> {
                InfantryUnit testUnit = new InfantryUnit("",100);
            });
        }
    }

    @Nested
    @DisplayName("Testing attack method")
    public class attackMethod{

        @Test
        @DisplayName("Attack method decreases health value")

        public void attackMethodDecreasesHealth(){
            int startHealth = 20;
            InfantryUnit testUnit = new InfantryUnit(NAME,startHealth);
            InfantryUnit testUnit1 = new InfantryUnit(NAME,startHealth);

            testUnit.attack(testUnit1,FOREST);

            assertTrue(testUnit1.getHealth() < startHealth);
        }

        @Test
        @DisplayName("Health never goes below zero")
        public void healthNeverBelowZero(){
            InfantryUnit testUnit = new InfantryUnit(NAME,20);
            InfantryUnit testUnit1 = new InfantryUnit(NAME,20);

            while(testUnit1.getHealth() > 0) {
                testUnit.attack(testUnit1,FOREST);
            }

            assertEquals(0, testUnit1.getHealth());
        }

        @Test
        @DisplayName("Attack method decreases armor value")
        public void attackMethodDecreasesArmor(){
            int startArmor = 20;
            InfantryUnit testUnit = new InfantryUnit(NAME,15,15,startArmor);
            InfantryUnit testUnit1 = new InfantryUnit(NAME,15,15,startArmor);

            testUnit.attack(testUnit1,PLAINS);

            assertTrue(testUnit1.getArmor() < startArmor);
        }
    }

    @Nested
    @DisplayName("Testing bonus returns")
    public class correctBonusReturns{

        @Test
        @DisplayName("Infantry has 2 attack bonus and 1 resistance on plains")
        public void getCorrectBonusesOnPlains(){
            InfantryUnit testUnit = new InfantryUnit(NAME,20);

            assertTrue(testUnit.getAttackBonus(PLAINS) == InfantryUnit.INFANTRY_ATTACK_BONUS
                        && testUnit.getResistBonus(PLAINS) == InfantryUnit.INFANTRY_RESISTANCE_BONUS);
        }

        @Test
        @DisplayName("Infantry has 2 attack bonus and 1 resistance on hill")
        public void getCorrectBonusesOnHill(){
            InfantryUnit testUnit = new InfantryUnit(NAME,20);

            assertTrue(testUnit.getAttackBonus(HILL) == InfantryUnit.INFANTRY_ATTACK_BONUS
                    && testUnit.getResistBonus(HILL) == InfantryUnit.INFANTRY_RESISTANCE_BONUS);
        }

        @Test
        @DisplayName("Infantry has more attack bonus and resistance bonus in forest")
        public void getMoreBonusOnForest(){
            InfantryUnit testUnit = new InfantryUnit(NAME,20);

            assertTrue(testUnit.getAttackBonus(FOREST) > InfantryUnit.INFANTRY_ATTACK_BONUS
                    && testUnit.getResistBonus(FOREST) > InfantryUnit.INFANTRY_RESISTANCE_BONUS);
        }
    }

}
