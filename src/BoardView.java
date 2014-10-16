import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

/**
 * This is the overall view of the Mancala board. Some of its views will be delegated to the StoneContainerView.
 *
 * @author David Tang, Jonathan Yee, Stefan Schmainta
 */
public class BoardView extends JFrame {
    public static final int PLAYER_ONE_MANCALA_INDEX = 6;
    public static final int PLAYER_TWO_MANCALA_INDEX = 13;

    private BoardStyle style;
    private Model model;
    private JPanel gamePanel;
    private JPanel pitsPanel;
    private JPanel buttonPanel;
    private JPanel newGamePanel;
    private StoneContainerView playerPits[];
    private String choices[] = {"3", "4"};
    private JComboBox dropDownList;
    private JLabel prompt;      
    private JButton newGameButton;
    private JButton takeBackButton;
    private JButton circleStyleButton;
    private JButton squareStyleButton;

    /**
     * Constructor that initializes privates attributes.
     */
    public BoardView(Model model) {
        style = null;
        this.model = model;
        model.setPrimaryView(this);
        setupBoard();
    }

    /**
     * Setup the Mancala board interface.
     */
    public void setupBoard() {
        gamePanel = new JPanel();
        pitsPanel = new JPanel();
        buttonPanel = new JPanel();
        playerPits = new StoneContainerView[(Model.PITS_PER_PLAYER + 1) * 2];
        prompt = new JLabel("Please select the number of starting stones per pit");      
        newGameButton = new JButton("New Game");
        newGamePanel = new JPanel();
        dropDownList = new JComboBox(choices);
        takeBackButton = new JButton("Take Back Move");
        circleStyleButton = new JButton("Circle Style");
        squareStyleButton = new JButton("Square Style");
        
        for (int i = 0; i < (Model.PITS_PER_PLAYER + 1) * 2; i++) {
            playerPits[i] = new StoneContainerView(model, this);
            
            if (i != PLAYER_ONE_MANCALA_INDEX && i != PLAYER_TWO_MANCALA_INDEX) {
                playerPits[i].addMouseListener(playerPits[i].getMouseListener(i));
                playerPits[i].addMouseMotionListener(playerPits[i].getMouseMotionListener(i));
            }
        }

        for (int i = 0; i < (Model.PITS_PER_PLAYER + 1) * 2; i++) {
            model.attach(playerPits[i]);
            playerPits[i].setIndex(i);
        }
        
        gamePanel.setLayout(new BorderLayout());
        pitsPanel.setLayout(new GridLayout(2, 6, 0, 0));
        pitsPanel.setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);
        
        for (int i = Model.PITS_PER_PLAYER * 2; i > PLAYER_ONE_MANCALA_INDEX; i--)
            pitsPanel.add(playerPits[i]);
        for (int i = 0; i < PLAYER_ONE_MANCALA_INDEX; i++)
            pitsPanel.add(playerPits[i]);
   
        gamePanel.add(playerPits[PLAYER_ONE_MANCALA_INDEX], BorderLayout.EAST);
        gamePanel.add(playerPits[PLAYER_TWO_MANCALA_INDEX], BorderLayout.WEST);
        gamePanel.add(pitsPanel, BorderLayout.CENTER);
        gamePanel.add(buttonPanel, BorderLayout.SOUTH);
        gamePanel.add(newGamePanel, BorderLayout.NORTH);
        gamePanel.setPreferredSize(new Dimension(1200, 650));
        
        newGamePanel.add(prompt);
        newGamePanel.add(dropDownList);
        newGamePanel.add(newGameButton);
           
        buttonPanel.add(circleStyleButton);
        buttonPanel.add(squareStyleButton);
        buttonPanel.add(takeBackButton);
        
        newGameButton.addActionListener(new
            ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    int startStones =  Integer.parseInt(((String) dropDownList.getSelectedItem()));
                    model.resetTakeBackCount();
                    model.setFirstPlayerTurn(true);
                    model.setGameNotFinished();
                    takeBackButton.setEnabled(false);
                    
                    for (int i = 0; i < (Model.PITS_PER_PLAYER + 1) * 2; i++) {
                        model.removeAllContainerStones(i);
                        
                        for (int j = 0; j < startStones; j++)
                            if (i != PLAYER_ONE_MANCALA_INDEX && i != PLAYER_TWO_MANCALA_INDEX) {
                                ContainingShape stone = style.getStoneStyle().clone();
                                stone.randomizeColor();
                                model.addStone(stone, i);
                            }
                    }
                    
                    repaint();
                }
            });
        
        circleStyleButton.addActionListener(new
            ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    if (style.getClass() != CircleStyle.class) {
                        style = new CircleStyle(BoardView.this);
                        applyStyle();
                        repaint();
                    }       
                }
            });
        
        squareStyleButton.addActionListener(new
             ActionListener() {
                 public void actionPerformed(ActionEvent e) {
                     if (style.getClass() != SquareStyle.class) {
                         style = new SquareStyle(BoardView.this);
                         applyStyle();
                         repaint();
                     }
                 }
             });

        takeBackButton.setEnabled(false);
        takeBackButton.addActionListener(new
            ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    if (model.getPreviousPosition() != null && model.takeBackAllowed()) {
                        model.returnToOldTakeBackCount();
                        model.incrementTakeBackCount();
                        model.setFirstPlayerTurn(model.getPreviousPosition().isFirstPlayerTurn());
                        model.setGameNotFinished();
                        
                        for (int i = 0; i < (Model.PITS_PER_PLAYER + 1) * 2; i++) {
                            int count = model.getPreviousPosition().getModel(i).size();
                            model.removeAllContainerStones(i);
                            
                            for (int j = 0; j < count; j++)
                                model.addStone(model.getPreviousPosition().getModel(i).remove(0), i);
                        }

                        applyStyle();
                        takeBackButton.setEnabled(false);
                        model.clearPreviousPosition();
                    }
                }
            });
        
        setSize(1300, 800);
        setTitle("Mancala");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
        add(gamePanel);
        applyStyle();
    }

    /**
     * Style the Mancala board. The default style of the board are circular pits and stones if one is not provided.
     */
    public void applyStyle() {
        if (style == null)
            style = new CircleStyle(this);
        
        style.styleBoard();
    }

    /**
     * Specify a style for the board.
     *
     * @param style
     */
    public void setStyle(BoardStyle style) {
        this.style = style;
        applyStyle();
    }

    /**
     * @return The Model.
     */
    public Model getModel() {
        return model;
    }

    /**
     * Disable the take back button.
     */
    public void disableTakeBack() {
        takeBackButton.setEnabled(false);
    }

    /**
     * Enable the take back button.
     */
    public void enableTakeBack() {
        takeBackButton.setEnabled(true);
    }

    /**
     * @return The style that is applied to the view.
     */
    public BoardStyle getStyle() {
        return style;
    }
}
