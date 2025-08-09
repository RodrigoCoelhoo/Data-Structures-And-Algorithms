package algorithms.sort;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import algorithms.interfaces.ISortAlgorithm;
import utils.State;

public class BucketSort<T extends Comparable<T>> implements ISortAlgorithm<T> {

	private final ArrayList<State> states = new ArrayList<>();
	private int bucketCount = 10; // Default number of buckets

	@Override
	public void setParameters(Map<String, Object> config) {
		if (config.containsKey("bucketCount")) {
			int count = (int) config.get("bucketCount");
			if (count > 0) {
				this.bucketCount = count;
			}
		}
	}

	@Override
	public List<String> requiredParameters() {
		return List.of("bucketCount");
	}

	@Override
	public T[] sort(T[] input) {
		if (input == null || input.length <= 0) return input;
		
		if(!(input[0] instanceof Number)) throw new IllegalArgumentException("Bucket sort requires numeric input.");
		
		// Find min-max
		T min = input[0], max = input[0];
		for (int i = 1; i < input.length; i++) {
			if (input[i].compareTo(min) < 0) {
				min = input[i];
			} else if (input[i].compareTo(max) > 0) {
				max = input[i];
			}
		}

		double minValue = toDouble(min);
		double maxValue = toDouble(max);

		ArrayList<ArrayList<T>> buckets = new ArrayList<>();
		for(int i = 0; i < input.length; i++) 
		{
			ArrayList<T> bucket = new ArrayList<>();
			buckets.add(bucket);
		}

		for(T i : input)
		{
			int bucketIndex = (int) ((double) (toDouble(i) - minValue) / (maxValue - minValue + 1) * input.length);
			buckets.get(bucketIndex).add(i);
		}

		for(ArrayList<T> bucket : buckets)
		{
			// Insertion Sort
			for(int i = 1; i < bucket.size(); i++)
			{
				T key = bucket.get(i);
				int j = i - 1;

				while (j >= 0 && key.compareTo(bucket.get(j)) < 0) {
					bucket.set(j + 1, bucket.get(j));
					j--;
				}

				bucket.set(j+1, key);
			}
		}

		return mergeBuckets(buckets).toArray(Arrays.copyOf(input, input.length));
	}

	@Override
	public ArrayList<T> sort(ArrayList<T> input) {
		if (input == null || input.size() <= 0) return input;

		if(!(input.get(0) instanceof Number)) throw new IllegalArgumentException("Bucket sort requires numeric input.");

		// Find min-max
		T min = input.get(0), max = input.get(0);
		for (int i = 1; i < input.size(); i++) {
			if (input.get(i).compareTo(min) < 0) {
				min = input.get(i);
			} else if (input.get(i).compareTo(max) > 0) {
				max = input.get(i);
			}
		}

		double minValue = toDouble(min);
    	double maxValue = toDouble(max);

		// Create buckets
		ArrayList<ArrayList<T>> buckets = new ArrayList<>();
		for(int i = 0; i < this.bucketCount; i++) 
		{
			ArrayList<T> bucket = new ArrayList<>();
			buckets.add(bucket);
		}

		// Populate buckets
		for(T i : input)
		{
			int bucketIndex = (int) ((double) (toDouble(i) - minValue) / (maxValue - minValue + 1) * this.bucketCount);
			buckets.get(bucketIndex).add(i);
			ArrayList<T> list = mergeBuckets(new ArrayList<>(buckets));
			saveState(list, null, null);
		}

		// Sort buckets
		int count = 0;
		for(ArrayList<T> bucket : buckets)
		{
			ArrayList<Integer> highLight = new ArrayList<>();
			for(int i = 0; i < bucket.size(); i++)
			{
				highLight.add(count++);
			}

			// Insertion Sort
			for(int i = 1; i < bucket.size(); i++)
			{
				T key = bucket.get(i);
				int j = i - 1;

				ArrayList<T> b = mergeBuckets(buckets);
				int temp = count - bucket.size();

				while (j >= 0 && key.compareTo(bucket.get(j)) < 0) {
					bucket.set(j + 1, bucket.get(j));
					saveState(b, List.of(i + temp, j + temp), highLight);
					j--;
				}

				bucket.set(j+1, key);
				saveState(mergeBuckets(buckets), List.of(j+1 + temp), highLight);
			}
		}

		return mergeBuckets(buckets);
	}

	private ArrayList<T> mergeBuckets(ArrayList<ArrayList<T>> buckets) {
		ArrayList<T> result = new ArrayList<>();
		for(ArrayList<T> bucket : buckets)
		{
			for(T item : bucket)
				result.add(item);
		}

		return new ArrayList<>(result);
	}

	// Helper method to convert T to double safely
	private double toDouble(T value) {
		if (value instanceof Number) {
			return ((Number) value).doubleValue();
		}
		throw new IllegalArgumentException("BucketSort supports only numeric types.");
	}

	@Override
	public String info() {
		String result = "Time Complexity: O(n + k)\n";
		result += "Space Complexity: O(n + k)\n\n";
		result += "Here, 'n' is the number of elements to be sorted, and 'k' is the number of buckets used.\n\n";

		result += "Bucket Sort is a non-comparison-based sorting algorithm that distributes elements into buckets. "
				+ "Each bucket contains a range of values, and elements are placed into buckets based on a computed index (typically derived from their value).\n\n";

		result += "Each bucket is then sorted individually using a simpler algorithm, such as Insertion Sort. At the end, the buckets are concatenated to form the final sorted array.\n\n";

		result += "Note:\nThis implementation uses a fixed number of buckets (configurable), but in some cases, the number of buckets is chosen to match the size of the input array to improve distribution.\n\n";

		result += "For Bucket Sort to work, the elements being sorted must be mappable to a numeric range — meaning they must be convertible to a number that determines which bucket they belong to. ";

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
