import java.awt.Color;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Stack;

public class GameBoard {
    private Color[][] board;
    private Queue<Block> blockQueue;
    private Stack<Color[]> boardStack;
    private Block currentBlock;
    private int score;
    private boolean gameOver;
    private static final int ROWS = 20;
    private static final int COLS = 10;

    public GameBoard() {
        board = new Color[ROWS][COLS];
        blockQueue = new LinkedList<>();
        boardStack = new Stack<>();
        score = 0;
        gameOver = false;
        initializeBoard();
    }

    private void initializeBoard() {
        for (int i = 0; i < ROWS; i++) {
            for (int j = 0; j < COLS; j++) {
                board[i][j] = Color.BLACK;
            }
            boardStack.push(new Color[COLS]);
        }
    }

    public boolean isCollision(Block block) {
        int[][] shape = block.getShape();
        int x = block.getPosition().x;
        int y = block.getPosition().y;

        for (int i = 0; i < shape.length; i++) {
            for (int j = 0; j < shape[0].length; j++) {
                if (shape[i][j] == 1) {
                    if (x + j < 0 || x + j >= COLS || y + i >= ROWS) {
                        return true;
                    }
                    if (y + i >= 0 && board[y + i][x + j] != Color.BLACK) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public void placeBlock() {
        int[][] shape = currentBlock.getShape();
        int x = currentBlock.getPosition().x;
        int y = currentBlock.getPosition().y;
        Color color = currentBlock.getColor();

        for (int i = 0; i < shape.length; i++) {
            for (int j = 0; j < shape[0].length; j++) {
                if (shape[i][j] == 1 && y + i >= 0) {
                    board[y + i][x + j] = color;
                }
            }
        }
        checkLines();
    }

    private void checkLines() {
        for (int i = ROWS - 1; i >= 0; i--) {
            boolean fullLine = true;
            for (int j = 0; j < COLS; j++) {
                if (board[i][j] == Color.BLACK) {
                    fullLine = false;
                    break;
                }
            }
            if (fullLine) {
                removeLine(i);
                score += 100;
            }
        }
    }

    private void removeLine(int line) {
        for (int i = line; i > 0; i--) {
            System.arraycopy(board[i - 1], 0, board[i], 0, COLS);
        }
        for (int j = 0; j < COLS; j++) {
            board[0][j] = Color.BLACK;
        }
    }

    // Getters and setters
    public Color[][] getBoard() { return board; }
    public Queue<Block> getBlockQueue() { return blockQueue; }
    public Block getCurrentBlock() { return currentBlock; }
    public void setCurrentBlock(Block block) { this.currentBlock = block; }
    public int getScore() { return score; }
    public boolean isGameOver() { return gameOver; }
    public void setGameOver(boolean gameOver) { this.gameOver = gameOver; }
    public static int getRows() { return ROWS; }
    public static int getCols() { return COLS; }
}
