import java.awt.*;
import java.awt.event.*;
import java.util.*;
import javax.swing.*;

/**
 * A view that contains the stones and stone container.
 *
 * @author David Tang, Jonathan Yee, Stefan Schmainta
 */
public class StoneContainerView extends JPanel {
    private int index;
    private Model model;
    private BoardView boardView;
    private ContainingShape container;

    /**
     * Constructor that initializes the private attributes.
     */
    public StoneContainerView(Model model, BoardView boardView) {
        this.model = model;
        this.boardView = boardView;
        container = null;
    }

    /**
     * Set a containing shape to be drawn on to the panel.
     *
     * @param container The shape to add.
     */
    public void setContainer(ContainingShape container) {
        this.container = container;
        int width = container.getWidth() + container.getX() * 2;
        int height = container.getHeight();
        this.setPreferredSize(new Dimension(width, height));
    }

    /**
     * @param g the graphics engine that will be used to draw the shape.
     */
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;

        container.draw(g2);
        for (ContainingShape stone : model.getModel(index)) //Retrieve specific model
            stone.drawFill(g2);
    }

    /**
     * Returns a MouseMotionListener that outlines the container yellow if the mouse cursor is over it and is also
     * the current player's pit
     *
     * @param index The index of the corresponding stone container view.
     * @return A MouseMotionListener.
     */
    public MouseMotionListener getMouseMotionListener(final int index) {
        return new
            MouseMotionAdapter() {
                public synchronized void mouseMoved(MouseEvent e) {
                    if (!model.isGameFinished() && container.contains(e.getPoint()) && model.isCorrectSide(index)) {
                        setContainerColor(boardView.getStyle().getHighlightedColor());
                        for (int i = 0; i < (Model.PITS_PER_PLAYER + 1) * 2; i++)
                            if (model.getView(i) != StoneContainerView.this)
                                model.getView(i).setContainerColor(boardView.getStyle().getNaturalColor());
                    }
                    else
                        setContainerColor(boardView.getStyle().getNaturalColor());
                }
            };
    }

    /**
     * Returns a MouseListener that makes a move on the board if a stone container is clicked and that container is also
     * the current player's pit.
     *
     * @param index The index of the corresponding container view.
     * @return A MouseListener.
     */
    public MouseListener getMouseListener(final int index) {
        return new
            MouseAdapter() {
                public synchronized void mousePressed(MouseEvent e) {
                    if (container.contains(e.getPoint()) && !model.getModel(index).isEmpty() && model.isCorrectSide(index)) {
                        model.copyPosition();
                        model.resetTakeBackCount();
                        int pitIndex = index;
                        int trueIndex = 0;

                        ArrayList<ContainingShape> stones = model.removeAllContainerStones(index);
                        setContainerColor(boardView.getStyle().getNaturalColor());
                        
                        for (int i = 0; i < stones.size(); i++) {
                            trueIndex = ++pitIndex % ((Model.PITS_PER_PLAYER + 1) * 2);

                            if (model.isFirstPlayerTurn() && trueIndex != BoardView.PLAYER_TWO_MANCALA_INDEX ||
                                    !model.isFirstPlayerTurn() && trueIndex != BoardView.PLAYER_ONE_MANCALA_INDEX)
                                model.addStone(stones.get(i), trueIndex);
                            else
                                i--;
                        }
                        
                        if (model.getModel(trueIndex).size() == 1 && model.isCorrectSide(trueIndex) &&
                                trueIndex != BoardView.PLAYER_TWO_MANCALA_INDEX && trueIndex != BoardView.PLAYER_TWO_MANCALA_INDEX) {
                            Iterator<ContainingShape> it = model.getModel(12 - trueIndex).iterator();
                            
                            while (it.hasNext()) {
                                ContainingShape stone = it.next();
                                it.remove();
                                
                                if (model.isFirstPlayerTurn())
                                    model.addStone(stone, BoardView.PLAYER_ONE_MANCALA_INDEX);
                                else
                                    model.addStone(stone, BoardView.PLAYER_TWO_MANCALA_INDEX);
                            }
                            
                            model.getView(12 - trueIndex).repaint();
                        }
                        
                        if (model.isFirstPlayerTurn() && pitIndex % ((Model.PITS_PER_PLAYER + 1) * 2) != BoardView.PLAYER_ONE_MANCALA_INDEX ||
                                !model.isFirstPlayerTurn() && pitIndex % ((Model.PITS_PER_PLAYER + 1) * 2) != BoardView.PLAYER_TWO_MANCALA_INDEX)
                            model.switchPlayers();

                        if (!model.takeBackAllowed())
                            boardView.disableTakeBack();

                        model.checkGameStatus();
                    }
                }
            };
    }

    /**
     * @return The shape of the stone container.
     */
    public ContainingShape getContainer() {
        return container;
    }

    /**
     * Change the color of the container.
     *
     * @param color The color to be changed to.
     */
    public void setContainerColor(Color color) {
        if (container != null) {
            container.setColor(color);
            repaint();
        }
    }

    /**
     * @return The model.
     */
    public Model getModel() {
        return model;
    }

    /**
     * Set the pit index of this stone container. It will be used to get its correct corresponding model.
     *
     * @param index The index of the corresponding model.
     */
    public void setIndex(int index) {
        this.index = index;
    }
}
