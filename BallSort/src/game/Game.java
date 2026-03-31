package game;

import model.*;
import rules.CompositeSequenceRule;
import rules.ColorSequenceRule;
import factory.LevelFactory;
import rules.SequenceRule;

import java.util.*;

public class Game {
    private Level _level;
    private final CompositeSequenceRule _rules = new CompositeSequenceRule(new ColorSequenceRule());
    private boolean _isGameFinished = false;

    public SequenceRule getRules() {
        return _rules;
    }

    public void start() {
        _level = getRandomLevel();
    }

    public Level getRandomLevel() {
        Random random = new Random();
        int levelNumber = random.nextInt(8) + 1;
        return LevelFactory.getLevel(levelNumber);
    }


    public boolean tryMove(Tube from, Tube to) {
        if (!validateMove(from, to)) {
            return false;
        }

        if(!_level.executeMove(from, to, _rules)) return false;

        if (isLevelCompleted()) {
            _isGameFinished = true;
        }

        return true;
    }

    public boolean validateMove(Tube from, Tube to) {
        List<Tube> levelTubes = _level.getTubes();

        if(!levelTubes.contains(from) || !levelTubes.contains(to)) return false;

        if (from == null || from.isEmpty()) {
            return false;
        }

        if (to == null || !to.hasSpace()) {
            return false;
        }

        Ball fromTopBall = from.peekOne();
        Ball targetTopBall = to.peekOne();

        return _rules.canStack(fromTopBall, targetTopBall);
    }


    public boolean isLevelCompleted() {
        return _level.isLevelCompleted(_rules);
    }

    public Level getCurrentLevel() {
        return _level;
    }

    public void reset() {
        if (_level != null) {
            _level.reset();
            _isGameFinished = false;
        }
    }
}