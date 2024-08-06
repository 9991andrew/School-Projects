import java.awt.*;
import java.util.ArrayList;
import java.util.Random;
/**
 * Purpose: The environment inside of the game panel where we play our game.
 * Author: Andrew Whenham
 * AUID: 3469950
 * Date: 2024-03-14
 * Assignment 3
 */
public class GameEnvironment {
    private final int gridSize = 10; //The size of the grid
    private final char[][] grid = new char[gridSize][gridSize]; //The grid defined
    private Point playerPosition; //The players position
    private Point treasurePoint; //The treasure point
    private final ArrayList<Point> obstacles = new ArrayList<>(); //The array of obstacles we define
    private final Random random = new Random(); //The random number generator

    /**
     * The default constructor otherwise known as the initialization constructor.
     */
    public GameEnvironment() {
        initializeGrid();
        placePlayer();
        placeTreasure();
        placeObstacles(20);
        

    }

    /**
     *
     * @param action The action we are going to perform
     * @return the point of the player after the action is complete.
     */
    public Point performAction(int action) {
        boolean moveSuccessful = movePlayer(action);
        if (moveSuccessful) {
            System.out.println("Moved: " + actionToString(action) + " to " + playerPosition.toString());
        } else {            System.out.println("Move failed: " + actionToString(action) + " from " + playerPosition.toString());
        }
        return new Point(playerPosition);
    }

    /**
     *
     * @param action the action we want to convert to a string
     * @return the return value which is a string in this case for what action we just did
     */
    private String actionToString(int action) {
        switch (action) {
            case 0: return "UP";
            case 1: return "DOWN";
            case 2: return "LEFT";
            case 3: return "RIGHT";
            default: return "UNKNOWN";
        }
    }

    /**
     *
     * @param count number of obstacles
     */
    private void placeObstacles(int count) {
        for(int i = 0; i < count; i++) {
            int x=0, y=0;
            do {
                x = random.nextInt(gridSize);
                y = random.nextInt(gridSize);
            }while(grid[x][y] != '-');
            obstacles.add(new Point(x,y));
            char OBSTACLE = 'O';
            grid[x][y] = OBSTACLE;
        }
    }

    /**
     * Place the treasure somewhere random.
     */
    private void placeTreasure() {
        int x = 0, y = 0;
        do {
            x = random.nextInt(gridSize);
            y = random.nextInt(gridSize);
        }while(grid[x][y] !='-');
        treasurePoint = new Point(x,y);
        char TREASURE = 'T';
        grid[x][y] = TREASURE;
    }

    /**
     * Start the player or place the player somewhere random
     */
    private void placePlayer() {
        int x = 0,y = 0;
        do {
            x = random.nextInt(gridSize);
            y = random.nextInt(gridSize);
        }while(grid[x][y] !='-');
        playerPosition = new Point(x,y);
        char PLAYER = 'P';
        grid[x][y] = PLAYER;
    }

    /**
     * Initialize the grid as empty at first.
     */
    private void initializeGrid() {
        for(int i = 0; i < gridSize; i++) {
            for(int j = 0; j < gridSize; j++) {
                char EMPTY = '-';
                grid[i][j] = EMPTY;
            }
        }
    }

    /**
     * Resets the environment by placing the player back to a random spot.
     */

    public void resetEnvironment() {
        for(int i = 0; i < gridSize; i++) {
            for(int j = 0; j < gridSize; j++) {
                grid[i][j] = '-';
            }
        }
        obstacles.clear();
        placePlayer();
        placeTreasure();
        placeObstacles(20);
    }

    /**
     *
     * @param action The action the player is going to perform
     * @return whether the action was done successfully
     */
    public boolean movePlayer(int action) {
        // Define actions: 0=up, 1=down, 2=left, 3=right
        int[][] directions = {{-1, 0}, {1, 0}, {0, -1}, {0, 1}};
        int newX = playerPosition.x + directions[action][0];
        int newY = playerPosition.y + directions[action][1];

        // Check if new position is valid and not an obstacle
        if (isValidMove(newX, newY)) {
            updatePlayerPosition(newX, newY);
            return true;
        }
        return false;
    }

    /**
     *
     * @param x The x coordinate of the move we are about to make
     * @param y The y coordinate of the mvoe we are about to make
     * @return
     */

    private boolean isValidMove(int x, int y) {
        return x >= 0 && x < gridSize && y >= 0 && y < gridSize && !obstacles.contains(new Point(x, y));
    }

    /**
     *
     * @param x The x coordinate to update
     * @param y The y coordinate to update
     */
    private void updatePlayerPosition(int x, int y) {
        grid[playerPosition.x][playerPosition.y] = '-'; // Clear old position
        playerPosition.setLocation(x, y);
        grid[x][y] = 'P'; // Mark new position
    }

    /**
     *
     * @param position The position of the reward
     * @return returns whether we got to the position of the reward
     */

    public double getReward(Point position) {
        if (position.equals(treasurePoint)) {
            return 100.0; // Found the treasure
        } else if (obstacles.contains(position)) {
            return -100.0; // Hit an obstacle
        }
        return -1.0;
    }

    /**
     *
     * @param position the point we are checking
     * @return was the treasure found or not
     */
    public boolean checkTreasureFound(Point position) {
        return position.equals(treasurePoint);
    }
    // Getters for player position, treasure, and obstacles
    public Point getPlayerPosition() {
        return playerPosition;
    }

    public Point getTreasurePosition() {
        return treasurePoint;
    }

    public ArrayList<Point> getObstacles() {
        return obstacles;
    }

    public char[][] getGrid() {
        return grid;
    }
    public int getGridSize() {
        return gridSize;
    }
}
