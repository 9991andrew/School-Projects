import javax.swing.*;
/**
 * Purpose: Implement the Minimax Algorithm to Connect 4.
 * Author: Andrew Whenham
 * AUID: 3469950
 * Date: 2024-03-14
 * Assignment 3
 */
public class Game {
    /**
     * THis is where the program begins.
     * @param args part of something we are not using here.
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(()-> {
            GameFrame game = new GameFrame();
            game.setVisible(true);
        });

    }
}
