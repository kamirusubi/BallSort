package game;

import model.*;
import rules.SequenceRule;
import factory.LevelFactory;
import java.util.*;

public class Game {
    private Level _level;
    private final List<GameListener> _moveListeners = new ArrayList<>();

    public void start() {
        _level = LevelFactory.getRandomLevel();
    }

    public void startForTests() {
        _level = LevelFactory.createSimpleLevel();
    }

    public SequenceRule getRules() {
        return _level != null ? _level.getRules() : null;
    }

    public boolean tryMove(Tube from, Tube to) {
        from.setSelected(false);
        to.setSelected(false);

        if (!validateMove(from, to)) {
            notifyMoveAttempt(false, from, to);
            return false;
        }

        if (!_level.executeMove(from, to)) {
            notifyMoveAttempt(false, from, to);
            return false;
        }

        notifyMoveAttempt(true, from, to);

        if (isLevelCompleted()) {
            notifyGameCompleted();
        }

        return true;
    }

    public boolean validateMove(Tube from, Tube to) {
        List<Tube> levelTubes = _level.getTubes();
        if (!levelTubes.contains(from) || !levelTubes.contains(to)) return false;
        if (from == null || from.isEmpty()) return false;
        if (to == null || !to.hasSpace()) return false;

        return _level.getRules().canStack(from.peekOne(), to.peekOne());
    }

    public boolean isLevelCompleted() {
        return _level.isLevelCompleted();
    }

    public Level getCurrentLevel() {
        return _level;
    }

    public void reset() {
        if (_level != null) {
            _level.reset();
        }
    }

    public void addGameListener(GameListener listener) {
        _moveListeners.add(listener);
    }

    public void removeGameListener(GameListener listener) {
        _moveListeners.remove(listener);
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