package ntnu.idatt2001.projects.controller;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
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
import ntnu.idatt2001.projects.model.units.UnitFactory;
import ntnu.idatt2001.projects.model.units.UnitType;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;

/**
 * Controls the edit army page
 */
public class EditArmiesController implements Initializable {

    //The battle
    private Battle battle;
    private Army selectedArmy = null;
    private Unit selectedUnit = null;

    //Army file handler
    private ArmyFileHandler armyFileHandler = new ArmyFileHandler();

    //Add unit
    @FXML private ChoiceBox<String> typeInput;
    @FXML private TextField nameInput;
    @FXML private TextField attackInput;
    @FXML private TextField healthInput;
    @FXML private TextField armorInput;

    //Army table
    @FXML private TableView<Unit> unitDisplayTable;
    @FXML private TableColumn<Unit,String> typeColumn;
    @FXML private TableColumn<Unit,String> nameColumn;
    @FXML private TableColumn<Unit,String> attackColumn;
    @FXML private TableColumn<Unit,String> healthColumn;
    @FXML private TableColumn<Unit,String> armorColumn;

    //Misc
    @FXML private TextField armyNameInput;
    @FXML private RadioButton radioButton1;
    @FXML private RadioButton radioButton2;
    @FXML private Label unitCountOutput;
    private ToggleGroup radioToggleGroup;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        //SETS UP THE BATTLE DATA
        setUpBattleData();

        //Set toggleGroup and text
        radioToggleGroup = new ToggleGroup();
        radioButton1.setToggleGroup(radioToggleGroup);
        radioButton1.setText(battle.getArmyOne().getName());
        radioButton2.setToggleGroup(radioToggleGroup);
        radioButton2.setText(battle.getArmyTwo().getName());

        //Everytime the radio button selection changes we need to update which
        //army we are working on
        radioToggleGroup.selectedToggleProperty().addListener(new ChangeListener<Toggle>() {
            @Override
            public void changed(ObservableValue<? extends Toggle> changed, Toggle oldVal, Toggle newVal) {
                //Check if we need to do anything
                if(oldVal != newVal){
                    //Check which army the new choice is
                    if(newVal == radioButton1){
                        selectedArmy = battle.getArmyOne();
                    }
                    else if(newVal == radioButton2){
                        selectedArmy =  battle.getArmyTwo();
                    }
                    //Change text and display units
                    armyNameInput.setText(selectedArmy.getName());
                    displayUnits();
                }
            }
        });

        //Add unit type items to choicebox
        for(UnitType type : UnitType.values()){
            typeInput.getItems().add(type.toString());
        }// Select Infantry as default
        typeInput.setValue(UnitType.INFANTRY.toString());

