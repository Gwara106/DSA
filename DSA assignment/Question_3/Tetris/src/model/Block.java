import java.awt.Color;
import java.awt.Point;

public class Block {
    private int[][] shape;
    private Color color;
    private Point position;
    private int rotation;

    public Block(int[][] shape, Color color) {
        this.shape = shape;
        this.color = color;
        this.position = new Point(0, 0);
        this.rotation = 0;
    }

    public void rotate() {
        int[][] rotated = new int[shape[0].length][shape.length];
        for (int i = 0; i < shape.length; i++) {
            for (int j = 0; j < shape[0].length; j++) {
                rotated[j][shape.length - 1 - i] = shape[i][j];
            }
        }
        shape = rotated;
        rotation = (rotation + 1) % 4;
    }

    public void moveLeft() {
        position.x--;
    }

    public void moveRight() {
        position.x++;
    }

    public void moveDown() {
        position.y++;
    }

    public void moveUp() {
        position.y--;
    }

    // Getters and setters
    public int[][] getShape() { return shape; }
    public Color getColor() { return color; }
    public Point getPosition() { return position; }
    public void setPosition(Point position) { this.position = position; }
    public int getRotation() { return rotation; }
}
