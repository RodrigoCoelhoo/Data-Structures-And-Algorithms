package datastructures.lists;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.NoSuchElementException;

import datastructures.interfaces.IDataStructure;
import datastructures.interfaces.INode;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import utils.DataStructureState;
import utils.DataStructureState.Parameters;

public class LinkedList<T> implements IDataStructure<T>, Iterable<T> {

    ArrayList<DataStructureState<T>> states = new ArrayList<>();
	
	private Node head = null;
	private int size = 0;
	
	@Override
	public void add(T value) {
        Node newNode = new Node(value, null);

        if(this.head == null) {
            this.head = newNode;

            Parameters param = new Parameters();
            param.getInvsible().add(0);
            saveState(this, param);

            param = new Parameters();
            param.setObjective(0);
            saveState(this, param);

            saveState(this, new Parameters());
        } else {
            Node last = getNodeAt(size - 1);  // traverse to last node
            last.setNext(newNode);
            
            Parameters param = new Parameters();
            param.getInvsible().add(this.size);
            saveState(this, param);

            param = new Parameters();
            param.setObjective(this.size);
            saveState(this, param);

            saveState(this, new Parameters());
        }
        
        this.size++;
	}

	@Override
	public void add(int index, T value) {

        if(index < 0 || index > this.size) throw new IndexOutOfBoundsException();
        
        if(index == 0) {
            if(this.head == null) {
                add(value);
                return;
            }

            Node newHead = new Node(value, this.head);
            this.head = newHead;
            this.size++;
            
            Parameters param = new Parameters();
            param.getInvsible().add(index);
            saveState(this, param);

            param = new Parameters();
            param.setObjective(index);
            saveState(this, param);

            saveState(this, new Parameters());
            return;
        }

        if(index == this.size) { 
			add(value);
			return;
		}

        

        Node currentNode = getNodeAt(index - 1);
        Node newNode = new Node(value, currentNode.next());
        currentNode.setNext(newNode);
        this.size++;
        Parameters param = new Parameters();
        param.getInvsible().add(index);
        saveState(this, param);

        param = new Parameters();
        param.setObjective(index);
        saveState(this, param);

        saveState(this, new Parameters());
	}

    @Override
    public T get(int index) {
        if(index < 0 || index >= this.size) throw new IndexOutOfBoundsException();

        Node node = getNodeAt(index);

        Parameters param = new Parameters();
        param.setObjective(index);
        saveState(this, param);
        saveState(this, param);

        saveState(this, new Parameters());

        return node.getValue();
    }

    @Override
    public void set(int index, T value) {
        if(index < 0 || index >= this.size) throw new IndexOutOfBoundsException();

        
        Node node = getNodeAt(index);
        
        Parameters param = new Parameters();
        param.setObjective(index);
        saveState(this.clone(), param);

        // For animation
        node.setValue(null);
        saveState(this.clone(), param);

        node.setValue(value);
        saveState(this.clone(), param);

        saveState(this, new Parameters());
    }

    @Override
    public void remove(int index) {
        if (index < 0 || index >= size) throw new IndexOutOfBoundsException();

        if (index == 0) {
            LinkedList<T> temp = this.clone();

            Parameters param = new Parameters();
            param.getIndexs().add(index);
            saveState(temp, param);
            
            param = new Parameters();
            param.getFailure().add(index);
            saveState(temp, param);
            
            param = new Parameters();
            param.getInvsible().add(index);
            saveState(temp, param);

            head = head.next();
            
            saveState(this, new Parameters());
        } else {
            Node prev = getNodeAt(index - 1);

            LinkedList<T> temp = this.clone();

            Parameters param = new Parameters();
            param.getIndexs().add(index);
            saveState(temp, param);
            
            param = new Parameters();
            param.getFailure().add(index);
            saveState(temp, param);
            
            param = new Parameters();
            param.getInvsible().add(index);
            saveState(temp, param);

            prev.setNext(prev.next().next());
            
            saveState(this, new Parameters());
        }

        size--;
    }

    @Override
    public void remove(T value) {
        if (this.size == 0) return; 

        Node prev = null;
        Node current = this.head;

        int index = 0;
        LinkedList<T> temp = this.clone();
        Parameters failureParameters = new Parameters();
        while (current != null) {
            Parameters param = new Parameters();
            param.getIndexs().add(index);
            saveState(temp, param);

            if (current.getValue().equals(value)) {
                if (prev == null) {

                    param = new Parameters();
                    param.getFailure().add(index);
                    saveState(temp, param);
                    
                    param = new Parameters();
                    param.getInvsible().add(index);
                    saveState(temp, param);

                    this.head = current.next();
            
                    saveState(this, new Parameters());
                } else {
                    param = new Parameters();
                    param.getFailure().add(index);
                    saveState(temp, param);
                    
                    param = new Parameters();
                    param.getInvsible().add(index);
                    saveState(temp, param);

                    prev.setNext(current.next());
            
                    saveState(this, new Parameters());
                }

                this.size--;
                return; 
            }

            prev = current;
            current = current.next();
            failureParameters.getFailure().add(index);
            index++;
        }
        saveState(this, failureParameters);
        saveState(this, failureParameters);

        saveState(this, new Parameters());
    }

