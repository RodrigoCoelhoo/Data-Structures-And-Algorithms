package gui;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import algortihms.BubbleSort;
import algortihms.SortAlgorithm;
import algortihms.State;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.SplitPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.stage.FileChooser;
import javafx.util.Duration;

public class Controller {

    /**
     * Variables declaration
     */

    private ArrayList<Integer> list = new ArrayList<>();
    private SortAlgorithm<Integer> algorithm = null;
    private int currentState = -1;

    private Timeline timeline;
    
    @FXML private SplitPane splitPane;
    @FXML private TextField sizeField;
    @FXML private Label arrayWarning;
    @FXML private Label algorithmInfo;
    @FXML private Label animationWarning;
    @FXML private Label stateLabel;
    @FXML private Label arrayStatus, arraySize;
    @FXML private ComboBox<String> algorithmComboBox;
    @FXML private Pane visualContainer;
    @FXML private ProgressBar progressBar;
    @FXML private Button sortButton, lastButton, previousButton, nextButton, firstButton, clearButton, algorithmButton, dsButton, uploadButton, generateButton, skipAnimation;
    @FXML private Circle arrayCircleStatus;
    @FXML private TextField animationDuration;

    private final Color barColor = Color.STEELBLUE;
    private final Color activeColor = Color.LIGHTGREEN;
    private final Color warningColor = Color.RED;

    /**
     * 
     */

    @FXML
    public void initialize() {
        algorithmComboBox.getItems().addAll("Bubble Sort", "Selection Sort", "Insertion Sort");
        algorithmComboBox.setOnAction(this::updateAlgorithm);
        
        // reset slider if user tries to drag it
        splitPane.setDividerPositions(0.2);
        splitPane.getDividers().get(0).positionProperty().addListener((_, _, _) -> {
            splitPane.setDividerPositions(0.2); 
        });
        
        stateLabelUpdate();
        arrayCircleStatus.setStrokeWidth(0);
        arraySize.setText("");
        updateWarning("arrayStatus", "Array is empty.", warningColor);

        skipAnimation.setDisable(true);
    }

    private void updateAlgorithm(ActionEvent e)
    {
        switch (algorithmComboBox.getValue()) {
            case "Bubble Sort":
                this.algorithm = new BubbleSort<>();
                algorithmInfo.setText(algorithm.info());
                updateWarning("clear", null, null);
                break;
            default:
                break;
        }
    }

    private void buttonState(boolean state) {
        sortButton.setDisable(!state);
        lastButton.setDisable(!state);
        previousButton.setDisable(!state);
        nextButton.setDisable(!state);
        firstButton.setDisable(!state);
        clearButton.setDisable(!state);
        algorithmButton.setDisable(!state);
        dsButton.setDisable(!state);
        uploadButton.setDisable(!state);
        generateButton.setDisable(!state);
        algorithmComboBox.setDisable(!state);
        animationDuration.setDisable(!state);
        sizeField.setDisable(!state);
    }

    public void updateWarning(String warning, String text, Color warningColor) {
        switch (warning) {
            case "arrayWarning":
                arrayWarning.setText(text);
                arrayWarning.setTextFill(warningColor);
                break;
            case "arrayStatus":
                arrayCircleStatus.setFill(warningColor);
                arrayStatus.setText(text);
                arrayStatus.setTextFill(warningColor);
                if(warningColor.equals(this.activeColor))
                {
                    arraySize.setText(this.list.size() + " elements");
                    arraySize.setTextFill(this.activeColor);
                } else {
                    arraySize.setText("");
                }
                break;
            case "animationWarning":
                animationWarning.setText(text);
                animationWarning.setTextFill(warningColor);
                break;
            case "clear":
                arrayWarning.setText("");
                animationWarning.setText("");
                break;
            default:
                break;
        }
    }


    /**
     * Array manipulation methods
     */

    public void createArray(ActionEvent e) 
    {
        try {
            int size = Integer.parseInt(sizeField.getText());
            if (size < 10 || size > 300) {
                updateWarning("arrayWarning", "Size must be between 10 and 300", warningColor);
                return;
            }
            updateWarning("clear", null, null);

            this.list.clear();
            
            Random r = new Random();
            for (int i = 0; i < size; i++) {
                this.list.add(r.nextInt(1, 101));
            }

            if (algorithm != null) {
                algorithm.clearStates();
            }
            sizeField.setText("");

            drawArray(this.list, null, null);
            updateWarning("arrayStatus", "Array is ready!", activeColor);

        } catch (NumberFormatException ex) {
            updateWarning("arrayWarning", "Please enter a valid number", warningColor);
            return;
        }
    }

    public void uploadArray(ActionEvent e) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select a File");
        fileChooser.getExtensionFilters().add(
            new FileChooser.ExtensionFilter(
                "Text Files (*.txt)", "*.txt",
                "Comma Separated Values (*.csv)", "*.csv",
                "JSON Files (*.json)", "*.json"
                )
        );

