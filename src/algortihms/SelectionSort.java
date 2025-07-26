package algortihms;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SelectionSort<T extends Comparable<T>> implements SortAlgorithm<T> {

	private final ArrayList<State> states = new ArrayList<>();

	@Override
	public T[] sort(T[] input) {
		if (input == null || input.length <= 1) return input;

		T[] result = Arrays.copyOf(input, input.length);

		for(int i = 0; i < result.length - 1; i++) {
			int minIndex = i;

			for(int j = i + 1; j < result.length; j++) {
				if(result[j].compareTo(result[minIndex]) < 0)
					minIndex = j;
			}

			T temp = result[i];
			result[i] = result[minIndex];
			result[minIndex] = temp;
		}

		return result;
	}

	@Override
	public ArrayList<T> sort(ArrayList<T> input) {
		if (input == null || input.size() <= 1) return input;

		ArrayList<T> result = new ArrayList<>(input);

		saveState(result, null, null);

		ArrayList<Integer> highLight = new ArrayList<>();
		for(int i = 0; i < result.size() - 1; i++) {
			int minIndex = i;
			saveState(result, List.of(i), highLight);

			for(int j = i + 1; j < result.size(); j++) {
				if(result.get(j).compareTo(result.get(minIndex)) < 0)
					minIndex = j;
				
				saveState(result, List.of(i, j), highLight);
			}
			
			saveState(result, List.of(i, minIndex), highLight);

			T temp = result.get(i);
			result.set(i, result.get(minIndex));
			result.set(minIndex, temp);
			highLight.add(i);
		}

		return result;
	}

	@Override
	public String info() {
		String result = "Time Complexity: O(n²)\n";
		result += "Space Complexity: O(1)\n\n";
		result += "'n' is the number of elements in the input array.\n\n";

		result += "Selection Sort is a comparison-based sorting algorithm that works by repeatedly selecting the smallest element from the unsorted portion of the array and moving it to its correct position.\n\n";

		result += "On each pass through the array, the algorithm maintains two parts: a sorted section at the beginning and an unsorted section at the end. It searches the unsorted part for the minimum element and swaps it with the first element in the unsorted part.\n\n";

		result += "Selection Sort performs the same number of comparisons regardless of the input order (best, average, and worst cases are all O(n²)), but it makes at most n - 1 swaps, which can be beneficial when swap operations are costly.\n\n";

		result += "Note:\nSelection Sort is not stable by default — equal elements may be reordered. However, it is easy to implement and useful for small datasets or educational purposes.\n";

		return result;
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

	@Override
	public ArrayList<State> getStates() {
		return new ArrayList<>(states);
	}

	@Override
	public void clearStates() {
		this.states.clear();
	}
	
}
