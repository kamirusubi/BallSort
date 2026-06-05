package ListenerTests;

import game.Game;
import game.GameEventListener;
import game.ListenerPriority;
import model.Level;
import model.Tube;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class GameEventListenerRobustnessTest {

    private Game game;
    private Level level;
    private final List<String> robustnessLog = new ArrayList<>();

    @BeforeEach
    void setUp() {
        game = new Game();
        game.startForTests();
        level = game.getCurrentLevel();
        robustnessLog.clear();
    }

    // Плохой слушатель не ломает модель при выборе трубы
    @Test
    void test01_badListenerDoesNotBreakModelOnTubeSelected() {
        GameEventListener badListener = new GameEventListener() {
            @Override
            public void onTubeSelected(Tube tube, int liftedCount) {
                throw new RuntimeException("Bad listener exception!");
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

        level.addEventListener(badListener);

        assertDoesNotThrow(() -> {
            level.selectTube(level.getTubeAt(0));
        });

        assertEquals(level.getTubeAt(0), level.getPendingTube());
    }

    // Плохой слушатель не ломает модель при перемещении
    @Test
    void test02_badListenerDoesNotBreakModelOnMove() {
        GameEventListener badListener = new GameEventListener() {
            @Override
            public void onMoveSucceeded(Tube from, Tube to, int movedCount) {
                throw new RuntimeException("Bad listener exception!");
            }
            @Override public void onTubeSelected(Tube tube, int liftedCount) {}
            @Override public void onTubeDeselected(Tube tube) {}
            @Override public void onMoveFailed(Tube from, Tube to) {}
            @Override public void onGameCompleted() {}
            @Override
            public ListenerPriority getPriority() {
                return ListenerPriority.HIGHEST;
            }
        };

        level.addEventListener(badListener);

        assertDoesNotThrow(() -> {
            level.selectTube(level.getTubeAt(0));
            level.selectTube(level.getTubeAt(3));
        });

        assertEquals(0, level.getTubeAt(0).getBallCount());
        assertEquals(2, level.getTubeAt(3).getBallCount());
    }

    // Множество плохих слушателей не ломают модель
    @Test
    void test03_multipleBadListenersDoNotBreakModel() {
        for (int i = 0; i < 5; i++) {
            final int id = i;
            level.addEventListener(new GameEventListener() {
                @Override
                public void onTubeSelected(Tube tube, int liftedCount) {
                    throw new RuntimeException("Bad listener " + id);
                }
                @Override public void onTubeDeselected(Tube tube) {}
                @Override public void onMoveSucceeded(Tube from, Tube to, int movedCount) {}
                @Override public void onMoveFailed(Tube from, Tube to) {}
                @Override public void onGameCompleted() {}
                @Override
                public ListenerPriority getPriority() {
                    return ListenerPriority.HIGHEST;
                }
            });
        }

        assertDoesNotThrow(() -> {
            level.selectTube(level.getTubeAt(0));
            level.selectTube(level.getTubeAt(3));
        });

        assertTrue(level.getTubeAt(0).isEmpty());
    }

    // Хороший и плохой слушатели вместе — хороший всё равно получает события
    @Test
    void test04_goodAndBadListenersMixed() {
        final int[] goodListenerCalls = {0};

        GameEventListener goodListener = new GameEventListener() {
            @Override
            public void onMoveSucceeded(Tube from, Tube to, int movedCount) {
                goodListenerCalls[0]++;
                robustnessLog.add("GOOD called");
            }
            @Override public void onTubeSelected(Tube tube, int liftedCount) {}
            @Override public void onTubeDeselected(Tube tube) {}
            @Override public void onMoveFailed(Tube from, Tube to) {}
            @Override public void onGameCompleted() {}
            @Override
            public ListenerPriority getPriority() {
                return ListenerPriority.LOW;
            }
        };

        GameEventListener badListener = new GameEventListener() {
            @Override
            public void onMoveSucceeded(Tube from, Tube to, int movedCount) {
                throw new RuntimeException("Bad listener!");
            }
            @Override public void onTubeSelected(Tube tube, int liftedCount) {}
            @Override public void onTubeDeselected(Tube tube) {}
            @Override public void onMoveFailed(Tube from, Tube to) {}
            @Override public void onGameCompleted() {}
            @Override
            public ListenerPriority getPriority() {
                return ListenerPriority.HIGHEST;
            }
        };

        level.addEventListener(badListener);
        level.addEventListener(goodListener);

        level.selectTube(level.getTubeAt(0));
        level.selectTube(level.getTubeAt(3));

        assertEquals(1, goodListenerCalls[0]);
    }

    // Слушателей можно добавлять и удалять во время игры
    @Test
    void test05_listenersCanBeAddedAndRemovedDuringGame() {
        final int[] calls = {0};

        GameEventListener listener = new GameEventListener() {
            @Override
            public void onMoveSucceeded(Tube from, Tube to, int movedCount) {
                calls[0]++;
            }
            @Override public void onTubeSelected(Tube tube, int liftedCount) {}
            @Override public void onTubeDeselected(Tube tube) {}
            @Override public void onMoveFailed(Tube from, Tube to) {}
            @Override public void onGameCompleted() {}
            @Override
            public ListenerPriority getPriority() {
                return ListenerPriority.HIGHEST;
            }
        };

        level.addEventListener(listener);

        level.selectTube(level.getTubeAt(0));
        level.selectTube(level.getTubeAt(3));

        assertEquals(1, calls[0]);

        level.removeEventListener(listener);

        level.reset();
        level.selectTube(level.getTubeAt(0));
        level.selectTube(level.getTubeAt(3));

        assertEquals(1, calls[0]);
    }

    // Удаление себя во время уведомления не вызывает ConcurrentModificationException
    @Test
    void test06_concurrentModificationDoesNotOccurWhenRemovingSelf() {
        GameEventListener selfRemoving = new GameEventListener() {
            @Override
            public void onTubeSelected(Tube tube, int liftedCount) {
                robustnessLog.add("SELF_REMOVING called");
                level.removeEventListener(this);
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

        GameEventListener other = new GameEventListener() {
            @Override
            public void onTubeSelected(Tube tube, int liftedCount) {
                robustnessLog.add("OTHER called");
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

        level.addEventListener(selfRemoving);
        level.addEventListener(other);

        robustnessLog.clear();

        assertDoesNotThrow(() -> {
            level.selectTube(level.getTubeAt(0));
        });

        assertTrue(robustnessLog.contains("SELF_REMOVING called"));
        assertTrue(robustnessLog.contains("OTHER called"));
        assertNotNull(level.getPendingTube());
    }
}