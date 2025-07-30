package gui;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class SceneManager {
    private static Stage stage;

    public static void setStage(Stage primaryStage) {
        stage = primaryStage;
    }

    public static void switchScene(String fxmlPath) throws IOException {
        Parent root = FXMLLoader.load(SceneManager.class.getResource(fxmlPath));
        Scene newScene = new Scene(root);
        newScene.getStylesheets().add(SceneManager.class.getResource("css/style.css").toExternalForm());
        stage.setScene(newScene);
    }
}
