package model;

import game.LevelListener;
import rules.SequenceRule;
import java.util.*;

public class Level implements TubeSelectionListener {

    private final List<Tube> _tubes = new ArrayList<>();
    private final List<Tube> _originalTubes;
    private Tube _selectedTube = null;
    private final List<TubeSelectionListener> _tubeSelectionListeners = new ArrayList<>();
    private final List<LevelListener> _levelListeners = new ArrayList<>();
    private final SequenceRule  _rules;

    public Level(List<Tube> tubes, SequenceRule rules) {
        _originalTubes = tubes;
        _rules = rules;
        for (Tube tube : tubes) {
            _tubes.add(new Tube(tube.getCapacity(), new ArrayList<>(tube.getBalls()), _rules));
        }

        for (Tube tube : _tubes) {
            tube.addSelectionListener(this);
        }
    }

    public void reset() {
        _tubes.clear();
        for (Tube tube : _originalTubes) {
            _tubes.add(new Tube(tube.getCapacity(), new ArrayList<>(tube.getBalls()), _rules));
        }

        for (Tube newTube : _tubes) {
            newTube.addSelectionListener(this);
        }

        if (_selectedTube != null) {
            _selectedTube.setSelected(false);
            _selectedTube = null;
        }
    }

    public List<Tube> getTubes() {
        return Collections.unmodifiableList(_tubes);
    }

    public boolean executeMove(Tube from, Tube to) {
        from.setSelected(false);
        to.setSelected(false);

        if (!_tubes.contains(from) || !_tubes.contains(to)) {
            notifyMoveAttempt(false, from, to);
            return false;
        }

        int movedCount = from.moveTo(to);
        boolean success = movedCount > 0;

        notifyMoveAttempt(success, from, to);

        if (success && isLevelCompleted()) {
            notifyLevelCompleted();
        }

        return success;
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

    public SequenceRule getRules() {
        return _rules;
    }

    public void addLevelListener(LevelListener listener) {
        _levelListeners.add(listener);
    }

    public void removeLevelListener(LevelListener listener) {
        _levelListeners.remove(listener);
    }

    private void notifyMoveAttempt(boolean success, Tube from, Tube to) {
        for (LevelListener listener : _levelListeners) {
            listener.onMoveAttempt(success, from, to);
        }
    }

    private void notifyLevelCompleted() {
        for (LevelListener listener : _levelListeners) {
            listener.onGameCompleted();
        }
    }

    public void addTubeSelectionListener(TubeSelectionListener listener) {
        _tubeSelectionListeners.add(listener);
    }

    public void removeTubeSelectionListener(TubeSelectionListener listener) {
        _tubeSelectionListeners.remove(listener);
    }

    private void handleTubeSelection(Tube tube) {
        if (_selectedTube == null) { // Если выбирается первая труба
            if (!tube.isEmpty()) {
                _selectedTube = tube;
                notifyFirstTubeSelected(tube);
            } else { // нельзя выбрать пустую
                tube.setSelected(false);
            }
        } else { // выбирается вторая труба
            if (_selectedTube == tube) { // если выбрана первая, то снимаем выделение
                _selectedTube.setSelected(false);
                _selectedTube = null;
            } else { // если отличается от первой
                executeMove(_selectedTube, tube);
            }
        }
    }

    private void notifyFirstTubeSelected(Tube tube) {
        for (TubeSelectionListener listener : _tubeSelectionListeners) {
            listener.onFirstTubeSelected(tube);
        }
    }

    private void notifyFirstTubeDeselected(Tube tube) {
        for (TubeSelectionListener listener : _tubeSelectionListeners) {
            listener.onFirstTubeDeselected(tube);
        }
    }

    @Override
    public void onFirstTubeSelected(Tube selectedTube) {
        handleTubeSelection(selectedTube);
    }

    @Override
    public void onFirstTubeDeselected(Tube tube) {
        _selectedTube = null;
        notifyFirstTubeDeselected(tube);
    }
}