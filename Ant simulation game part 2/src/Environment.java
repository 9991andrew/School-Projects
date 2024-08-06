import javax.swing.*;
import java.awt.*;
import java.sql.Array;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;


/*
    Author: Andrew Whenham
    Purpose: Class will be responsible for the cells, ant behavior and spawning as well as creating the grid and drawing it and the ants.
    AUID: 3469950
 */

public class Environment extends JPanel implements GridChecker {
    private Cell grid[][]; //The grid we have the food, water, poison on as well as the ants.
    private ArrayList<Ant> ants; //the list of ants that we spawn into the grid.
    private int size; //The size of the grid
    private int numofAnts;//Number of ants initially, specified by the person initially running the game
    private Scanner scanner; //The way at which we grab the input from the player on starting the program to define the size of the grid
    private static final Random random = new Random(); //The random generator we use for creating random spawn locations and movement.
    public Environment() {
        this.scanner = new Scanner(System.in); //Create the scanner instance
        initGrid(); //Init the grid
        initAnt(); //Init the initial ant
        placeItems(size * 2, 'F'); // place food
        placeItems(size * 2, 'W'); // place water
        placeItems(size, 'P'); // place poison, poison will have less spawned to help ants spawn and die less

    }
    //Is the cell occupied?
    @Override
    public boolean isCellOccupied(int x, int y) {
        for(Ant ant : ants) {
            if(ant.getX() == ant.getHomePosX() && ant.getY() == ant.getHomePosY())
                return false;
            if(ant.getX() == x && ant.getY() == y)
                return true;
        }
        return false;
    }
/*
   detect whether a cell is the ant's house cell or not.
 */
    @Override
    public boolean isHouseCell(int x, int y) {
        for(Ant ant : ants) {
            if (ant.getHomePosY() == y && ant.getHomePosX() == x) {
                return true;
            }
        }
        return false;
    }
    /*
       get the cell at a specific x,y coordinate.
     */
    @Override
    public Cell getCellAt(int x, int y) {
        if(x >= 0 && x < grid.length && y >= 0 && y<grid[x].length){
            return grid[x][y];
        } else {
            return null;
        }
    }

    //Init the grid
    private void initGrid() {
        System.out.println("Please provide the WIDTH x HEIGHT of the grid: ");
        size = scanner.nextInt();
        this.grid = new Cell[size][size];


        for(int i = 0; i < size; i++) {
            for(int j = 0; j < size; j++) {
                grid[i][j] = new Cell(i, j, ' ');
            }
        }
    }
    //Init all the ants
    private void initAnt() {
        System.out.println("Please provide how many ants you would like in the game");
        this.numofAnts = scanner.nextInt();
        this.ants = new ArrayList<>(this.numofAnts);

        for(int i = 0; i < this.numofAnts; i++) {
            int x = random.nextInt(size);
            int y = random.nextInt(size);
            ants.add(new Ant(x, y, this));
        }
    }
    //Place the different items.
    //char type is going to be either:
    //F, W or P where F is Food, W
    public void placeItems(int count, char type) {
        int placed = 0;
        while(placed < count) {
            int x = random.nextInt(size);
            int y = random.nextInt(size);
            if(grid[x][y].getType() == ' ' && !(isHouseCell(x,y))) {
                grid[x][y].setType(type);
                placed++;
            }
        }
    }
    //Draw the ants to the colony
    private void drawAnts(Graphics g) {
        g.setColor(new Color(165,42,42));
        int cellWidth = getWidth() / size;
        int cellHeight = getHeight() / size;
        for(Ant ant : ants) {
            int x = ant.getX() * cellWidth;
            int y = ant.getY() * cellHeight;
            if(!ant.isPoisoned()) {
                g.fillOval(x,y,cellWidth,cellHeight);
            }

        }
    }
    //Spawn a new ant.
    private Ant spawnNewAnt() {
       boolean isValidSpawnLocation = false;
       int newX =0, newY=0;
        while(!isValidSpawnLocation) {
            newX = random.nextInt(size);
            newY = random.nextInt(size);
            Cell cell = getCellAt(newX, newY);
            //Should make it so that the ants don't spawn on top of eachother?
            if(!cell.isHasFood() && !cell.isHasWater() && !cell.isHasPoison())
                isValidSpawnLocation = true;
        }
        //Make sure that the spawnlocation doesn't have anything on it by checking isValidSpawnLocation is true beforehand
        if(isValidSpawnLocation)
            return new Ant(newX,newY, this);
        else
            return null;
    }

    //Update the ants, move them.
    public void update(long deltaTime) {
        ArrayList<Ant> newAnts = new ArrayList<>();
        ArrayList<Ant> poisonedAnts = new ArrayList<>();
        for (Ant ant : ants) {
            // Move the ant first
            ant.move(size, deltaTime);
            // Then check the cell type and update state accordingly
            Cell cell = getCellAt(ant.getX(), ant.getY());
            if (cell.isHasWater() && ant.getAntState() == Ant.State.SEARCHING_WATER) {
                System.out.println("Ant found water, looking for food now.");
                ant.setAntState(Ant.State.SEARCHING_FOOD);
            } else if (cell.isHasFood() && ant.getAntState() == Ant.State.SEARCHING_FOOD) {
                System.out.println("Ant found food, returning home");
                ant.setAntState(Ant.State.RETURNING_HOME);
            } else if (this.isHouseCell(ant.getX(), ant.getY()) && ant.getAntState() ==Ant.State.RETURNING_HOME) {
                ant.setAntState(Ant.State.SEARCHING_WATER);
                Ant newAnt = spawnNewAnt();
                newAnts.add(newAnt);
            }

            // Check for poison after moving
            if (cell.isHasPoison()) {
                ant.setIsPoisoned(true);
                System.out.println("State: " + ant.getAntState());
                poisonedAnts.add(ant);
            }
            System.out.println("State: " + ant.getAntState());
        }
        ants.addAll(newAnts);
        ants.removeAll(poisonedAnts);
    }

    //This is where we call drawGrid, drawAnts and then call paintComponent to make sure htat we do in fact do that.
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        drawGrid(g);
        drawAnts(g);

    }
    //We draw the grid.
    public void drawGrid(Graphics g) {
        int cellWidth = getWidth() / size;
        int cellHeight = getHeight() / size;
        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[i].length; j++) {
                int x = i * cellWidth;
                int y = j * cellHeight;
                char type = grid[i][j].getType();
                if (type == 'F') {
                    g.setColor(Color.GREEN);
                } else if (type == 'W') {
                    g.setColor(Color.BLUE);
                } else if (type == 'P') {
                    g.setColor(Color.RED);
                } else if(isHouseCell(i,j)){
                    g.setColor(Color.PINK);
                } else {
                    g.setColor(Color.WHITE);
                }
                // Draw the cell
                g.fillRect(x, y, cellWidth, cellHeight);
                g.setColor(Color.BLACK); // For drawing the cell borders
                g.drawRect(x, y, cellWidth, cellHeight);
            }
        }
    }
    //Getters and setters.
    public Cell[][] getGrid() {
        return grid;
    }

}
