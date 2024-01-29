import java.awt.*;
import java.io.*;
import java.util.LinkedList;

/**
 * The Nim class represents the model that is independent of the GUI components, etc.
 * It contains all the instance variables (arrays, ints, booleans, linkedlists, buffered
 * readers/writers, etc.) that are involved that are all private with get methods.
 *
 * The constructor calls resetBoard and resetFile, the latter which is used for all the
 * I/O stuff. There is a helper method inBoard for testing whether a point given is in
 * the board (array index-wise). There are move, resetBoard, resetFile, turnDone, undo,
 * pause, and unpause that have counterparts in the GameBoard class (the resets both are
 * called in the GameBoard.reset method).
 *
 * These all represent the functions that would be accomplished by clicking on buttons
 * or matches in the actual game. There is also a checkWinner method that helps in the
 * updateStatus method of gameBoard.
 */
public class Nim {

    // ragged array that represents the board
    private boolean[][] board;
    // number of total matches left
    private int numLeft;
    // row that player is currently removing from
    private int rowNow;
    // true if currently on player 1, false if on player 2
    private boolean player1;
    // whether a turn has just ended and another has started
    private boolean startOfTurn;
    // if a player has won
    private boolean gameOver;
    // if pause has been clicked, and unpause has not
    private boolean gamePaused;
    // all turns and moves within those turns
    private LinkedList<LinkedList<Point>> movesInTurns;
    // buffered writer/reader for pausing and unpausing
    private BufferedWriter bw;
    private BufferedReader br;

    /**
     * Constructor sets up game state. Both resets are called on initial creation.
     */
    public Nim() {
        resetBoard();
        resetFile();
    }

    /**
     * Helper function determining if a point is in the grid
     * @param r = row
     * @param c = col
     * @return boolean that's true if in the board, false otherwise
     */
    public boolean inBoard(int r, int c) {
        if (r == 0) {
            return c == 0;
        } else if (r == 1) {
            return c >= 0 && c <= 2;
        } else if (r == 2) {
            return c >= 0 && c <= 4;
        } else if (r == 3) {
            return c >= 0 && c <= 6;
        }
        return false;
    }

    /**
     * Allows players to play a turn. Returns true if the move is
     * successful and false if a player tries to play in a location that is
     * taken or after the game has ended. If the turn is successful and the game
     * has not ended, the player is changed. If the turn is unsuccessful or the
     * game has ended, the player is not changed.
     *
     * @param c column to play in
     * @param r row to play in
     * @return whether the turn was successful
     */

    public boolean move(int r, int c) {
        // accessing a match that doesn't exist or is already taken, or game over
        if (!inBoard(r, c) || !board[r][c] || gameOver) {
            return false;
        }
        // either assign r as startOfRow or check if r is in current row
        if (startOfTurn) {
            rowNow = r;
        } else {
            if (r != rowNow) {
                return false;
            }
        }
        // if the game is paused, the writing doesn't occur (it just runs through this method)
        if (!gamePaused) {
            try {
                bw.write("\nmove," + (getCurrentPlayer() ? "1," : "2,") + r + "," + c);
                bw.flush();
            } catch (IOException e) {
            }
        }
        // apply move and change state for numLeft
        board[r][c] = !board[r][c];
        numLeft--;
        // add a new LinkedList<Point> or add a Point to a last LinkedList<Point>
        if (startOfTurn) {
            LinkedList<Point> newTurn = new LinkedList<>();
            newTurn.add(new Point(r, c));
            movesInTurns.add(newTurn);
        } else {
            movesInTurns.getLast().add(new Point(r, c));
        }
        // if there's no more matches, the game is over instantly
        if (numLeft == 1) {
            gameOver = true;
        }
        // after one move, we are no longer at the start of turn
        startOfTurn = false;
        // default return
        return true;
    }

