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

public class AStar implements ISearchAlgorithm {
	
	private final ArrayList<State> states = new ArrayList<>();
	
	@Override
	public List<INode> solve(ILayout layout) {
		List<INode> path = new ArrayList<>();
		PriorityQueue<INode> openQueue = new PriorityQueue<>(Comparator.comparingInt(INode::getF));
		Set<INode> openSet = new HashSet<>();
    	Set<INode> closedSet = new HashSet<>();
		
		INode start = layout.getInitialNode();
		if(layout.isGoal(start)) { 
			path.add(start);
			return path;
		}
		
		start.setParent(null);
		start.setG(0);
		openQueue.add(start);
		openSet.add(start);
		
		while (!openQueue.isEmpty()) {
			
			INode current = openQueue.poll();
			if(current != layout.getInitialNode())
				saveState(layout, openSet, closedSet, null);
			
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

	@Override
	public String info() {
		String result = 
			"A* (A-Star) is a best-first search algorithm that finds the shortest path from a start node to a goal node. "
				+ "It uses a heuristic function 'h(n)' to estimate the cost from a node to the goal, "
				+ "and a path cost function 'g(n)' to track the cost from the start node to the current node. "
				+ "The sum f(n) = g(n) + h(n) determines the priority of nodes for exploration.\n\n";

		result += "Nodes are expanded based on the lowest f(n) value, ensuring that the algorithm always considers the most promising paths first. "
				+ "If a node is revisited with a lower g(n), its path and f(n) are updated to reflect the better path.\n\n";

		result += "A* guarantees an optimal solution if the heuristic 'h(n)' is admissible, "
				+ "meaning it never overestimates the actual cost to reach the goal.\n\n";

		result += "Note:\nA* maintains both open and closed sets. "
				+ "The open set stores nodes to be explored, ordered by f(n), while the closed set keeps track of already expanded nodes to avoid redundant work. "
				+ "This ensures efficiency but requires memory proportional to the number of nodes.\n";

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
