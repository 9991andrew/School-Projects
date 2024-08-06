package org.example;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class Enemy extends Vehicle {
    private static final float DODGE_CHANCE = 0.6f; // Chance for the enemy to dodge a bullet
    private final int SEEK = 0; // Constant representing the 'seek' behavior
    private final int FLEE = 1; // Constant representing the 'flee' behavior
    private final int ARRIVE = 2; // Constant representing the 'arrive' behavior

    private Image enemyImage; // Image representing the enemy

    private Spaceship player; // Reference to the player's spaceship

    private int currentBehavior; // Current behavior of the enemy
    private Vector2D targetPosition; // Target position for certain behaviors
    private float fleeDistance = 100; // Distance within which the enemy will start fleeing
    private boolean defeated; // Flag to indicate if the enemy is defeated
    private int health; // Health of the enemy

    // Constructor
    public Enemy(Vector2D position, Dimension size, int health, Spaceship player, String img) {
        super(position, size);
        this.player = player;
        this.defeated = false;
        this.health = health;
        enemyImage = new ImageIcon(getClass().getResource(img)).getImage();
    }

    // Render method to draw the enemy
    @Override
    public void render(Graphics g) {
        if (enemyImage != null) {
            g.drawImage(enemyImage, (int)position.x, (int)position.y, size.width, size.height, null);
        } else {
            g.setColor(Color.RED);
            g.fillRect((int)position.x, (int)position.y, size.width, size.height);
        }
    }

    // Update method to update the enemy's behavior and position
    public void update(ArrayList<Bullet> bullets, ArrayList<Enemy> enemies) {
        Vector2D closestBulletPos = findClosestBulletPos(bullets);
        float distanceToPlayer = Vector2D.distance(this.position, player.position);

        Vector2D sepForce = separation(enemies);
        Vector2D steeringForce = new Vector2D(0,0);

        // Determine the current behavior based on the situation
        if (closestBulletPos != null && Vector2D.distance(this.position, closestBulletPos) < fleeDistance) {
            currentBehavior = FLEE;
        } else if (distanceToPlayer > 250) {
            currentBehavior = SEEK;
        } else {
            currentBehavior = ARRIVE;
            targetPosition = player.position;
        }

        // Apply the behavior
        switch (currentBehavior) {
            case SEEK:
                steeringForce = seek(player.position);
                break;
            case ARRIVE:
                steeringForce = arrive(targetPosition);
                break;
            case FLEE:
                steeringForce = flee(closestBulletPos);
                break;
        }
        steeringForce.add(sepForce);
        acceleration.add(steeringForce);
        super.update();
    }

    // Separation method to keep a distance from other enemies
    private Vector2D separation(ArrayList<Enemy> enemies) {
        Vector2D steering = new Vector2D(0, 0);
        int count = 0;
        final float desiredSeparation = 35;
        for (Enemy other : enemies) {
            float dist = Vector2D.distance(this.position, other.position);
            if (dist > 0 && dist < desiredSeparation) {
                Vector2D diff = Vector2D.subtract(this.position, other.position);
                diff.normalize();
                diff.divide(dist);
                steering.add(diff);
                count++;
            }
        }
        if (count > 0) {
            steering.divide((float) count);
        }
        if(steering.magnitude() > 0 ) {
            steering.normalize();
            steering.multiply(5);
            steering.subtract(this.velocity);
            steering.limit(5);
        }
        return steering;
    }

    // Method to find the closest bullet position
    private Vector2D findClosestBulletPos(ArrayList<Bullet> bullets) {
        Vector2D closestBulletPos = null;
        float minDistance = Float.MAX_VALUE;
        for (Bullet bullet : bullets) {
            float distance = Vector2D.distance(this.position, bullet.position);
            if (distance < minDistance) {
                minDistance = distance;
                closestBulletPos = bullet.position;
            }
        }

        // Random chance to dodge the bullet
        if (closestBulletPos != null && Math.random() < DODGE_CHANCE) {
            return closestBulletPos;
        }

        return null; // Returns null if no bullets are close enough to consider dodging
    }

    // Method to check if the enemy is defeated
    public boolean isDefeated() {
        return defeated = health <= 0;
    }

    // Method to apply damage to the enemy
    public void hit(int damage) {
        health -= damage;
    }
}
