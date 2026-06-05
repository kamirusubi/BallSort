package model;

import rules.SequenceRule;
import java.util.*;

public class Tube {
    private final List<Ball> _balls = new ArrayList<>();
    private final int _capacity;
    private final SequenceRule _rules;
    private final List<Ball> _originalBalls = new ArrayList<>();

    public Tube(int capacity, SequenceRule rules) {
        _capacity = capacity;
        _rules = rules;
    }

    public Tube(int capacity, List<Ball> initialBalls, SequenceRule rules) {
        this(capacity, rules);
        if (initialBalls.size() > capacity) {
            throw new IllegalArgumentException("Too many balls for tube capacity");
        }
        _originalBalls.addAll(initialBalls);
        fill(initialBalls);
    }

    public int moveTo(Tube target) {
        if (!canMoveTo(target)) return 0;

        List<Ball> ballsToMove = peekSequence();
        int spaceAvailable = target.getCapacity() - target.getBallCount();
        int ballsToMoveCount = Math.min(ballsToMove.size(), spaceAvailable);

        for (int i = 0; i < ballsToMoveCount; i++) {
            Ball ball = popOne();
            target.pushOne(ball);
        }
        return ballsToMoveCount;
    }

    public Ball peekOne() {
        if (_balls.isEmpty()) return null;
        return _balls.get(getBallCount() - 1);
    }

    public List<Ball> peekSequence() {
        List<Ball> result = new ArrayList<>();
        if (_balls.isEmpty()) return result;

        Ball currentBall = _balls.get(getBallCount() - 1);
        boolean canStack = true;
        result.add(currentBall);

        for (int i = _balls.size() - 2; i >= 0 && canStack; i--) {
            Ball nextBall = _balls.get(i);
            canStack = _rules.canStack(nextBall, currentBall);
            if (canStack) {
                currentBall = nextBall;
                result.add(currentBall);
            }
        }
        return result;
    }

    public Ball popOne() {
        if (_balls.isEmpty()) return null;
        return _balls.remove(getBallCount() - 1);
    }

    public void pushOne(Ball ball) {
        if (hasSpace()) {
            _balls.add(ball);
        }
    }

    public boolean pushSequence(List<Ball> balls){
        if(!hasSpace()) return false;

        for (Ball ball : balls) pushOne(ball);

        return true;
    }

    public boolean hasSpace() {
        return _balls.size() < _capacity;
    }

    public boolean isEmpty() {
        return _balls.isEmpty();
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

    public SequenceRule getRules() {
        return _rules;
    }

    public boolean isUniformed() {
        List<Ball> balls = getBalls();
        for (int i = 0; i < balls.size() - 1; i++) {
            if (!_rules.canStack(balls.get(i + 1), balls.get(i))) return false;
        }
        return true;
    }

    public void reset() {
        _balls.clear();
        _balls.addAll(_originalBalls);
    }

    private boolean canMoveTo(Tube target) {
        if (isEmpty()) return false;
        if (!target.hasSpace()) return false;
        return _rules.canStack(peekOne(), target.peekOne());
    }

    private void fill(List<Ball> initialBalls) {
        _balls.clear();
        _balls.addAll(initialBalls);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("[");
        for (int i = _balls.size() - 1; i >= 0; i--) {
            sb.append(_balls.get(i).toString());
            if (i > 0) sb.append("_");
        }
        sb.append("]");
        return sb.toString();
    }
}