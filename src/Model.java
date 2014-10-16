import javax.swing.*;
import java.util.*;

/**
 * The model that holds the stones and views that must be notified of changes.
 *
 * @author David Tang, Jonathan Yee, Stefan Schmainta
 */
public class Model implements Cloneable {
    public static final int PITS_PER_PLAYER = 6;
    public static final int MAX_TAKE_BACK_PER_TURN = 3;

    private Model previousPosition;
    private ArrayList<ContainingShape>[] model;
    private BoardView primaryView;
    private ArrayList<StoneContainerView> views;
    private int takeBackCount;
    private boolean firstPlayerTurn;
    private boolean gameFinished;

    /**
     * Constructor that initializes all private attributes.
     */
    public Model() {
        previousPosition = null;
        primaryView = null;
        model = new ArrayList[(PITS_PER_PLAYER + 1) * 2];
        views = new ArrayList();

        for (int i = 0; i < (PITS_PER_PLAYER + 1) * 2; i++)
            model[i] = new ArrayList();

        takeBackCount = 0;
        firstPlayerTurn = true;
        gameFinished = true;
    }

    /**
     * Attach a listener to this model.
     *
     * @param stoneView A view to be notified when changes are made.
     */
    public void attach(StoneContainerView stoneView) {
        views.add(stoneView);
    }

    /**
     * Add a stone to the model and notify the corresponding view.
     *
     * @param stone The stone to be added.
     * @param index The index of the corresponding model.
     */
    public void addStone(ContainingShape stone, int index) {
        stone.setX(views.get(index).getContainer().getWidth() / 4 + model[index].size() / 8 * stone.getWidth());
        stone.setY(views.get(index).getContainer().getHeight() / 4 + (model[index].size() * stone.getHeight()) % (stone.getHeight() * 8));
        model[index].add(stone);
        views.get(index).repaint(); //Notify view
    }

    /**
     * @param index The index of the corresponding model.
     * @return The corresponding model.
     */
    public ArrayList<ContainingShape> getModel(int index) {
        return model[index];
    }

    /**
     * @param index The index of the corresponding view.
     * @return The corresponding view.
     */
    public StoneContainerView getView(int index) {
        return views.get(index);
    }

    /**
     * @return True if it is the first player's turn. False otherwise.
     */
    public boolean isFirstPlayerTurn() {
        return firstPlayerTurn;
    }

    /**
     * Switch the player turn.
     */
    public void switchPlayers() {
        if (firstPlayerTurn)
            firstPlayerTurn = false;
        else
            firstPlayerTurn = true;
    }

    /**
     * @return True if the game status is finished. False otherwise.
     */
    public boolean isGameFinished() {
        return gameFinished;
    }

    /**
     * Remove all the stones from a corresponding stone container model.
     *
     * @param index The index of the corresponding stone container model.
     * @return The stones that have been removed.
     */
    public ArrayList<ContainingShape> removeAllContainerStones(int index) {
        ArrayList<ContainingShape> temp = model[index];
        model[index] = new ArrayList();
        views.get(index).repaint();
        return temp;
    }

    /**
     * Determine whether the corresponding container matches the current player's container.
     *
     * @param index The index of the corresponding container.
     * @return True if it is the player's container. False otherwise.
     */
    public boolean isCorrectSide(int index) {
        if (firstPlayerTurn && index >= 0 && index < BoardView.PLAYER_ONE_MANCALA_INDEX)
            return true;
        else if (!firstPlayerTurn && index > BoardView.PLAYER_ONE_MANCALA_INDEX && index <= BoardView.PLAYER_TWO_MANCALA_INDEX)
            return true;

        return false;
    }

    /**
     * Reset the take back count to 0 and enable the take back button.
     */
    public void resetTakeBackCount() {
        takeBackCount = 0;

        if (previousPosition != null && previousPosition.takeBackCount < Model.MAX_TAKE_BACK_PER_TURN)
            primaryView.enableTakeBack();
    }

    /**
     * Change who the current player is.
     *
     * @param status Whether or not it will be the first player's turn.
     */
    public void setFirstPlayerTurn(boolean status) {
        firstPlayerTurn = status;
    }

