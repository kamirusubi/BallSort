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
        Tube tube = new Tube(4, rule);

        assertNotNull(tube);
        assertEquals(4, tube.getCapacity());
        assertEquals(0, tube.getBallCount());
        assertTrue(tube.isEmpty());
        assertTrue(tube.hasSpace());
    }

    @Test
    void test02_ConstructorWithBalls() {
        List<Ball> balls = new ArrayList<>();
        balls.add(new Ball());
        balls.add(new Ball());
        balls.add(new Ball());
        balls.add(new Ball());

        Tube tube = new Tube(4, balls, rule);

        assertEquals(4, tube.getCapacity());
        assertEquals(4, tube.getBallCount());
        assertFalse(tube.isEmpty());
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

        assertThrows(IllegalArgumentException.class, () -> new Tube(4, balls, rule));
    }

    @Test
    void test04_CreateTubeWithBallsUsingConstructor() {
        List<Ball> balls = new ArrayList<>();
        balls.add(new Ball(new ColorProperty(Color.RED)));
        balls.add(new Ball(new ColorProperty(Color.RED)));

        Tube tube = new Tube(4, balls, rule);

        assertEquals(2, tube.getBallCount());
        assertEquals(Color.RED, tube.getBalls().get(0).getProperty(ColorProperty.class).getColor());
        assertEquals(Color.RED, tube.getBalls().get(1).getProperty(ColorProperty.class).getColor());
    }

    @Test
    void test05_PeekOneOnEmptyTube() {
        Tube tube = new Tube(4, rule);

        Ball ball = tube.peekOne();

        assertNull(ball);
    }

    @Test
    void test06_PeekOneOnNonEmptyTube() {
        List<Ball> balls = new ArrayList<>();
        Ball redBall = new Ball(new ColorProperty(Color.RED));
        balls.add(redBall);
        Tube tube = new Tube(4, balls, rule);

        Ball ball = tube.peekOne();

        assertNotNull(ball);
        assertEquals(Color.RED, ball.getProperty(ColorProperty.class).getColor());
        assertEquals(1, tube.getBallCount());
    }

    @Test
    void test07_PeekSequenceWithConsecutiveMatchingBalls() {
        List<Ball> balls = new ArrayList<>();
        balls.add(new Ball(new ColorProperty(Color.BLUE)));
        balls.add(new Ball(new ColorProperty(Color.RED)));
        balls.add(new Ball(new ColorProperty(Color.RED)));
        balls.add(new Ball(new ColorProperty(Color.RED)));
        Tube tube = new Tube(4, balls, rule);

        List<Ball> sequence = tube.peekSequence();

        assertEquals(3, sequence.size());
        assertEquals(Color.RED, sequence.get(0).getProperty(ColorProperty.class).getColor());
        assertEquals(Color.RED, sequence.get(1).getProperty(ColorProperty.class).getColor());
        assertEquals(Color.RED, sequence.get(2).getProperty(ColorProperty.class).getColor());
        assertEquals(4, tube.getBallCount());
    }

    @Test
    void test08_PeekSequenceWithNonConsecutiveBalls() {
        List<Ball> balls = new ArrayList<>();
        balls.add(new Ball(new ColorProperty(Color.RED)));
        balls.add(new Ball(new ColorProperty(Color.RED)));
        balls.add(new Ball(new ColorProperty(Color.BLUE)));
        balls.add(new Ball(new ColorProperty(Color.RED)));
        Tube tube = new Tube(4, balls, rule);

        List<Ball> sequence = tube.peekSequence();

        assertEquals(1, sequence.size());
        assertEquals(Color.RED, sequence.get(0).getProperty(ColorProperty.class).getColor());
        assertEquals(4, tube.getBallCount());
    }

    @Test
    void test09_PopOneOnEmptyTube() {
        Tube tube = new Tube(4, rule);

        Ball ball = tube.popOne();

        assertNull(ball);
        assertEquals(0, tube.getBallCount());
    }

    @Test
    void test10_PopOneOnNonEmptyTube() {
        List<Ball> balls = new ArrayList<>();
        Ball redBall = new Ball(new ColorProperty(Color.RED));
        balls.add(redBall);
        Tube tube = new Tube(4, balls, rule);

        Ball ball = tube.popOne();

        assertNotNull(ball);
        assertEquals(Color.RED, ball.getProperty(ColorProperty.class).getColor());
        assertEquals(0, tube.getBallCount());
    }

    @Test
    void test11_PushOneWithSpaceAvailable() {
        Tube tube = new Tube(4, rule);
        Ball redBall = new Ball(new ColorProperty(Color.RED));

        tube.pushOne(redBall);

        assertEquals(1, tube.getBallCount());
        assertEquals(Color.RED, tube.peekOne().getProperty(ColorProperty.class).getColor());
        assertTrue(tube.hasSpace());
    }

    @Test
    void test12_PushOneWithoutSpace() {
        Tube tube = new Tube(2, rule);
        tube.pushOne(new Ball(new ColorProperty(Color.RED)));
        tube.pushOne(new Ball(new ColorProperty(Color.RED)));

        Ball extraBall = new Ball(new ColorProperty(Color.BLUE));
        tube.pushOne(extraBall);

        assertEquals(2, tube.getBallCount());
        assertFalse(tube.hasSpace());
        assertEquals(Color.RED, tube.peekOne().getProperty(ColorProperty.class).getColor());
    }

    @Test
    void test13_PushSequenceWithSpaceAvailable() {
        Tube tube = new Tube(4, rule);
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
    void test14_PushSequenceWithoutSpace() {
        Tube tube = new Tube(2, rule);
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
    void test15_PushSequenceWithNotEnoughSpace() {
        Tube tube = new Tube(3, rule);
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