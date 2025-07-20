package algortihms;

import java.util.ArrayList;

public interface SortAlgorithm<T extends Comparable<T>> {

	public T[] sort(T[] input);
	public ArrayList<T> sort(ArrayList<T> input);
	public void info();
	public ArrayList<State> getStates();
}