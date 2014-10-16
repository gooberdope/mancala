import java.awt.*;
import java.util.*;

/**
 * A shape capable of containing other shapes within itself.
 *
 * @author David Tang, Jonathan Yee, Stefan Schmainta
 */
public abstract class ContainingShape implements Cloneable {
    private int x;
    private int y;
    private int width;
    private int height;
    private Color color;
    private Shape shape;

    /**
     * Constructor that initializes privates attributes to the given parameters.
     *
     * @param x x-coordinate
     * @param y y-coordinate
     * @param width width of the shape.
     * @param height height of the shape.
     */
    public ContainingShape(int x, int y, int width, int height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        color = Color.BLACK;
    }

    /**
     * This method should implement the shape of the containing shape. It will be called whenever it is requested
     * to be drawn.
     *
     * @return The shape of the containing shape.
     */
    public abstract Shape recreateShape();

    /**
     * Draws the outline of the containing shape.
     *
     * @param g2 the graphics engine that will be used to draw the shape.
     */
    public void draw(Graphics2D g2) {
        setShape(recreateShape());
        g2.setColor(getColor());
        g2.draw(getShape());
    }

    /**
     * Draws the containing shape filled with its color.
     *
     * @param g2 the graphics engine that will be used to draw the shape.
     */
    public void drawFill(Graphics2D g2) {
        setShape(recreateShape());
        g2.setColor(getColor());
        g2.fill(getShape());
    }

    /**
     * @return Rectangle that completely encloses the shape.
     */
    public Rectangle getBounds() {
        return new Rectangle(x, y, width, width);
    }

    /**
     * @return the x-coordinate
     */
    public int getX() {
        return x;
    }

    /**
     * @param x the x to set
     */
    public void setX(int x) {
        this.x = x;
    }

    /**
     * @return the y-coordinate
     */
    public int getY() {
        return y;
    }

    /**
     * @param y the y to set
     */
    public void setY(int y) {
        this.y = y;
    }

    /**
     * @return the width of the shape
     */
    public int getWidth() {
        return width;
    }

    /**
     * @param width the width to set
     */
    public void setWidth(int width) {
        this.width = width;
    }

    /**
     * @return the height
     */
    public int getHeight() {
        return height;
    }

    /**
     * @param height the height to set
     */
    public void setHeight(int height) {
        this.height = height;
    }

    /**
     * @return the color
     */
    public Color getColor() {
        return color;
    }

    /**
     * @param color the color to set
     */
    public void setColor(Color color) {
        this.color = color;
    }

    /**
     * @return The containing shape.
     */
    public Shape getShape() {
        return shape;
    }

    /**
     * @param shape The new containing shape.
     */
    public void setShape(Shape shape) {
        this.shape = shape;
    }

    /**
     * Randomize the color of the containing shape.
     */
    public void randomizeColor() {
        Random rand = new Random();
        float r = rand.nextFloat();
        float g = rand.nextFloat();
        float b = rand.nextFloat();
        color = new Color(r, g, b);
    }

    /**
     * @param point The coordinates of the mouse cursor.
     * @return True if the point is at least on the outline of the shape. False otherwise.
     */
    public boolean contains(Point point) {
        if (shape != null)
            return shape.contains(point);

        return false;
    }

    /**
     * @return A shallow copy of this class.
     */
    public ContainingShape clone() {
        ContainingShape clone = null;

        try {
            clone =(ContainingShape) super.clone();
        } catch (CloneNotSupportedException e) {}

        return clone;
    }
}
