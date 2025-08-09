package algorithms.interfaces;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import utils.State;

public interface ISearchAlgorithm {
    List<INode> solve(ILayout layout);
	public String info();

	/** Optional: Usefull for algorithm visualizer */
	default ArrayList<State> getStates() { return new ArrayList<>(); }
	default void clearStates() { }
	default void saveState(ILayout layout, Set<INode> openSet, Set<INode> closedSet) { }
}
