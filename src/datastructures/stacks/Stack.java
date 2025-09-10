package datastructures.stacks;

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

public class Stack<T> implements IDataStructure<T>, Iterable<T> {

    ArrayList<DataStructureState<T>> states = new ArrayList<>();

	Node top = null;
	int size = 0;

	@Override
	public void push(T value) {
		Node newtop = new Node(value, this.top);
		this.top = newtop;
		this.size++;

		Parameters param = new Parameters();
		param.getInvsible().add(0);
		saveState(this, param);

		param = new Parameters();
		param.setObjective(0);

		saveState(this, param);
		saveState(this, param);
		saveState(this, new Parameters());
	}

	@Override
	public T pop() {
		if(this.size == 0) throw new NoSuchElementException("Stack is empty");

		Stack<T> clone = this.clone();
		Parameters param = new Parameters();
		param.getFailure().add(0);
		saveState(clone, param);

		param = new Parameters();
		param.getInvsible().add(0);
		saveState(clone, param);

		Node prevNode = this.top.prev();
		T result = this.top.getValue();
		this.top = prevNode;
		this.size--;

		saveState(this, new Parameters());
		return result;
	}

	@Override
	public T peek() {
		if(this.size == 0) throw new NoSuchElementException("Stack is empty");
		
		Parameters param = new Parameters();
		param.setObjective(0);
		saveState(this, param);
		saveState(this, param);
		saveState(this, new Parameters());

		return this.top.getValue();
	}

	@Override
	public void clear() {
		this.top = null;
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
	public Stack<T> clone() {
		Stack<T> result = new Stack<>();
		Stack<T> temp = new Stack<>();

		for(T value : this) {
			temp.push(value);
		}

		for(T value : temp) {
			result.push(value);
		}

		return result;
	}

    private class StackIterator implements Iterator<T> {
        private Node current = top;

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
            current = current.prev();
            return value;
        }

        @Override
        public void remove() {
            throw new UnsupportedOperationException("Remove not supported in iterator");
        }
    }
	
	private class Node implements INode<T> {
        private T value;
        private Node prev;

        public Node(T value, Node prev) {
            this.value = value;
            this.prev = prev;
        }

        public T getValue() {
            return this.value;
        }

        public Node prev() {
            return this.prev;
        }
    }

	@Override
	public void draw(Pane pane, Parameters param) {
		pane.getChildren().clear();

        double paneWidth = pane.getWidth() > 0 ? pane.getWidth() : 800;
        double paneHeight = pane.getHeight() > 0 ? pane.getHeight() : 600;
		
		double width = 150;
		double height = 50;
		
		
		int slots = this.size == 0 ? 1 : this.size;
		double totalHeight = slots * height + height/2;
		
		double x = paneWidth/2 - width/2;
		double startY = paneHeight/2 - totalHeight/2;
		
		if(this.size == 0) {
			Line leftLine = new Line(x, startY, x, startY + height/2);
			leftLine.setStroke(Color.BLACK);

			Line rightLine = new Line(x + width, startY, x + width, startY + height/2);
			rightLine.setStroke(Color.BLACK);

			startY += height/2;
			
			Rectangle rect = new Rectangle(x, startY, width, height);
			rect.setFill(Color.LIGHTBLUE);
			rect.setStroke(Color.BLACK);

			Text value = new Text("NULL");
			double textWidth = value.getBoundsInLocal().getWidth();
			double textHeight = value.getBoundsInLocal().getHeight();
			double textX = x + (width - textWidth) / 2;
			double textY = startY + (height + textHeight) / 2 - 4;
			value.setX(textX);
			value.setY(textY);
			
			pane.getChildren().addAll(leftLine, rightLine, rect, value);
		} else {
			Line leftLine = new Line(x, startY, x, startY + height/2);
			leftLine.setStroke(Color.BLACK);

			Line rightLine = new Line(x + width, startY, x + width, startY + height/2);
			rightLine.setStroke(Color.BLACK);

			pane.getChildren().addAll(leftLine, rightLine);

			startY += height/2;

			int count = 0;
			for(T v : this) {
				Rectangle rect = new Rectangle(x, startY, width, height);
				Color color = param.getColor(count);
				rect.setFill(color);
				rect.setStroke(Color.BLACK);
				pane.getChildren().add(rect);

				String val = (v != null) ? v.toString() : "NULL";
				Text value = new Text(val);
				double textWidth = value.getBoundsInLocal().getWidth();
				double textHeight = value.getBoundsInLocal().getHeight();
				double textX = x + (width - textWidth) / 2;
				double textY = startY + (height + textHeight) / 2 - 4;
				value.setX(textX);
				value.setY(textY);
				if(color != Color.TRANSPARENT)
    				pane.getChildren().add(value);

				startY += height;
				count++;
			}
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
