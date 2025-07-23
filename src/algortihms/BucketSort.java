package algortihms;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class BucketSort<T extends Comparable<T>> implements SortAlgorithm<T> {

	private final ArrayList<State> states = new ArrayList<>();
	private int bucketCount = 10; // Default number of buckets

	public void setParameters(Map<String, Object> config) {
		if (config.containsKey("bucketCount")) {
			int count = (int) config.get("bucketCount");
			if (count > 0) {
				this.bucketCount = count;
			}
		}
	}

	@Override
	public T[] sort(T[] input) {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException("Unimplemented method 'sort'");
	}

	@Override
	public ArrayList<T> sort(ArrayList<T> input) {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException("Unimplemented method 'sort'");
	}

	@Override
	public String info() {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException("Unimplemented method 'info'");
	}

	@Override
	public ArrayList<State> getStates() {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException("Unimplemented method 'getStates'");
	}

	@Override
	public void clearStates() {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException("Unimplemented method 'clearStates'");
	}

	@Override
	public void saveState(ArrayList<T> input, List<Integer> indexs, List<Integer> highLight) 
	{
		if (!input.isEmpty() && input.get(0) instanceof Integer) 
		{
			ArrayList<Integer> copy = new ArrayList<>();
			for (T element : input) {
				copy.add((Integer) element);
			}
			states.add(new State(copy, indexs, highLight));
		} else {
			throw new UnsupportedOperationException("Only ArrayList<Integer> is supported by State.");
		}
	}
}
