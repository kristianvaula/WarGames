package ntnu.idatt2001.projects.simulation;

import ntnu.idatt2001.projects.units.*;

import java.util.Scanner;

/**
 * Runs the battle simulator as an interactive program
 */
public class BattleClient {
    //Scanner input to handle inputs from user
    Scanner input = new Scanner(System.in);
    //Battle we simulate
    Battle battle;

    private static final int SIMULATE_A_BATTLE = 1;
    private static final int ADD_UNITS_TO_ARMIES = 2;
    private static final int EDIT_ARMY_NAMES = 3;
    private static final int FILL_ARMIES_BY_DEFAULT = 4;
    private static final int EXIT = 9;

    /**
     * Initiates BattleClient with scanner input
     * and two empty armies.
     * @param armyOne Empty army
     * @param armyTwo Empty army
     */
    public BattleClient(Army armyOne, Army armyTwo) {
        this.battle = new Battle(armyOne,armyTwo);
    }

    /**
     * Prints the main menu
     */
    public void displayMainMenu(){
        System.out.println();
        System.out.println("**** - Welcome to the Battle Simulator - ****");
        System.out.println();
        System.out.println("    1. Simulate a battle");
        System.out.println("    2. Add units to armies");
        System.out.println("    3. Edit army names");
        System.out.println("    4. Fill armies from default");
        System.out.println("    9. Exit");
    }

    /**
     * Prints all units the user can create
     */
    public void displayUnitsToCreate(){
        System.out.println("**** - Please select which type of unit to add - ****");
        System.out.println("    1. Infantry unit ");
        System.out.println("    2. Cavalry unit ");
        System.out.println("    3. Ranged unit ");
        System.out.println("    4. Commander unit ");
        System.out.println("    9. Cancel ");
    }

    /**
     * Method for letting user choose if he wants
     * to add more units
     * @return true if user wants to add more units
     */
    public boolean addMoreUnits(){
        System.out.println("**** - Do you want to add more units? - ****");
        System.out.println("    1. YES ");
        System.out.println("    2. NO");
        return input.nextInt() == 1;
    }


    /**
     * If the armies contain units we call for
     * the simulation on the battle show result.
     */
    public void simulateABattle(){
        try {
            Army winner = battle.simulate();
            String result = "The winner was " + winner.getName()+ " with " + winner.getArmySize() + " units left";
            result += winner.toString();
            System.out.println(result);
        }catch (IllegalStateException e){
            System.out.println("**** - Both armies need to have units to perform a battle - ****");
            System.out.println("    Add units to both armies before continuing");
        }

        System.out.println("\n   1. To continue");
        int continueButton = input.nextInt();
    }

    /**
     * Lets user create different units to different armies.
     * User selects which army to add to, which unit to add,
     * how many and the name and health of the unit. User can
     * keep adding for as long as he wishes.
     */
    public void addUnitsToArmies(){
        //Goes through the loop if user wants to add more units
        boolean addMoreUnits;
        do {
            boolean success = true;

            //Choose which army to add units to
            System.out.println("**** - What army would you like to add units to? - ****");
            System.out.println("    1. " + battle.getArmyOne().getName());
            System.out.println("    2. " + battle.getArmyTwo().getName());
            int armyChoice = input.nextInt();

            //We use templateUnit as the model for the units we are adding
            Unit templateUnit = null;

            //Calls for the displayUnitsToCreate menu print method
            displayUnitsToCreate();
            //User chooses which unit.
            int menuChoice = input.nextInt();

            //User chooses how many units he wants to create
            System.out.println("**** - How many units do you want to create? - ****");
            //User has to enter amount again if he enters a negative number
            int quantity;
            do{
                quantity = input.nextInt();
            }while(quantity < 0);

            //User has to enter name for the unit
            System.out.println("    Please enter name: ");
            String name;
            do {
                name = input.nextLine();
            }while(name.length() == 0);

            //User has to enter health for the unit
            System.out.println("    Please enter health");
            int health;
            do{
               health = input.nextInt();
            }while(health <= 0);

            //Chooses unit type based on user input
            switch (menuChoice){
                case 1 -> { // Creates Infantry
                    try{
                        templateUnit = new InfantryUnit(name,health);
                    }catch (IllegalArgumentException e){
                        success = false;
                    }
                }
                case 2 -> { // Creates Cavalry
                    try{
                        templateUnit = new CavalryUnit(name,health);
                    }catch (IllegalArgumentException e){
                        success = false;
                    }
                }
                case 3 -> { // Creates Ranged
                    try{
                        templateUnit = new RangedUnit(name,health);
                    }catch (IllegalArgumentException e){
                        success = false;
                    }
                }
                case 4 -> { //Creates commander
                    try{
                        templateUnit = new CommanderUnit(name,health);
                    }catch (IllegalArgumentException e){
                        success = false;
                    }
                }
                default -> success = false;
            }

            //Calls for UnitAssembler on correct army
            if(armyChoice == 1 && success){
                battle.getArmyOne().addAll(UnitAssembler.requestUnits(templateUnit,quantity));
                System.out.println("Units successfully added");
            }
            else if (armyChoice == 2 && success){
                battle.getArmyTwo().addAll(UnitAssembler.requestUnits(templateUnit,quantity));
                System.out.println("Units successfully added");
            }
            else{ // If success is false, Unit constructor has thrown exception.
                System.out.println("Failed to create new units, please check input values");
            }

            addMoreUnits = addMoreUnits();
        }while(addMoreUnits);
    }

