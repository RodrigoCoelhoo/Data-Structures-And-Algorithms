package datastructures.trees;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

import datastructures.interfaces.IDataStructure;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.text.Text;
import utils.DataStructureState;
import utils.DataStructureState.Parameters;

public class MinHeap<T extends Comparable<T>> implements IDataStructure<T> {

    ArrayList<DataStructureState<T>> states = new ArrayList<>();
	
	private List<T> heap = new ArrayList<>();

	@Override
	public void push(T value) {
		int currentIndex = heap.size();
		heap.add(value);

		// Add node animation
		MinHeap<T> clone = this.clone();

		Parameters param = new Parameters();
		param.getInvsible().add(currentIndex);
		saveState(clone, param);
		param = new Parameters();
		param.setObjective(currentIndex);
		saveState(clone, param);

		ArrayList<Integer> indexs = new ArrayList<>();
		int swapps = 0;
		while(currentIndex != 0) {
			int parentIndex = getParent(currentIndex);
			T parentValue = heap.get(parentIndex);
			T currentValue = heap.get(currentIndex);

			if(parentValue.compareTo(currentValue) > 0) {
				swap(parentIndex, currentIndex);
				currentIndex = parentIndex;

				// Add each "Swapped" index
				indexs.add(parentIndex);

				param = new Parameters();
				param.setObjective(heap.size() - 1);
				param.getIndexs().addAll(indexs);
				saveState(clone, param);
				swapps++;
			} 
			else {
				break;
			}
		}

		if(this.heap.size() > 1 && swapps != 0) {
			param = new Parameters();
			indexs.add(heap.size() - 1);
			param.setObjective(currentIndex);
			param.getIndexs().addAll(indexs);
			saveState(this, param);
	
			param = new Parameters();
			param.setObjective(currentIndex);
			saveState(this, param);
		}
		saveState(this, new Parameters());
	}
	
	private int getParent(int index) {
		if (index == 0) {
			throw new IllegalArgumentException("Root has no parent");
		}
		return (index - 1) / 2;
	}

	private void swap(int i, int j) {
		T temp = heap.get(i);
		heap.set(i, heap.get(j));
		heap.set(j, temp);
	}

	@Override
	public T pop() {
		if (heap.isEmpty()) {
			throw new NoSuchElementException("Heap is empty");
		}

		MinHeap<T> clone = this.clone();

		if(heap.size() == 1) {
			Parameters param = new Parameters();
			param.getFailure().add(0);
			saveState(clone, param);

			param = new Parameters();
			param.getInvsible().add(0);
			saveState(clone, param);

			saveState(this, new Parameters());

			return heap.remove(0);
		}

		Parameters param = new Parameters();
		param.getFailure().add(0);
		saveState(clone, param);

		param = new Parameters();
		param.getInvsible().add(0);
		saveState(clone, param);

		param = new Parameters();
		param.getInvsible().add(0);
		param.getFailure().add(heap.size() - 1);
		saveState(clone, param);

		param = new Parameters();
		param.getInvsible().add(0);
		param.getInvsible().add(heap.size() - 1);
		saveState(clone, param);

		T result = heap.get(0);
		heap.set(0, heap.remove(heap.size() - 1));

		clone = this.clone();
		param = new Parameters();
		param.setObjective(0);
		saveState(clone, param);

		bubbleDown(0);
		return result;
	}

	private void bubbleDown(int index) {
		int leftChild, rightChild, smallest;

		int currentIndex = index;

		// Add node animation
		MinHeap<T> clone = this.clone();

		Parameters param = new Parameters();

		ArrayList<Integer> indexs = new ArrayList<>();
		int swapps = 0;
		while (true) {
			leftChild = 2 * currentIndex + 1; 
			rightChild = 2 * currentIndex + 2;
			smallest = currentIndex;          

			// Compare with left child
			if (leftChild < heap.size() && heap.get(leftChild).compareTo(heap.get(smallest)) < 0) {
				smallest = leftChild;
			}

			// Compare with right child
			if (rightChild < heap.size() && heap.get(rightChild).compareTo(heap.get(smallest)) < 0) {
				smallest = rightChild;
			}

			// If the smallest is not the current node, swap and continue
			if (smallest != currentIndex) {
				T temp = heap.get(currentIndex);
				heap.set(currentIndex, heap.get(smallest));
				heap.set(smallest, temp);

				currentIndex = smallest;

				// Add each "Swapped" index
				indexs.add(currentIndex);

				param = new Parameters();
				param.setObjective(index);
				param.getIndexs().addAll(indexs);
				saveState(clone, param);
				swapps++;
			} else {
				break;
			}
		}

		if(this.heap.size() > 1 && swapps != 0) {
			param = new Parameters();
			indexs.add(index);
			param.setObjective(currentIndex);
			param.getIndexs().addAll(indexs);
			saveState(this, param);
	
			param = new Parameters();
			param.setObjective(currentIndex);
			saveState(this, param);
		}
		saveState(this, new Parameters());
	}

