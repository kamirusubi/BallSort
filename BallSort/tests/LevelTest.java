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

import java.awt.Color;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class LevelTest {

    private Level level;
    private SequenceRule rule;
    private List<Tube> tubes;

    @BeforeEach
    void setUp() {
        level = LevelFactory.createSimpleLevel();
        rule = new CompositeSequenceRule(new ColorSequenceRule());
        tubes = level.getTubes();
    }

    @Test
    void test01_ConstructorCreatesCorrectNumberOfTubes() {
        assertEquals(4, tubes.size());
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

        assertThrows(UnsupportedOperationException.class, () -> tubesList.add(new Tube(4)));
    }

    @Test
    void test04_ResetRestoresInitialState() {
        int initialBallCount0 = tubes.get(0).getBallCount();
        int initialBallCount1 = tubes.get(3).getBallCount();

        Tube from = tubes.get(0);
        Tube to = tubes.get(3);
        level.executeMove(from, to);

        assertNotEquals(initialBallCount0, tubes.get(0).getBallCount());
        assertNotEquals(initialBallCount1, tubes.get(3).getBallCount());

        level.reset();

        assertEquals(initialBallCount0, tubes.get(0).getBallCount());
        assertEquals(initialBallCount1, tubes.get(3).getBallCount());
    }

    @Test
    void test05_ExecuteMoveTransfersBallsCorrectly() {
        Tube from = tubes.get(0);
        Tube to = tubes.get(3);

        int fromInitialCount = from.getBallCount();
        int toInitialCount = to.getBallCount();

        List<Ball> ballsToMove = from.peekSequence(rule);
        int expectedMoveCount = ballsToMove.size();

        boolean result = level.executeMove(from, to);

        assertTrue(result);
        assertEquals(fromInitialCount - expectedMoveCount, from.getBallCount());
        assertEquals(toInitialCount + expectedMoveCount, to.getBallCount());
    }

    @Test
    void test06_ExecuteMoveFromEmptyTubeFails() {
        Tube from = tubes.get(3);
        Tube to = tubes.get(2);

        boolean result = level.executeMove(from, to);

        assertFalse(result);
    }

    @Test
    void test07_ExecuteMoveToFullTubeFails() {
        Tube from = tubes.get(0);
        Tube to = tubes.get(3);

        level.executeMove(from, to);

        Tube anotherFrom = tubes.get(1);
        level.executeMove(anotherFrom, to);

        assertFalse(to.hasSpace());

        boolean result = level.executeMove(to, to);

        assertFalse(result);
        assertEquals(to.getBallCount(), to.getBallCount());
        assertFalse(to.hasSpace());
    }

    @Test
    void test08_IsLevelCompletedReturnsTrue() {
        Tube from = tubes.get(1);
        Tube to = tubes.get(2);

        level.executeMove(from, to);
        assertTrue(level.isLevelCompleted());
    }

    @Test
    void test09_IsLevelCompletedReturnsFalse() {
        assertFalse(level.isLevelCompleted());
    }

    @Test
    void test10_ExecuteMoveWithSecondEmptyTube() {
        Tube from = tubes.get(1);
        Tube to = tubes.get(3);

        Ball fromBall = from.peekOne();
        assertEquals(Color.BLUE, fromBall.getProperty(ColorProperty.class).getColor());

        boolean result = level.executeMove(from, to);

        assertTrue(result);
        assertEquals(0, from.getBallCount());
        assertEquals(1, to.getBallCount());
    }

    @Test
    void test11_MultipleMovesWorkCorrectly() {
        Tube tube0 = tubes.get(0);
        Tube tube1 = tubes.get(1);
        Tube tube3 = tubes.get(3);

        level.executeMove(tube0, tube3);
        int tube3AfterFirst = tube3.getBallCount();

        boolean secondMoveResult = level.executeMove(tube1, tube0);

        assertTrue(secondMoveResult);
        assertEquals(tube3AfterFirst, tube3.getBallCount());
    }

    @Test
    void test12_ResetAfterMultipleMoves() {
        List<Integer> initialCounts = tubes.stream()
                .map(Tube::getBallCount)
                .toList();

        level.executeMove(tubes.get(0), tubes.get(3));
        level.executeMove(tubes.get(1), tubes.get(3));

        assertNotEquals(initialCounts, tubes.stream().map(Tube::getBallCount).toList());

        level.reset();

        assertEquals(initialCounts, tubes.stream().map(Tube::getBallCount).toList());
    }

    @Test
    void test13_ExecuteMoveWithNullParametersThrowsException() {
        Tube validTube = tubes.get(0);

        assertThrows(NullPointerException.class, () -> level.executeMove(null, validTube));
        assertThrows(NullPointerException.class, () -> level.executeMove(validTube, null));
    }

    @Test
    void test14_ExecuteMoveRemovesSelectionFromBothTubes() {
        Tube from = tubes.get(0);
        Tube to = tubes.get(3);

        boolean fromWasSelected = from.isSelected();
        boolean toWasSelected = to.isSelected();

        level.executeMove(from, to);

        assertFalse(from.isSelected());
        assertFalse(to.isSelected());
    }


    @Test
    void test15_IsLevelCompletedWithEmptyTubes() {
        List<Tube> customTubes = List.of(
                new Tube(4),
                new Tube(4)
        );
        Level emptyLevel = new Level(customTubes, rule);

        assertTrue(emptyLevel.isLevelCompleted());
    }

    @Test
    void test16_ExecuteMoveWithSameTube() {
        Tube tube = tubes.get(0);
        int initialCount = tube.getBallCount();

        tube.setSelected(true);
        assertTrue(tube.isSelected());

        boolean result = level.executeMove(tube, tube);

        assertFalse(result);
        assertEquals(initialCount, tube.getBallCount());
        assertFalse(tube.isSelected());
    }

    @Test
    void test17_ExecuteMoveNotifiesListeners() {
        utils.TestLevelListener listener = new utils.TestLevelListener();
        level.addLevelListener(listener);

        Tube from = tubes.get(0);
        Tube to = tubes.get(3);

        level.executeMove(from, to);

        assertTrue(listener.isMoveAttemptCalled());
        assertEquals(1, listener.moveAttemptCount);
        assertTrue(listener.lastMoveSuccess);
        assertEquals(from, listener.moveFrom);
        assertEquals(to, listener.moveTo);
    }

    @Test
    void test18_LevelCompletedNotifiesListeners() {
        utils.TestLevelListener listener = new utils.TestLevelListener();
        level.addLevelListener(listener);

        Tube from = tubes.get(1);
        Tube to = tubes.get(2);

        assertEquals(0, listener.gameCompletedCount);

        level.executeMove(from, to);

        assertTrue(level.isLevelCompleted());
        assertEquals(1, listener.gameCompletedCount);
    }

    @Test
    void test19_ExecuteMoveWithDifferentColorsFails() {
        Tube from = tubes.get(1);
        Tube to = tubes.get(0);

        int fromInitialCount = from.getBallCount();
        int toInitialCount = to.getBallCount();

        boolean result = level.executeMove(from, to);

        assertFalse(result);
        assertEquals(fromInitialCount, from.getBallCount());
        assertEquals(toInitialCount, to.getBallCount());
    }

    @Test
    void test20_CanAddAndRemoveLevelListener() {
        utils.TestLevelListener listener1 = new utils.TestLevelListener();
        utils.TestLevelListener listener2 = new utils.TestLevelListener();

        level.addLevelListener(listener1);
        level.addLevelListener(listener2);

        Tube from = tubes.get(0);
        Tube to = tubes.get(3);

        level.executeMove(from, to);

        assertEquals(1, listener1.moveAttemptCount);
        assertEquals(1, listener2.moveAttemptCount);

        level.removeLevelListener(listener1);

        level.reset();
        level.executeMove(from, to);

        assertEquals(1, listener1.moveAttemptCount);
        assertEquals(2, listener2.moveAttemptCount);
    }

    @Test
    void test21_ExecuteMoveWithTubeNotInLevel() {
        Tube validTube = tubes.get(0);
        Tube outsideTube = new Tube(4);

        boolean result = level.executeMove(validTube, outsideTube);

        assertFalse(result);
    }
}