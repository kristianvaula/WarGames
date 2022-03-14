package ntnu.idatt2001.projects.units;

import ntnu.idatt2001.projects.simulation.Battle;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class CavalryUnitTest {

    @Nested
    @DisplayName("Testing initiation of a new cavalry unit")
    class initiationOfObject{

        @Test
        @DisplayName("Constructor initiates object with all parameters")
        public void initiatingWithAllParameters(){
            String name = "TestName";
            int health = 20;
            int attack = 15;
            int armor = 10;
            CavalryUnit testUnit = new CavalryUnit(name,health,attack,armor);
            assertSame(name,testUnit.getName());
            assertSame(health,testUnit.getHealth());
            assertSame(attack,testUnit.getAttack());
            assertSame(armor,testUnit.getArmor());
        }

        @Test
        @DisplayName("Constructor initiates object without all parameters")
        public void initiatingWithoutAllParameters(){
            String name = "TestName";

            CavalryUnit testUnit = new CavalryUnit(name,20);

            assertSame(name,testUnit.getName());
        }

        @Test
        @DisplayName("Constructor throws IllegalArgumentException with negative values")
        public void initiatingWithNegativeHealth(){
            assertThrows(IllegalArgumentException.class, () -> {
                CavalryUnit testUnit = new CavalryUnit("Name",-100);
            });
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
            CavalryUnit testUnit = new CavalryUnit("Name",startHealth);
            CavalryUnit testUnit1 = new CavalryUnit("Name",startHealth);

            testUnit.attack(testUnit1);

            assertTrue(testUnit1.getHealth() < startHealth);
        }

        @Test
        @DisplayName("Health never goes below zero")
        public void healthNeverBelowZero(){
            CavalryUnit testUnit = new CavalryUnit("Name",20);
            CavalryUnit testUnit1 = new CavalryUnit("Name",20);

            while(testUnit1.getHealth() > 0) {
                testUnit.attack(testUnit1);
                System.out.println(testUnit1.getHealth());
            }

            assertEquals(0, testUnit1.getHealth());
        }
    }

    @Nested
    @DisplayName("Testing bonus returns")
    public class correctBonusReturns{

        @Test
        @DisplayName("Cavalry has 6 attack bonus and 1 resistance before being attacked")
        public void getCorrectStartBonuses(){
            CavalryUnit testUnit = new CavalryUnit("Name",20);

            assertTrue(testUnit.getAttackBonus() == CavalryUnit.CAVALRY_CHARGE_ATTACK_BONUS
                        && testUnit.getResistBonus() == CavalryUnit.CAVALRY_RESISTANCE_BONUS);
        }

        @Test
        @DisplayName("Cavalry has less attack bonus after attacking an opponent")
        public void getCorrectBonusAfterAttacked(){
            CavalryUnit testUnit = new CavalryUnit("Name",20);
            CavalryUnit testUnit1 = new CavalryUnit("Name",20);

            int attackBonusBeforeAttacking = testUnit.getAttackBonus();
            testUnit.attack(testUnit1);

            assertTrue(testUnit.getAttackBonus() < attackBonusBeforeAttacking);
        }

        @Test
        @DisplayName("Cavalry has maximum attack bonus even after being attacked")
        public void getMaxBonusAfterBeingAttacked(){
            CavalryUnit testUnit = new CavalryUnit("Name",20);
            CavalryUnit testUnit1 = new CavalryUnit("Name",20);

            testUnit1.attack(testUnit);

            assertEquals(CavalryUnit.CAVALRY_CHARGE_ATTACK_BONUS, testUnit.getAttackBonus());
        }
    }

}