    @Override
    public int size() {
        return this.size;
    }

    @Override
    public void clear() {
        this.head = null;
        this.size = 0;
    }

    @Override
    public boolean contains(T value) {
        Node current = this.head;
        while(current != null) {
            if(current.getValue().equals(value)) {
                return true;
            }

            current = current.next();
        }
        
        return false;
    }

    @Override  
	public boolean isEmpty() {
		return this.size == 0;
	}

    @Override
    public String info() {
        String 
        result = "A linked list is a linear data structure where each element (node) "
               + "contains a value and a reference (or pointer) to the next node in the sequence. "
               + "Unlike arrays, linked lists do not require contiguous memory locations, "
               + "making insertions and deletions efficient at the beginning or when a node reference is known.\n\n";

        result += "However, linked lists have slower access times compared to arrays, "
               + "since accessing an element requires traversal from the head node.\n\n";
        
        result += "Linked lists are commonly used in scenarios where dynamic resizing is needed "
               + "and frequent insertions/deletions occur, such as in queues, stacks.";
        
        result += "'n' is the number of elements in the linked list.\n\n";
        
        result += "Note: If a tail pointer is maintained, insertions at the end of the list "
                + "can be performed in constant time O(1). Without it, appending requires "
                + "traversing the entire list, which takes O(n).\n\n";

        result += "Access Time Complexity: O(n)\n";
        result += "Search Time Complexity: O(n)\n";
        result += "Insert Time Complexity: O(1) (at head or with tail pointer), O(n) (at arbitrary position without reference)\n";
        result += "Delete Time Complexity: O(1) (at head), O(n) (at arbitrary position without reference)\n";
        result += "Space Complexity: O(n) (plus optional O(1) extra if storing a tail reference)\n\n";
        
        return result;
    }


    @Override
    public Iterator<T> iterator() {
        return new LinkedListIterator();
    }

    private class LinkedListIterator implements Iterator<T> {
        private Node current = head;

        @Override
        public boolean hasNext() {
            return current != null;
        }

        @Override
        public T next() {
            if (current == null) {
                throw new NoSuchElementException();
            }
            T value = current.getValue();
            current = current.next();
            return value;
        }

        @Override
        public void remove() {
            throw new UnsupportedOperationException("Remove not supported in iterator");
        }
    }

    /**
     * Auxiliary function to get a node at a specific index
     * @param index
     * @pre 0 <= index < size
     * @return Node in the given index
     */
    private Node getNodeAt(int index) {
        LinkedList<T> temp = this.clone();

        if(index == 0) {
            Parameters param = new Parameters();
            param.getIndexs().add(0);
            saveState(temp, param);

            return this.head;
        }

        Node current = this.head;
        for(int i = 0; i < index; i++) {
            current = current.next();

            Parameters param = new Parameters();
            param.getIndexs().add(i);
            saveState(temp, param);
        }

        Parameters param = new Parameters();
        param.getIndexs().add(index);
        saveState(temp, param);
        return current;
    }

	private class Node implements INode<T> {
        private T value;
        private Node next;

        public Node(T value, Node next) {
            this.value = value;
            this.next = next;
        }

        public T getValue() {
            return value;
        }

        public void setValue(T value) {
            this.value = value;
        }

        public Node next() {
            return next;
        }

        public void setNext(Node next) {
            this.next = next;
        }
    }

    @Override
    public LinkedList<T> clone() {
        LinkedList<T> result = new LinkedList<>();
        
        for(T data : this) {
            result.addRaw(data);
        }
        
        return result;
    }

    private void addRaw(T value) {
        Node newNode = new Node(value, null);
        if (this.head == null) {
            this.head = newNode;
        } else {
            Node last = getNodeAt(size - 1);
            last.setNext(newNode);
        }
        this.size++;
    }

