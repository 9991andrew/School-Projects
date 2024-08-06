import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/*
    Author: Andrew Whenham
    Purpose: Class will represent the ant and its states and movement
    AUID: 3469950
 */
public class Ant {
    private int x, y; // Ant's position in the grid
    private final int homePosX; // The house x coordinate.
    private final int homePosY; //The house y coord.
    private Environment environment; //The environment, grid we spawn ants into.
    private boolean isPoisoned; //Is the ant poisoned.

    private long lastMoveTime = 0; //When was the last move
    private final long MOVE_THRESHOLD = 100; //HOw long to wait until we can move again

    //States the ant could be in.
    enum State {
        SEARCHING_FOOD, //The ant is searching for food
        SEARCHING_WATER, //The ant is searching for water
        RETURNING_HOME // The ant is trying to return home.
    }

    private State antState; //The current state the ant is in.

    public Ant(int x, int y, Environment gridChecker) {
        this.x = x;
        this.y = y;

        this.homePosX = x;
        this.homePosY = y;

        this.environment = gridChecker;
        this.antState = State.SEARCHING_FOOD;

    }

    //Moves the ant according to all the different states.
    public void move(int gridSize, long deltaTime) {
        switch(antState) {
            case SEARCHING_FOOD:
                moveRandomly(gridSize, deltaTime);
                break;
            case SEARCHING_WATER:
                moveRandomly(gridSize, deltaTime);
                break;
            case RETURNING_HOME:
                moveToHome();
                break;
        }
    }
    //Move the ant to the house that was created for it when it spawns into the grid
    private boolean moveToHome() {
        // Attempt to move diagonally first, then horizontally or vertically
        int dx = Integer.compare(homePosX, x);
        int dy = Integer.compare(homePosY, y);
        System.out.println(" dx,dy: " + (x + dx) + canMoveTo(dx + x,dy + y));
        // Check diagonal movement
        if (dx != 0 && dy != 0 && canMoveTo(x + dx, y + dy)) {
            System.out.println("Hi, I am treying to return home1");
            x += dx;
            y += dy;
        } else if (dx != 0 && canMoveTo(x + dx, y)) {
            // If diagonal is not possible or not needed, try horizontal
            System.out.println("Hi, I am treying to return home2");
            x += dx;
        } else if (dy != 0 && canMoveTo(x, y + dy)) {
            // Then try vertical if horizontal is not possible or not needed
            System.out.println("Hi, I am treying to return home3");
            y += dy;
        }

        return (x == homePosX && y == homePosY);
    }

    // Move the ant in a random direction
    public void moveRandomly(int gridSize, long deltatime) {

        if (System.currentTimeMillis() - lastMoveTime < MOVE_THRESHOLD) {
            return;
        }

        boolean moved=false;
        int prevX = x, prevY = y; // Track the previous position
        for (int attempt = 0; attempt < 10 && !moved; attempt++) {
                // Usual movement logic
                int dx = (int) (Math.random() * 3) - 1;
                int dy = (int) (Math.random() * 3) - 1;

                int newX = Math.min(Math.max(x + dx, 0), gridSize - 1);
                int newY = Math.min(Math.max(y + dy, 0), gridSize - 1);

                //We are trying to go backwards when we really shouldn't be.
                if(newX == prevX && newY == prevY) {
                    continue; //We try again if this happens.
                }

                // Move to the new cell if it's not occupied
                if (!environment.isCellOccupied(newX, newY)) {
                    // Before moving, store the current position as the previous position
                    x = newX;
                    y = newY;
                    prevX = x;
                    prevY = y;
                    moved = true; // Exit after moving forward
                }
            }
        if (moved) {
            lastMoveTime = System.currentTimeMillis();
        }
    }


    // Getters and Setters
    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getHomePosX() {
        return homePosX;
    }

    public int getHomePosY() {
        return homePosY;
    }

    public void setAntState(State state) {
        this.antState = state;
    }
    public State getAntState() {
        return antState;
    }
    public void setIsPoisoned(boolean poison) {
        this.isPoisoned = poison;
    }
    public boolean isPoisoned(){
        return this.isPoisoned;
    }

    private boolean canMoveTo(int x, int y) {
        Cell cell =  environment.getCellAt(x,y);
        return !cell.isHasWater() || !cell.isHasPoison() || !cell.isHasFood();
    }
}

