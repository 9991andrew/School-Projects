import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
/*
*
* Purpose: The ant game in which the A* pathfinding algorithm will run
* Author: Andrew Whenham
* */

public class AntGame extends JPanel {
    public static final int SIZE = 16; //The size of the grid
    private AStarPathFinder pathFinder; //The pathfinder (using A* pathfinding)
    private Cell[][] grid = new Cell[SIZE][SIZE]; //Grid we use as cells
    private Cell startCell; //The starting cell(the cell we spawn at)
    private Cell goalCell; //The goal cell (the cell we want to get to)
    private Cell antCell; //The cell we are currently on.
    //Initialize the AntGame
    public AntGame() {
        initGrid();
        setStartCell(0, 0);
        setGoalCell(SIZE - 1, SIZE - 1);
        pathFinder = new AStarPathFinder(grid, startCell, goalCell);
    }
    //Init the grid
    private void initGrid() {
        for(int i = 0; i < SIZE; i++) {
            for(int j = 0; j < SIZE; j++) {
                grid[i][j] = new Cell(i, j, Terrain.OPEN);
            }
        }
    }
    //Set the start cell.
    public void setStartCell(int x, int y) {
        startCell = grid[x][y];
        startCell.terrain = Terrain.OPEN;
    }
    //Set the goal cell.
    public void setGoalCell(int x, int y) {
        goalCell = grid[x][y];
        goalCell.terrain = Terrain.GRASSLAND;
    }
    //Sets the cell terrain
    public void setCellTerrain(int x, int y, Terrain terrain) {
        grid[x][y].terrain = terrain;
    }
    //Moves the ant
    public void moveAnt(Cell newCell) {
        if(antCell != null)
            antCell.hasAnt = false; //Clear the current position if ant is already there
        antCell = newCell; // Update the ant's position
        antCell.hasAnt = true; // Mark the new cell as containing the ant
        repaint(); // repaint the grid to reflect the ant's new position.
    }
    //Don't really know what this does, it is required by the Graphics API to work with JPanel.
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        drawGrid(g);
    }

    //Draw the grid
    private void drawGrid(Graphics g) {
        int cellSize = Math.min(getWidth(), getHeight()) / SIZE;
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                int x = i * cellSize;
                int y = j * cellSize;
                Cell cell = grid[i][j];
                switch (cell.terrain) {
                    case OPEN:
                        g.setColor(Color.WHITE);
                        break;
                    case GRASSLAND:
                        g.setColor(Color.GREEN);
                        break;
                    case SWAMPLAND:
                        g.setColor(Color.BLUE);
                        break;
                    case OBSTACLE:
                        g.setColor(Color.BLACK);
                        break;
                }
                g.fillRect(x, y, cellSize, cellSize);
                g.setColor(Color.GRAY);
                g.drawRect(x, y, cellSize, cellSize);

                // Highlight start and goal cells
                if (cell.equals(startCell)) {
                    g.setColor(Color.GREEN);
                    g.fillOval(x, y, cellSize, cellSize);
                } else if (cell.equals(goalCell)) {
                    g.setColor(Color.GRAY);
                    g.fillOval(x, y, cellSize, cellSize);
                }
                if (cell.hasAnt) {
                    g.setColor(Color.BLUE);
                    int padding = cellSize / 4;
                    g.fillRect(x + padding, y + padding, cellSize - 2 * padding, cellSize - 2 * padding);
                }
            }

        }
    }
    //Start the path finding algorithm (movement, finds paths so on).
    public void startPathFinding() {
        ArrayList<Cell> path = (ArrayList<Cell>) pathFinder.findPath();
        if(path != null && !path.isEmpty()) {
            new Thread(() -> {
              for (Cell cell : path) {
                  SwingUtilities.invokeLater(() -> moveAnt(cell));
                  try {
                      Thread.sleep(1000);
                  }catch(InterruptedException e) {
                      Thread.currentThread().interrupt();
                  }
              }
            }).start();
        }
    }

    public Terrain getCellTerrain(int x, int y) {
        return grid[x][y].terrain;
    }
}
