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

public class DoublyLinkedList<T> implements IDataStructure<T>, Iterable<T> {

    ArrayList<DataStructureState<T>> states = new ArrayList<>();
	
	private Node head = null;
    private Node tail = null;
	private int size = 0;
	
	@Override
	public void add(T value) {
		if(this.head == null) {
			Node newNode = new Node(value, null, null);
            this.head = newNode;
            this.tail = newNode;
        } else {
			Node newNode = new Node(value, this.tail, null);
            this.tail.setNext(newNode);
            this.tail = newNode;
        }

        this.size++;

        Parameters param = new Parameters();
        param.getInvsible().add(this.size);
        saveState(this, param);

        param = new Parameters();
        param.setObjective(this.size);
        saveState(this, param);

        saveState(this, new Parameters());
        
	}

	@Override
	public void add(int index, T value) {

        if(index < 0 || index > this.size) throw new IndexOutOfBoundsException();
        
        if(index == 0) {
            if(this.head == null) {
                add(value);
                return;
            }

            Node newHead = new Node(value, null, this.head);
			this.head.setPrev(newHead);
            this.head = newHead;
            this.size++;

            Parameters param = new Parameters();
            param.getInvsible().add(1);
            saveState(this, param);

            param = new Parameters();
            param.setObjective(1);
            saveState(this, param);

            saveState(this, new Parameters());
            return;
        }

        if(index == this.size) { 
			add(value);
			return;
		}

        Node currentNode = getNodeAt(index - 1);
        Node newNode = new Node(value, currentNode, currentNode.next());
		currentNode.next().setPrev(newNode);
        currentNode.setNext(newNode);
        this.size++;

        Parameters param = new Parameters();
        param.getInvsible().add(index + 1);
        saveState(this, param);

        param = new Parameters();
        param.setObjective(index + 1);
        saveState(this, param);

        saveState(this, new Parameters());
	}

	@Override
    public T get(int index) {
        if(index < 0 || index >= this.size) throw new IndexOutOfBoundsException();

        Node result = getNodeAt(index);

        Parameters param = new Parameters();
        if(index >= this.size/2) {
            param.getIndexs().add(index+1);
            saveState(this, param);
        }

        param = new Parameters();
        param.setObjective(index+1);
        saveState(this, param);
        saveState(this, param);

        saveState(this, new Parameters());

        return result.getValue();
    }

    @Override
    public void set(int index, T value) {
        if(index < 0 || index >= this.size) throw new IndexOutOfBoundsException();

        Node node = getNodeAt(index);
        Parameters param = new Parameters();

        DoublyLinkedList<T> temp = this.clone();
        if(index >= this.size/2) {
            param.getIndexs().add(index+1);
            saveState(temp, param);
        }
        
        param = new Parameters();
        param.setObjective(index+1);
        saveState(temp.clone(), param);

        // For animation
        node.setValue(null);
        saveState(this.clone(), param);

        node.setValue(value);
        saveState(this.clone(), param);

        saveState(this, new Parameters());
    }

    @Override
    public void remove(int index) {
        if(index < 0 || index >= this.size) throw new IndexOutOfBoundsException();

		Node nodeToRemove = getNodeAt(index);

        DoublyLinkedList<T> temp = this.clone();

        Parameters param = new Parameters();
        if(index >= this.size/2) {
            param.getIndexs().add(index+1);
            saveState(temp, param);
        }
        
        param = new Parameters();
        param.getFailure().add(index+1);
        saveState(temp, param);
        
        param = new Parameters();
        param.getInvsible().add(index+1);
        saveState(temp, param);

        if(nodeToRemove.prev() != null) {
			nodeToRemove.prev().setNext(nodeToRemove.next());
		} else {
			this.head = nodeToRemove.next();
		}
        
        if(nodeToRemove.next() != null) {
			nodeToRemove.next().setPrev(nodeToRemove.prev());
		} else {
			this.tail = nodeToRemove.prev();
		}
		
		nodeToRemove.setNext(null);
		nodeToRemove.setPrev(null);

        this.size--;

        saveState(this, new Parameters());
    }

    @Override
    public void remove(T value) {
        if(this.size == 0) throw new IndexOutOfBoundsException();

        Node current = this.head;
        DoublyLinkedList<T> temp = this.clone();
        Parameters failureParameters = new Parameters();
        for(int i = 0; i < this.size; i++) 
		{
            Parameters param = new Parameters();
            param.getIndexs().add(i+1);
            saveState(temp, param);

            T currentValue = current.getValue(); 

            if(currentValue.equals(value)) {
				if(current.prev() != null) {
					current.prev().setNext(current.next());
				} else {
					this.head = current.next();
				}
				
				if(current.next() != null) {
					current.next().setPrev(current.prev());
				} else {
					this.tail = current.prev();
				}

				current.setNext(null);
				current.setPrev(null);
				this.size--;

                param = new Parameters();
                param.getFailure().add(i+1);
                saveState(temp, param);
                
                param = new Parameters();
                param.getInvsible().add(i+1);
                saveState(temp, param);

                saveState(this, new Parameters());
				return;
            }

            current = current.next();
            failureParameters.getFailure().add(i+1);
        }
        
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
        return new DoublyLinkedListIterator();
    }

