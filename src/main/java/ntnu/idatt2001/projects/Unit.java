package ntnu.idatt2001.projects;

/**
 * Unit is an abstract class representing the shared characteristics
 * off all types of units.
 *
 * @author Kristian Vaula Jensen
 * @version 2022.02.09
 */
public abstract class Unit {
    // The units descriptive name
    private String name;
    // The value representing the units health
    private int health;
    // The value of attacking damage
    private int attack;
    // The value of defensive resistance
    private int armor;

    /**
     * Works as the blueprint for creating a Unit object
     *
     * @param name   The unit descriptive name
     * @param health The value of the units health
     * @param attack The attack value of the unit
     * @param armor  The defensive resistance of the unit
     */
    public Unit(String name, int health, int attack, int armor) {
        this.name = name;
        this.health = health;
        this.attack = attack;
        this.armor = armor;
    }

    /**
     * Attack an opponent. Health impact on opponent equals
     * the difference between the units attack-force and the
     * opponents defensive resistance.
     *
     * @param opponent The opponent unit that gets attacked
     */
    public void attack(Unit opponent){
        //attack-force equals attack value + attack bonus.
        int attForce = this.getAttack() + this.getAttackBonus();
        //defensive resistance equals armor + resistance bonus.
        int oppDefense = opponent.getArmor() + opponent.getResistBonus();

        //Ensures that an opponent cannot gain health if resistance is greater than attack
        if ((attForce-oppDefense) >= 0){
            int attCalc = opponent.getHealth() - attForce + oppDefense;
            opponent.setHealth(attCalc);
        }
    }

    /**
     * Gets the unit descriptive name
     *
     * @return the unit description
     */
    public String getName() {
        return name;
    }

    /**
     * Gets unit health
     *
     * @return The health value
     */
    public int getHealth() {
        return health;
    }

    /**
     * Gets the unit attack value
     *
     * @return The attack value
     */
    public int getAttack() {
        return attack;
    }

    /**
     * Gets the unit armor value
     *
     * @return The armor value
     */
    public int getArmor() {
        return armor;
    }

    /**
     * Sets the unit health value. Cannot
     * be less than zero.
     *
     * @param health The health value
     */
    public void setHealth(int health) {
        if(health <= 0) this.health = 0;
        this.health = health;
    }

    /**
     * Returns a text-representation of a
     * unit object. Contains all information
     * about the unit.
     *
     * @return The unit represented as a String
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("| ");
        sb.append(name).append(" | HP: ");
        sb.append(health).append(" | ATT: ");
        sb.append(attack).append(" | DEF: ");
        sb.append(armor).append(" |");
        return sb.toString();
    }

    /**
     * Abstract method for unit subclasses to get
     * attack bonus
     * TODO add return javaDoc for abstract methods?
     * @return
     */
    public abstract int getAttackBonus();

    /**
     * Abstract method for unit sunclasses to get
     * resistance bonus
     * TODO add return javaDoc for abstract methods?
     * @return
     */
    public abstract int getResistBonus();
}