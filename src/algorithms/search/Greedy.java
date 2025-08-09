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
			saveState(layout, openSet, closedSet);
			
			ArrayList<INode> currentSuccessors = (ArrayList<INode>) layout.getSuccessors(current);
			for(INode successor : currentSuccessors)
			{
				if(closedSet.contains(successor) || openSet.contains(successor)) continue;
				
				successor.setParent(current);
				openQueue.add(successor);
				openSet.add(successor);
			}
			saveState(layout, openSet, closedSet);
		}
		
		return path;  
	}

	@Override
	public String info() {
		return "Teste";
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
	public void saveState(ILayout layout, Set<INode> openSet, Set<INode> closedSet) 
	{
		states.add(new State(layout, openSet, closedSet));
	}
}
