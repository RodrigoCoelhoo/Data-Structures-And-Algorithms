package gui.controllers;

import java.net.URI;

import gui.SceneManager;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.input.MouseEvent;
import java.awt.Desktop;

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

    @FXML
    public void openGitHub(MouseEvent event) {
    try {
        Desktop.getDesktop().browse(new URI("https://github.com/RodrigoCoelhoo/Data-Structures-And-Algorithms"));
    } catch (Exception e) {
        e.printStackTrace();
    }
}
}
