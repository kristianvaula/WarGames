package ntnu.idatt2001.projects;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;

public class WargamesApplication extends Application{


    @Override
    public void start(Stage primaryStage) throws IOException {
        //Add event handler to close button so that we are sure everything closes
        primaryStage.setOnCloseRequest(event -> {
            Platform.exit();
            System.exit(0);
        });
        
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("view/MainMenu.fxml"));
            Parent root = fxmlLoader.load();
            primaryStage.initStyle(StageStyle.TRANSPARENT);
            Scene scene = new Scene(root);

            primaryStage.setScene(scene);
            primaryStage.setResizable(false);
            primaryStage.show();

        } catch (IOException e) {
            e.printStackTrace();
            throw e;
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