	@Override
	public T peek() {
		if (heap.isEmpty()) {
			throw new NoSuchElementException("Heap is empty");
		}

		Parameters param = new Parameters();
		param.setObjective(0);
		saveState(this, param);
		saveState(this, param);
		saveState(this, new Parameters());

		return this.heap.get(0);
	}

	@Override
	public void clear() {
		this.heap = new ArrayList<>();
	}

	@Override
	public boolean isEmpty() {
		return this.heap.size() == 0;
	}

	@Override
	public MinHeap<T> clone() {
		MinHeap<T> copy = new MinHeap<>();
		copy.heap = new ArrayList<>(this.heap);
		return copy;
	}

	@Override
	public String info() {
		String result = "A Min Heap is a binary tree based data structure "
				+ "where each parent node is less than or equal to its child nodes. "
				+ "This ensures that the minimum element is always stored at the root of the heap.\n\n";

		result += "The Min Heap is commonly implemented using arrays (or ArrayLists in Java), "
				+ "where for a node at index i:\n"
				+ "- The left child is at index 2*i + 1\n"
				+ "- The right child is at index 2*i + 2\n"
				+ "- The parent is at index (i - 1) / 2\n\n";

		result += "Operations in a Min Heap:\n"
				+ "- Insertion (push): The element is added at the end and then 'bubbled up' "
				+ "to maintain the heap property.\n"
				+ "- Deletion (pop): The root element (minimum) is removed, and the last element "
				+ "is moved to the root, followed by a 'bubble down' to restore the heap property.\n"
				+ "- Peek: Retrieves the minimum element (root) without removing it.\n\n";

		result += "'n' is the number of elements in the heap.\n\n";

		result += "Access Time Complexity: O(1) for min (root)\n";
		result += "Search Time Complexity: O(n)\n";
		result += "Insert (Push) Time Complexity: O(log n)\n";
		result += "Delete (Pop) Time Complexity: O(log n)\n";
		result += "Space Complexity: O(n)\n\n";

		return result;
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


	@Override
	public void draw(Pane pane, Parameters param) {
		pane.getChildren().clear();

		double paneWidth = pane.getWidth() > 0 ? pane.getWidth() : 1600;
		double startY = 50;        // top margin
		double levelSpacing = 80;  // vertical space between levels
		double nodeRadius = 20;

		if (heap.isEmpty()) {
			// Draw "NULL" if heap is empty
			double nodeX = paneWidth / 2;
			double nodeY = 50;

			Circle circle = new Circle(nodeX, nodeY, nodeRadius);
			circle.setFill(Color.LIGHTGRAY);
			circle.setStroke(Color.BLACK);

			Text text = new Text("NULL");
			text.setFill(Color.BLACK);
			text.setFont(javafx.scene.text.Font.font(14));

			double textWidth = text.getBoundsInLocal().getWidth();
			double textHeight = text.getBoundsInLocal().getHeight();
			text.setX(nodeX - textWidth / 2);
			text.setY(nodeY + textHeight / 4);

			pane.getChildren().addAll(circle, text);
			return;
		}

		// Draw nodes level by level
		for (int i = 0; i < heap.size(); i++) {
			int level = (int) (Math.log(i + 1) / Math.log(2));    // which level (0-based)
			int indexInLevel = i - ((1 << level) - 1);            // position within level

			int nodesInLevel = 1 << level; // 2^level
			double spacing = paneWidth / (nodesInLevel + 1);      // horizontal spacing

			double x = (indexInLevel + 1) * spacing;
			double y = startY + level * levelSpacing;

			// Draw line to parent if not root
			if (i != 0) {
				int parentIndex = (i - 1) / 2;
				int parentLevel = (int) (Math.log(parentIndex + 1) / Math.log(2));
				int parentIndexInLevel = parentIndex - ((1 << parentLevel) - 1);
				int parentNodesInLevel = 1 << parentLevel;
				double parentSpacing = paneWidth / (parentNodesInLevel + 1);
				double parentX = (parentIndexInLevel + 1) * parentSpacing;
				double parentY = startY + parentLevel * levelSpacing;

				Line line = new Line(parentX, parentY, x, y);
				pane.getChildren().add(0, line); // draw behind nodes
			}

			// Draw node circle
			Circle circle = new Circle(x, y, nodeRadius);
			Color color = param.getColor(i); // index as position key
			circle.setFill(color);
			circle.setStroke(Color.BLACK);

			// Draw node value
			Text text = new Text(color == Color.TRANSPARENT ? "" : heap.get(i).toString());
			text.setFont(javafx.scene.text.Font.font(14));
			double textWidth = text.getBoundsInLocal().getWidth();
			double textHeight = text.getBoundsInLocal().getHeight();
			text.setX(x - textWidth / 2);
			text.setY(y + textHeight / 4);

			pane.getChildren().addAll(circle, text);
		}
	}
}
