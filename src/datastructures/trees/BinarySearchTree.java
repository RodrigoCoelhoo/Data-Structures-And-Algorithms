package datastructures.trees;

import datastructures.interfaces.INode;
import datastructures.interfaces.IDataStructure;

public class BinarySearchTree<T> implements IDataStructure<T> {
	Node root = null;

	private class Node<T> implements INode<T> {
		T value;
		Node leftChild;
		Node rightChild;

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
}
