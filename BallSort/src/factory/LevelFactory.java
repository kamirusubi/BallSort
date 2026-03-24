package factory;

import model.*;
import java.util.*;

public class LevelFactory {

    public static Level createLevel1() {
        int capacity = 4;
        List<Tube> tubes = new ArrayList<>();

        tubes.add(createTube(capacity, "RED", "RED", "GREEN", "RED"));
        tubes.add(createTube(capacity, "BLUE", "BLUE", "BLUE", "BLUE"));
        tubes.add(createTube(capacity, "GREEN", "RED", "GREEN", "GREEN"));
        tubes.add(createTube(capacity)); // пустая
        tubes.add(createTube(capacity)); // пустая

        return new Level(tubes);
    }

    public static Level createLevel2() {
        int capacity = 4;
        List<Tube> tubes = new ArrayList<>();

        tubes.add(createTube(capacity, "RED", "BLUE", "GREEN", "YELLOW"));
        tubes.add(createTube(capacity, "RED", "BLUE", "GREEN", "ORANGE"));
        tubes.add(createTube(capacity, "RED", "YELLOW", "ORANGE", "BLUE"));
        tubes.add(createTube(capacity, "GREEN", "YELLOW", "ORANGE", "RED"));
        tubes.add(createTube(capacity, "BLUE", "GREEN", "YELLOW", "ORANGE"));
        tubes.add(createTube(capacity)); // пустая
        tubes.add(createTube(capacity)); // пустая

        return new Level(tubes);
    }

    public static Level createLevel3() {
        int capacity = 4;
        List<Tube> tubes = new ArrayList<>();

        tubes.add(createTube(capacity, "RED", "BLUE", "RED", "YELLOW"));
        tubes.add(createTube(capacity, "GREEN", "BLUE", "GREEN", "YELLOW"));
        tubes.add(createTube(capacity, "RED", "BLUE", "GREEN", "YELLOW"));
        tubes.add(createTube(capacity)); // пустая
        tubes.add(createTube(capacity)); // пустая

        return new Level(tubes);
    }

    private static Tube createTube(int capacity, String... colors) {
        Tube tube = new Tube(capacity);
        for (String color : colors) {
            tube.push(new Ball(new ColorProperty(color)));
        }
        return tube;
    }

    public static Level getLevel(int levelNumber) {
        switch (levelNumber) {
            case 1:
                return createLevel1();
            case 2:
                return createLevel2();
            case 3:
                return createLevel3();
            default:
                throw new IllegalArgumentException("Неверный номер уровня: " + levelNumber);
        }
    }

    public static List<Level> getAllLevels() {
        List<Level> levels = new ArrayList<>();
        levels.add(createLevel1());
        levels.add(createLevel2());
        levels.add(createLevel3());
        return levels;
    }
}