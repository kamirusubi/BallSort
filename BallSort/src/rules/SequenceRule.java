package rules;

import model.Ball;

public interface SequenceRule {
    boolean canStack(Ball topBall, Ball targetTopBall);
}