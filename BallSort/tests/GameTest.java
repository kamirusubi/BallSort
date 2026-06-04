import game.Game;
import model.Level;
import model.Tube;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import rules.CompositeSequenceRule;
import rules.SequenceRule;

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
        assertInstanceOf(SequenceRule.class, game.getRules());
    }

    @Test
    void test02_StartSetsLevel() {
        Game newGame = new Game();
        assertNull(newGame.getCurrentLevel());

        newGame.startForTests();

        assertNotNull(newGame.getCurrentLevel());
    }

    @Test
    void test03_TryMoveWithValidMove() {
        List<Tube> tubes = game.getCurrentLevel().getTubes();
        Tube from = tubes.get(0);
        Tube to = tubes.get(3);

        boolean result = game.tryMove(from, to);

        assertTrue(result);
        assertEquals(0, from.getBallCount());
        assertEquals(2, to.getBallCount());
    }

    @Test
    void test04_TryMoveWithEmptyFromTube() {
        List<Tube> tubes = game.getCurrentLevel().getTubes();
        Tube from = tubes.get(3);
        Tube to = tubes.get(2);

        int toInitialCount = to.getBallCount();
        boolean result = game.tryMove(from, to);

        assertFalse(result);
        assertEquals(0, from.getBallCount());
        assertEquals(toInitialCount, to.getBallCount());
    }

    @Test
    void test05_TryMoveWithFullToTube() {
        List<Tube> tubes = game.getCurrentLevel().getTubes();

        Tube from = tubes.get(0);
        Tube to = tubes.get(3);

        game.tryMove(from, to);
        assertEquals(2, to.getBallCount());
        assertTrue(!to.hasSpace());

        Tube anotherFrom = tubes.get(1);
        int anotherFromInitialCount = anotherFrom.getBallCount();

        boolean result = game.tryMove(anotherFrom, to);

        assertFalse(result);
        assertEquals(anotherFromInitialCount, anotherFrom.getBallCount());
        assertEquals(2, to.getBallCount());
    }

    @Test
    void test06_TryMoveWithTubeNotInLevel() {
        List<Tube> tubes = game.getCurrentLevel().getTubes();
        Tube from = tubes.get(0);
        Tube outsideTube = new Tube(4);

        int fromInitialCount = from.getBallCount();
        boolean result = game.tryMove(from, outsideTube);

        assertFalse(result);
        assertEquals(fromInitialCount, from.getBallCount());
    }

    @Test
    void test07_TryMoveWithDifferentColors() {
        List<Tube> tubes = game.getCurrentLevel().getTubes();
        Tube from = tubes.get(1);
        Tube to = tubes.get(0);

        int fromInitialCount = from.getBallCount();
        int toInitialCount = to.getBallCount();

        boolean result = game.tryMove(from, to);

        assertFalse(result);
        assertEquals(fromInitialCount, from.getBallCount());
        assertEquals(toInitialCount, to.getBallCount());
    }

    @Test
    void test08_TryMoveUpdatesTubeState() {
        List<Tube> tubes = game.getCurrentLevel().getTubes();
        Tube from = tubes.get(0);
        Tube to = tubes.get(3);

        int fromInitialCount = from.getBallCount();
        int toInitialCount = to.getBallCount();

        game.tryMove(from, to);

        assertNotEquals(fromInitialCount, from.getBallCount());
        assertNotEquals(toInitialCount, to.getBallCount());
        assertEquals(fromInitialCount - 2, from.getBallCount());
        assertEquals(toInitialCount + 2, to.getBallCount());
    }

    @Test
    void test09_IsLevelCompletedReturnsFalseInitially() {
        assertFalse(game.isLevelCompleted());
    }

    @Test
    void test10_ResetRestoresLevelState() {
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
    void test11_ResetOnNullLevelDoesNothing() {
        Game newGame = new Game();
        assertDoesNotThrow(newGame::reset);
    }

    @Test
    void test12_GetCurrentLevel() {
        assertNotNull(game.getCurrentLevel());
        assertInstanceOf(Level.class, game.getCurrentLevel());
    }

    @Test
    void test13_FullGameCycle() {
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

    @Test
    void test14_TryMoveReturnsFalseForInvalidMoveAndRollback() {
        List<Tube> tubes = game.getCurrentLevel().getTubes();
        Tube from = tubes.get(0);
        Tube to = tubes.get(1);

        int fromCount = from.getBallCount();
        int toCount = to.getBallCount();

        boolean result = game.tryMove(from, to);

        assertFalse(result);
        assertEquals(fromCount, from.getBallCount());
        assertEquals(toCount, to.getBallCount());
    }

    @Test
    void test15_TryMoveFromSelectedTubeToItself() {
        List<Tube> tubes = game.getCurrentLevel().getTubes();
        Tube tube = tubes.get(0);

        tube.setSelected(true);
        int initialCount = tube.getBallCount();

        boolean result = game.tryMove(tube, tube);

        assertFalse(result);
        assertEquals(initialCount, tube.getBallCount());
        assertFalse(tube.isSelected());
    }

    @Test
    void test16_MultipleValidMovesInSequence() {
        List<Tube> tubes = game.getCurrentLevel().getTubes();

        assertTrue(game.tryMove(tubes.get(0), tubes.get(3)));

        assertTrue(game.tryMove(tubes.get(1), tubes.get(0)));

        assertTrue(game.tryMove(tubes.get(2), tubes.get(1)));

        assertTrue(game.tryMove(tubes.get(0), tubes.get(1)));

        assertTrue(game.isLevelCompleted());
    }

    @Test
    void test17_TryMoveWithPartialSequence() {
        List<Tube> tubes = game.getCurrentLevel().getTubes();

        Tube from = tubes.get(0);
        Tube to = tubes.get(3);

        game.tryMove(from, to);

        assertEquals(0, from.getBallCount());
        assertEquals(2, to.getBallCount());
    }
}