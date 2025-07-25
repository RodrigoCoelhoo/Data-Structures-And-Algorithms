package gui;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("layouts/algorithm.fxml"));
        primaryStage.setTitle("Data Structure and Algorithm Visualizer");
        Scene scene = new Scene(root);
        scene.getStylesheets().add(getClass().getResource("css/style.css").toExternalForm());
        
        primaryStage.setScene(scene);

        Image icon = new Image("images/icon.png");
        primaryStage.getIcons().add(icon);
        primaryStage.setResizable(false);

        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}