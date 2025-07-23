package algortihms;

import java.util.ArrayList;
import java.util.List;

public class BubbleSort<T extends Comparable<T>> implements SortAlgorithm<T> {

	private final ArrayList<State> states = new ArrayList<>();
	
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

		saveState(input, null, null);

		boolean swapped = true;
		int tailIndex = 1;
		ArrayList<Integer> highLight = new ArrayList<>();
		while (swapped) 
		{
			swapped = false;
			for (int i = 0; i < input.size() - tailIndex; i++) 
			{
				ArrayList<Integer> indexs = new ArrayList<>();
				indexs.add(i);
				indexs.add(i + 1);
				
				if (input.get(i).compareTo(input.get(i + 1)) > 0) 
				{
					T temp = input.get(i);
					input.set(i, input.get(i + 1));
					input.set(i + 1, temp);
					swapped = true;
				}

				saveState(input, indexs, highLight);
			}
			highLight.add(input.size() - tailIndex);
			tailIndex++;
		}
		return input;
	}


	@Override
	public String info() 
	{
		String result = "Time Complexity: O(n^2)\n\n";
		result += "Repeatedly steps through the list, compares adjacent elements and swaps them if they are in the wrong order. With each pass, the largest unsorted element moves to its correct position at the end. The sorted portion builds up from the end.";
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
		return this.states;
	}

	@Override
	public void clearStates() {
		this.states.clear();
	}
}
