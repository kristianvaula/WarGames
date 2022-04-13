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
import java.util.ResourceBundle;

public class ImportArmiesController implements Initializable {

    //The battle
    Battle battle;
    Army selectedArmy = null;

    //Army list
    @FXML TableView<Army> armyList;
    @FXML TableColumn<Army,String> selectArmyColumn;

    //Selected army table
    @FXML Label armyNameLabel;
    @FXML TableView<Unit> selectedArmyTable;
    @FXML TableColumn<Unit,String> typeColumn;
    @FXML TableColumn<Unit,String> nameColumn;
    @FXML TableColumn<Unit,String> attackColumn;
    @FXML TableColumn<Unit,String> healthColumn;
    @FXML TableColumn<Unit,String> armorColumn;

    //Misc
    @FXML RadioButton radioButton1;
    @FXML RadioButton radioButton2;
    private ToggleGroup radioToggleGroup;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        //Initialize the table for displaying armies by their name
        armyList.setEditable(false);
        selectArmyColumn.setCellValueFactory(new PropertyValueFactory<>("name"));

        //Initialize the table for displaying units of single army
        selectedArmyTable.setEditable(false);
        typeColumn.setCellValueFactory(new PropertyValueFactory<>("type"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        attackColumn.setCellValueFactory(new PropertyValueFactory<>("attack"));
        healthColumn.setCellValueFactory(new PropertyValueFactory<>("health"));
        armorColumn.setCellValueFactory(new PropertyValueFactory<>("armor"));

        //Set toggleGroup
        radioToggleGroup = new ToggleGroup();
        radioButton1.setToggleGroup(radioToggleGroup);
        radioButton2.setToggleGroup(radioToggleGroup);

        //setRowFactory to get the army of selected row when we
        //press it and then call the displaySelectedArmy
        armyList.setRowFactory(TableView -> {
            TableRow<Army> row = new TableRow<>();

            row.setOnMouseClicked(event -> {
                //If single click we select the army for import
                if(!row.isEmpty() && event.getButton() == MouseButton.PRIMARY && event.getClickCount() == 1){
                    selectedArmy = row.getItem();
                }
                //If double click we load it straight away
                else if(!row.isEmpty() && event.getButton() == MouseButton.PRIMARY && event.getClickCount() == 2){
                    selectedArmy = row.getItem();
                    displaySelectedArmy();
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
            displayArmyList();
            radioButton1.setText(battle.getArmyOne().getName());
            radioButton2.setText(battle.getArmyTwo().getName());
        }catch (IOException e){
            e.printStackTrace();
            alertUser(Alert.AlertType.ERROR,"Error occoured while loading files");
        }


    }

    /**
     * Displays armies to the army list table
     */
    @FXML
    public void displayArmyList(){
        ObservableList<Army> armiesObservable = FXCollections.observableArrayList();
        ArmyFileHandler armyFileHandler = new ArmyFileHandler();
        try{
            armiesObservable.setAll(armyFileHandler.getArmySavefiles());
        }catch(Exception e){
            e.printStackTrace();
            alertUser(Alert.AlertType.ERROR,"Error occurred while loading files, please try again.");
        }
        armyList.setItems(armiesObservable);
    }

    /**
     * Displays the selected army with all its units
     */
    @FXML
    public void displaySelectedArmy(){
        ObservableList<Unit> unitsObservable = FXCollections.observableArrayList();
        Army army = selectedArmy;
        armyNameLabel.setText(army.getName());
        if(army != null){
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
    public void replaceSelectedArmy(){
        if(selectedArmy != null){
            Army newArmy = selectedArmy;
            if(radioToggleGroup.getSelectedToggle().equals(radioButton1)){
                try{
                    Battle replacementBattle = new Battle(selectedArmy,battle.getArmyTwo());
                    battle = replacementBattle;
                    radioToggleGroup.getSelectedToggle().setSelected(false);
                }
                catch (IllegalArgumentException e){
                    alertUser(Alert.AlertType.ERROR,e.getMessage());
                }
                //TODO UPDATE BATTLE WHEN TEMP STORAGE IMPLEMENTED
            }
            else if(radioToggleGroup.getSelectedToggle().equals(radioButton2)){
                try{
                    Battle replacementBattle = new Battle(battle.getArmyOne(),selectedArmy);
                    battle = replacementBattle;
                    radioToggleGroup.getSelectedToggle().setSelected(false);
                }
                catch (IllegalArgumentException e){
                    alertUser(Alert.AlertType.ERROR,e.getMessage());
                }
                //TODO UPDATE BATTLE WHEN TEMP STORAGE IMPLEMENTED
            }
            else{
                alertUser(Alert.AlertType.WARNING,"Please select which existing army to replace.");
            }
        }
        else{
            alertUser(Alert.AlertType.WARNING,"New army must be imported before replacing");
        }
        radioButton1.setText(battle.getArmyOne().getName());
        radioButton2.setText(battle.getArmyTwo().getName());
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
