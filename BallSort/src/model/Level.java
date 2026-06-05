package model;

import rules.SequenceRule;
import game.GameEventListener;
import game.ListenerPriority;

import java.util.*;

public class Level {

    private final List<Tube> _tubes = new ArrayList<>();
    private Tube _pendingTube = null;
    private final List<GameEventListener> _listeners = new ArrayList<>();

    public Level(List<Tube> tubes, SequenceRule rules) {
        if (tubes == null || tubes.size() < 2) {
            throw new IllegalArgumentException("Level requires at least 2 tubes");
        }

        for (Tube tube : tubes) {
            _tubes.add(new Tube(tube.getCapacity(), new ArrayList<>(tube.getBalls()), rules));
        }
    }

    public void reset() {
        for (Tube tube : _tubes) {
            tube.reset();
        }

        if (_pendingTube != null) {
            Tube oldPending = _pendingTube;
            _pendingTube = null;
            notifyTubeDeselected(oldPending);
        }
    }

    public List<Tube> getTubes() {
        return Collections.unmodifiableList(_tubes);
    }

    public int getTubeCount() {
        return _tubes.size();
    }

    public Tube getTubeAt(int index) {
        if (index < 0 || index >= _tubes.size()) {
            throw new IndexOutOfBoundsException("Invalid tube index: " + index);
        }
        return _tubes.get(index);
    }

    public void selectTube(Tube tube) {
        if (!_tubes.contains(tube)) {
            notifyMoveFailed(tube, tube);
            return;
        }

        if (_pendingTube == null) {
            if (tube.isEmpty()) {
                return;
            }
            _pendingTube = tube;
            notifyTubeSelected(tube);
            return;
        }

        if (_pendingTube == tube) {
            _pendingTube = null;
            notifyTubeDeselected(tube);
            return;
        }

        executeMove(_pendingTube, tube);
    }

    public Tube getPendingTube() {
        return _pendingTube;
    }

    public List<Ball> getSequenceToMove(Tube tube) {
        if (tube == null) {
            throw new IllegalArgumentException("Tube cannot be null");
        }
        if (tube.isEmpty()) {
            return Collections.emptyList();
        }
        return tube.peekSequence();
    }

    public boolean isLevelCompleted() {
        for (Tube tube : _tubes) {
            if (!tube.isEmpty()) {
                if (!tube.isUniformed() || tube.hasSpace()) {
                    return false;
                }
            }
        }
        return true;
    }

    public void addEventListener(GameEventListener listener) {
        if (listener != null && !_listeners.contains(listener)) {
            _listeners.add(listener);
            _listeners.sort((a, b) ->
                    Integer.compare(b.getPriority().getValue(), a.getPriority().getValue())
            );
        }
    }

    public void removeEventListener(GameEventListener listener) {
        _listeners.remove(listener);
    }

    public void clearEventListeners() {
        _listeners.clear();
    }

    private void executeMove(Tube from, Tube to) {
        if (!from.canStackOnTop(to.peekOne())) {
            notifyMoveFailed(from, to);
            _pendingTube = null;
            notifyTubeDeselected(from);
            return;
        }

        int movedCount = from.moveTo(to);

        if (movedCount > 0) {
            Tube oldPending = _pendingTube;
            _pendingTube = null;

            notifyMoveSucceeded(oldPending, to, movedCount);
            notifyTubeDeselected(oldPending);

            if (isLevelCompleted()) {
                notifyGameCompleted();
            }
        } else {
            notifyMoveFailed(from, to);
            _pendingTube = null;
            notifyTubeDeselected(from);
        }
    }

    @FunctionalInterface
    private interface EventDispatcher {
        void dispatch(GameEventListener listener);
    }

    private void notifyListenersByWave(EventDispatcher dispatcher) {
        Set<Integer> priorities = new TreeSet<>((a, b) -> Integer.compare(b, a));
        for (GameEventListener listener : _listeners) {
            priorities.add(listener.getPriority().getValue());
        }

        for (int priority : priorities) {
            for (GameEventListener listener : _listeners) {
                if (listener.getPriority().getValue() == priority) {
                    try {
                        dispatcher.dispatch(listener);
                    } catch (Exception e) {
                        System.err.println("Error in GameEventListener (priority " + priority + "): " + e.getMessage());
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    private void notifyTubeSelected(Tube tube) {
        int liftedCount = tube.peekSequence().size();
        notifyListenersByWave(listener -> listener.onTubeSelected(tube, liftedCount));
    }

    private void notifyTubeDeselected(Tube tube) {
        notifyListenersByWave(listener -> listener.onTubeDeselected(tube));
    }

    private void notifyMoveSucceeded(Tube from, Tube to, int movedCount) {
        notifyListenersByWave(listener -> listener.onMoveSucceeded(from, to, movedCount));
    }

    private void notifyMoveFailed(Tube from, Tube to) {
        notifyListenersByWave(listener -> listener.onMoveFailed(from, to));
    }

    private void notifyGameCompleted() {
        notifyListenersByWave(GameEventListener::onGameCompleted);
    }
}