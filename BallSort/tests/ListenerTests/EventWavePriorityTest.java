package ListenerTests;

import factory.LevelFactory;
import game.GameEventListener;
import game.ListenerPriority;
import model.Level;
import model.Tube;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class EventWavePriorityTest {

    private Level level;
    private final List<String> eventLog = new ArrayList<>();

    @BeforeEach
    void setUp() {
        level = LevelFactory.createSimpleLevel();
        eventLog.clear();
    }

    // Самый высокий приоритет получает события первым
    @Test
    void test01_highestPriorityReceivesEventsFirst() {
        GameEventListener highestListener = createPriorityListener("HIGHEST", ListenerPriority.HIGHEST);
        GameEventListener lowestListener = createPriorityListener("LOWEST", ListenerPriority.LOWEST);

        level.addEventListener(highestListener);
        level.addEventListener(lowestListener);

        Tube from = level.getTubeAt(0);
        Tube to = level.getTubeAt(3);

        level.selectTube(from);
        level.selectTube(to);

        int highestIdx = -1, lowestIdx = -1;
        for (int i = 0; i < eventLog.size(); i++) {
            if (eventLog.get(i).contains("HIGHEST")) highestIdx = i;
            if (eventLog.get(i).contains("LOWEST")) lowestIdx = i;
        }
        assertTrue(highestIdx < lowestIdx);
    }

    // Все приоритеты соблюдают правильный порядок от HIGHEST до LOWEST
    @Test
    void test02_allPrioritiesInCorrectOrder() {
        GameEventListener highest = createPriorityListener("HIGHEST", ListenerPriority.HIGHEST);
        GameEventListener high = createPriorityListener("HIGH", ListenerPriority.HIGH);
        GameEventListener normal = createPriorityListener("NORMAL", ListenerPriority.NORMAL);
        GameEventListener low = createPriorityListener("LOW", ListenerPriority.LOW);
        GameEventListener lowest = createPriorityListener("LOWEST", ListenerPriority.LOWEST);

        level.addEventListener(lowest);
        level.addEventListener(low);
        level.addEventListener(normal);
        level.addEventListener(high);
        level.addEventListener(highest);

        Tube from = level.getTubeAt(0);
        Tube to = level.getTubeAt(3);

        level.selectTube(from);
        level.selectTube(to);

        List<String> observedOrder = new ArrayList<>();
        for (String log : eventLog) {
            String priority = log.substring(1, log.indexOf("]"));
            if (!observedOrder.contains(priority)) {
                observedOrder.add(priority);
            }
        }

        assertEquals(5, observedOrder.size());
        assertEquals("HIGHEST", observedOrder.get(0));
        assertEquals("HIGH", observedOrder.get(1));
        assertEquals("NORMAL", observedOrder.get(2));
        assertEquals("LOW", observedOrder.get(3));
        assertEquals("LOWEST", observedOrder.get(4));
    }

    // Приоритет слушателя можно динамически изменять
    @Test
    void test03_listenerPriorityCanBeChangedDynamically() {
        TestGameEventListener testListener = new TestGameEventListener(ListenerPriority.LOW);
        level.addEventListener(testListener);

        Tube from = level.getTubeAt(0);
        Tube to = level.getTubeAt(3);

        level.selectTube(from);
        level.selectTube(to);

        testListener.setPriority(ListenerPriority.HIGHEST);

        level.removeEventListener(testListener);
        level.addEventListener(testListener);

        level.reset();
        eventLog.clear();

        GameEventListener otherLow = createPriorityListener("OTHER_LOW", ListenerPriority.LOW);
        level.addEventListener(otherLow);

        level.selectTube(from);
        level.selectTube(to);

        int testIdx = -1, otherIdx = -1;
        for (int i = 0; i < eventLog.size(); i++) {
            if (eventLog.get(i).contains("TEST_HIGHEST")) testIdx = i;
            if (eventLog.get(i).contains("OTHER_LOW")) otherIdx = i;
        }
        assertTrue(testIdx < otherIdx);
    }

    // Одинаковый приоритет сохраняет порядок добавления
    @Test
    void test04_samePriorityPreservesAdditionOrder() {
        GameEventListener first = createPriorityListener("FIRST", ListenerPriority.NORMAL);
        GameEventListener second = createPriorityListener("SECOND", ListenerPriority.NORMAL);

        level.addEventListener(first);
        level.addEventListener(second);

        Tube tube = level.getTubeAt(0);
        level.selectTube(tube);

        int firstIdx = -1, secondIdx = -1;
        for (int i = 0; i < eventLog.size(); i++) {
            if (eventLog.get(i).contains("FIRST")) firstIdx = i;
            if (eventLog.get(i).contains("SECOND")) secondIdx = i;
        }
        assertTrue(firstIdx < secondIdx);
    }

    // Приоритет по умолчанию — LOW
    @Test
    void test05_defaultPriorityIsLOW() {
        GameEventListener defaultListener = new GameEventListener() {
            @Override
            public void onTubeSelected(Tube tube, int liftedCount) {
                eventLog.add("[DEFAULT] onTubeSelected");
            }
            @Override public void onTubeDeselected(Tube tube) {}
            @Override public void onMoveSucceeded(Tube from, Tube to, int movedCount) {}
            @Override public void onMoveFailed(Tube from, Tube to) {}
            @Override public void onGameCompleted() {}
            @Override
            public ListenerPriority getPriority() {
                return ListenerPriority.LOW;
            }
        };

        GameEventListener highListener = createPriorityListener("HIGH", ListenerPriority.HIGH);

        level.addEventListener(defaultListener);
        level.addEventListener(highListener);

        Tube tube = level.getTubeAt(0);
        level.selectTube(tube);

        int highIdx = -1, defaultIdx = -1;
        for (int i = 0; i < eventLog.size(); i++) {
            if (eventLog.get(i).contains("HIGH")) highIdx = i;
            if (eventLog.get(i).contains("DEFAULT")) defaultIdx = i;
        }
        assertTrue(highIdx < defaultIdx);
    }

    // Вложенные события сохраняют правильный порядок вызовов
    @Test
    void test06_nestedEventsMaintainPriorityOrder() {
        TestGameEventListener highestListener = new TestGameEventListener(ListenerPriority.HIGHEST);

        level.addEventListener(highestListener);

        Tube from = level.getTubeAt(0);
        Tube to = level.getTubeAt(3);

        level.selectTube(from);
        level.selectTube(to);

        int tubeSelectedIdx = -1;
        int moveSucceededIdx = -1;
        int tubeDeselectedIdx = -1;

        for (int i = 0; i < highestListener.callHistory.size(); i++) {
            String call = highestListener.callHistory.get(i);
            if (call.startsWith("onTubeSelected")) {
                tubeSelectedIdx = i;
            } else if (call.startsWith("onMoveSucceeded")) {
                moveSucceededIdx = i;
            } else if (call.startsWith("onTubeDeselected")) {
                tubeDeselectedIdx = i;
            }
        }

        assertTrue(tubeSelectedIdx >= 0);
        assertTrue(moveSucceededIdx >= 0);
        assertTrue(tubeDeselectedIdx >= 0);
        assertTrue(tubeSelectedIdx < moveSucceededIdx);
        assertTrue(moveSucceededIdx < tubeDeselectedIdx);
    }

    // Исключение в высокоприоритетном слушателе не блокирует низкоприоритетный
    @Test
    void test07_exceptionInHighPriorityDoesNotBlockLowPriority() {
        GameEventListener brokenListener = new GameEventListener() {
            @Override
            public void onTubeSelected(Tube tube, int liftedCount) {
                throw new RuntimeException("Broken in test");
            }
            @Override public void onTubeDeselected(Tube tube) {}
            @Override public void onMoveSucceeded(Tube from, Tube to, int movedCount) {}
            @Override public void onMoveFailed(Tube from, Tube to) {}
            @Override public void onGameCompleted() {}
            @Override
            public ListenerPriority getPriority() {
                return ListenerPriority.HIGHEST;
            }
        };

        GameEventListener healthyListener = createPriorityListener("HEALTHY", ListenerPriority.LOW);

        level.addEventListener(brokenListener);
        level.addEventListener(healthyListener);

        assertDoesNotThrow(() -> {
            Tube tube = level.getTubeAt(0);
            level.selectTube(tube);
        });

        assertTrue(eventLog.stream().anyMatch(log -> log.contains("HEALTHY")));
    }

    // Удаление слушателя во время уведомления не ломает других слушателей
    @Test
    void test08_removingListenerDuringNotificationDoesNotBreakOthers() {
        final boolean[] selfBeforeRemove = {false};
        final boolean[] selfAfterRemove = {false};

        GameEventListener selfRemoving = new GameEventListener() {
            @Override
            public void onTubeSelected(Tube tube, int liftedCount) {
                selfBeforeRemove[0] = true;
                eventLog.add("[SELF] before remove");
                level.removeEventListener(this);
                selfAfterRemove[0] = true;
                eventLog.add("[SELF] after remove");
            }
            @Override public void onTubeDeselected(Tube tube) {}
            @Override public void onMoveSucceeded(Tube from, Tube to, int movedCount) {}
            @Override public void onMoveFailed(Tube from, Tube to) {}
            @Override public void onGameCompleted() {}
            @Override
            public ListenerPriority getPriority() {
                return ListenerPriority.HIGHEST;
            }
        };

        GameEventListener other = createPriorityListener("OTHER", ListenerPriority.LOW);

        level.addEventListener(selfRemoving);
        level.addEventListener(other);

        Tube tube = level.getTubeAt(0);
        assertFalse(tube.isEmpty());

        level.selectTube(tube);

        assertTrue(selfBeforeRemove[0]);
        assertTrue(selfAfterRemove[0]);

        assertTrue(eventLog.contains("[SELF] before remove"));
        assertTrue(eventLog.contains("[SELF] after remove"));
        assertTrue(eventLog.contains("[OTHER] onTubeSelected"));

        assertNotNull(level.getPendingTube());
        assertEquals(tube, level.getPendingTube());
    }

    // Завершение игры приходит после всех событий перемещения
    @Test
    void test09_gameCompletedAfterAllMoveEvents() {
        TestGameEventListener listener = new TestGameEventListener(ListenerPriority.HIGHEST);
        level.addEventListener(listener);

        Tube t0 = level.getTubeAt(0);
        Tube t1 = level.getTubeAt(1);
        Tube t2 = level.getTubeAt(2);
        Tube t3 = level.getTubeAt(3);

        level.selectTube(t0);
        level.selectTube(t3);
        level.selectTube(t1);
        level.selectTube(t0);
        level.selectTube(t2);
        level.selectTube(t1);
        level.selectTube(t0);
        level.selectTube(t1);

        assertTrue(listener.wasMethodCalledBefore("onMoveSucceeded", "onGameCompleted"));
    }

    // Слушатели с одинаковым приоритетом получают события в порядке добавления
    @Test
    void test10_listenersWithSamePriorityGetEventsInAdditionOrderEvenWithMixedTypes() {
        GameEventListener first = createPriorityListener("FIRST", ListenerPriority.HIGH);
        GameEventListener second = createPriorityListener("SECOND", ListenerPriority.HIGH);
        GameEventListener third = createPriorityListener("THIRD", ListenerPriority.HIGH);

        level.addEventListener(second);
        level.addEventListener(first);
        level.addEventListener(third);

        Tube tube = level.getTubeAt(0);
        level.selectTube(tube);

        int idxSecond = -1, idxFirst = -1, idxThird = -1;
        for (int i = 0; i < eventLog.size(); i++) {
            if (eventLog.get(i).contains("SECOND")) idxSecond = i;
            if (eventLog.get(i).contains("FIRST")) idxFirst = i;
            if (eventLog.get(i).contains("THIRD")) idxThird = i;
        }

        assertTrue(idxSecond < idxFirst);
        assertTrue(idxFirst < idxThird);
    }

    private GameEventListener createPriorityListener(String name, ListenerPriority priority) {
        return new GameEventListener() {
            @Override
            public void onTubeSelected(Tube tube, int liftedCount) {
                eventLog.add("[" + name + "] onTubeSelected");
            }
            @Override
            public void onTubeDeselected(Tube tube) {
                eventLog.add("[" + name + "] onTubeDeselected");
            }
            @Override
            public void onMoveSucceeded(Tube from, Tube to, int movedCount) {
                eventLog.add("[" + name + "] onMoveSucceeded");
            }
            @Override
            public void onMoveFailed(Tube from, Tube to) {
                eventLog.add("[" + name + "] onMoveFailed");
            }
            @Override
            public void onGameCompleted() {
                eventLog.add("[" + name + "] onGameCompleted");
            }
            @Override
            public ListenerPriority getPriority() {
                return priority;
            }
        };
    }
}