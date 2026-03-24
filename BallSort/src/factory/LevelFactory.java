package factory;

import model.*;
import java.util.*;

public class LevelFactory {

    private static Tube createTube(int capacity, String... colors) {
        Tube tube = new Tube(capacity);
        for (String color : colors) {
            tube.push(new Ball(new ColorProperty(color)));
        }
        return tube;
    }

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

    public static Level createLevel4() {
        int capacity = 4;
        List<Tube> tubes = new ArrayList<>();

        tubes.add(createTube(capacity, "RED", "RED", "BLUE", "BLUE"));
        tubes.add(createTube(capacity, "GREEN", "GREEN", "YELLOW", "YELLOW"));
        tubes.add(createTube(capacity, "RED", "BLUE", "GREEN", "YELLOW"));
        tubes.add(createTube(capacity)); // пустая
        tubes.add(createTube(capacity)); // пустая

        return new Level(tubes);
    }

    public static Level createLevel5() {
        int capacity = 4;
        List<Tube> tubes = new ArrayList<>();

        tubes.add(createTube(capacity, "RED", "RED", "RED", "BLUE"));
        tubes.add(createTube(capacity, "BLUE", "BLUE", "GREEN", "GREEN"));
        tubes.add(createTube(capacity, "GREEN", "YELLOW", "YELLOW", "YELLOW"));
        tubes.add(createTube(capacity, "RED", "GREEN", "BLUE", "YELLOW"));
        tubes.add(createTube(capacity)); // пустая
        tubes.add(createTube(capacity)); // пустая

        return new Level(tubes);
    }

    public static Level createLevel6() {
        int capacity = 4;
        List<Tube> tubes = new ArrayList<>();

        tubes.add(createTube(capacity, "RED", "RED", "RED", "RED"));
        tubes.add(createTube(capacity, "BLUE", "BLUE", "BLUE", "BLUE"));
        tubes.add(createTube(capacity, "GREEN", "GREEN", "GREEN", "GREEN"));
        tubes.add(createTube(capacity, "YELLOW", "YELLOW", "YELLOW", "YELLOW"));
        tubes.add(createTube(capacity, "RED", "BLUE", "GREEN", "YELLOW"));
        tubes.add(createTube(capacity)); // пустая
        tubes.add(createTube(capacity)); // пустая

        return new Level(tubes);
    }

    public static Level createLevel7() {
        int capacity = 4;
        List<Tube> tubes = new ArrayList<>();

        tubes.add(createTube(capacity, "RED", "BLUE", "GREEN", "YELLOW"));
        tubes.add(createTube(capacity, "RED", "BLUE", "GREEN", "ORANGE"));
        tubes.add(createTube(capacity, "RED", "YELLOW", "ORANGE", "BLUE"));
        tubes.add(createTube(capacity, "GREEN", "YELLOW", "ORANGE", "RED"));
        tubes.add(createTube(capacity, "BLUE", "GREEN", "YELLOW", "ORANGE"));
        tubes.add(createTube(capacity)); // пустая

        return new Level(tubes);
    }

    public static Level createLevel8() {
        int capacity = 5;
        List<Tube> tubes = new ArrayList<>();

        tubes.add(createTube(capacity, "RED", "RED", "BLUE", "BLUE", "RED"));
        tubes.add(createTube(capacity, "GREEN", "GREEN", "YELLOW", "YELLOW", "BLUE"));
        tubes.add(createTube(capacity, "ORANGE", "ORANGE", "RED", "BLUE", "YELLOW"));
        tubes.add(createTube(capacity, "GREEN", "YELLOW", "ORANGE", "RED", "ORANGE"));
        tubes.add(createTube(capacity, "BLUE", "GREEN", "YELLOW", "ORANGE", "GREEN"));
        tubes.add(createTube(capacity)); // пустая
        tubes.add(createTube(capacity)); // пустая

        return new Level(tubes);
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