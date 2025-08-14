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

import algorithms.interfaces.INode;
import algorithms.interfaces.ISearchAlgorithm;
import algorithms.search.AStar;
import algorithms.search.BFS;
import algorithms.search.Greedy;
import algorithms.search.JPS;
import gui.SceneManager;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.SplitPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Duration;
import utils.Grid;
import utils.GridSnapshot;
import utils.State;
import utils.Grid.Cell;
import utils.Grid.Point;

public class AlgorithmSearchController {

    Grid grid;
    private ISearchAlgorithm algorithm = null;
    private int currentState = -1;

    private Timeline timeline;

    @FXML private Pane visualContainer;
    @FXML private SplitPane splitPane;
    @FXML private Label stateLabel, gridWarning, animationWarning, objectiveLabel, startLabel;
    @FXML private Button searchButton, lastButton, previousButton, nextButton, firstButton, clearButton, uploadButton, generateButton, skipAnimation, downloadButton;
    @FXML private ProgressBar progressBar;
    @FXML private ComboBox<String> algorithmComboBox, heuristicComboBox;
    @FXML private TextArea algorithmInfo;
    @FXML private TextField rowsField, columnsField, animationDuration;
    @FXML private Circle startCircle, objectiveCircle;
    @FXML private CheckBox diagonalMovements;

    private final Color warningColor = Color.RED;

    /**
     * UI related methods
     */

    @FXML
    public void initialize() {
        algorithmComboBox.getItems().addAll("A*", "Breadth-First Search (BFS)", "Best First Search (Greedy)", "Jump Point Search");
        algorithmComboBox.setOnAction(this::updateAlgorithm);

        
        heuristicComboBox.getItems().addAll("Manhattan", "Euclidean", "Octile", "Chebyshev");
        heuristicComboBox.setOnAction(this::updateHeuristic);
        
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

        stateLabel.setText("State 0 of 0");
    }

