package model;

import rules.SequenceRule;
import java.util.*;

public class Level implements TubeSelectionListener {

    private final List<Tube> _tubes = new ArrayList<>();
    private final List<Tube> _originalTubes;
    private Tube _selectedTube = null;
    private final List<TubeSelectionListener> _levelListeners = new ArrayList<>();

    public Level(List<Tube> tubes) {
        _originalTubes = tubes;

        for (Tube tube : tubes) {
            _tubes.add(new Tube(tube.getCapacity(), new ArrayList<>(tube.getBalls())));
        }

        for (Tube tube : _tubes) {
            tube.addSelectionListener(this);
        }
    }

    public void reset() {
        _tubes.clear();
        for (Tube tube : _originalTubes) {
            _tubes.add(new Tube(tube.getCapacity(), new ArrayList<>(tube.getBalls())));
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

    public boolean executeMove(Tube from, Tube to, SequenceRule rules) {
        if (from.isEmpty() || !to.hasSpace()) {
            return false;
        }

        List<Ball> ballsToMove = from.peekSequence(rules);

        for (Ball ball : ballsToMove) {
            if(to.hasSpace()){
                to.pushOne(from.popOne());
            }
        }

        return true;
    }

    public boolean isLevelCompleted(SequenceRule rules) {
        for (Tube tube : _tubes) {
            if (!tube.isEmpty()) {
                if (!isTubeUniform(tube, rules) || !tube.isFull()) {
                    return false;
                }
            }
        }
        return true;
    }

    public void addLevelListener(TubeSelectionListener listener) {
        _levelListeners.add(listener);
    }

    public void removeLevelListener(TubeSelectionListener listener) {
        _levelListeners.remove(listener);
    }

    private boolean isTubeUniform(Tube tube, SequenceRule rules) {
        if (tube.isEmpty() || tube.getBallCount() == 1) {
            return true;
        }

        List<Ball> balls = tube.getBalls();

        for (int i = 0; i < balls.size() - 1; i++) {
            Ball currentBall = balls.get(i);
            Ball nextBall = balls.get(i + 1);

            if (!rules.canStack(nextBall, currentBall)) {
                return false;
            }
        }

        return true;
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
                notifyMoveAttempt(_selectedTube, tube);
            }
        }
    }

    private void notifyMoveAttempt(Tube from, Tube to) {
        for (TubeSelectionListener listener : _levelListeners) {
            listener.onTwoTubesSelected(from, to);
        }
    }

    private void notifyFirstTubeSelected(Tube tube) {
        for (TubeSelectionListener listener : _levelListeners) {
            listener.onFirstTubeSelected(tube);
        }
    }

    private void notifyFirstTubeDeselected(Tube tube) {
        for (TubeSelectionListener listener : _levelListeners) {
            listener.onFirstTubeDeselected(tube);
        }
    }

    @Override
    public void onFirstTubeSelected(Tube tube) {
        handleTubeSelection(tube);
    }

    @Override
    public void onFirstTubeDeselected(Tube tube) {
        if (_selectedTube == tube) {
            _selectedTube = null;
        }
        notifyFirstTubeDeselected(tube);
    }

    @Override
    public void onTwoTubesSelected(Tube from, Tube to) {
        // Не используется в Level
    }
}