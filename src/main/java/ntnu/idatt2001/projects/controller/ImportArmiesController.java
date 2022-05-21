package ntnu.idatt2001.projects.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseButton;
import javafx.stage.Stage;
import ntnu.idatt2001.projects.io.ArmyFileHandler;
import ntnu.idatt2001.projects.model.simulation.Army;
import ntnu.idatt2001.projects.model.simulation.Battle;
import ntnu.idatt2001.projects.model.units.Unit;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;

/**
 * Controls the import armies page
 */
public class ImportArmiesController implements Initializable {

    //The battle
    private Battle battle;
    //Selected army
    private Army selectedArmy = null;

    //Army file handler
    private ArmyFileHandler armyFileHandler = new ArmyFileHandler();

    //Army list
    @FXML private TableView<Army> armyList;
    @FXML private TableColumn<Army,String> selectArmyColumn;

    //Selected army table
    @FXML private Label armyNameLabel;
    @FXML private TableView<Unit> selectedArmyTable;
    @FXML private TableColumn<Unit,String> typeColumn;
    @FXML private TableColumn<Unit,String> nameColumn;
    @FXML private TableColumn<Unit,String> attackColumn;
    @FXML private TableColumn<Unit,String> healthColumn;
    @FXML private TableColumn<Unit,String> armorColumn;

    //Misc
    @FXML private Label unitCountOutput;
    @FXML private RadioButton radioButton1;
    @FXML private RadioButton radioButton2;
    private ToggleGroup radioToggleGroup;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        //SETS UP THE BATTLE DATA
        setUpBattleData();

        //Initialize the table for displaying armies by their name
        armyList.setEditable(false);
        selectArmyColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        displayArmyList();
        //setRowFactory to get the army that the user has
        //clicked and saved to selectedArmy
        armyList.setRowFactory(TableView -> {
            TableRow<Army> row = new TableRow<>();

            row.setOnMouseClicked(event -> {
                //If single click we select the army for import
                if(!row.isEmpty() && event.getButton() == MouseButton.PRIMARY && event.getClickCount() == 1){
                    selectedArmy = row.getItem();
                    //Update army size
                    unitCountOutput.setText(String.valueOf(selectedArmy.getArmySize()));
                }
                //If double click we load it straight away
                else if(!row.isEmpty() && event.getButton() == MouseButton.PRIMARY && event.getClickCount() == 2){
                    selectedArmy = row.getItem();
                    //Update army size
                    unitCountOutput.setText(String.valueOf(selectedArmy.getArmySize()));
                    displaySelectedArmy();
                }
            });
            return row;
        });

        //Initialize the table for displaying units of single army
        selectedArmyTable.setEditable(false);
        typeColumn.setCellValueFactory(new PropertyValueFactory<>("type"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        attackColumn.setCellValueFactory(new PropertyValueFactory<>("attack"));
        healthColumn.setCellValueFactory(new PropertyValueFactory<>("health"));
        armorColumn.setCellValueFactory(new PropertyValueFactory<>("armor"));

        //Set toggleGroup for army radio buttons
        radioToggleGroup = new ToggleGroup();
        radioButton1.setToggleGroup(radioToggleGroup);
        radioButton1.setText(battle.getArmyOne().getName());
        radioButton2.setToggleGroup(radioToggleGroup);
        radioButton2.setText(battle.getArmyTwo().getName());
    }

    /**
     * Sets up the battle data used by the controller.
     * Initiates the battle field of controller as the
     * two active armies.
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
        battle = new Battle(armyOne,armyTwo);
    }

    /**
     * Displays armies to the army list table.
     * Uses armyFileHandler to get all army save
     * files and adds them to the TableView armyList
     */
    @FXML
    private void displayArmyList(){
        ObservableList<Army> armiesObservable = FXCollections.observableArrayList();
        armyFileHandler = new ArmyFileHandler();
        try{
            armiesObservable.setAll(armyFileHandler.getArmySaveFiles());
        }catch(Exception e){
            e.printStackTrace();
            alertUser(Alert.AlertType.ERROR,"Error occurred while loading files, please try again.");
        }
        armyList.setItems(armiesObservable);
    }

    /**
     * Displays the selected army with all its units.
     * When the user presses the preview button we
     * get the selected army and enter all the units to
     * the unit list
     */
    @FXML
    private void displaySelectedArmy(){
        ObservableList<Unit> unitsObservable = FXCollections.observableArrayList();
        Army army = selectedArmy;
        armyNameLabel.setText(army.getName());
        unitCountOutput.setText(String.valueOf(selectedArmy.getArmySize()));
        if(selectedArmy != null){
            unitsObservable.addAll(army.getCommanderUnits());
            unitsObservable.addAll(army.getInfantryUnits());
            unitsObservable.addAll(army.getRangedUnits());
            unitsObservable.addAll(army.getCavalryUnits());
        }
        selectedArmyTable.setItems(unitsObservable);
    }

    /**
     * Replaces an army with one the user has
     * selected. Which army is replaced is decided
     * using the radio buttons next to the "Replace"
     * button.
     */
    @FXML
    private void replaceSelectedArmy(){
        armyFileHandler = new ArmyFileHandler();
        try{
            //if user has not selected an army throw exception
            if(selectedArmy == null){
                throw new IllegalStateException("New army must be imported before replacing.");
            }
            //Else if neither radio buttons are chosen throw exception
            else if(radioToggleGroup.getSelectedToggle()== null){
                throw new IllegalStateException("Please select which existing army to replace.");
            }

            Army armyOne = battle.getArmyOne();
            Army armyTwo = battle.getArmyTwo();
            //If radioButton1 is chosen we change armyOne
            if(radioToggleGroup.getSelectedToggle().equals(radioButton1)){
                armyOne = selectedArmy;
            }
            //Else if radioButton2 is chosen we change armyTwo
            else if(radioToggleGroup.getSelectedToggle().equals(radioButton2)){
                armyTwo = selectedArmy;
            }

            //Create new battle with the updated armies
            battle = new Battle(armyOne,armyTwo);

            //Save it as active armies
            armyFileHandler.setActiveArmyDirectory();
            armyFileHandler.setActiveArmies(new ArrayList<>(Arrays.asList(battle.getArmyOne(), battle.getArmyTwo())));
            //Positive user feedback
            alertUser(Alert.AlertType.CONFIRMATION,"Army imported successfully");

            //Update text and remove selection
            radioButton1.setText(battle.getArmyOne().getName());
            radioButton2.setText(battle.getArmyTwo().getName());
            radioToggleGroup.getSelectedToggle().setSelected(false);
        }
        catch (IllegalArgumentException | IllegalStateException e){
            alertUser(Alert.AlertType.WARNING,e.getMessage());
        }
        catch(IOException e ){
            e.printStackTrace();
            alertUser(Alert.AlertType.ERROR,"Error while importing army, please try again.");
        }
    }

    /**
     * Loads main menu when the exit button is pressed
     *
     * @param event button click
     */
    @FXML
    private void loadMainMenu(ActionEvent event){
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
            alertUser(Alert.AlertType.ERROR,"There was an error loading the main menu, please try again");
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