    @Override
    public DoublyLinkedList<T> clone() {
        DoublyLinkedList<T> result = new DoublyLinkedList<>();

        for(T data : this) {
            result.add(data);
        }

        return result;
    }

    @Override
    public String info() {
        String result = "A doubly linked list is a linear data structure where each element (node) "
                + "contains a value, a reference (or pointer) to the next node, "
                + "and a reference to the previous node in the sequence. "
                + "Unlike arrays, doubly linked lists do not require contiguous memory locations, "
                + "and allow efficient insertions and deletions at both the beginning and the end, "
                + "as well as before or after a known node.\n\n";

        result += "Differently from singly linked lists, doubly linked lists have the ability to traverse backwards and "
                + "access the previous node makes certain operations more efficient, "
                + "particularly deletions, since a node can be removed directly if a reference to it is available.\n\n";

        result += "However, doubly linked lists use more memory than singly linked lists "
                + "because each node requires an extra pointer to the previous node.\n\n";
                

        result += "'n' is the number of elements in the doubly linked list.\n\n";

        result += "Access Time Complexity: O(n)\n";
        result += "Search Time Complexity: O(n)\n";
        result += "Insert Time Complexity: O(1) (at head, tail, or before/after a known node)\n";
        result += "Delete Time Complexity: O(1) (at head, tail, or with direct node reference)\n";
        result += "Space Complexity: O(n) (each node stores two references)\n\n";

        return result;
    }


    private class DoublyLinkedListIterator implements Iterator<T> {
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
        int mid = this.size/2;

        DoublyLinkedList<T> temp = this.clone();

		if(index < mid) { // From head
			Node current = this.head;
			for(int i = 0; i < index; i++) {
                Parameters param = new Parameters();
                param.getIndexs().add(i+1);
                saveState(temp, param);

				current = current.next();
			}
            
            Parameters param = new Parameters();
            param.getIndexs().add(index+1);
            saveState(temp, param);

        	return current;
		} else { // From tail
			Node current = this.tail;
			for (int i = this.size - 1; i > index; i--) {
                Parameters param = new Parameters();
                param.getIndexs().add(i + 1);
                saveState(temp, param);

				current = current.prev();
			}

        	return current;
		}
    }

	private class Node implements INode<T> {
        private T value;
		private Node prev;
        private Node next;

        public Node(T value, Node prev, Node next) {
            this.value = value;
			this.prev = prev;
            this.next = next;
        }

        public T getValue() {
            return value;
        }

        public void setValue(T value) {
            this.value = value;
        }

		public Node prev() {
			return this.prev;
		}

		public void setPrev(Node prev) {
			this.prev = prev;
		}

        public Node next() {
            return this.next;
        }

        public void setNext(Node next) {
            this.next = next;
        }
    }

