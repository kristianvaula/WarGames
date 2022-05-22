package ntnu.idatt2001.projects.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import ntnu.idatt2001.projects.io.ArmyFileHandler;
import ntnu.idatt2001.projects.io.TerrainFileHandler;
import ntnu.idatt2001.projects.model.simulation.Army;
import ntnu.idatt2001.projects.model.simulation.Battle;
import ntnu.idatt2001.projects.model.simulation.Map;
import ntnu.idatt2001.projects.model.units.Unit;
import ntnu.idatt2001.projects.model.units.UnitType;
import ntnu.idatt2001.projects.view.BattleView;

import java.io.IOException;
import java.net.URL;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Controls the simulate battle page
 */
public class SimulateBattleController implements Initializable {

    //The battle
    private Battle battle;
    //Terrain File Handler
    private final TerrainFileHandler terrainFileHandler = new TerrainFileHandler(DEPTH,WIDTH);
    //Army file handler
    private final ArmyFileHandler armyFileHandler = new ArmyFileHandler();
    //Storing the maps the user can choose
    private final HashMap<String,Map> maps = new HashMap<>();

    //Constant that sets delay time in simulation
    private static final int SIMULATION_DELAY_MILLIS = 250;
    //Constants for the maps we want to include
    private static final String[] MAP_NAMES = {"Mixed Terrain","Forest","Hill","Plains"};
    //Default Selected Map
    private static final String DEFAULT_MAP = MAP_NAMES[0];
    //Default Width
    private static final int WIDTH = 135;
    //Default Depth
    private static final int DEPTH = 85;

    //Status Display
    @FXML private Label statusLabel;

    //Map ChoiceBox
    @FXML private ChoiceBox<String> mapSelector;

    //Buttons
    @FXML private Button slowSimulationButton;
    @FXML private Button fastSimulationButton;
    @FXML private Button exitButton;

    //Army information displays
    @FXML private Label armyName1;
    @FXML private Label armyName2;
    @FXML private FlowPane unitFlowPane1;
    @FXML private FlowPane unitFlowPane2;
    private HashMap<String,Image> icons = new HashMap<>();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        //Sets up the battle data
        setUpBattleData();

        //Import icons
        icons.put(UnitType.INFANTRY.toString(),new Image(String.valueOf(getClass().getResource("../view/icons/infantry.png"))));
        icons.put(UnitType.RANGED.toString(),new Image(String.valueOf(getClass().getResource("../view/icons/archer.png"))));
        icons.put(UnitType.CAVALRY.toString(),new Image(String.valueOf(getClass().getResource("../view/icons/knight.png"))));
        icons.put(UnitType.COMMANDER.toString(),new Image(String.valueOf(getClass().getResource("../view/icons/commander.png"))));

        try{
            //Imports maps from directory
            for(String mapName : MAP_NAMES){
                maps.put(mapName,new Map(terrainFileHandler.getTerrainFromFile(mapName),DEPTH,WIDTH));
            }
            battle.setMap(maps.get(DEFAULT_MAP));
        } // If it fails we alert user
        catch (Exception e ){
            e.printStackTrace();
            alertUser(Alert.AlertType.ERROR,"There was an issue loading the maps, please try again.");
        }

        //Fills the map selector choicebox
        for(String mapName : MAP_NAMES){
            mapSelector.getItems().add(mapName);
        }
        //Add listener to choicebox that calls changed method when user selects a different map
        mapSelector.getSelectionModel().selectedItemProperty().addListener((observableValue, oldValue, newValue) -> {
            battle.getMap().clear();
            battle.setMap(maps.get(newValue));
            setUpBattleData();
        });
        //Select the default map
        mapSelector.getSelectionModel().select(DEFAULT_MAP);

