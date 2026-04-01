package factory;

import model.*;
import java.awt.Color;
import java.util.*;

public class LevelFactory {

    private static Tube createTube(int capacity, Color... colors) {
        Tube tube = new Tube(capacity);
        List<Ball> balls = new ArrayList<>();
        for (Color color : colors) {
            balls.add(new Ball(new ColorProperty(color)));
        }
        tube.pushSequence(balls);
        return tube;
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
        int capacity = 4;
        List<Tube> tubes = new ArrayList<>();

        tubes.add(createTube(capacity, Color.RED, Color.BLUE, Color.RED, Color.YELLOW));
        tubes.add(createTube(capacity, Color.GREEN, Color.BLUE, Color.GREEN, Color.YELLOW));
        tubes.add(createTube(capacity, Color.RED, Color.BLUE, Color.GREEN, Color.YELLOW));
        tubes.add(createTube(capacity)); // пустая
        tubes.add(createTube(capacity)); // пустая

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

    public static Level createLevel5() {
        int capacity = 4;
        List<Tube> tubes = new ArrayList<>();

        tubes.add(createTube(capacity, Color.RED, Color.RED, Color.RED, Color.BLUE));
        tubes.add(createTube(capacity, Color.BLUE, Color.BLUE, Color.GREEN, Color.GREEN));
        tubes.add(createTube(capacity, Color.GREEN, Color.YELLOW, Color.YELLOW, Color.YELLOW));
        tubes.add(createTube(capacity, Color.RED, Color.GREEN, Color.BLUE, Color.YELLOW));
        tubes.add(createTube(capacity)); // пустая
        tubes.add(createTube(capacity)); // пустая

        return new Level(tubes);
    }

    public static Level createLevel6() {
        int capacity = 4;
        List<Tube> tubes = new ArrayList<>();

        tubes.add(createTube(capacity, Color.RED, Color.RED, Color.RED, Color.RED));
        tubes.add(createTube(capacity, Color.BLUE, Color.BLUE, Color.BLUE, Color.BLUE));
        tubes.add(createTube(capacity, Color.GREEN, Color.GREEN, Color.GREEN, Color.GREEN));
        tubes.add(createTube(capacity, Color.YELLOW, Color.YELLOW, Color.YELLOW, Color.YELLOW));
        tubes.add(createTube(capacity, Color.RED, Color.BLUE, Color.GREEN, Color.YELLOW));
        tubes.add(createTube(capacity)); // пустая
        tubes.add(createTube(capacity)); // пустая

        return new Level(tubes);
    }

    public static Level createLevel7() {
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

    public static Level createLevel8() {
        int capacity = 5;
        List<Tube> tubes = new ArrayList<>();

        tubes.add(createTube(capacity, Color.RED, Color.RED, Color.BLUE, Color.BLUE, Color.RED));
        tubes.add(createTube(capacity, Color.GREEN, Color.GREEN, Color.YELLOW, Color.YELLOW, Color.BLUE));
        tubes.add(createTube(capacity, Color.ORANGE, Color.ORANGE, Color.RED, Color.BLUE, Color.YELLOW));
        tubes.add(createTube(capacity, Color.GREEN, Color.YELLOW, Color.ORANGE, Color.RED, Color.ORANGE));
        tubes.add(createTube(capacity, Color.BLUE, Color.GREEN, Color.YELLOW, Color.ORANGE, Color.GREEN));
        tubes.add(createTube(capacity)); // пустая
        tubes.add(createTube(capacity)); // пустая

        return new Level(tubes);
    }

    public static Level getRandomLevel() {
        Random random = new Random();
        int levelNumber = random.nextInt(8) + 1;
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
                return createLevel5();
            case 6:
                return createLevel6();
            case 7:
                return createLevel7();
            case 8:
                return createLevel8();
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
        levels.add(createLevel5());
        levels.add(createLevel6());
        levels.add(createLevel7());
        levels.add(createLevel8());
        return levels;
    }
}