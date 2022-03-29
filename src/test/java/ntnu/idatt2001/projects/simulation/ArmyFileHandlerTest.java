package ntnu.idatt2001.projects.simulation;

import ntnu.idatt2001.projects.units.*;
import org.junit.jupiter.api.*;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

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
            "ntnu" + DLM + "idatt2001" + DLM + "projects" + DLM + "armyTestFiles";

    //Pre initiated txt files used in test classes:
    //      armynametwo.txt
    //      corruptarmyname.txt
    //      corruptunitvalues.txt
    //Please check resources -> armyTestFiles before running tests

    @BeforeEach
    void setUp() {
        corruptUnitValues = "CorruptUnitValues";
        corruptArmyName = "CorruptArmyName";
        corruptArmyFile = "CorruptArmyFile";
        armyNameTwo = "ArmyNameTwo";

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
        files.add(FILE_DIRECTORY + DLM + testArmy.getName().toLowerCase() + ".txt");
        files.add(FILE_DIRECTORY + DLM + emptyArmy.getName().toLowerCase() + ".txt");

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
            boolean checkBefore = armyFileHandler.armyFileExists(testArmy.getName());

            try {
                armyFileHandler.writeArmyToFile(testArmy);
            }catch(Exception e){
                System.out.println(e);
                fail();
            }

            assertNotEquals(checkBefore,armyFileHandler.armyFileExists(testArmy.getName()));
        }

        @Test
        @DisplayName("Writing army to file")
        public void writingEmptyArmyToFile(){
            boolean checkBefore = armyFileHandler.armyFileExists(emptyArmy.getName());

            try {
                armyFileHandler.writeArmyToFile(emptyArmy);
            }catch(Exception e){
                System.out.println(e);
                fail();
            }

            assertNotEquals(checkBefore,armyFileHandler.armyFileExists(emptyArmy.getName()));
        }

        @Test
        @DisplayName("Writing an army that is already written to file")
        public void writingAlreadyWrittenArmyToFile(){
            try {
                armyFileHandler.writeArmyToFile(testArmy);
            }catch(Exception e){
                System.out.println(e);
                fail();
            }
            testArmy.add(new InfantryUnit("Swordsman",20,10,10));

            try {
                armyFileHandler.writeArmyToFile(testArmy);
            }catch(Exception e){
                System.out.println(e);
                fail();
            }

            assertTrue(armyFileHandler.armyFileExists(testArmy.getName()));
        }
    }

    @Nested
    @DisplayName("Reading from file")
    public class readingFromFileTest {

        @Test
        @DisplayName("Reading armyNameTwo.txt")
        public void readingArmyNameTwoTxt(){
            Army readArmy = null;
            try {
                readArmy = armyFileHandler.readArmyFromFile(armyNameTwo);
            }catch (Exception e){
                System.out.println(e);
                fail();
            }

            assertTrue(readArmy.hasUnits() && readArmy.getName().equals(armyNameTwo));
        }

        @Test
        @DisplayName("Reading corruptunitvalues.txt throws exception")
        public void readingCorruptUnitValuesTxt(){
            assertThrows(IOException.class, () ->{
                try {
                    Army army = armyFileHandler.readArmyFromFile(corruptUnitValues);
                }catch(Exception e){
                    System.out.println(e);
                    throw e;
                }
            });
        }

        @Test
        @DisplayName("Reading corruptarmyfile.txt throws exception")
        public void readingCorruptArmyFileTxt(){
            assertThrows(IOException.class, () ->{
                try {
                    Army army = armyFileHandler.readArmyFromFile(corruptArmyFile);

                }catch(Exception e){
                    System.out.println(e);
                    throw e;
                }
            });
        }

        @Test
        @DisplayName("Reading corruptarmyname.txt throws exception")
        public void readingCorruptArmyNameTxt(){
            assertThrows(IOException.class, () ->{
                try {
                    Army army = armyFileHandler.readArmyFromFile(corruptArmyName);
                }catch(Exception e){
                    System.out.println(e);
                    throw e;
                }
            });
        }
    }

    @Nested
    @DisplayName("Other methods")
    public class methodTesting {

        @Test
        @DisplayName("getFilePath() gets correct file path")
        public void getsCorrectFilePath() {
            String correctFilePath = FILE_DIRECTORY + DLM + "armyname.txt";
            System.out.println(DLM);
            assertEquals(armyFileHandler.getFilePath(testArmy.getName()), correctFilePath);
        }

        @Test
        @DisplayName("armyFileExists() returns true if army file exists")
        public void armyFileExistReturnTrue() {
            String extistingArmyName = armyNameTwo;

            assertTrue(armyFileHandler.armyFileExists(extistingArmyName));
        }

        @Test
        @DisplayName("armyFileExists() returns false if army file not exists")
        public void armyFileExistReturnFalse() {
            String nonExistingArmyName = "RandomArmyName";

            assertFalse(armyFileHandler.armyFileExists(nonExistingArmyName));
        }
    }
}