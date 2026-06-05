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

public class StateAfterEachEventTest {

    private Level level;
    private final List<String> stateSnapshotLog = new ArrayList<>();

    @BeforeEach
    void setUp() {
        level = LevelFactory.createSimpleLevel();
        stateSnapshotLog.clear();
    }

    // Состояние модели после выбора трубы
    @Test
    void test01_stateAfterTubeSelected() {
        Tube expectedPending = level.getTubeAt(0);
        int expectedLiftedCount = expectedPending.peekSequence().size();

        level.addEventListener(new StateVerifyingListener());

        level.selectTube(expectedPending);

        assertTrue(stateSnapshotLog.contains("PENDING_TUBE=" + expectedPending));
        assertTrue(stateSnapshotLog.contains("LIFTED_COUNT=" + expectedLiftedCount));
    }

    // Состояние модели после снятия выбора трубы
    @Test
    void test02_stateAfterDeselect() {
        Tube tube = level.getTubeAt(0);
        level.selectTube(tube);

        level.addEventListener(new StateVerifyingListener());
        level.selectTube(tube);

        assertTrue(stateSnapshotLog.contains("PENDING_TUBE=null"));
    }

    // Состояние модели после успешного перемещения шаров
    @Test
    void test03_stateAfterMoveSucceeded() {
        Tube from = level.getTubeAt(0);
        Tube to = level.getTubeAt(3);
        int fromInitial = from.getBallCount();
        int toInitial = to.getBallCount();
        int expectedMoved = Math.min(from.peekSequence().size(), to.getCapacity() - toInitial);

        level.addEventListener(new StateVerifyingListener());

        level.selectTube(from);
        level.selectTube(to);

        assertTrue(stateSnapshotLog.contains("FROM_BALLS=" + (fromInitial - expectedMoved)));
        assertTrue(stateSnapshotLog.contains("TO_BALLS=" + (toInitial + expectedMoved)));
        assertTrue(stateSnapshotLog.contains("PENDING_TUBE=null"));
    }

    // Состояние модели после неудачного перемещения шаров
    @Test
    void test04_stateAfterMoveFailed() {
        Tube from = level.getTubeAt(1);
        Tube to = level.getTubeAt(0);
        int fromInitial = from.getBallCount();
        int toInitial = to.getBallCount();

        level.addEventListener(new StateVerifyingListener());

        level.selectTube(from);
        level.selectTube(to);

        assertTrue(stateSnapshotLog.contains("FROM_BALLS=" + fromInitial));
        assertTrue(stateSnapshotLog.contains("TO_BALLS=" + toInitial));
        assertTrue(stateSnapshotLog.contains("PENDING_TUBE=null"));
    }

    // Согласованность состояния после нескольких быстрых событий
    @Test
    void test05_stateConsistencyAfterMultipleRapidEvents() {
        level.addEventListener(new StateVerifyingListener());

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

        int totalBalls = 0;
        for (Tube tube : level.getTubes()) {
            totalBalls += tube.getBallCount();
        }
        assertEquals(4, totalBalls, "Общее количество шаров не изменилось");
    }

    private class StateVerifyingListener implements GameEventListener {
        @Override
        public void onTubeSelected(Tube tube, int liftedCount) {
            captureState("onTubeSelected");
            stateSnapshotLog.add("PENDING_TUBE=" + level.getPendingTube());
            stateSnapshotLog.add("LIFTED_COUNT=" + liftedCount);
        }

        @Override
        public void onTubeDeselected(Tube tube) {
            captureState("onTubeDeselected");
            stateSnapshotLog.add("PENDING_TUBE=" + level.getPendingTube());
        }

        @Override
        public void onMoveSucceeded(Tube from, Tube to, int movedCount) {
            captureState("onMoveSucceeded");
            stateSnapshotLog.add("FROM_BALLS=" + from.getBallCount());
            stateSnapshotLog.add("TO_BALLS=" + to.getBallCount());
            stateSnapshotLog.add("PENDING_TUBE=" + level.getPendingTube());
        }

        @Override
        public void onMoveFailed(Tube from, Tube to) {
            captureState("onMoveFailed");
            stateSnapshotLog.add("FROM_BALLS=" + from.getBallCount());
            stateSnapshotLog.add("TO_BALLS=" + to.getBallCount());
            stateSnapshotLog.add("PENDING_TUBE=" + level.getPendingTube());
        }

        @Override
        public void onGameCompleted() {
            captureState("onGameCompleted");
            stateSnapshotLog.add("LEVEL_COMPLETED=" + level.isLevelCompleted());
        }

        @Override
        public ListenerPriority getPriority() {
            return ListenerPriority.HIGHEST;
        }

        private void captureState(String eventName) {
            stateSnapshotLog.add("EVENT=" + eventName);
        }
    }
}