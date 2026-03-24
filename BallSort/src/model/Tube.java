package model;

import java.util.*;

public class Tube {
    private final Stack<Ball> _balls = new Stack<>();
    private final int capacity;

    public Tube(int capacity) {
        this.capacity = capacity;
    }

    public Tube(int capacity, List<Ball> initialBalls) {
        this(capacity);
        if (initialBalls.size() > capacity) {
            throw new IllegalArgumentException("Too many balls for tube capacity");
        }
        fill(initialBalls);
    }

    public Ball peek() {
        if (_balls.isEmpty()) {
            return null;
        }
        return _balls.peek();
    }

    public Ball pop() {
        if (_balls.isEmpty()) {
            return null;
        }
        return _balls.pop();
    }

    public boolean push(Ball ball) {
        if (hasSpace()) {
            _balls.push(ball);
            return true;
        }
        return false;
    }

    public void fill(List<Ball> initialBalls) {
        _balls.clear();
        for (Ball ball : initialBalls) {
            _balls.push(ball);
        }
    }

    public boolean hasSpace() {
        return _balls.size() < capacity;
    }

    public boolean isEmpty() {
        return _balls.isEmpty();
    }

    public boolean isFull() {
        return _balls.size() >= capacity;
    }

    public int getBallCount() {
        return _balls.size();
    }

    public int getCapacity() {
        return capacity;
    }

    public List<Ball> getBalls() {
        return Collections.unmodifiableList(_balls);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("[");
        for (int i = _balls.size() - 1; i >= 0; i--) {
            sb.append(_balls.get(i).toString());
        }
        while (sb.length() - 1 < capacity) {
            sb.append("_");
        }
        sb.append("]");
        return sb.toString();
    }
}