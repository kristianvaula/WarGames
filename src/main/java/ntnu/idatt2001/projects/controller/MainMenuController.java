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
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;
import ntnu.idatt2001.projects.io.ArmyFileHandler;
import ntnu.idatt2001.projects.model.simulation.Army;
import ntnu.idatt2001.projects.model.simulation.Battle;
import ntnu.idatt2001.projects.model.units.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.ResourceBundle;

import static javafx.geometry.Pos.CENTER_LEFT;

/**
 * Controls the main menu page
 */
public class MainMenuController implements Initializable {

    //The battle
    private Battle battle;

    //Army file handler
    private ArmyFileHandler armyFileHandler = new ArmyFileHandler();

    //Constants for icons
    private static final String DLM = File.separator;
    private final static String ICONS_URL = "src" + DLM + "main" + DLM + "resources" + DLM +
                                             "ntnu" + DLM + "idatt2001" + DLM + "projects"+ DLM +
                                             "view" + DLM + "icons" + DLM;

    //Army display boxes
    @FXML private Label armyNameOutputA;
    @FXML private VBox armyUnitsOutputA;
    @FXML private Label armyNameOutputB;
    @FXML private VBox armyUnitsOutputB;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        //Sets up the battle data
        setUpBattleData();

        //Calls display on both armies
        displayArmy(battle.getArmyOne(),armyNameOutputA,armyUnitsOutputA);
        displayArmy(battle.getArmyTwo(),armyNameOutputB,armyUnitsOutputB);
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
     * Displays the armies in the main menu.
     * Creates a list of all unit types and the
     * amount of the given unit. We differentiate
     * between same type units if they have different
     * names
     *
     * @param army Army to display
     * @param armyNameContainer Container for armyname
     * @param unitContainer Container for units
     */
    @FXML
    private void displayArmy(Army army,Label armyNameContainer, VBox unitContainer){
        //Display army name
        armyNameContainer.setText(army.getName());
        //Get units grouped by name
        LinkedHashMap<String, ArrayList<Unit>> unitsByName = army.getUnitsByName();
        List<String> unitNames = unitsByName.keySet().stream().toList();

        //Iterate through the types and create row for each type
        for(String unit : unitNames){
            ArrayList<Unit> specifiedUnits = unitsByName.get(unit);
            HBox rowContainer = new HBox();
            rowContainer.setAlignment(CENTER_LEFT);
            VBox.setMargin(rowContainer,new Insets(5,0,0,10));

            ImageView icon = new ImageView();
            if(getUnitIcons(specifiedUnits.get(0).getClass()) != null){
                icon.setImage(getUnitIcons(specifiedUnits.get(0).getClass()));
                icon.setFitHeight(40.0);
                icon.setFitWidth(40.0);
                rowContainer.getChildren().add(icon);
            }

            Label text = new Label("  " + specifiedUnits.size() + "x " + specifiedUnits.get(0).getName());
            text.getStyleClass().set(0,"menuUnitItem");
            text.setTextAlignment(TextAlignment.CENTER);
            rowContainer.getChildren().add(text);

            unitContainer.getChildren().add(rowContainer);
        }
    }

    /**
     * Gets the corresponding icons based on unit type
     * Returns null if the icons could not be found.
     * @param type Unit type
     * @return Image icon based on type
     */
    @FXML
    private Image getUnitIcons(Class<?> type){
        try{
            if(type == CommanderUnit.class) {
                return new Image(new FileInputStream(ICONS_URL + "commander.png"));
            }
            else if(type == CavalryUnit.class) {
                return new Image(new FileInputStream(ICONS_URL + "knight.png"));
            }
            else if(type == RangedUnit.class) {
                return new Image(new FileInputStream(ICONS_URL + "archer.png"));
            }
            else if(type == InfantryUnit.class){
                return new Image(new FileInputStream(ICONS_URL + "infantry.png"));
            }
        }catch (FileNotFoundException e){
            return null;
        }
        return null;
    }

    /**
     * Loads simulate tournament scene when corresponding button
     * is pressed
     * @param event button click
     * @throws IOException If fxml load fails
     */
    @FXML
    private void loadSimulateBattle(ActionEvent event) throws IOException {
        try {
            Stage window = (Stage) ((Node)event.getSource()).getScene().getWindow();
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("../view/SimulateBattle.fxml"));
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
     * Loads edit armies scene when corresponding button
     * is pressed
     * @param event button click
     * @throws IOException If fxml load fails
     */
    @FXML
    private void loadEditArmies(ActionEvent event) throws IOException {
        try {
            Stage window = (Stage) ((Node)event.getSource()).getScene().getWindow();
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("../view/EditArmies.fxml"));
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
     * Loads import armies scene when corresponding button
     * is pressed
     * @param event button click
     * @throws IOException If fxml load fails
     */
    @FXML
    private void loadImportArmies(ActionEvent event) throws IOException {
        try {
            Stage window = (Stage) ((Node)event.getSource()).getScene().getWindow();
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("../view/ImportArmies.fxml"));
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
}
