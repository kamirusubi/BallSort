import game.Game;
import game.GameListener;
import model.Level;
import model.Tube;
import model.TubeSelectionListener;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ListenerTest {

    private Game game;
    private Level level;

    @BeforeEach
    void setUp() {
        game = new Game();
        game.startForTests();
        level = game.getCurrentLevel();
    }

    @Test
    void test01_GameListenerOnMoveAttemptSuccess() {
        TestGameListener listener = new TestGameListener();
        game.addGameListener(listener);

        Tube from = level.getTubes().get(0);
        Tube to = level.getTubes().get(3);
        game.tryMove(from, to);

        assertTrue(listener.moveAttemptCalled);
        assertTrue(listener.moveSuccess);
        assertEquals(from, listener.moveFrom);
        assertEquals(to, listener.moveTo);
    }

    @Test
    void test02_GameListenerOnMoveAttemptFailed() {
        TestGameListener listener = new TestGameListener();
        game.addGameListener(listener);

        Tube from = level.getTubes().get(1);
        Tube to = level.getTubes().get(0);
        game.tryMove(from, to);

        assertTrue(listener.moveAttemptCalled);
        assertFalse(listener.moveSuccess);
        assertEquals(from, listener.moveFrom);
        assertEquals(to, listener.moveTo);
    }

    @Test
    void test03_GameListenerOnGameCompleted() {
        TestGameListener listener = new TestGameListener();
        game.addGameListener(listener);

        game.tryMove(level.getTubes().get(0), level.getTubes().get(3));
        game.tryMove(level.getTubes().get(1), level.getTubes().get(0));
        game.tryMove(level.getTubes().get(2), level.getTubes().get(1));
        game.tryMove(level.getTubes().get(0), level.getTubes().get(1));

        assertTrue(game.isLevelCompleted());
        assertTrue(listener.gameCompletedCalled);
    }

    @Test
    void test04_TubeSelectionListenerOnFirstTubeSelected() {
        TestTubeSelectionListener listener = new TestTubeSelectionListener();
        level.addTubeSelectionListener(listener);

        Tube tube = level.getTubes().get(0);
        tube.setSelected(true);

        assertTrue(listener.firstTubeSelectedCalled);
        assertEquals(tube, listener.selectedTube);
    }

    @Test
    void test05_TubeSelectionListenerOnFirstTubeDeselected() {
        TestTubeSelectionListener listener = new TestTubeSelectionListener();
        level.addTubeSelectionListener(listener);

        Tube tube = level.getTubes().get(0);
        tube.setSelected(true);
        tube.setSelected(false);

        assertTrue(listener.firstTubeDeselectedCalled);
        assertEquals(tube, listener.deselectedTube);
    }

    @Test
    void test06_TubeSelectionListenerOnTwoTubesSelected() {
        TestTubeSelectionListener listener = new TestTubeSelectionListener();
        level.addTubeSelectionListener(listener);

        Tube tube1 = level.getTubes().get(0);
        Tube tube2 = level.getTubes().get(3);

        tube1.setSelected(true);
        tube2.setSelected(true);

        assertTrue(listener.twoTubesSelectedCalled);
        assertEquals(tube1, listener.twoTubesFrom);
        assertEquals(tube2, listener.twoTubesTo);
    }

    private static class TestGameListener implements GameListener {
        public boolean moveAttemptCalled = false;
        public boolean moveSuccess = false;
        public Tube moveFrom = null;
        public Tube moveTo = null;
        public boolean gameCompletedCalled = false;

        @Override
        public void onMoveAttempt(boolean success, Tube from, Tube to) {
            moveAttemptCalled = true;
            moveSuccess = success;
            moveFrom = from;
            moveTo = to;
        }

        @Override
        public void onGameCompleted() {
            gameCompletedCalled = true;
        }
    }

    private static class TestTubeSelectionListener implements TubeSelectionListener {
        public boolean firstTubeSelectedCalled = false;
        public boolean firstTubeDeselectedCalled = false;
        public boolean twoTubesSelectedCalled = false;

        public Tube selectedTube = null;
        public Tube deselectedTube = null;
        public Tube twoTubesFrom = null;
        public Tube twoTubesTo = null;

        @Override
        public void onFirstTubeSelected(Tube tube) {
            firstTubeSelectedCalled = true;
            selectedTube = tube;
        }

        @Override
        public void onFirstTubeDeselected(Tube tube) {
            firstTubeDeselectedCalled = true;
            deselectedTube = tube;
        }

        @Override
        public void onTwoTubesSelected(Tube from, Tube to) {
            twoTubesSelectedCalled = true;
            twoTubesFrom = from;
            twoTubesTo = to;
        }
    }
}