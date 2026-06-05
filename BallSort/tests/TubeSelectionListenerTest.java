import game.Game;
import model.Level;
import model.Tube;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import rules.SequenceRule;
import utils.TestTubeSelectionListener;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class TubeSelectionListenerTest {

    private Game game;
    private Level level;
    private TestTubeSelectionListener listener;

    @BeforeEach
    void setUp() {
        game = new Game();
        game.startForTests();
        level = game.getCurrentLevel();
        listener = new TestTubeSelectionListener();
        level.addTubeSelectionListener(listener);
    }

    @Test
    void test01_onFirstTubeSelectedWhenTubeIsSelected() {
        Tube tube = level.getTubes().get(0);

        tube.setSelected(true);

        assertEquals(1, listener.firstTubeSelectedCount);
        assertEquals(0, listener.firstTubeDeselectedCount);
        assertEquals(tube, listener.selectedTube);
        assertTrue(listener.isFirstTubeSelectedCalled());
    }

    @Test
    void test02_onFirstTubeDeselectedWhenTubeIsDeselected() {
        Tube tube = level.getTubes().get(0);

        tube.setSelected(true);
        tube.setSelected(false);

        assertEquals(1, listener.firstTubeSelectedCount);
        assertEquals(1, listener.firstTubeDeselectedCount);
        assertEquals(tube, listener.deselectedTube);
        assertTrue(listener.isFirstTubeDeselectedCalled());
    }

    @Test
    void test03_selectingSameTubeTwiceDoesDeselect() {
        Tube tube = level.getTubes().get(0);

        tube.setSelected(true);
        tube.setSelected(true);

        assertEquals(1, listener.firstTubeSelectedCount);
        assertEquals(1, listener.firstTubeDeselectedCount);
    }

    @Test
    void test04_selectingEmptyTube() {
        Tube emptyTube = level.getTubes().get(3);

        emptyTube.setSelected(true);

        assertEquals(0, listener.firstTubeSelectedCount);
        assertEquals(1, listener.firstTubeDeselectedCount);
        assertFalse(emptyTube.isSelected());
    }

    @Test
    void test05_singleTubeSelectionWorks() {
        Tube tube = level.getTubes().get(0);

        listener.clear();

        tube.setSelected(true);

        assertEquals(1, listener.firstTubeSelectedCount);
        assertTrue(tube.isSelected());

        tube.setSelected(false);

        assertEquals(1, listener.firstTubeDeselectedCount);
        assertFalse(tube.isSelected());
    }

    @Test
    void test06_twoTubesSelectionTriggersMove() {
        Tube tube1 = level.getTubes().get(0);
        Tube tube2 = level.getTubes().get(3);

        int initialCount1 = tube1.getBallCount();
        int initialCount2 = tube2.getBallCount();

        listener.clear();

        tube1.setSelected(true);

        int afterFirstSelected = listener.firstTubeSelectedCount;

        tube2.setSelected(true);

        assertTrue(afterFirstSelected > 0);

        if (initialCount2 == 0 && !tube1.isEmpty()) {
            assertTrue(tube1.getBallCount() < initialCount1 || tube2.getBallCount() > initialCount2);
        }
    }

    @Test
    void test07_multipleListenersAllReceiveEvents() {
        TestTubeSelectionListener listener2 = new TestTubeSelectionListener();
        level.addTubeSelectionListener(listener2);

        Tube tube = level.getTubes().get(0);

        listener.clear();
        listener2.clear();

        tube.setSelected(true);

        assertEquals(1, listener.firstTubeSelectedCount);
        assertEquals(1, listener2.firstTubeSelectedCount);
        assertEquals(tube, listener.selectedTube);
        assertEquals(tube, listener2.selectedTube);
    }

    @Test
    void test08_deselectedAfterMove() {
        Tube tube1 = level.getTubes().get(0);
        Tube tube2 = level.getTubes().get(3);

        tube1.setSelected(true);
        assertTrue(tube1.isSelected());

        tube2.setSelected(true);

        assertFalse(tube1.isSelected());
        assertFalse(tube2.isSelected());

        assertTrue(listener.firstTubeDeselectedCount > 0);
    }

    @Test
    void test09_selectionEventFiredAfterTubeStateChanged() {
        Tube tube = level.getTubes().get(0);

        assertFalse(tube.isSelected());

        tube.setSelected(true);

        assertTrue(listener.isFirstTubeSelectedCalled());
        assertTrue(tube.isSelected());

        listener.clear();

        tube.setSelected(false);

        assertTrue(listener.isFirstTubeDeselectedCalled());
        assertFalse(tube.isSelected());
    }

    @Test
    void test10_emptyTubeCannotBeSelected() {
        Tube emptyTube = level.getTubes().get(3);

        assertTrue(emptyTube.isEmpty());

        listener.clear();
        emptyTube.setSelected(true);

        assertFalse(emptyTube.isSelected());
        assertEquals(0, listener.firstTubeSelectedCount);
    }

    @Test
    void test11_eventNotFiredWhenSelectionStateDoesNotChange() {
        Tube tube = level.getTubes().get(0);

        tube.setSelected(true);
        int selectedCount = listener.firstTubeSelectedCount;

        tube.setSelected(true);

        assertEquals(selectedCount, listener.firstTubeSelectedCount);
    }

    @Test
    void test12_selectMultipleTubesSequentially() {
        Tube tube1 = level.getTubes().get(0);
        Tube tube2 = level.getTubes().get(1);
        Tube tube3 = level.getTubes().get(2);

        listener.clear();

        tube1.setSelected(true);
        int selectedCount = listener.firstTubeSelectedCount;
        assertTrue(selectedCount >= 1);

        tube1.setSelected(false);

        tube2.setSelected(true);
        assertTrue(listener.firstTubeSelectedCount >= selectedCount + 1);

        tube2.setSelected(false);

        tube3.setSelected(true);
        assertTrue(listener.firstTubeSelectedCount >= selectedCount + 2);
    }

    @Test
    void test13_removeListenerStopsReceivingEvents() {
        Tube tube = level.getTubes().get(0);

        level.removeTubeSelectionListener(listener);

        listener.clear();
        tube.setSelected(true);

        assertEquals(0, listener.firstTubeSelectedCount);

        tube.setSelected(false);

        assertEquals(0, listener.firstTubeDeselectedCount);
    }

    @Test
    void test14_tubeFromAnotherLevelDoesNotAffectLevel() {
        Tube tubeFromLevel = level.getTubes().get(0);
        SequenceRule rule = level.getRules();
        Tube outsideTube = new Tube(4, rule);

        int initialCount = tubeFromLevel.getBallCount();

        outsideTube.setSelected(true);

        assertEquals(initialCount, tubeFromLevel.getBallCount());
    }


    @Test
    void test15_verifyFirstTubeSelectedEvent() {
        Tube tube = level.getTubes().get(0);

        listener.clear();

        assertNotNull(listener);

        tube.setSelected(true);

        assertTrue(listener.firstTubeSelectedCount > 0);
    }
}