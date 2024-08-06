
/*
    Author: Andrew Whenham
    Purpose: The purpose of this program is to create an ant colony simulation in which ants gather food and drink water as well as die to
    poison that can spawn in the grid.
    Date: February 21st 2024
    AUID: 3469950
 */
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;



public class Main extends JFrame{
    private Environment environment; //The instance of the environment class we use to call environment.update(), to start the game
    private long lastUpdateTime; //The time we last updated the program.
    //Initialization constructor
    public Main() {

        setupJFrame();
        setupSimulationThread();

    }
    //Sets up the JFrame we draw the grid and ants to
    public void setupJFrame(){
        setPreferredSize(new Dimension(800, 600));
        setTitle("Ant Simulation");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        environment = new Environment();
        add(environment);
        pack();

        setLocationRelativeTo(null);
        setVisible(true);
    }
    //The simulation thread in which we update the environment which includes all the and their behaviors
    private void setupSimulationThread() {
        int delay = 1000;
        ActionListener taskPerformer = evt->
        {
            long currentTime = System.currentTimeMillis();
            long deltaTime = currentTime - lastUpdateTime;
            lastUpdateTime = currentTime;
            environment.update(deltaTime);
            environment.repaint();
        };
        new Timer(delay, taskPerformer).start();
    }
    //Where the program starts it execution.
    public static void main(String[] args) {
        SwingUtilities.invokeLater(()->new Main());
    }
}