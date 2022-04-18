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
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import ntnu.idatt2001.projects.io.ArmyFileHandler;
import ntnu.idatt2001.projects.model.simulation.Army;
import ntnu.idatt2001.projects.model.simulation.Battle;
import ntnu.idatt2001.projects.model.units.Unit;

import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.ResourceBundle;

public class SimulateBattleController implements Initializable {

    //The battle
    Battle battle;
    Army selectedArmy = null;

    //Max unit health used to base health bars on
    int maxUnitHealth = 0;
    //Used to get information about the unit when hovering over it
    HashMap<ImageView,Unit> unitAndImageViewLinker = new HashMap<>();

    //Army information displays
    @FXML Label armyName1;
    @FXML Label armyName2;
    @FXML FlowPane unitFlowPane1;
    @FXML FlowPane unitFlowPane2;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // GIVES US BATTLE TO WORK WITH BEFORE IMPLEMENTING EMBEDDED DATABASE
        //TODO REMOVE WHEN TEMPORARY STORAGE IMPLEMENTED
        try{
            ArmyFileHandler fileHandler = new ArmyFileHandler();
            Army humanArmy = fileHandler.getArmyFromFile("Human Army");
            Army orcArmy = fileHandler.getArmyFromFile("Orc Army");
            battle = new Battle(humanArmy,orcArmy);
            armyName1.setText(battle.getArmyOne().getName());
            armyName2.setText(battle.getArmyTwo().getName());
            maxUnitHealth = setMaxUnitHealth();
            displayUnitsAndHealth();
        }catch (IOException e){
            e.printStackTrace();
            alertUser(Alert.AlertType.ERROR,"Error occoured while loading files");
        }
    }

    /**
     * Calls for generateUnitVBox to display all units
     */
    @FXML
    public void displayUnitsAndHealth(){
        for(Unit unit : battle.getArmyOne().getAllUnits()){
            VBox unitContainer = generateUnitVBox(unit);
            unitFlowPane1.getChildren().add(unitContainer);
            unitFlowPane1.setMargin(unitContainer,new Insets(2,2,2,2));
        }
        for(Unit unit: battle.getArmyTwo().getAllUnits()){
            VBox unitContainer = generateUnitVBox(unit);
            unitFlowPane2.getChildren().add(unitContainer);
            unitFlowPane2.setMargin(unitContainer,new Insets(2,2,2,2));
        }
    }

    /**<VBox prefHeight="43.0" prefWidth="30.0" style="-fx-border-color: #191308;">
     <children>
         <Pane prefHeight="35.0" prefWidth="30.0" style="-fx-background-color: #646E78;">
         <children>
             <ImageView fitHeight="31.0" fitWidth="32.0" pickOnBounds="true" preserveRatio="true">
             <image>
                 <Image url="@icons/infantry.png" />
                 </image>
             </ImageView>
         </children>
         </Pane>
         <Pane prefHeight="8.0" prefWidth="30.0" style="-fx-background-color: red;" />
     </children>
     <FlowPane.margin>
     <Insets bottom="2.0" left="2.0" right="2.0" top="2.0" />
     </FlowPane.margin>
     </VBox>*/
    @FXML
    public VBox generateUnitVBox(Unit unit){
        VBox unitContainer = new VBox();
        unitContainer.setPrefHeight(45);
        unitContainer.setMinHeight(45);
        unitContainer.setPrefHeight(30);
        unitContainer.setStyle("-fx-border-color:#191308;");
        //UNIT ICON
        VBox imagePane = new VBox();
        imagePane.setStyle("fx-background-color: gray;");
        imagePane.setPrefHeight(35);
        imagePane.setPrefHeight(30);

        ImageView image = new ImageView();
        if(unit.getType().equals("Infantry Unit")){
            image.setImage(new Image(String.valueOf(getClass().getResource("../view/icons/infantry.png"))));
        }
        else if(unit.getType().equals("Ranged Unit")){
            image.setImage(new Image(String.valueOf(getClass().getResource("../view/icons/archer.png"))));
        }
        else if(unit.getType().equals("Cavalry Unit")){
            image.setImage(new Image(String.valueOf(getClass().getResource("../view/icons/knight.png"))));
        }
        else if(unit.getType().equals("Commander Unit")){
            image.setImage(new Image(String.valueOf(getClass().getResource("../view/icons/commander.png"))));
        }
        image.setFitHeight(35);
        image.setFitWidth(30);
        imagePane.getChildren().add(image);
        //HEALTH BAR
        VBox healthPane = new VBox();
        healthPane.setPrefHeight(8);
        double paneWidth = Math.floorDiv(unit.getHealth()*30,maxUnitHealth);
        healthPane.setPrefWidth(paneWidth);
        healthPane.setMaxWidth(paneWidth);
        healthPane.setStyle("-fx-background-color: red;");
        //ADD TO UNIT CONTAINER
        unitContainer.getChildren().add(imagePane);
        unitContainer.getChildren().add(healthPane);
        return unitContainer;
    }

    /**
     * Finds the highest unit health for both armies.
     */
    @FXML
    public int setMaxUnitHealth(){
        int maxUnitHealth = 0;
        ArrayList<Unit> allUnits = new ArrayList<>();
        allUnits.addAll(battle.getArmyOne().getAllUnits());
        allUnits.addAll(battle.getArmyTwo().getAllUnits());
        for (Unit unit : allUnits){
            if(maxUnitHealth < unit.getHealth()){
                maxUnitHealth = unit.getHealth();
            }
        }
        return maxUnitHealth;
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
