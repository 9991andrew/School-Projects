import javax.swing.*;
import java.awt.*;
import java.util.Scanner;
/*
* Purpose: Where the program starts execution, and initialization.
* Author: andrew whenham
* Date: February 22 2024
* AUID: 3469950
* */
public class Main extends JFrame {
    public AntGame game; //Ant game

    public Scanner scanner; //The scanner for taking in player input
    //The initialization constructor of the JFrame and so on.
    public Main() {
        setPreferredSize(new Dimension(800, 600));
        setTitle("Ant Simulation");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        scanner = new Scanner(System.in);
        game = new AntGame();
        add(game);
        pack();

        setLocationRelativeTo(null);
        setVisible(true);

    }
    //Where the game starts its execution
    public static void main(String[] args) {
        Main main = new Main();
        AntGameConfigurator configurator; //Configures the game on startup
        configurator = new AntGameConfigurator(main.game, main.scanner);
        configurator.configureTheGame();
        main.game.startPathFinding();
    }
}