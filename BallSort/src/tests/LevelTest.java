package tests;

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

        Level newLevel = new Level(originalTubes);
        List<Tube> newTubes = newLevel.getTubes();

        assertEquals(originalTubes.size(), newTubes.size());

        originalTubes.getFirst().popOne();

        assertNotEquals(originalTubes.getFirst().getBallCount(), newTubes.getFirst().getBallCount());
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
        level.executeMove(from, to, rule);

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

        boolean result = level.executeMove(from, to, rule);

        assertTrue(result);
        assertEquals(fromInitialCount - expectedMoveCount, from.getBallCount());
        assertEquals(toInitialCount + expectedMoveCount, to.getBallCount());
    }

    @Test
    void test06_ExecuteMoveFromEmptyTubeFails() {
        Tube from = tubes.get(3);
        Tube to = tubes.get(2);

        boolean result = level.executeMove(from, to, rule);

        assertFalse(result);
    }

    @Test
    void test07_ExecuteMoveToFullTubeFails() {
        Tube from = tubes.get(0);
        Tube to = tubes.get(3);

        level.executeMove(from, to, rule);

        Tube anotherTo = tubes.get(1);
        level.executeMove(anotherTo, to, rule);

        assertTrue(to.isFull());

        boolean result = level.executeMove(to, to, rule);

        assertFalse(result);
        assertEquals(to.getBallCount(), to.getBallCount());
        assertTrue(to.isFull());
    }

    @Test
    void test08_IsLevelCompletedReturnsTrue() {
        Tube from = tubes.get(1);
        Tube to = tubes.get(2);

        level.executeMove(from, to, rule);
        assertTrue(level.isLevelCompleted(rule));
    }

    @Test
    void test9_IsLevelCompletedReturnsFalse() {
        assertFalse(level.isLevelCompleted(rule));
    }

    @Test
    void test10_ExecuteMoveWithSecondEmptyTube() {
        Tube from = tubes.get(1);
        Tube to = tubes.get(3);

        Ball fromBall = from.peekOne();
        assertEquals(Color.BLUE, fromBall.getProperty(ColorProperty.class).getColor());

        boolean result = level.executeMove(from, to, rule);

        assertTrue(result);
        assertEquals(0, from.getBallCount());
        assertEquals(1, to.getBallCount());
    }

    @Test
    void test11_MultipleMovesWorkCorrectly() {
        Tube tube0 = tubes.get(0);
        Tube tube1 = tubes.get(1);
        Tube tube3 = tubes.get(3);

        level.executeMove(tube0, tube3, rule);
        int tube3AfterFirst = tube3.getBallCount();

        boolean secondMoveResult = level.executeMove(tube1, tube0, rule);

        assertTrue(secondMoveResult);
        assertEquals(tube3AfterFirst, tube3.getBallCount());
    }

    @Test
    void test12_ResetAfterMultipleMoves() {
        List<Integer> initialCounts = tubes.stream()
                .map(Tube::getBallCount)
                .toList();

        level.executeMove(tubes.get(0), tubes.get(3), rule);
        level.executeMove(tubes.get(1), tubes.get(3), rule);

        assertNotEquals(initialCounts, tubes.stream().map(Tube::getBallCount).toList());

        level.reset();

        assertEquals(initialCounts, tubes.stream().map(Tube::getBallCount).toList());
    }

    @Test
    void test13_ExecuteMoveWithNullParameters() {
        Tube validTube = tubes.getFirst();

        assertThrows(NullPointerException.class, () -> level.executeMove(null, validTube, rule));

        assertThrows(NullPointerException.class, () -> level.executeMove(validTube, null, rule));
    }

    @Test
    void test14_IsLevelCompletedWithEmptyTubes() {
        List<Tube> customTubes = List.of(
                new Tube(4),
                new Tube(4)
        );
        Level emptyLevel = new Level(customTubes);

        assertTrue(emptyLevel.isLevelCompleted(rule));
    }
}