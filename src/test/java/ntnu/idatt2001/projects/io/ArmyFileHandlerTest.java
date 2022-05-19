package ntnu.idatt2001.projects.io;

import ntnu.idatt2001.projects.model.simulation.Army;
import ntnu.idatt2001.projects.model.units.CavalryUnit;
import ntnu.idatt2001.projects.model.units.CommanderUnit;
import ntnu.idatt2001.projects.model.units.InfantryUnit;
import ntnu.idatt2001.projects.model.units.RangedUnit;
import org.junit.jupiter.api.*;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for armyFileHandler class
 */
public class ArmyFileHandlerTest {

    ArmyFileHandler armyFileHandler;
    Army testArmy, emptyArmy;
    InfantryUnit infantryUnit;
    CavalryUnit cavalryUnit;
    RangedUnit rangedUnit;
    CommanderUnit commanderUnit;
    String armyName, armyNameTwo,corruptUnitValues,corruptArmyName, corruptArmyFile;

    // DELIMITER
    private static final String DLM = File.separator;
    // DEFAULT FILE DIRECTORY
    private static final String FILE_DIRECTORY = "src" + DLM + "main" + DLM + "resources" + DLM +
            "ntnu" + DLM + "idatt2001" + DLM + "projects" + DLM + "testFiles" + DLM + "armyTestFiles";

    //Pre initiated csv files used in test classes:
    //      army_name_two.csv
    //      corruptarmyname.csv
    //      corruptunitvalues.csv
    //Please check resources -> armyTestFiles before running tests

    @BeforeEach
    void setUp() {
        corruptUnitValues = "CorruptUnitValues";
        corruptArmyName = "CorruptArmyName";
        corruptArmyFile = "CorruptArmyFile";
        armyNameTwo = "Army Name Two";

        armyName = "ArmyName";
        testArmy = new Army(armyName);

        infantryUnit = new InfantryUnit("InfantryUnit", 20);
        cavalryUnit = new CavalryUnit("CavalryUnit", 20);
        rangedUnit = new RangedUnit("RangedUnit", 20);
        commanderUnit = new CommanderUnit("CommanderUnit", 40);

        testArmy.add(infantryUnit);
        testArmy.add(cavalryUnit);
        testArmy.add(rangedUnit);
        testArmy.add(commanderUnit);

        emptyArmy = new Army("EmptyArmy");

        armyFileHandler = new ArmyFileHandler();
        armyFileHandler.setFileDirectory(FILE_DIRECTORY);
    }

    @AfterEach
    void tearDown() {
        ArrayList<String> files = new ArrayList<>();
        files.add(FILE_DIRECTORY + DLM + testArmy.getName().toLowerCase() + ".csv");
        files.add(FILE_DIRECTORY + DLM + emptyArmy.getName().toLowerCase() + ".csv");

        boolean deleteSuccess = true;
        for(String filePath : files){
            if(new File(filePath).exists()){
                deleteSuccess = new File(filePath).delete();
            }
        }
        assertTrue(deleteSuccess);
    }

    @Nested
    @DisplayName("Writing to file")
    public class writingToFileTest {

        @Test
        @DisplayName("Writing army to file")
        public void writingArmyToFile(){
            boolean checkBefore = armyFileHandler.armyFileExists(armyFileHandler.getFilePath(testArmy.getName()));

            try {
                armyFileHandler.writeArmyToFile(testArmy);
            }catch(Exception e){
                e.printStackTrace();
                fail();
            }

            assertNotEquals(checkBefore,armyFileHandler.armyFileExists(armyFileHandler.getFilePath(testArmy.getName())));
        }

        @Test
        @DisplayName("Writing army to file")
        public void writingEmptyArmyToFile(){
            boolean checkBefore = armyFileHandler.armyFileExists(armyFileHandler.getFilePath(emptyArmy.getName()));

            try {
                armyFileHandler.writeArmyToFile(emptyArmy);
            }catch(Exception e){
                e.printStackTrace();
                fail();
            }

            assertNotEquals(checkBefore,armyFileHandler.armyFileExists(armyFileHandler.getFilePath(emptyArmy.getName())));
        }

        @Test
        @DisplayName("Writing an army that is already written to file")
        public void writingAlreadyWrittenArmyToFile(){
            try {
                armyFileHandler.writeArmyToFile(testArmy);
            }catch(Exception e){
                e.printStackTrace();
                fail();
            }
            testArmy.add(new InfantryUnit("Swordsman",20,10,10));

            try {
                armyFileHandler.writeArmyToFile(testArmy);
            }catch(Exception e){
                e.printStackTrace();
                fail();
            }

            assertTrue(armyFileHandler.armyFileExists(armyFileHandler.getFilePath(testArmy.getName())));
        }
    }

    @Nested
    @DisplayName("Reading from file")
    public class readingFromFileTest {

        @Test
        @DisplayName("Reading armyNameTwo.csv")
        public void readingArmyNameTwoCSV(){
            Army readArmy = null;
            try {
                readArmy = armyFileHandler.getArmyFromFile(armyNameTwo);
            }catch (Exception e){
                e.printStackTrace();
                fail();
            }

            assertTrue(readArmy.hasUnits() && readArmy.getName().equals(armyNameTwo));
        }

        @Test
        @DisplayName("Reading corruptunitvalues.csv throws exception")
        public void readingCorruptUnitValuesCSV(){
            assertThrows(IOException.class, () ->{
                try {
                    Army army = armyFileHandler.getArmyFromFile(corruptUnitValues);
                }catch(IOException e){
                    throw e;
                }
            });
        }

        @Test
        @DisplayName("Reading corruptarmyfile.csv throws exception")
        public void readingCorruptArmyFileCSV(){
            assertThrows(IOException.class, () ->{
                try {
                    Army army = armyFileHandler.getArmyFromFile(corruptArmyFile);

                }catch(IOException e){
                    throw e;
                }
            });
        }

        @Test
        @DisplayName("Reading corruptarmyname.csv throws exception")
        public void readingCorruptArmyNameCSV(){
            assertThrows(IOException.class, () ->{
                try {
                    Army army = armyFileHandler.getArmyFromFile(corruptArmyName);
                }catch(Exception e){
                    throw e;
                }
            });
        }

        @Test
        @DisplayName("Reading all default savefiles")
        public void readingAllDefaultSavefiles(){
            ArmyFileHandler defaultFileHandler = new ArmyFileHandler();
            List<Army> armies = null;
            try {
                armies = defaultFileHandler.getArmySaveFiles();
            }catch (Exception e){
                e.printStackTrace();
                fail();
            }

            assertTrue(armies.size() > 1 && armies.get(0).hasUnits());
        }
    }

    @Nested
    @DisplayName("Other methods")
    public class methodTesting {

        @Test
        @DisplayName("getFilePath() gets correct file path")
        public void getsCorrectFilePath() {
            String correctFilePath = FILE_DIRECTORY + DLM + "armyname.csv";

            assertEquals(armyFileHandler.getFilePath(testArmy.getName()), correctFilePath);
        }

        @Test
        @DisplayName("armyFileExists() returns true if army file exists")
        public void armyFileExistReturnTrue() {
            String extistingArmyName = armyNameTwo;

            assertTrue(armyFileHandler.armyFileExists(armyFileHandler.getFilePath(extistingArmyName)));
        }

        @Test
        @DisplayName("armyFileExists() returns false if army file not exists")
        public void armyFileExistReturnFalse() {
            String nonExistingArmyName = "RandomArmyName";

            assertFalse(armyFileHandler.armyFileExists(armyFileHandler.getFilePath(nonExistingArmyName)));
        }
    }
}