        //Initialize the table for displaying units of the selected army
        unitDisplayTable.setEditable(false);
        unitDisplayTable.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        typeColumn.setCellValueFactory(new PropertyValueFactory<>("type"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        attackColumn.setCellValueFactory(new PropertyValueFactory<>("attack"));
        healthColumn.setCellValueFactory(new PropertyValueFactory<>("health"));
        armorColumn.setCellValueFactory(new PropertyValueFactory<>("armor"));

        //setRowFactory to get the selected item if we press on it
        //If double click we load the unit fields into the add unit
        //inputs to easily create more
        unitDisplayTable.setRowFactory(TableView -> {
            TableRow<Unit> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                //If single click we select the army for import
                if(!row.isEmpty() && event.getButton() == MouseButton.PRIMARY && event.getClickCount() == 1){
                    selectedUnit = row.getItem();
                }
                else if (!row.isEmpty() && event.getButton() == MouseButton.PRIMARY && event.getClickCount() == 2){
                    selectedUnit = row.getItem();
                    typeInput.setValue(selectedUnit.getType());
                    nameInput.setText(selectedUnit.getName());
                    attackInput.setText(String.valueOf(selectedUnit.getAttack()));
                    healthInput.setText(String.valueOf(selectedUnit.getHealth()));
                    armorInput.setText(String.valueOf(selectedUnit.getArmor()));
                }
            });
            return row;
        });
    }

    /**
     * Sets up the battle used by the controller.
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
     * Adds a unit when user presses add unit.
     * Checks that there are values in each field.
     * Then converts to integer, checks that they
     * are above zero and creates unit.
     *
     * @param event Mouseclick
     */
    @FXML
    private void addUnit(ActionEvent event){
        if(selectedArmy != null) {
            try {
                if(selectedArmy.getArmySize() >= 500){
                    throw new IllegalStateException("Maximum unit capacity reached, please delete units before adding more");
                }

                //Check for blanks
                if (nameInput.getText().isBlank()) {
                    throw new IllegalArgumentException("Please enter name for unit");
                } else if (attackInput.getText().isBlank()) {
                    throw new IllegalArgumentException("Please enter attack value for unit");
                } else if (healthInput.getText().isBlank()) {
                    throw new IllegalArgumentException("Please enter health value for unit");
                } else if (armorInput.getText().isBlank()) {
                    throw new IllegalArgumentException("Please enter armor value for unit");
                }
                //Check that values are numbers above zero
                int attack = Integer.parseInt(attackInput.getText());
                int health = Integer.parseInt(healthInput.getText());
                int armor = Integer.parseInt(armorInput.getText());
                if (attack <= 1 || health <= 1 || armor <= 1) {
                    throw new IllegalArgumentException("Attack, health and armor values must be a positive number");
                }
                //Gets type
                for (UnitType type : UnitType.values()) {
                    if (typeInput.getValue().equals(type.toString())) {
                        selectedArmy.add(UnitFactory.getUnit(type, nameInput.getText(), health, attack, armor));
                    }
                }
                displayUnits();
            } catch (IllegalArgumentException e) {
                alertUser(Alert.AlertType.WARNING, e.getMessage());
            }
        }
        else alertUser(Alert.AlertType.WARNING,"Please select an army before adding unit");
    }

    /**
     * Displays name and units of selected army.
     * Gets the selected army and adds all units
     * to table.
     */
    @FXML
    private void displayUnits(){
        ObservableList<Unit> unitsObservable = FXCollections.observableArrayList();
        Army army = selectedArmy;

        //Display the size
        unitCountOutput.setText(String.valueOf(selectedArmy.getArmySize()));
        if(army != null){
            unitsObservable.addAll(army.getCommanderUnits());
            unitsObservable.addAll(army.getInfantryUnits());
            unitsObservable.addAll(army.getRangedUnits());
            unitsObservable.addAll(army.getCavalryUnits());
            unitDisplayTable.setItems(unitsObservable);
        }
    }

    /**
     * Removes selected Unit when user presses delete button
     *
     * @param event Mouseclick
     */
    @FXML
    private void removeUnit(ActionEvent event){
        if(selectedUnit != null){
            selectedArmy.remove(unitDisplayTable.getSelectionModel().getSelectedItem());
            displayUnits();
        }
    }

    /**
     * Saves the army to file when the "save changes"
     * button is pressed. If the name of the army is
     * unchanged we will update the previous army file.
     * If army name is changed, we will save the army as a
     * new file. When we save the active armies are also updated.
     *
     * @param event Mouseclick
     */
    @FXML
    private void saveChanges(ActionEvent event){
        //Check that we have selected an army
        if(selectedArmy != null){
            armyFileHandler = new ArmyFileHandler();
            try {
                //Change selected army name if changed
                if(!armyNameInput.getText().equals(selectedArmy.getName())){
                    selectedArmy.setName(armyNameInput.getText());
                }
                //Save army
                armyFileHandler.setDefaultDirectory();
                armyFileHandler.writeArmyToFile(selectedArmy);
                //Update the active armies
                armyFileHandler.setActiveArmyDirectory();
                List<Army> armies = new ArrayList<>(Arrays.asList(battle.getArmyOne(), battle.getArmyTwo()));
                armyFileHandler.setActiveArmies(armies);

                //Change radiobuttons text
                radioButton1.setText(armies.get(0).getName());
                radioButton2.setText(armies.get(1).getName());

                //Positive user feedback
                alertUser(Alert.AlertType.CONFIRMATION,"Changes were successfully saved");
            }
            catch (IOException e) { // Alert user if failed
                e.printStackTrace();
                alertUser(Alert.AlertType.ERROR,e.getMessage());
            }
            catch (IllegalArgumentException e){
                alertUser(Alert.AlertType.WARNING,e.getMessage());
            }
        }
    }

    /**
     * Loads main menu when the exit button is pressed.
     *
     * @param event button click
     * @throws IOException If fxml load fails
     */
    @FXML
    private void loadMainMenu(ActionEvent event) throws IOException {
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
