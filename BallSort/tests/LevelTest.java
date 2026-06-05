import factory.LevelFactory;
import model.Ball;
import model.ColorProperty;
import model.Level;
import model.Tube;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import rules.ColorSequenceRule;
import rules.CompositeSequenceRule;
import rules.SequenceRule;
import ListenerTests.TestGameEventListener;

import java.awt.Color;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class LevelTest {

    private Level level;
    private SequenceRule rule;

    @BeforeEach
    void setUp() {
        level = LevelFactory.createSimpleLevel();
        rule = new CompositeSequenceRule(new ColorSequenceRule());
    }

    @Test
    void test01_ConstructorCreatesCorrectNumberOfTubes() {
        assertEquals(4, level.getTubeCount());
    }

    @Test
    void test02_ConstructorCreatesDeepCopyOfTubes() {
        Level originalLevel = LevelFactory.createSimpleLevel();
        List<Tube> originalTubes = originalLevel.getTubes();

        Level newLevel = new Level(originalTubes, rule);
        List<Tube> newTubes = newLevel.getTubes();

        assertEquals(originalTubes.size(), newTubes.size());

        originalTubes.get(0).popOne();

        assertNotEquals(originalTubes.get(0).getBallCount(), newTubes.get(0).getBallCount());
    }

    @Test
    void test03_GetTubesReturnsUnmodifiableList() {
        List<Tube> tubesList = level.getTubes();
        assertThrows(UnsupportedOperationException.class, () -> tubesList.add(new Tube(4, rule)));
    }

    @Test
    void test04_ResetRestoresInitialState() {
        int initialBallCount0 = level.getTubeAt(0).getBallCount();
        int initialBallCount3 = level.getTubeAt(3).getBallCount();

        level.selectTube(level.getTubeAt(0));
        level.selectTube(level.getTubeAt(3));

        assertNotEquals(initialBallCount0, level.getTubeAt(0).getBallCount());
        assertNotEquals(initialBallCount3, level.getTubeAt(3).getBallCount());

        level.reset();

        assertEquals(initialBallCount0, level.getTubeAt(0).getBallCount());
        assertEquals(initialBallCount3, level.getTubeAt(3).getBallCount());
    }

    @Test
    void test05_SelectTubeTransfersBallsCorrectly() {
        Tube from = level.getTubeAt(0);
        Tube to = level.getTubeAt(3);

        int fromInitialCount = from.getBallCount();
        int toInitialCount = to.getBallCount();

        List<Ball> ballsToMove = level.getSequenceToMove(from);
        int expectedMoveCount = ballsToMove.size();

        level.selectTube(from);
        level.selectTube(to);

        assertEquals(fromInitialCount - expectedMoveCount, from.getBallCount());
        assertEquals(toInitialCount + expectedMoveCount, to.getBallCount());
    }

    @Test
    void test06_SelectTubeFromEmptyTubeFails() {
        Tube from = level.getTubeAt(3);
        Tube to = level.getTubeAt(2);

        level.selectTube(from);
        assertNull(level.getPendingTube());

        level.selectTube(to);
        assertEquals(1, to.getBallCount());
    }

    @Test
    void test07_SelectTubeToFullTubeFails() {
        Tube from = level.getTubeAt(0);
        Tube to = level.getTubeAt(3);

        level.selectTube(from);
        level.selectTube(to);

        assertFalse(to.hasSpace());

        Tube anotherFrom = level.getTubeAt(1);
        level.selectTube(anotherFrom);
        level.selectTube(to);

        assertEquals(to.getCapacity(), to.getBallCount());
    }

    @Test
    void test08_IsLevelCompletedReturnsTrue() {
        level.selectTube(level.getTubeAt(1));
        level.selectTube(level.getTubeAt(2));
        assertTrue(level.isLevelCompleted());
    }

    @Test
    void test09_IsLevelCompletedReturnsFalse() {
        assertFalse(level.isLevelCompleted());
    }

    @Test
    void test10_SelectTubeWithSecondEmptyTube() {
        Tube from = level.getTubeAt(1);
        Tube to = level.getTubeAt(3);

        Ball fromBall = from.peekOne();
        assertEquals(Color.BLUE, fromBall.getProperty(ColorProperty.class).getColor());

        level.selectTube(from);
        level.selectTube(to);

        assertEquals(0, from.getBallCount());
        assertEquals(1, to.getBallCount());
    }

    @Test
    void test11_MultipleMovesWorkCorrectly() {
        Tube tube0 = level.getTubeAt(0);
        Tube tube1 = level.getTubeAt(1);
        Tube tube3 = level.getTubeAt(3);

        level.selectTube(tube0);
        level.selectTube(tube3);
        int tube3AfterFirst = tube3.getBallCount();

        level.selectTube(tube1);
        level.selectTube(tube0);

        assertEquals(tube3AfterFirst, tube3.getBallCount());
    }

    @Test
    void test12_ResetAfterMultipleMoves() {
        List<Integer> initialCounts = level.getTubes().stream()
                .map(Tube::getBallCount)
                .toList();

        level.selectTube(level.getTubeAt(0));
        level.selectTube(level.getTubeAt(3));
        level.selectTube(level.getTubeAt(1));
        level.selectTube(level.getTubeAt(3));

        assertNotEquals(initialCounts, level.getTubes().stream().map(Tube::getBallCount).toList());

        level.reset();

        assertEquals(initialCounts, level.getTubes().stream().map(Tube::getBallCount).toList());
    }

    @Test
    void test13_SelectSameTubeTwiceDeselects() {
        Tube tube = level.getTubeAt(0);

        level.selectTube(tube);
        assertNotNull(level.getPendingTube());

        level.selectTube(tube);
        assertNull(level.getPendingTube());
    }

    @Test
    void test14_IsLevelCompletedWithEmptyTubes() {
        List<Tube> customTubes = List.of(
                new Tube(4, rule),
                new Tube(4, rule)
        );
        Level emptyLevel = new Level(customTubes, rule);
        assertTrue(emptyLevel.isLevelCompleted());
    }

    @Test
    void test15_LevelCompletedNotifiesListeners() {
        TestGameEventListener listener = new TestGameEventListener();
        level.addEventListener(listener);

        level.selectTube(level.getTubeAt(1));
        level.selectTube(level.getTubeAt(2));

        assertTrue(level.isLevelCompleted());
        assertEquals(1, listener.gameCompletedCount);
    }

    @Test
    void test16_SelectTubeWithDifferentColorsFails() {
        Tube from = level.getTubeAt(1);
        Tube to = level.getTubeAt(0);

        int fromInitialCount = from.getBallCount();
        int toInitialCount = to.getBallCount();

        level.selectTube(from);
        level.selectTube(to);

        assertEquals(fromInitialCount, from.getBallCount());
        assertEquals(toInitialCount, to.getBallCount());
    }

    @Test
    void test17_CanAddAndRemoveGameEventListener() {
        TestGameEventListener listener1 = new TestGameEventListener();
        TestGameEventListener listener2 = new TestGameEventListener();

        level.addEventListener(listener1);
        level.addEventListener(listener2);

        level.selectTube(level.getTubeAt(0));
        level.selectTube(level.getTubeAt(3));

        assertEquals(1, listener1.moveSucceededCount);
        assertEquals(1, listener2.moveSucceededCount);

        level.removeEventListener(listener1);

        level.reset();
        level.selectTube(level.getTubeAt(0));
        level.selectTube(level.getTubeAt(3));

        assertEquals(1, listener1.moveSucceededCount);
        assertEquals(2, listener2.moveSucceededCount);
    }

    @Test
    void test18_GetSequenceToMoveReturnsCorrectSequence() {
        Tube tube = level.getTubeAt(0);
        List<Ball> sequence = level.getSequenceToMove(tube);

        assertNotNull(sequence);
        assertFalse(sequence.isEmpty());
        assertEquals(2, sequence.size());
    }

    @Test
    void test19_GetSequenceToMoveOnEmptyTube() {
        Tube tube = level.getTubeAt(3);
        List<Ball> sequence = level.getSequenceToMove(tube);

        assertTrue(sequence.isEmpty());
    }

    @Test
    void test20_GetPendingTubeReturnsNullInitially() {
        assertNull(level.getPendingTube());
    }

    @Test
    void test21_GetPendingTubeAfterSelection() {
        Tube tube = level.getTubeAt(0);
        level.selectTube(tube);
        assertEquals(tube, level.getPendingTube());
    }
}