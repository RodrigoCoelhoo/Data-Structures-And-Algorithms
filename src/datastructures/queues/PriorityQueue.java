package datastructures.queues;
import java.util.Comparator;

import utils.DataStructureState.Parameters;

public class PriorityQueue<T> extends Queue<T> {
	private Comparator<T> comparator;

    public PriorityQueue(Comparator<T> comparator) {
        this.comparator = comparator;
    }

	@Override
    public void enqueue(T value) {
        Node newNode = new Node(value, null);

        PriorityQueue<T> temp = this.clone();
        Parameters param = new Parameters();
        int count = -1;

        if (head == null || comparator.compare(value, head.getValue()) < 0) {
            newNode.setNext(head);
            head = newNode;
            if (tail == null) tail = newNode;
        } else {
            Node current = head;
            while (current.next() != null && comparator.compare(value, current.next().getValue()) >= 0) {
                current = current.next();

                count++;
                param = new Parameters();
                param.getIndexs().add(count);
		        saveState(temp, param);
            }

            param = new Parameters();
            param.getIndexs().add(count + 1);
            saveState(temp, param);

            newNode.setNext(current.next());
            current.setNext(newNode);
            
            if (newNode.next() == null) tail = newNode;
        }

        int index = count == -1 ? 0 : count + 2;
        
        param = new Parameters();
        param.getInvsible().add(index);
        saveState(this, param);

        param = new Parameters();
        param.setObjective(index);
        saveState(this, param);
        saveState(this, param);
        saveState(this, new Parameters());
        
        size++;
    }

    @Override
    public PriorityQueue<T> clone() {
        PriorityQueue<T> result = new PriorityQueue<>(comparator);
        
        for(T data : this) {
            result.enqueue(data);
        }
        
        return result;
    }

    @Override
    public String info() {
        String result = "A priority queue is an abstract data structure where each element is associated with a priority, "
                + "and elements are served based on their priority rather than their order of insertion. "
                + "By default, higher-priority elements are dequeued before lower-priority ones. "
                + "If two elements have the same priority, the order of arrival may determine which is served first.\n\n";

        result += "In this example the lower values have a bigger priority.";

        result += "'n' is the number of elements in the priority queue.\n\n";

        result += "Access Time Complexity: O(n)\n";
        result += "Search Time Complexity: O(n)\n";
        result += "Insert (Enqueue) Time Complexity: O(log n)\n";
        result += "Delete (Dequeue highest priority) Time Complexity: O(log n)\n";
        result += "Space Complexity: O(n)\n\n";

        return result;
    }
}