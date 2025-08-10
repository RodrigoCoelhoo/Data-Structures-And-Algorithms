package utils;

import java.util.Set;

import algorithms.interfaces.ILayout;
import algorithms.interfaces.INode;
import javafx.scene.paint.Color;
import utils.Grid.Cell;

public class GridSnapshot {
	static final int EMPTY = 0;
	static final int WALL = 1;
	static final int OPEN = 2;
	static final int CLOSED = 3;
	static final int START = 4;
	static final int GOAL = 5;
	static final int PATH = 6;
	static final int HIGHLIGHT = 7;

	static final Color EMPTY_COLOR 	= Color.LIGHTGRAY;
    static final Color OPEN_COLOR 		= Color.web("#90CAF9");       
    static final Color CLOSED_COLOR 	= Color.web("#1976D2");
	static final Color WALL_COLOR 		= Color.RED;
	static final Color GOAL_COLOR		= Color.YELLOW;
	static final Color START_COLOR		= Color.web("#66BB6A");
	static final Color PATH_COLOR		= Color.ORANGE;
	static final Color HIGHLIGHT_COLOR  = Color.AQUAMARINE;

	private final int[][] snapshot;

	public GridSnapshot(ILayout layout, Set<INode> openSet, Set<INode> closedSet, Set<INode> highLight) {
		Cell[][] grid = ((Grid) layout).getGrid();

		this.snapshot = new int[grid.length][grid[0].length];

		for(int row = 0 ; row < snapshot.length; row++) 
		{
			for(int column = 0; column < snapshot[0].length ; column++) 
			{
				Cell current = grid[row][column];
				this.snapshot[row][column] = getType(current, openSet, closedSet, highLight);
			}
		}
	}

	public GridSnapshot(int[][] snapshot) {
		this.snapshot = snapshot;
	}

	private int getType(Cell current, Set<INode> openSet, Set<INode> closedSet, Set<INode> highLight) {
		if(current.isObjective()) return GOAL;
		if(current.isStart()) return START;
		if(current.isWall()) return WALL;
		if(openSet != null && openSet.contains(current)) return OPEN;
		if(closedSet != null && closedSet.contains(current)) return CLOSED;
		if(highLight != null && highLight.contains(current)) return HIGHLIGHT;
		
		return EMPTY;
	}

	public int[][] getGrid() { return this.snapshot; }

	public static Color getColor(int i) {
		switch (i) {
			case EMPTY:
				return EMPTY_COLOR;
			case WALL:
				return WALL_COLOR;
			case START:
				return START_COLOR;
			case GOAL:
				return GOAL_COLOR;
			case OPEN:
				return OPEN_COLOR;
			case CLOSED:
				return CLOSED_COLOR;
			case PATH:
				return PATH_COLOR;
			case HIGHLIGHT:
				return HIGHLIGHT_COLOR;
			default:
				return null;
		}
	}

	public static void setCellProperties(Cell cell, int i)
	{
		switch (i) {
			case WALL:
				cell.setWall(true);
				break;
			case START:
				cell.setStart(true);
				break;
			case GOAL:
				cell.setObjective(true);
				break;
		}
	}
}
