package ntnu.idatt2001.projects.io;

import ntnu.idatt2001.projects.model.simulation.Army;
import ntnu.idatt2001.projects.model.units.*;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Pattern;

/**
 * Takes care of handling army files.
 *
 * Consists of methods for both reading from and
 * writing an army to a csv file. First line of the
 * file represents the name of the army, then every new
 * line represents a Unit.
 * <p><br>
 * Example of file data:
 *
 * <pre>{@code}
 * 'armyName'
 * 'Class','Name','Health','Attack','Armor'
 * 'Class','Name','Health','Attack','Armor'
 *
 * TwoManArmy
 * InfantryUnit,Swordsman,20,15,10
 * InfantryUnit,Swordsman,20,15,10
 * </pre>{@code}
 * </p>
 */
public class ArmyFileHandler {

    // File directory
    private String fileDirectory;

    // DELIMITER
    private static final String DLM = File.separator;
    // NEWLINE
    private static final String NWL = "\n";
    //SPLITTER
    private static final String SPL = ",";
    //Pattern used to check if name contains any characters except letters, digits and "-" "."
    private static final Pattern NAME_PATTERN = Pattern.compile("[^a-zA-Z0-9-.\s]");
    //DEFAULT FILE DIRECTORY
    private static final String DEFAULT_DIRECTORY = "src" + DLM + "main" + DLM + "resources" + DLM +
            "ntnu" + DLM + "idatt2001" + DLM + "projects"+ DLM +
            "savefiles" + DLM + "armyFiles";

    //ACTIVE ARMIES FILE DIRECTORY
    private static final String ACTIVE_ARMY_DIRECTORY = "src" + DLM + "main" + DLM + "resources" + DLM +
            "ntnu" + DLM + "idatt2001" + DLM + "projects"+ DLM +
            "savefiles" + DLM + "active";

    /**
     * Initiates an armyFileHandler.
     * Sets the default file directory
     */
    public ArmyFileHandler() {
        this.fileDirectory = DEFAULT_DIRECTORY;
    }

    /**
     * Takes a army name and checks if a corresponding file
     * exists. If it does we call the readArmyFromFile method.
     *
     * @param armyName Name of army we are trying to read
     * @return the army we have read
     * @throws IOException if file does not exist or is corrupt
     * @throws NumberFormatException if file values are corrupt
     */
    public Army getArmyFromFile(String armyName) throws IOException,NumberFormatException{
        if(!armyFileExists(getFilePath(armyName))){
            throw new IOException("There were no records of " + armyName + ". Please try again");
        }

        File file = new File(getFilePath(armyName));

        return readArmyFromFile(file);
    }

    /**
     * Gets all army save files and returns them as a list.
     *
     * @return the list of armies we have read
     * @throws IOException if file does not exist or is corrupt
     * @throws NumberFormatException if file values are corrupt
     */
    public List<Army> getArmySaveFiles() throws IOException,NumberFormatException{
        String[] fileList = new File(fileDirectory).list();
        if(fileList == null) throw new IOException("Could not find any files");

        ArrayList<Army> armies = new ArrayList<>();
        for(String armyFile : fileList){

            if(!armyFileExists(fileDirectory + DLM + (armyFile))){
                throw new IOException("There was an issue loading savefile: " + armyFile);
            }
            File file = new File(fileDirectory + DLM + (armyFile));
           armies.add(readArmyFromFile(file));
        }

        return armies;
    }

    /**
     * Gets the active armies that the user has selected.
     * Sets the directory to the active army storage.
     * Then checks that we have files there and returns these.
     * If there are no active armies we set them from default armies.
     * As a backup if all fails, we just return empty armies.
     *
     * @return List containing both active armies.
     * @throws IllegalStateException If we are unable to get the active armies
     */
    public List<Army> getActiveArmies() throws IllegalStateException{
        List<Army> activeArmies = new ArrayList<>();
        try {
            //We attempt to get the active armies
            setActiveArmyDirectory();
            List<Army> armiesFromActive = getArmySaveFiles();
            //Check if there are any
            if (armiesFromActive.size() == 2) {
                //Get them
                activeArmies.addAll(armiesFromActive);
            }
            else { //Else we try to get the two first saved armies
                setDefaultDirectory();
                List<Army> armiesFromDefault = getArmySaveFiles();
                //Check that we have armies
                if (armiesFromDefault.size() > 1) {
                    activeArmies.add(armiesFromDefault.get(0));
                    activeArmies.add(armiesFromDefault.get(1));
                }
                else { //If not create a new empty one.
                    activeArmies.add(new Army("Army One"));
                    activeArmies.add(new Army("Army Two"));
                }
            }
        }
        //Should getting the armies from files fail, we simply initiate a blank battle
        catch (Exception e) {
            e.printStackTrace();
            throw new IllegalStateException("Active armies could not be found or set");
        }

        //Return armies
        return activeArmies;
    }

    /**
     * Sets the active armies that the user has selected.
     * Is called for to save changes made by a user on the
     * active armies.
     *
     * @param activeArmies List of the two active armies we
     *                     want to set
     */
    public void setActiveArmies(List<Army> activeArmies) throws IOException{
        //Do some checks
        if(activeArmies.size() != 2) {
            throw new IllegalArgumentException("Wrong amount of armies passed to method");
        }
        if(activeArmies.get(0) == null || activeArmies.get(1) == null) {
            throw new IllegalArgumentException("Armies not initiated");
        }
        //Delete existing active files
        File[] files = new File(fileDirectory).listFiles();
        if(files!=null) { //some JVMs return null for empty dirs
            for(File file: files) {
                if(!file.delete()){
                    throw new IOException("Failed to delete file " + file.getName());
                }
            }
        }
        writeArmyToFile(activeArmies.get(0));
        writeArmyToFile(activeArmies.get(1));
    }

