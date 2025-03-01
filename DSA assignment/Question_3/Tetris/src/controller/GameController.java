import javax.swing.*;
import java.awt.Point;

public class GameController {
    private GameBoard gameBoard;
    private GameView gameView;
    private Timer gameTimer;
    private static final int INITIAL_DELAY = 1000;
    private int currentDelay;

    public GameController() {
        gameBoard = new GameBoard();
        gameView = new GameView(gameBoard, this);
        currentDelay = INITIAL_DELAY;
        initializeGame();
    }

    private void initializeGame() {
        // Initialize block queue
        for (int i = 0; i < 3; i++) {
            gameBoard.getBlockQueue().offer(BlockFactory.createRandomBlock());
        }
        spawnNewBlock();

        // Create game timer
        gameTimer = new Timer(currentDelay, e -> update());
        gameTimer.start();
    }

    private void update() {
        if (!gameBoard.isGameOver()) {
            if (canMoveDown()) {
                gameBoard.getCurrentBlock().moveDown();
            } else {
                gameBoard.placeBlock();
                if (!spawnNewBlock()) {
                    gameOver();
                }
            }
            gameView.updateView();
        }
    }

    private boolean spawnNewBlock() {
        Block nextBlock = gameBoard.getBlockQueue().poll();
        if (nextBlock == null) {
            return false;
        }

        // Add new block to queue
        gameBoard.getBlockQueue().offer(BlockFactory.createRandomBlock());

        // Position the block at the top center
        Point startPosition = new Point(
            (GameBoard.getCols() - nextBlock.getShape()[0].length) / 2,
            -nextBlock.getShape().length
        );
        nextBlock.setPosition(startPosition);
        
        // Check if the new block can be placed
        if (gameBoard.isCollision(nextBlock)) {
            gameBoard.setGameOver(true);
            return false;
        }

        gameBoard.setCurrentBlock(nextBlock);
        return true;
    }

    public void moveLeft() {
        Block block = gameBoard.getCurrentBlock();
        block.moveLeft();
        if (gameBoard.isCollision(block)) {
            block.moveRight();
        }
        gameView.updateView();
    }

    public void moveRight() {
        Block block = gameBoard.getCurrentBlock();
        block.moveRight();
        if (gameBoard.isCollision(block)) {
            block.moveLeft();
        }
        gameView.updateView();
    }

    public void rotate() {
        Block block = gameBoard.getCurrentBlock();
        block.rotate();
        if (gameBoard.isCollision(block)) {
            // Try to adjust position if rotation causes collision
            block.moveLeft();
            if (gameBoard.isCollision(block)) {
                block.moveRight();
                block.moveRight();
                if (gameBoard.isCollision(block)) {
                    block.moveLeft();
                    // Rotate back if no valid position found
                    for (int i = 0; i < 3; i++) {
                        block.rotate();
                    }
                }
            }
        }
        gameView.updateView();
    }

    public void moveDown() {
        if (canMoveDown()) {
            gameBoard.getCurrentBlock().moveDown();
            gameView.updateView();
        }
    }

    public void dropBlock() {
        while (canMoveDown()) {
            gameBoard.getCurrentBlock().moveDown();
        }
        update();
    }

    private boolean canMoveDown() {
        Block block = gameBoard.getCurrentBlock();
        block.moveDown();
        boolean canMove = !gameBoard.isCollision(block);
        block.moveUp(); // Reset position
        return canMove;
    }

    private void gameOver() {
        gameTimer.stop();
        JOptionPane.showMessageDialog(gameView,
            "Game Over!\nFinal Score: " + gameBoard.getScore(),
            "Game Over",
            JOptionPane.INFORMATION_MESSAGE);
    }

    public void startGame() {
        gameView.setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            GameController game = new GameController();
            game.startGame();
        });
    }
}
