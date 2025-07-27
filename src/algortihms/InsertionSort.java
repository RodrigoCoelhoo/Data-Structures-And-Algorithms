package algortihms;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class InsertionSort<T extends Comparable<T>> implements SortAlgorithm<T> {

	private final ArrayList<State> states = new ArrayList<>();

	@Override
	public T[] sort(T[] input) {
		if (input == null || input.length <= 1) return input;

		T[] result = Arrays.copyOf(input, input.length);
		
		for(int i = 1; i < result.length; i++)
		{
			T key = result[i];
			result[i] = null;
			int j = i - 1;

			while (j >= 0 && key.compareTo(result[j]) < 0) {
				result[j + 1] = result[j];
				j--;
			}

			result[j+1] = key;
		}
		
		return result;
	}

	@Override
	public ArrayList<T> sort(ArrayList<T> input) {
		if (input == null || input.size() <= 1) return input;

		ArrayList<T> result = new ArrayList<>(input);
		saveState(result, null, null);

		for(int i = 1; i < result.size(); i++)
		{
			ArrayList<Integer> highLight = new ArrayList<>();
			T key = result.get(i);
			result.set(i, null);
			int j = i - 1;
			saveState(result, List.of(i), List.of(i));

			while (j >= 0 && key.compareTo(result.get(j)) < 0) {
				saveState(result, List.of(j), highLight);
				result.set(j + 1, result.get(j));
				highLight.add(j+1);
				j--;
			}

			result.set(j+1, null);

			saveState(result, null, highLight);

			result.set(j+1, key);
			saveState(result, List.of(j+1), highLight);
		}

		return result;
	}

	@Override
	public String info() {
		String result = "Time Complexity: O(n²)\n";
		result += "Space Complexity: O(1)\n\n";
		result += "'n' is the number of elements in the input array.\n\n";

		result += "Insertion Sort is a comparison-based sorting algorithm that builds the final sorted array one element at a time.\n";
		result += "At each step, it removes the current element (the 'key') from the array, then shifts larger elements one position to the right while searching for the correct position to insert the key.\n\n";

		result += "This creates a temporary \"empty\" slot in the array where the key will eventually be inserted.\n";
		result += "The process is repeated for each element until the array is fully sorted.\n\n";

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
		boolean condition = false;
		for(T element : input)
		{
			if(element instanceof Integer)
			{
				condition = true;
				break;
			}
		}

		if (!input.isEmpty() && condition) 
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
