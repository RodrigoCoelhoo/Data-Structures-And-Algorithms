package gui.controllers;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import algorithms.search.AStar;
import algorithms.search.Grid;
import algorithms.search.Grid.Cell;
import algorithms.search.Grid.Point;
import algorithms.search.INode;
import algorithms.search.SearchAlgorithm;
import gui.SceneManager;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.SplitPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.stage.FileChooser;
import utils.GridSnapshot;
import utils.State;

public class AlgorithmSearchController {

    Grid grid;
    private SearchAlgorithm algorithm = null;
    private int currentState = -1;

    private Timeline timeline;

    @FXML private Pane visualContainer;
    @FXML private SplitPane splitPane;
    @FXML private Label stateLabel, gridWarning, animationWarning, objectiveLabel, startLabel;
    @FXML private Button searchButton, lastButton, previousButton, nextButton, firstButton, clearButton, uploadButton, generateButton, skipAnimation;
    @FXML private ProgressBar progressBar;
    @FXML private ComboBox<String> algorithmComboBox;
    @FXML private TextArea algorithmInfo;
    @FXML private TextField rowsField, columnsField, animationDuration;
    @FXML private Circle startCircle, objectiveCircle;

    private final Color warningColor = Color.RED;

    @FXML
    public void initialize() {
        algorithmComboBox.getItems().addAll("A*", "Bucket Sort", "Insertion Sort", "Selection Sort", "Merge Sort", "Quick Sort");
        algorithmComboBox.setOnAction(this::updateAlgorithm);
        
        splitPane.setDividerPositions(0.2);
        splitPane.getDividers().get(0).positionProperty().addListener((_, _, _) -> {
            splitPane.setDividerPositions(0.2); 
        });

        javafx.application.Platform.runLater(() -> {
            this.grid = new Grid(10, 12, visualContainer);
            rowsField.setText("10");
            columnsField.setText("12");
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

    private void updateAlgorithm(ActionEvent e)
    {
        switch (algorithmComboBox.getValue()) {
            case "A*":
                this.algorithm = new AStar();
                refreshUI();
                break;
            case "IDA*":
                //this.algorithm = new
                refreshUI();
                break;
            case "Djisktra's":
                //this.algorithm = new 
                refreshUI();
                break;
            case "DFS":
                //this.algorithm = new 
                refreshUI();
                break;
            case "BFS":
                //this.algorithm = new 
                refreshUI();
                break;
            default:
                break;
        }
    }

    private void refreshUI() {
        algorithmInfo.setText(algorithm.info());
        //updateWarning("clear", null, null);
        drawGrid();
    }

    public void updateWarning(String warning, String text, Color warningColor) {
        switch (warning) {
            case "gridWarning":
                gridWarning.setText(text);
                gridWarning.setTextFill(warningColor);
                break;
            case "animationWarning":
                animationWarning.setText(text);
                animationWarning.setTextFill(warningColor);
                break;
            case "objectiveStatus":
                objectiveCircle.setFill(warningColor);
                objectiveLabel.setText(text);
                objectiveLabel.setTextFill(warningColor);
                break;
            case "startStatus":
                objectiveCircle.setFill(warningColor);
                objectiveLabel.setText(text);
                objectiveLabel.setTextFill(warningColor);
                break;
            case "clear":
                gridWarning.setText("");
                animationWarning.setText("");
                break;
            default:
                break;
        }
    }

    private void buttonState(boolean state) {
        searchButton.setDisable(!state);
        lastButton.setDisable(!state);
        previousButton.setDisable(!state);
        nextButton.setDisable(!state);
        firstButton.setDisable(!state);
        clearButton.setDisable(!state);
        uploadButton.setDisable(!state);
        generateButton.setDisable(!state);
        algorithmComboBox.setDisable(!state);
        animationDuration.setDisable(!state);
        rowsField.setDisable(!state);
        columnsField.setDisable(!state);
    }

    /**
     * Logic methods (Grid)
     */

    public void generateGrid(ActionEvent e) {
        if(!isValid(rowsField.getText()) || !isValid(columnsField.getText())) {
            updateWarning("gridWarning", "The input must be a valid integer between [2-100]", warningColor);
            return;
        }
        
        int rows = Integer.parseInt(rowsField.getText());
        int columns = Integer.parseInt(columnsField.getText());

        if((rows < 2 || rows > 100) || (columns < 2 || columns > 100)) {
            updateWarning("gridWarning", "The input must be a valid integer between [2-100]", warningColor);
            return;
        }

        updateWarning("clear", null, null);

        this.grid = new Grid(rows, columns, visualContainer);
        drawGrid();
    }
    
    public boolean isValid(String text) {
        try {
            int _ = Integer.parseInt(text);
            return true;
        } catch(NumberFormatException e) {
            return false;
        }
    }
    
    public void clearGrid(ActionEvent e) {
        if(this.grid == null) return;
        Cell[][] oldGrid = this.grid.getGrid();
        int rows = oldGrid.length;
        int columns = oldGrid[0].length;
        this.grid = new Grid(rows, columns, visualContainer);
        drawGrid();
    }

    public void uploadGrid(ActionEvent e) {
        File selectedFile = getFileChooser().showOpenDialog(uploadButton.getScene().getWindow());

        List<int[]> rows = new ArrayList<>();
        if(selectedFile != null) {
            try (BufferedReader reader = new BufferedReader(new FileReader(selectedFile))) {
                String line;

                while ((line = reader.readLine()) != null) {
                    String[] tokens = line.trim().split("[,\\s]+");
                    int[] array = tokensToArray(tokens);
                    if(array == null) return;

                    rows.add(array);
                }
            } catch (IOException exception) {
                exception.printStackTrace();
            }
        }

        // New empty grid
        Grid newGrid = new Grid(rows.size(), rows.get(0).length, visualContainer);
        Cell[][] newGridCells = newGrid.getGrid();

        for(int row = 0; row < rows.size(); row++)
        {
            for(int column = 0; column < rows.get(0).length; column++)
            {
                newGridCells[row][column].getRect().setFill(GridSnapshot.getColor(rows.get(row)[column]));
            }
        }

        rowsField.setText(String.valueOf(rows.size()));
        columnsField.setText(String.valueOf(rows.get(0).length));
        this.grid = newGrid;
        drawGrid();
    }

    private int[] tokensToArray(String[] tokens) {
        int[] result = new int[tokens.length];

        for(int i = 0; i < tokens.length; i++) 
        {
            String token = tokens[i];
            if(isValid(token))
            {
                result[i] = Integer.parseInt(token);
            } else {
                updateWarning("gridWarning", "The file has a wrong format!", warningColor);
                return null;
            }
        }

        return result;
    }

    public void downloadGrid(ActionEvent e) {
        if(this.grid == null) return;

        File selectedFile = getFileChooser().showSaveDialog(uploadButton.getScene().getWindow());
        if(selectedFile != null) {
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(selectedFile))) 
            {
                GridSnapshot snapshot = new GridSnapshot(this.grid, new HashSet<INode>(), new HashSet<INode>());
                int[][] snapshotGrid = snapshot.getGrid();
                for(int row = 0; row < snapshotGrid.length; row++)
                {
                    for(int column = 0; column < snapshotGrid[0].length; column++)
                    {
                        writer.write(snapshotGrid[row][column] + " ");
                    }
                    writer.newLine();
                }

            } catch (IOException exception) {
                exception.printStackTrace();
            }
        }
    }

    private FileChooser getFileChooser() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select a File");
        fileChooser.getExtensionFilters().addAll(
            new FileChooser.ExtensionFilter("Text Files (*.txt)", "*.txt"),
            new FileChooser.ExtensionFilter("CSV Files (*.csv)", "*.csv"),
            new FileChooser.ExtensionFilter("JSON Files (*.json)", "*.json")
        );

        return fileChooser;
    }

