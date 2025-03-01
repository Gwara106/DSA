import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class GameView extends JFrame {
    private GameBoard gameBoard;
    private GameController controller;
    private BoardPanel boardPanel;
    private PreviewPanel previewPanel;
    private JLabel scoreLabel;
    private static final int CELL_SIZE = 30;

    public GameView(GameBoard gameBoard, GameController controller) {
        this.gameBoard = gameBoard;
        this.controller = controller;
        
        setTitle("Tetris");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        
        initializeComponents();
        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                switch (e.getKeyCode()) {
                    case KeyEvent.VK_LEFT:
                        controller.moveLeft();
                        break;
                    case KeyEvent.VK_RIGHT:
                        controller.moveRight();
                        break;
                    case KeyEvent.VK_UP:
                        controller.rotate();
                        break;
                    case KeyEvent.VK_DOWN:
                        controller.moveDown();
                        break;
                    case KeyEvent.VK_SPACE:
                        controller.dropBlock();
                        break;
                }
            }
        });

        setFocusable(true);
        pack();
        setLocationRelativeTo(null);
    }

    private void initializeComponents() {
        // Create game board panel
        boardPanel = new BoardPanel();
        add(boardPanel, BorderLayout.CENTER);

        // Create side panel
        JPanel sidePanel = new JPanel();
        sidePanel.setLayout(new BoxLayout(sidePanel, BoxLayout.Y_AXIS));
        sidePanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Add preview panel
        previewPanel = new PreviewPanel();
        sidePanel.add(previewPanel);
        sidePanel.add(Box.createVerticalStrut(20));

        // Add score panel
        scoreLabel = new JLabel("Score: 0");
        scoreLabel.setFont(new Font("Arial", Font.BOLD, 20));
        sidePanel.add(scoreLabel);

        add(sidePanel, BorderLayout.EAST);
    }

    public void updateView() {
        boardPanel.repaint();
        previewPanel.repaint();
        scoreLabel.setText("Score: " + gameBoard.getScore());
    }

    private class BoardPanel extends JPanel {
        public BoardPanel() {
            setPreferredSize(new Dimension(
                GameBoard.getCols() * CELL_SIZE,
                GameBoard.getRows() * CELL_SIZE
            ));
            setBackground(Color.BLACK);
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            
            // Draw board
            Color[][] board = gameBoard.getBoard();
            for (int row = 0; row < GameBoard.getRows(); row++) {
                for (int col = 0; col < GameBoard.getCols(); col++) {
                    drawCell(g, col * CELL_SIZE, row * CELL_SIZE, board[row][col]);
                }
            }

            // Draw current block
            Block currentBlock = gameBoard.getCurrentBlock();
            if (currentBlock != null) {
                int[][] shape = currentBlock.getShape();
                Point pos = currentBlock.getPosition();
                for (int row = 0; row < shape.length; row++) {
                    for (int col = 0; col < shape[0].length; col++) {
                        if (shape[row][col] == 1) {
                            drawCell(g, (pos.x + col) * CELL_SIZE,
                                      (pos.y + row) * CELL_SIZE,
                                      currentBlock.getColor());
                        }
                    }
                }
            }
        }
    }

    private class PreviewPanel extends JPanel {
        public PreviewPanel() {
            setPreferredSize(new Dimension(4 * CELL_SIZE, 4 * CELL_SIZE));
            setBackground(Color.DARK_GRAY);
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Block nextBlock = gameBoard.getBlockQueue().peek();
            if (nextBlock != null) {
                int[][] shape = nextBlock.getShape();
                for (int row = 0; row < shape.length; row++) {
                    for (int col = 0; col < shape[0].length; col++) {
                        if (shape[row][col] == 1) {
                            drawCell(g, col * CELL_SIZE + CELL_SIZE,
                                      row * CELL_SIZE + CELL_SIZE,
                                      nextBlock.getColor());
                        }
                    }
                }
            }
        }
    }

    private void drawCell(Graphics g, int x, int y, Color color) {
        g.setColor(color);
        g.fillRect(x, y, CELL_SIZE - 1, CELL_SIZE - 1);
        g.setColor(color.brighter());
        g.drawLine(x, y, x + CELL_SIZE - 1, y);
        g.drawLine(x, y, x, y + CELL_SIZE - 1);
        g.setColor(color.darker());
        g.drawLine(x + CELL_SIZE - 1, y, x + CELL_SIZE - 1, y + CELL_SIZE - 1);
        g.drawLine(x, y + CELL_SIZE - 1, x + CELL_SIZE - 1, y + CELL_SIZE - 1);
    }
}
