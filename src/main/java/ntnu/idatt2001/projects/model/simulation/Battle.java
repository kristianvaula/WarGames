package ntnu.idatt2001.projects.model.simulation;

import ntnu.idatt2001.projects.model.units.Unit;
import ntnu.idatt2001.projects.view.BattleView;
import org.apache.commons.collections4.BidiMap;

import java.util.*;

/**
 * Battle simulates a battle between two armies.
 *
 * The armies fight each other until one of them
 * are defeated. We have the possibility to add
 * a MapView object to let our simulation be displayed
 * in 2 dimensional graphic. In this case the units will
 * move towards the closest enemy and attack until one
 * of the armies have won.
 */
public class Battle {
    //The two armies fighting each other
    private Army armyOne;
    private Army armyTwo;
    //The map on which the battle is taking place
    private Map map;
    //Map View
    private BattleView view = null;

    /**
     * Initiates a new Battle object. Takes each
     * army as argument.
     *
     * @param armyOne The first army going into battle
     * @param armyTwo The second army going into battle
     * @throws IllegalArgumentException If the armies are equal
     */
    public Battle(Army armyOne, Army armyTwo) throws IllegalArgumentException{
        if(armyOne.equals(armyTwo)) throw new IllegalArgumentException("Battle cannot contain the same army twice");
        this.armyOne = armyOne;
        this.armyTwo = armyTwo;
    }

    /**
     * Gets army one
     *
     * @return The army
     */
    public Army getArmyOne() {
        return armyOne;
    }

    /**
     * Gets army two
     *
     * @return The army
     */
    public Army getArmyTwo() {
        return armyTwo;
    }

    /**
     * Gets the map
     *
     * @return the map
     */
    public Map getMap() {
        return map;
    }

    /**
     * Sets army one
     *
     * @param armyOne the army to set
     */
    public void setArmyOne(Army armyOne) {
        this.armyOne = armyOne;
    }

    /**
     * Sets army two
     *
     * @param armyTwo the army to set
     */
    public void setArmyTwo(Army armyTwo) {
        this.armyTwo = armyTwo;
    }

    /**
     * Sets the map
     *
     * @param map the map to set
     */
    public void setMap(Map map) {
        this.map = map;
    }

    /**
     * Sets the MapView of the battle.
     *
     * @param view the BattleView we are setting
     */
    public void setView(BattleView view) {
        this.view = view;
    }

    /**
     * Closes the mapview
     */
    public void closeMap(){
        view.closeMap();
    }

    /**
     * Places both armies on their corresponding
     * sides. Calls for the placeUnits with correct
     * positions
     */
    public void placeArmies(){
        placeUnits(armyOne.getAllUnits(),false);
        placeUnits(armyTwo.getAllUnits(),true);
    }

    /**
     * Simulates the battle. While both of the armies have
     * units we choose one from each army. One of these are
     * chosen randomly to attack the other. We then check to
     * see if unit is dead. Continues until one of the armies
     * are defeated.
     *
     * We have different simulation cases. If map is null we run the
     * simplified simulation. Else we run the 2D simulation. We only
     * display the battle if the MapView is set.
     *
     * @param milliDelay the delay between each step in milliseconds,
     *                   if the delay is <=0 the displayMap function
     *                   will not be called.
     * @throws IllegalStateException If one of the armies are empty
     * @return The winning army
     */
    public Army simulate(int milliDelay) throws IllegalStateException{
        if(!armyOne.hasUnits() || !armyTwo.hasUnits()){
            throw new IllegalStateException("Both armies must contain units to simulate a battle");
        }
        //If we have not declared a map we run simplified simulation
        if(map == null){
            return simplifiedSimulate();
        }
        //Else we run the 2D simulation with displaying if MapView is set
        else if(view != null){
            //if delay is 0 we run the 2D simulation only displaying result
            if(milliDelay <= 0 ){
                while(armyOne.hasUnits() && armyTwo.hasUnits()){
                    simulateOneStep();
                }
                updateMap();
            }
            //Else run while displaying underway
            else{
                while(armyOne.hasUnits() && armyTwo.hasUnits()){
                    simulateOneStep();
                    updateMap();
                    pause(milliDelay);
                }
            }
        }
        //If we have a map, but no view we run 2D simulation without displaying
        else{
            while(armyOne.hasUnits() && armyTwo.hasUnits()){
                simulateOneStep();
            }
        }

        //Return winner
        if(armyTwo.hasUnits()) return armyTwo;
        return armyOne;
    }