    /**
     * Lets user decide what the armies
     * are named.
     */
    public void editArmyNames(){
        System.out.println("**** - Which army name would you like to edit? - ****");
        System.out.println("    1. " + battle.getArmyOne().getName());
        System.out.println("    2. " + battle.getArmyTwo().getName());
        int armyChoice = input.nextInt();

        System.out.println("**** - Please choose a new name - ****");

        String name;
        do {
            name = input.nextLine();
        }while(name.length() == 0);

        if(armyChoice == 1){
            battle.getArmyOne().setName(name);
        }
        else if(armyChoice == 2){
            battle.getArmyTwo().setName(name);
        }

        System.out.println("New name " + name + " was successfully set");
        System.out.println("\n   1. To continue");
        int continueButton = input.nextInt();
    }

    /**
     * Fills armies with default units
     */
    public void fillArmiesByDefault(){
        Unit templateUnit = new InfantryUnit("Swordman",15);
        Unit templateUnit1 = new InfantryUnit("Axe wielding orc",15);
        Unit templateUnit2 = new CavalryUnit("Knight",25);
        Unit templateUnit3 = new CavalryUnit("Wolf Rider",25);
        Unit templateUnit4 = new RangedUnit("Long Bow Unit",10);
        Unit templateUnit5 = new RangedUnit("Crossbow Goul",10);
        Unit templateUnit6 = new CommanderUnit("King",40);
        Unit templateUnit7 = new CommanderUnit("Orc Commander",40);

        battle.getArmyOne().addAll(UnitAssembler.requestUnits(templateUnit,50));
        battle.getArmyTwo().addAll(UnitAssembler.requestUnits(templateUnit1,50));
        battle.getArmyOne().addAll(UnitAssembler.requestUnits(templateUnit2,20));
        battle.getArmyTwo().addAll(UnitAssembler.requestUnits(templateUnit3,20));
        battle.getArmyOne().addAll(UnitAssembler.requestUnits(templateUnit4,40));
        battle.getArmyTwo().addAll(UnitAssembler.requestUnits(templateUnit5,40));
        battle.getArmyOne().addAll(UnitAssembler.requestUnits(templateUnit6,1));
        battle.getArmyTwo().addAll(UnitAssembler.requestUnits(templateUnit7,1));

        System.out.println("**** - Default units were successfully added - ****");
        System.out.println("\n   1. To continue");
        int continueButton = input.nextInt();
    }

    /**
     * Displays the main menu and lets user choose what
     * he whishes to do. Calls the method after user
     * has given input.
     *
     * @return False if user wants to exit the program
     */
    public boolean runClient(){
        displayMainMenu();

        int menuChoice = input.nextInt();
        switch (menuChoice){
            case SIMULATE_A_BATTLE -> simulateABattle();

            case ADD_UNITS_TO_ARMIES -> addUnitsToArmies();

            case EDIT_ARMY_NAMES -> editArmyNames();

            case FILL_ARMIES_BY_DEFAULT -> fillArmiesByDefault();

            case EXIT -> {
                System.out.println("**** - Thank you for using Battle Client! - ****");
                return false;
            }
            default -> {

            }
        }

        return true;
    }

    public static void main(String[] args) {
        BattleClient client = new BattleClient(new Army("Humans"),new Army("Orcs"));

        //Keeps running the runClient until it returns false
        boolean clientContinue;
        do{
            clientContinue = client.runClient();
        }while(clientContinue);
    }
}
