package ntnu.idatt2001.projects.units;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class CommanderUnitTest {

    @Nested
    @DisplayName("Testing initiation of a new commander unit")
    class initiationOfObject{

        @Test
        @DisplayName("Constructor initiates object with all parameters")
        public void initiatingWithAllParameters(){
            String name = "TestName";
            int health = 20;
            int attack = 15;
            int armor = 10;
            CommanderUnit testUnit = new CommanderUnit(name,health,attack,armor);
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
            CommanderUnit testUnit = new CommanderUnit(name,health);
            assertSame(name,testUnit.getName());
            assertSame(health,testUnit.getHealth());
        }

        @Test
        @DisplayName("Constructor throws IllegalArgumentException with negative values")
        public void initiatingWithNegativeHealth(){
            try{
                RangedUnit testUnit = new RangedUnit("Name",-100);
                fail("Constructor did not throw exception");
            }catch (IllegalArgumentException e){
                assertEquals(e.getMessage(),"Inputs cannot be negative or zero");
            }
        }

        @Test
        @DisplayName("Constructor throws IllegalArgumentException with empty name")
        public void initiatingWithEmptyName(){
            try{
                RangedUnit testUnit = new RangedUnit("",100);
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
            CommanderUnit testUnit = new CommanderUnit("Name",startHealth);
            CommanderUnit testUnit1 = new CommanderUnit("Name",startHealth);

            testUnit.attack(testUnit1);
            assertTrue(testUnit1.getHealth() < startHealth);
        }

        @Test
        @DisplayName("Health never goes below zero")
        public void healthNeverBelowZero(){
            CommanderUnit testUnit = new CommanderUnit("Name",20);
            CommanderUnit testUnit1 = new CommanderUnit("Name",20);

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
        @DisplayName("Ranged has 6 attack bonus and 1 resistance before being attacked")
        public void getCorrectStartBonuses(){
            CommanderUnit testUnit = new CommanderUnit("Name",20);
            assertTrue(testUnit.getAttackBonus() == CavalryUnit.CAVALRY_CHARGE_ATTACK_BONUS
                        && testUnit.getResistBonus() == CavalryUnit.CAVALRY_RESISTANCE_BONUS);
        }

        @Test
        @DisplayName("Ranged has less attack bonus after being attacked")
        public void getCorrectBonusAfterAttacked(){
            CommanderUnit testUnit = new CommanderUnit("Name",20);
            CommanderUnit testUnit1 = new CommanderUnit("Name",20);
            testUnit.attack(testUnit1);
            assertTrue(testUnit.getAttackBonus() > testUnit1.getResistBonus());
        }
    }

}