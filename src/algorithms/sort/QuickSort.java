package algorithms.sort;

import java.util.ArrayList;
import java.util.List;

public class QuickSort<T extends Comparable<T>> implements SortAlgorithm<T> {

	private final ArrayList<State> states = new ArrayList<>();

	@Override
	public T[] sort(T[] input) {
		if(input == null || input.length == 1) return input;

		return quickSort(input, 0, input.length - 1);
	}

	private T[] quickSort(T[] input, int start, int end)
	{
		if (end <= start) { return input; }

		T pivot = input[end];
		int i = start - 1;

		for (int j = start; j < end; j++) {
			if (input[j].compareTo(pivot) <= 0) {
				i++;
				swap(input, i, j);
			}
		}

		int pivotIndex = i + 1;
		swap(input, pivotIndex, end);

		quickSort(input, start, pivotIndex - 1);
		quickSort(input, pivotIndex + 1, end);

		return input;
	}

	private void swap(T[] input, int i, int j) {
		T temp = input[i];
		input[i] = input[j];
		input[j] = temp;
	}

	@Override
	public ArrayList<T> sort(ArrayList<T> input) {
		if(input == null || input.size() == 1) return input;
		return quickSort(input, 0, input.size() - 1);
	}

	private ArrayList<T> quickSort(ArrayList<T> input, int start, int end)
	{
		if (end <= start) return input;

		T pivot = input.get(end);
		int i = start - 1;

		saveState(input, List.of(end), null);

		for (int j = start; j < end; j++) 
		{
			List<Integer> indexList = new ArrayList<>(List.of(j));
			if (i >= start) indexList.add(i);
			saveState(input, indexList, List.of(end));

			if (input.get(j).compareTo(pivot) <= 0) {
				i++;
				if (i != j) {
					saveState(input, List.of(i, j), List.of(end));
					saveState(input, null, List.of(i, j, end));
					swap(input, i, j);
					saveState(input, null, List.of(i, j, end));
				}
			}
		}

		int pivotIndex = i + 1;

		if (pivotIndex != end) {
			saveState(input, List.of(end, i), null);
			saveState(input, List.of(end, pivotIndex), null);
			swap(input, pivotIndex, end);
			saveState(input, null, List.of(pivotIndex, end));
			saveState(input, null, List.of(pivotIndex));
		}

		quickSort(input, start, pivotIndex - 1);
		quickSort(input, pivotIndex + 1, end);

		return input;
	}



	private void swap(ArrayList<T> input, int i, int j) {
		T temp = input.get(i);
		input.set(i, input.get(j));
		input.set(j, temp);
	}

	@Override
	public String info() {
		String result = "Time Complexity: O(n log n) on average, O(n²) in the worst case\n";
		result += "Space Complexity: O(log n)\n\n";
		result += "Here, 'n' is the number of elements to be sorted.\n\n";

		result += "Quick Sort is a comparison-based, divide-and-conquer sorting algorithm. "
				+ "It selects a 'pivot' element, partitions the array into elements less than or equal to the pivot and those greater than the pivot, "
				+ "then recursively applies the same process to the sub-arrays.\n\n";

		result += "Partitioning ensures that the pivot ends up in its final sorted position, and no extra space is used for merging.\n\n";

		result += "https://www.youtube.com/watch?v=WprjBK0p6rw&ab_channel=CuriousWalk";

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
