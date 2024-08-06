package org.example;

import javax.swing.*;
import java.awt.*;

public class Bullet extends GameObject {
    private Image bulletImage; //Bullet image.
    //Initialize all the bullet stuff since it is a gameobject. We have velocity, aceleration, position and size.
    public Bullet(Vector2D pos, Dimension size, Vector2D vel) {
        super(pos, size);
        this.position = pos;
        this.velocity = new Vector2D(0, -10);
        this.acceleration = new Vector2D(0,0);
        ImageIcon icon = new ImageIcon(getClass().getResource("/shot.png"));
        bulletImage = icon.getImage();
    }
    //Update the bullet
    @Override
    public void update() {
        super.update();
    }
    //Render the bullet
    @Override
    public void render(Graphics g) {
        if (bulletImage != null) {
            g.drawImage(bulletImage, (int)position.x, (int)position.y, size.width, size.height, null);
        } else {
            g.setColor(Color.WHITE);
            g.fillOval((int)position.x, (int)position.y, size.width, size.height);
        }
    }
}
