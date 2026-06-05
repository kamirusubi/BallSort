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
        balls.add(new Ball(new ColorProperty(Color.RED)));
        balls.add(new Ball(new ColorProperty(Color.RED)));
        balls.add(new Ball(new ColorProperty(Color.RED)));
        balls.add(new Ball(new ColorProperty(Color.RED)));

        Tube tube = new Tube(4, balls, rule);

        assertEquals(4, tube.getCapacity());
        assertEquals(4, tube.getBallCount());
        assertFalse(tube.isEmpty());
        assertFalse(tube.hasSpace());
    }

    @Test
    void test03_ConstructorWithMoreBallsThanCapacity() {
        List<Ball> balls = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            balls.add(new Ball(new ColorProperty(Color.RED)));
        }

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
        assertNull(tube.peekOne());
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
        assertNull(tube.popOne());
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
    void test13_MoveToTransfersBalls() {
        Tube from = new Tube(4, rule);
        Tube to = new Tube(4, rule);

        from.pushOne(new Ball(new ColorProperty(Color.RED)));
        from.pushOne(new Ball(new ColorProperty(Color.RED)));

        int moved = from.moveTo(to);

        assertEquals(2, moved);
        assertEquals(0, from.getBallCount());
        assertEquals(2, to.getBallCount());
    }

    @Test
    void test14_MoveToRespectsCapacity() {
        Tube from = new Tube(4, rule);
        Tube to = new Tube(2, rule);

        from.pushOne(new Ball(new ColorProperty(Color.RED)));
        from.pushOne(new Ball(new ColorProperty(Color.RED)));
        from.pushOne(new Ball(new ColorProperty(Color.RED)));

        int moved = from.moveTo(to);

        assertEquals(2, moved);
        assertEquals(1, from.getBallCount());
        assertEquals(2, to.getBallCount());
    }

    @Test
    void test15_MoveToFailsWhenColorsDontMatch() {
        Tube from = new Tube(4, rule);
        Tube to = new Tube(4, rule);

        to.pushOne(new Ball(new ColorProperty(Color.BLUE)));
        from.pushOne(new Ball(new ColorProperty(Color.RED)));

        int moved = from.moveTo(to);

        assertEquals(0, moved);
        assertEquals(1, from.getBallCount());
        assertEquals(1, to.getBallCount());
    }

    @Test
    void test16_IsUniformedReturnsTrueForUniformTube() {
        Tube tube = new Tube(4, rule);
        tube.pushOne(new Ball(new ColorProperty(Color.RED)));
        tube.pushOne(new Ball(new ColorProperty(Color.RED)));
        tube.pushOne(new Ball(new ColorProperty(Color.RED)));

        assertTrue(tube.isUniformed());
    }

    @Test
    void test17_IsUniformedReturnsFalseForNonUniformTube() {
        Tube tube = new Tube(4, rule);
        tube.pushOne(new Ball(new ColorProperty(Color.RED)));
        tube.pushOne(new Ball(new ColorProperty(Color.RED)));
        tube.pushOne(new Ball(new ColorProperty(Color.BLUE)));

        assertFalse(tube.isUniformed());
    }

    @Test
    void test18_ResetRestoresOriginalBalls() {
        List<Ball> balls = new ArrayList<>();
        balls.add(new Ball(new ColorProperty(Color.RED)));
        balls.add(new Ball(new ColorProperty(Color.RED)));

        Tube tube = new Tube(4, balls, rule);
        tube.popOne();

        assertEquals(1, tube.getBallCount());

        tube.reset();

        assertEquals(2, tube.getBallCount());
        assertEquals(Color.RED, tube.peekOne().getProperty(ColorProperty.class).getColor());
    }

    @Test
    void test19_CanStackOnTopReturnsTrueForEmptyTube() {
        Tube tube = new Tube(4, rule);
        assertTrue(tube.canStackOnTop(new Ball(new ColorProperty(Color.RED))));
    }

    @Test
    void test20_CanStackOnTopReturnsTrueForMatchingBalls() {
        Tube tube = new Tube(4, rule);
        tube.pushOne(new Ball(new ColorProperty(Color.RED)));
        assertTrue(tube.canStackOnTop(new Ball(new ColorProperty(Color.RED))));
    }

    @Test
    void test21_CanStackOnTopReturnsFalseForNonMatchingBalls() {
        Tube tube = new Tube(4, rule);
        tube.pushOne(new Ball(new ColorProperty(Color.RED)));
        assertFalse(tube.canStackOnTop(new Ball(new ColorProperty(Color.BLUE))));
    }
}