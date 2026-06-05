package game;

import model.*;
import factory.LevelFactory;
import rules.SequenceRule;

import java.util.*;

public class Game {
    private Level _level;

    public void start() {
        _level = LevelFactory.getRandomLevel();
    }

    public void startForTests() {
        _level = LevelFactory.createSimpleLevel();
    }

    public SequenceRule getRules() {
        return _level != null ? _level.getRules() : null;
    }

    public boolean isLevelCompleted() {
        return _level != null && _level.isLevelCompleted();
    }

    public Level getCurrentLevel() {
        return _level;
    }

    public void reset() {
        if (_level != null) {
            _level.reset();
        }
    }
}