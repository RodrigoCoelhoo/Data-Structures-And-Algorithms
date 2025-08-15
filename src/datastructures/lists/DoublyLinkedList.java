package datastructures.lists;

import java.util.Iterator;
import java.util.NoSuchElementException;

import datastructures.interfaces.IDataStructure;
import datastructures.interfaces.INode;

public class DoublyLinkedList<T> implements IDataStructure<T>, Iterable<T> {
	
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
	}

	@Override
	public void add(T value, int index) {

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
	}

	@Override
    public T get(int index) {
        if(index < 0 || index >= this.size) throw new IndexOutOfBoundsException();

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

		Node nodeToRemove = getNodeAt(index);


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
    }

    @Override
    public void remove(T value) {
        if(this.size == 0) throw new IndexOutOfBoundsException();

        Node current = this.head;
        for(int i = 0; i < this.size; i++) 
		{
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
				return;
            }

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
        return new DoublyLinkedListIterator();
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

		if(index < mid) { // From head
			Node current = this.head;
			for(int i = 0; i < index; i++) {
				current = current.next();
			}
        	return current;
		} else { // From tail
			Node current = this.tail;
			for(int i = 0; i < this.size - 1 - index; i++) {
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
}
