package algorithms.search;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class Grid implements ILayout {
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
	public static Cell getStartCell() { return startCell; }
	public static Cell getObjectiveCell() { return objectiveCell; }

	public class Cell implements INode {
		private final int row;
		private final int column;
		private final Rectangle rect;

		private boolean isWall;
		private boolean isObjective;
		private boolean isStart;

		private INode parent;

		public Cell(int row, int column, double x1, double y1, double x2, double y2) {
			if (x1 == x2 || y1 == y2) 
				throw new IllegalArgumentException("A cell must be a non-degenerate rectangle.");

			double minX = Math.min(x1, x2);
			double minY = Math.min(y1, y2);
			double maxX = Math.max(x1, x2);
			double maxY = Math.max(y1, y2);

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
			this.row = row;
			this.column = column;
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

		public Rectangle getRect() { return this.rect; }

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

			return dx + dy;
		}

		@Override
		public int getG() {
			if(this.parent == null) return 1;
			return parent.getG() + 1;
		}

		@Override
		public int getF() {
			return getG() + getH();
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

        int[][] directions = { {-1, 0}, {1, 0}, {0, -1}, {0, 1} };

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
}
