package factory;

import model.*;
import java.awt.Color;
import java.util.*;

public class LevelFactory {

    private static Tube createTube(int capacity, Object... ballSpecs) {
        Tube tube = new Tube(capacity);
        List<Ball> balls = new ArrayList<>();

        for (Object spec : ballSpecs) {
            if (spec instanceof Ball) {
                balls.add((Ball) spec);
            } else if (spec instanceof Color) {
                balls.add(new Ball(new ColorProperty((Color) spec)));
            }
        }

        tube.pushSequence(balls);
        return tube;
    }

    private static Ball createBall(Color color, Charge charge) {
        return new Ball(new ColorProperty(color), new ChargeProperty(charge));
    }

    // Уровень для тестов
    public static Level createSimpleLevel() {
        int capacity = 2;
        List<Tube> tubes = new ArrayList<>();

        tubes.add(createTube(capacity, Color.RED, Color.RED));
        tubes.add(createTube(capacity, Color.BLUE));
        tubes.add(createTube(capacity, Color.BLUE));
        tubes.add(createTube(capacity)); // пустая

        return new Level(tubes);
    }

    public static Level createLevel1() {
        int capacity = 4;
        List<Tube> tubes = new ArrayList<>();

        tubes.add(createTube(capacity, Color.RED, Color.RED, Color.GREEN, Color.RED));
        tubes.add(createTube(capacity, Color.BLUE, Color.BLUE, Color.BLUE, Color.BLUE));
        tubes.add(createTube(capacity, Color.GREEN, Color.RED, Color.GREEN, Color.GREEN));
        tubes.add(createTube(capacity)); // пустая
        tubes.add(createTube(capacity)); // пустая

        return new Level(tubes);
    }

    public static Level createLevel2() {
        int capacity = 4;
        List<Tube> tubes = new ArrayList<>();

        tubes.add(createTube(capacity, Color.RED, Color.BLUE, Color.GREEN, Color.YELLOW));
        tubes.add(createTube(capacity, Color.RED, Color.BLUE, Color.GREEN, Color.ORANGE));
        tubes.add(createTube(capacity, Color.RED, Color.YELLOW, Color.ORANGE, Color.BLUE));
        tubes.add(createTube(capacity, Color.GREEN, Color.YELLOW, Color.ORANGE, Color.RED));
        tubes.add(createTube(capacity, Color.BLUE, Color.GREEN, Color.YELLOW, Color.ORANGE));
        tubes.add(createTube(capacity)); // пустая

        return new Level(tubes);
    }

    public static Level createLevel3() {
        int capacity = 3;
        List<Tube> tubes = new ArrayList<>();

        tubes.add(createTube(capacity, Color.RED, Color.BLUE, Color.YELLOW));
        tubes.add(createTube(capacity, Color.GREEN, Color.GREEN, Color.YELLOW));
        tubes.add(createTube(capacity, Color.RED, Color.BLUE, Color.GREEN));
        tubes.add(createTube(capacity, Color.RED, Color.YELLOW));
        tubes.add(createTube(capacity, Color.BLUE));

        return new Level(tubes);
    }

    public static Level createLevel4() {
        int capacity = 3;
        List<Tube> tubes = new ArrayList<>();

        tubes.add(createTube(capacity, Color.RED, Color.RED, Color.BLUE));
        tubes.add(createTube(capacity, Color.GREEN, Color.GREEN, Color.YELLOW));
        tubes.add(createTube(capacity, Color.RED, Color.BLUE, Color.GREEN));
        tubes.add(createTube(capacity, Color.YELLOW, Color.YELLOW, Color.BLUE));
        tubes.add(createTube(capacity)); // пустая

        return new Level(tubes);
    }

    public static Level createLevelWithCharges() {
        int capacity = 4;
        List<Tube> tubes = new ArrayList<>();

        Ball redPositive = createBall(Color.RED, Charge.POSITIVE);
        Ball redNegative = createBall(Color.RED, Charge.NEGATIVE);
        Ball bluePositive = createBall(Color.BLUE, Charge.POSITIVE);
        Ball blueNegative = createBall(Color.BLUE, Charge.NEGATIVE);
        Ball greenPositive = createBall(Color.GREEN, Charge.POSITIVE);
        Ball greenNegative = createBall(Color.GREEN, Charge.NEGATIVE);
        Ball yellowPositive = createBall(Color.YELLOW, Charge.POSITIVE);
        Ball yellowNegative = createBall(Color.YELLOW, Charge.NEGATIVE);

        tubes.add(createTube(capacity, redPositive, redNegative, Color.RED, redPositive));

        tubes.add(createTube(capacity, bluePositive, blueNegative, Color.BLUE, greenPositive));

        tubes.add(createTube(capacity, greenPositive, greenNegative, Color.GREEN, bluePositive));

        tubes.add(createTube(capacity, yellowPositive, yellowNegative, Color.YELLOW, yellowPositive));

        tubes.add(createTube(capacity));
        tubes.add(createTube(capacity));

        return new Level(tubes);
    }

    public static Level getRandomLevel() {
        Random random = new Random();
        int levelNumber = random.nextInt(5) + 1;
        return getLevel(levelNumber);
    }

    public static Level getLevel(int levelNumber) {
        switch (levelNumber) {
            case 1:
                return createLevel1();
            case 2:
                return createLevel2();
            case 3:
                return createLevel3();
            case 4:
                return createLevel4();
            case 5:
                return createLevelWithCharges();
            default:
                throw new IllegalArgumentException("Неверный номер уровня: " + levelNumber);
        }
    }

    public static List<Level> getAllLevels() {
        List<Level> levels = new ArrayList<>();
        levels.add(createLevel1());
        levels.add(createLevel2());
        levels.add(createLevel3());
        levels.add(createLevel4());
        levels.add(createLevelWithCharges());
        return levels;
    }
}