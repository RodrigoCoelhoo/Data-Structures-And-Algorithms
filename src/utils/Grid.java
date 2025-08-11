package utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import algorithms.interfaces.ILayout;
import algorithms.interfaces.INode;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class Grid implements ILayout {
	private Cell[][] grid;
	private Cell startCell;
	private Cell objectiveCell;
	private static String heuristic = null;
	private static int[][] directions = {
                {-1, 0}, {-1, 1}, {0, 1}, {1, 1}, 
                {1, 0}, {1, -1}, {0, -1}, {-1, -1}
            };

	static final Color EMPTY_COLOR 	= Color.LIGHTGRAY;
    static final Color OPEN_COLOR 		= Color.web("#90CAF9");       
    static final Color CLOSED_COLOR 	= Color.web("#1976D2");
	static final Color WALL_COLOR 		= Color.RED;
	static final Color GOAL_COLOR		= Color.YELLOW;
	static final Color START_COLOR		= Color.web("#66BB6A");

	
    private enum Action { WALL, START, OBJECTIVE, NONE }
    private Action currentAction = Action.NONE;
    private boolean dragSetValue = true;

	public Grid(int rows, int columns, Pane visualContainer) {
        this.grid = new Cell[rows][columns];
		this.startCell = null;
		this.objectiveCell = null;

        double outerPadding = 5.0;
        double innerPadding = 2.0;

        double paneWidth = visualContainer.getWidth();
        double paneHeight = visualContainer.getHeight();

        double usableWidth = paneWidth - 2 * outerPadding;
        double usableHeight = paneHeight - 2 * outerPadding;

        double cellWidth = usableWidth / columns;
        double cellHeight = usableHeight / rows;

        for (int row = 0; row < rows; row++) {
            for (int column = 0; column < columns; column++) {
                double x = outerPadding + column * cellWidth + innerPadding / 2;
                double y = outerPadding + row * cellHeight + innerPadding / 2;
                double width = cellWidth - innerPadding;
                double height = cellHeight - innerPadding;

                grid[row][column] = new Cell(row, column, x, y, x + width, y + height);
            }
        }
    }

	public Cell[][] getGrid() { return this.grid; }
	public Cell getObjectiveCell() { return objectiveCell; }
	public static String getHeuristic() { return heuristic; }
	public static void setHeuristic(String h) { heuristic = h; }
	public static void setDirections(int[][] dir) { directions = dir; }
	public static int[][] getDirections() { return directions; }

	@Override
	public INode getInitialNode() {
		return startCell;
	}

	@Override
	public boolean isGoal(INode node) {
		return node.equals(objectiveCell);
	}

	@Override
    public List<INode> getSuccessors(INode node) {
        List<INode> successors = new ArrayList<>();
        Cell cell = (Cell) node;

        for (int[] dir : directions) {
            int newRow = cell.row + dir[0];
            int newCol = cell.column + dir[1];

            if (newRow >= 0 && newRow < grid.length &&
                newCol >= 0 && newCol < grid[0].length) {

                Cell neighbor = grid[newRow][newCol];
                if (!neighbor.isWall()) {
                    successors.add(neighbor);
                }
            }
        }

        return successors;
    }

	public class Cell implements INode {
		private final int row;
		private final int column;
		private final Rectangle rect;
		private final Point p1;
		private final Point p2;

		private boolean isWall;
		private boolean isObjective;
		private boolean isStart;

		private INode parent;
		private int g = Integer.MAX_VALUE;

		public Cell(int row, int column, double x1, double y1, double x2, double y2) {
			if (x1 == x2 || y1 == y2) 
				throw new IllegalArgumentException("A cell must be a non-degenerate rectangle.");

			double minX = Math.min(x1, x2);
			double minY = Math.min(y1, y2);
			double maxX = Math.max(x1, x2);
			double maxY = Math.max(y1, y2);
			this.p1 = new Point(minX, minY);
			this.p2 = new Point(maxX, maxY);

			this.rect = new Rectangle(minX, minY, maxX - minX, maxY - minY);
			this.rect.setFill(EMPTY_COLOR);
			this.rect.setArcWidth(4);
			this.rect.setArcHeight(4);

			// Step 1 — Mouse pressed: decide the action & value
			rect.setOnMousePressed(e -> {
				if (e.isControlDown()) {
					Grid.this.currentAction = Action.OBJECTIVE;
					Grid.this.dragSetValue = !this.isObjective();
					this.setObjective(Grid.this.dragSetValue);
				} else if (e.isShiftDown()) {
					Grid.this.currentAction = Action.START;
					Grid.this.dragSetValue = !this.isStart();
					this.setStart(Grid.this.dragSetValue);
				} else {
					Grid.this.currentAction = Action.WALL;
					Grid.this.dragSetValue = !this.isWall();
					this.setWall(Grid.this.dragSetValue);
				}
			});

			// Step 2 — Drag detected: tell JavaFX to send drag-entered events to other cells
			rect.setOnDragDetected(e -> {
				rect.startFullDrag(); // this is the legal place to start full drag
			});

			// Step 3 — Apply the action when entering another rectangle during drag
			rect.setOnMouseDragEntered(e -> {
				switch (Grid.this.currentAction) {
					case OBJECTIVE -> this.setObjective(Grid.this.dragSetValue);
					case START     -> this.setStart(Grid.this.dragSetValue);
					case WALL      -> this.setWall(Grid.this.dragSetValue);
					default        -> {}
				}
			});

			// Step 4 — Reset when released
			rect.setOnMouseReleased(e -> {
				Grid.this.currentAction = Action.NONE;
			});

			this.isWall = false;
			this.isObjective = false;
			this.isStart = false;
			this.row = row;
			this.column = column;
		}

		public void setWall(boolean wall) { 
			this.isWall = wall;
			this.rect.setFill(wall ? WALL_COLOR : EMPTY_COLOR);

			if (wall) {
				this.isObjective = false;
				this.isStart = false;
			}
		}
		
		public void setStart(boolean start) {
			if(startCell != null) {
				startCell.isObjective = false;
				startCell.isStart = false;
				startCell.isWall = false;
				startCell.rect.setFill(EMPTY_COLOR);
				startCell = null;
			}

			this.isStart = start;

			if (start) {
				this.isWall = false;
				this.isObjective = false;
				this.rect.setFill(START_COLOR);
				startCell = this;
			} else {
				this.rect.setFill(EMPTY_COLOR);
			}
		}

		public void setObjective(boolean objective) {
			if(objectiveCell != null) {
				objectiveCell.isObjective = false;
				objectiveCell.isStart = false;
				objectiveCell.isWall = false;
				objectiveCell.rect.setFill(EMPTY_COLOR);
				objectiveCell = null;
			}

			this.isObjective = objective;

			if (objective) {
				this.isWall = false;
				this.isStart = false;
				this.rect.setFill(GOAL_COLOR);
				objectiveCell = this;
			} else {
				this.rect.setFill(EMPTY_COLOR);
			}
		}

		public boolean isWall() { return this.isWall; }
		public boolean isStart() { return this.isStart; }
		public boolean isObjective() { return this.isObjective; }

		public Rectangle getRect() { return this.rect; }
		public Point getP1() { return this.p1; }
		public Point getP2() { return this.p2; }
		public int getColumn() { return column; }
		public int getRow() { return row; }

		@Override
		public boolean equals(Object o) {
			if (this == o) return true;
			if (!(o instanceof Cell)) return false;
			Cell cell = (Cell) o;
			return row == cell.row && column == cell.column;
		}

		@Override
		public int hashCode() {
			return Objects.hash(row, column);
		}

		@Override
		public int getH() {
			int dx = Math.abs(this.column - objectiveCell.column);
    		int dy = Math.abs(this.row - objectiveCell.row);
			
			switch (heuristic) {
				case "Manhattan":
					return dx + dy;

				case "Euclidean":
					return (int) Math.sqrt(dx * dx + dy * dy);

				case "Octile":
					double F = Math.sqrt(2) - 1;
            		return (int) (F * Math.min(dx, dy) + Math.max(dx, dy));

				case "Chebyshev":
					return Math.max(dx, dy);
					
				default:
					throw new IllegalArgumentException("Unknown heuristic: " + heuristic);
			}
		}

		@Override
		public int getG() {
			return this.g;
		}

		@Override
		public int getF() {
			return getG() + getH();
		}

		@Override
		public void setG(int g) {
			this.g = g;
		}

		@Override
		public INode getParent() {
			return this.parent;
		}

		@Override
		public void setParent(INode parent) {
			this.parent = parent;
		}
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