        File selectedFile = fileChooser.showOpenDialog(uploadButton.getScene().getWindow());
        if (selectedFile != null) {
            List<Integer> numbers = parseFile(selectedFile);

            if (numbers == null) {
                return;
            }

            int size = numbers.size();
            if (size < 10 || size > 300) {
                updateWarning("arrayWarning", "Size must be between 10 and 300", warningColor);
                return;
            }

            this.list = new ArrayList<>(numbers);
            updateWarning("arrayStatus", selectedFile.getName(), activeColor);
            updateWarning("clear", null, null);

            if (algorithm != null) {
                algorithm.clearStates();
            }

            drawArray(this.list, null, null);
        }
    }

    private List<Integer> parseFile(File file) {
        try {
            String content = Files.readString(file.toPath());
            String[] parts = content.trim().split("[,\\s]+");
            List<Integer> numbers = new ArrayList<>();

            for (String part : parts) {
                try {
                    numbers.add(Integer.parseInt(part));
                } catch (NumberFormatException e) {
                    updateWarning("arrayStatus", "Wrong Format", warningColor);

                    return null;
                }
            }
            return numbers;
        } catch (IOException e) {
            updateWarning("arrayStatus", "Error reading file", warningColor);
            return null;
        }
    }

    public void sortArray(ActionEvent e) {
        if (algorithm == null) {
            updateWarning("animationWarning", "Please select a sorting algorithm.", warningColor);
            return;
        }

        if (list.isEmpty()) {
            updateWarning("animationWarning", "Array is empty. Please create an array first.", warningColor);
            return;
        }

        double duration = Double.parseDouble(animationDuration.getText());
        if (duration <= 0) {
            updateWarning("animationWarning", "Please enter a valid duration.", warningColor);
            return;
        }

        algorithm.clearStates();
        algorithm.sort(new ArrayList<>(list));
        this.currentState = 0;


        stateLabel.setText("Calculating states ...");
        buttonState(false);
        animate();

        this.currentState = algorithm.getStates().size() - 1;
    }

    public void clearArray(ActionEvent e) {
        list.clear();
        if (algorithm != null)
            algorithm.clearStates();
        
        visualContainer.getChildren().clear();
        this.currentState = -1;
        stateLabelUpdate();
        
        updateWarning("arrayStatus", "Array is empty.", warningColor);
        updateWarning("clear", null, null);
    }


    /**
     * Animation navigation methods
     * These methods allow the user to navigate through the states of the algorithm
     */

    public void startState(ActionEvent e) {
        if(algorithm == null || algorithm.getStates().size() == 0) return;

        this.currentState = 0;
        State s = algorithm.getStates().get(this.currentState);
        drawArray(s.getList(), s.getIndexs(), s.getHighLight());
        stateLabelUpdate();
    }

    public void nextState(ActionEvent e) {
        if(algorithm == null || algorithm.getStates().size() == 0) return;

        if (this.currentState < algorithm.getStates().size() - 1)
            this.currentState++;

        State s = algorithm.getStates().get(this.currentState);
        drawArray(s.getList(), s.getIndexs(), s.getHighLight());
        stateLabelUpdate();
    }

    public void previousState(ActionEvent e) {
        if(algorithm == null || algorithm.getStates().size() == 0) return;

        if (this.currentState > 0)
            this.currentState--;
        
        State s = algorithm.getStates().get(this.currentState);
        drawArray(s.getList(), s.getIndexs(), s.getHighLight());
        stateLabelUpdate();
    }

    public void endState(ActionEvent e) {
        if(algorithm == null || algorithm.getStates().size() == 0) return;

        this.currentState = algorithm.getStates().size() - 1;
        State s = algorithm.getStates().get(this.currentState);
        drawArray(s.getList(), s.getIndexs(), s.getHighLight());
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

        drawArray(s.getList(), s.getIndexs(), s.getHighLight());
    }


    /**
     * Visual Container manipulation methods
     */

    public void drawArray(List<Integer> list, List<Integer> indexs, List<Integer> highLight) {
        visualContainer.getChildren().clear();

        double width = visualContainer.getWidth();
        double height = visualContainer.getHeight();

        int size = list.size();
        double slotWidth = width / size;
        double barWidth = Math.max(0, slotWidth - 2);

        for (int i = 0; i < size; i++) {
            double barHeight = (list.get(i) * height) / 100.0;
            double x = i * slotWidth + 1;
            double y = height - barHeight;

            Rectangle bar = new Rectangle(x, y, barWidth, barHeight);
            bar.setFill(barColor);
            if(indexs != null && indexs.contains(i))
                bar.setFill(activeColor);
            else if (highLight != null && highLight.contains(i))
                bar.setFill(Color.RED);
            visualContainer.getChildren().add(bar);
        }
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
                drawArray(state.getList(), state.getIndexs(), state.getHighLight());
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