    @Override
    public void draw(Pane pane, Parameters param) {
        pane.getChildren().clear();

        double paneWidth = pane.getWidth() > 0 ? pane.getWidth() : 800;
        double paneHeight = pane.getHeight() > 0 ? pane.getHeight() : 600;

        double nodeWidth = 90;
        double nodeHeight = 30;
        double spacingX = 40;
        double spacingY = 70;

        int totalNodes = this.size + 2; // +2 for NULLs
        if (totalNodes == 2) {
            double x = (paneWidth - nodeWidth) / 2;
            double y = (paneHeight - nodeHeight) / 2;
            drawNode(pane, "NULL", x, y, nodeWidth, nodeHeight, Color.LIGHTGRAY);
            return;
        }

        int nodesInRow = Math.max(1, (int)((paneWidth - 100) / (nodeWidth + spacingX)));
        int totalRows = (int) Math.ceil((double) totalNodes / nodesInRow);
        double firstRowStartX = (paneWidth - Math.min(nodesInRow, totalNodes) * nodeWidth
                - (Math.min(nodesInRow, totalNodes) - 1) * spacingX) / 2;

        Node current = this.head;

        for (int i = 0; i < totalNodes; i++) {
            boolean isStartNull = (i == 0);
            boolean isEndNull = (i == totalNodes - 1);

            int row = i / nodesInRow;
            int col = i % nodesInRow;
            int nodesThisRow = Math.min(nodesInRow, totalNodes - row * nodesInRow);

            double x = firstRowStartX + col * (nodeWidth + spacingX);
            double offset = totalRows % 2 == 1 ? row - totalRows / 2 : row - totalRows / 2 + 0.5;
            double y = paneHeight / 2 + offset * spacingY;

            if (isStartNull || isEndNull) {
                drawNode(pane, "NULL", x, y, nodeWidth, nodeHeight, Color.LIGHTGRAY);
            } else {
                drawNode(pane, current.getValue(), x, y, nodeWidth, nodeHeight, param.getColor(i));
                current = current.next();
            }

            if (i < totalNodes - 1) {
                boolean lastInRow = (col == nodesThisRow - 1);
                double centerY = y + nodeHeight / 2;
                double arrowOffsetY = 8;

                if (!lastInRow) {
                    if(isStartNull) {
                        drawArrow(pane, x + nodeWidth + spacingX, centerY, x + nodeWidth, centerY, false); 
                    } else if(i == totalNodes - 2) {
                        drawArrow(pane, x + nodeWidth, centerY, x + nodeWidth + spacingX, centerY, true);
                    }
                    else {
                        drawArrow(pane, x + nodeWidth, centerY - arrowOffsetY, x + nodeWidth + spacingX, centerY - arrowOffsetY, true);
                        drawArrow(pane, x + nodeWidth + spacingX, centerY + arrowOffsetY, x + nodeWidth, centerY + arrowOffsetY, false);
                    }
                } else {
                    int nextRow = row + 1;
                    double nextRowOffset = totalRows % 2 == 1 ? nextRow - totalRows / 2 : nextRow - totalRows / 2 + 0.5;
                    double nextRowY = paneHeight / 2 + nextRowOffset * spacingY + nodeHeight / 2;

                    double midX = x + nodeWidth + spacingX / 2;
                    
                    if(i == totalNodes - 2) {
                        drawArrow(pane, x + nodeWidth, centerY, midX, centerY, true);
                        drawArrow(pane, firstRowStartX - spacingX / 2, nextRowY, firstRowStartX, nextRowY, true);
                    } else {
                        drawArrow(pane, x + nodeWidth, centerY - arrowOffsetY, midX, centerY - arrowOffsetY, true);
                        drawArrow(pane, firstRowStartX - spacingX / 2, nextRowY - arrowOffsetY, firstRowStartX, nextRowY - arrowOffsetY, true);
    
                        drawArrow(pane, firstRowStartX, nextRowY + arrowOffsetY, firstRowStartX - spacingX / 2, nextRowY + arrowOffsetY, false); 
                        drawArrow(pane, midX, centerY + arrowOffsetY, x + nodeWidth, centerY + arrowOffsetY, false);
                    }
                }
            }
        }
    }

    private void drawArrow(Pane pane, double startX, double startY, double endX, double endY, boolean forward) {
        Line line = new Line(startX, startY, endX, endY);
        double arrowSize = 5;
        double dx = endX - startX;
        double dy = endY - startY;
        double angle = Math.atan2(dy, dx);
        double arrowAngle = Math.PI / 6;
        double x1, y1, x2, y2;

        if (forward) {
            x1 = endX - arrowSize * Math.cos(angle - arrowAngle);
            y1 = endY - arrowSize * Math.sin(angle - arrowAngle);
            x2 = endX - arrowSize * Math.cos(angle + arrowAngle);
            y2 = endY - arrowSize * Math.sin(angle + arrowAngle);
        } else {
            angle = Math.atan2(startY - endY, startX - endX);
            x1 = endX + arrowSize * Math.cos(angle - arrowAngle);
            y1 = endY + arrowSize * Math.sin(angle - arrowAngle);
            x2 = endX + arrowSize * Math.cos(angle + arrowAngle);
            y2 = endY + arrowSize * Math.sin(angle + arrowAngle);
        }

        Line arrow1 = new Line(endX, endY, x1, y1);
        Line arrow2 = new Line(endX, endY, x2, y2);
        pane.getChildren().addAll(line, arrow1, arrow2);
    }

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
            // Center NULL in full rectangle
            text.setX(x + (width - textWidth) / 2);
            text.setY(y + (height + textHeight) / 2 - 2);
            pane.getChildren().addAll(rect, text);
        } else {
            // Divider positions
            double third = width / 3;
            double div1X = x + third;
            double div2X = x + 2 * third;

            Line divider1 = new Line(div1X, y, div1X, y + height);
            Line divider2 = new Line(div2X, y, div2X, y + height);
            divider1.setStroke(Color.BLACK);
            divider2.setStroke(Color.BLACK);

            // Left text ("Prev")
            Text prevText = new Text("Prev");
            double prevWidth = prevText.getBoundsInLocal().getWidth();
            prevText.setX(x + (third - prevWidth) / 2);
            prevText.setY(y + (height + textHeight) / 2 - 2);

            // Middle text (node value)
            double valueWidth = text.getBoundsInLocal().getWidth();
            text.setX(div1X + (third - valueWidth) / 2);
            text.setY(y + (height + textHeight) / 2 - 2);

            // Right text ("Next")
            Text nextText = new Text("Next");
            double nextWidth = nextText.getBoundsInLocal().getWidth();
            nextText.setX(div2X + (third - nextWidth) / 2);
            nextText.setY(y + (height + textHeight) / 2 - 2);

            pane.getChildren().addAll(rect, divider1, divider2, prevText, text, nextText);
        }
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
