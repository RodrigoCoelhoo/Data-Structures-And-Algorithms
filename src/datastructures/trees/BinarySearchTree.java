package datastructures.trees;

import datastructures.interfaces.INode;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.text.Text;
import utils.DataStructureState;
import utils.DataStructureState.Parameters;

import java.util.ArrayList;

import datastructures.interfaces.IDataStructure;

public class BinarySearchTree<T extends Comparable<T>> implements IDataStructure<T> {

    ArrayList<DataStructureState<T>> states = new ArrayList<>();

	private Node root = null;
	
	@Override
	public void insert(T value) {
		if(this.root == null) {
			this.root = new Node(value, null, null);
			saveState(this, new Parameters());
			return;
		}

		saveState(this, new Parameters());

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

	@Override
	public BinarySearchTree<T> clone() {
		BinarySearchTree<T> copy = new BinarySearchTree<>();
		copy.root = cloneNode(this.root);
		return copy;
	}

	private Node cloneNode(Node original) {
		if (original == null) return null;
		Node leftCopy = cloneNode(original.getLeft());
		Node rightCopy = cloneNode(original.getRight());
		return new Node(original.getValue(), leftCopy, rightCopy);
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
	public void draw(Pane pane, Parameters param) {
		pane.getChildren().clear();

		double paneWidth = pane.getWidth() > 0 ? pane.getWidth() : 1600;

		double startY = 50;      // top margin, change to dynamic vertical center?
		double minSpacing = 50;  // minimum horizontal spacing between nodes

		if (root != null) {
			// Calculate total width of the tree
			double treeWidth = calculateTreeWidth(root, minSpacing);

			// Start X to center the tree
			double startX = (paneWidth - treeWidth) / 2;

			// Draw tree recursively
			drawNodeCentered(pane, param, root, startX, startY, minSpacing);
		}
	}

	/**
	 * Draw a node and its subtree, return the width of the subtree.
	 */
	private double drawNodeCentered(Pane pane, Parameters param, Node node, double x, double y, double minSpacing) {
		if (node == null) return 0;

		double nodeRadius = 20;
		double verticalSpacing = 80;

		// Calculate widths of left and right subtrees
		double leftWidth = calculateTreeWidth(node.getLeft(), minSpacing);
		double rightWidth = calculateTreeWidth(node.getRight(), minSpacing);

		// Current node's X position
		double nodeX = x + leftWidth + (rightWidth > 0 ? 0 : minSpacing / 2);

		if (node.getLeft() != null) {
			double childX = x;
			double childY = y + verticalSpacing;
			double leftChildCenterX = drawNodeCentered(pane, param, node.getLeft(), childX, childY, minSpacing);
			Line line = new Line(nodeX, y, leftChildCenterX, childY);
			pane.getChildren().add(0, line); 
		}

		if (node.getRight() != null) {
			double childX = x + leftWidth + minSpacing;
			double childY = y + verticalSpacing;
			double rightChildCenterX = drawNodeCentered(pane, param, node.getRight(), childX, childY, minSpacing);
			Line line = new Line(nodeX, y, rightChildCenterX, childY);
			pane.getChildren().add(0, line); 
		}

		// Draw the current node
		Circle circle = new Circle(nodeX, y, nodeRadius);
		circle.setFill(Color.LIGHTBLUE);
		circle.setStroke(Color.BLACK);
		Text text = new Text(nodeX - 6, y + 4, node.getValue().toString());
		pane.getChildren().addAll(circle, text);

		return nodeX;
	}

	/**
	 * Recursively calculate the width of the subtree for centering.
	 */
	private double calculateTreeWidth(Node node, double minSpacing) {
		if (node == null) return 0;

		double leftWidth = calculateTreeWidth(node.getLeft(), minSpacing);
		double rightWidth = calculateTreeWidth(node.getRight(), minSpacing);

		if (leftWidth == 0 && rightWidth == 0) return minSpacing;

		return leftWidth + rightWidth + minSpacing;
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
