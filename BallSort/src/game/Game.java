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

        boolean success = _level.executeMove(from, to);

        notifyMoveAttempt(success, from, to);

        if (success && isLevelCompleted()) {
            notifyGameCompleted();
        }

        return success;
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