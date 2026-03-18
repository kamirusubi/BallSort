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

    public static Level createLevelWithColors() {
        int capacity = 4;
        List<Tube> tubes = new ArrayList<>();

        tubes.add(createTube(capacity, Color.RED, Color.RED, Color.GREEN, Color.RED));
        tubes.add(createTube(capacity, Color.BLUE, Color.BLUE, Color.BLUE, Color.BLUE));
        tubes.add(createTube(capacity, Color.GREEN, Color.RED, Color.GREEN, Color.GREEN));
        tubes.add(createTube(capacity)); // пустая
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
        int levelNumber = random.nextInt(2) + 1;
        return getLevel(levelNumber);
    }

    public static Level getLevel(int levelNumber) {
        switch (levelNumber) {
            case 1:
                return createLevelWithColors();
            case 2:
                return createLevelWithCharges();
            default:
                throw new IllegalArgumentException("Неверный номер уровня: " + levelNumber);
        }
    }

    public static List<Level> getAllLevels() {
        List<Level> levels = new ArrayList<>();
        levels.add(createLevelWithColors());
        levels.add(createLevelWithCharges());
        return levels;
    }
}