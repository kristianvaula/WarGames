package ntnu.idatt2001.projects.controller;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.event.ActionEvent;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * Controller for TopBar.fxml which is used by all application scenes
 */
public class TopBarController implements Initializable {

    // Offsets
    private double xOffset = 0;
    private double yOffset = 0;

    //Fxml nodes
    @FXML Pane topBarPane;
    @FXML Button closeButton;
    @FXML Button minimizeButton;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
    }

    /**
     * Closes the application when the X button is clicked
     * @param event click on X button
     */
    @FXML
    protected void handleCloseAction(ActionEvent event){
        Stage stage = (Stage)closeButton.getScene().getWindow();
        stage.close();
    }

    /**
     * Minimizes the application when the _ button is clicked
     * @param event click on _ button
     */
    @FXML
    protected void handleMinimizeAction(ActionEvent event){
        Stage stage = (Stage)minimizeButton.getScene().getWindow();
        stage.setIconified(true);
    }

    /**
     * Sets the offset of x and y when the bar is clicked
     * @param event click on top bar
     */
    @FXML
    protected void handleClickAction(MouseEvent event){
        Stage stage = (Stage)topBarPane.getScene().getWindow();
        xOffset = stage.getX() - event.getScreenY();
        yOffset = stage.getY() - event.getScreenX();
    }

    /**
     * Places the window when it is dragged
     * @param event top bar dragged
     */
    @FXML
    protected void handleMovementAction(MouseEvent event){
        Stage stage = (Stage)topBarPane.getScene().getWindow();
        stage.setX(event.getScreenX() + xOffset);
        stage.setY(event.getScreenY() + yOffset);
    }
}
