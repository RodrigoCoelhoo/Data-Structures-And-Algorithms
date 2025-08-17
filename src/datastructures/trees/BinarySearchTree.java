package datastructures.trees;

import datastructures.interfaces.INode;
import javafx.scene.layout.Pane;
import datastructures.interfaces.IDataStructure;

public class BinarySearchTree<T extends Comparable<T>> implements IDataStructure<T> {
	Node root = null;
	
	@Override
	public void insert(T value) {
		if(this.root == null) {
			this.root = new Node(value, null, null);
			return;
		}

		Node current = this.root;
		while(current != null) {
			if(current.getValue().compareTo(value) > 0) {
				Node next = current.getLeft();

				if(next == null) {
					Node newNode = new Node(value, null, null);

					current.setLeft(newNode);
					return;
				}
				current = next;
			} else {
				Node next = current.getRight();

				if(next == null) {
					Node newNode = new Node(value, null, null);

					current.setRight(newNode);
					return;
				}
				current = next;
			}
		}
	}

	@Override
	public Node search(T value) {
		Node current = this.root;

		while(current != null) {

			int compareToValue = current.getValue().compareTo(value);

			if(compareToValue > 0) {
				current = current.getLeft();
			} 
			else if (compareToValue < 0) {
				current = current.getRight();
			} 
			else {
				return current;
			}
		}

		return null;
	}

	@Override
	public void delete(T value) {
		Node prev = null;
		Node current = this.root;

		while(current != null) {

			int compareToValue = current.getValue().compareTo(value);

			if(compareToValue > 0) {
				prev = current;
				current = current.getLeft();
			} 
			else if (compareToValue < 0) {
				prev = current;
				current = current.getRight();
			} 
			else {
				handleDelete(prev, current);
    			break;
			}
		}
	}

	/**
	 * 
	 * @param prev Parent of current
	 * @param current Node to be deleted
	 */
	private void handleDelete(Node prev, Node current) {
		int childCount = countChild(current);

		if(childCount == 0) {
			if(current == this.root) {
				this.root = null;
			}
			else if(prev.getLeft().equals(current)) {
				prev.setLeft(null);
			} 
			else {
				prev.setRight(null);
			}

			return;
		} 
		else if(childCount == 1) {
			Node child = (current.getLeft() != null) ? current.getLeft() : current.getRight();
			if(prev == null) {
				root = child;
			} else if(prev.getLeft() == current) {
				prev.setLeft(child);
			} else {
				prev.setRight(child);
			}

			return;
		}
		else if(childCount == 2) {
			// Find leftmost node of right subtree
			Node parentOfSuccessor = current;
			Node successor = current.getRight();

			while(successor.getLeft() != null) {
				parentOfSuccessor = successor;
				successor = successor.getLeft();
			}

    		current.setValue(successor.getValue());

			// Handle deleted node child
			Node child = successor.getRight(); // may be null
			if(parentOfSuccessor.getLeft() == successor) {
				parentOfSuccessor.setLeft(child);
			} else {
				parentOfSuccessor.setRight(child);
			}
		}
	}

	/**
	 * @param node
	 * @return number of childrens of node
	 */
	private int countChild(Node node) {
		int count = 0;
		if(node.getLeft() != null) count++;
		if(node.getRight() != null) count++;

		return count;
	}

	private class Node implements INode<T> {
		T value;
		Node leftChild;
		Node rightChild;

		public Node(T value, Node leftChild, Node rightChild) {
			this.value = value;
			this.leftChild = leftChild;
			this.rightChild = rightChild;
		}

		public Node getLeft() {
			return this.leftChild;
		}

		public void setLeft(Node left) {
			this.leftChild = left;
		}

		public Node getRight() {
			return this.rightChild;
		}

		public void setRight(Node right) {
			this.rightChild = right;
		}

		public T getValue() {
			return this.value;
		}

		public void setValue(T value) {
			this.value = value;
		}

	}

	@Override
	public void draw(Pane pane) {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException("Unimplemented method 'draw'");
	}
}
