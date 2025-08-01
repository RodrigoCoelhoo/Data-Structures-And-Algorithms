package algorithms.sort;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class MergeSort<T extends Comparable<T>> implements SortAlgorithm<T> {

	private final ArrayList<State> states = new ArrayList<>();

	@Override
	public T[] sort(T[] input) {
		if(input == null || input.length == 1) return input;
		
		int mid = input.length / 2;

		T[] left = Arrays.copyOfRange(input, 0, mid);
    	T[] right = Arrays.copyOfRange(input, mid, input.length);

		T[] leftSorted = sort(left);
    	T[] rightSorted = sort(right);

		return merge(leftSorted, rightSorted);
	}

	@SuppressWarnings("unchecked")
	private T[] merge(T[] left, T[] right) {
		T[] result = (T[]) Array.newInstance(left.getClass().getComponentType(), left.length + right.length);

		int i = 0, j = 0, k = 0;

		// Merge the arrays
		while (i < left.length && j < right.length) {
			if (left[i].compareTo(right[j]) <= 0) {
				result[k++] = left[i++];
			} else {
				result[k++] = right[j++];
			}
		}

		// Add remaining elements from left
		while (i < left.length) {
			result[k++] = left[i++];
		}

		// Add remaining elements from right
		while (j < right.length) {
			result[k++] = right[j++];
		}

		return result;
	}

	@Override
	public ArrayList<T> sort(ArrayList<T> input) {
		if(input == null || input.size() == 1) return input;

		return mergeSort(new ArrayList<>(input), 0, input.size());
	}

	public ArrayList<T> mergeSort(ArrayList<T> result, int start, int end) {
		if (end - start <= 1) return result;

		int mid = (start + end) / 2;

		List<Integer> indexs = new ArrayList<Integer>();
		for(int i = start; i < mid; i++) {
			indexs.add(i);
		}
		saveState(result, indexs, null);
		mergeSort(result, start, mid);
		
		indexs = new ArrayList<Integer>();
		for(int i = mid; i < end; i++) {
			indexs.add(i);
		}
		saveState(result, indexs, null);
		mergeSort(result, mid, end);
		
		List<Integer> highlight = new ArrayList<Integer>();
		for(int i = start; i < end; i++) {
			highlight.add(i);
		}
		saveState(result, null, highlight);

		merge(result, start, mid, end);

		if(start == 0 && end == result.size())
			saveState(result, null, highlight);

		return result;
	}


	private void merge(ArrayList<T> result, int start, int mid, int end) {
		ArrayList<T> merged = new ArrayList<>();
		int i = start, j = mid;

		while (i < mid && j < end) {
			if (result.get(i).compareTo(result.get(j)) <= 0) {
				merged.add(result.get(i++));
			} else {
				merged.add(result.get(j++));
			}
		}

		while (i < mid) {
			merged.add(result.get(i++));
		}

		while (j < end) {
			merged.add(result.get(j++));
		}

		// Copy merged content back into the original array
		ArrayList<Integer> highlight = new ArrayList<>();
		for (int k = 0; k < merged.size(); k++) {
			result.set(start + k, null);
		}
		saveState(result, null, null);
		
		for (int k = 0; k < merged.size(); k++) {
			result.set(start + k, merged.get(k));
			saveState(result, List.of(start + k), highlight);
			highlight.add(start + k);
		}
	}

	@Override
	public String info() {
		String result = "Time Complexity: O(n log n)\n";
		result += "Space Complexity: O(n)\n\n";
		result += "Here, 'n' is the number of elements to be sorted.\n\n";

		result += "Merge Sort is a comparison-based, divide-and-conquer sorting algorithm. "
				+ "It recursively splits the input array into two halves, sorts each half, and then merges the sorted halves to produce the final sorted array.\n\n";

		result += "The merge step ensures that the resulting array remains sorted by comparing elements from both halves and inserting them in order.\n\n";

		result += "Merge Sort is a stable sort, meaning that it preserves the relative order of equal elements.\n\n";

		result += "Note:\nWhile Merge Sort guarantees O(n log n) time complexity even in the worst case, it requires additional space proportional to the input size due to the temporary arrays used during merging.\n";

		return result;
	}

	@Override
	public ArrayList<State> getStates() {
		return new ArrayList<>(states);
	}

	@Override
	public void clearStates() {
		this.states.clear();
	}

	@Override
	public void saveState(ArrayList<T> input, List<Integer> indexs, List<Integer> highLight) 
	{
		boolean allNull = input.stream().allMatch(Objects::isNull);
		boolean condition = false;
		for(T element : input)
		{
			if(element instanceof Integer)
			{
				condition = true;
				break;
			}
		}

		if (!input.isEmpty() && (condition || allNull)) 
		{
			ArrayList<Integer> copy = new ArrayList<>();
			for (T element : input) {
				copy.add((element != null) ? (Integer) element : null);
			}
			states.add(new State(copy, indexs, highLight));
		} else {
			throw new UnsupportedOperationException("Only ArrayList<Integer> is supported by State.");
		}
	}
}
