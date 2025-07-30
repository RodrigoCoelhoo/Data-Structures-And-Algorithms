package gui.controllers;

import gui.SceneManager;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;

public class MainController {
	
	@FXML
    private void handleSwitchToAlgorithm(ActionEvent event) {
        try {
            SceneManager.switchScene("layouts/algorithm.fxml");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

	@FXML
    private void handleSwitchToDataStructure(ActionEvent event) {
        try {
            SceneManager.switchScene("layouts/datastructure.fxml");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
