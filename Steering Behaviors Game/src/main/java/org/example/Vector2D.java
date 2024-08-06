package org.example;

public class Vector2D {
    public float x; // X coord
    public float y; //Y coord

    // Constructor
    public Vector2D(float x, float y) {
        this.x = x;
        this.y = y;
    }

    // Add another vector to this one
    public void add(Vector2D v) {
        if (v == null) {
            System.out.println("Attempted to add a null Vector2D object.");
            return; // Avoid proceeding with null
        }
        this.x += v.x;
        this.y += v.y;
    }

    // Subtract another vector from this one
    public void subtract(Vector2D v) {
        this.x -= v.x;
        this.y -= v.y;
    }

    public void divide(float v) {
        this.x /= v;
        this.y /= v;
    }

    // Multiply this vector by a scalar
    public void multiply(float scalar) {
        this.x *= scalar;
        this.y *= scalar;
    }

    // Calculate the magnitude (length) of the vector
    public float magnitude() {
        return (float) Math.sqrt(x * x + y * y);
    }

    // Normalize the vector (make it 1 unit in length)
    public void normalize() {
        float mag = magnitude();
        if (mag > 0) {
            multiply(1.0f / mag);
        }
    }

    // Static method for subtracting two vectors
    public static Vector2D subtract(Vector2D v1, Vector2D v2) {
        return new Vector2D(v1.x - v2.x, v1.y - v2.y);
    }
    public void limit(float max) {
        if (magnitude() > max) {
            normalize();
            multiply(max);
        }
    }
    public static float distance(Vector2D a, Vector2D b) {
        float dx = a.x - b.x;
        float dy = a.y - b.y;
        return (float) Math.sqrt(dx * dx + dy * dy);
    }
}