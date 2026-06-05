import game.Game;
import model.Level;
import model.Tube;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import utils.TestLevelListener;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class GameListenerTest {

    private Game game;
    private Level level;
    private TestLevelListener listener;

    @BeforeEach
    void setUp() {
        game = new Game();
        game.startForTests();
        level = game.getCurrentLevel();
        listener = new TestLevelListener();
        level.addLevelListener(listener);
    }

    @Test
    void test01_onMoveAttemptCalledOnSuccessfulMove() {
        List<Tube> tubes = level.getTubes();
        Tube from = tubes.get(0);
        Tube to = tubes.get(3);

        game.tryMove(from, to);

        assertTrue(listener.isMoveAttemptCalled());
        assertEquals(1, listener.moveAttemptCount);
        assertTrue(listener.lastMoveSuccess);
        assertEquals(1, listener.successCount);
        assertEquals(0, listener.failureCount);
        assertEquals(from, listener.moveFrom);
        assertEquals(to, listener.moveTo);
    }

    @Test
    void test02_onMoveAttemptCalledOnFailedMove() {
        List<Tube> tubes = level.getTubes();
        Tube from = tubes.get(1);
        Tube to = tubes.get(0);

        game.tryMove(from, to);

        assertTrue(listener.isMoveAttemptCalled());
        assertEquals(1, listener.moveAttemptCount);
        assertFalse(listener.lastMoveSuccess);
        assertEquals(0, listener.successCount);
        assertEquals(1, listener.failureCount);
        assertEquals(from, listener.moveFrom);
        assertEquals(to, listener.moveTo);
    }

    @Test
    void test03_multipleMovesProduceMultipleEvents() {
        List<Tube> tubes = level.getTubes();
        Tube from1 = tubes.get(0);
        Tube to1 = tubes.get(3);
        Tube from2 = tubes.get(1);
        Tube to2 = tubes.get(3);

        game.tryMove(from1, to1);
        game.tryMove(from2, to2);

        assertEquals(2, listener.moveAttemptCount);
        assertEquals(1, listener.successCount);
        assertEquals(1, listener.failureCount);
    }

    @Test
    void test04_onGameCompletedCalledWhenLevelIsFinished() {
        List<Tube> tubes = level.getTubes();

        game.tryMove(tubes.get(0), tubes.get(3));
        game.tryMove(tubes.get(1), tubes.get(0));
        game.tryMove(tubes.get(2), tubes.get(1));

        assertEquals(0, listener.gameCompletedCount);

        game.tryMove(tubes.get(0), tubes.get(1));

        assertTrue(game.isLevelCompleted());
        assertEquals(1, listener.gameCompletedCount);
    }

    @Test
    void test05_onGameCompletedCalledOnlyOnce() {
        List<Tube> tubes = level.getTubes();

        game.tryMove(tubes.get(0), tubes.get(3));
        game.tryMove(tubes.get(1), tubes.get(0));
        game.tryMove(tubes.get(2), tubes.get(1));
        game.tryMove(tubes.get(0), tubes.get(1));

        game.tryMove(tubes.get(0), tubes.get(1));

        assertEquals(1, listener.gameCompletedCount);
    }

    @Test
    void test06_eventOrderForSuccessfulMove() {
        List<Tube> tubes = level.getTubes();
        Tube from = tubes.get(0);
        Tube to = tubes.get(3);

        listener.clear();
        game.tryMove(from, to);

        List<String> history = listener.callHistory;
        assertFalse(history.isEmpty());
        assertTrue(history.get(0).startsWith("onMoveAttempt"));

        if (game.isLevelCompleted()) {
            assertEquals(2, history.size());
            assertEquals("onGameCompleted", history.get(1));
        }
    }

    @Test
    void test07_clearResetsAllCounters() {
        List<Tube> tubes = level.getTubes();
        Tube from = tubes.get(0);
        Tube to = tubes.get(3);

        game.tryMove(from, to);
        listener.clear();

        assertEquals(0, listener.moveAttemptCount);
        assertEquals(0, listener.successCount);
        assertEquals(0, listener.failureCount);
        assertEquals(0, listener.gameCompletedCount);
        assertNull(listener.moveFrom);
        assertNull(listener.moveTo);
        assertNull(listener.lastMoveSuccess);
        assertTrue(listener.callHistory.isEmpty());
    }

    @Test
    void test08_multipleListenersAllReceiveEvents() {
        TestLevelListener listener2 = new TestLevelListener();
        level.addLevelListener(listener2);

        List<Tube> tubes = level.getTubes();
        Tube from = tubes.get(0);
        Tube to = tubes.get(3);

        game.tryMove(from, to);

        assertEquals(1, listener.moveAttemptCount);
        assertEquals(1, listener2.moveAttemptCount);
        assertEquals(listener.lastMoveSuccess, listener2.lastMoveSuccess);
    }

    @Test
    void test09_moveFromEmptyTubeDoesNotChangeCounters() {
        List<Tube> tubes = level.getTubes();
        Tube emptyTube = tubes.get(3);
        Tube to = tubes.get(2);

        listener.clear();
        game.tryMove(emptyTube, to);

        assertTrue(listener.isMoveAttemptCalled());
        assertFalse(listener.lastMoveSuccess);
        assertEquals(1, listener.failureCount);
        assertEquals(0, listener.successCount);
    }

    @Test
    void test10_moveToFullTubeDoesNotChangeCounters() {
        List<Tube> tubes = level.getTubes();
        Tube from = tubes.get(0);
        Tube to = tubes.get(3);

        game.tryMove(from, to);

        listener.clear();
        game.tryMove(from, to);

        assertTrue(listener.isMoveAttemptCalled());
        assertFalse(listener.lastMoveSuccess);
        assertEquals(1, listener.failureCount);
    }

    @Test
    void test11_eventFiredAfterStateChangeNotBefore() {
        List<Tube> tubes = level.getTubes();
        Tube from = tubes.get(0);
        Tube to = tubes.get(3);

        int fromCountBefore = from.getBallCount();
        int toCountBefore = to.getBallCount();
        boolean completedBefore = game.isLevelCompleted();

        listener.clear();
        game.tryMove(from, to);

        assertTrue(listener.isMoveAttemptCalled());

        if (listener.lastMoveSuccess) {
            assertNotEquals(fromCountBefore, from.getBallCount());
            assertNotEquals(toCountBefore, to.getBallCount());
        }

        assertEquals(completedBefore, game.isLevelCompleted() || completedBefore);
    }

    @Test
    void test12_gameCompletedEventFiredOnlyAfterAllConditionsMet() {
        List<Tube> tubes = level.getTubes();

        game.tryMove(tubes.get(0), tubes.get(3));

        assertEquals(0, listener.gameCompletedCount);
        assertFalse(game.isLevelCompleted());

        game.tryMove(tubes.get(2), tubes.get(1));

        assertTrue(game.isLevelCompleted());
        assertEquals(1, listener.gameCompletedCount);

        List<String> history = listener.callHistory;
        int lastIndex = history.size() - 1;

        assertEquals("onGameCompleted", history.get(lastIndex));

        if (lastIndex > 0) {
            assertTrue(history.get(lastIndex - 1).contains("onMoveAttempt(success)"));
        }
    }

    @Test
    void test13_moveFromSelectedTubeToItself() {
        List<Tube> tubes = level.getTubes();
        Tube tube = tubes.get(0);

        tube.setSelected(true);
        int initialCount = tube.getBallCount();

        listener.clear();
        boolean result = game.tryMove(tube, tube);

        assertFalse(result);
        assertEquals(initialCount, tube.getBallCount());
        assertFalse(listener.lastMoveSuccess);
    }
}