    /**
     * Draws the map on the canvas
     */
    public void updateMap(){
        if(view != null){
            view.showStatus(map);
        }
    }
    /**
     * Gets all enemies within range of the unit standing at
     * the given location. The location is passed as argument.
     * We call for the getAdjacentLocations with the location
     * and check each adjacent location for enemies by comparing
     * tags.
     *
     * @param location Location unit is standing on
     * @return All enemy locations within range
     * @throws IllegalArgumentException if location passed is invalid
     */
    protected List<Location> getEnemyLocationsWithinRange(Location location) throws IllegalArgumentException{
        if(!map.isLocationValid(location)) throw new IllegalArgumentException("Location passed is invalid");

        List<Location> enemiesWithinRange = new ArrayList<>();
        String friendlyTag = map.getUnitByLocation(location).getTag();
        //Get all adjacent locations
        List<Location> adjacentLocations = map.getAdjacentLocations(location);

        //Get unit tracker
        for(Location adjLoc : adjacentLocations){
            //If there is a unit on location
            if(map.isLocationOccupied(adjLoc)){
                //Checks if unit on location has the same tag
                if(!map.getUnitByLocation(adjLoc).getTag().equals(friendlyTag)){
                    //Is enemy
                    enemiesWithinRange.add(adjLoc);
                }
            }
        }

        return enemiesWithinRange;
    }

    /**
     * Gets the closest enemy location in regards to own location.
     * Checks the distance to all enemy units and returns
     * the location with the shortest distance to location.
     *
     * @param location the location where we are looking for enemies
     * @return The closest enemy location
     */
    protected Location getClosestEnemyLocation(Location location){
        //Get the unitLocationTracker
        BidiMap<Location, Unit> unitLocationTracker = map.getUnitLocationTracker();
        //Get the unit looking for enemies` tag
        String thisUnitTag = unitLocationTracker.get(location).getTag();
        Location closeEnemyPos = null;
        //Default set max distance of map
        double dist = Math.sqrt(map.getWidth()*map.getWidth()
                + map.getDepth()* map.getDepth());
        //Iterate through units locations
        for(Location unitLoc : unitLocationTracker.keySet()){
            //Check if enemy
            if(!unitLocationTracker.get(unitLoc).getTag().equals(thisUnitTag)){
                //Calculate distance
                double tempDist = Math.sqrt(Math.pow(unitLoc.getRow() - location.getRow(),2)
                        + Math.pow(unitLoc.getCol() - location.getCol(),2));
                if(tempDist < dist){
                    dist = tempDist;
                    closeEnemyPos = unitLoc;
                }
            }
        }
        return closeEnemyPos;
    }

    /**
     * Simulates an attack between two units.
     * We get the units through their location and
     * then the attacker attacks. If the defender dies
     * we remove the defender from location and army, and
     * move the attacker.
     *
     * @param attacker Attacking unit
     * @param defender Defending unit
     */
    private void attack(Unit attacker, Unit defender) throws Exception{
        if(attacker.getTag().equals(defender.getTag())){
            throw new Exception("Attacker and defender on the same team");
        }

        Location defenderLoc = map.getLocationByUnit(defender);
        //Attacker attacks the defender
        attacker.attack(defender,defenderLoc.getTerrain());
        //If the defender died
        if(defender.getHealth() <= 0){
            //Remove dead unit from location
            map.removeUnit(defender);
            //Move attacker to the defenders old position
            map.moveUnit(attacker,defenderLoc);

            //Remove dead unit from his army
            if(armyOne.getName().equals(defender.getTag())){
                armyOne.remove(defender);
            }
            else{
                armyTwo.remove(defender);
            }
        }
    }

    /**
     * Moves unit towards enemy. Gets closest enemy
     * and finds which free adjacent location brings the
     * unit closest to the enemy.
     *
     * @param unit Unit wanting to move
     */
    private void moveTowardsEnemy(Unit unit){
        //Initiate newPos as current position
        Location newPos = map.getLocationByUnit(unit);
        //Get free adjacent locations and closest enemy location
        List<Location> freeAdjLoc = map.getFreeAdjacentLocations(newPos);
        Location closestEnemyLoc = getClosestEnemyLocation(newPos);
        if(closestEnemyLoc == null) return;
        //Set distance between current location and enemy as default
        double dist = Math.sqrt(Math.pow(closestEnemyLoc.getRow() - newPos.getRow(),2)
                + Math.pow(closestEnemyLoc.getCol() - newPos.getCol(),2));

        //Loop through free adjacent locations and calculate their distance to enemy
        for(Location freeLoc : freeAdjLoc){
            double tempDist = Math.sqrt(Math.pow(closestEnemyLoc.getRow() - freeLoc.getRow(),2)
                    + Math.pow(closestEnemyLoc.getCol() - freeLoc.getCol(),2));
            //If the distance is shorter set location as newPosition and tempDistance as Distance
            if(tempDist < dist){
                dist = tempDist;
                newPos = freeLoc;
            }
        }
        //Move unit
        try{
            //Only move if new position
            if(!map.getLocationByUnit(unit).equals(newPos)){
                map.moveUnit(unit,newPos);
            }
        }//If fail, stay
        catch(Exception e ){
            e.printStackTrace();
        }
    }