    /**
     * Deletes an army.
     * Uses delete File delete if
     * file exists
     *
     * @param armyName Name of army we are deleting
     * @throws IOException if deletion fails or army file does not exist
     */
    public void deleteArmySaveFile(String armyName) throws IOException{
        if(!armyFileExists(getFilePath(armyName))){
            throw new IOException("There were no records of " + armyName + ". Please try again");
        }
        if(!new File(getFilePath(armyName)).delete()){
            throw new IOException("There was an error deleting " + armyName + ". Please try again");
        }
    }

    /**
     * Reads and returns an army from a csv text file.
     * Takes in the army file as parameter, reads it and
     * returns it as an army object.
     * @param file Army file we are trying to read
     * @return the army we have read
     * @throws IOException if file does not exist or is corrupt
     * @throws NumberFormatException if file values are corrupt
     */
    private Army readArmyFromFile(File file) throws IOException,NumberFormatException{
        Army army;
        // try/catch bracket automatically opens and closes file
        try(Scanner scanner = new Scanner(file)){

            if(!scanner.hasNextLine()) throw new IOException("File does not contain any values");

            //Checks if the first line is an army name.
            String nameOfArmy = scanner.nextLine().trim().replace("\n", "").replace("\r", "");

            //Calls namePattern with a matcher on nameOfArmy and checks it for illegal characters
            if(NAME_PATTERN.matcher(nameOfArmy).find()){
                throw new IOException("Name of army contains illegal characters");
            }
            army = new Army(nameOfArmy);
            //Cycles through all lines
            while(scanner.hasNextLine()){
                String line = scanner.nextLine();
                String[] values = line.split(SPL);
                if(values.length != 5) {
                    throw new IOException("File is corrupt and could not be read");
                }
                //Sets values for unit attributes
                String type = values[0].trim();
                String name = values[1].trim();
                int health = Integer.parseInt(values[2]);
                int attack = Integer.parseInt(values[3]);
                int armor = Integer.parseInt(values[4]);

                //Controls values before sending them trough constructor
                if(health < 0 || attack < 0 || armor < 0 || name.isBlank()){
                    throw new IOException("File contains incorrect values and could not be read");
                }
                //Army cannot exceed 500 units
                if(army.getArmySize() < 500){
                    UnitType unitType;
                    switch (type) {
                        case "InfantryUnit" -> unitType = UnitType.INFANTRY;
                        case "RangedUnit" -> unitType = UnitType.RANGED;
                        case "CommanderUnit" -> unitType = UnitType.COMMANDER;
                        case "CavalryUnit" -> unitType = UnitType.CAVALRY;
                        default -> throw new IOException("File contains incorrect values and could not be read");
                    }
                    army.add(UnitFactory.getUnit(unitType,name, health, attack, armor));
                }
            }
        }

        return army;
    }

    /**
     * Writes an army to a csv text file.
     * Constructs a line for each unit with
     * all its attributes delimited.
     * @param army Army consisting of a name and a list of units
     * @throws IOException if failed to write to file
     */
    public void writeArmyToFile(Army army) throws IOException{
        if(NAME_PATTERN.matcher(army.getName()).find()){
            throw new IOException("Name of army contains illegal characters");
        }
        try(FileWriter fileWriter = new FileWriter(getFilePath(army.getName()))) {
            fileWriter.write(army.getName() + NWL);

            for(Unit unit : army.getAllUnits()){
                String line = unit.getClass().getSimpleName() + SPL
                                + unit.getName() + SPL
                                    + unit.getHealth() + SPL
                                        + unit.getAttack() + SPL
                                            + unit.getArmor();

                fileWriter.write(line + NWL);
            }
        } catch(IOException e) {
            throw new IOException("Unable to write army to file: " + e.getMessage());
        }
    }

    /**
     * Safe way to check if a file exists
     * without worrying about exceptions
     * @param filepath filepath of the army whose File we are looking for
     * @return true if army file exists
     */
    protected boolean armyFileExists(String filepath){
        return new File(filepath).exists();
    }

    /**
     * Gets the potential/real file path
     * of an army based on its name
     * @param armyName name of army whose path we get
     * @return String path
     */
    protected String getFilePath(String armyName){
        return fileDirectory + DLM + armyName.toLowerCase().replaceAll(" ","_") + ".csv";
    }

    /**
     * Sets the file directory in which we write
     * and read files. Used when we want to save
     * the files to a different directory, for example
     * when we are doing tests.
     * @param fileDirectory the new directory
     */
    public void setFileDirectory(String fileDirectory) {
       this.fileDirectory = fileDirectory;
    }

    /**
     * Sets the file directory in which we write
     * and read files to the directory for active armies.
     * We use this to save our selections of armies between
     * pages.
     */
    public void setActiveArmyDirectory() {
        this.fileDirectory = ACTIVE_ARMY_DIRECTORY;
    }

    /**
     * Sets the file directory in which we write
     * and read files to the default directory. Can be used
     * to regret setting a custom file directory.
     */
    public void setDefaultDirectory() {
        this.fileDirectory = DEFAULT_DIRECTORY;
    }
}
