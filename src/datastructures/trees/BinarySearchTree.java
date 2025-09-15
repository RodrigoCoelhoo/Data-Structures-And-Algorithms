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
			this.root.setPosition("");
			
			Parameters param = new Parameters();
			param.setInv("");
			saveState(this, param);

			param = new Parameters();
			param.setObj("");
			saveState(this, param);
			saveState(this, param);
			saveState(this, new Parameters());
			
			return;
		}
		
		BinarySearchTree<T> clone = this.clone();
		
		Node current = this.root;
		Parameters param = new Parameters();
		String str = "";
		param.setIndex(str);
		saveState(clone, param);
		
		while(current != null) {
			param = new Parameters();
			if(current.getValue().compareTo(value) > 0) {
				str += "L";
				Node next = current.getLeft();
				
				if(next == null) {
					param = new Parameters();

					Node newNode = new Node(value, null, null);
					newNode.setPosition(str);
					current.setLeft(newNode);
					
					param.setInv(str);
					saveState(this, param);

					param = new Parameters();
					param.setObj(str);
					saveState(this, param);
					saveState(this, param);
					saveState(this, new Parameters());

					return;
				}
				current = next;
			} else {
				str += "R";
				Node next = current.getRight();

				if(next == null) {
					param = new Parameters();

					Node newNode = new Node(value, null, null);
					newNode.setPosition(str);
					current.setRight(newNode);
					
					param.setInv(str);
					saveState(this, param);

					param = new Parameters();
					param.setObj(str);
					saveState(this, param);
					saveState(this, param);
					saveState(this, new Parameters());

					return;
				}
				current = next;
			}

			param.setIndex(str);
			saveState(clone, param);
		}
	}

	@Override
	public Node search(T value) {
		Node current = this.root;

		Parameters param = new Parameters();
		String str = "";
		param.setIndex(str);
		saveState(this, param);

		while(current != null) {
			param = new Parameters();

			int compareToValue = current.getValue().compareTo(value);

			if(compareToValue > 0) {
				str += "L";
				current = current.getLeft();
			} 
			else if (compareToValue < 0) {
				str += "R";
				current = current.getRight();
			} 
			else {
				param.setObj(str);
				saveState(this, param);
				saveState(this, param);
				saveState(this, new Parameters());
				return current;
			}

			if(current != null) {
				param.setIndex(str);
				saveState(this, param);
			}
		}

		if(current == null) {
			param = new Parameters();
			param.setFail("*");
			saveState(this, param);
			saveState(this, param);
			saveState(this, new Parameters());
		}

		return null;
	}

	@Override
	public void delete(T value) {
		Node prev = null;
		Node current = this.root;
		
		BinarySearchTree<T> clone = this.clone();

		Parameters param = new Parameters();
		String str = "";
		param.setIndex(str);
		saveState(clone, param);

		while(current != null) {
			param = new Parameters();

			int compareToValue = current.getValue().compareTo(value);

			if(compareToValue > 0) {
				str += "L";
				prev = current;
				current = current.getLeft();
			} 
			else if (compareToValue < 0) {
				str += "R";
				prev = current;
				current = current.getRight();
			} 
			else {
				handleDelete(prev, current);
    			break;
			}

			if(current != null) {
				param.setIndex(str);
				saveState(clone, param);
			}
		}

		if(current == null) {
			param = new Parameters();
			param.setFail("*");
			saveState(this, param);
			saveState(this, param);
			saveState(this, new Parameters());
		}
	}

	/**
	 * 
	 * @param prev Parent of current
	 * @param current Node to be deleted
	 */
	private void handleDelete(Node prev, Node current) {
		int childCount = countChild(current);

		BinarySearchTree<T> clone = this.clone();

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

			Parameters param = new Parameters();
			String str = current.getPosition();
			param.setFail(str);
			saveState(clone, param);
			param = new Parameters();
			param.setInv(str);
			saveState(clone, param);
			saveState(this, new Parameters());

			return;
		} 
		else if(childCount == 1) {
			Node child = (current.getLeft() != null) ? current.getLeft() : current.getRight();

			Parameters param = new Parameters();
			String str = current.getPosition();
			param.setFail(str);
			saveState(clone, param);
			param = new Parameters();
			param.setInv(str);
			saveState(clone, param);
			
			child.setPosition(current.getPosition());
			saveState(this, new Parameters());

			
			if(prev == null) {
				root = child;
			} else if(prev.getLeft() == current) {
				prev.setLeft(child);
			} else {
				prev.setRight(child);
			}

			return;
		}
		else if (childCount == 2) {
			// Find leftmost node of right subtree (successor)
			Node parentOfSuccessor = current;

			Parameters param = new Parameters();
			String str = current.getPosition();
			param.setFail(str);
			saveState(clone, param);

			Node successor = current.getRight();
			param = new Parameters();
			param.setFail(str);
			param.setIndex(successor.getPosition());
			saveState(clone, param);

			while (successor.getLeft() != null) {
				parentOfSuccessor = successor;
				successor = successor.getLeft();
				
				param = new Parameters();
				param.setFail(str);
				param.setIndex(successor.getPosition());
				saveState(clone, param);
			}

			param = new Parameters();
			param.setFail(str);
			param.setObj(successor.getPosition());
			saveState(clone, param);

			// Replace current node's value with successor's
			current.setValue(successor.getValue());

			clone = this.clone();
			param = new Parameters();
			param.setObj(str);
			param.setInv(successor.getPosition());
			saveState(clone, param);

			saveState(this, new Parameters());

			// If successor had a right child, reattach it
			Node child = successor.getRight();
			if (parentOfSuccessor.getLeft() == successor) {
				parentOfSuccessor.setLeft(child);
			} else {
				parentOfSuccessor.setRight(child);
			}
			
			if (child != null) {
				child.setPosition(successor.getPosition());
				reassignPositions(child);
			}
		}
	}

	private void reassignPositions(Node node) {
		if (node == null) return;

		if (node.getLeft() != null) {
			node.getLeft().setPosition(node.getPosition() + "L");
			reassignPositions(node.getLeft());
		}

		if (node.getRight() != null) {
			node.getRight().setPosition(node.getPosition() + "R");
			reassignPositions(node.getRight());
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

		Node clone = new Node(original.getValue(), leftCopy, rightCopy);
		clone.setPosition(original.getPosition());

		return clone;
	}


	@Override
	public String info() {
		String str = "";
		return str;
	}

	private class Node implements INode<T> {
		private T value;
		private Node leftChild;
		private Node rightChild;

		private String position; // For animation

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

		public String getPosition() {
			return position;
		}

		public void setPosition(String position) {
			this.position = position;
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
		} else {
			double nodeX = paneWidth / 2;
			double nodeY = 50;
			double nodeRadius = 20;

			Circle circle = new Circle(nodeX, nodeY, nodeRadius);
			circle.setFill(Color.LIGHTGRAY);
			circle.setStroke(Color.BLACK);

			Text text = new Text("NULL");
			text.setFill(Color.BLACK);
			text.setFont(javafx.scene.text.Font.font(14));

			// Center the text
			double textWidth = text.getBoundsInLocal().getWidth();
			double textHeight = text.getBoundsInLocal().getHeight();
			text.setX(nodeX - textWidth / 2);
			text.setY(nodeY + textHeight / 4); // adjust vertically

			pane.getChildren().addAll(circle, text);
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
		Color color = param.getColor(node.getPosition());
		circle.setFill(color);
		circle.setStroke(Color.BLACK);

		Text text = new Text(color == Color.TRANSPARENT ? "" : node.getValue().toString());
		text.setFont(javafx.scene.text.Font.font(14));

		// Center the text inside the circle
		double textWidth = text.getBoundsInLocal().getWidth();
		double textHeight = text.getBoundsInLocal().getHeight();
		text.setX(nodeX - textWidth / 2);
		text.setY(y + textHeight / 4); 

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
