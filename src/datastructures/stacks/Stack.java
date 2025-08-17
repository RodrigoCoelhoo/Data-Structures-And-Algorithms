package datastructures.stacks;

import java.util.Iterator;
import java.util.NoSuchElementException;

import datastructures.interfaces.IDataStructure;
import datastructures.interfaces.INode;
import javafx.scene.layout.Pane;

public class Stack<T> implements IDataStructure<T>, Iterable<T> {

	Node top = null;
	int size = 0;

	@Override
	public void push(T value) {
		Node newtop = new Node(value, this.top);
		this.top = newtop;
		this.size++;
	}

	@Override
	public T pop() {
		if(this.size == 0) throw new NoSuchElementException("Stack is empty");

		Node prevNode = this.top.prev();
		T result = this.top.getValue();
		this.top = prevNode;
		this.size--;
		return result;
	}

	@Override
	public T peek() {
		if(this.size == 0) throw new NoSuchElementException("Stack is empty");
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
	public void draw(Pane pane) {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException("Unimplemented method 'draw'");
	}
}
