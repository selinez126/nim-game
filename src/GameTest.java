import org.junit.Test;

import java.awt.*;
import java.util.LinkedList;

import static org.junit.Assert.*;

/**
 * The GameTest class then tests all the methods in the Nim class, as discussed
 * previously in the JUnit test section.
 */

public class GameTest {

    // tests for inBoard
    @Test
    public void inBoardCorrect() {
        Nim n = new Nim();
        assertTrue(n.inBoard(0, 0));
        assertTrue(n.inBoard(1, 1));
        assertTrue(n.inBoard(2, 4));
        assertTrue(n.inBoard(3, 5));
    }

    @Test
    public void inBoardNotCorrect() {
        Nim n = new Nim();
        assertFalse(n.inBoard(0, 1));
        assertFalse(n.inBoard(1, -1));
        assertFalse(n.inBoard(2, 6));
        assertFalse(n.inBoard(3, 8));

    }

    // tests for move
    @Test
    public void moveInvalidSpot() {
        Nim n = new Nim();
        assertFalse(n.move(0, 3));
    }

    @Test
    public void moveMatchNotThere() {
        Nim n = new Nim();
        n.move(0, 0);
        assertFalse(n.move(0, 0));
    }

    @Test
    public void moveGameOver() {
        Nim n = new Nim();
        for (int i = 0; i < n.getBoard().length; i++) {
            for (int j = 0; j < n.getBoard()[i].length; j++) {
                n.move(i, j);
            }
            n.turnDone();
        }
        assertFalse(n.move(1, 2));
    }

    @Test
    public void moveValidSingleMove() {
        Nim n = new Nim();
        assertTrue(n.move(0, 0));
    }

    @Test
    public void moveSameTurnSameRow() {
        Nim n = new Nim();
        n.move(1, 0);
        assertTrue(n.move(1, 1));
    }

    @Test
    public void moveSameTurnOtherRow() {
        Nim n = new Nim();
        n.move(1, 0);
        assertFalse(n.move(2, 0));
    }

    @Test
    public void moveNewStartOfTurn() {
        Nim n = new Nim();
        n.move(0, 0);
        n.turnDone();
        assertTrue(n.move(2, 3));
    }

    @Test
    public void moveCellChanged() {
        Nim n = new Nim();
        n.move(0, 0);
        assertFalse(n.getCell(0, 0));
    }

    @Test
    public void moveNumLeft() {
        Nim n = new Nim();
        n.move(0, 0);
        n.turnDone();
        n.move(1, 0);
        assertEquals(14, n.getNumLeft());
    }

    @Test
    public void moveMovesInTurnsStartOfTurn() {
        Nim n = new Nim();
        n.move(0, 0);
        LinkedList<LinkedList<Point>> exp = new LinkedList<LinkedList<Point>>();
        LinkedList<Point> exp2 = new LinkedList<Point>();
        exp2.add(new Point(0, 0));
        exp.add(exp2);
        assertEquals(exp, n.getMovesInTurns());
    }

    @Test
    public void moveMovesInTurnsDuringTurn() {
        Nim n = new Nim();
        n.move(1, 0);
        n.move(1, 1);
        LinkedList<LinkedList<Point>> exp = new LinkedList<>();
        LinkedList<Point> exp2 = new LinkedList<>();
        exp2.add(new Point(1, 0));
        exp2.add(new Point(1, 1));
        exp.add(exp2);
        assertEquals(exp, n.getMovesInTurns());
    }

    @Test
    public void moveGameOverAfter() {
        Nim n = new Nim();
        for (int i = 1; i < n.getBoard().length; i++) {
            for (int j = 0; j < n.getBoard()[i].length; j++) {
                n.move(i, j);
            }
            n.turnDone();
        }
        assertTrue(n.getGameOver());
    }

    @Test
    public void moveStartOfTurnSetToFalseAfter() {
        Nim n = new Nim();
        n.move(0, 0);
        assertFalse(n.getStartOfTurn());
    }

    // tests for reset
    @Test
    public void resetBoardFromConstructor() {
        Nim n = new Nim();
        boolean[][] exp = new boolean[4][];
        for (int i = 0; i < 4; i++) {
            exp[i] = new boolean[2 * i + 1];
        }
        for (int i = 0; i < exp.length; i++) {
            for (int j = 0; j < exp[i].length; j++) {
                exp[i][j] = true;
            }
        }
        assertArrayEquals(exp, n.getBoard());
        assertEquals(16, n.getNumLeft());
        assertEquals(0, n.getRowNow());
        assertTrue(n.getCurrentPlayer());
        assertTrue(n.getStartOfTurn());
        assertFalse(n.getGameOver());
        LinkedList<LinkedList<Point>> expList = new LinkedList<>();
        assertEquals(expList, n.getMovesInTurns());
    }

    // these are test for turnDone, but I just call them "done"
    @Test
    public void doneGameOver() {
        Nim n = new Nim();
        for (int i = 1; i < n.getBoard().length; i++) {
            for (int j = 0; j < n.getBoard()[i].length; j++) {
                n.move(i, j);
            }
            n.turnDone();
        }
        assertTrue(n.getCurrentPlayer());
        assertFalse(n.getStartOfTurn());
        assertFalse(n.turnDone());
    }

