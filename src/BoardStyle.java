import java.awt.*;

/**
 * This interface will be the strategy that BoardView uses to style its pits and stones.
 *
 * @author David Tang, Jonathan Yee, Stefan Schmainta
 */
public abstract class BoardStyle {
    private BoardView container;
    private Color naturalColor;
    private Color highlightedColor;

    /**
     * Constructor that keeps a reference of the underlying user of this strategy.
     *
     * @param container The container that will be applying this style.
     */
    public BoardStyle(BoardView container) {
        this.container = container;
        naturalColor = Color.BLACK;
        highlightedColor = Color.BLACK;
    }

    /**
     * Implementation to style the board
     */
    public abstract void styleBoard();
    
    /**
     * @return the mancalaStyle
     */
    public abstract ContainingShape getMancalaStyle();

    /**
     * @return the pitStyle
     */
    public abstract ContainingShape getPitStyle();

    /**
     * @return the stoneStyle
     */
    public abstract ContainingShape getStoneStyle();

    /**
     * @return The container using this strategy.
     */
    public BoardView getContainer() {
        return container;
    }

    /**
     * @return The natural color.
     */
    public Color getNaturalColor() {
        return naturalColor;
    }

    /**
     * @param naturalColor The natural color to set.
     */
    public void setNaturalColor(Color naturalColor) {
        this.naturalColor = naturalColor;
    }

    /**
     * @return The highlighted color.
     */
    public Color getHighlightedColor() {
        return highlightedColor;
    }

    /**
     * @param highlightedColor The highlightedColor to set;
     */
    public void setHighlightedColor(Color highlightedColor) {
        this.highlightedColor = highlightedColor;
    }
}
