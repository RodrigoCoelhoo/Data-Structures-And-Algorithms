package algorithms.search;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Set;

import utils.State;

public class AStar implements SearchAlgorithm {
	
	private final ArrayList<State> states = new ArrayList<>();

	@Override
	public List<INode> solve(ILayout layout) {
		List<INode> path = new ArrayList<>();
		PriorityQueue<INode> openQueue = new PriorityQueue<>(Comparator.comparingInt(INode::getF));
		Set<INode> openSet = new HashSet<>();
    	Set<INode> closedSet = new HashSet<>();

		INode start = layout.getInitialNode();
		if(layout.isGoal(start)) return path;

		start.setParent(null);
		start.setG(0);
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

			ArrayList<INode> currentSuccessors = (ArrayList<INode>) layout.getSuccessors(current);
			for(INode successor : currentSuccessors)
			{
				if(closedSet.contains(successor)) continue;
				
				int newG = current.getG() + 1;

				if(!openSet.contains(successor)) {
					successor.setParent(current);
					successor.setG(newG);

					openQueue.add(successor);
					openSet.add(successor);
				} else if (newG < successor.getG()) {
					// Better parent (Path) found
					successor.setParent(current);
					successor.setG(newG);
					
					// Re-add to priority queue to reorder
					openQueue.remove(successor);
					openQueue.add(successor);
				}
			}
		}

		return path;  
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
		return new ArrayList<>(states);
	}

	@Override
	public void clearStates() {
		this.states.clear();
	}

	@Override
	public void saveState(ILayout layout, Set<INode> openSet, Set<INode> closedSet) 
	{
		states.add(new State(layout, openSet, closedSet));
	}
}
