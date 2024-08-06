import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.PriorityQueue;
/*
*
* Author: Andrew Whenham
* Purpose: Where we have implemented the A* star pathfinding algorithm.
* */
public class AStarPathFinder {
    private Cell[][] grid; // The grid on which the pathfinding operates
    private PriorityQueue<Cell> openSet; // Set of nodes to be evaluated
    private boolean[][] closedSet; // Set of nodes already evaluated
    private Cell startCell; // Starting cell of the path
    private Cell goalCell; // Goal cell of the path
    private int size; // Size of the grid

    // Constructor initializes the pathfinder with the grid and start/goal cells
    public AStarPathFinder(Cell[][] grid, Cell startCell, Cell goalCell) {
        this.grid = grid;
        this.startCell = startCell;
        this.goalCell = goalCell;
        this.size = grid.length;
        // PriorityQueue sorts cells by their fCost for efficient retrieval of the lowest-cost cell
        this.openSet = new PriorityQueue<>(Comparator.comparingInt(c -> c.fCost));
        this.closedSet = new boolean[size][size]; // Tracks which cells have been processed

        // Initialize start cell costs and add it to the open set
        startCell.gCost = 0; // Cost from start to start is zero
        startCell.hCost = calculateHeuristic(startCell, goalCell); // Estimated cost from start to goal
        startCell.fCost = startCell.gCost + startCell.hCost; // Total cost
        openSet.add(startCell); // Add start cell to open set to begin the algorithm
    }

    // Method to find the shortest path from start to goal
    public List<Cell> findPath() {
        while (!openSet.isEmpty()) { // Continue until there are no more cells to evaluate
            Cell current = openSet.poll(); // Retrieve and remove the cell with the lowest fCost from the open set

            // If the goal cell is reached, construct and return the path from start to goal
            if (current.equals(goalCell)) {
                return retracePath(startCell, goalCell);
            }

            closedSet[current.x][current.y] = true; // Mark the current cell as evaluated

            // Iterate through each neighbor of the current cell
            for (Cell neighbor : getNeighbors(current)) {
                if (closedSet[neighbor.x][neighbor.y] || neighbor.terrain == Terrain.OBSTACLE) {
                    continue; // Skip if neighbor has been evaluated or is an obstacle
                }

                int tentativeGCost = current.gCost + getMovementCost(current, neighbor); // Calculate tentative gCost through the current cell
                // If the path through the current cell is better, or the neighbor is not in openSet
                if (tentativeGCost < neighbor.gCost || !openSet.contains(neighbor)) {
                    // Update the neighbor with new costs and parent
                    neighbor.gCost = tentativeGCost;
                    neighbor.hCost = calculateHeuristic(neighbor, goalCell);
                    neighbor.fCost = neighbor.gCost + neighbor.hCost;
                    neighbor.parent = current; // Set current cell as parent for path tracing

                    if (!openSet.contains(neighbor)) {
                        openSet.add(neighbor); // Add the neighbor to the open set if it's not already there
                    }
                }
            }
        }

        return new ArrayList<>(); // Return an empty path if the goal is not reachable
    }

    // Method to calculate the heuristic (Manhattan distance) between two cells
    private int calculateHeuristic(Cell cell1, Cell cell2) {
        return Math.abs(cell1.x - cell2.x) + Math.abs(cell1.y - cell2.y);
    }

    // Method to retrace the path from goal to start using the parent references
    private List<Cell> retracePath(Cell startCell, Cell goalCell) {
        List<Cell> path = new ArrayList<>();
        Cell currentCell = goalCell;
        while (!currentCell.equals(startCell)) { // Trace back from goal to start
            path.add(0, currentCell); // Add each cell to the beginning of the list to reverse the path
            currentCell = currentCell.parent; // Move to the parent cell
        }
        path.add(0, startCell); // Optionally add the start cell to the path
        return path; // Return the constructed path
    }

    // Method to get the valid neighbors of a cell (excluding diagonals for simplicity)
    private List<Cell> getNeighbors(Cell cell) {
        List<Cell> neighbors = new ArrayList<>();
        for (int x = -1; x <= 1; x++) {
            for (int y = -1; y <= 1; y++) {
                if (x == 0 && y == 0) continue; // Skip the current cell itself
                if (Math.abs(x) + Math.abs(y) == 2) continue; // Skip diagonal neighbors

                int checkX = cell.x + x;
                int checkY = cell.y + y;

                // Ensure the neighbor is within grid bounds
                if (checkX >= 0 && checkX < size && checkY >= 0 && checkY < size) {
                    neighbors.add(grid[checkX][checkY]);
                }
            }
        }
        return neighbors;
    }

    // Method to calculate the movement cost between two adjacent cells
    private int getMovementCost(Cell current, Cell neighbor) {
        return neighbor.terrain.getCost(); // Return the cost based on the neighbor's terrain
    }
}
