import javax.swing.*;
import java.awt.*;

/**
 * The RunNim class sets up the top-level frame and widgets for the GUI. It
 * implements Runnable and has just a run method.
 *
 * In this method, a JFrame is created overall. There is a JPanel that is a
 * status_panel with the JLabel displaying the "player 1's turn"-type messages.
 * There is another JPanel that is a control panel with the reset, undo, done,
 * pause, and unpause buttons. These all have action listeners that call methods
 * in GameBoard, which call methods in Nim to accomplish the actions. There is
 * also an instructions button that is toggled when the button is clicked/not
 * that displays a new window (JFrame) with the instructions. Lastly, it calls
 * reset (GameBoard) to start the game.
 */
public class RunNim implements Runnable {
    public void run() {
        // Top-level frame in which game components live
        final JFrame frame = new JFrame("n i m");
        frame.setLocation(300, 300);

        // Status panel
        final JPanel status_panel = new JPanel();
        frame.add(status_panel, BorderLayout.SOUTH);
        final JLabel status = new JLabel("setting up...");
        status_panel.add(status);

        // Game board
        final GameBoard board = new GameBoard(status);
        frame.add(board, BorderLayout.CENTER);

        // Instructions window
        final JFrame instruct = new JFrame("instructions");
        instruct.setLocation(350, 350);
        final JPanel instruct_panel = new JPanel();
        instruct.add(instruct_panel, BorderLayout.NORTH);
        final JLabel words = new JLabel("<html>instructions for n i m" +
                "<br><br>- 2 players taken turns removing matches." +
                "<br><br>- on each turn, a player can only take from 1 row" +
                "<br>and must taken >= 1 and at most max in row right then" +
                "<br><br>- after this has occurred, then click done to move on" +
                "<br>to the next player (be sure to do this). " +
                "<br><br>- undo allows for the undoing of all moves on current" +
                "<br>turn, or if the turn hasn't started, the previous turn." +
                "<br><br>- reset restarts the entire game board and can be clicked" +
                "<br>at any time." +
                "<br><br>- pause stops all actions except reset; unpause after" +
                "<br>pause resumes the game to where it was when paused." +
                "<br><br>- the player left with the last match loses.</html>");
        instruct_panel.add(words);

        // control panel
        final JPanel control_panel = new JPanel();
        frame.add(control_panel, BorderLayout.NORTH);

        // Instructions button
        final JButton instructions = new JButton("instructions");
        control_panel.add(instructions);
        instructions.addActionListener(e -> {
            instruct.pack();
            instruct.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            instruct.setVisible(!instruct.isVisible());
        });

        // Reset button
        final JButton reset = new JButton("reset");
        reset.addActionListener(e -> board.reset());
        control_panel.add(reset);

        // create an undo button
        final JButton undo = new JButton("undo");
        undo.addActionListener(e -> board.undo());
        control_panel.add(undo);

        // create a turn done button
        final JButton done = new JButton("done");
        done.addActionListener(e -> board.done());
        control_panel.add(done);

        // create a pause button
        final JButton pause = new JButton("pause");
        pause.addActionListener(e -> board.pause());
        control_panel.add(pause);

        // create an unpause button
        final JButton unpause = new JButton("unpause");
        unpause.addActionListener(e -> board.unpause());
        control_panel.add(unpause);

        // Put the frame on the screen
        frame.pack();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);

        // Start the game
        board.reset();
    }
}
