package algorithms.search;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Set;

import algorithms.interfaces.ILayout;
import algorithms.interfaces.INode;
import algorithms.interfaces.ISearchAlgorithm;
import utils.State;

/**
 * Best-First Search
 */
public class Greedy implements ISearchAlgorithm{
	
	private final ArrayList<State> states = new ArrayList<>();
	
	@Override
	public List<INode> solve(ILayout layout) {
		List<INode> path = new ArrayList<>();
		PriorityQueue<INode> openQueue = new PriorityQueue<>(Comparator.comparingInt(INode::getH));
		Set<INode> openSet = new HashSet<>();
    	Set<INode> closedSet = new HashSet<>();
		
		INode start = layout.getInitialNode();
		if(layout.isGoal(start)) { 
			path.add(start);
			return path;
		}
		
		start.setParent(null);
		openQueue.add(start);
		openSet.add(start);
		
		while (!openQueue.isEmpty()) {
			
			INode current = openQueue.poll();
			
			if(openSet.contains(current)) openSet.remove(current);


			if(layout.isGoal(current)) {
				path = getPath(current);
				break;
			}
			
			closedSet.add(current);
			saveState(layout, openSet, closedSet, null);
			
			ArrayList<INode> currentSuccessors = (ArrayList<INode>) layout.getSuccessors(current);
			for(INode successor : currentSuccessors)
			{
				if(closedSet.contains(successor) || openSet.contains(successor)) continue;
				
				successor.setParent(current);
				openQueue.add(successor);
				openSet.add(successor);
			}
			saveState(layout, openSet, closedSet, null);
		}
		
		return path;  
	}

	@Override
	public String info() {
		String result = "Greedy Best-First Search is an informed search algorithm that uses a heuristic function 'h(n)' to guide the search. "
				+ "At each step, it selects the node that appears to be closest to the goal according to the heuristic.\n\n";

		result += "The algorithm uses a priority queue to always expand the node with the lowest heuristic value first. "
				+ "It maintains an open set (nodes to be explored) and a closed set (nodes already explored) to avoid revisiting.\n\n";

		result += "Unlike A*, Greedy Best-First Search does not consider the cost to reach a node (g(n)), only the estimated cost to the goal (h(n)). "
				+ "This can make it faster, but it is not guaranteed to find the shortest path and may get stuck in loops if the heuristic is misleading.\n\n";

		result += "Note:\n"
				+ "Its memory consumption can be high because it stores all generated nodes in the open set.\n";

		return result;
	}

	private List<INode> getPath(INode node) {
		ArrayList<INode> result = new ArrayList<>();

		INode current = node;
		result.add(current);
		while(current.getParent() != null) {
			current = current.getParent();
			result.add(current);
		}

		Collections.reverse(result);
		return result;
	}


	@Override
	public ArrayList<State> getStates() {
		return states;
	}

	@Override
	public void clearStates() {
		this.states.clear();
	}

	@Override
	public void saveState(ILayout layout, Set<INode> openSet, Set<INode> closedSet, Set<INode> highlight) 
	{
		states.add(new State(layout, openSet, closedSet, highlight));
	}
}
