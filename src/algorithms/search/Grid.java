package algorithms.search;

import javafx.scene.layout.Pane;

public class Grid {
	private Cell[][] grid;

	public Grid(int height, int width, Pane visualContainer) {
		this.grid = new Cell[height][width];
	}
	
	private class Cell {
		private boolean isWall;
		private final Point p1;
		private final Point p2;

		public Cell(double x1, double y1, double x2, double y2) {
			if (x1 == x2 || y1 == y2) 
				throw new IllegalArgumentException("A cell must be a non-degenerate rectangle.");

			double minX = Math.min(x1, x2);
			double minY = Math.min(y1, y2);
			double maxX = Math.max(x1, x2);
			double maxY = Math.max(y1, y2);

			this.p1 = new Point(minX, minY); 
			this.p2 = new Point(maxX, maxY); 
			this.isWall = false;
		}

		public void setWall(boolean wall) { this.isWall = wall; }
		public boolean getWall() { return this.isWall; }
		public Point getP1() { return this.p1; }
		public Point getP2() { return this.p2; }
	}

	private class Point {
		private final double x;
		private final double y;

		public Point(double x, double y) {
			if (x < 0 || y < 0) throw new IllegalArgumentException("Coordinates must be non-negative.");
			
			this.x = x;
			this.y = y;
		}

		public double getX() { return this.x; }
		public double getY() { return this.y; }
	}
}
