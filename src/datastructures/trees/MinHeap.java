package datastructures.trees;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

import datastructures.interfaces.IDataStructure;
import javafx.scene.layout.Pane;

public class MinHeap<T extends Comparable<T>> implements IDataStructure<T> {
	List<T> heap = new ArrayList<>();

	@Override
	public void push(T value) {
		int currentIndex = heap.size();
		heap.add(value);

		while(currentIndex != 0) {
			int parentIndex = getParent(currentIndex);
			T parentValue = heap.get(parentIndex);
			T currentValue = heap.get(currentIndex);

			if(parentValue.compareTo(currentValue) > 0) {
				swap(parentIndex, currentIndex);
				currentIndex = parentIndex;
			} 
			else {
				break;
			}
		}
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

		T result = heap.get(0);
		heap.set(0, heap.remove(heap.size() - 1));

		bubbleDown(0);
		return result;
	}

	private void bubbleDown(int index) {
		int leftChild, rightChild, smallest;

		int currentIndex = index;
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
			} else {
				break;
			}
		}
	}

	@Override
	public T peek() {
		if (heap.isEmpty()) {
			throw new NoSuchElementException("Heap is empty");
		}

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
	public void draw(Pane pane) {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException("Unimplemented method 'draw'");
	}
}
