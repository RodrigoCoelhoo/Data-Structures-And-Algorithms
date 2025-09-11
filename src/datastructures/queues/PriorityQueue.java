package datastructures.queues;

import java.lang.reflect.Parameter;
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
}