import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * The GameBoard class has a constructor that initializes a new Nim object and
 * status JLabel. A mouse listener listens for mouse releases, in which case it
 * calls the move method on the Nim object based on where it's clicked and
 * updates the status (with a later function) and reflects the visual change
 * in the array of MatchPanel objects that are visible/not based on the method
 * paintComponent (also a later function).
 *
 * There are also reset, done, and undo methods that call corresponding methods
 * in Nim.java (only when it is not paused) and update the status text. Pause and
 * unpause are also methods here, they do not update any status. The updateStatus
 * method enacts those changes and also calls checkWinner on the Nim object that
 * displays a different status when the game is over.
 *
 * We override the paintComponent class to display the particular match if it is
 * present in the matches boolean array. We also override getPreferredSize.
 */
public class GameBoard extends JPanel {

    // Model for the game
    private Nim n;

    // Current status text
    private JLabel status;

    // Game constants
    public static final int BOARD_WIDTH = 288;
    public static final int BOARD_HEIGHT = 400;

    // Jagged array of matches, similar to that in Nim.java, but this one is for the images
    private MatchPanel[][] matches;

    public GameBoard(JLabel initial) {

        // Set border and keyboard/mouse focus
        setBorder(BorderFactory.createLineBorder(Color.BLACK));
        setFocusable(true);

        // Initializes model for the game
        n = new Nim();
        // Initializes the status JLabel
        status = initial;

        // Listens for mouseclicks. Updates the model, then updates the game
        // board based off of the updated model. Only do if the game isn't paused
        addMouseListener(new MouseAdapter() {
            public void mouseReleased(MouseEvent e) {
                if (!n.isPaused()) {
                    Point p = e.getPoint();

                    int row = p.y / 100;
                    int colP = p.x;
                    int col;

                    // updates the model given the coordinates of the mouseclick
                    if (row == 0 && colP > 128 && colP < 160) {
                        col = 0;
                        n.move(0, col);
                    } else if (row == 1 && colP > 96 && colP < 192) {
                        col = (colP - 96) / 32;
                        n.move(1, col);
                    } else if (row == 2 && colP > 64 && colP < 224) {
                        col = (colP - 64) / 32;
                        n.move(2, col);
                    } else if (row == 3 && colP > 32 && colP < 256) {
                        col = (colP - 32) / 32;
                        n.move(3, col);
                    }

                    updateStatus();
                    repaint(); // repaints the game board
                }
            }
        });

        // create array and initialize the match images (MatchPanel objects) that will be in it
        matches = new MatchPanel[4][];
        for (int i = 0; i < matches.length; i++) {
            matches[i] = new MatchPanel[2 * i + 1];
        }

        matches[0][0] = new MatchPanel(128,0);
        matches[1][0] = new MatchPanel(96,100);
        matches[1][1] = new MatchPanel(128,100);
        matches[1][2] = new MatchPanel(160,100);
        matches[2][0] = new MatchPanel(64,200);
        matches[2][1] = new MatchPanel(96,200);
        matches[2][2] = new MatchPanel(128,200);
        matches[2][3] = new MatchPanel(160,200);
        matches[2][4] = new MatchPanel(192,200);
        matches[3][0] = new MatchPanel(32,300);
        matches[3][1] = new MatchPanel(64,300);
        matches[3][2] = new MatchPanel(96,300);
        matches[3][3] = new MatchPanel(128,300);
        matches[3][4] = new MatchPanel(160,300);
        matches[3][5] = new MatchPanel(192,300);
        matches[3][6] = new MatchPanel(224,300);
    }

    /**
     * (Re-)sets the game to its initial state, unless it's paused, and set status text to player 1
     * Reset always works when game is on pause
     */
    public void reset() {
        if (n.isPaused()) {
            n.unpause();
        }
        n.resetBoard();
        n.resetFile();
        status.setText("player 1's turn");
        repaint();

        // Ensure keyboard/mouse focus
        requestFocusInWindow();
    }

    /**
     * Signals that a turn is over, doesn't do anything if the game is paused
     * Otherwise set text in status based on whoever's turn is next (calculated in turnDone)
     */
    public void done() {
        if (n.isPaused()) {
            return;
        }
        if (n.checkWinner() == 0) {
            n.turnDone();
            updateStatus();
            repaint();
        }

        requestFocusInWindow();
    }

    /**
     * Undos the current entire turn or if it's startOfTurn, undos the previous turn
     * Then set the text to whoever it was whose turn was undone
     */
    public void undo() {
        if (n.isPaused()) {
            return;
        }
        if (n.checkWinner() == 0) {
            n.undo();
            updateStatus();
            repaint();
        }

        requestFocusInWindow();
    }

    /**
     * Updates the JLabel to reflect the current state of the game.
     * There are different cases based on whether the game is over or not.
     */
    private void updateStatus() {
        if (n.checkWinner() == 0) {
            if (n.getCurrentPlayer()) {
                status.setText("player 1's turn");
            } else {
                status.setText("player 2's turn");
            }
        } else {
            int winner = n.checkWinner();
            if (winner == 1) {
                status.setText("player 1 wins!!!");
            } else if (winner == 2) {
                status.setText("player 2 wins!!!");
            }
        }
    }

    // The only thing this function does is call the pause method in Nim.java
    public void pause() {
        n.pause();
    }

    // Similar to above, but calling the unpause method
    public void unpause() {
        n.unpause();
    }

    /**
     * Draws the game board based on the matches array
     */
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        for (int i = 0; i < matches.length; i++) {
            for (int j = 0; j < matches[i].length; j++) {
                if (n.getCell(i, j)) {
                    matches[i][j].draw(g);
                }
            }
        }
    }

    /**
     * Returns the size of the game board.
     */
    @Override
    public Dimension getPreferredSize() {
        return new Dimension(BOARD_WIDTH, BOARD_HEIGHT);
    }
}
