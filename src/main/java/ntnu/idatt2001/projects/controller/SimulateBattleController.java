package ntnu.idatt2001.projects.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class SimulateBattleController implements Initializable {
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

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
            System.out.println(e.getCause());
            throw e;
        }
    }
}