    /**
     * reset the game state to start a new game, referring to actual board.
     */
    public void resetBoard() {
        // creates ragged array and fills it with all true
        board = new boolean[4][];
        for (int i = 0; i < 4; i++) {
            board[i] = new boolean[2 * i + 1];
        }
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[i].length; j++) {
                board[i][j] = true;
            }
        }
        // initialize int related quantities
        numLeft = 16;
        rowNow = 0;
        // initializes boolean quantities
        player1 = true;
        startOfTurn = true;
        gameOver = false;
        // creates Collections object for turns and moves therein
        movesInTurns = new LinkedList<LinkedList<Point>>();
    }

    /**
     * resets file being written to when the game is reset so contents are overwritten
     */
    public void resetFile() {
        try {
            FileWriter w = new FileWriter("./files/gameasfile.csv", false);
            bw = new BufferedWriter(w);
            bw.write("start");
            bw.flush();
        } catch (IOException e) {
        }
    }

    /**
     * Method called when a turn is over and moving on to next player
     * @return false if game is over or if it's the start of a turn, true otherwise
     */
    public boolean turnDone() {
        // check whether it is still start of turn or if the game is already over
        if (gameOver || startOfTurn) {
            return false;
        }
        // if game is paused, do not write to file
        if (!gamePaused) {
            try {
                bw.write("\n" + "done," + (getCurrentPlayer() ? "1" : "2"));
                bw.flush();
            } catch (IOException e) {
            }
        }
        // change the player
        player1 = !player1;
        // set to a new turn
        startOfTurn = true;
        return true;
    }

    /**
     * Method to allow undoing of an entire previous turn or all moves in current turn
     * @return false if the game is over or there are not steps to undo, true otherwise
     */
    public boolean undo() {
        // if no matches have been taken or the game is over, return false
        if (gameOver || numLeft == 16) {
            return false;
        }
        // get linkedlist of moves in last turn and integer representing their row
        LinkedList<Point> turnToUndo = movesInTurns.getLast();
        int r = (int) turnToUndo.getLast().getX();
        // print out of "undo" and row to reput matches in
        if (!gamePaused) {
            try {
                if (startOfTurn) {
                    bw.write("\nundo," + (!getCurrentPlayer() ? "1," : "2,") + r);
                } else {
                    bw.write("\nundo," + (getCurrentPlayer() ? "1," : "2,") + r);
                }
            } catch (IOException e) {
            }
        }
        // continues printing out each column for matches to be put back
        for (Point p : turnToUndo) {
            int c = (int) p.getY();
            board[r][c] = true;
            numLeft++;
            if (!gamePaused) {
                try {
                    bw.write("," + c);
                } catch (IOException e) {
                }
            }
        }
        // flush everything so it appears immediately
        if (!gamePaused) {
            try {
                bw.flush();
            } catch (IOException e) {
            }
        }
        // remove the last turn that is now an empty linkedlist
        movesInTurns.remove(movesInTurns.size() - 1);
        // calculate next player and go to start of turn
        player1 = movesInTurns.size() % 2 == 0;
        startOfTurn = true;
        return true;
    }

    /**
     * checkWinner checks whether the game has reached a win condition.
     * checkWinner only looks for horizontal wins.
     *
     * @return 0 if nobody has won yet, 1 if player 1 has won, and 2 if player 2
     */
    public int checkWinner() {
        // if there's only 1 left, gameOver and close writer; return player number or 0
        if (gameOver) {
            try {
                bw.flush();
                bw.close();
            } catch (IOException e) {
            }
            return !player1 ? 2 : 1;
        } else {
            return 0;
        }
    }

    /**
     * Method to pause a game (if possible), updating state of gamePaused
     */
    public void pause() {
        // if no matches have been taken or if the game is over, do nothing
        if (numLeft == 16 || gameOver) {
            return;
        }
        // flush so everything updates immediately, set game as paused
        try {
            bw.flush();
        } catch (IOException e) {
        }
        gamePaused = true;
    }

    /**
     * Method to unpause and read file I/O information to walk through steps again
     */
    public void unpause() {
        // if pause hasn't just been clicked, pushing unpause doesn't do anything
        if (!isPaused()) {
            return;
        }
        // physical board is reset (not the file)
        resetBoard();
        // buffered reader reads the lines, calling methods depending on first word after split
        try {
            FileReader r = new FileReader("./files/gameasfile.csv");
            br = new BufferedReader(r);
            String s = br.readLine();
            while (s != null) {
                String[] arr = s.split(",");
                if (arr[0].equals("move")) {
                    move(Integer.parseInt(arr[2]), Integer.parseInt(arr[3]));
                } else if (arr[0].equals("done")) {
                    turnDone();
                } else if (arr[0].equals("undo")) {
                    undo();
                } else {
                    System.out.println("improper formatting");
                }
                s = br.readLine();
            }
            br.close();
        } catch (FileNotFoundException f) {
            System.out.println("file not found");
        } catch (IOException e) {
        }
        // game is no longer paused
        gamePaused = false;
    }

    // the following are get methods for the fields of this class
    public boolean[][] getBoard() {
        return board;
    }

    public int getNumLeft() {
        return numLeft;
    }

    public int getRowNow() {
        return rowNow;
    }

    public boolean getStartOfTurn() {
        return startOfTurn;
    }

    public boolean getGameOver() {
        return gameOver;
    }

    public LinkedList<LinkedList<Point>> getMovesInTurns() {
        return movesInTurns;
    }

    /**
     * getCurrentPlayer is a getter for the player
     * whose turn it is in the game.
     *
     * @return true if it's Player 1's turn,
     *         false if it's Player 2's turn.
     */
    public boolean getCurrentPlayer() {
        return player1;
    }

    /**
     * getCell is a getter for the contents of the cell specified by the method
     * arguments.
     *
     * @param c column to retrieve
     * @param r row to retrieve
     * @return a boolean denoting the contents of the corresponding cell on the
     *         game board. true = match present, false = no match present
     */
    public boolean getCell(int r, int c) {
        return board[r][c];
    }

    /**
     * gets the value of gamePaused
     */
    public boolean isPaused() {
        return gamePaused;
    }

    /**
     * printGameState prints the current game state
     * for debugging.
     */
    public void printGameState() {
        // by default, there are 0 moves
        int movesInTurn = 0;
        // sets up a variable for which player's turn it is
        int turn;
        // turn 1 on reset, otherwise calculate based on startOfTurn and movesInTurns size
        if (movesInTurns.isEmpty()) {
            turn = 1;
        } else {
            if (startOfTurn) {
                movesInTurn = 0;
                if (movesInTurns.size() % 2 == 1) {
                    turn = movesInTurns.size() / 2 + movesInTurns.size() % 2;
                } else {
                    turn = movesInTurns.size() / 2 + movesInTurns.size() % 2 + 1;
                }
            } else {
                movesInTurn = movesInTurns.getLast().size();
                turn = movesInTurns.size() / 2 + movesInTurns.size() % 2;
            }
        }
        // print out model of board based on above info
        System.out.println("\n\nPlayer " + (player1 ? 1 : 2) + " Turn " + turn +
                " Move " + movesInTurn + ":\n");
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[i].length; j++) {
                System.out.print(board[i][j]);
                if (j < 2 * i) {
                    System.out.print(" | ");
                }
            }
            if (i < 3) {
                System.out.println("\n---------");
            }
        }
    }

}
