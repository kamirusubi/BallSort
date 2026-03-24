package game;

import model.*;
import rules.CompositeSequenceRule;
import rules.ColorSequenceRule;
import factory.LevelFactory;

import java.util.*;

public class Game {
    private Level _level;
    private final CompositeSequenceRule _rules;
    private boolean _isGameFinished = false;

    public Game() {
        _rules = new CompositeSequenceRule();
        _rules.addRule(new ColorSequenceRule());
    }


    public void start() {
        _level = loadRandomLevel();
    }


    public Level loadRandomLevel() {
        Random random = new Random();
        int levelNumber = random.nextInt(8) + 1;
        return LevelFactory.getLevel(levelNumber);
    }


    public boolean tryMove(Tube from, Tube to) {
        if (!validateMove(from, to)) {
            return false;
        }

        if(!_level.executeMove(from, to)) return false;

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

        Ball ballToMove = from.peek();
        Ball targetTopBall = to.peek();

        return _rules.canStack(ballToMove, targetTopBall);
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