    /**
     * Simulates the battle in the simple form. While both of
     * the armies have units we choose one from each army. One
     * of these are chosen randomly to attack the other. We then
     * check to see if unit is dead. Continues until one of the
     * armies are defeated.
     *
     * @return The winning army
     */
    private Army simplifiedSimulate(){
        Random rand = new Random();

        while(armyOne.hasUnits() && armyTwo.hasUnits()){
            Unit armyOneUnit = armyOne.getRandom();
            Unit armyTwoUnit = armyTwo.getRandom();

            //The nextBoolean() gives a random true or false value which
            //decides who attacks who
            if(rand.nextBoolean()){
                armyOneUnit.attack(armyTwoUnit,Terrain.PLAINS);
                if(armyTwoUnit.getHealth() == 0) armyTwo.remove(armyTwoUnit);
            }
            else{
                armyTwoUnit.attack(armyOneUnit,Terrain.PLAINS);
                if(armyOneUnit.getHealth() == 0) armyOne.remove(armyOneUnit);
            }
        }
        if(armyTwo.hasUnits()) return armyTwo;
        return armyOne;
    }

    /**
     * Runs through one step of the simulation.
     * In a step we pick random units until all of
     * the units have acted. Unit can only act if he
     * is alive.
     */
    private void simulateOneStep(){
        //Add all units to a list to keep track of turn
        ArrayList<Unit> allUnits = new ArrayList<>(armyOne.getAllUnits());
        allUnits.addAll(armyTwo.getAllUnits());
        //Randomize unit list
        Collections.shuffle(allUnits);
        try{
            for(Unit attacker : allUnits){
                if(attacker.getHealth() > 0) {
                    Location attackingUnitLoc = map.getLocationByUnit(attacker);
                    //Get enemies within range
                    List<Location> enemiesWithinRange = getEnemyLocationsWithinRange(attackingUnitLoc);
                    //If any enemies
                    if (enemiesWithinRange.size() > 0) {
                        //Attack
                        Unit defender = map.getUnitLocationTracker().get(enemiesWithinRange.get(0));
                        attack(attacker, defender);

                    } else {
                        //Move
                        moveTowardsEnemy(attacker);
                    }
                }
                if(!armyOne.hasUnits() || !armyTwo.hasUnits()){
                    break;
                }
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }

    }

    /**
     * Pauses thread for a given time.
     * We dont need to handle the exception.
     *
     * @param millis  The time to pause for in milliseconds
     */
    private void pause(int millis) {
        try {
            Thread.sleep(millis);
        }
        catch (InterruptedException ie) {}
    }

    /**
     * Places army units on map
     *
     * @param units Units to place
     * @param pos Where units shall be place
     *            True = Right
     *            False = Left
     */
    private void placeUnits(List<Unit> units, boolean pos){
        int startCol = 1;
        int direction = 1;
        if(pos){
            startCol = map.getWidth()-2;
            direction = -1;
        }
        Iterator<Unit> it = units.iterator();
        double unitsPerCol = Math.floor(((double)map.getDepth()-4)/2);
        for (int i = 0; i < Math.ceil(units.size()/unitsPerCol); i++) {
            for (int j = 0; j < unitsPerCol; j++) {
                if(it.hasNext()){
                    try {
                        if(j%2==0){
                            map.moveUnit(it.next(), (int) (Math.floor((double)map.getDepth()/2)+j),startCol+direction*i*2);
                        }
                        else{
                            map.moveUnit(it.next(), (int) (Math.floor((double)map.getDepth()/2)-j),startCol+direction*i*2);
                        }
                    }catch (Exception e){
                        e.printStackTrace();
                        break;
                    }
                }
            }
        }
    }

}