import java.awt.*;
import java.awt.geom.*;

/**
 * A rectangle shape capable of containing other shapes within itself.
 *
 * @author David Tang, Jonathan Yee, Stefan Schmainta
 */
public class RectangleShape extends ContainingShape {
    /**
     * Constructor that calls the super constructor to initialize the inherited
     * attributes.
     * @param x x-coordinate
     * @param y y-coordinate
     * @param width width of the shape
     */
    public RectangleShape(int x, int y, int width, int height) {
        super(x, y, width, height);
        setShape(recreateShape());
    }

    /**
     * @return A rectangle containing shape.
     */
    public Shape recreateShape() {
        return new Rectangle2D.Double(getX(), getY(), getWidth(), getHeight());
    }
}
