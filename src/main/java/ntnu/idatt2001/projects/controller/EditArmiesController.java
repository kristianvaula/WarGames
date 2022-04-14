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
import java.util.ResourceBundle;

public class EditArmiesController implements Initializable {

    //The battle
    Battle battle;
    Army selectedArmy = null;
    Unit selectedUnit = null;

    //Add unit
    @FXML ChoiceBox<String> typeInput;
    @FXML TextField nameInput;
    @FXML TextField attackInput;
    @FXML TextField healthInput;
    @FXML TextField armorInput;


    //Army table
    @FXML TableView<Unit> unitDisplayTable;
    @FXML TableColumn<Unit,String> typeColumn;
    @FXML TableColumn<Unit,String> nameColumn;
    @FXML TableColumn<Unit,String> attackColumn;
    @FXML TableColumn<Unit,String> healthColumn;
    @FXML TableColumn<Unit,String> armorColumn;

    //Misc
    @FXML TextField armyNameInput;
    @FXML RadioButton radioButton1;
    @FXML RadioButton radioButton2;
    private ToggleGroup radioToggleGroup;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        //Set toggleGroup
        radioToggleGroup = new ToggleGroup();
        radioButton1.setToggleGroup(radioToggleGroup);
        radioButton2.setToggleGroup(radioToggleGroup);

            //Everytime the radio button selection changes we need to update which
            //army we are working on
        radioToggleGroup.selectedToggleProperty().addListener(new ChangeListener<Toggle>() {
            @Override
            public void changed(ObservableValue<? extends Toggle> changed, Toggle oldVal, Toggle newVal) {
                if((RadioButton)newVal == radioButton1){
                    selectedArmy = battle.getArmyOne();
                }
                else if((RadioButton)newVal == radioButton2){
                    selectedArmy =  battle.getArmyTwo();
                }
                displayArmy();
            }
        });

        //Add units fields
        for(UnitType type : UnitType.values()){
            typeInput.getItems().add(type.toString());
        }
        typeInput.setValue(UnitType.INFANTRY.toString());

        //Initialize the table for displaying units of single army
        unitDisplayTable.setEditable(false);
        unitDisplayTable.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        typeColumn.setCellValueFactory(new PropertyValueFactory<>("type"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        attackColumn.setCellValueFactory(new PropertyValueFactory<>("attack"));
        healthColumn.setCellValueFactory(new PropertyValueFactory<>("health"));
        armorColumn.setCellValueFactory(new PropertyValueFactory<>("armor"));

            //setRowFactory to get the army of selected row when
            //user clicks on it
        unitDisplayTable.setRowFactory(TableView -> {
            TableRow<Unit> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                //If single click we select the army for import
                if(!row.isEmpty() && event.getButton() == MouseButton.PRIMARY && event.getClickCount() == 1){
                    selectedUnit = row.getItem();
                }
            });
            return row;
        });

        // GIVES US BATTLE TO WORK WITH BEFORE IMPLEMENTING EMBEDDED DATABASE
        //TODO REMOVE WHEN TEMPORARY STORAGE IMPLEMENTED
        try{
            ArmyFileHandler fileHandler = new ArmyFileHandler();
            Army humanArmy = fileHandler.getArmyFromFile("Human Army");
            Army orcArmy = fileHandler.getArmyFromFile("Orc Army");
            battle = new Battle(humanArmy,orcArmy);
            radioButton1.setText(battle.getArmyOne().getName());
            radioButton2.setText(battle.getArmyTwo().getName());
        }catch (IOException e){
            e.printStackTrace();
            alertUser(Alert.AlertType.ERROR,"Error occoured while loading files");
        }
    }

    //TODO ADD SAVE CHANGES METHOD

    /**
     * Displays name and units of selected army
     */
    @FXML
    public void displayArmy(){
        ObservableList<Unit> unitsObservable = FXCollections.observableArrayList();
        Army army = selectedArmy;

        if(army != null){
            armyNameInput.setText(army.getName());
            unitsObservable.addAll(army.getCommanderUnits());
            unitsObservable.addAll(army.getInfantryUnits());
            unitsObservable.addAll(army.getRangedUnits());
            unitsObservable.addAll(army.getCavalryUnits());
            unitDisplayTable.setItems(unitsObservable);
        }
    }

    /**
     * Adds a unit when user presses add unit.
     */
    @FXML
    public void addUnit(){
        if(nameInput.getText().isBlank()){
            alertUser(Alert.AlertType.WARNING,"Please enter name for unit");
        }
        else if(attackInput.getText().isBlank()){
            alertUser(Alert.AlertType.WARNING,"Please enter attack value for unit");
        }
        else if(healthInput.getText().isBlank()){
            alertUser(Alert.AlertType.WARNING,"Please enter health value for unit");
        }
        else if(armorInput.getText().isBlank()){
            alertUser(Alert.AlertType.WARNING,"Please enter armor value for unit");
        }
        else{
            int attack,health,armor;
            try{
                attack = Integer.parseInt(attackInput.getText());
                health = Integer.parseInt(healthInput.getText());
                armor = Integer.parseInt(armorInput.getText());
                if(attack < 1 || health < 1 || armor < 1){
                    throw new NumberFormatException("Invalid values, must be above zero");
                }
                for(UnitType type : UnitType.values()){
                    if(typeInput.getValue().equals(type.toString())){
                        selectedArmy.add(UnitFactory.getUnit(type,nameInput.getText(),health,attack,armor));
                    }
                }
                displayArmy();
            }catch (NumberFormatException e){
                e.printStackTrace();
                alertUser(Alert.AlertType.WARNING,"Attack, health and armor values must be a positive number");
                ;
            }
        }
    }

    /**
     * Removes selected Unit when user presses delete button
     */
    @FXML
    public void removeUnit(){
        if(selectedUnit != null){
            selectedArmy.remove(unitDisplayTable.getSelectionModel().getSelectedItem());
            displayArmy();
        }
    }

    /**
     * Loads main menu when the exit button is pressed
     * @param event button click
     * @throws IOException If fxml load fails
     */
    @FXML
    public void loadMainMenu(ActionEvent event) throws IOException {
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
