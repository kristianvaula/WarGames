package ntnu.idatt2001.projects;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class WargamesApplication extends Application{


    @Override
    public void start(Stage primaryStage) throws IOException {

        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("view/MainMenu.fxml"));
            Parent root = fxmlLoader.load();
            Scene scene = new Scene(root);

            primaryStage.setScene(scene);
            primaryStage.setResizable(false);
            primaryStage.show();

        } catch (IOException e) {
            System.out.println(e.getCause());
            throw e;
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