    @FXML
    private void showInfoPopup() {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle("Controls & Instructions");
        alert.setHeaderText(null);

        String helpText =
            "Defining Nodes: \n" +
            "\t- CTRL + LEFT CLICK -> Goal\n" +
            "\t- SHIFT + LEFT CLICK -> Start\n" +
            "\t- LEFT CLICK -> Wall\n\n" +
            "To edit the grid, it must be clear. To clear, use:\n" +
            "\t- Clear option (Eraser icon),\n" +
            "\t- Generate a new grid.\n" +
            "\t- Upload a grid.\n\n" +
            "If the bottom shows 'State 0 of 0' the grid is available to edit.";
        alert.setContentText(helpText);

        Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
        stage.getIcons().add(new Image(getClass().getResource("/images/help.png").toString()));

        alert.showAndWait();
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
                enableHeuristic(true);
                refreshUI();
                break;
            case "Breadth-First Search (BFS)":
                this.algorithm = new BFS();
                enableHeuristic(false);
                refreshUI();
                break;
            case "Best First Search (Greedy)":
                this.algorithm = new Greedy(); 
                enableHeuristic(true);
                refreshUI();
                break;
            case "Jump Point Search":
                this.algorithm = new JPS();
                enableHeuristic(true);
                refreshUI();
                break;
            default:
                break;
        }
    }

    private void updateHeuristic(ActionEvent e) {
        String value = heuristicComboBox.getValue();
        if (value == null) {
            return;
        }

        switch (value) {
            case "Manhattan":
            case "Euclidean":
            case "Octile":
            case "Chebyshev":
                Grid.setHeuristic(value);
                break;
        }
        refreshUI();
    }

    private void enableHeuristic(boolean activate) {
        heuristicComboBox.setDisable(!activate);
    }

    private void refreshUI() {
        algorithmInfo.setText(algorithm.info());
        updateWarning("clear", null, null);
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
                stateLabel.setText("State 0 of 0");
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
        downloadButton.setDisable(!state);
        diagonalMovements.setDisable(!state);
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
        if(this.algorithm != null)
            this.algorithm.clearStates();

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

        updateWarning("clear", null, null);
        this.algorithm.clearStates();
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
        visualContainer.getChildren().clear();
        Grid newGrid = new Grid(rows.size(), rows.get(0).length, visualContainer);
        Cell[][] newGridCells = newGrid.getGrid();

        for(int row = 0; row < rows.size(); row++)
        {
            for(int column = 0; column < rows.get(0).length; column++)
            {
                Cell current = newGridCells[row][column];
                current.getRect().setFill(GridSnapshot.getColor(rows.get(row)[column]));
                GridSnapshot.setCellProperties(current, rows.get(row)[column]);
            }
        }

        rowsField.setText(String.valueOf(rows.size()));
        columnsField.setText(String.valueOf(rows.get(0).length));
        this.grid = newGrid;
        updateWarning("clear", null, null);
        if(this.algorithm != null) this.algorithm.clearStates();
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
                GridSnapshot snapshot = new GridSnapshot(this.grid, new HashSet<INode>(), new HashSet<INode>(), new HashSet<INode>());
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

    public void setGridMovements(ActionEvent e) {
        if(diagonalMovements.isSelected()) {
            Grid.setDirections(new int[][]{
                {-1, 0}, {-1, 1}, {0, 1}, {1, 1}, 
                {1, 0}, {1, -1}, {0, -1}, {-1, -1}
            });
            return;
        }

        Grid.setDirections(new int[][]{
            {-1, 0}, {0, 1}, {1, 0}, {0, -1}
        });
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

    /**
     * Animation method
     */

    public void search(ActionEvent e) {
        if (algorithm == null) {
            updateWarning("animationWarning", "Please select an algorithm.", warningColor);
            return;
        }

        if (!heuristicComboBox.isDisabled() && Grid.getHeuristic() == null) {
            updateWarning("animationWarning", "Please select an heuristic.", warningColor);
            return;
        }

        if (this.grid == null || this.grid.getInitialNode() == null || this.grid.getObjectiveCell() == null) {
            updateWarning("animationWarning", "Please define a start and goal node!", warningColor);
            return;
        }

        double duration = Double.parseDouble(animationDuration.getText());
        if (duration <= 0) {
            updateWarning("animationWarning", "Please enter a valid duration.", warningColor);
            return;
        }

        algorithm.clearStates();
        List<INode> path = algorithm.solve(this.grid);
        
        getPathStates(path);
        this.currentState = 0;

        stateLabel.setText("Calculating states ...");
        buttonState(false);
        animate();

        this.currentState = algorithm.getStates().size() - 1;
    }

    private void getPathStates(List<INode> path) {
        State lastState = this.algorithm.getStates().get(this.algorithm.getStates().size() - 1);
        GridSnapshot snapshot = lastState.getSnapshot();
        int[][] baseGrid = snapshot.getGrid();

        for(INode node : path) {
            Cell cell = (Cell) node;
            int[][] grid = deepCopy(baseGrid);
            grid[cell.getRow()][cell.getColumn()] = 6;
            algorithm.getStates().add(new State(new GridSnapshot(grid)));
            baseGrid = grid;
        }
    }

    private int[][] deepCopy(int[][] array)
    {
        if(array == null) return null;

        int[][] result = new int[array.length][array[0].length];
        for(int i = 0; i < array.length; i++) {
            for(int j = 0; j < array[0].length; j++) {
                result[i][j] = array[i][j];
            }
        }
        return result;
    }

    public void animate() {
        if (algorithm == null || algorithm.getStates().isEmpty()) {
            return;
        }

        ArrayList<State> states = algorithm.getStates();
        int size = states.size();

        if (size == 0) return;

        updateWarning("clear", null, null);
        
        double duration = Double.parseDouble(animationDuration.getText());
        double timePerState = (double) duration / size;

        // Timeline for animation
        this.timeline = new Timeline();

        for (int i = 0; i < size; i++) {
            State state = states.get(i);

            // Define which second will the state be drawn in the timeline
            KeyFrame keyFrame = new KeyFrame(Duration.seconds(i * timePerState), _ -> {
                drawGrid(state);
            });
            
            timeline.getKeyFrames().add(keyFrame);
        }
        
        timeline.currentTimeProperty().addListener((_, _, newTime) -> {
            double progress = newTime.toSeconds() / duration;
            progressBar.setProgress(progress);
        });
        
        timeline.setOnFinished(_ -> {
            progressBar.setProgress(0);
            stateLabelUpdate();
            buttonState(true);
            skipAnimation.setDisable(true);
        });

        skipAnimation.setDisable(false);
        timeline.play();
    }
}
