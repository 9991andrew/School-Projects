
package org.example;

/*
*Author: Andrew Whenham
*AUID: 3469950
*Course: Comp 452
* Assignment #1: Steering Behaviours Implementation
* */
import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Iterator;

public class Main implements Runnable{
    public static JFrame frame; //JFrame the stuff will be drawn on
    private static final int MAX_WAVES = 3; //The maxn umber of waves.
    private boolean running = false; //Whether the game is running or not.
    private Graphics g2d; // The graphics context
    private BufferedImage doublebuffer; //The double buffer
    private Spaceship spaceship; //The spaceship instance

    private boolean[] keys= new boolean[256]; //array of booleans for the way for which we will be tracking what key is pressed.
    private Enemy enemy; //The enemy
    private ArrayList<Enemy> enemies; //The array of enemies
    private int enemiesKilled = 0; //The enemies we have killed
    private int currentWave = 0; //The current wave that we are on
    private String waveMessage = ""; //The wave message.

    //Initialize everything
    public Main() {
        frame = new JFrame("Steering Behaviors Game");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setFocusable(true);
        frame.setSize(800,600);
        frame.setPreferredSize(new Dimension(800, 600));
        frame.setVisible(true);
        frame.setResizable(false);

        spaceship = new Spaceship(new Vector2D(frame.getWidth() / 2, frame.getHeight() - 50), new Dimension(25,25) , "/player.png");
        doublebuffer = new BufferedImage(800, 600, BufferedImage.TYPE_INT_ARGB);
        g2d = doublebuffer.getGraphics();
        enemies = new ArrayList<>();

        frame.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                keys[e.getKeyCode()] = true;
            }

            @Override
            public void keyReleased(KeyEvent e) {
                keys[e.getKeyCode()] = false;
            }
        });
        frame.addMouseMotionListener(new MouseAdapter() {
            @Override
            public void mouseMoved(MouseEvent e) {
                spaceship.setAimDirection(e.getX(), e.getY());
            }
        });
        frame.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                spaceship.shoot();
            }
        });


    }
    //Generate a random point that the enemies will spawn on
    private Vector2D generateRandomPoint() {
        double x = Math.random() * (frame.getWidth() / 2);
        double y = Math.random() * (frame.getHeight() / 2);
        return new Vector2D((float) x,(float) y);
    }

    //Start the next wave.
    private void startNextWave() {
        if (currentWave < MAX_WAVES) {
            currentWave++;
            spawnEnemiesForWave(currentWave);
        }

    }

    //Spawns the enemies for the wave we're on
    private void spawnEnemiesForWave(int waveNumber) {
        int numberOfEnemies = calculateEnemiesForWave(waveNumber);
        for (int i = 0; i < numberOfEnemies; i++) {
            Vector2D position;
            do {
                position = this.generateRandomPoint();
            }while (isTooCloseToOtherEnemies(position));
            enemies.add(new Enemy(new Vector2D(position.x, position.y), new Dimension(25,25), 3, spaceship,"/alien.png"));
        }
    }
    //Detects whether a player's bullet is too close to an enemy.
    private boolean isTooCloseToOtherEnemies(Vector2D position) {
        for (Enemy enemy : enemies) {
            if (Vector2D.distance(position, enemy.position) >= 150) {
                return true;
            }
        }
        return false;
    }
    //Calculates the number of enemies that need to spawn.
    private int calculateEnemiesForWave(int waveNumber) {
        return waveNumber * 5;
    }
    //Check for collision using below "bulletcollideswithenemy" function.
    private void checkCollisions() {
        Iterator<Bullet> bulletIterator = spaceship.getActiveBullets().iterator();
        while(bulletIterator.hasNext()) {
            Bullet bullet = bulletIterator.next();
            for (Enemy enemy : enemies)  {
                if(bulletCollidesWithEnemy(bullet, enemy)) {
                    enemy.hit(1);
                    bulletIterator.remove();
                    break;
                }
            }

        }
    }
    //Detect collision between bullet and enemy

    private boolean bulletCollidesWithEnemy(Bullet bullet, Enemy enemy) {
        // Check if bullet's rectangle intersects with enemy's rectangle
        Rectangle bulletRect = new Rectangle((int)bullet.position.x, (int)bullet.position.y, bullet.size.width, bullet.size.height);
        Rectangle enemyRect = new Rectangle((int)enemy.position.x, (int)enemy.position.y, enemy.size.width, enemy.size.height);
        return bulletRect.intersects(enemyRect);
    }
    //Handle the input
    private void handleInput() {
        if (keys[KeyEvent.VK_LEFT]) {
            spaceship.moveLeft();
        } else if (keys[KeyEvent.VK_RIGHT]) {
            spaceship.moveRight();
        }else if(keys[KeyEvent.VK_UP]) {
            spaceship.moveUp();
        }else if(keys[KeyEvent.VK_DOWN]) {
            spaceship.moveDown();
        } else {
            spaceship.stopMovement();
        }
    }
    //STart the game
    public synchronized void start() {
        running = true;
        new Thread(this).start();
    }
    //Stop the game
    public synchronized void stop() {
        running = false;
    }

    //Render all the stuff in the game.
    private void render() {
        g2d.clearRect(0, 0, 800, 600);
        spaceship.render(g2d);

        // Always display the score at a visible position
        g2d.setColor(Color.WHITE);
        g2d.setFont(new Font("Arial", Font.BOLD, 20));
        g2d.drawString("Enemies Killed: " + enemiesKilled, 300, 50); // Adjusted position


        for (Enemy enemy : enemies) {
            enemy.render(g2d);
        }

        if (!this.waveMessage.isEmpty()) {
            g2d.drawString(waveMessage, frame.getWidth() /2, frame.getHeight()/2);
        }

        Graphics g = frame.getGraphics();
        g.drawImage(doublebuffer, 0, 0, null);
        g.dispose();

    }

    private void update() {
            checkWaveTransition();

        ArrayList<Enemy> remove_enemies = new ArrayList<>();
        spaceship.update();

        for (Enemy enemy : enemies) {
            enemy.update(this.spaceship.getActiveBullets(), this.enemies);
            if (enemy.isDefeated()) {
                remove_enemies.add(enemy);
                enemiesKilled++;
            }
        }
        checkCollisions();
        enemies.removeAll(remove_enemies);

    }
    //Check to see if we should spawn more enemies
    private void checkWaveTransition() {

        if (enemies.isEmpty() &&  currentWave < MAX_WAVES) {
            startNextWave();
        } else if (enemies.isEmpty() && currentWave >= MAX_WAVES) {
            waveMessage = "All waves completed!";
        }
    }
    //Run the game
    @Override
    public void run() {
        long lastTime = System.nanoTime();
        final double amountOfTicks = 60.0;
        double ns = 1000000000 / amountOfTicks;
        double delta = 0;
        long time = System.currentTimeMillis();
        while (running ) {
            long now = System.nanoTime();
            delta += (now - lastTime) / ns;
            lastTime = now;
            if (delta >= 1) {
                    handleInput();
                    update();
                    render();
                    delta--;

            }
        }
    }
    //The main method where the game first starts running.
    public static void main(String[] args) {
        Main game = new Main();
        game.start();


    }

}
