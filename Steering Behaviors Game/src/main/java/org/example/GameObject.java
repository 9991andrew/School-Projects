package org.example;

import java.awt.*;

public abstract class GameObject {
    protected Vector2D position; //Position of the gameobject
    protected Vector2D velocity; //Velocity of the gameobject
    protected Vector2D acceleration; //Acceleration of the gameobject.
    protected Dimension size; //The size of the game object.
    //The gameobject constructor that initializes the position and size.
    public GameObject(Vector2D position, Dimension size) {
        this.position = position;
        this.size =  size;
    }
    //Update the gameobject so that it can move.
    public void update() {
        velocity.add(acceleration);
        position.add(velocity);
        acceleration.multiply(0); // Reset acceleration
    }
    //Abstract render method we will use for all gameobjects.
    public abstract void render(Graphics g);
}
