//The terrain enumeration that specifies what type of terrain is at what cell.
public enum Terrain {
    OPEN(1), GRASSLAND(3), SWAMPLAND(4), OBSTACLE(Integer.MAX_VALUE);
    private int cost;

    /**
     * Initialization constructor
     * @param cost Cost off going around or towards it.
     */
    Terrain(int cost) {
        this.cost = cost;
    }

    /**
     *
     * @return getter and setter, so return the cost
     */
    public int getCost() {
        return cost;
    }
}
