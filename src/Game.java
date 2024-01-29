import javax.swing.*;

/**
 * The Game class contains the main method that starts and runs the game by
 * initializing the runnable game class RunNim, that is then executed.
 */
public class Game {
    public static void main(String[] args) {
        Runnable game = new RunNim();
        SwingUtilities.invokeLater(game);
    }
}
