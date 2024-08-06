
/*
    Author: Andrew Whenham
    Purpose: Class represents the cell for which the poison, food, water will be located as well as the ant. And empty cells
    AUID: 3469950
 */
public class Cell {
    private final int x, y; // The coordinates of the cell in the grid
    private char type; // Represents the type of cell (' ' for empty, 'F' for food, 'W' for water, 'P' for poison)

    private boolean hasFood;
    private boolean hasWater;
    private boolean hasPoison;

    public Cell(int x, int y, char type) {
        this.x = x;
        this.y = y;
        this.type = type;

        this.hasFood = false;
        this.hasWater = false;
        this.hasPoison = false;
    }

    // Getters and Setters
    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public char getType() {
        return type;
    }

    public void setType(char type) {
        this.type = type;
        switch(type) {
            case 'F':
                this.setHasFood(true);
                this.setHasWater(false);
                this.setHasPoison(false);
                break;
            case 'W':
                this.setHasWater(true);
                this.setHasFood(false);
                this.setHasPoison(false);
                break;
            case 'P':
                this.setHasPoison(true);
                this.setHasWater(false);
                this.setHasFood(false);
                break;
            default:
                this.setHasFood(false);
                this.setHasWater(false);
                this.setHasPoison(false);
        }

    }

    public boolean isHasFood() {
        return hasFood;
    }

    public void setHasFood(boolean hasFood) {
        this.hasFood = hasFood;
    }

    public boolean isHasWater() {
        return hasWater;
    }
    public void setHasWater(boolean hasWater) {
        this.hasWater = hasWater;
    }
    public void clear() {
        this.hasWater = false;
        this.hasFood = false;
    }

    public boolean isHasPoison() {
        return hasPoison;
    }
    public void setHasPoison(boolean poison) {
        this.hasPoison = poison;
    }
}