    /**
     * Animation navigation methods
     * These methods allow the user to navigate through the states of the algorithm
     */

    public void startState(ActionEvent e) {
        if(algorithm == null || algorithm.getStates().size() == 0) return;

        this.currentState = 0;
        State s = algorithm.getStates().get(this.currentState);
        drawGrid(s);
        stateLabelUpdate();
    }

    public void nextState(ActionEvent e) {
        if(algorithm == null || algorithm.getStates().size() == 0) return;

        if (this.currentState < algorithm.getStates().size() - 1)
            this.currentState++;

        State s = algorithm.getStates().get(this.currentState);
        drawGrid(s);
        stateLabelUpdate();
    }

    public void previousState(ActionEvent e) {
        if(algorithm == null || algorithm.getStates().size() == 0) return;

        if (this.currentState > 0)
            this.currentState--;
        
        State s = algorithm.getStates().get(this.currentState);
        drawGrid(s);
        stateLabelUpdate();
    }

    public void endState(ActionEvent e) {
        if(algorithm == null || algorithm.getStates().size() == 0) return;

        this.currentState = algorithm.getStates().size() - 1;
        State s = algorithm.getStates().get(this.currentState);
        drawGrid(s);
        stateLabelUpdate();
    }

    public void stateLabelUpdate() {
        if (this.currentState == - 1) {
            stateLabel.setText("State 0 of 0");
            return;
        }

        String text = "State " + (this.currentState + 1) + " of " + algorithm.getStates().size();
        stateLabel.setText(text);
    }

    public void skipAnimation(ActionEvent e) 
    {
        if(timeline != null && timeline.getStatus() == Timeline.Status.RUNNING) {
            timeline.stop();
            progressBar.setProgress(0);
            stateLabelUpdate();
            buttonState(true);
            skipAnimation.setDisable(true);
        }

        this.currentState = algorithm.getStates().size() - 1;
        State s = algorithm.getStates().get(this.currentState);

        drawGrid(s);
    }

    /**
     * Draw methods
     */

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

    public void drawGrid(State state) {
        visualContainer.getChildren().clear();
        GridSnapshot snapshot = state.getSnapshot();
        int[][] gridSnapshot = snapshot.getGrid();

        Cell[][] gridLayout = this.grid.getGrid();
        for(int row = 0; row < gridLayout.length; row++) {
            for(int column = 0; column < gridLayout[0].length; column++) {
                Cell cell = gridLayout[row][column];
                Point p1 = cell.getP1();
                Point p2 = cell.getP2();

                double x = p1.getX();
                double y = p1.getY();
                double width = p2.getX() - x;
                double height = p2.getY() - y;
                Rectangle rect = new Rectangle(x, y, width, height);
                
                rect.setFill(GridSnapshot.getColor(gridSnapshot[row][column]));
                rect.setArcWidth(4);
                rect.setArcHeight(4);
                visualContainer.getChildren().add(rect);
            }

            // Performance -> Fazer com que só altere as diferenças, se forem iguais e só mudar o 1 celula, esse retangulo é atualiazdo (grid "estática" somente muda a cor, para visualização só precisa dos retangulos)?
        }
    }
}
