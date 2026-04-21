package game;

import model.*;
import rules.ChargeSequenceRule;
import rules.CompositeSequenceRule;
import rules.ColorSequenceRule;
import factory.LevelFactory;
import rules.SequenceRule;

import java.util.*;

public class Game {
    private Level _level;
    private final CompositeSequenceRule _rules = new CompositeSequenceRule(new ColorSequenceRule(), new ChargeSequenceRule());
    private final List<GameListener> _moveListeners = new ArrayList<>();

    public SequenceRule getRules() {
        return _rules;
    }

    public void addGameListener(GameListener listener) {
        _moveListeners.add(listener);
    }

    public void removeGameListener(GameListener listener) {
        _moveListeners.remove(listener);
    }

    public void start() {
        _level = LevelFactory.getRandomLevel();
    }

    public void startForTests() {
        _level = LevelFactory.createSimpleLevel();
    }

    public boolean tryMove(Tube from, Tube to) {
        from.setSelected(false);
        to.setSelected(false);

        if (!validateMove(from, to)) {
            notifyMoveAttempt(false, from, to);
            return false;
        }

        if(!_level.executeMove(from, to, _rules)) {
            notifyMoveAttempt(false, from, to);
            return false;
        }

        if (isLevelCompleted()) {
            notifyGameCompleted();
        }

        notifyMoveAttempt(true, from, to);
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
        }
    }

    private void notifyMoveAttempt(boolean success, Tube from, Tube to) {
        for (GameListener listener : _moveListeners) {
            listener.onMoveAttempt(success, from, to);
        }
    }

    private void notifyGameCompleted() {
        for (GameListener listener : _moveListeners) {
            listener.onGameCompleted();
        }
    }
}