package gui.controllers;

import gui.SceneManager;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;

public class DataStructureController {
	@FXML
    private void handleSwitchToHome(ActionEvent event) {
        try {
            SceneManager.switchScene("layouts/main.fxml");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
