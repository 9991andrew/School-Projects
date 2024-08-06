package org.example;

import java.awt.*;

public class Vehicle extends GameObject{
    private static final float maxSpeed = 16; //max speed of the vehicle
    private float maxForce = 1; //Max force of the vehicle for behaviors


    //Initializatian constructor that calls gameobject constructor and then initialize both the velocity and acceleration to zero.
    public Vehicle(Vector2D position, Dimension size) {
        super(position, size);
        this.velocity = new Vector2D(0,0);
        this.acceleration = new Vector2D(0,0);
    }
    //Seek out the player (target) using some vectors.
    public Vector2D seek(Vector2D target) {
        Vector2D desired = Vector2D.subtract(target, position); // Desired vector
        desired.normalize(); // Normalize to get the direction
        desired.multiply(maxSpeed); // Scale to maximum speed

        Vector2D steer = Vector2D.subtract(desired, velocity); // Steering = Desired minus Velocity

        steer.limit(maxForce); // Limit the steering force
        return steer;
    }
    //Literally do the same thing as above (seek) but reverse it.
    public Vector2D flee(Vector2D target) {
        Vector2D desired = Vector2D.subtract(position, target); // Opposite direction to seek
        desired.normalize();
        desired.multiply(maxSpeed);

        Vector2D steer = Vector2D.subtract(desired, velocity);
        steer.limit(maxForce);
        return steer;
    }
    //Same thing as seek and flee but, we reduce the speed.
    public Vector2D arrive(Vector2D target) {
        float slowingRadius = 5.0f;
        Vector2D desired = Vector2D.subtract(target, position);
        float distance = desired.magnitude();
        desired.normalize();

        if (distance < slowingRadius) { // slowingRadius is the distance at which it starts slowing down
            float m = map(distance, 0, slowingRadius, 0, maxSpeed);
            desired.multiply(m);
        } else {
            desired.multiply(maxSpeed);
        }

        Vector2D steer = Vector2D.subtract(desired, velocity);
        steer.limit(maxForce);
        return steer;
    }

    // Utility method to map a value from one range to another
    private float map(float value, float start1, float stop1, float start2, float stop2) {
        return start2 + (stop2 - start2) * ((value - start1) / (stop1 - start1));
    }
    //Render the vehicle
    @Override
    public void render(Graphics g) {
        g.setColor(Color.RED);
        g.fillOval((int)position.x, (int)position.y, size.width, size.height);
    }
    //Update the vehicle
    @Override
    public void update() {
            super.update();
            // Apply acceleration to velocity
            velocity.add(acceleration);
            // Apply velocity to position
            position.add(velocity);
            velocity.limit(maxForce);
            // Reset acceleration for the next frame
            acceleration.multiply(0);

    }

}
