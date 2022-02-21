package ntnu.idatt2001.projects.units;


import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class CavalryUnitTest {

    @Nested
    class initiationOfObject{
        @Test
        @DisplayName("Constructor initiates object with all parameters")
        public void initiatingWithAllParameters(){
            CavalryUnit testUnit = new CavalryUnit("Name",100,14,10);
            assertTrue(testUnit instanceof CavalryUnit);
        }

        @Test
        @DisplayName("Constructor initiates object without all parameters")
        public void initiatingWithoutAllParameters(){
            CavalryUnit testUnit = new CavalryUnit("Name",100);
            assertTrue(testUnit instanceof CavalryUnit);
        }

        @Test
        @DisplayName("Constructor throws IllegalArgumentException with negative values")
        public void initiatingWithNegativeHealth(){
            try{
                CavalryUnit testUnit = new CavalryUnit("Name",-100);
                fail("Constructor did not throw exception");
            }catch (IllegalArgumentException e){
                assertEquals(e.getMessage(),"Inputs cannot be negative or zero");
            }
        }

        @Test
        @DisplayName("Constructor throws IllegalArgumentException with empty name")
        public void initiatingWithEmptyName(){
            try{
                CavalryUnit testUnit = new CavalryUnit("",100);
                fail("Constructor did not throw exception");
            }catch (IllegalArgumentException e){
                assertEquals(e.getMessage(),"Name cannot be empty");
            }
        }
    }

    @Nested
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
            assertTrue(testUnit1.getHealth() == 0);
        }
    }

    @Nested
    public class correctBonusReturns{
        @Test
        @DisplayName("Cavalry has 6 attack bonus and 1 resistance before being attacked")
        public void getCorrectStartBonuses(){
            CavalryUnit testUnit = new CavalryUnit("Name",20);
            assertTrue(testUnit.getAttackBonus() == CavalryUnit.CAVALRY_CHARGE_ATTACK_BONUS
                        && testUnit.getResistBonus() == CavalryUnit.CAVALRY_RESISTANCE_BONUS);
        }

        @Test
        @DisplayName("Cavalry has less attack bonus after being attacked")
        public void getCorrectBonusAfterAttacked(){
            CavalryUnit testUnit = new CavalryUnit("Name",20);
            CavalryUnit testUnit1 = new CavalryUnit("Name",20);
            testUnit.attack(testUnit1);
            assertTrue(testUnit.getAttackBonus() > testUnit1.getResistBonus());
        }
    }

}