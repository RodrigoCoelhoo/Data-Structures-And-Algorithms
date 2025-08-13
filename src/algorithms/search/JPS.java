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
import utils.Grid;
import utils.Grid.Cell;
import utils.State;

public class JPS implements ISearchAlgorithm {
	
	// Sets for visualization
	Set<INode> visualizationOpenSet = new HashSet<>();
	Set<INode> visualizationClosedSet = new HashSet<>();
	Set<INode> visualizationHighlightSet = new HashSet<>();
	private final ArrayList<State> states = new ArrayList<>();

	// https://www.youtube.com/watch?v=WVDnN4Cyj2A&ab_channel=GameAiUncovered
	// For the start node

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

	// After checking the direction(s) and adding all the jumping points to the open list, 
	// we will check the next Node (From the jumping points found) based on the < (F = G + H)
	
	// For the next nodes the search direction will be defined from the direction it was found (From parent) and forced neighbors of that node

	// Repeat the cycle until a target is found or open list is empty

	@Override
	public List<INode> solve(ILayout layout) {
		visualizationOpenSet.clear();
		visualizationClosedSet.clear();
		visualizationHighlightSet.clear();

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

		saveState(layout, visualizationOpenSet, visualizationClosedSet, visualizationHighlightSet);

		for(int[] direction : Grid.getDirections())
		{
			INode jumpingPoint = getJumpingPoint(layout, start, direction);
			visualizationOpenSet.clear();
			
			if(jumpingPoint != null) {
				openQueue.add(jumpingPoint);
				openSet.add(jumpingPoint);
				visualizationHighlightSet.add(jumpingPoint);
				saveState(layout, visualizationOpenSet, visualizationClosedSet, visualizationHighlightSet);
			}
		}
		closedSet.add(start);
		visualizationClosedSet.add(start);
		
		Cell[][] grid = ((Grid) layout).getGrid();
		while(!openQueue.isEmpty()) {
			// Check if the node is the target
			visualizationOpenSet.clear();
			
			INode current = openQueue.poll();
			visualizationClosedSet.add(current);
			if(layout.isGoal(current)) {
				path = getPath(current);
				break;
			}
			
			if(openSet.contains(current)) openSet.remove(current);
			if(visualizationHighlightSet.contains(current)) { 
				visualizationHighlightSet.remove(current);
			}
			saveState(layout, visualizationOpenSet, visualizationClosedSet, visualizationHighlightSet);
 
			// Check Direction from parent node
			List<int[]> directions = new ArrayList<>();
			directions.addAll(getDirection(current, current.getParent()));
			directions.addAll(getForcedNeighbors(grid, current, directions.get(0)));

			for(int[] direction : directions)
			{
				INode jumpingPoint = getJumpingPoint(layout, current, direction);
				visualizationOpenSet.clear();
				
				if(jumpingPoint != null) {
					openQueue.add(jumpingPoint);
					openSet.add(jumpingPoint);
					visualizationHighlightSet.add(jumpingPoint);
					saveState(layout, visualizationOpenSet, visualizationClosedSet, visualizationHighlightSet);
				}
			}
			closedSet.add(current);
		}
		
		return path;
	}

	
	/**
	 * @return null if hit wall/end of grid, Node if it is a jumping point
	 */
	private INode getJumpingPoint(ILayout layout, INode node, int[] direction) 
	{
		Grid gridLayout = (Grid) layout;
		Cell[][] grid = gridLayout.getGrid();
		
		if(direction[0] != 0 && direction[1] != 0) // Diagonal 
		{
			Cell currentNode = (Cell) node;
			
			while(currentNode != null) 
			{
				int nextRow = currentNode.getRow() + direction[0];
				int nextColumn = currentNode.getColumn() + direction[1];
				if(isOpenNode(grid, nextRow, nextColumn) == 1) {
					INode nextNode = grid[nextRow][nextColumn];
					nextNode.setParent(currentNode);
					nextNode.setG(currentNode.getG() + 1);
					currentNode = (Cell) nextNode;
				} else {
					return null;
				}

				if(!getForcedNeighbors(grid, currentNode, direction).isEmpty()) {
                    return currentNode;
                }
				
				visualizationOpenSet.add(currentNode);
				saveState(layout, visualizationOpenSet, visualizationClosedSet, visualizationHighlightSet);
				
				if(layout.isGoal(currentNode)) return currentNode;
				
				int[] direction1 = {direction[0], 0};
				int[] direction2 = {0, direction[1]};
				
				INode result = sweep(grid, currentNode, direction1);
				saveState(layout, visualizationOpenSet, visualizationClosedSet, visualizationHighlightSet);
				if(result != null) return currentNode;
				
				result = sweep(grid, currentNode, direction2);
				saveState(layout, visualizationOpenSet, visualizationClosedSet, visualizationHighlightSet);
				if(result != null) return currentNode;
			}
			
			return null;
		} 
		else // Ortogonal 
		{
			INode result = sweep(grid, node, direction);
			saveState(layout, visualizationOpenSet, visualizationClosedSet, visualizationHighlightSet);
			visualizationOpenSet.clear();
			return result;
		}
	}
	
	
	/**
	 * @return null if hit wall/end of grid, Node if it is a jumping point
	 */
	private INode sweep(Cell[][] grid, INode node, int[] direction) 
	{
		// direction e.g. --> (row, column) (-1, 0) -> Same column, previous row (Up)
		Cell currentNode = (Cell) node;
		
		int nextRow = currentNode.getRow();
		int nextColumn = currentNode.getColumn();
		while(currentNode != null) 
		{
			nextRow = nextRow + direction[0];
			nextColumn = nextColumn + direction[1];
			
			if(isOpenNode(grid, nextRow, nextColumn) == 1) 
			{
				Cell nextNode = grid[nextRow][nextColumn];
				nextNode.setParent(currentNode);
				nextNode.setG(currentNode.getG() + 1);
				currentNode = nextNode;
				visualizationOpenSet.add(currentNode);
				
				if(currentNode.isObjective()) {
					visualizationOpenSet.remove(node);
					visualizationHighlightSet.add(node);
					return currentNode;
				}
				
				if(!getForcedNeighbors(grid, currentNode, direction).isEmpty()) {
					visualizationOpenSet.remove(node);
					visualizationHighlightSet.add(node);
					return currentNode;
				} 	
			} 
			else {
				return null;
			}
		}
		return null;
	}
	