    @Override
    public void draw(Pane pane, Parameters param) {
        pane.getChildren().clear();

        double paneWidth = pane.getWidth() > 0 ? pane.getWidth() : 800;
        double paneHeight = pane.getHeight() > 0 ? pane.getHeight() : 600;

        double nodeWidth = 60;
        double nodeHeight = 30;
        double spacingX = 40;
        double spacingY = 70;

        int totalNodes = this.size + 1; // +1 for NULL
        if (totalNodes == 0) return;

        int nodesInRow = Math.max(1, (int)((paneWidth - 100) / (nodeWidth + spacingX)));
        int totalRows = (int) Math.ceil((double) totalNodes / nodesInRow);

        double firstRowStartX = (paneWidth - Math.min(nodesInRow, totalNodes) * nodeWidth
                - (Math.min(nodesInRow, totalNodes) - 1) * spacingX) / 2;

        Node current = this.head;
        for (int i = 0; i < totalNodes; i++) {

            Color nodeColor = param.getColor(i);

            int row = i / nodesInRow;
            int col = i % nodesInRow;
            int nodesThisRow = Math.min(nodesInRow, totalNodes - row * nodesInRow);

            double x = firstRowStartX + col * (nodeWidth + spacingX);

            // Symmetric vertical centering
            double offset = totalRows % 2 == 1 ? row - totalRows / 2 : row - totalRows / 2 + 0.5;
            double y = paneHeight / 2 + offset * spacingY;

            // Draw node
            boolean isNullNode = (i == this.size);
            if (isNullNode) {
                drawNode(pane, "NULL", x, y, nodeWidth, nodeHeight, Color.LIGHTGRAY);
            } else {
                drawNode(pane, current.getValue(), x, y, nodeWidth, nodeHeight, nodeColor);
                current = current.next();
            }

            // Draw arrows
            if (i < totalNodes - 1) {
                boolean lastInRow = (col == nodesThisRow - 1);
                if (!lastInRow) {
                    drawHorizontalArrow(pane, x + nodeWidth, y + nodeHeight / 2, spacingX);
                } else {
                    int nextRow = row + 1;
                    double nextRowOffset = totalRows % 2 == 1 ? nextRow - totalRows / 2 : nextRow - totalRows / 2 + 0.5;
                    double nextRowY = paneHeight / 2 + nextRowOffset * spacingY + nodeHeight / 2;
                    drawRowWrapArrow(pane, x + nodeWidth, y + nodeHeight / 2, firstRowStartX, nextRowY, spacingX);
                }
            }
        }
    }

    // Draw a node
    private void drawNode(Pane pane, Object value, double x, double y, double width, double height, Color fill) {
        Rectangle rect = new Rectangle(x, y, width, height);
        
        if (fill.equals(Color.TRANSPARENT)) {
            rect.setStroke(Color.GRAY);
            rect.setFill(Color.TRANSPARENT);
            pane.getChildren().add(rect);
            return;
        } else {
            rect.setStroke(Color.BLACK);
            rect.setFill(fill);
        }

        String textValue = value == null ? "" : value.toString();
        Text text = new Text(textValue);
        double textWidth = text.getBoundsInLocal().getWidth();
        double textHeight = text.getBoundsInLocal().getHeight();

        if (value != null && "NULL".equals(value.toString())) {
            text.setX(x + (width - textWidth) / 2);
            text.setY(y + (height + textHeight) / 2 - 2);
            pane.getChildren().addAll(rect, text);
        } else {
            double midX = x + width / 2;
            Line divider = new Line(midX, y, midX, y + height);
            divider.setStroke(Color.BLACK);

            text.setX(x + (width / 2 - textWidth) / 2);
            text.setY(y + (height + textHeight) / 2 - 2);

            Text nextText = new Text("Next");
            double nextWidth = nextText.getBoundsInLocal().getWidth();
            nextText.setX(midX + (width / 2 - nextWidth) / 2);
            nextText.setY(y + (height + textHeight) / 2 - 2);

            pane.getChildren().addAll(rect, divider, text, nextText);
        }
    }

    // Horizontal arrow between nodes
    private void drawHorizontalArrow(Pane pane, double startX, double startY, double spacingX) {
        double endX = startX + spacingX;
        double endY = startY;

        Line line = new Line(startX, startY, endX, endY);
        Line arrow1 = new Line(endX, endY, endX - 10, endY - 5);
        Line arrow2 = new Line(endX, endY, endX - 10, endY + 5);
        pane.getChildren().addAll(line, arrow1, arrow2);
    }

    // Wrap arrow to next row
    private void drawRowWrapArrow(Pane pane, double startX, double startY, double nextRowStartX, double nextRowY, double spacingX) {
        double midX = startX + spacingX / 2;
        Line lineStub = new Line(startX, startY, midX, startY);
        pane.getChildren().add(lineStub);

        double reappearX = nextRowStartX - spacingX / 2;
        double reappearY = nextRowY;

        Line lineReappear = new Line(reappearX, reappearY, nextRowStartX, reappearY);
        Line arrow1 = new Line(nextRowStartX, reappearY, nextRowStartX - 10, reappearY - 5);
        Line arrow2 = new Line(nextRowStartX, reappearY, nextRowStartX - 10, reappearY + 5);

        pane.getChildren().addAll(lineReappear, arrow1, arrow2);
    }

    public ArrayList<DataStructureState<T>> getStates() { 
        return this.states; 
    }

    public void clearStates() { 
        this.states = new ArrayList<>(); 
    }

	public void saveState(IDataStructure<T> ds, Parameters param) {
        this.states.add(new DataStructureState<>(ds, param));
    }
}
