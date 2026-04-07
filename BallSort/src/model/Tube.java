package model;

import rules.SequenceRule;

import java.util.*;

public class Tube {
    private final List<Ball> _balls = new ArrayList<>();
    private final int _capacity;
    private boolean _isSelected = false;
    private List<TubeSelectionListener> _listeners = new ArrayList<>();

    public Tube(int capacity) {
        _capacity = capacity;
    }

    public Tube(int capacity, List<Ball> initialBalls) {
        this(capacity);
        if (initialBalls.size() > capacity) {
            throw new IllegalArgumentException("Too many balls for tube capacity");
        }
        fill(initialBalls);
    }

    public void fill(List<Ball> initialBalls) {
        _balls.clear();
        _balls.addAll(initialBalls);
    }

    public Ball peekOne(){
        if (_balls.isEmpty()) {
            return null;
        }
        return _balls.get(getBallCount()-1);
    }

    public List<Ball> peekSequence(SequenceRule rules) {
        List<Ball> result = new ArrayList<>();

        if (_balls.isEmpty()) {
            return result;
        }

        Ball currentBall = _balls.get(getBallCount()-1);
        boolean canStack = true;

        result.add(currentBall);

        for (int i = _balls.size() - 2; i >= 0 && canStack; i--) {
            Ball nextBall = _balls.get(i);
            canStack = rules.canStack(nextBall, currentBall);

            if(canStack){
                currentBall = nextBall;
                result.add(currentBall);
            }
        }


        return result;
    }

    public Ball popOne() {
        if (_balls.isEmpty()) {
            return null;
        }
        return _balls.remove(getBallCount()-1);
    }

    public List<Ball> popSequence(SequenceRule rules) {
        List<Ball> result = new ArrayList<>();

        if (_balls.isEmpty()) {
            return result;
        }

        Ball currentBall = popOne();
        result.add(currentBall);

        while (!_balls.isEmpty() && rules.canStack(_balls.get(getBallCount()-1), currentBall)) {
            currentBall = popOne();
            result.add(currentBall);
        }

        return result;
    }

    public void pushOne(Ball ball) {
        if (hasSpace()) {
            _balls.add(ball);
        }
    }

    public boolean pushSequence(List<Ball> balls){
        if(!hasSpace()) return false;

        for (Ball ball : balls) {
            pushOne(ball);
        }

        return true;
    }

    public boolean hasSpace() {
        return _balls.size() < _capacity;
    }

    public boolean isEmpty() {
        return _balls.isEmpty();
    }

    public boolean isFull() {
        return _balls.size() >= _capacity;
    }

    public int getBallCount() {
        return _balls.size();
    }

    public int getCapacity() {
        return _capacity;
    }

    public List<Ball> getBalls() {
        return Collections.unmodifiableList(_balls);
    }

    public boolean isSelected() {
        return _isSelected;
    }

    public void setSelected(boolean selected) {
        _isSelected = selected;
        notifySelectionChanged();
    }

    public void addSelectionListener(TubeSelectionListener listener) {
        _listeners.add(listener);
    }

    public void removeSelectionListener(TubeSelectionListener listener) {
        _listeners.remove(listener);
    }

    private void notifySelectionChanged() {
        for (TubeSelectionListener listener : _listeners) {
            if (_isSelected) {
                listener.onTubeSelected(this);
            } else {
                listener.onTubeDeselected(this);
            }
        }
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("[");
        for (int i = _balls.size() - 1; i >= 0; i--) {
            sb.append(_balls.get(i).toString());
            if(i > 0) sb.append("_");
        }

        sb.append("]");
        return sb.toString();
    }
}