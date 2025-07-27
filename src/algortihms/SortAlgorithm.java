package algortihms;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public interface SortAlgorithm<T extends Comparable<T>> {
	public T[] sort(T[] input);
	public ArrayList<T> sort(ArrayList<T> input);
	public String info();

	/** Optional: Usefull for algorithm visualizer */
	default ArrayList<State> getStates() { return new ArrayList<>(); }
	default void clearStates() { }
	default void saveState(ArrayList<T> input, List<Integer> indexs, List<Integer> highLight) { }

	/** Optional: for configurable algorithms (e.g., BucketSort) */
	default void setParameters(Map<String, Object> config) { }
	default List<String> requiredParameters() { return List.of(); }
}