package ntnu.idatt2001.projects.model.units;

import ntnu.idatt2001.projects.model.simulation.Terrain;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class RangedUnitTest {
    String name = "TestName";
    private static final Terrain FOREST = Terrain.FOREST;
    private static final Terrain PLAINS = Terrain.PLAINS;
    private static final Terrain HILL = Terrain.HILL;
    //Maximum value for attack,health,armor stats
    private static final int MAXIMUM_STAT_VALUE = 99;

    @Nested
    @DisplayName("Testing initiation of a new ranged unit")
    class initiationOfObject{

        @Test
        @DisplayName("Constructor initiates object with all parameters")
        public void initiatingWithAllParameters(){
            RangedUnit testUnit = new RangedUnit(name,20,15,10);

            assertSame(name,testUnit.getName());
        }

        @Test
        @DisplayName("Constructor initiates object without all parameters")
        public void initiatingWithoutAllParameters(){
            RangedUnit testUnit = new RangedUnit(name,20);

            assertEquals(name,testUnit.getName());
        }

        @Test
        @DisplayName("Constructor throws IllegalArgumentException with negative values")
        public void initiatingWithNegativeHealth(){
            assertThrows(IllegalArgumentException.class, () -> {
                RangedUnit testUnit = new RangedUnit(name,-100);
            });
        }

        @Test
        @DisplayName("Constructor will change values that are too high")
        public void initiatingWithTooHighValueChangesValueToMax(){
            RangedUnit testUnit = new RangedUnit(name,150);

            assertEquals(testUnit.getHealth(),MAXIMUM_STAT_VALUE);
        }

        @Test
        @DisplayName("Constructor throws IllegalArgumentException with empty name")
        public void initiatingWithEmptyName(){
            assertThrows(IllegalArgumentException.class, () -> {
                RangedUnit testUnit = new RangedUnit("",100);
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
            RangedUnit testUnit = new RangedUnit(name,startHealth);
            RangedUnit testUnit1 = new RangedUnit(name,startHealth);

            testUnit.attack(testUnit1,PLAINS);

            assertTrue(testUnit1.getHealth() < startHealth);
        }

        @Test
        @DisplayName("Health never goes below zero")
        public void healthNeverBelowZero(){
            RangedUnit testUnit = new RangedUnit(name,20);
            RangedUnit testUnit1 = new RangedUnit(name,20);

            while(testUnit1.getHealth() > 0) {
                testUnit.attack(testUnit1,PLAINS);
            }

            assertEquals(0, testUnit1.getHealth());
        }

        @Test
        @DisplayName("Attack method decreases armor value")
        public void attackMethodDecreasesArmor(){
            int startArmor = 20;
            RangedUnit testUnit = new RangedUnit(name,15,15,startArmor);
            RangedUnit testUnit1 = new RangedUnit(name,15,15,startArmor);

            testUnit.attack(testUnit1,PLAINS);

            assertTrue(testUnit1.getArmor() < startArmor);
        }
    }

    @Nested
    @DisplayName("Testing bonus returns")
    public class correctBonusReturns{

        @Test
        @DisplayName("Ranged has coorect attack and resistance bonus before being attacked (plains)")
        public void getCorrectStartBonusesOnPlains(){
            RangedUnit testUnit = new RangedUnit(name,20);

            assertTrue(testUnit.getAttackBonus(PLAINS) == RangedUnit.RANGED_ATTACK_BONUS
                        && testUnit.getResistBonus(PLAINS) == RangedUnit.RANGED_MAXIMUM_RESISTANCE_BONUS);
        }

        @Test
        @DisplayName("Ranged has less resistance bonus after being attacked")
        public void getCorrectBonusAfterAttacked(){
            RangedUnit testUnit = new RangedUnit("Name",20);
            RangedUnit testUnit1 = new RangedUnit("Name",20);

            int resistBonusBeforeAttacked = testUnit1.getResistBonus(PLAINS);
            testUnit.attack(testUnit1,PLAINS);

            assertTrue(testUnit1.getResistBonus(PLAINS) < resistBonusBeforeAttacked);
        }

        @Test
        @DisplayName("Ranged has improved attack bonus on a hill")
        public void getImprovedBonusOnHill(){
            RangedUnit testUnit = new RangedUnit(name,20);

            assertTrue(testUnit.getAttackBonus(HILL) > RangedUnit.RANGED_ATTACK_BONUS
                    && testUnit.getResistBonus(HILL) == RangedUnit.RANGED_MAXIMUM_RESISTANCE_BONUS);
        }

        @Test
        @DisplayName("Ranged has less attack bonus in a forest")
        public void getLessBonusInForest(){
            RangedUnit testUnit = new RangedUnit(name,20);

            assertTrue(testUnit.getAttackBonus(FOREST) < RangedUnit.RANGED_ATTACK_BONUS
                    && testUnit.getResistBonus(FOREST) == RangedUnit.RANGED_MAXIMUM_RESISTANCE_BONUS);
        }
    }

}