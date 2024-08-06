/**
 * Purpose: The class is for implementing the minimax algorithm
 * Author: Andrew Whenham
 * AUID: 3469950
 * Date: 2024-03-14
 * Assignment 3
 */
public class MinimaxAlgorithm {
    /**
     * Depth, how many moves to look at into the future.
     */
    private static final int MAX_DEPTH = 4;

    /**
     * Decides the move
     * @param board the board we're playing on
     * @param depth how many moves to look into the future with.
     * @param player the player, meaning what turn B is for Black or Computer and W is for White or Human
     * @return returns the best column to go to.
     */
    public static int decideMove(char[][] board, int depth, char player){
        int bestScore = (player == 'B') ? Integer.MIN_VALUE : Integer.MAX_VALUE;
        int bestColumn = -1;
        for(int column = 0; column < board[0].length; column++) { //Check the column of the very last row
            if(board[0][column] == ' ') {  
                int row = findRowForColumn(board, column);
                board[row][column] = player;
                int score = minimax(board, depth - 1, Integer.MIN_VALUE, Integer.MAX_VALUE, false);
                board[row][column] = ' ';
                if((player == 'B' && score > bestScore) || (player == 'W' && score < bestScore)) {
                    bestScore = score;
                    bestColumn = column;
                }
                System.out.println("Min eval: " + score);
            }

        }
        return bestColumn;
    }

    /**
     *
     * @param board the board state
     * @param depth the amount of moves into the future we want to consider
     * @param alpha the highest value
     * @param beta the lowest value
     * @param isMaximizingPlayer are we the minimizer or maximizer
     * @return returns the score for current board state
     */
    private static int minimax(char[][] board, int depth, int alpha, int beta, boolean isMaximizingPlayer) {
        // If the depth is greater than max_depth we just exit the program because
        // the code hasn't been tested higher than the max set.
        if (depth > MAX_DEPTH) {
            System.exit(0);
        }
        // Base case: return the board's evaluated score if reached the specified depth or the game is over
        if(depth == 0 ) {
            return evaluateBoard(board);
        }

        if(isMaximizingPlayer) {
            System.out.println("Maximizer!");
            // Initialize the maximum evaluation to the lowest possible integer value
            int maxEval = Integer.MIN_VALUE;
            // Iterate through all columns of the board
            for(int column = 0; column < board[0].length; column++) {
                // Check if the top row of the current column is empty, indicating a valid move
                if(board[0][column] == ' ') {
                    // Find the lowest empty row in the current column
                    int row = findRowForColumn(board, column);
                    // Temporarily place the maximizer's chip ('B') in the found position
                    board[row][column] = 'B';
                    // Recursively call minimax for the opponent (minimizing player), decreasing depth by 1
                    int eval = minimax(board, depth - 1, alpha, beta, false);
                    // Undo the move to backtrack
                    board[row][column] = ' ';
                    // Update maxEval with the maximum of itself and the returned evaluation
                    maxEval = Math.max(maxEval, eval);
                    // Update alpha with the maximum of itself and the current max evaluation
                    alpha = Math.max(alpha, eval);
                    // Alpha-beta pruning: break if alpha is greater than or equal to beta
                    if(beta <= alpha)
                        break;
                }
            }
            // Return the maximum evaluation found for this depth
            return maxEval;
        } else {
            System.out.println("Minimizer!");
            // Initialize the minimum evaluation to the highest possible integer value
            int minEval = Integer.MAX_VALUE;
            // Iterate through all columns of the board
            for(int column = 0; column < board[0].length; column++) {
                // Check if the top row of the current column is empty, indicating a valid move
                if(board[0][column] == ' ') {
                    // Find the lowest empty row in the current column
                    int row = findRowForColumn(board, column);
                    // Temporarily place the minimizer's chip ('W') in the found position
                    board[row][column] ='W';
                    // Recursively call minimax for the opponent (maximizing player), decreasing depth by 1
                    int eval = minimax(board, depth - 1, alpha, beta, true);
                    // Undo the move to backtrack
                    board[row][column] = ' ';
                    // Update minEval with the minimum of itself and the returned evaluation
                    minEval = Math.min(minEval, eval);
                    // Update beta with the minimum of itself and the current min evaluation
                    beta = Math.min(beta, eval);
                    // Alpha-beta pruning: break if beta is less than or equal to alpha
                    if(beta <= alpha) break;
                }
            }
            // Return the minimum evaluation found for this depth
            return minEval;
        }
    }

    /**
     *
     * @param board the current board state.
     * @param column the column we want to find a row on.
     * @return row if empty, -1 if not empty.
     */
    private static int findRowForColumn(char[][] board, int column) {

        for (int row = board.length - 1; row >= 0; row--) {
            if (board[row][column] == ' ') {
                return row;
            }
        }
        return -1;
    }

    /**
     *
     * @param board the board we are playing on
     * @return the way we evaluate the board.
     */
    private static int evaluateBoard(char[][] board) {
        int score = 0;
        // Evaluate rows
        for (int row = 0; row < board.length; row++) {
            for (int col = 0; col < board[0].length - 3; col++) {
                int tempScore = evaluateLine(board[row][col], board[row][col + 1], board[row][col + 2], board[row][col + 3]);
                score += tempScore;
            }
        }

        // Evaluate columns
        for (int col = 0; col < board[0].length; col++) {
            for (int row = 0; row < board.length - 3; row++) {
                int tempScore = evaluateLine(board[row][col], board[row + 1][col], board[row + 2][col], board[row + 3][col]);
                score += tempScore;
            }
        }

        // Evaluate positive diagonal
        for (int row = 0; row < board.length - 3; row++) {
            for (int col = 0; col < board[0].length - 3; col++) {
                int tempScore = evaluateLine(board[row][col], board[row + 1][col + 1], board[row + 2][col + 2], board[row + 3][col + 3]);
                score += tempScore;
            }
        }

        // Evaluate negative diagonal
        for (int row = 3; row < board.length; row++) {
            for (int col = 0; col < board[0].length - 3; col++) {
                int tempScore = evaluateLine(board[row][col], board[row - 1][col + 1], board[row - 2][col + 2], board[row - 3][col + 3]);
                score += tempScore;
            }
        }

        return score;
    }


    /**
     *
     * @param c1 column 1
     * @param c2 column 2
     * @param c3 column 3
     * @param c4 column 4
     * @return The score for which we use this in.
     */
    private static int evaluateLine(char c1, char c2, char c3, char c4) {
        int score = 0;
        int bCount = 0;
        int wCount = 0;

        if (c1 == 'B') bCount++;
        if (c2 == 'B') bCount++;
        if (c3 == 'B') bCount++;
        if (c4 == 'B') bCount++;

        if (c1 == 'W') wCount++;
        if (c2 == 'W') wCount++;
        if (c3 == 'W') wCount++;
        if (c4 == 'W') wCount++;

        if (bCount == 4) {
            score += 100;
        } else if (bCount == 3 && wCount == 0) {
            score += 10;
        } else if (bCount == 2 && wCount == 0) {
            score += 1;
        }

        if (wCount == 4) {
            score -= 100;
        } else if (wCount == 3 && bCount == 0) {
            score -= 10;
        } else if (wCount == 2 && bCount == 0) {
            score -= 1;
        }

        return score;
    }

}
