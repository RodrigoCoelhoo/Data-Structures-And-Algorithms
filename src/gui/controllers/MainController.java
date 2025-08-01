package gui.controllers;

import gui.SceneManager;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;

public class MainController {
	
	@FXML
    private void handleSwitchToAlgorithmSort(ActionEvent event) {
        try {
            SceneManager.switchScene("layouts/algorithm_sort.fxml");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleSwitchToAlgorithmSearch(ActionEvent event) {
        try {
            SceneManager.switchScene("layouts/algorithm_search.fxml");
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
