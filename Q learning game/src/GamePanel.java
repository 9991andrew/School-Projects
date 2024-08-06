import javax.swing.*;
import java.awt.*;
/**
 * Purpose: The game panel inside of our game frame.
 * Author: Andrew Whenham
 * AUID: 3469950
 * Date: 2024-03-14
 * Assignment 3
 */
public class GamePanel extends JPanel {
    private GameEnvironment game;

    /**
     *
     * @param game the game environment we are playing on
     */
    public GamePanel(GameEnvironment game) {
        this.game = game;
    }


    /**
     * USed to draw the board
     * @param g the <code>Graphics</code> object to protect
     */
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        // Assuming each cell is 50x50 pixels
        int cellSize = 45;
        for (int i = 0; i < game.getGridSize(); i++) {
            for (int j = 0; j < game.getGridSize(); j++) {
                int x = j * cellSize;
                int y = i * cellSize;
                char cell = game.getGrid()[i][j];
                if (cell == 'P') {
                    g.setColor(Color.BLUE);
                    g.fillRect(x, y, cellSize, cellSize);
                } else if (cell == 'T') {
                    g.setColor(Color.YELLOW);
                    g.fillRect(x, y, cellSize, cellSize);
                } else if (cell == 'O') {
                    g.setColor(Color.GRAY);
                    g.fillRect(x, y, cellSize, cellSize);
                } else {
                    g.setColor(Color.WHITE);
                    g.fillRect(x, y, cellSize, cellSize);
                }
                g.setColor(Color.BLACK);
                g.drawRect(x, y, cellSize, cellSize);
            }
        }
    }
}
