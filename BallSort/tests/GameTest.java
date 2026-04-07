package tests;

import game.Game;
import model.Level;
import model.Tube;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import rules.CompositeSequenceRule;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class GameTest {

    private Game game;

    @BeforeEach
    void setUp() {
        game = new Game();
        game.startForTests();
    }

    @Test
    void test01_GetRulesReturnsCompositeRule() {
        assertNotNull(game.getRules());
        assertInstanceOf(CompositeSequenceRule.class, game.getRules());
    }

    @Test
    void test02_StartSetsLevel() {
        Game newGame = new Game();
        assertNull(newGame.getCurrentLevel());

        newGame.startForTests();

        assertNotNull(newGame.getCurrentLevel());
    }

    @Test
    void test03_ValidateMoveWithValidMove() {
        List<Tube> tubes = game.getCurrentLevel().getTubes();
        Tube from = tubes.get(0);
        Tube to = tubes.get(3);

        boolean result = game.validateMove(from, to);

        assertTrue(result);
    }

    @Test
    void test04_ValidateMoveWithFromNull() {
        List<Tube> tubes = game.getCurrentLevel().getTubes();
        Tube to = tubes.get(3);

        boolean result = game.validateMove(null, to);

        assertFalse(result);
    }

    @Test
    void test05_ValidateMoveWithToNull() {
        List<Tube> tubes = game.getCurrentLevel().getTubes();
        Tube from = tubes.get(0);

        boolean result = game.validateMove(from, null);

        assertFalse(result);
    }

    @Test
    void test06_ValidateMoveWithEmptyFromTube() {
        List<Tube> tubes = game.getCurrentLevel().getTubes();
        Tube from = tubes.get(3);
        Tube to = tubes.get(2);

        boolean result = game.validateMove(from, to);

        assertFalse(result);
    }

    @Test
    void test07_ValidateMoveWithFullToTube() {
        List<Tube> tubes = game.getCurrentLevel().getTubes();

        Tube from = tubes.get(0);
        Tube to = tubes.get(3);

        game.tryMove(from, to);

        boolean result = game.validateMove(from, to);

        assertFalse(result);
    }

    @Test
    void test08_ValidateMoveWithTubeNotInLevel() {
        List<Tube> tubes = game.getCurrentLevel().getTubes();
        Tube from = tubes.get(0);
        Tube outsideTube = new Tube(4);

        boolean result = game.validateMove(from, outsideTube);

        assertFalse(result);
    }

    @Test
    void test09_ValidateMoveWithDifferentColors() {
        List<Tube> tubes = game.getCurrentLevel().getTubes();
        Tube from = tubes.get(1);
        Tube to = tubes.get(0);

        boolean result = game.validateMove(from, to);

        assertFalse(result);
    }

    @Test
    void test10_TryMoveWithValidMove() {
        List<Tube> tubes = game.getCurrentLevel().getTubes();
        Tube from = tubes.get(0);
        Tube to = tubes.get(3);

        boolean result = game.tryMove(from, to);

        assertTrue(result);
    }

    @Test
    void test11_TryMoveUpdatesTubeState() {
        List<Tube> tubes = game.getCurrentLevel().getTubes();
        Tube from = tubes.get(0);
        Tube to = tubes.get(3);

        int fromInitialCount = from.getBallCount();
        int toInitialCount = to.getBallCount();

        game.tryMove(from, to);

        assertNotEquals(fromInitialCount, from.getBallCount());
        assertNotEquals(toInitialCount, to.getBallCount());
    }

    @Test
    void test12_IsLevelCompletedReturnsFalseInitially() {
        assertFalse(game.isLevelCompleted());
    }

    @Test
    void test13_ResetRestoresLevelState() {
        List<Tube> tubes = game.getCurrentLevel().getTubes();

        Tube from = tubes.get(0);
        Tube to = tubes.get(3);
        game.tryMove(from, to);

        assertEquals(0, from.getBallCount());
        assertEquals(2, to.getBallCount());

        game.reset();

        assertEquals(2, tubes.get(0).getBallCount());
        assertEquals(0, tubes.get(3).getBallCount());
    }

    @Test
    void test14_ResetOnNullLevelDoesNothing() {
        Game newGame = new Game();
        assertDoesNotThrow(newGame::reset);
    }

    @Test
    void test15_GetCurrentLevel() {
        assertNotNull(game.getCurrentLevel());
        assertInstanceOf(Level.class, game.getCurrentLevel());
    }

    @Test
    void test16_FullGameCycle() {
        Level level = game.getCurrentLevel();
        List<Tube> tubes = level.getTubes();

        assertFalse(game.isLevelCompleted());

        game.tryMove(tubes.get(0), tubes.get(3));
        assertEquals(0, tubes.get(0).getBallCount());
        assertEquals(2, tubes.get(3).getBallCount());
        assertFalse(game.isLevelCompleted());

        game.tryMove(tubes.get(1), tubes.get(0));
        assertEquals(1, tubes.get(0).getBallCount());
        assertEquals(0, tubes.get(1).getBallCount());
        assertFalse(game.isLevelCompleted());

        game.tryMove(tubes.get(2), tubes.get(1));
        assertEquals(1, tubes.get(1).getBallCount());
        assertEquals(0, tubes.get(2).getBallCount());
        assertFalse(game.isLevelCompleted());

        game.tryMove(tubes.get(0), tubes.get(1));
        assertEquals(0, tubes.get(0).getBallCount());
        assertEquals(2, tubes.get(1).getBallCount());

        assertTrue(game.isLevelCompleted());
    }
}