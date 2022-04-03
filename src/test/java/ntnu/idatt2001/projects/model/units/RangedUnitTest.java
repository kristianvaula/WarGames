package ntnu.idatt2001.projects.model.units;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class RangedUnitTest {

    @Nested
    @DisplayName("Testing initiation of a new ranged unit")
    class initiationOfObject{

        @Test
        @DisplayName("Constructor initiates object with all parameters")
        public void initiatingWithAllParameters(){
            String name = "TestName";

            RangedUnit testUnit = new RangedUnit(name,20,15,10);

            assertSame(name,testUnit.getName());
        }

        @Test
        @DisplayName("Constructor initiates object without all parameters")
        public void initiatingWithoutAllParameters(){
            String name = "TestName";

            RangedUnit testUnit = new RangedUnit(name,20);

            assertEquals(name,testUnit.getName());
        }

        @Test
        @DisplayName("Constructor throws IllegalArgumentException with negative values")
        public void initiatingWithNegativeHealth(){
            assertThrows(IllegalArgumentException.class, () -> {
                RangedUnit testUnit = new RangedUnit("Name",-100);
            });
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
            RangedUnit testUnit = new RangedUnit("Name",startHealth);
            RangedUnit testUnit1 = new RangedUnit("Name",startHealth);

            testUnit.attack(testUnit1);

            assertTrue(testUnit1.getHealth() < startHealth);
        }

        @Test
        @DisplayName("Health never goes below zero")
        public void healthNeverBelowZero(){
            RangedUnit testUnit = new RangedUnit("Name",20);
            RangedUnit testUnit1 = new RangedUnit("Name",20);

            while(testUnit1.getHealth() > 0) {
                testUnit.attack(testUnit1);
            }

            assertEquals(0, testUnit1.getHealth());
        }
    }

    @Nested
    @DisplayName("Testing bonus returns")
    public class correctBonusReturns{

        @Test
        @DisplayName("Ranged has 3 attack bonus and 6 resistance before being attacked")
        public void getCorrectStartBonuses(){
            RangedUnit testUnit = new RangedUnit("Name",20);

            assertTrue(testUnit.getAttackBonus() == RangedUnit.RANGED_ATTACK_BONUS
                        && testUnit.getResistBonus() == RangedUnit.RANGED_MAXIMUM_RANGE_BONUS);
        }

        @Test
        @DisplayName("Ranged has less resistance bonus after being attacked")
        public void getCorrectBonusAfterAttacked(){
            RangedUnit testUnit = new RangedUnit("Name",20);
            RangedUnit testUnit1 = new RangedUnit("Name",20);

            int resistBonusBeforeAttacked = testUnit1.getResistBonus();
            testUnit.attack(testUnit1);

            assertTrue(testUnit1.getResistBonus() < resistBonusBeforeAttacked);
        }
    }

}