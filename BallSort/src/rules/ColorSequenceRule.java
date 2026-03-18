package rules;

import model.Ball;
import model.ColorProperty;

public class ColorSequenceRule implements SequenceRule {

    @Override
    public boolean canStack(Ball topBall, Ball bottomBall) {
        if (bottomBall == null) {
            return true;
        }

        ColorProperty topColor = topBall.getProperty(ColorProperty.class);
        ColorProperty bottomColor = bottomBall.getProperty(ColorProperty.class);

        if (topColor == null || bottomColor == null) {
            return false;
        }

        return topColor.equals(bottomColor);
    }
}