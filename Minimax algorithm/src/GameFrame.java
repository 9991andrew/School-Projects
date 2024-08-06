import javax.swing.*;
/**
 * Purpose: The JFrame class, creates/initializes the JFrame.
 * Author: Andrew Whenham
 * AUID: 3469950
 * Date: 2024-03-14
 * Assignment 3
 */
public class GameFrame extends JFrame {
    //The frame at which we are drawing the panel on.
   public GameFrame() {
       this.setTitle("Connect 4");
       this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
       this.setResizable(false);
       this.add(new GameBoardPanel());
       this.pack();
       this.setLocationRelativeTo(null);
       this.setVisible(true);
   }
}