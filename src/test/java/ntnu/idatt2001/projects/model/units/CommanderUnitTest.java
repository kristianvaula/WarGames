package ntnu.idatt2001.projects.model.units;

import ntnu.idatt2001.projects.model.simulation.Terrain;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class CommanderUnitTest {
    String name = "TestName";
    private static final Terrain FOREST = Terrain.FOREST;
    private static final Terrain PLAINS = Terrain.PLAINS;
    private static final Terrain HILL = Terrain.HILL;

    @Nested
    @DisplayName("Testing initiation of a new commander unit")
    class initiationOfObject{

        @Test
        @DisplayName("Constructor initiates object with all parameters")
        public void initiatingWithAllParameters(){
            CommanderUnit testUnit = new CommanderUnit(name,20,15,10);

            assertEquals(name,testUnit.getName());
        }

        @Test
        @DisplayName("Constructor initiates object without all parameters")
        public void initiatingWithoutAllParameters(){
            int health = 20;

            CommanderUnit testUnit = new CommanderUnit(name,health);

            assertEquals(health,testUnit.getHealth());
        }

        @Test
        @DisplayName("Constructor throws IllegalArgumentException with negative values")
        public void initiatingWithNegativeHealth(){
            assertThrows(IllegalArgumentException.class, () -> {
                CommanderUnit testUnit = new CommanderUnit(name,-100);
            });
        }

        @Test
        @DisplayName("Constructor throws IllegalArgumentException with empty name")
        public void initiatingWithEmptyName(){
            assertThrows(IllegalArgumentException.class, () -> {
                CommanderUnit testUnit = new CommanderUnit("",100);
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
            CommanderUnit testUnit = new CommanderUnit(name,startHealth);
            CommanderUnit testUnit1 = new CommanderUnit(name,startHealth);

            testUnit.attack(testUnit1,HILL);

            assertTrue(testUnit1.getHealth() < startHealth);
        }

        @Test
        @DisplayName("Health never goes below zero")
        public void healthNeverBelowZero(){
            CommanderUnit testUnit = new CommanderUnit(name,20);
            CommanderUnit testUnit1 = new CommanderUnit(name,20);

            while(testUnit1.getHealth() > 0) {
                testUnit.attack(testUnit1,HILL);
                System.out.println(testUnit1.getHealth());
            }

            assertEquals(0, testUnit1.getHealth());
        }
    }

    @Nested
    @DisplayName("Testing bonus returns")
    public class correctBonusReturns{

        @Test
        @DisplayName("CommanderUnit has correct attack and  resistance bonuses before being attacked")
        public void getCorrectStartBonuses(){
            CommanderUnit testUnit = new CommanderUnit(name,20);

            assertTrue(testUnit.getAttackBonus(HILL) == CommanderUnit.CAVALRY_CHARGE_ATTACK_BONUS + CommanderUnit.CAVALRY_DEFAULT_ATTACK_BONUS
                    && testUnit.getResistBonus(HILL) == CommanderUnit.CAVALRY_RESISTANCE_BONUS);
        }

        @Test
        @DisplayName("CommanderUnit has less attack bonus after attacking an opponent")
        public void getCorrectBonusAfterAttacked(){
            CommanderUnit testUnit = new CommanderUnit(name,20);
            CommanderUnit testUnit1 = new CommanderUnit(name,20);

            int attackBonusBeforeAttacking = testUnit.getAttackBonus(PLAINS);
            testUnit.attack(testUnit1,HILL);

            assertTrue(testUnit.getAttackBonus(PLAINS) < attackBonusBeforeAttacking);
        }

        @Test
        @DisplayName("CommanderUnit has maximum attack bonus even after being attacked")
        public void getMaxBonusAfterBeingAttacked(){
            int hillMaxBonus = CommanderUnit.CAVALRY_CHARGE_ATTACK_BONUS + CommanderUnit.CAVALRY_DEFAULT_ATTACK_BONUS;
            CommanderUnit testUnit = new CommanderUnit(name,20);
            CommanderUnit testUnit1 = new CommanderUnit(name,20);

            testUnit1.attack(testUnit,HILL);

            assertEquals(hillMaxBonus, testUnit.getAttackBonus(HILL));
        }

        @Test
        @DisplayName("CommanderUnit has improved attack bonus on plains")
        public void getImprovedBonusOnPlains(){
            int defaultCavalryCharge = CommanderUnit.CAVALRY_DEFAULT_ATTACK_BONUS
                    + CommanderUnit.CAVALRY_CHARGE_ATTACK_BONUS;

            CommanderUnit testUnit = new CommanderUnit(name,20);

            assertTrue(testUnit.getAttackBonus(PLAINS) > defaultCavalryCharge
                    && testUnit.getResistBonus(PLAINS) == CommanderUnit.CAVALRY_RESISTANCE_BONUS);
        }

        @Test
        @DisplayName("CommanderUnit has less resistance bonus in a forest")
        public void getLessResistanceInForest(){
            CommanderUnit testUnit = new CommanderUnit(name,20);

            assertTrue(testUnit.getResistBonus(FOREST) < testUnit.getResistBonus(HILL)
                    && testUnit.getResistBonus(FOREST) < testUnit.getResistBonus(PLAINS));
        }
    }

}