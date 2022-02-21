package ntnu.idatt2001.projects.units;

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
            int health = 20;
            int attack = 15;
            int armor = 10;
            InfantryUnit testUnit = new InfantryUnit(name,health,attack,armor);
            assertSame(name,testUnit.getName());
            assertSame(health,testUnit.getHealth());
            assertSame(attack,testUnit.getAttack());
            assertSame(armor,testUnit.getArmor());
        }

        @Test
        @DisplayName("Constructor initiates object without all parameters")
        public void initiatingWithoutAllParameters(){
            String name = "TestName";
            int health = 20;
            InfantryUnit testUnit = new InfantryUnit(name,health);
            assertSame(name,testUnit.getName());
            assertSame(health,testUnit.getHealth());
        }

        @Test
        @DisplayName("Constructor throws IllegalArgumentException with negative values")
        public void initiatingWithNegativeHealth(){
            try{
                InfantryUnit testUnit = new InfantryUnit("Name",-100);
                fail("Constructor did not throw exception");
            }catch (IllegalArgumentException e){
                assertEquals(e.getMessage(),"Inputs cannot be negative or zero");
            }
        }

        @Test
        @DisplayName("Constructor throws IllegalArgumentException with empty name")
        public void initiatingWithEmptyName(){
            try{
                InfantryUnit testUnit = new InfantryUnit("",100);
                fail("Constructor did not throw exception");
            }catch (IllegalArgumentException e){
                assertEquals(e.getMessage(),"Name cannot be empty");
            }
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
            assertTrue(testUnit1.getHealth() == 0);
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
