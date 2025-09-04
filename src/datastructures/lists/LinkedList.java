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

public class LinkedList<T> implements IDataStructure<T>, Iterable<T> {

    ArrayList<DataStructureState<T>> states = new ArrayList<>();
	
	private Node head = null;
    private Node tail = null;
	private int size = 0;
	
	@Override
	public void add(T value) {
        Node newNode = new Node(value, null);

        if(this.head == null) {
            this.head = newNode;
            this.tail = newNode;
        } else {
            this.tail.setNext(newNode);
            this.tail = newNode;
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
	}

    @Override
    public T get(int index) {
        if(index < 0 || index >= this.size) throw new IndexOutOfBoundsException();

        if(index == 0) {
            return this.head.getValue();
        }

        if(index == this.size - 1) {
            return this.tail.getValue();
        }

        Node result = getNodeAt(index);
        return result.getValue();
    }

    @Override
    public void set(int index, T value) {
        if(index < 0 || index >= this.size) throw new IndexOutOfBoundsException();

        Node node = getNodeAt(index);
        node.setValue(value);
    }

    @Override
    public void remove(int index) {
        if(index < 0 || index >= this.size) throw new IndexOutOfBoundsException();

        if(index == 0) {
            this.head = this.head.next();
            if (this.size == 1) {
                this.tail = null;
            }

            this.size--;
            return;
        }
        
        Node prev = getNodeAt(index - 1);

        if(index == this.size - 1) {
            this.tail = prev;
        }

        prev.setNext(prev.next().next());
        this.size--;
    }

    @Override
    public void remove(T value) {
        if(this.size == 0) throw new IndexOutOfBoundsException();

        Node prev = null;
        Node current = this.head;
        for(int i = 0; i < this.size; i++) {
            T currentValue = current.getValue(); 
            if(currentValue.equals(value)) {
                if(i == 0) {
                    this.head = this.head.next();
                    if (this.size == 1) {
                        this.tail = null;
                    }
                    this.size--;
                    return;
                }

                if(i == this.size -1) {
                    this.tail = prev;
                }

                prev.setNext(current.next());
                this.size--;
                return;
            }

            prev = current;
            current = current.next();
        }
    }

    @Override
    public int size() {
        return this.size;
    }

    @Override
    public void clear() {
        this.head = null;
        this.tail = null;
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
        if(index == 0) {
            return this.head;
        }

        if(index == this.size - 1) {
            return this.tail;
        }

        Node current = this.head;
        for(int i = 0; i < index; i++) {
            current = current.next();
        }
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
            result.add(data);
        }
        
        return result;
    }

    @Override
    public void draw(Pane pane) {
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
                drawNode(pane, current.getValue(), x, y, nodeWidth, nodeHeight);
                current = current.next();
            }

            // Draw arrows if not last
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
        rect.setStroke(Color.BLACK);
        rect.setFill(fill);

        Text text = new Text(value.toString());
        double textWidth = text.getBoundsInLocal().getWidth();
        double textHeight = text.getBoundsInLocal().getHeight();

        if ("NULL".equals(value.toString())) {
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

    private void drawNode(Pane pane, Object value, double x, double y, double width, double height) {
        drawNode(pane, value, x, y, width, height, Color.LIGHTBLUE);
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

    public void saveState(IDataStructure<T> ds, int index, String path) {
        this.states.add(new DataStructureState<>(ds, index, path));
    }
}
