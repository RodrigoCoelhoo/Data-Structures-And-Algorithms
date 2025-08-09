package algorithms.sort;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import algorithms.interfaces.ISortAlgorithm;
import utils.State;

public class BubbleSort<T extends Comparable<T>> implements ISortAlgorithm<T> {

	private final ArrayList<State> states = new ArrayList<>();
	
	@Override
	public T[] sort(T[] input) 
	{
		if (input == null || input.length <= 1) return input;

		T[] result = Arrays.copyOf(input, input.length);
			
		boolean swapped = true;
		int tailIndex= 1;
		while(swapped)
		{
			swapped = false;

			for(int i = 0; i < result.length - tailIndex ; i++)
			{
				if(result[i].compareTo(result[i+1]) > 0)
				{
					T temp = result[i];
					result[i] = result[i+1];
					result[i+1] = temp;
					swapped = true;
				}
			}
			tailIndex++;
		}
		return result;	
	}

	@Override
	public ArrayList<T> sort(ArrayList<T> input) 
	{
		if (input == null || input.size() <= 1) return input;

		ArrayList<T> result = new ArrayList<>(input);

		saveState(result, null, null);

		boolean swapped = true;
		int tailIndex = 1;
		ArrayList<Integer> highLight = new ArrayList<>();
		while (swapped) 
		{
			swapped = false;
			for (int i = 0; i < result.size() - tailIndex; i++) 
			{
				ArrayList<Integer> indexs = new ArrayList<>();
				indexs.add(i);
				indexs.add(i + 1);
				
				if (result.get(i).compareTo(result.get(i + 1)) > 0) 
				{
					T temp = result.get(i);
					result.set(i, result.get(i + 1));
					result.set(i + 1, temp);
					swapped = true;
				}

				saveState(result, indexs, highLight);
			}
			highLight.add(result.size() - tailIndex);
			tailIndex++;
		}
		return result;
	}


	@Override
	public String info() {
		String result = "Time Complexity: O(n^2)\n";
		result += "Space Complexity: O(1)\n\n";
		result += "'n' is the number of elements of the input.\n\n";
		result += "Bubble Sort repeatedly steps through the list, compares adjacent elements, and swaps them if they are in the wrong order. "
				+ "With each pass, the largest unsorted element \"bubbles up\" to its correct position at the end. "
				+ "The sorted portion grows from the end towards the beginning.\n\n";
		result += "This algorithm is simple to implement but inefficient for large datasets due to its quadratic time complexity.";
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
