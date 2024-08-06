/*
*
* Purpose: The cell we have terrain and the ant in .
* Author: Andrew Whenham
* */
public class Cell {
    int x, y;
    Terrain terrain;
    Cell parent;
    int hCost;
    int gCost;
    int fCost;
    boolean hasAnt;

    /**
     *
     * @param x - The x coord of the cell
     * @param y - The y coord of the cell
     * @param terrain - the terrain enumeration
     */
    public Cell(int x, int y, Terrain terrain) {
        this.x = x;
        this.y = y;
        this.terrain = terrain;
        this.hasAnt = false;
    }
    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }

    public void setTerrain(Terrain terrain) {
        this.terrain = terrain;
    }

    public void setParent(Cell parent) {
        this.parent = parent;
    }

    public void setgCost(int gCost) {
        this.gCost = gCost;
    }

    public void sethCost(int hCost) {
        this.hCost = hCost;
    }

    public void setfCost(int fCost) {
        this.fCost = fCost;
    }



    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public Terrain getTerrain() {
        return terrain;
    }

    public Cell getParent() {
        return parent;
    }

    public int getgCost() {
        return gCost;
    }

    public int gethCost() {
        return hCost;
    }

    public int getfCost() {
        return fCost;
    }
    public boolean getHasAnt() {
        return this.hasAnt;
    }
    public void setHasAnt(boolean b) {
        this.hasAnt = b;
    }
}
