import java.awt.*;
import java.awt.geom.*;

/**
 * A diamond shape capable of containing other shapes within itself.
 *
 * @author David Tang, Jonathan Yee, Stefan Schmainta
 */
public class DiamondShape extends ContainingShape {
    /**
     * Constructor that calls the super constructor to initialize the inherited
     * attributes.
     * @param x x-coordinate
     * @param y y-coordinate
     * @param width width of the shape
     */
    public DiamondShape(int x, int y, int width, int height) {
        super(x, y, width, height);
        setShape(recreateShape());
    }

    /**
     * @return A diamond shape.
     */
    public Shape recreateShape() {
        GeneralPath path = new GeneralPath();

        path.append(new Line2D.Double(getX() + getWidth() / 2, getY(), getX(), getY() + getHeight() / 2), true);
        path.append(new Line2D.Double(getX(), getY() + getHeight() / 2, getX() + getWidth() / 2, getY() + getHeight()), true);
        path.append(new Line2D.Double(getX() + getWidth() / 2, getY() + getHeight(), getX() + getWidth(), getY() + getHeight() / 2), true);
        path.append(new Line2D.Double(getX() + getWidth(), getY() + getHeight() / 2, getX() + getWidth() / 2, getY()), true);

        return path;
    }
}
