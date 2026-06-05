package ListenerTests;

import game.Game;
import game.GameEventListener;
import model.Level;
import model.Tube;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class GameEventListenerTest {

    private Game game;
    private Level level;
    private TestGameEventListener listener;

    @BeforeEach
    void setUp() {
        game = new Game();
        game.startForTests();
        level = game.getCurrentLevel();
        listener = new TestGameEventListener();
        level.addEventListener(listener);
    }

    // Событие onTubeSelected вызывается при выборе трубы
    @Test
    void test01_onTubeSelectedCalledWhenTubeSelected() {
        Tube tube = level.getTubeAt(0);

        level.selectTube(tube);

        assertTrue(listener.isTubeSelectedCalled());
        assertEquals(1, listener.tubeSelectedCount);
        assertEquals(tube, listener.lastSelectedTube);
        assertEquals(2, listener.lastLiftedCount);
    }

    // Событие onTubeDeselected вызывается при снятии выбора трубы
    @Test
    void test02_onTubeDeselectedCalledWhenTubeDeselected() {
        Tube tube = level.getTubeAt(0);

        level.selectTube(tube);
        level.selectTube(tube);

        assertEquals(1, listener.tubeDeselectedCount);
        assertEquals(tube, listener.lastDeselectedTube);
    }

    // Событие onMoveSucceeded вызывается при успешном перемещении
    @Test
    void test03_onMoveSucceededCalledOnSuccessfulMove() {
        Tube from = level.getTubeAt(0);
        Tube to = level.getTubeAt(3);

        level.selectTube(from);
        level.selectTube(to);

        assertTrue(listener.isMoveSucceededCalled());
        assertEquals(1, listener.moveSucceededCount);
        assertEquals(from, listener.lastMoveFrom);
        assertEquals(to, listener.lastMoveTo);
    }

    // Событие onMoveFailed вызывается при неудачном перемещении
    @Test
    void test04_onMoveFailedCalledOnFailedMove() {
        Tube from = level.getTubeAt(1);
        Tube to = level.getTubeAt(0);

        level.selectTube(from);
        level.selectTube(to);

        assertTrue(listener.isMoveFailedCalled());
        assertEquals(1, listener.moveFailedCount);
        assertEquals(from, listener.lastMoveFrom);
        assertEquals(to, listener.lastMoveTo);
    }

    // Несколько ходов порождают несколько событий
    @Test
    void test05_multipleMovesProduceMultipleEvents() {
        Tube from1 = level.getTubeAt(0);
        Tube to1 = level.getTubeAt(3);
        Tube from2 = level.getTubeAt(1);
        Tube to2 = level.getTubeAt(3);

        level.selectTube(from1);
        level.selectTube(to1);

        level.selectTube(from2);
        level.selectTube(to2);

        assertEquals(1, listener.moveSucceededCount);
        assertEquals(1, listener.moveFailedCount);
    }

    // Событие onGameCompleted вызывается при завершении уровня
    @Test
    void test06_onGameCompletedCalledWhenLevelIsFinished() {
        Tube tube0 = level.getTubeAt(0);
        Tube tube1 = level.getTubeAt(1);
        Tube tube2 = level.getTubeAt(2);
        Tube tube3 = level.getTubeAt(3);

        level.selectTube(tube0);
        level.selectTube(tube3);

        level.selectTube(tube1);
        level.selectTube(tube0);

        level.selectTube(tube2);
        level.selectTube(tube1);

        level.selectTube(tube0);
        level.selectTube(tube1);

        assertTrue(level.isLevelCompleted());
        assertEquals(1, listener.gameCompletedCount);
    }

    // Событие onGameCompleted вызывается только один раз
    @Test
    void test07_onGameCompletedCalledOnlyOnce() {
        Tube tube0 = level.getTubeAt(0);
        Tube tube1 = level.getTubeAt(1);
        Tube tube2 = level.getTubeAt(2);
        Tube tube3 = level.getTubeAt(3);

        level.selectTube(tube0);
        level.selectTube(tube3);

        level.selectTube(tube1);
        level.selectTube(tube0);

        level.selectTube(tube2);
        level.selectTube(tube1);

        level.selectTube(tube0);
        level.selectTube(tube1);

        level.selectTube(tube0);
        level.selectTube(tube1);

        assertEquals(1, listener.gameCompletedCount);
    }

    // Множественные слушатели все получают события
    @Test
    void test08_multipleListenersAllReceiveEvents() {
        TestGameEventListener listener2 = new TestGameEventListener();
        level.addEventListener(listener2);

        Tube from = level.getTubeAt(0);
        Tube to = level.getTubeAt(3);

        level.selectTube(from);
        level.selectTube(to);

        assertEquals(1, listener.moveSucceededCount);
        assertEquals(1, listener2.moveSucceededCount);
    }

    // Удаление слушателя останавливает получение событий
    @Test
    void test09_removeListenerStopsReceivingEvents() {
        level.removeEventListener(listener);
        listener.clear();

        Tube from = level.getTubeAt(0);
        Tube to = level.getTubeAt(3);

        level.selectTube(from);
        level.selectTube(to);

        assertEquals(0, listener.moveSucceededCount);
        assertEquals(0, listener.tubeSelectedCount);
    }

    // Выбор пустой трубы не вызывает события onTubeSelected
    @Test
    void test10_selectingEmptyTubeDoesNotTriggerSelection() {
        Tube emptyTube = level.getTubeAt(3);

        level.selectTube(emptyTube);

        assertEquals(0, listener.tubeSelectedCount);
        assertNull(level.getPendingTube());
    }

    // Отмена выбора через повторный клик по той же трубе работает корректно
    @Test
    void test11_deselectViaSelectingSameTube() {
        Tube tube = level.getTubeAt(0);

        level.selectTube(tube);
        assertNotNull(level.getPendingTube());
        assertEquals(1, listener.tubeSelectedCount);

        level.selectTube(tube);
        assertNull(level.getPendingTube());
        assertEquals(1, listener.tubeDeselectedCount);
    }
}