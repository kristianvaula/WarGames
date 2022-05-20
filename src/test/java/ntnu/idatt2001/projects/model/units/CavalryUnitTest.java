package ntnu.idatt2001.projects.model.units;

import ntnu.idatt2001.projects.model.simulation.Terrain;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class CavalryUnitTest {
    String name = "TestName";
    private static final Terrain FOREST = Terrain.FOREST;
    private static final Terrain PLAINS = Terrain.PLAINS;
    private static final Terrain HILL = Terrain.HILL;
    //Maximum value for attack,health,armor stats
    private static final int MAXIMUM_STAT_VALUE = 99;

    @Nested
    @DisplayName("Testing initiation of a new cavalry unit")
    class initiationOfObject{

        @Test
        @DisplayName("Constructor initiates object with all parameters")
        public void initiatingWithAllParameters(){
            int health = 20;
            int attack = 15;
            int armor = 10;

            CavalryUnit testUnit = new CavalryUnit(name,health,attack,armor);

            assertTrue(name.equals(testUnit.getName()) && attack == testUnit.getAttack());
        }

        @Test
        @DisplayName("Constructor initiates object without all parameters")
        public void initiatingWithoutAllParameters(){
            CavalryUnit testUnit = new CavalryUnit(name,20);

            assertEquals(name,testUnit.getName());
        }

        @Test
        @DisplayName("Constructor throws IllegalArgumentException with negative values")
        public void initiatingWithNegativeHealth(){
            assertThrows(IllegalArgumentException.class, () -> {
                CavalryUnit testUnit = new CavalryUnit(name,-100);
            });
        }

        @Test
        @DisplayName("Constructor will change values that are too high")
        public void initiatingWithTooHighValueChangesValueToMax(){
            CavalryUnit testUnit = new CavalryUnit(name,150);

            assertEquals(testUnit.getHealth(),MAXIMUM_STAT_VALUE);
        }

        @Test
        @DisplayName("Constructor throws IllegalArgumentException with empty name")
        public void initiatingWithEmptyName(){
            assertThrows(IllegalArgumentException.class, () -> {
                CavalryUnit testUnit = new CavalryUnit("",100);
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
            CavalryUnit testUnit = new CavalryUnit(name,startHealth);
            CavalryUnit testUnit1 = new CavalryUnit(name,startHealth);

            testUnit.attack(testUnit1,HILL);

            assertTrue(testUnit1.getHealth() < startHealth);
        }

        @Test
        @DisplayName("Health never goes below zero")
        public void healthNeverBelowZero(){
            CavalryUnit testUnit = new CavalryUnit(name,20);
            CavalryUnit testUnit1 = new CavalryUnit(name,20);

            while(testUnit1.getHealth() > 0) {
                testUnit.attack(testUnit1,HILL);
            }

            assertEquals(0, testUnit1.getHealth());
        }

        @Test
        @DisplayName("Attack method decreases armor value")
        public void attackMethodDecreasesArmor(){
            int startArmor = 20;
            CavalryUnit testUnit = new CavalryUnit(name,15,15,startArmor);
            CavalryUnit testUnit1 = new CavalryUnit(name,15,15,startArmor);

            testUnit.attack(testUnit1,PLAINS);

            assertTrue(testUnit1.getArmor() < startArmor);
        }
    }

    @Nested
    @DisplayName("Testing bonus returns")
    public class correctBonusReturns{

        @Test
        @DisplayName("Cavalry has correct attack and  resistance bonuses before being attacked")
        public void getCorrectStartBonuses(){
            CavalryUnit testUnit = new CavalryUnit(name,20);

            assertTrue(testUnit.getAttackBonus(HILL) == CavalryUnit.CAVALRY_CHARGE_ATTACK_BONUS + CavalryUnit.CAVALRY_DEFAULT_ATTACK_BONUS
                        && testUnit.getResistBonus(HILL) == CavalryUnit.CAVALRY_RESISTANCE_BONUS);
        }

        @Test
        @DisplayName("Cavalry has less attack bonus after attacking an opponent")
        public void getCorrectBonusAfterAttacked(){
            CavalryUnit testUnit = new CavalryUnit(name,20);
            CavalryUnit testUnit1 = new CavalryUnit(name,20);

            int attackBonusBeforeAttacking = testUnit.getAttackBonus(PLAINS);
            testUnit.attack(testUnit1,HILL);

            assertTrue(testUnit.getAttackBonus(PLAINS) < attackBonusBeforeAttacking);
        }

        @Test
        @DisplayName("Cavalry has maximum attack bonus even after being attacked")
        public void getMaxBonusAfterBeingAttacked(){
            int hillMaxBonus = CavalryUnit.CAVALRY_CHARGE_ATTACK_BONUS + CavalryUnit.CAVALRY_DEFAULT_ATTACK_BONUS;
            CavalryUnit testUnit = new CavalryUnit(name,20);
            CavalryUnit testUnit1 = new CavalryUnit(name,20);

            testUnit1.attack(testUnit,HILL);

            assertEquals(hillMaxBonus, testUnit.getAttackBonus(HILL));
        }

        @Test
        @DisplayName("Cavalry has improved attack bonus on plains")
        public void getImprovedBonusOnPlains(){
            int defaultCavalryCharge = CavalryUnit.CAVALRY_DEFAULT_ATTACK_BONUS
                                    + CavalryUnit.CAVALRY_CHARGE_ATTACK_BONUS;

            CavalryUnit testUnit = new CavalryUnit(name,20);

            assertTrue(testUnit.getAttackBonus(PLAINS) > defaultCavalryCharge
                    && testUnit.getResistBonus(PLAINS) == CavalryUnit.CAVALRY_RESISTANCE_BONUS);
        }

        @Test
        @DisplayName("Cavalry has less resistance bonus in a forest")
        public void getLessResistanceInForest(){
            CavalryUnit testUnit = new CavalryUnit(name,20);

            assertTrue(testUnit.getResistBonus(FOREST) < testUnit.getResistBonus(HILL)
                    && testUnit.getResistBonus(FOREST) < testUnit.getResistBonus(PLAINS));
        }
    }

}