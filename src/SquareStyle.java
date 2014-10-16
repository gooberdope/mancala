import java.awt.*;
import java.util.*;

/**
 * A strategy that uses rectangles for the mancala and pits. The stones will be diamonds and will be filled with a
 * random color.
 *
 * @author David Tang, Jonathan Yee, Stefan Schmainta
 */
public class SquareStyle extends BoardStyle {
    private ContainingShape mancalaStyle;
    private ContainingShape pitStyle;
    private ContainingShape stoneStyle;

    /**
     * Constructor that initializes all its private attributes.
     *
     * @param container The underlying view that will be applying this strategy.
     */
    public SquareStyle(BoardView container) {
        super(container);
        mancalaStyle = new RectangleShape(10, 10, 150, 600);
        pitStyle = new RectangleShape(0, 25, 150, 225);
        stoneStyle = new DiamondShape(0, 0, 20, 20);
        stoneStyle.randomizeColor();
        setNaturalColor(Color.RED);
        setHighlightedColor(Color.WHITE);
        mancalaStyle.setColor(getNaturalColor());
        pitStyle.setColor(getNaturalColor());
    }

    /**
     * Style the container that uses this strategy.
     */
    public void styleBoard() {
        Model model = getContainer().getModel();
     
        model.getView(BoardView.PLAYER_ONE_MANCALA_INDEX).setContainer(mancalaStyle.clone());
        model.getView(BoardView.PLAYER_TWO_MANCALA_INDEX).setContainer(mancalaStyle.clone());
            
        for (int i = 0; i < (Model.PITS_PER_PLAYER + 1) * 2; i++)
            if (i != BoardView.PLAYER_ONE_MANCALA_INDEX && i != BoardView.PLAYER_TWO_MANCALA_INDEX)
                model.getView(i).setContainer(pitStyle.clone());

        for (int i = 0; i < (Model.PITS_PER_PLAYER + 1) * 2; i++) {
            int size = model.getModel(i).size();
            ArrayList<ContainingShape> oldStones = model.removeAllContainerStones(i);
            
            for (int j = 0; j < size; j++) {
                ContainingShape newStone = stoneStyle.clone();
                newStone.setColor(oldStones.get(j).getColor());
                model.addStone(newStone, i);
            }     
        }
    }

    /**
     * @return the mancalaStyle
     */
    public ContainingShape getMancalaStyle() {
        return mancalaStyle;
    }

    /**
     * @return the pitStyle
     */
    public ContainingShape getPitStyle() {
        return pitStyle;
    }

    /**
     * @return the stoneStyle
     */
    public ContainingShape getStoneStyle() {
        return stoneStyle;
    }
}
