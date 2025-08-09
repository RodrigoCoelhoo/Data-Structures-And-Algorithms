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

public class JPS implements ISearchAlgorithm {
	
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
		
		openQueue.add(start);
		openSet.add(start);
		
		while(!openQueue.isEmpty()) {
			// Check if the node is the target



			// For each direction of the grid check if there is a jumping point (When a forced neighbor is found)
			// Jumping Point:
			//
			// Ortogonal Directions
			// For the up, right, down, left, go in a straight line
			// If, in the current direction we find walls up and down for the right and left or find walls left and right for up and down
			// Check the cell next to the wall (in the current direction), if it is open we found a forced neighbor and the current cell is a jumping point

			// Example:
			// Wall OPEN <-- Forced Neighbor therefore, NODE is a jumpingPoint
			// NODE OPEN

			// Diagonal Directions
			// For the up-right, down-right, down-left, up-left we need to check the 3 directions for example:
			// Up-Right direction we need to check up, right and up-right (Check up and right -> Get the node in the up-right position, repeat until we find a forced neighbor or no other possibilities)

			// Example:
			// -------------------+ Searching in the Up-Right direction
			// OPEN FcNb ^^^^ OPEN| Here, was searched 2 nodes: PREV (UP-RIGHT of NODE) -> JUMP (Here was found a Forced Neighbor at FcNb)
			// WALL WALL JUMP OPEN| Found forced neighbor going up, no need to look for more -> Add JUMP to open list and return
			// XXXX PREV ---> --->| PREV search up, found a wall, search right, end of grid, get the up-right node and did the same
			// NODE XXXX XXXX XXXX| The only difference in diagonal directions is the "Get the up-right node" since we found FcNb going up

			// While checking, set the parents


			// After checking all the directions and adding all the jumping points to the open list, 
			// we will check the next Node (From the jumping points found) based on the < (F = G + H)
			// Repeat the cycle until a target is found or open list is empty
		}
		
		return path;
	}


	/**
	 * @return null if hit wall/end of grid, Node if it is a jumping point
	 */
	public INode findJumpingPoint(ILayout layout, INode node, int[] direction) {
		return null;
	}


	@Override
	public String info() {
		return "";
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
