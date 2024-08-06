import java.util.Scanner;

/**
 * Author: Andrew Whenham
 * Purpose: Configures the game according to what the player types prior to the game starting.
 */
public class AntGameConfigurator {
    private AntGame game; //The ant game instance we use here
    private Scanner scanner; //The scanner instance we use here
    //initialization constructor
    public AntGameConfigurator(AntGame game, Scanner scanner) {
        this.game = game;
        this.scanner = scanner;
    }
    public void configureTheGame() {
        System.out.println("Configuring the game grid:");

        // Set the game's starting cell
        System.out.println("Enter start cell coordinates (x,y): ");
        int startX = scanner.nextInt();
        int startY = scanner.nextInt();
        this.game.setStartCell(startX, startY);

        // Set the game's goal cell
        System.out.println("Enter goal cell coordinates (x,y): ");
        int goalX = scanner.nextInt();
        int goalY = scanner.nextInt();
        game.setGoalCell(goalX, goalY);

        // Set obstacles for the game
        System.out.println("Enter number of obstacles: ");
        int obstacles = scanner.nextInt();
        for (int i = 0; i < obstacles; i++) {
            System.out.println("Enter obstacle coordinates for (x,y): ");
            int obstacleX = scanner.nextInt();
            int obstacleY = scanner.nextInt();
            game.setCellTerrain(obstacleX, obstacleY, Terrain.OBSTACLE);
        }

        System.out.println("Enter a single terrain type for all non-obstacle cells - [O]pen, [G]rassland, [S]wampland: ");
        char terrainType = scanner.next().charAt(0);
        Terrain terrain = Terrain.OPEN;
        switch (terrainType) {
            case 'O':
                terrain = Terrain.OPEN;
                break;
            case 'G':
                terrain = Terrain.GRASSLAND;
                break;
            case 'S':
                terrain = Terrain.SWAMPLAND;
                break;
            default:
                System.out.println("Invalid terrain type. Defaulting to Open.");
                break;
        }

        // Apply the chosen terrain type to all non-obstacle cells
        for (int x = 0; x < AntGame.SIZE; x++) {
            for (int y = 0; y < AntGame.SIZE; y++) {
                // Skip if the cell is an obstacle, start, or goal cell
                if (game.getCellTerrain(x, y) != Terrain.OBSTACLE && !(x == startX && y == startY) && !(x == goalX && y == goalY)) {
                    game.setCellTerrain(x, y, terrain);
                }
            }
        }
    }


}
