package org.example;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Vector;

import static java.lang.Math.abs;

public class Spaceship extends Vehicle {
    private long lastShotTime; //The time the bullet was last shot.
    private long shotCooldown = 500; // Cooldown time in milliseconds

    private Image spaceshipImage; //spaceship image
    private ArrayList<Bullet> bullets; // the array of bullets
    private double angle = 0; //the angle of the spaceship
    public Spaceship(Vector2D position, Dimension size, String imgPath) {
        super(position, size);
        spaceshipImage = new ImageIcon(getClass().getResource(imgPath)).getImage();
        bullets = new ArrayList<>();
    }
    //Direction of the spaceship.
    public void setAimDirection(int mouseX, int mouseY) {
        double deltaX = mouseX - (position.x + size.width / 2.0);
        double deltaY = mouseY - (position.y + size.height / 2.0);
        angle = Math.atan2(deltaY, deltaX);// - Math.PI / 2;

        // Normalize the angle to be within the range of 0 to 2pi
        if (angle < 0) {
            angle += 2 * Math.PI;
        }
    }
    //Update the spaceships position and update the bullet (if the bullet is off the screen then we get rid of it.)
    @Override
    public void update() {
        // Implement spaceship-specific update logic
        super.update(); // Call the Vehicle's update method


        if (this.position.x + size.width >= Main.frame.getWidth()) {
            this.position.x = Main.frame.getWidth() - size.width;
        }


        if (this.position.x <= 0) {
            this.position.x = 0;
        }

        if (this.position.y <= 0) {
            this.position.y = 0;
        }

        if (this.position.y + size.height >= Main.frame.getHeight()) {
            this.position.y = Main.frame.getHeight() - size.height;
        }

        Iterator<Bullet> bulletIterator = bullets.iterator();
        while(bulletIterator.hasNext()) {
            Bullet bullet = bulletIterator.next();
            bullet.update();
            if(bulletIsOffScreen(bullet)) {
                bulletIterator.remove();
            }
        }
    }
    //Do the render of the bullet and spaceship.
    @Override
    public void render(Graphics g) {
        super.render(g);
        Graphics2D g2d = (Graphics2D) g.create();
        float centerX = 0;
        float centerY = 0;
        // Rotate the spaceship around its center
        centerX = position.x + size.width / 2;
        centerY = position.y + size.height / 2;
        g2d.rotate(angle + Math.PI / 2, centerX, centerY);
        g2d.drawImage(spaceshipImage, (int) position.x, (int) position.y, size.width, size.height, null);

        g2d.dispose(); // Dispose of the graphics copy to release resources

        for (Bullet bullet : getActiveBullets()) {
            bullet.render(g);
        }
    }
    //Shoot bullets
    public void shoot() {
        long currentTime = System.currentTimeMillis();
        if (currentTime - lastShotTime >= shotCooldown) {
            // Calculate bullet's velocity based on the spaceship's angle
            float bulletSpeed = 5; // Adjust bullet speed as needed
            Vector2D bulletVelocity = new Vector2D((float)Math.cos(angle) * bulletSpeed, (float)Math.sin(angle) * bulletSpeed);

            // Adjust bullet's initial position to be centered on the spaceship
            Bullet newBullet = new Bullet(new Vector2D(position.x + size.width / 2 - 5, position.y + size.height / 2 - 5), new Dimension(10, 10), bulletVelocity);

            newBullet.velocity = bulletVelocity;
            bullets.add(newBullet);
            lastShotTime = currentTime;
        }
    }
    //Detects whether a bullet is off the screen or not
    private boolean bulletIsOffScreen(Bullet bullet) {
        return bullet.position.y < 0 || bullet.position.y > Main.frame.getHeight() ||
                bullet.position.x < 0 || bullet.position.x > Main.frame.getWidth();
    }

    public void moveLeft() {
        velocity.x = -5;
    }
    public void moveRight() {
        velocity.x = 5;
    }
    public void moveUp(){
        velocity.y -= 5;
    }
    public void moveDown() {
        velocity.y += 5;
    }
    public void stopMovement() {
        velocity.x = 0;
        velocity.y =0;
    }
    public ArrayList<Bullet> getActiveBullets() {
        return bullets;
    }

}
