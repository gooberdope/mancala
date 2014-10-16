import java.awt.*;
import java.awt.geom.*;

/**
 * An ellipse shape capable of containing other shapes within itself.
 *
 * @author David Tang, Jonathan Yee, Stefan Schmainta
 */
public class EllipseShape extends ContainingShape {
    /**
     * Constructor that calls the super constructor to initialize the inherited
     * attributes.
     * @param x x-coordinate
     * @param y y-coordinate
     * @param width width of the shape
     */
    public EllipseShape(int x, int y, int width, int height) {
        super(x, y, width, height);
        setShape(new Ellipse2D.Double(getX(), getY(), getWidth(), getHeight()));
    }

    /**
     * @return An ellipse shape.
     */
    public Shape recreateShape() {
        return new Ellipse2D.Double(getX(), getY(), getWidth(), getHeight());
    }
}
