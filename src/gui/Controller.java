package gui;

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
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;

public class Controller {

    private final ArrayList<Integer> list = new ArrayList<>();
    private SortAlgorithm<Integer> algorithm = null;
    
    @FXML private TextField sizeField;
    @FXML private Label sizeFieldWarning;
    @FXML private Label algorithmInfo;
    @FXML private Label sortWarning;
    @FXML private ComboBox<String> algorithmComboBox;
    @FXML private Pane visualContainer;
    @FXML private ProgressBar progressBar;
    @FXML private Button sortButton;

    private final Color barColor = Color.STEELBLUE;
    private final Color activeColor = Color.LIGHTGREEN;
    private final Color warningColor = Color.RED;

    @FXML
    public void initialize() {
        algorithmComboBox.getItems().addAll("Bubble Sort", "Selection Sort", "Insertion Sort");
        algorithmComboBox.setOnAction(this::updateAlgorithm);
    }

    public void createArray(ActionEvent e) {
        try {
            int size = Integer.parseInt(sizeField.getText());
            if (size < 10 || size > 300) {
                sizeFieldWarning.setText("Size must be between 10 and 300");
                sizeFieldWarning.setTextFill(warningColor);
                return;
            }
            sizeFieldWarning.setText("");

            this.list.clear();
            
            Random r = new Random();
            for (int i = 0; i < size; i++) {
                this.list.add(r.nextInt(1, 101));
            }

            if (algorithm != null) {
                algorithm.clearStates();
            }
            sizeField.setText("");

            drawArray(this.list, null);
        } catch (NumberFormatException ex) {
            sizeFieldWarning.setText("Please enter a valid number");
            sizeFieldWarning.setTextFill(warningColor);
            return;
        }
    }

    public void sortArray(ActionEvent e) {
        if (algorithm == null) {
            sortWarning.setText("Please select a sorting algorithm.");
            sortWarning.setTextFill(warningColor);
            return;
        }

        if (list.isEmpty()) {
            sortWarning.setText("Array is empty. Please create an array first.");
            sortWarning.setTextFill(warningColor);
            return;
        }

        if (isSorted(list)) {
            sortWarning.setText("Array is already sorted.");
            sortWarning.setTextFill(warningColor);
            return;
        }

        algorithm.sort(list);
        animate(10);
    }

    private boolean isSorted(ArrayList<Integer> list) {
        for(int i = 0; i < list.size() - 1; i++) {
            if(list.get(i) > list.get(i + 1)) {
                return false;
            }
        }
        return true;
    }

    public void printArray(ActionEvent e) {
        System.out.println(algorithm.getStates().size());
        System.out.println(list);
    }

    public void clearArray(ActionEvent e) {
        list.clear();
        algorithm.clearStates();
        visualContainer.getChildren().clear();
    }

    public void drawArray(List<Integer> list, List<Integer> indexs) {
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
            visualContainer.getChildren().add(bar);
        }
    }

    private void updateAlgorithm(ActionEvent e)
    {
        System.out.println(algorithmComboBox.getValue());
        switch (algorithmComboBox.getValue()) {
            case "Bubble Sort":
                this.algorithm = new BubbleSort<>();
                algorithmInfo.setText(algorithm.info());
                sortWarning.setText("");
                break;
            default:
                break;
        }
    }

    public void animate(int duration) {
        if (algorithm == null || algorithm.getStates().isEmpty()) {
            return;
        }

        ArrayList<State> states = algorithm.getStates();
        int size = states.size();

        if (size == 0) return;

        double timePerState = (double) duration / size; // in seconds

        // Timeline for animation
        Timeline timeline = new Timeline();

        for (int i = 0; i < size; i++) {
            State state = states.get(i);

            // Define which second will the state be drawn in the timeline
            KeyFrame keyFrame = new KeyFrame(Duration.seconds(i * timePerState), _ -> {
                drawArray(state.getList(), state.getIndexs());
            });

            
            timeline.getKeyFrames().add(keyFrame);
        }
        
        timeline.currentTimeProperty().addListener((_, _, newTime) -> {
            double progress = newTime.toSeconds() / duration;
            progressBar.setProgress(progress);
        });
        
        timeline.setOnFinished(_ -> {
            progressBar.setProgress(0);
            sortButton.setDisable(false);
        });

        sortButton.setDisable(true);
        timeline.play();
    }
}