package tests;

import model.Ball;
import model.ColorProperty;
import model.Tube;
import org.junit.jupiter.api.Test;
import rules.ColorSequenceRule;
import rules.CompositeSequenceRule;
import rules.SequenceRule;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class TubeTest {

    private final SequenceRule rule = new CompositeSequenceRule(new ColorSequenceRule());

    @Test
    void test01_ConstructorEmptyTube() {
        Tube tube = new Tube(4);

        assertNotNull(tube);
        assertEquals(4, tube.getCapacity());
        assertEquals(0, tube.getBallCount());
        assertTrue(tube.isEmpty());
        assertFalse(tube.isFull());
        assertTrue(tube.hasSpace());
    }

    @Test
    void test02_ConstructorWithBalls() {
        List<Ball> balls = new ArrayList<>();
        balls.add(new Ball());
        balls.add(new Ball());
        balls.add(new Ball());
        balls.add(new Ball());

        Tube tube = new Tube(4, balls);

        assertEquals(4, tube.getCapacity());
        assertEquals(4, tube.getBallCount());
        assertFalse(tube.isEmpty());
        assertTrue(tube.isFull());
        assertFalse(tube.hasSpace());
    }

    @Test
    void test03_ConstructorWithMoreBallsThanCapacity() {
        List<Ball> balls = new ArrayList<>();
        balls.add(new Ball());
        balls.add(new Ball());
        balls.add(new Ball());
        balls.add(new Ball());
        balls.add(new Ball());

        assertThrows(IllegalArgumentException.class, () -> new Tube(4, balls));
    }

    @Test
    void test04_FillEmptyTube() {
        Tube tube = new Tube(4);

        List<Ball> balls = new ArrayList<>();
        balls.add(new Ball(new ColorProperty(Color.RED)));
        balls.add(new Ball(new ColorProperty(Color.RED)));

        tube.fill(balls);

        assertEquals(2, tube.getBallCount());
        assertEquals(Color.RED, tube.getBalls().get(0).getProperty(ColorProperty.class).getColor());
        assertEquals(Color.RED, tube.getBalls().get(1).getProperty(ColorProperty.class).getColor());
    }

    @Test
    void test05_FillOverwriteExistingBalls() {
        Tube tube = new Tube(4);

        List<Ball> firstBalls = new ArrayList<>();
        firstBalls.add(new Ball(new ColorProperty(Color.RED)));
        tube.fill(firstBalls);
        assertEquals(1, tube.getBallCount());

        List<Ball> secondBalls = new ArrayList<>();
        secondBalls.add(new Ball(new ColorProperty(Color.BLUE)));
        secondBalls.add(new Ball(new ColorProperty(Color.BLUE)));
        tube.fill(secondBalls);

        assertEquals(2, tube.getBallCount());
        assertEquals(Color.BLUE, tube.getBalls().get(0).getProperty(ColorProperty.class).getColor());
        assertEquals(Color.BLUE, tube.getBalls().get(1).getProperty(ColorProperty.class).getColor());
    }

    @Test
    void test06_PeekOneOnEmptyTube() {
        Tube tube = new Tube(4);

        Ball ball = tube.peekOne();

        assertNull(ball);
    }

    @Test
    void test07_PeekOneOnNonEmptyTube() {
        Tube tube = new Tube(4);
        List<Ball> balls = new ArrayList<>();
        Ball redBall = new Ball(new ColorProperty(Color.RED));
        balls.add(redBall);
        tube.fill(balls);

        Ball ball = tube.peekOne();

        assertNotNull(ball);
        assertEquals(Color.RED, ball.getProperty(ColorProperty.class).getColor());
        assertEquals(1, tube.getBallCount());
    }

    @Test
    void test08_PeekSequenceWithConsecutiveMatchingBalls() {
        Tube tube = new Tube(4);
        List<Ball> balls = new ArrayList<>();
        balls.add(new Ball(new ColorProperty(Color.BLUE)));
        balls.add(new Ball(new ColorProperty(Color.RED)));
        balls.add(new Ball(new ColorProperty(Color.RED)));
        balls.add(new Ball(new ColorProperty(Color.RED)));
        tube.fill(balls);

        List<Ball> sequence = tube.peekSequence(rule);

        assertEquals(3, sequence.size());
        assertEquals(Color.RED, sequence.get(0).getProperty(ColorProperty.class).getColor());
        assertEquals(Color.RED, sequence.get(1).getProperty(ColorProperty.class).getColor());
        assertEquals(Color.RED, sequence.get(2).getProperty(ColorProperty.class).getColor());
        assertEquals(4, tube.getBallCount());
    }

    @Test
    void test09_PeekSequenceWithNonConsecutiveBalls() {
        Tube tube = new Tube(4);
        List<Ball> balls = new ArrayList<>();
        balls.add(new Ball(new ColorProperty(Color.RED)));
        balls.add(new Ball(new ColorProperty(Color.RED)));
        balls.add(new Ball(new ColorProperty(Color.BLUE)));
        balls.add(new Ball(new ColorProperty(Color.RED)));
        tube.fill(balls);

        List<Ball> sequence = tube.peekSequence(rule);

        assertEquals(1, sequence.size());
        assertEquals(Color.RED, sequence.get(0).getProperty(ColorProperty.class).getColor());
        assertEquals(4, tube.getBallCount());
    }

    @Test
    void test10_PopOneOnEmptyTube() {
        Tube tube = new Tube(4);

        Ball ball = tube.popOne();

        assertNull(ball);
        assertEquals(0, tube.getBallCount());
    }

    @Test
    void test11_PopOneOnNonEmptyTube() {
        Tube tube = new Tube(4);
        List<Ball> balls = new ArrayList<>();
        Ball redBall = new Ball(new ColorProperty(Color.RED));
        balls.add(redBall);
        tube.fill(balls);

        Ball ball = tube.popOne();

        assertNotNull(ball);
        assertEquals(Color.RED, ball.getProperty(ColorProperty.class).getColor());
        assertEquals(0, tube.getBallCount());
    }

    @Test
    void test12_PopSequenceWithConsecutiveMatchingBalls() {
        Tube tube = new Tube(4);
        List<Ball> balls = new ArrayList<>();
        balls.add(new Ball(new ColorProperty(Color.BLUE)));
        balls.add(new Ball(new ColorProperty(Color.RED)));
        balls.add(new Ball(new ColorProperty(Color.RED)));
        balls.add(new Ball(new ColorProperty(Color.RED)));
        tube.fill(balls);

        List<Ball> sequence = tube.popSequence(rule);

        assertEquals(3, sequence.size());
        assertEquals(Color.RED, sequence.get(0).getProperty(ColorProperty.class).getColor());
        assertEquals(Color.RED, sequence.get(1).getProperty(ColorProperty.class).getColor());
        assertEquals(Color.RED, sequence.get(2).getProperty(ColorProperty.class).getColor());
        assertEquals(1, tube.getBallCount());
        assertEquals(Color.BLUE, tube.peekOne().getProperty(ColorProperty.class).getColor());
    }

    @Test
    void test13_PopSequenceWithNonConsecutiveBalls() {
        Tube tube = new Tube(4);
        List<Ball> balls = new ArrayList<>();
        balls.add(new Ball(new ColorProperty(Color.RED)));
        balls.add(new Ball(new ColorProperty(Color.RED)));
        balls.add(new Ball(new ColorProperty(Color.BLUE)));
        balls.add(new Ball(new ColorProperty(Color.RED)));
        tube.fill(balls);

        List<Ball> sequence = tube.popSequence(rule);

        assertEquals(1, sequence.size());
        assertEquals(Color.RED, sequence.get(0).getProperty(ColorProperty.class).getColor());
        assertEquals(3, tube.getBallCount());
        assertEquals(Color.BLUE, tube.peekOne().getProperty(ColorProperty.class).getColor());
    }

    @Test
    void test14_PushOneWithSpaceAvailable() {
        Tube tube = new Tube(4);
        Ball redBall = new Ball(new ColorProperty(Color.RED));

        tube.pushOne(redBall);

        assertEquals(1, tube.getBallCount());
        assertEquals(Color.RED, tube.peekOne().getProperty(ColorProperty.class).getColor());
        assertTrue(tube.hasSpace());
    }

    @Test
    void test15_PushOneWithoutSpace() {
        Tube tube = new Tube(2);
        tube.pushOne(new Ball(new ColorProperty(Color.RED)));
        tube.pushOne(new Ball(new ColorProperty(Color.RED)));

        Ball extraBall = new Ball(new ColorProperty(Color.BLUE));
        tube.pushOne(extraBall);

        assertEquals(2, tube.getBallCount());
        assertFalse(tube.hasSpace());
        assertEquals(Color.RED, tube.peekOne().getProperty(ColorProperty.class).getColor());
    }

    @Test
    void test16_PushSequenceWithSpaceAvailable() {
        Tube tube = new Tube(4);
        List<Ball> balls = new ArrayList<>();
        balls.add(new Ball(new ColorProperty(Color.RED)));
        balls.add(new Ball(new ColorProperty(Color.RED)));

        boolean result = tube.pushSequence(balls);

        assertTrue(result);
        assertEquals(2, tube.getBallCount());
        assertEquals(Color.RED, tube.getBalls().get(0).getProperty(ColorProperty.class).getColor());
        assertEquals(Color.RED, tube.getBalls().get(1).getProperty(ColorProperty.class).getColor());
    }

    @Test
    void test17_PushSequenceWithoutSpace() {
        Tube tube = new Tube(2);
        tube.pushOne(new Ball(new ColorProperty(Color.RED)));
        tube.pushOne(new Ball(new ColorProperty(Color.RED)));

        List<Ball> balls = new ArrayList<>();
        balls.add(new Ball(new ColorProperty(Color.BLUE)));
        balls.add(new Ball(new ColorProperty(Color.BLUE)));

        boolean result = tube.pushSequence(balls);

        assertFalse(result);
        assertEquals(2, tube.getBallCount());
        assertEquals(Color.RED, tube.peekOne().getProperty(ColorProperty.class).getColor());
    }

    @Test
    void test18_PushSequenceWithNotEnoughSpace() {
        Tube tube = new Tube(3);
        tube.pushOne(new Ball(new ColorProperty(Color.RED)));
        tube.pushOne(new Ball(new ColorProperty(Color.RED)));

        List<Ball> balls = new ArrayList<>();
        balls.add(new Ball(new ColorProperty(Color.BLUE)));
        balls.add(new Ball(new ColorProperty(Color.BLUE)));

        boolean result = tube.pushSequence(balls);

        assertTrue(result);
        assertEquals(3, tube.getBallCount());
        assertEquals(Color.BLUE, tube.peekOne().getProperty(ColorProperty.class).getColor());
    }
}