    @Test
    public void doneStartOfEverything() {
        Nim n = new Nim();
        assertFalse(n.turnDone());
        assertTrue(n.getCurrentPlayer());
        assertTrue(n.getStartOfTurn());
    }

    @Test
    public void doneStartOfTurn() {
        Nim n = new Nim();
        n.move(1, 0);
        n.turnDone();
        assertFalse(n.getCurrentPlayer());
        assertFalse(n.turnDone());
        assertTrue(n.getStartOfTurn());
    }

    @Test
    public void doneAlreadyDone() {
        Nim n = new Nim();
        n.move(1, 0);
        n.turnDone();
        n.turnDone();
        assertFalse(n.getCurrentPlayer());
        assertFalse(n.turnDone());
        assertTrue(n.getStartOfTurn());
    }

    // tests for undo
    @Test
    public void undoNothingToUndo() {
        Nim n = new Nim();
        assertFalse(n.undo());
        assertTrue(n.getCurrentPlayer());
    }

    @Test
    public void undoGameOver() {
        Nim n = new Nim();
        for (int i = 1; i < n.getBoard().length; i++) {
            for (int j = 0; j < n.getBoard()[i].length; j++) {
                n.move(i, j);
            }
            n.turnDone();
        }
        assertFalse(n.undo());
    }

    @Test
    public void undoBackToStart() {
        Nim n = new Nim();
        n.move(1, 0);
        n.move(1, 2);
        assertFalse(n.getCell(1, 0));
        assertFalse(n.getCell(1, 2));
        assertEquals(14, n.getNumLeft());
        n.undo();
        assertTrue(n.getCell(1, 0));
        assertTrue(n.getCell(1, 2));
        assertEquals(16, n.getNumLeft());
        LinkedList<LinkedList<Point>> exp = new LinkedList<>();
        assertEquals(exp, n.getMovesInTurns());
        assertTrue(n.getStartOfTurn());
        assertTrue(n.getCurrentPlayer());
    }

    @Test
    public void undoNotStartOfTurn() {
        Nim n = new Nim();
        n.move(1, 0);
        n.move(1, 1);
        n.turnDone();
        n.move(2, 0);
        n.move(2, 3);
        n.undo();
        assertTrue(n.getCell(2, 0));
        assertTrue(n.getCell(2, 3));
        assertFalse(n.getCell(1, 0));
        assertFalse(n.getCell(1, 1));
        assertEquals(14, n.getNumLeft());
        LinkedList<LinkedList<Point>> exp = new LinkedList<>();
        LinkedList<Point> exp2 = new LinkedList<>();
        exp2.add(new Point(1, 0));
        exp2.add(new Point(1, 1));
        exp.add(exp2);
        assertEquals(exp, n.getMovesInTurns());
        assertTrue(n.getStartOfTurn());
        assertFalse(n.getCurrentPlayer());
    }

    @Test
    public void undoStartOfTurn() {
        Nim n = new Nim();
        n.move(1, 0);
        n.move(1, 1);
        n.turnDone();
        n.move(2, 0);
        n.move(2, 3);
        n.turnDone();
        n.undo();
        assertTrue(n.getCell(2, 0));
        assertTrue(n.getCell(2, 3));
        assertFalse(n.getCell(1, 0));
        assertFalse(n.getCell(1, 1));
        assertEquals(14, n.getNumLeft());
        LinkedList<LinkedList<Point>> exp = new LinkedList<>();
        LinkedList<Point> exp2 = new LinkedList<>();
        exp2.add(new Point(1, 0));
        exp2.add(new Point(1, 1));
        exp.add(exp2);
        assertEquals(exp, n.getMovesInTurns());
        assertTrue(n.getStartOfTurn());
        assertFalse(n.getCurrentPlayer());
    }

    @Test
    public void undoTwoCalls() {
        Nim n = new Nim();
        n.move(1, 0);
        n.move(1, 1);
        n.turnDone();
        n.move(2, 0);
        n.move(2, 3);
        n.turnDone();
        n.move(2, 1);
        n.move(2, 4);
        n.undo();
        n.undo();
        assertFalse(n.getCell(1, 0));
        assertFalse(n.getCell(1, 1));
        assertTrue(n.getCell(2, 0));
        assertTrue(n.getCell(2, 3));
        assertTrue(n.getCell(2, 1));
        assertTrue(n.getCell(2, 4));
        assertEquals(14, n.getNumLeft());
        LinkedList<LinkedList<Point>> exp = new LinkedList<>();
        LinkedList<Point> exp2 = new LinkedList<>();
        exp2.add(new Point(1, 0));
        exp2.add(new Point(1, 1));
        exp.add(exp2);
        assertEquals(exp, n.getMovesInTurns());
        assertTrue(n.getStartOfTurn());
        assertFalse(n.getCurrentPlayer());
    }