	private List<int[]> getForcedNeighbors(Cell[][] grid, INode node, int[] dir) {
		List<int[]> forcedDirs = new ArrayList<>();
		int r = ((Cell) node).getRow();
		int c = ((Cell) node).getColumn();
		
		int dr = dir[0];
		int dc = dir[1];
		
		// Moving vertically
		if (dr != 0 && dc == 0) {
			// Check left side
			if (isOpenNode(grid, r, c - 1) == -1 && isOpenNode(grid, r + dr, c - 1) == 1)
				forcedDirs.add(new int[]{dr, -1});
			// Check right side
			if (isOpenNode(grid, r, c + 1) == -1 && isOpenNode(grid, r + dr, c + 1) == 1)
				forcedDirs.add(new int[]{dr, 1});
		}

		// Moving horizontally
		else if (dc != 0 && dr == 0) {
			// Check up
			if (isOpenNode(grid, r - 1, c) == -1 && isOpenNode(grid, r - 1, c + dc) == 1)
				forcedDirs.add(new int[]{-1, dc});
			// Check down
			if (isOpenNode(grid, r + 1, c) == -1 && isOpenNode(grid, r + 1, c + dc) == 1)
				forcedDirs.add(new int[]{1, dc});
		}
		else if (dr != 0 && dc != 0) { // Diagonal movement

			// CURRENT ROW + DIR[0], CURRENT COLUMN - DIR[1]
			// R, C - DC
			/**
			 * (0,0) (0,1) FCNGB (0,3)
			 * (1,0) WALLL WALLL NNNNN	(1-2, 3 - 2) -> (-1, 1)			| 		
			 * (2,0) (2,1) PPPPP (2,3)	FCNGB = (0,2), NNNNN = (1,3)	| WALLL = (1,2) NNNNN = (1,3)			
			 * (3,0) (3,1) (3,2) (3,3)  (1 + (-1), 3 - 1)				| R, C - DC
			 */

			// CURRENT ROW - DIR[0], CURRENT COLUMN + DIR[1]
			/**
			 * (0,0) (0,1) (0,2) (0,3) (0,4)
			 * (1,0) WALLL WALLL NNNNN (1,4)	(1-2, 3 - 2) -> (-1, 1)			| (-1, 1)				
			 * (2,0) (2,1) PPPPP WALLL FCNGB	FCNGB = (2,4), NNNNN = (1,3)	| WALLL = (2,3) NNNNN = (1,3)			
			 * (3,0) (3,1) (3,2) (3,3) (3,4)    (1 - (-1), 3 + 1)				| R - DR, C
			 */

			if (isOpenNode(grid, r, c - dc) == -1 && isOpenNode(grid, r + dr, c - dc) == 1)
				forcedDirs.add(new int[]{dr, -dc});

			if (isOpenNode(grid, r - dr, c) == -1 && isOpenNode(grid, r - dr, c + dc) == 1)
				forcedDirs.add(new int[]{-dr, dc}); 
		}

		return forcedDirs;
	}

	/**
	 * @param current
	 * @return [0] row, [1] column
	 */
	private List<int[]> getDirection(INode current, INode previous) {
		List<int[]> result = new ArrayList<>();
		
		Cell currentCell = (Cell) current;
		int currentRow = currentCell.getRow();
		int currentColumn = currentCell.getColumn();
		
		Cell previousCell = (Cell) previous;
		int previousRow = previousCell.getRow();
		int previousColumn = previousCell.getColumn();
		
		int[] dir = {currentRow - previousRow, currentColumn - previousColumn};
		result.add(dir);

		if(dir[0] == 0 || dir[1] == 0)	
			return result;

		result.add(new int[]{dir[0], 0});
		result.add(new int[]{0, dir[1]});
		return result;
	}

	/**
	 * Auxiliary Funciton for JPS
	 * @return -1 if is not Open, 0 if outside boundaries, 1 if is open
	 */
	private int isOpenNode(Cell[][] grid, int row, int column) {
		int rows = grid.length;
		int columns = grid[0].length;

		if((row < 0 || row >= rows) || (column < 0 || column >= columns)) {
			return 0; // Outside boundaries
		}

		Cell node = grid[row][column];
		if(node.isWall()) return -1; // There's a wall, not open
		
		return 1;
	}


	@Override
	public String info() {
		return "Jump Point Search (JPS) is an optimization of the A* algorithm for uniform-cost grids. " +
			"It reduces the number of nodes explored by 'jumping' over nodes that do not affect the " +
			"optimal path, only considering nodes where a decision (forced neighbor) is required. " +
			"JPS works for both orthogonal and diagonal movement, maintaining the optimality of A* " +
			"while significantly improving performance on large grids.";
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
