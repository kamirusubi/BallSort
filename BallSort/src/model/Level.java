package model;

import rules.SequenceRule;
import java.util.*;

public class Level {

    private final List<Tube> _tubes = new ArrayList<>();
    private final Map<Tube, List<Ball>> _initialLevelState = new HashMap<>();

    public Level(List<Tube> tubes) {
        for (Tube tube : tubes) {
            Tube tubeCopy = new Tube(tube.getCapacity());

            for (Ball ball : tube.getBalls()) {
                tubeCopy.push(ball);
            }

            _tubes.add(tubeCopy);
            _initialLevelState.put(tubeCopy, tube.getBalls());
        }
    }

    public List<Tube> getTubes() {
        return Collections.unmodifiableList(_tubes);
    }


    public void reset() {
        for (Tube tube : _tubes) {
            List<Ball> initialBalls = _initialLevelState.get(tube);
            tube.fill(initialBalls != null ? new ArrayList<>(initialBalls) : new ArrayList<>());
        }
    }

    public boolean executeMove(Tube from, Tube to) {
        if (from.isEmpty() || !to.hasSpace()) {
            return false;
        }

        Ball movedBall = from.pop();

        return to.push(movedBall);
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

}