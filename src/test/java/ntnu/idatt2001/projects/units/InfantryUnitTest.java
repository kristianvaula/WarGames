package ntnu.idatt2001.projects.units;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class InfantryUnitTest {

    @Nested
    class initiationOfObject{
        @Test
        public void initiatingWithAllParameters(){
            InfantryUnit testUnit = new InfantryUnit("Name",100,14,10);
            assertTrue(testUnit instanceof InfantryUnit);
        }

        @Test
        public void initiatingWithoutAllParameters(){
            InfantryUnit testUnit = new InfantryUnit("Name",100);
            assertTrue(testUnit instanceof InfantryUnit);
        }
    }

    @Nested
    public class attackMethod{
        @Test
        public void attackMethodDecreasesHealth(){
            int startHealth = 20;
            InfantryUnit testUnit = new InfantryUnit("Name",startHealth);
            InfantryUnit testUnit1 = new InfantryUnit("Name",startHealth);

            testUnit.attack(testUnit1);
            assertTrue(testUnit1.getHealth() < startHealth);
        }

        @Test
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
    public class correctBonusReturns{
        @Test
        public void getCorrectBonuses(){
            InfantryUnit testUnit = new InfantryUnit("Name",20);
            assertTrue(testUnit.getAttackBonus() == InfantryUnit.INFANTRY_ATTACK_BONUS
                        && testUnit.getResistBonus() == InfantryUnit.INFANTRY_RESISTANCE_BONUS);
        }
    }

}
