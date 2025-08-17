package datastructures.queues;

import java.util.Iterator;
import java.util.NoSuchElementException;

import datastructures.interfaces.IDataStructure;
import datastructures.interfaces.INode;
import javafx.scene.layout.Pane;

public class Queue<T> implements IDataStructure<T>, Iterable<T> {

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
		
		this.size++;
	}

	@Override
	public T dequeue() {
		if(this.size == 0) throw new NoSuchElementException("Queue is empty");

		Node nextNode = this.head.next();
		T result = this.head.getValue();
		this.head = nextNode;
		this.size--;
		return result;
	}

	@Override
	public T peek() {
		if(this.size == 0) throw new NoSuchElementException("Queue is empty");
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
	public void draw(Pane pane) {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException("Unimplemented method 'draw'");
	}
}
