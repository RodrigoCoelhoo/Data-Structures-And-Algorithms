package gui.controllers;

import algorithms.search.Grid;
import algorithms.search.Grid.Cell;
import gui.SceneManager;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.SplitPane;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Rectangle;

public class AlgorithmSearchController {

    Grid grid;

    @FXML private Pane visualContainer;
    @FXML private SplitPane splitPane;

    //private final Color visitingColor = Color.web("#90CAF9");       
    //private final Color visitedColor = Color.web("#1976D2"); 

    @FXML
    public void initialize() {
        //algorithmComboBox.getItems().addAll("Bubble Sort", "Bucket Sort", "Insertion Sort", "Selection Sort", "Merge Sort", "Quick Sort");
        //algorithmComboBox.setOnAction(this::updateAlgorithm);
        
        splitPane.setDividerPositions(0.2);
        splitPane.getDividers().get(0).positionProperty().addListener((_, _, _) -> {
            splitPane.setDividerPositions(0.2); 
        });

        javafx.application.Platform.runLater(() -> {
            this.grid = new Grid(90, 120, visualContainer);
            drawGrid();
        });
    }

	@FXML
    private void handleSwitchToHome(ActionEvent event) {
        try {
            SceneManager.switchScene("layouts/main.fxml");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void drawGrid() { 
        visualContainer.getChildren().clear();
        Cell[][] grid = this.grid.getGrid();
        
        for(int i = 0; i < grid.length; i++) 
        {
            for(int j = 0; j < grid[i].length; j++)
            {
                Cell cell = grid[i][j];
                Rectangle rect = cell.getRect();

                visualContainer.getChildren().add(rect);
            }
        }
    }
}
