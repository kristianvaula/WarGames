package ntnu.idatt2001.projects.view;

import ntnu.idatt2001.projects.model.simulation.*;
import ntnu.idatt2001.projects.model.units.Unit;

import java.awt.*;
import javax.swing.*;
import java.util.HashMap;

/**
 * A graphical view of the simulation grid.
 *
 * Uses javax swing and is heavily inspired by the Fox and Rabbits project
 * from Objects First With Java. Following the authors David J.Barnes and
 * Michael Kölling solution (posted in the wiki of this projects` GitLab)
 * I was able to convert their "SimulatorView.java" into something i could
 * use to display my simulation.
 *
 * The view displays a colored rectangle for each location
 * representing its terrain. The color of each locations is
 * based on its Terrain. We first draw all of the locations
 * and then loop through the units and overwrite their
 * corresponding locations. Using the unit "tag" field we can
 * color the units of different armies in different colors.
 *
 */
public class BattleView extends JFrame {
    //Instance of local class mapView
    private MapView mapView;

    //Hashmaps for linking colors to armies
    private final HashMap<String,Color> ARMY_COLORS = new HashMap<>();
    private final HashMap<String,Color> ARMY_COLORS_LIGHT = new HashMap<>();
    //Hashmap for linking colors to Terrain
    private final HashMap<Terrain,Color> TERRAIN_COLORS = new HashMap<>();

    /**
     * Initiate a mapController of the given width and height.
     * Because we want to check each unit for their tag we compute
     * a color for each of armies` units` tag (The army name).
     *
     * @param depth The map's height.
     * @param width  The map's width.
     * @param armyOneTag Tag of units in army one (Army name)
     * @param armyTwoTag Tag of units in army two (Army name)
     */
    public BattleView(int depth, int width, String armyOneTag, String armyTwoTag) {
        TERRAIN_COLORS.put(Terrain.FOREST,Color.decode("#0CA320"));
        TERRAIN_COLORS.put(Terrain.PLAINS,Color.decode("#A7E713"));
        TERRAIN_COLORS.put(Terrain.HILL,Color.decode("#909090"));

        ARMY_COLORS.put(armyOneTag,Color.decode("#2F00FF"));
        ARMY_COLORS.put(armyTwoTag,Color.decode("#FF0000"));

        ARMY_COLORS_LIGHT.put(armyOneTag,Color.decode("#7A5CFF"));
        ARMY_COLORS_LIGHT.put(armyTwoTag,Color.decode("#FF5C5C"));

        //Initiate mapView object
        mapView = new MapView(depth, width);
        //Add to the
        Container contents = getContentPane();
        contents.add(mapView, BorderLayout.CENTER);
        pack();
        setVisible(true);
    }

    /**
     * Show the current status of the map.
     *
     * @param map The battle whose status is to be displayed.
     */
    public void showStatus(Map map)
    {
        if(!isVisible()) {
            setVisible(true);
        }

        mapView.preparePaint();
        for(int row = 0; row < map.getDepth(); row++) {
            for(int col = 0; col < map.getWidth(); col++) {
                Location location = map.getLocationAt(row, col);
                if(location != null) {
                    mapView.drawMark(col, row, TERRAIN_COLORS.get(map.getLocationAt(row,col).getTerrain()));
                }
            }
        }
        for(Location key : map.getUnitLocationTracker().keySet()){
            Unit unit = map.getUnitLocationTracker().get(key);
            Color color = ARMY_COLORS.get(unit.getTag());
            if(unit.getHealth()<unit.getInitialHealth()){
                color = ARMY_COLORS_LIGHT.get(unit.getTag());
            }
            mapView.drawMark(key.getCol(),key.getRow(),color);
        }

        mapView.repaint();
    }

    /**
     * Closes the MapView window
     */
    public void closeMap(){
        this.dispose();
    }

    /**
     * Provide a graphical view of a rectangular map. This is
     * a nested class which defines a custom component for the
     * user interface. This component displays the map.
     */
    private class MapView extends JPanel{
        //The scaling of each location to their pixel representation
        private final int GRID_VIEW_SCALING_FACTOR = 4;

        //gridWidth and gridHeight together with the scaling factor
        //make up the dimensions of our MapView.
        private final int gridWidth;
        private final int gridHeight;
        private int xScale, yScale;
        Dimension size;

        private Graphics g;
        private Image fieldImage;

        /**
         * Create a new FieldView component.
         */
        public MapView(int height, int width) {
            gridHeight = height;
            gridWidth = width;
            size = new Dimension(0, 0);
        }

        /**
         * Tell the GUI manager how big we would like the mapView
         * to be.
         */
        public Dimension getPreferredSize() {
            return new Dimension(gridWidth * GRID_VIEW_SCALING_FACTOR,
                    gridHeight * GRID_VIEW_SCALING_FACTOR);
        }

        /**
         * Prepare for a new round of painting. Since the component
         * may be resized, compute the scaling factor again.
         */
        public void preparePaint() {
            //If the size has changed we need to resize accordingly
            //This is not too difficult since we are painting everything
            if(! size.equals(getSize())) {
                size = getSize();
                fieldImage = mapView.createImage(size.width, size.height);
                g = fieldImage.getGraphics();

                xScale = size.width / gridWidth;
                if(xScale < 1) {
                    xScale = GRID_VIEW_SCALING_FACTOR;
                }
                yScale = size.height / gridHeight;
                if(yScale < 1) {
                    yScale = GRID_VIEW_SCALING_FACTOR;
                }
            }
        }

        /**
         * Paint on grid location on this field in a given color.
         */
        public void drawMark(int x, int y, Color color) {
            g.setColor(color);
            g.fillRect(x * xScale, y * yScale, xScale, yScale);
        }

        /**
         * The field view component needs to be redisplayed. Copy the
         * internal image to screen.
         */
        public void paintComponent(Graphics g) {
            if(fieldImage != null) {
                Dimension currentSize = getSize();
                if(size.equals(currentSize)) {
                    g.drawImage(fieldImage, 0, 0, null);
                }
                else {
                    // Rescale the previous image.
                    g.drawImage(fieldImage, 0, 0, currentSize.width, currentSize.height, null);
                }
            }
        }
    }
}
