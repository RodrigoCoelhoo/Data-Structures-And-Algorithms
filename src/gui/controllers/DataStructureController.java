package gui.controllers;


import java.util.Comparator;

import datastructures.interfaces.IDataStructure;
import datastructures.lists.DoublyLinkedList;
import datastructures.lists.LinkedList;
import datastructures.queues.PriorityQueue;
import datastructures.queues.Queue;
import datastructures.stacks.Stack;
import datastructures.trees.BinarySearchTree;
import datastructures.trees.MaxHeap;
import datastructures.trees.MinHeap;
import gui.SceneManager;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.SplitPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;

public class DataStructureController {
    
    @FXML private SplitPane splitPane;
    @FXML private ComboBox<String> algorithmComboBox;
    @FXML private Pane visualContainer;
    
    IDataStructure<Integer> dataStructure = null;

	@FXML
    private void handleSwitchToHome(ActionEvent event) {
        try {
            SceneManager.switchScene("layouts/main.fxml");
        } catch (Exception e) {
            e.printStackTrace();
        }
        
    }

    @FXML
    public void initialize() {
        algorithmComboBox.getItems().addAll("Linked List", "Doubly Linked List", "Queue", "Priority Queue", "Stack", "Binary Search Tree", "Max Heap", "Min Heap");
        algorithmComboBox.setOnAction(this::updateAlgorithm);
        
        // reset slider if user tries to drag it
        splitPane.setDividerPositions(0.2);
        splitPane.getDividers().get(0).positionProperty().addListener((_, _, _) -> {
            splitPane.setDividerPositions(0.2); 
        });
    }

    private void updateAlgorithm(ActionEvent e)
    {
        switch (algorithmComboBox.getValue()) {
            case "Linked List":
                this.dataStructure = new LinkedList<>();
                refreshUI();
                break;
            case "Doubly Linked List":
                this.dataStructure = new DoublyLinkedList<>();
                refreshUI();
                break;
            case "Queue":
                this.dataStructure = new Queue<>(); 
                refreshUI();
                break;
            case "Priority Queue":
                this.dataStructure = new PriorityQueue<Integer>(Comparator.comparingInt(x -> x));
                refreshUI();
                break;
            case "Stack":
                this.dataStructure = new Stack<>();
                refreshUI();
                break;
            case "Binary Search Tree":
                this.dataStructure = new BinarySearchTree<>(); 
                refreshUI();
                break;
            case "Max Heap":
                this.dataStructure = new MaxHeap<>();
                refreshUI();
                break;
            case "Min Heap":
                this.dataStructure = new MinHeap<>();
                refreshUI();
                break;
            default:
                break;
        }
    }

    private void refreshUI() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'refreshUI'");
    }

    public void updateWarning(String warning, String text, Color warningColor) {
        switch (warning) {
            case "gridWarning":
                //gridWarning.setText(text);
                //gridWarning.setTextFill(warningColor);
                //break;
            case "animationWarning":
                //animationWarning.setText(text);
                //animationWarning.setTextFill(warningColor);
                //break;
            case "objectiveStatus":
                //objectiveCircle.setFill(warningColor);
                //objectiveLabel.setText(text);
                //objectiveLabel.setTextFill(warningColor);
                //break;
            case "startStatus":
                //objectiveCircle.setFill(warningColor);
                //objectiveLabel.setText(text);
                //objectiveLabel.setTextFill(warningColor);
                //break;
            case "clear":
                //gridWarning.setText("");
                //animationWarning.setText("");
                //stateLabel.setText("State 0 of 0");
                //break;
            default:
                break;
        }
    }

    @FXML
    private void handleInsert(ActionEvent event) {
        
    }

    private void insertValue(Integer value, int index) {
        switch (dataStructure) {
            case Stack<Integer> stack -> stack.push(value);
            case Queue<Integer> queue -> queue.enqueue(value);
            case BinarySearchTree<Integer> tree -> tree.insert(value);
            case MaxHeap<Integer> heap -> heap.push(value);
            case MinHeap<Integer> heap -> heap.push(value);
            case LinkedList<Integer> list when index >= 0 -> list.add(index, value);
            case LinkedList<Integer> list -> list.add(value);
            case DoublyLinkedList<Integer> dlist when index >= 0 -> dlist.add(index, value);
            case DoublyLinkedList<Integer> dlist -> dlist.add(value);

            default -> throw new UnsupportedOperationException(
                "Insert not supported for " + dataStructure.getClass().getSimpleName()
            );
        }
    }

    @FXML
    private void handleDelete(ActionEvent event) {
        // Se indexField tiver desativado ou vazio é -1, caso contrário é o valor que está lá
        // deleteValue(deleteValueField, deleteIndexField);
        this.dataStructure.draw(visualContainer);
    }

    private void deleteValue(Integer value, int index) {
        switch (dataStructure) {
            case Stack<Integer> stack -> stack.pop();
            case Queue<Integer> queue -> queue.dequeue();
            case BinarySearchTree<Integer> tree -> tree.delete(value);
            case MaxHeap<Integer> heap -> heap.pop();
            case MinHeap<Integer> heap -> heap.pop();
            case LinkedList<Integer> list when index >= 0 -> list.remove(index);
            case LinkedList<Integer> list -> list.remove(value);
            case DoublyLinkedList<Integer> dlist when index >= 0 -> dlist.remove(index);
            case DoublyLinkedList<Integer> dlist -> dlist.remove(value);
            
            default -> throw new UnsupportedOperationException(
                "Delete not supported for " + dataStructure.getClass().getSimpleName()
            );
        }
    }

    @FXML
    private void handleSearch(ActionEvent event) { }

    private void searchValue(Integer value, int index) {
        switch (dataStructure) {
            case Stack<Integer> stack -> stack.peek();
            case Queue<Integer> queue -> queue.peek();
            case BinarySearchTree<Integer> tree -> tree.search(value);
            case MaxHeap<Integer> heap -> heap.peek();
            case MinHeap<Integer> heap -> heap.peek();
            case LinkedList<Integer> list -> list.get(index);
            case DoublyLinkedList<Integer> dlist -> dlist.get(index);
            
            default -> throw new UnsupportedOperationException(
                "Get not supported for " + dataStructure.getClass().getSimpleName()
            );
        }
    }

    @FXML
    private void handleUpdate(ActionEvent event) { }

    private void updateValue(Integer value, int index) {
        switch (dataStructure) {
            case LinkedList<Integer> list -> list.set(value, index);
            case DoublyLinkedList<Integer> dlist -> dlist.set(value, index);
            
            default -> throw new UnsupportedOperationException(
                "Update not supported for " + dataStructure.getClass().getSimpleName()
            );
        }
    }

    @FXML
    public void clear(ActionEvent event) {
        this.dataStructure.clear();
        this.dataStructure.draw(visualContainer);
    }
}
