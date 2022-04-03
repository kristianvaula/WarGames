package ntnu.idatt2001.projects.model.units;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class InfantryUnitTest {

    @Nested
    @DisplayName("Testing initiation of a new infantry unit")
    class initiationOfObject{

        @Test
        @DisplayName("Constructor initiates object with all parameters")
        public void initiatingWithAllParameters(){
            String name = "TestName";

            InfantryUnit testUnit = new InfantryUnit(name,20,15,10);

            assertEquals(name,testUnit.getName());
        }

        @Test
        @DisplayName("Constructor initiates object without all parameters")
        public void initiatingWithoutAllParameters(){
            String name = "TestName";
            int health = 20;

            InfantryUnit testUnit = new InfantryUnit(name,health);

            assertEquals(health,testUnit.getHealth());
        }

        @Test
        @DisplayName("Constructor throws IllegalArgumentException with negative values")
        public void initiatingWithNegativeHealth(){
            assertThrows(IllegalArgumentException.class, () -> {
                InfantryUnit testUnit = new InfantryUnit("Name",-100);
            });
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
            InfantryUnit testUnit = new InfantryUnit("Name",startHealth);
            InfantryUnit testUnit1 = new InfantryUnit("Name",startHealth);

            testUnit.attack(testUnit1);

            assertTrue(testUnit1.getHealth() < startHealth);
        }

        @Test
        @DisplayName("Health never goes below zero")
        public void healthNeverBelowZero(){
            InfantryUnit testUnit = new InfantryUnit("Name",20);
            InfantryUnit testUnit1 = new InfantryUnit("Name",20);

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
        @DisplayName("Infantry has 2 attack bonus and 1 resistance")
        public void getCorrectBonuses(){
            InfantryUnit testUnit = new InfantryUnit("Name",20);

            assertTrue(testUnit.getAttackBonus() == InfantryUnit.INFANTRY_ATTACK_BONUS
                        && testUnit.getResistBonus() == InfantryUnit.INFANTRY_RESISTANCE_BONUS);
        }
    }

}