    /**
     * Flag the game as being unfinished.
     */
    public void setGameNotFinished() {
        gameFinished = false;
    }

    /**
     * Creates a copy of the current position. Only the stone containers will contain a deep copy.
     */
    public void copyPosition() {
        try {
            previousPosition = (Model) super.clone();
            previousPosition.model = model.clone();

            for (int i = 0; i < (PITS_PER_PLAYER + 1) * 2; i++) {
                ArrayList<ContainingShape> temp = previousPosition.model[i];
                previousPosition.model[i] = new ArrayList();

                for (ContainingShape shape : temp)
                    previousPosition.model[i].add(shape.clone());
            }
        } catch (CloneNotSupportedException ex) {}
    }

    /**
     * @return The model of the previous position.
     */
    public Model getPreviousPosition() {
        return previousPosition;
    }

    /**
     * Destroy the previous position that was created.
     */
    public void clearPreviousPosition() {
        previousPosition = null;
    }

    /**
     * @return True if take backs are allowed. False otherwise.
     */
    public boolean takeBackAllowed() {
        return takeBackCount < MAX_TAKE_BACK_PER_TURN;
    }

    /**
     * Increase the take back count by one.
     */
    public void incrementTakeBackCount() {
        takeBackCount++;
    }

    /**
     * Set the current take back count to the old take back count.
     */
    public void returnToOldTakeBackCount() {
        if (previousPosition != null)
            takeBackCount =  previousPosition.takeBackCount;
    }

    /**
     * Check to see if the game is finished.
     */
    public void checkGameStatus() {
        if (firstPlayerPitsEmpty() || secondPlayerPitsEmpty()) {
            int playerOneCount = model[BoardView.PLAYER_ONE_MANCALA_INDEX].size();
            int playerTwoCount = model[BoardView.PLAYER_TWO_MANCALA_INDEX].size();

            if (playerOneCount > playerTwoCount)
                JOptionPane.showMessageDialog(null, "Player one wins!");
            else if (playerOneCount < playerTwoCount)
                JOptionPane.showMessageDialog(null, "Player two wins!");
            else
                JOptionPane.showMessageDialog(null, "Tie game!");

            gameFinished = true;
        }
    }

    /**
     * Check if all the first player's pits are empty. If it is, it places all the remaining stones in the second
     * player's pits to its own mancala.
     *
     * @return True if all of the first player's pits are empty. False otherwise.
     */
    private boolean firstPlayerPitsEmpty() {
        for (int i = 0; i < Model.PITS_PER_PLAYER; i++)
            if (model[i].size() > 0)
                return false;

        for (int i = Model.PITS_PER_PLAYER + 1; i < (Model.PITS_PER_PLAYER + 1) * 2 - 1; i++) {
            int count = model[i].size();

            for (int j = 0; j < count; j++)
                addStone(model[i].remove(0), BoardView.PLAYER_TWO_MANCALA_INDEX);

            views.get(i).repaint();
            views.get(BoardView.PLAYER_TWO_MANCALA_INDEX).repaint();
        }

        return true;
    }

    /**
     * Check if all the second player's pits are empty. If it is, it places all the remaining stones in the first
     * player's pits to its own mancala.
     *
     * @return True if all of the second player's pits are empty. False otherwise.
     */
    private boolean secondPlayerPitsEmpty() {
        for (int i = Model.PITS_PER_PLAYER + 1; i < (Model.PITS_PER_PLAYER + 1) * 2 - 1; i++)
            if (model[i].size() > 0)
                return false;

        for (int i = 0; i < Model.PITS_PER_PLAYER; i++) {
            int count = model[i].size();

            for (int j = 0; j < count; j++)
                addStone(model[i].remove(0), BoardView.PLAYER_ONE_MANCALA_INDEX);

            views.get(i).repaint();
            views.get(BoardView.PLAYER_ONE_MANCALA_INDEX).repaint();
        }

        return true;
    }

    /**
     * @return The entire board view.
     */
    public BoardView getPrimaryView() {
        return primaryView;
    }

    /**
     * Keep a reference of the board view.
     *
     * @param primaryView The board view
     */
    public void setPrimaryView(BoardView primaryView) {
        this.primaryView = primaryView;
    }
}
