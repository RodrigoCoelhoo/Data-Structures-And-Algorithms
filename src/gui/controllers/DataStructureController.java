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
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.SplitPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;

public class DataStructureController {

    IDataStructure<Integer> dataStructure = null;
    
    @FXML private SplitPane splitPane;
    @FXML private ComboBox<String> datastructureComboBox;
    @FXML private Pane visualContainer;
    @FXML private TextField insertValueField, deleteValueField, searchValueField, updateValueField, insertIndexField, deleteIndexField, searchIndexField, updateIndexField;
    @FXML private Label operationsWarning;
    @FXML private Button insertButton, deleteButton, searchButton, updateButton;

    private final Color warningColor = Color.RED;

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
        datastructureComboBox.getItems().addAll("Linked List", "Doubly Linked List", "Queue", "Priority Queue", "Stack", "Binary Search Tree", "Max Heap", "Min Heap");
        datastructureComboBox.setOnAction(this::updateAlgorithm);
        
        // reset slider if user tries to drag it
        splitPane.setDividerPositions(0.2);
        splitPane.getDividers().get(0).positionProperty().addListener((_, _, _) -> {
            splitPane.setDividerPositions(0.2); 
        });
    }

    private void updateAlgorithm(ActionEvent e)
    {
        switch (datastructureComboBox.getValue()) {
            case "Linked List":
                this.dataStructure = new LinkedList<>();
                toggleInsert(true, true, true);
                toggleDelete(true, true, true);
                toggleSearch(true, true, false);
                toggleUpdate(true, true, true);
                refreshUI();
                break;
            case "Doubly Linked List":
                this.dataStructure = new DoublyLinkedList<>();
                toggleInsert(true, true, true);
                toggleDelete(true, true, true);
                toggleSearch(true, true, false);
                toggleUpdate(true, true, true);
                refreshUI();
                break;
            case "Queue":
                this.dataStructure = new Queue<>();
                toggleInsert(true, false, true);
                toggleDelete(true, false, false);
                toggleSearch(true, false, false);
                toggleUpdate(false, false, false);
                refreshUI();
                break;
            case "Priority Queue":
                this.dataStructure = new PriorityQueue<Integer>(Comparator.comparingInt(x -> x));
                toggleInsert(true, false, true);
                toggleDelete(true, false, false);
                toggleSearch(true, false, false);
                toggleUpdate(false, false, false);
                refreshUI();
                break;
            case "Stack":
                this.dataStructure = new Stack<>();
                toggleInsert(true, false, true);
                toggleDelete(true, false, false);
                toggleSearch(true, false, false);
                toggleUpdate(false, false, false);
                refreshUI();
                break;
            case "Binary Search Tree":
                this.dataStructure = new BinarySearchTree<>();
                toggleInsert(true, false, true);
                toggleDelete(true, false, true);
                toggleSearch(true, false, true);
                toggleUpdate(false, false, false);
                refreshUI();
                break;
            case "Max Heap":
                this.dataStructure = new MaxHeap<>();
                toggleInsert(true, false, true);
                toggleDelete(true, false, true);
                toggleSearch(true, false, true);
                toggleUpdate(false, false, false);
                refreshUI();
                break;
            case "Min Heap":
                this.dataStructure = new MinHeap<>();
                toggleInsert(true, false, true);
                toggleDelete(true, false, true);
                toggleSearch(true, false, true);
                toggleUpdate(false, false, false);
                refreshUI();
                break;
            default:
                break;
        }
    }

    private void refreshUI() {
        insertValueField.setText("");
        deleteValueField.setText("");
        searchValueField.setText("");
        updateValueField.setText("");
        insertIndexField.setText("");
        deleteIndexField.setText("");
        searchIndexField.setText("");
        updateIndexField.setText("");
        updateWarning("clear", null, null);
        this.dataStructure.draw(visualContainer);
    }

    private void toggleInsert(boolean button, boolean index, boolean value) {
        insertButton.setDisable(!button);
        insertIndexField.setDisable(!index);
        insertValueField.setDisable(!value);
    }

    private void toggleDelete(boolean button, boolean index, boolean value) {
        deleteButton.setDisable(!button);
        deleteIndexField.setDisable(!index);
        deleteValueField.setDisable(!value);
    }

    private void toggleSearch(boolean button, boolean index, boolean value) {
        searchButton.setDisable(!button);
        searchIndexField.setDisable(!index);
        searchValueField.setDisable(!value);
    }

    private void toggleUpdate(boolean button, boolean index, boolean value) {
        updateButton.setDisable(!button);
        updateIndexField.setDisable(!index);
        updateValueField.setDisable(!value);
    }

    public void updateWarning(String warning, String text, Color warningColor) {
        switch (warning) {
            case "operationsWarning":
                operationsWarning.setText(text);
                operationsWarning.setTextFill(warningColor);
                break;
            case "clear":
                operationsWarning.setText("");
                break;
            default:
                break;
        }
    }

    @FXML
    private void handleInsert(ActionEvent event) {
        try {
            if(datastructureComboBox.getValue() == null) {
                updateWarning("operationsWarning", "Please select a data structure.", warningColor);
                return;
            }

            if(insertValueField.getText().isEmpty()) {
                updateWarning("operationsWarning", "Please enter a value.", warningColor);
                return;
            }
            int value = Integer.valueOf(insertValueField.getText());

            int index;
            if(insertIndexField.isDisabled() || insertIndexField.getText().isEmpty()) {
                index = -1;
            }
            else {
                index = Integer.valueOf(insertIndexField.getText());

                int dsSize = this.dataStructure.size();
                if(index < 0 || index > dsSize) {
                    updateWarning("operationsWarning", "Please enter a valid index", warningColor);
                    return;
                }
            }

            insertValue(value, index);

            this.dataStructure.draw(visualContainer);
        } catch (NumberFormatException ex) {
            updateWarning("operationsWarning", "Please enter a valid number", warningColor);
            return;
        }
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
        try {
            if(datastructureComboBox.getValue() == null) {
                updateWarning("operationsWarning", "Please select a data structure.", warningColor);
                return;
            }

            boolean hasIndex = !deleteIndexField.getText().isEmpty();
            boolean hasValue = !deleteValueField.getText().isEmpty();

            if (hasIndex && hasValue) {
                updateWarning("operationsWarning", "Cannot delete by index and value at once.", warningColor);
                return;
            }

            int value = -1;
            int index = -1;

            if (hasValue) {
                value = Integer.valueOf(deleteValueField.getText());
            } else if (hasIndex) {
                index = Integer.valueOf(deleteIndexField.getText());

                int dsSize = this.dataStructure.size();
                if (index < 0 || index >= dsSize) {
                    updateWarning("operationsWarning", "Please enter a valid index.", warningColor);
                    return;
                }
            } else {
                updateWarning("operationsWarning", "Please enter a value.", warningColor);
                return;
            }
            
            deleteValue(value, index);

            this.dataStructure.draw(visualContainer);
        } catch (NumberFormatException ex) {
            updateWarning("operationsWarning", "Please enter a valid number", warningColor);
            return;
        }
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
    private void handleSearch(ActionEvent event) {
        try {
            if(datastructureComboBox.getValue() == null) {
                updateWarning("operationsWarning", "Please select a data structure.", warningColor);
                return;
            }

            boolean hasIndex = !searchIndexField.getText().isEmpty();
            boolean hasValue = !searchValueField.getText().isEmpty();

            if (hasIndex && hasValue) {
                updateWarning("operationsWarning", "Cannot search by index and value at once.", warningColor);
                return;
            }

            int value = -1;
            int index = -1;

            if (hasValue) {
                value = Integer.valueOf(searchValueField.getText());
            } else if (hasIndex) {
                index = Integer.valueOf(searchIndexField.getText());

                int dsSize = this.dataStructure.size();
                if (index < 0 || index >= dsSize) {
                    updateWarning("operationsWarning", "Please enter a valid index.", warningColor);
                    return;
                }
            } else {
                updateWarning("operationsWarning", "Please enter a value.", warningColor);
                return;
            }
            
            searchValue(value, index);

            this.dataStructure.draw(visualContainer);
        } catch (NumberFormatException ex) {
            updateWarning("operationsWarning", "Please enter a valid number", warningColor);
            return;
        }
    }

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
    private void handleUpdate(ActionEvent event) {
        try {
            if (datastructureComboBox.getValue() == null) {
                updateWarning("operationsWarning", "Please select a data structure.", warningColor);
                return;
            }

            if (updateValueField.getText().isEmpty() || updateIndexField.getText().isEmpty()) {
                updateWarning("operationsWarning", "Please enter both value and index.", warningColor);
                return;
            }

            int value = Integer.valueOf(updateValueField.getText());
            int index = Integer.valueOf(updateIndexField.getText());

            int dsSize = this.dataStructure.size();
            if (index < 0 || index >= dsSize) {
                updateWarning("operationsWarning", "Please enter a valid index.", warningColor);
                return;
            }

            updateValue(value, index);

            this.dataStructure.draw(visualContainer);
        } catch (NumberFormatException ex) {
            updateWarning("operationsWarning", "Please enter a valid number.", warningColor);
        }
    }


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