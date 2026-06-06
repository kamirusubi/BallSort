package factory;

import model.*;
import rules.*;

import java.awt.Color;
import java.util.*;

public class LevelFactory {
    // Уровень для тестов
    public static Level createSimpleLevel() {
        int capacity = 2;
        List<Tube> tubes = new ArrayList<>();
        SequenceRule rules = new ColorSequenceRule();

        tubes.add(createTube(capacity, rules, Color.RED, Color.RED));
        tubes.add(createTube(capacity, rules, Color.BLUE));
        tubes.add(createTube(capacity, rules, Color.BLUE));
        tubes.add(createTube(capacity, rules)); // пустая

        return new Level(tubes);
    }

    public static Level createLevelWithColors() {
        int capacity = 4;
        List<Tube> tubes = new ArrayList<>();
        SequenceRule rules = new ColorSequenceRule();

        tubes.add(createTube(capacity, rules, Color.RED, Color.RED, Color.GREEN, Color.RED));
        tubes.add(createTube(capacity, rules, Color.BLUE, Color.BLUE, Color.BLUE, Color.BLUE));
        tubes.add(createTube(capacity, rules, Color.GREEN, Color.RED, Color.GREEN, Color.GREEN));
        tubes.add(createTube(capacity, rules)); // пустая
        tubes.add(createTube(capacity, rules)); // пустая

        return new Level(tubes);
    }

    public static Level createLevelWithFragile() {
        int capacity = 4;
        List<Tube> tubes = new ArrayList<>();
        SequenceRule rules = new CompositeSequenceRule(new ColorSequenceRule(), new FragileSequenceRule());

        tubes.add(createTube(capacity, rules, Color.RED, Color.RED, Color.GREEN, Color.BLUE));
        tubes.add(createTube(capacity, rules, Color.BLUE, Color.BLUE, Color.BLUE, Color.RED));

        Ball greenFragile = new Ball(new ColorProperty(Color.GREEN), new FragileProperty());

        tubes.add(createTube(capacity, rules, Color.GREEN, Color.RED, Color.GREEN, greenFragile));

        tubes.add(createTube(capacity, rules)); // пустая
        tubes.add(createTube(capacity, rules)); // пустая

        return new Level(tubes);
    }

    public static Level createLevelWithCharges() {
        int capacity = 4;
        List<Tube> tubes = new ArrayList<>();
        SequenceRule rules = new CompositeSequenceRule(new ColorSequenceRule(), new ChargeSequenceRule());

        Ball redPositive = createBall(Color.RED, Charge.POSITIVE);
        Ball redNegative = createBall(Color.RED, Charge.NEGATIVE);
        Ball bluePositive = createBall(Color.BLUE, Charge.POSITIVE);
        Ball blueNegative = createBall(Color.BLUE, Charge.NEGATIVE);
        Ball greenPositive = createBall(Color.GREEN, Charge.POSITIVE);
        Ball greenNegative = createBall(Color.GREEN, Charge.NEGATIVE);
        Ball yellowPositive = createBall(Color.YELLOW, Charge.POSITIVE);
        Ball yellowNegative = createBall(Color.YELLOW, Charge.NEGATIVE);

        tubes.add(createTube(capacity, rules, redPositive, redNegative, Color.RED, redPositive));
        tubes.add(createTube(capacity, rules, bluePositive, blueNegative, Color.BLUE, greenPositive));
        tubes.add(createTube(capacity, rules, greenPositive, greenNegative, Color.GREEN, bluePositive));
        tubes.add(createTube(capacity, rules, yellowPositive, yellowNegative, Color.YELLOW, yellowPositive));
        tubes.add(createTube(capacity, rules));
        tubes.add(createTube(capacity, rules));

        return new Level(tubes);
    }

    public static Level getRandomLevel() {
        Random random = new Random();
        int levelNumber = random.nextInt(3) + 1;
        return getLevel(levelNumber);
    }

    private static Level getLevel(int levelNumber) {
        switch (levelNumber) {
            case 1:
                return createLevelWithColors();
            case 2:
                return createLevelWithFragile();
            case 3:
                return createLevelWithCharges();
            default:
                throw new IllegalArgumentException("Неверный номер уровня: " + levelNumber);
        }
    }

    private static Tube createTube(int capacity, SequenceRule rules, Object... ballSpecs) {
        List<Ball> balls = new ArrayList<>();

        for (Object spec : ballSpecs) {
            if (spec instanceof Ball) {
                balls.add((Ball) spec);
            } else if (spec instanceof Color) {
                balls.add(new Ball(new ColorProperty((Color) spec)));
            }
        }

        return new Tube(capacity, balls, rules);
    }

    private static Ball createBall(Color color, Charge charge) {
        return new Ball(new ColorProperty(color), new ChargeProperty(charge));
    }
}