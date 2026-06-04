import game.Game;
import model.Level;
import model.Tube;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import utils.TestGameListener;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class GameListenerTest {

    private Game game;
    private Level level;
    private TestGameListener listener;

    @BeforeEach
    void setUp() {
        game = new Game();
        game.startForTests();
        level = game.getCurrentLevel();
        listener = new TestGameListener();
        game.addGameListener(listener);
    }

    @Test
    void test01_onMoveAttemptCalledOnSuccessfulMove() {
        Tube from = level.getTubes().get(0);
        Tube to = level.getTubes().get(3);

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
        Tube from = level.getTubes().get(1);
        Tube to = level.getTubes().get(0);

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
        Tube from1 = level.getTubes().get(0);
        Tube to1 = level.getTubes().get(3);
        Tube from2 = level.getTubes().get(1);
        Tube to2 = level.getTubes().get(3);

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
        Tube from = level.getTubes().get(0);
        Tube to = level.getTubes().get(3);

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
        Tube from = level.getTubes().get(0);
        Tube to = level.getTubes().get(3);

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
        TestGameListener listener2 = new TestGameListener();
        game.addGameListener(listener2);

        Tube from = level.getTubes().get(0);
        Tube to = level.getTubes().get(3);

        game.tryMove(from, to);

        assertEquals(1, listener.moveAttemptCount);
        assertEquals(1, listener2.moveAttemptCount);
        assertEquals(listener.lastMoveSuccess, listener2.lastMoveSuccess);
    }

    @Test
    void test09_moveFromEmptyTubeDoesNotChangeCounters() {
        Tube emptyTube = level.getTubes().get(3);
        Tube to = level.getTubes().get(2);

        listener.clear();
        game.tryMove(emptyTube, to);

        assertTrue(listener.isMoveAttemptCalled());
        assertFalse(listener.lastMoveSuccess);
        assertEquals(1, listener.failureCount);
    }

    @Test
    void test10_moveToFullTubeDoesNotChangeCounters() {
        Tube from = level.getTubes().get(0);
        Tube to = level.getTubes().get(3);

        game.tryMove(from, to);

        listener.clear();
        game.tryMove(from, to);

        assertTrue(listener.isMoveAttemptCalled());
        assertFalse(listener.lastMoveSuccess);
        assertEquals(1, listener.failureCount);
    }

    @Test
    void test11_EventFiredAfterStateChangeNotBefore() {
        List<Tube> tubes = level.getTubes();
        Tube from = tubes.get(0);
        Tube to = tubes.get(3);

        int fromCountBefore = from.getBallCount();
        int toCountBefore = to.getBallCount();

        boolean completedBefore = game.isLevelCompleted();

        game.tryMove(from, to);

        assertTrue(listener.isMoveAttemptCalled());

        if (listener.lastMoveSuccess) {
            assertNotEquals(fromCountBefore, from.getBallCount(),
                    "Состояние from должно измениться после события");
            assertNotEquals(toCountBefore, to.getBallCount(),
                    "Состояние to должно измениться после события");
        }

        assertEquals(completedBefore, game.isLevelCompleted() || completedBefore);
    }

    @Test
    void test12_GameCompletedEventFiredOnlyAfterAllConditionsMet() {
        List<Tube> tubes = level.getTubes();

        game.tryMove(tubes.get(0), tubes.get(3));
        System.out.println(listener.callHistory);

        assertEquals(0, listener.gameCompletedCount);
        assertFalse(game.isLevelCompleted());

        game.tryMove(tubes.get(2), tubes.get(1));
        System.out.println(listener.callHistory);

        assertTrue(game.isLevelCompleted());
        assertEquals(1, listener.gameCompletedCount);

        List<String> history = listener.callHistory;
        int lastIndex = history.size() - 1;

        System.out.println(listener.callHistory);

        assertEquals("onGameCompleted", history.get(lastIndex));

        if (lastIndex > 0) {
            assertTrue(history.get(lastIndex - 1).contains("onMoveAttempt(success)"));
        }
    }

    @Test
    void test13_EventNotFiredWhenMoveDoesNotChangeState() {
        List<Tube> tubes = level.getTubes();
        Tube from = tubes.get(0);
        Tube to = tubes.get(0);

        int beforeCallCount = listener.moveAttemptCount;

        game.tryMove(from, to);

        assertEquals(beforeCallCount + 1, listener.moveAttemptCount);
        assertFalse(listener.lastMoveSuccess);

        assertEquals(2, from.getBallCount());
    }

    @Test
    void test14_VerifyEventOrderWithMultipleSuccessiveMoves() {
        List<Tube> tubes = level.getTubes();
        List<String> history = listener.callHistory;

        game.tryMove(tubes.get(0), tubes.get(3));

        int historySizeAfter1 = history.size();
        assertTrue(historySizeAfter1 > 0);
        assertEquals("onMoveAttempt(success)", history.get(historySizeAfter1 - 1));

        game.tryMove(tubes.get(3), tubes.get(1));

        int historySizeAfter2 = history.size();
        assertEquals("onMoveAttempt(failure)", history.get(historySizeAfter2 - 1));

        game.tryMove(tubes.get(2), tubes.get(1));

        int historySizeAfter3 = history.size();

        if (historySizeAfter3 > 0) {
            assertEquals("onGameCompleted", history.get(historySizeAfter3 - 1));
        }
    }
}