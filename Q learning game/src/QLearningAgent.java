/**
 * Purpose: The agent or player (robot)
 * Author: Andrew Whenham
 * AUID: 3469950
 * Date: 2024-03-14
 * Assignment 3
 */
public class QLearningAgent {
    private final double alpha = 0.5; // Learning rate
    private final double gamma = 0.9; // Discount factor
    private final double epsilon = 0.4; // Exploration rate
    private int gridSize; //The size of the grid, gridSize x gridSize.
    private final int actionCount = 4; // up, down, left, right
    private double[][][] qTable; // State-action values

    /**
     *
     * @param gridSize the size of the grid
     */

    public QLearningAgent(int gridSize){
        this.gridSize = gridSize;
        this.qTable= new double[gridSize][gridSize][actionCount];
        initQTable();
    }

    /**
     * Initializes the Q table.
     */
    private void initQTable() {
        for (int i = 0; i < gridSize; i++) {
            for(int j =0;j<gridSize; j++){
                for(int k = 0; k < actionCount; k++) {
                    qTable[i][j][k] = Math.random()*0.01;
                }
            }
        }
    }

    /**
     *
     * @param x coordinate to choose the action from
     * @param y coordinate to choose the action from
     * @return the action chosen based on the random value and epsilon
     */
    public int chooseAction(int x, int y) {
        if (Math.random() < epsilon) {
            return (int) (Math.random() * actionCount);

        }else{
            return bestAction(x,y);
        }
    }

    /**
     *
     * @param x The x coordinate of the action we are trying to determine the best action with
     * @param y The y coordinate of the action we are trying to determine the best action with
     * @return returns the best action in this case
     */
    private int bestAction(int x, int y) {
        double maxVal = Double.MIN_VALUE;
        int bestAction = 0;
        for(int action = 0; action<actionCount;action++) {
            if(qTable[x][y][action] > maxVal) {
                maxVal = qTable[x][y][action];
                bestAction =action;
            }
        }
        return bestAction;
    }

    /**
     *
     * @param stateX
     * @param stateY
     * @param action
     * @param reward
     * @param nextStateX
     * @param nextStateY
     */
    public void updateQTable(int stateX, int stateY, int action, double reward, int nextStateX, int nextStateY) {
        double maxFutureQ = Double.MIN_VALUE;
        for(int i = 0; i < actionCount; i++) {
            if(qTable[nextStateX][nextStateY][i] > maxFutureQ) {
                maxFutureQ =qTable[nextStateX][nextStateY][i];
            }
        }
        double update = reward + gamma * maxFutureQ;
        qTable[stateX][stateY][action] = (1-alpha) *qTable[stateX][stateY][action] + alpha * update;
    }

}
