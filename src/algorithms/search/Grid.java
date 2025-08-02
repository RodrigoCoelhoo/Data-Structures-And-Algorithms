package algorithms.search;

import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class Grid {
	private Cell[][] grid;
	private static Cell startCell;
	private static Cell objectiveCell;

	public Grid(int rows, int columns, Pane visualContainer) {
        this.grid = new Cell[rows][columns];
		Grid.startCell = null;
		Grid.objectiveCell = null;

        double outerPadding = 5.0;
        double innerPadding = 2.0;

        double paneWidth = visualContainer.getWidth();
        double paneHeight = visualContainer.getHeight();

        double usableWidth = paneWidth - 2 * outerPadding;
        double usableHeight = paneHeight - 2 * outerPadding;

        double cellWidth = usableWidth / columns;
        double cellHeight = usableHeight / rows;

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                double x = outerPadding + j * cellWidth + innerPadding / 2;
                double y = outerPadding + i * cellHeight + innerPadding / 2;
                double width = cellWidth - innerPadding;
                double height = cellHeight - innerPadding;

                grid[i][j] = new Cell(x, y, x + width, y + height);
            }
        }
    }

	public Cell[][] getGrid() { return this.grid; }
	public static Cell getStartCell() { return startCell; }
	public static Cell getObjectiveCell() { return objectiveCell; }

	public class Cell {
		private boolean isWall;
		private boolean isObjective;
		private boolean isStart;

		private final Point p1;
		private final Point p2;

		private final Rectangle rect;

		public Cell(double x1, double y1, double x2, double y2) {
			if (x1 == x2 || y1 == y2) 
				throw new IllegalArgumentException("A cell must be a non-degenerate rectangle.");

			double minX = Math.min(x1, x2);
			double minY = Math.min(y1, y2);
			double maxX = Math.max(x1, x2);
			double maxY = Math.max(y1, y2);

			this.p1 = new Point(minX, minY); 
			this.p2 = new Point(maxX, maxY);

			this.rect = new Rectangle(minX, minY, maxX - minX, maxY - minY);
			this.rect.setFill(Color.LIGHTGRAY);
			this.rect.setArcWidth(4);
			this.rect.setArcHeight(4);

			rect.setOnMouseClicked(e -> {
				if (e.isControlDown()) {
					this.setObjective(!this.isObjective());
				} else if (e.isShiftDown()) {
					this.setStart(!this.isStart());
				} else {
					this.setWall(!this.isWall());
				}
			});


			this.isWall = false;
			this.isObjective = false;
			this.isStart = false;
		}

		public void setWall(boolean wall) { 
			this.isWall = wall;
			this.rect.setFill(wall ? Color.RED : Color.LIGHTGRAY);

			if (wall) {
				this.isObjective = false;
				this.isStart = false;
			}
		}

		
		public void setStart(boolean start) {
			if(Grid.startCell != null) {
				Grid.startCell.isObjective = false;
				Grid.startCell.isStart = false;
				Grid.startCell.isWall = false;
				Grid.startCell.rect.setFill(Color.LIGHTGRAY);
				Grid.startCell = null;
			}

			this.isStart = start;

			if (start) {
				this.isWall = false;
				this.isObjective = false;
				this.rect.setFill(Color.YELLOW);
				Grid.startCell = this;
			} else {
				this.rect.setFill(Color.LIGHTGRAY);
			}
		}

		public void setObjective(boolean objective) {
			if(Grid.objectiveCell != null) {
				Grid.objectiveCell.isObjective = false;
				Grid.objectiveCell.isStart = false;
				Grid.objectiveCell.isWall = false;
				Grid.objectiveCell.rect.setFill(Color.LIGHTGRAY);
				Grid.objectiveCell = null;
			}

			this.isObjective = objective;

			if (objective) {
				this.isWall = false;
				this.isStart = false;
				this.rect.setFill(Color.web("#66BB6A"));
				Grid.objectiveCell = this;
			} else {
				this.rect.setFill(Color.LIGHTGRAY);
			}
		}

		public boolean isWall() { return this.isWall; }
		public boolean isStart() { return this.isStart; }
		public boolean isObjective() { return this.isObjective; }

		public Point getP1() { return this.p1; }
		public Point getP2() { return this.p2; }
		public Rectangle getRect() { return this.rect; }
	}

	public class Point {
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
