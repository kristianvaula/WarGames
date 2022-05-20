package ntnu.idatt2001.projects.model.units;

import ntnu.idatt2001.projects.model.simulation.Terrain;

import java.util.Objects;

/**
 * Unit is an abstract class representing the shared characteristics
 * off all types of units.
 *
 * @author Kristian Vaula Jensen
 */
public abstract class Unit implements Comparable<Unit>{
    // The units descriptive name
    private String name;
    // The value representing the units health
    private int health;
    // Initial value for units health
    private int initialHealth;
    // The value of attacking damage
    private int attack;
    // The value of defensive resistance
    private int armor;
    // A tag specifies some sort of info of the unit like for example which army he belongs to
    private String tag;

    //Constant used to calculate the wear on defendants armor upon attack
    //Half of the attackers attack value scaled equals wear on armor
    private static final double ARMOR_REDUCTION_SCALAR = 0.25;
    //Maximum value for attack,health,armor stats
    private static final int MAXIMUM_STAT_VALUE = 99;

    /**
     * Abstract superclass that blueprint for creating a Unit object.
     * Contains both abstract methods and general methods that
     * is implemented for all unit subclasses. A unit cannot have
     * a stat above 99. This limit is set to ensure better balance
     * in the battles and correct proportion between units.
     *
     * @param name   The unit descriptive name
     * @param health The value of the units health
     * @param attack The attack value of the unit
     * @param armor  The defensive resistance of the unit
     * @throws IllegalArgumentException If arguments are below or equal zero
     */
    public Unit(String name, int health, int attack, int armor) throws IllegalArgumentException{
        if(health <= 0 || attack <= 0 || armor <= 0) throw new IllegalArgumentException("Inputs cannot be negative or zero");
        if(name.isBlank() || name.isEmpty()) throw new IllegalArgumentException("Name cannot be empty");
        this.name = name;
        this.health = Math.min(health,MAXIMUM_STAT_VALUE);
        this.initialHealth = this.health;
        this.attack = Math.min(attack,MAXIMUM_STAT_VALUE);
        this.armor = Math.min(armor,MAXIMUM_STAT_VALUE);
    }

    /**
     * Attack an opponent. Health impact on opponent equals
     * the difference between the units attack-force and the
     * opponents defensive resistance. The defendant has his
     * armor reduced by a scalar of the attack value
     *
     * @param opponent The opponent unit that gets attacked
     * @param opponentTerrain The terrain on which the defending unit is standing
     */
    public void attack(Unit opponent, Terrain opponentTerrain){
        //attack-force equals attack value + attack bonus.
        int attForce = this.getAttack() + this.getAttackBonus(opponentTerrain);
        //defensive resistance equals armor + resistance bonus.
        int oppDefense = opponent.getArmor() + opponent.getResistBonus(opponentTerrain);
        //Ensure that an opponent cannot gain health if resistance is greater than attack
        if ((attForce-oppDefense) >= 0){
            int attCalculation = opponent.getHealth() - attForce + oppDefense;
            opponent.takeDamage(attCalculation);
        }
        //Calculate and simulate armor wear on the defendant
        //Attack value multiplied by reduction scalar equals armor reduction
        int newArmorValue = opponent.getArmor() - (int)Math.floor(attForce / ARMOR_REDUCTION_SCALAR);
        opponent.setArmor(Math.max(0,newArmorValue));
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
     * Gets initial health
     *
     * @return The initial health value
     */
    public int getInitialHealth() {
        return initialHealth;
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
     * Gets the units tag
     *
     * @return unit tag
     */
    public String getTag() {
        return tag;
    }

    /**
     * Sets the units tag
     *
     * @return the tag
     */
    public void setTag(String tag) {
        this.tag = tag;
    }

    /**
     * Sets the unit health value. Cannot
     * be less than zero or more than 99.
     *
     * @param health The health value
     */
    public void setHealth(int health) {
        this.health = Math.max(Math.min(health,99), 0);
    }

    /**
     * Sets the unit armor value.
     * Cannot be less than zereo or
     * more than 99.
     *
     * @param armor The armor value
     */
    public void setArmor(int armor) {
        this.armor = Math.max(Math.min(armor,99), 0);
    }

    /**
     * Calls for setHealth method with value that unit
     * takes damage. This way we can Override the method
     * to create specified takeDamage methods in subclasses
     *
     * @param health The value in which the unit takes damage
     */
    public void takeDamage(int health){
        this.setHealth(health);
    }

    /**
     * Returns the default units attack bonus.
     *
     * @return The attack bonus
     */
    public abstract int getAttackBonus(Terrain terrain);

    /**
     * Returns the default units resistance bonus
     *
     * @return The resistance bonus
     */
    public abstract int getResistBonus(Terrain terrain);

    /**
     * Returns the class as string
     *
     * @return class
     */
    public abstract String getType();

    /**
     * Compares two units. First we check if they
     * are the same subclass. Then we check each
     * individual field of the unit.
     *
     * @param u The object we are comparing
     * @return The result of the comparison.
     */
    @Override
    public int compareTo(Unit u) {
        if(this.getType().equals(u.getType())) return 1;
        else if(!this.getName().equals(u.getName())) return 1;
        else if(this.getHealth() > u.getHealth()) return 1;
        else if(this.getAttack() > u.getAttack()) return 1;
        else if(this.getArmor() > u.getArmor()) return 1;
        else return -1;
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
}