        //Setting text and calling display units method
        armyName1.setText(battle.getArmyOne().getName());
        armyName2.setText(battle.getArmyTwo().getName());
        displayUnitsAndHealth();
    }

    /**
     * Sets up the battle data used by the controller.
     * Gets the active armies that we have selected in
     * the application. Then we initiate the battle and
     * set the view.
     *
     * If the battle has already been initiated, we will
     * simply refresh the map and armies.
     */
    @FXML
    private void setUpBattleData() {
        Army armyOne,armyTwo;
        try {
            List<Army> activeArmies = armyFileHandler.getActiveArmies();
            armyOne = activeArmies.get(0);
            armyTwo = activeArmies.get(1);
        }
        catch (IllegalStateException e){
            armyOne = new Army("Army One");
            armyTwo = new Army("Army Two");
        }
        if(battle == null){ // Initiates first time
            battle = new Battle(armyOne,armyTwo);
            //Add a MapView because we want to graphically display the battle
            battle.setView(new BattleView(DEPTH,WIDTH,battle.getArmyOne().getName(),battle.getArmyTwo().getName()));
        }
        else{ // Resets after last battle
            battle.getMap().clear();
            battle.closeMap();
            battle.setArmyOne(armyOne);
            battle.setArmyTwo(armyTwo);
            battle.placeArmies();
            battle.updateMap();
        }
    }

    /**
     * Calls the run simulation method standard
     * delay time to display simulation while the
     * battle is progressing.
     */
    @FXML
    private void runSlowSimulationEvent(){
        runSimulation(SIMULATION_DELAY_MILLIS);
    }

    /**
     * Calls the run simulation method with
     * no delay to get instantaneous result.
     */
    @FXML
    private void runFastSimulationEvent(){
         runSimulation(0);
    }

    /**
     * Runs the simulation. Starts by deactivating
     * all buttons. Then calls the setUpBattleData.
     * We start the simulation and wait for it to finish
     * and return the winner. Last we display results
     * and update army display boxes.
     *
     * @param millis Delay between each simulation step
     */
    @FXML
    private void runSimulation(int millis){
        //Preparing:
        statusLabel.setText("Battle simulation underway");
        buttonDeactivation(true);
        setUpBattleData();
        //Run simulation:
        Army winner = battle.simulate(millis);
        //Results:
        displayUnitsAndHealth();
        statusLabel.setText("The winner of the battle was " + winner.getName() + " with " + winner.getArmySize() + " units left.");
        buttonDeactivation(false);
    }

    /**
     * Deactivates all buttons. Used while
     * we run the simulation so that the user
     * does not affect the running simulation
     *
     * @param value True deactivates button, false
     *              will reactivate them.
     */
    @FXML
    private void buttonDeactivation(boolean value){
        mapSelector.setDisable(value);
        exitButton.setDisable(value);
        slowSimulationButton.setDisable(value);
        fastSimulationButton.setDisable(value);
    }

    /**
     * Calls for generateUnitVBox to display all units
     */
    @FXML
    private void displayUnitsAndHealth(){
        unitFlowPane1.getChildren().clear();
        unitFlowPane2.getChildren().clear();
        //Comparator to compare health and type
        Comparator<Unit> comparator = Comparator.comparing(Unit::getType)
                .thenComparing(Unit::getHealth)
                .reversed();

        List<Unit> armyOne = battle.getArmyOne().getAllUnits()
                .stream()
                .sorted(comparator)
                .collect(Collectors.toList());

        for(Unit unit : armyOne){
            VBox unitContainer = generateUnitVBox(unit);
            unitFlowPane1.getChildren().add(unitContainer);
            FlowPane.setMargin(unitContainer,new Insets(2,2,2,2));
        }
        //Units of army two pipeline
        List<Unit> armyTwo = battle.getArmyTwo().getAllUnits()
                .stream()
                .sorted(comparator)
                .collect(Collectors.toList());

        for(Unit unit: armyTwo){
            VBox unitContainer = generateUnitVBox(unit);
            unitFlowPane2.getChildren().add(unitContainer);
            FlowPane.setMargin(unitContainer,new Insets(2,2,2,2));
        }
    }

    /**
     * Displays a unit as a tiny VBox with an
     * icon representing its unit type and a health
     * bar showing how much of the initial health is
     * left. First we try getting the pre-made fxml
     * VBox and using controller to set values. If this
     * fails we make it manually by setting each value
     * and style. We want to use pre made to enhance
     * performance.
     */
    @FXML
    private VBox generateUnitVBox(Unit unit){
        VBox unitContainer = null;
        //We can get the icon for either of the methods.
        unitContainer = new VBox();
        unitContainer.setPrefHeight(45);
        unitContainer.setMinHeight(45);
        unitContainer.setPrefWidth(30);
        unitContainer.setStyle("-fx-border-color:#191308;-fx-background-color: #191308");
        //UNIT ICON
        VBox imagePane = new VBox();
        imagePane.setStyle("fx-background-color: gray;");
        imagePane.setPrefHeight(35);
        imagePane.setPrefWidth(30);

        ImageView image = new ImageView();
        image.setImage(icons.get(unit.getType()));
        image.setFitHeight(35);
        image.setFitWidth(30);
        imagePane.getChildren().add(image);
        //HEALTH BAR
        VBox healthPane = new VBox();
        healthPane.setPrefHeight(8);
        double paneWidth = Math.floorDiv(unit.getHealth()*30,unit.getInitialHealth());
        healthPane.setPrefWidth(paneWidth);
        healthPane.setMaxWidth(paneWidth);
        healthPane.setStyle("-fx-background-color: red;");
        //ADD TO UNIT CONTAINER
        unitContainer.getChildren().add(imagePane);
        unitContainer.getChildren().add(healthPane);

        return unitContainer;
    }

    /**
     * Loads main menu when the exit button is pressed
     * @param event button click
     * @throws IOException If fxml load fails
     */
    @FXML
    private void loadMainMenu(ActionEvent event) throws IOException {
        battle.closeMap();
        try {
            Stage window = (Stage) ((Node)event.getSource()).getScene().getWindow();
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("../view/MainMenu.fxml"));
            Parent root = fxmlLoader.load();
            Scene scene = new Scene(root);

            window.setScene(scene);
            window.setResizable(false);
            window.show();

        } catch (IOException e) {
            e.printStackTrace();
            throw e;
        }
    }

    /**
     * Generic alert method
     * @param alertType the type of alert we give
     * @param message message for user
     */
    @FXML
    private void alertUser(Alert.AlertType alertType, String message){
        Alert alert = new Alert(alertType);
        alert.setContentText(message);
        alert.show();
    }
}
