package ntnu.idatt2001.projects.io;

import ntnu.idatt2001.projects.model.simulation.Army;
import ntnu.idatt2001.projects.model.units.*;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
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
    //TODO MAKE BOTH FILEHANDLER POLYMORPHIC WITH SUPERCLASS?

    // FILE DIRECTORY
    private String fileDirectory = "src" + DLM + "main" + DLM + "resources" + DLM +
                                    "ntnu" + DLM + "idatt2001" + DLM + "projects"+ DLM +
                                    "savefiles" + DLM + "armyFiles";

    // DELIMITER
    private static final String DLM = File.separator;
    // NEWLINE
    private static final String NWL = "\n";
    //SPLITTER
    private static final String SPL = ",";
    //Pattern used to check if name contains any characters except letters, digits and "-" "."
    private static final Pattern namePattern = Pattern.compile("[^a-zA-Z0-9-.\s]");

    /**
     * Initiates an armyFileHandler
     */
    public ArmyFileHandler() {}

    /**
     * Takes a army name and checks if a corresponding file
     * exists. If it does we call the readArmyFromFile method.
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
        Army army = readArmyFromFile(file);

        return army;
    }

    /**
     * Gets all army savefiles and returns them as a list.
     * @return the list of armies we have read
     * @throws IOException if file does not exist or is corrupt
     * @throws NumberFormatException if file values are corrupt
     */
    public ArrayList<Army> getArmySavefiles() throws IOException,NumberFormatException{
        File directory = new File(fileDirectory);
        String[] fileList = directory.list();

        ArrayList<Army> armies = new ArrayList<>();
        for(String armyFile : fileList){

            if(!armyFileExists(fileDirectory + DLM + (armyFile))){
                throw new IOException("There were as issue loading savefile: " + armyFile);
            }
            File file = new File(fileDirectory + DLM + (armyFile));
           armies.add(readArmyFromFile(file));
        }

        return armies;
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
            if(namePattern.matcher(nameOfArmy).find()){
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

                switch (type) {
                    case "InfantryUnit" -> army.add(new InfantryUnit(name, health, attack, armor));
                    case "RangedUnit" -> army.add(new RangedUnit(name, health, attack, armor));
                    case "CommanderUnit" -> army.add(new CommanderUnit(name, health, attack, armor));
                    case "CavalryUnit" -> army.add(new CavalryUnit(name, health, attack, armor));
                    default -> throw new IOException("File contains incorrect values and could not be read");
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
}