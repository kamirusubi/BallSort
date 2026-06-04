import game.Game;
import model.Level;
import model.Tube;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
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
        assertEquals(0, listener.twoTubesSelectedCount);
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
        assertEquals(0, listener.twoTubesSelectedCount);
        assertEquals(tube, listener.deselectedTube);
        assertTrue(listener.isFirstTubeDeselectedCalled());
    }

    @Test
    void test03_onTwoTubesSelectedWhenSecondTubeIsSelected() {
        Tube tube1 = level.getTubes().get(0);
        Tube tube2 = level.getTubes().get(3);

        tube1.setSelected(true);
        tube2.setSelected(true);

        assertEquals(1, listener.firstTubeSelectedCount);
        assertEquals(0, listener.firstTubeDeselectedCount);
        assertEquals(1, listener.twoTubesSelectedCount);
        assertEquals(tube1, listener.twoTubesFrom);
        assertEquals(tube2, listener.twoTubesTo);
        assertTrue(listener.isTwoTubesSelectedCalled());
    }

    @Test
    void test04_selectingSameTubeTwiceDoesNotDeselect() {
        Tube tube = level.getTubes().get(0);

        tube.setSelected(true);
        tube.setSelected(true);

        assertEquals(1, listener.firstTubeSelectedCount);
        assertEquals(0, listener.firstTubeDeselectedCount);
        assertEquals(0, listener.twoTubesSelectedCount);
    }

    @Test
    void test05_selectingEmptyTube() {
        Tube emptyTube = level.getTubes().get(3);

        emptyTube.setSelected(true);

        assertEquals(0, listener.firstTubeSelectedCount);
        assertEquals(1, listener.firstTubeDeselectedCount);
        assertEquals(0, listener.twoTubesSelectedCount);
        assertFalse(emptyTube.isSelected());
    }

    @Test
    void test06_multipleSelectionSequences() {
        Tube tube1 = level.getTubes().get(0);
        Tube tube2 = level.getTubes().get(3);

        tube1.setSelected(true);
        tube2.setSelected(true);

        assertEquals(1, listener.firstTubeSelectedCount);
        assertEquals(0, listener.firstTubeDeselectedCount);
        assertEquals(1, listener.twoTubesSelectedCount);

        tube1.setSelected(false);
        tube2.setSelected(false);
        listener.clear();

        tube1.setSelected(true);
        tube2.setSelected(true);

        assertEquals(1, listener.firstTubeSelectedCount);
        assertEquals(0, listener.firstTubeDeselectedCount);
        assertEquals(1, listener.twoTubesSelectedCount);
    }

    @Test
    void test07_eventOrderForTwoTubes() {
        Tube tube1 = level.getTubes().get(0);
        Tube tube2 = level.getTubes().get(3);

        tube1.setSelected(true);
        tube2.setSelected(true);

        List<String> history = listener.callHistory;
        assertEquals(2, history.size());
        assertEquals("onTwoTubesSelected", history.get(1));
    }

    @Test
    void test08_multipleListenersAllReceiveEvents() {
        TestTubeSelectionListener listener2 = new TestTubeSelectionListener();
        level.addTubeSelectionListener(listener2);

        Tube tube = level.getTubes().get(0);

        tube.setSelected(true);

        assertEquals(1, listener.firstTubeSelectedCount);
        assertEquals(1, listener2.firstTubeSelectedCount);
        assertEquals(tube, listener.selectedTube);
        assertEquals(tube, listener2.selectedTube);
    }

    @Test
    void test09_checkSelectedTubes() {
        Tube tube1 = level.getTubes().get(0);
        Tube tube3 = level.getTubes().get(1);

        tube1.setSelected(true);
        tube3.setSelected(true);

        assertEquals(1, listener.firstTubeSelectedCount);
        assertEquals(0, listener.firstTubeDeselectedCount);
        assertEquals(1, listener.twoTubesSelectedCount);
        assertEquals(tube1, listener.twoTubesFrom);
        assertEquals(tube3, listener.twoTubesTo);
    }

    @Test
    void test10_deselectAfterTwoTubesSelection() {
        Tube tube1 = level.getTubes().get(0);
        Tube tube2 = level.getTubes().get(3);

        tube1.setSelected(true);
        tube2.setSelected(true);

        listener.clear();

        tube1.setSelected(false);

        assertEquals(0, listener.firstTubeSelectedCount);
        assertEquals(1, listener.firstTubeDeselectedCount);
        assertEquals(0, listener.twoTubesSelectedCount);
    }

    @Test
    void test11_SelectionEventFiredAfterTubeStateChanged() {
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
    void test12_EmptyTubeSelectionEventFiredThenImmediatelyDeselected() {
        Tube emptyTube = level.getTubes().get(3);

        assertTrue(emptyTube.isEmpty());

        listener.clear();
        emptyTube.setSelected(true);

        assertEquals(0, listener.firstTubeSelectedCount);
        assertTrue(listener.firstTubeDeselectedCount > 0);

        assertFalse(emptyTube.isSelected());
    }

    @Test
    void test13_VerifyTwoTubesEventFiredAfterBothSelectionsComplete() {
        Tube tube1 = level.getTubes().get(0);
        Tube tube2 = level.getTubes().get(3);

        listener.clear();

        tube1.setSelected(true);
        assertEquals(1, listener.firstTubeSelectedCount);
        assertEquals(0, listener.twoTubesSelectedCount);

        tube2.setSelected(true);

        assertEquals(1, listener.firstTubeSelectedCount);
        assertEquals(1, listener.twoTubesSelectedCount);

        List<String> history = listener.callHistory;
        assertEquals(2, history.size());
        assertEquals("onFirstTubeSelected", history.get(0));
        assertEquals("onTwoTubesSelected", history.get(1));

        assertTrue(tube1.isSelected());
        assertTrue(tube2.isSelected());
    }

    @Test
    void test14_EventNotFiredWhenSelectionStateDoesNotChange() {
        Tube tube = level.getTubes().get(0);

        listener.clear();
        tube.setSelected(true);
        int selectedCount = listener.firstTubeSelectedCount;

        tube.setSelected(true);

        assertEquals(selectedCount, listener.firstTubeSelectedCount);
    }
}