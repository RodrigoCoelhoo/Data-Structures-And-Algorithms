package utils;

import java.util.Set;

import algorithms.search.Grid;
import algorithms.search.Grid.Cell;
import algorithms.search.ILayout;
import algorithms.search.INode;

public class GridSnapshot {
	static final int EMPTY = 0;
	static final int WALL = 1;
	static final int OPEN = 2;
	static final int CLOSED = 3;
	static final int START = 4;
	static final int GOAL = 5;
	//static final int PATH = 6;

	private final int[][] snapshot;

	public GridSnapshot(ILayout layout, Set<INode> openSet, Set<INode> closedSet) {
		Cell[][] grid = ((Grid) layout).getGrid();

		this.snapshot = new int[grid.length][grid[0].length];

		for(int row = 0 ; row < snapshot.length; row++) 
		{
			for(int column = 0; column < snapshot[0].length ; column++) 
			{
				Cell current = grid[row][column];
				this.snapshot[row][column] = getType(current, openSet, closedSet);
			}
		}
	}

	private int getType(Cell current, Set<INode> openSet, Set<INode> closedSet) {
		if(current.isObjective()) return GOAL;
		if(current.isStart()) return START;
		if(current.isWall()) return WALL;
		if(openSet.contains(current)) return OPEN;
		if(closedSet.contains(current)) return CLOSED;
		
		return EMPTY;
	}
}
