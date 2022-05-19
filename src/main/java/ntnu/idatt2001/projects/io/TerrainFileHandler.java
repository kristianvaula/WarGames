package ntnu.idatt2001.projects.io;

import ntnu.idatt2001.projects.model.simulation.Terrain;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

/**
 * Takes care of handling terrain csv files.
 *
 * Consists of methods for both reading terrain
 * from csv file. Each cell in the csv file has a
 * number that represents a terrain type. Width and
 * Depth is determined by rows and columns length in
 * file.
 *
 * <p><br>
 * Example of file data:
 *
 * <pre>{@code}
 * 'PLAINS','PLAINS','PLAINS','HILL','HILL'
 * 'FOREST','FOREST','FOREST','HILL','HILL'
 *
 * 2,2,2,3,3
 * 1,1,1,3,3
 * </pre>{@code}
 * </p>
 *
 */
public class TerrainFileHandler {
    //Depth of the Terrain arrays
    private int arrayDepth;
    //Width of the Terrain arrays
    private int arrayWidth;

    // FILE DIRECTORY
    private String fileDirectory = "src" + DLM + "main" + DLM + "resources" + DLM +
            "ntnu" + DLM + "idatt2001" + DLM + "projects" + DLM + "saveFiles" + DLM + "mapFiles";

    // DELIMITER
    private static final String DLM = File.separator;
    //SPLITTER
    private static final String SPL = ",";
    //Terrain types
    private static final HashMap<Integer, Terrain> TERRAIN_TYPES = new HashMap<>();

    /**
     * Initiates an mapFileHandler.
     * Define the depth of the width of the Terrain arrays
     * the file handler will be looking for and working with.
     * We classify each of the terrain types as a int value.
     *
     * @param depth int depth of the Terrain arrays
     * @param width int width of the Terrain arrays
     */
    public TerrainFileHandler(int depth, int width) {
        this.arrayDepth = depth;
        this.arrayWidth = width;

        TERRAIN_TYPES.put(1,Terrain.FOREST);
        TERRAIN_TYPES.put(2,Terrain.PLAINS);
        TERRAIN_TYPES.put(3,Terrain.HILL);
    }

    /**
     * Takes a terrain file name and checks if a corresponding file
     * exists. If it does we call the readTerrainFromFile method.
     *
     * @param terrainFileName Name of file we are trying to read
     * @return the terrain we have read
     * @throws IOException if file does not exist or is corrupt
     * @throws NumberFormatException if file values are corrupt
     */
    public Terrain[][] getTerrainFromFile(String terrainFileName) throws IOException,NumberFormatException{
        if(!fileExists(getFilePath(terrainFileName))){
            throw new IOException("There were no records of " + terrainFileName + ". Please try again");
        }

        File file = new File(getFilePath(terrainFileName));
        Terrain[][] terrain = readTerrainFromFile(file);

        return terrain;
    }

    /**
     * Gets all terrain save files and returns them as a list.
     *
     * @return the list of terrains we have read
     * @throws IOException if file does not exist or is corrupt
     * @throws NumberFormatException if file values are corrupt
     */
    public ArrayList<Terrain[][]> getTerrainSaveFiles() throws IOException,NumberFormatException{
        File directory = new File(fileDirectory);
        String[] fileList = directory.list();

        ArrayList<Terrain[][]> terrains = new ArrayList<>();
        for(String terrainFile : fileList){

            if(!fileExists(fileDirectory + DLM + (terrainFile))){
                throw new IOException("There were no records of " + terrainFile + ". Please try again");
            }
            File file = new File(fileDirectory + DLM + (terrainFile));
            terrains.add(readTerrainFromFile(file));
        }

        return terrains;
    }

    /**
     * Reads and returns an terrain from a csv text file.
     * Takes in the file as parameter, reads it and
     * returns it as an terrain array object.
     *
     * @param file file we are trying to read
     * @return the terrain we have read
     * @throws IOException if file does not exist or is corrupt
     * @throws NumberFormatException if file values are corrupt
     */
    private Terrain[][] readTerrainFromFile(File file) throws IOException,NumberFormatException{
        Terrain[][] terrainGrid = new Terrain[arrayDepth][arrayWidth];
        // try/catch bracket automatically opens and closes file
        try(Scanner scanner = new Scanner(file)){

            if(!scanner.hasNextLine()) throw new IOException("File does not contain any values");

            int depthCounter = 0;
            //Cycles through all lines
            while(scanner.hasNextLine()){
                String line = scanner.nextLine();
                String[] values = line.split(SPL);

                if(values.length != arrayWidth) {
                    throw new IOException("File is corrupt and could not be read");
                }
                //Sets values for unit attributes
                for (int i = 0; i < values.length; i++) {
                    int terrainType = Integer.parseInt(values[i]);
                    //Checks that cell value represents a terraintype
                    if(!TERRAIN_TYPES.containsKey(terrainType)){
                        throw new IOException("File contains illegal characters");
                    }
                    terrainGrid[depthCounter][i] = TERRAIN_TYPES.get(terrainType);
                }
                depthCounter++;
            }
            if(depthCounter != arrayDepth) {
                throw new IOException("File did not match depth criteria");
            }
        }
        return terrainGrid;
    }

    /**
     * Safe way to check if a file exists
     * without worrying about exceptions
     *
     * @param filepath filepath of the army whose File we are looking for
     * @return true if army file exists
     */
    protected boolean fileExists(String filepath){
        return new File(filepath).exists();
    }

    /**
     * Gets the potential/real file path
     * of an terrain based on its name
     *
     * @param terrainName name of terrain file whose path we get
     * @return String path
     */
    protected String getFilePath(String terrainName){
        return fileDirectory + DLM + terrainName.toLowerCase().replaceAll(" ","_") + ".csv";
    }

    /**
     * Sets the file directory in which we write
     * and read files. Used when we want to save
     * the files to a different directory, for example
     * when we are doing tests.
     *
     * @param fileDirectory the new directory
     */
    public void setFileDirectory(String fileDirectory) {
        this.fileDirectory = fileDirectory;
    }
}
