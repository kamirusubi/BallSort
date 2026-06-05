package rules;

import model.Ball;
import model.FragileProperty;

public class FragileSequenceRule implements SequenceRule {
    @Override
    public boolean canStack(Ball topBall, Ball bottomBall) {
        if (bottomBall == null) {
            return true;
        }

        FragileProperty bottomFragile = bottomBall.getProperty(FragileProperty.class);
        return bottomFragile == null;
    }
}