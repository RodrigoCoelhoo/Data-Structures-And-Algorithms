package datastructures.queues;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.NoSuchElementException;

import datastructures.interfaces.IDataStructure;
import datastructures.interfaces.INode;
import javafx.geometry.Bounds;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import utils.DataStructureState;
import utils.DataStructureState.Parameters;

public class Queue<T> implements IDataStructure<T>, Iterable<T> {

    ArrayList<DataStructureState<T>> states = new ArrayList<>();

	Node head = null;
	Node tail = null;
	int size = 0;

	@Override
	public void enqueue(T value) {
		Node newhead = new Node(value, null);
		
		if(this.size == 0) {
			this.tail = newhead;
			this.head = newhead;
		} else {
			this.tail.setNext(newhead);
			this.tail = newhead;
		}

		Parameters param = new Parameters();
		param.getInvsible().add(this.size);
		saveState(this, param);
		param = new Parameters();
		param.setObjective(this.size);
		saveState(this, param);
		saveState(this, param);
		saveState(this, new Parameters());
		
		this.size++;
	}

	@Override
	public T dequeue() {
		if(this.size == 0) throw new NoSuchElementException("Queue is empty");
		Queue<T> temp = this.clone();

		Node nextNode = this.head.next();
		T result = this.head.getValue();
		this.head = nextNode;
		this.size--;

		Parameters param = new Parameters();
		param.getFailure().add(0);
		saveState(temp, param);
		param = new Parameters();
		param.getInvsible().add(0);
		saveState(temp, param);
		saveState(this, new Parameters());
		
		return result;
	}

	@Override
	public T peek() {
		if(this.size == 0) throw new NoSuchElementException("Queue is empty");

		Parameters param = new Parameters();
		param.setObjective(0);
		saveState(this, param);
		saveState(this, param);
		saveState(this, new Parameters());

		return this.head.getValue();
	}

	@Override
	public void clear() {
		this.head = null;
		this.size = 0;
	}

	@Override
	public int size() {
		return this.size;
	}

	@Override  
	public boolean isEmpty() {
		return this.size == 0;
	}

	@Override
    public Iterator<T> iterator() {
        return new StackIterator();
    }

	@Override
	public String info() {
		String result = "A queue is a linear data structure that follows the First-In-First-Out (FIFO) principle. "
				+ "This means that the first element added to the queue will be the first one to be removed. "
				+ "Elements are inserted at the rear (enqueue) and removed from the front (dequeue).\n\n";

		result += "Queues can be implemented using arrays or linked lists. "
				+ "An array-based implementation requires managing indices and may waste space if not circular, "
				+ "while a linked-list-based implementation grows dynamically but requires extra memory for pointers.\n\n";

		result += "Queues are commonly used in scheduling, buffering, breadth-first search, "
				+ "and any scenario where elements must be processed in order of arrival.\n\n";

		result += "'n' is the number of elements in the queue.\n\n";

		result += "Access Time Complexity: O(n)\n";
		result += "Search Time Complexity: O(n)\n";
		result += "Insert (Enqueue) Time Complexity: O(1)\n";
		result += "Delete (Dequeue) Time Complexity: O(1)\n";
		result += "Space Complexity: O(n)\n\n";

		return result;
	}


	@Override
    public Queue<T> clone() {
        Queue<T> result = new Queue<>();
        
        for(T data : this) {
            result.enqueue(data);
        }
        
        return result;
    }

    private class StackIterator implements Iterator<T> {
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
	
	class Node implements INode<T> {
        private T value;
        private Node next;

        public Node(T value, Node next) {
            this.value = value;
            this.next = next;
        }

        public T getValue() {
            return this.value;
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
		
		double box = 50;
		
		int slots = this.size == 0 ? 1 : this.size;
		double totalWidth = box * slots + box/2;
		double startX = paneWidth/2 - totalWidth/2;
		double y = paneHeight/2 - box/2;

		// Head = null
		if(this.size == 0) {
			Rectangle rect = new Rectangle(startX, y, box, box);
			rect.setFill(param.getColor(0));
			rect.setStroke(Color.BLACK);

			Text value = new Text("NULL");
			value.setFont(Font.font("Arial", 16));
			value.setX(startX + box / 2);
			value.setY(y + box / 2);
			Bounds bounds = value.getBoundsInLocal();
			value.setX(startX + (box - bounds.getWidth()) / 2);
			value.setY(y + (box + bounds.getHeight()) / 2);

			Line upLine = new Line(startX + box, y, startX + box + box/2, y);
			upLine.setStroke(Color.BLACK);

			Line downLine = new Line(startX + box, y + box, startX + box + box/2, y + box);
			downLine.setStroke(Color.BLACK);

			pane.getChildren().addAll(rect, value, upLine, downLine);
			return;
		}

		int count = 0;
		for(T v : this) {
			Rectangle rect = new Rectangle(startX, y, box, box);
			Color color = param.getColor(count++);

			String text = color == Color.TRANSPARENT ? "" : v.toString();
			Text value = new Text(text);
			value.setFont(Font.font("Arial", 16));
			value.setX(startX + box / 2);
			value.setY(y + box / 2);
			Bounds bounds = value.getBoundsInLocal();
			value.setX(startX + (box - bounds.getWidth()) / 2);
			value.setY(y + (box + bounds.getHeight()) / 2);

			startX += box;

			rect.setFill(color);
			rect.setStroke(Color.BLACK);

			pane.getChildren().addAll(rect, value);
		}

		Line upLine = new Line(startX, y, startX + box/2, y);
		upLine.setStroke(Color.BLACK);

		Line downLine = new Line(startX, y + box, startX + box/2, y + box);
		downLine.setStroke(Color.BLACK);

		pane.getChildren().addAll(upLine, downLine);
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
