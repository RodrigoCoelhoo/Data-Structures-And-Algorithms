package datastructures.lists;

import java.util.Iterator;
import java.util.NoSuchElementException;

import datastructures.interfaces.IDataStructure;
import datastructures.interfaces.INode;
import javafx.scene.layout.Pane;

public class LinkedList<T> implements IDataStructure<T>, Iterable<T> {
	
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
	public void add(T value, int index) {

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
    public void draw(Pane pane) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'draw'");
    }
}
