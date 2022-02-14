package ntnu.idatt2001.projects.unitstest;


import ntnu.idatt2001.projects.units.RangedUnit;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class RangedUnitTest {

    @Nested
    class initiationOfObject{
        @Test
        @DisplayName("Constructor initiates object with all parameters")
        public void initiatingWithAllParameters(){
            RangedUnit testUnit = new RangedUnit("Name",100,14,10);
            assertTrue(testUnit instanceof RangedUnit);
        }

        @Test
        @DisplayName("Constructor initiates object without all parameters")
        public void initiatingWithoutAllParameters(){
            RangedUnit testUnit = new RangedUnit("Name",100);
            assertTrue(testUnit instanceof RangedUnit);
        }
    }

    @Nested
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
                System.out.println(testUnit1.getHealth());
            }
            assertTrue(testUnit1.getHealth() == 0);
        }
    }

    @Nested
    public class correctBonusReturns{
        @Test
        @DisplayName("Ranged has 3 attack bonus and 6 resistance before being attacked")
        public void getCorrectStartBonuses(){
            RangedUnit testUnit = new RangedUnit("Name",20);
            assertTrue(testUnit.getAttackBonus() == 3 && testUnit.getResistBonus() == 6);
        }

        @Test
        @DisplayName("Ranged has less resistance bonus after being attacked")
        public void getCorrectBonusAfterAttacked(){
            RangedUnit testUnit = new RangedUnit("Name",20);
            RangedUnit testUnit1 = new RangedUnit("Name",20);
            testUnit.attack(testUnit1);
            assertTrue(testUnit.getResistBonus() > testUnit1.getResistBonus());
        }
    }

}