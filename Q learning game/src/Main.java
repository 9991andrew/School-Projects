import javax.swing.*;
import java.awt.*;

/**
 * Purpose: To create an AI that learns from its experiences in the game.
 * Author: Andrew Whenham
 * AUID: 3469950
 * Date: 2024-03-14
 * Assignment 3
 */

public class Main extends JFrame {
    private GamePanel gamePanel; //The game panel we have in our frame
    private GameEnvironment gameEnvironment; //The environment we're playing in.
    private QLearningAgent agent; //The agent or player we are trying to make learn.

    /**
     * Initializes the JFrame
     */
    public Main() {
        gameEnvironment = new GameEnvironment();
        setTitle("Treasure Hunting Game");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(500, 500);
        setLocationRelativeTo(null);
        gamePanel = new GamePanel(gameEnvironment);
        add(gamePanel);
        setVisible(true);

        agent = new QLearningAgent(gameEnvironment.getGridSize());
    }

    /**
     * Where we run the game.
     */
    public void runGame() {
        new Thread(() -> {
            int episodes = 1000;
            for (int episode = 0; episode < episodes; episode++) {
                gameEnvironment.resetEnvironment();
                SwingUtilities.invokeLater(gamePanel::repaint); // Update GUI in EDT

                Point playerPosition = gameEnvironment.getPlayerPosition();
                boolean treasureFound = false;

                while (!treasureFound) {
                    int action = agent.chooseAction(playerPosition.x, playerPosition.y);
                    Point nextPosition = gameEnvironment.performAction(action);
                    double reward = gameEnvironment.getReward(nextPosition);
                    agent.updateQTable(playerPosition.x, playerPosition.y, action, reward, nextPosition.x, nextPosition.y);
                    playerPosition = nextPosition;
                    treasureFound = gameEnvironment.checkTreasureFound(nextPosition);

                    if (treasureFound) {
                        System.out.println("Treasure found in episode " + episode);
                    }

                    // Ensure GUI updates are done on the EDT
                    SwingUtilities.invokeLater(gamePanel::repaint);

                    // Sleep to slow down the loop for visual purposes
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }

    /**
     *
     * @param args to do with the console and something we are not using
     * This is where the game begins its execution
     */
    public static void main(String[] args) {
        Main main = new Main();
        SwingUtilities.invokeLater(main::runGame); // Ensure GUI construction is completed
    }
}