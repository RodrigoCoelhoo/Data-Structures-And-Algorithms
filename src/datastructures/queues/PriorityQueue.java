package datastructures.queues;

import java.util.Comparator;

public class PriorityQueue<T> extends Queue<T> {
	private Comparator<T> comparator;

    public PriorityQueue(Comparator<T> comparator) {
        this.comparator = comparator;
    }

	@Override
    public void enqueue(T value) {
        Node newNode = new Node(value, null);

        if (head == null || comparator.compare(value, head.getValue()) < 0) {
            newNode.setNext(head);
            head = newNode;
            if (tail == null) tail = newNode;
        } else {
            Node current = head;
            while (current.next() != null && comparator.compare(value, current.next().getValue()) >= 0) {
                current = current.next();
            }
            newNode.setNext(current.next());
            current.setNext(newNode);
            if (newNode.next() == null) tail = newNode;
        }

        size++;
    }
}