    // tests for checkWinner
    @Test
    public void checkWinnerGameNotOver() {
        Nim n = new Nim();
        n.move(0, 0);
        assertEquals(0, n.checkWinner());
    }

    @Test
    public void checkWinner1Wins() {
        Nim n = new Nim();
        for (int i = 1; i < n.getBoard().length; i++) {
            for (int j = 0; j < n.getBoard()[i].length; j++) {
                n.move(i, j);
            }
            n.turnDone();
        }
        assertEquals(1, n.checkWinner());
    }

    @Test
    public void checkWinner2Wins() {
        Nim n = new Nim();
        for (int i = 2; i < n.getBoard().length; i++) {
            for (int j = 0; j < n.getBoard()[i].length; j++) {
                n.move(i, j);
            }
            n.turnDone();
        }
        n.move(1, 0);
        n.move(1, 1);
        n.turnDone();
        n.move(1, 2);
        assertEquals(2, n.checkWinner());
    }

    // tests for pause
    @Test
    public void pauseNothingTaken() {
        Nim n = new Nim();
        assertFalse(n.isPaused());
    }

    @Test
    public void pauseGameOver() {
        Nim n = new Nim();
        for (int i = 1; i < n.getBoard().length; i++) {
            for (int j = 0; j < n.getBoard()[i].length; j++) {
                n.move(i, j);
            }
            n.turnDone();
        }
        assertFalse(n.isPaused());
    }

    @Test
    public void pauseCorrectlyPauses() {
        Nim n = new Nim();
        n.move(0, 0);
        n.pause();
        assertTrue(n.isPaused());
    }

    @Test
    public void pauseAlreadyPaused() {
        Nim n = new Nim();
        n.move(0, 0);
        n.pause();
        n.pause();
        assertTrue(n.isPaused());
    }

    // tests for unpause
    @Test
    public void unpauseNotPaused() {
        Nim n = new Nim();
        n.unpause();
        assertFalse(n.isPaused());
    }

    @Test
    public void unpauseAlreadyPaused() {
        Nim n = new Nim();
        n.move(0, 0);
        n.pause();
        assertTrue(n.isPaused());
        n.unpause();
        assertFalse(n.isPaused());
    }

    @Test
    public void unpauseAlreadyUnpaused() {
        Nim n = new Nim();
        n.move(0, 0);
        n.pause();
        n.unpause();
        n.unpause();
        assertFalse(n.isPaused());
    }

    @Test
    public void unpauseAfterGameOver() {
        Nim n = new Nim();
        for (int i = 1; i < n.getBoard().length; i++) {
            for (int j = 0; j < n.getBoard()[i].length; j++) {
                n.move(i, j);
            }
            n.turnDone();
        }
        n.unpause();
        assertTrue(n.getGameOver());
    }

    @Test
    public void unpauseCheckAfterRunningThrough() {
        Nim n = new Nim();
        n.move(1, 0);
        n.move(1, 1);
        n.turnDone();
        n.move(2, 0);
        n.pause();
        n.unpause();
        assertFalse(n.isPaused());
        assertFalse(n.move(1, 2));
        assertTrue(n.move(2, 1));
        assertEquals(2, n.getRowNow());
        n.undo();
        assertTrue(n.getStartOfTurn());
        assertFalse(n.getCurrentPlayer());
        assertEquals(14, n.getNumLeft());
    }

    // the following are combination tests

    // resetFile would be called to, but not testing file I/O portion
    @Test
    public void resetBoardDuringGameAndContinue() {
        Nim n = new Nim();
        n.move(0, 0);
        n.turnDone();
        n.move(1, 1);
        n.move(1, 2);
        n.resetBoard();
        assertEquals(16, n.getNumLeft());
        assertTrue(n.getCell(0, 0));
        assertEquals(0, n.getRowNow());
        assertTrue(n.getCurrentPlayer());
        assertTrue(n.getStartOfTurn());
        assertFalse(n.getGameOver());
        assertFalse(n.isPaused());
        LinkedList<LinkedList<Point>> exp = new LinkedList<>();
        assertEquals(exp, n.getMovesInTurns());

        n.move(3, 0);
        n.move(3, 1);
        assertEquals(14, n.getNumLeft());
        assertFalse(n.getCell(3, 1));
        assertEquals(3, n.getRowNow());
        assertTrue(n.getCurrentPlayer());
        assertFalse(n.getStartOfTurn());
        LinkedList<LinkedList<Point>> exp2 = new LinkedList<>();
        LinkedList<Point> exp3 = new LinkedList<>();
        exp3.add(new Point(3, 0));
        exp3.add(new Point(3, 1));
        exp2.add(exp3);
        assertEquals(exp2, n.getMovesInTurns());
    }

    @Test
    public void moveAfterUndo() {
        Nim n = new Nim();
        n.move(1, 0);
        n.move(1, 1);
        n.turnDone();
        n.move(2, 0);
        n.turnDone();
        n.undo();
        n.move(3, 0);
        assertFalse(n.getCell(3, 0));
        assertEquals(13, n.getNumLeft());
        assertEquals(3, n.getRowNow());
        assertFalse(n.getCurrentPlayer());
    }
}

