import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
/**
 * Purpose: The class that is responsible for the gameboard, add its it to the panel.
 * Author: Andrew Whenham
 * AUID: 3469950
 * Date: 2024-03-14
 * Assignment 3
 */
public class GameBoardPanel extends JPanel {
    private final int ROWS = 6; //The number of Rows
    private final int COLUMNS = 7; // The number of Columns
    private final int CELL_SIZE = 100; // Pixel size of each cell
    private final Color GRID_COLOR = new Color(0,0,255); // Blue color for the grid
    private final Color EMPTY_COLOR = new Color(200,200,200); // Light gray for empty cells
    private char[][] board; // The game board
    private final GameController gameController; //The controller (where we make the moves).
    private boolean won = false; //Whether we have a winner or not.

    /**
     * Construct the game board
     */
    public GameBoardPanel() {
        this.setPreferredSize(new Dimension(COLUMNS * CELL_SIZE, ROWS * CELL_SIZE));
        gameController = new GameController(this);
        this.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int col = e.getX() / CELL_SIZE;
                if(!won){
                if(gameController.makeHumanMove(col)) {
                    repaint();
                }
                }

            }
        });
        this.board = new char[ROWS][COLUMNS];
        initializeBoard();
    }

    /**
     * Initialize the board.
     */
    public void initializeBoard(){
        for(int i = 0; i < ROWS; i++) {
            for (int j = 0; j < COLUMNS; j++) {
                board[i][j] = ' ';
            }
        }
    }

    /**
     *
     * @param lastRow The last row we are on
     * @param lastCol The last column we are on
     * @param player The player at which we are checking for a winning move.
     * @return True/False depending on if there's winner or not.
     */
    private boolean isWinningMove(int lastRow, int lastCol, char player) {
        // Check horizontal
        for (int col = 0; col < COLUMNS - 3; col++) {
            if (board[lastRow][col] == player && board[lastRow][col+1] == player &&
                    board[lastRow][col+2] == player && board[lastRow][col+3] == player) {
                return true;
            }
        }

        // Check vertical
        for (int row = 0; row < ROWS - 3; row++) {
            if (board[row][lastCol] == player && board[row+1][lastCol] == player &&
                    board[row+2][lastCol] == player && board[row+3][lastCol] == player) {
                return true;
            }
        }

        // Check diagonal (forward)
        for (int row = 3; row < ROWS; row++) {
            for (int col = 0; col < COLUMNS - 3; col++) {
                if (board[row][col] == player && board[row-1][col+1] == player &&
                        board[row-2][col+2] == player && board[row-3][col+3] == player) {
                    return true;
                }
            }
        }

        // Check diagonal (backward)
        for (int row = 3; row < ROWS; row++) {
            for (int col = 3; col < COLUMNS; col++) {
                if (board[row][col] == player && board[row-1][col-1] == player &&
                        board[row-2][col-2] == player && board[row-3][col-3] == player) {
                    return true;
                }
            }
        }

        return false; // No winning move found
    }

    /**
     *
     * @param g the <code>Graphics</code> object to protect
     */
    @Override
    public void paintComponent(Graphics g){
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;

        // Draw the board
        for (int i = 0; i < ROWS; i++) {
            for (int j = 0; j < COLUMNS; j++) {
                g2d.setColor(EMPTY_COLOR);
                g2d.fillRect(j * CELL_SIZE, i * CELL_SIZE, CELL_SIZE, CELL_SIZE);
                g2d.setColor(GRID_COLOR);
                g2d.drawRect(j * CELL_SIZE, i * CELL_SIZE, CELL_SIZE, CELL_SIZE);

                // Draw chips
                if (board[i][j] != ' ') {
                    if (board[i][j] == 'W') { // White chip for human player
                        g2d.setColor(Color.WHITE);
                    } else { // Black chip for computer player
                        g2d.setColor(Color.BLACK);
                    }
                    g2d.fillOval(j * CELL_SIZE + 10, i * CELL_SIZE + 10, CELL_SIZE - 20, CELL_SIZE - 20);
                }
                repaint();


                if(isWinningMove(i,j,'W')) {
                    repaint();
                    won=true;
                    g2d.setColor(Color.WHITE);
                    g.drawString("White won!", getWidth()/2, getHeight()/2);
                } else if(isWinningMove(i,j,'B')){
                    repaint();
                    won=true;
                    g2d.setColor(Color.WHITE);
                    g.drawString("Black won!", getWidth()/2, getHeight()/2);
                }
            }

        }
    }

    /**
     *
     * @param newBoard set the new board state
     */

    public void setBoard(char[][] newBoard){
        this.board = newBoard;
        repaint();
    }

    /**
     *
     * @return the current board state
     */
    public char[][] getBoard() {
        return board;
    }
}
