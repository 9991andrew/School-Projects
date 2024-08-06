/**
 * Purpose: Controls the game
 * Author: Andrew Whenham
 * AUID: 3469950
 * Date: 2024-03-14
 * Assignment 3
 */
public class GameController {
    private final int ROWS = 6; // The number of rows.
    private final int COLUMNS = 7; //The number of columns (we aren't using it but still have it here just in case)
    private final GameBoardPanel gameBoardPanel; //The game board we are drawing stuff on
    private char currPlayer; //The turn we are on W or B.

    /**
     *
     * @param gameBoardPanel JPanel instance to render the GameBoard on.
     */
    public GameController(GameBoardPanel gameBoardPanel) {
        this.gameBoardPanel =  gameBoardPanel;
        this.currPlayer = 'W';
    }

    /**
     *
     * @param column the column we clicked in.
     * @return the move.
     */
    public boolean makeHumanMove(int column) {

        boolean validMove = makeMove(column, currPlayer);
        if(validMove) {
            currPlayer = 'B';
            makeComputerMove();
        }
        return validMove;
    }


    //Make a move for the computer which means that we call decide move.
    private void makeComputerMove() {
        int selectedColumn = MinimaxAlgorithm.decideMove(gameBoardPanel.getBoard(), 1, 'B');
        makeMove(selectedColumn, 'B');
        currPlayer = 'W';
    }

    /**
     *
     * @param column the column we are going to
     * @param player the move (Computer (B) or Player (W)).
     * @return true if we made the move, and false otherwise. This will probably always return true.
     */
    private boolean makeMove(int column, char player) {
        char[][] board = gameBoardPanel.getBoard();
        for(int i = ROWS - 1; i >= 0; i--) {
            if(board[i][column] == ' ') {
                board[i][column] = player;
                gameBoardPanel.setBoard(board);
                return true;

            }
        }
        return false;
    }
}

