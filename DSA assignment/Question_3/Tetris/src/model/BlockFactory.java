import java.awt.Color;
import java.util.Random;

public class BlockFactory {
    private static final Random random = new Random();

    private static final int[][][] SHAPES = {
        // I-Block
        {{1, 1, 1, 1}},
        // O-Block
        {{1, 1},
         {1, 1}},
        // T-Block
        {{0, 1, 0},
         {1, 1, 1}},
        // L-Block
        {{1, 0},
         {1, 0},
         {1, 1}},
        // J-Block
        {{0, 1},
         {0, 1},
         {1, 1}},
        // S-Block
        {{0, 1, 1},
         {1, 1, 0}},
        // Z-Block
        {{1, 1, 0},
         {0, 1, 1}}
    };

    private static final Color[] COLORS = {
        Color.CYAN,    // I-Block
        Color.YELLOW,  // O-Block
        Color.MAGENTA, // T-Block
        Color.ORANGE,  // L-Block
        Color.BLUE,    // J-Block
        Color.GREEN,   // S-Block
        Color.RED      // Z-Block
    };

    public static Block createRandomBlock() {
        int index = random.nextInt(SHAPES.length);
        return new Block(SHAPES[index], COLORS[index]);
    }
}
