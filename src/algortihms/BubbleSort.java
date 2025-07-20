package algortihms;

import java.util.ArrayList;

public class BubbleSort<T extends Comparable<T>> implements SortAlgorithm<T> {

	private final ArrayList<State> states = new ArrayList<>();

	public ArrayList<State> getStates() {
		return this.states;
	}
	
	@Override
    public T[] sort(T[] input) 
	{
		if (input == null || input.length <= 1) return input;
			
		boolean swapped = true;
		int tailIndex= 1;
		while(swapped)
		{
			swapped = false;

			for(int i = 0; i < input.length - tailIndex ; i++)
			{
				if(input[i].compareTo(input[i+1]) > 0)
				{
					T temp = input[i];
					input[i] = input[i+1];
					input[i+1] = temp;
					swapped = true;
				}
			}
			tailIndex++;
		}
		return input;	
	}

	@Override
	public ArrayList<T> sort(ArrayList<T> input) 
	{
		if (input == null || input.size() <= 1) return input;

		saveState(input, null);

		boolean swapped = true;
		int tailIndex = 1;
		while (swapped) 
		{
			swapped = false;
			for (int i = 0; i < input.size() - tailIndex; i++) 
			{
				if (input.get(i).compareTo(input.get(i + 1)) > 0) 
				{
					T temp = input.get(i);
					input.set(i, input.get(i + 1));
					input.set(i + 1, temp);
					swapped = true;
				}
				saveState(input, new int[]{i, i + 1});
			}
			tailIndex++;
		}
		return input;
	}


	@Override
	public void info() 
	{
		System.out.println("==========================");
		System.out.println("Bubble Sort Algorithm");
		System.out.println("Time Complexity: O(n^2)");

		System.out.println();

		System.out.println("Description:");
		System.out.println("Repeatedly steps through the list, compares");
		System.out.println("adjacent elements and swaps them if they are");
		System.out.println("in the wrong order.");
		System.out.println("With each pass, the largest unsorted element");
		System.out.println("moves to its correct position at the end.");

		System.out.println("");

		System.out.println("The sorted portion builds up from the end.");
		System.out.println("==========================");
	}

	private void saveState(ArrayList<T> input, int[] indices) 
	{
		if (!input.isEmpty() && input.get(0) instanceof Integer) 
		{
			ArrayList<Integer> copy = new ArrayList<>();
			for (T element : input) {
				copy.add((Integer) element);
			}
			states.add(new State(copy, indices));
		} else {
			throw new UnsupportedOperationException("Only ArrayList<Integer> is supported by State.");
		}
	}
}
