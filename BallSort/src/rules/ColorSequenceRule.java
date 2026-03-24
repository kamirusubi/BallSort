package rules;

import model.Ball;
import model.ColorProperty;

public class ColorSequenceRule implements SequenceRule {

    @Override
    public boolean canStack(Ball topBall, Ball targetTopBall) {
        if (targetTopBall == null) {
            return true;
        }

        ColorProperty fromColor = topBall.getProperty(ColorProperty.class);
        ColorProperty targetColor = targetTopBall.getProperty(ColorProperty.class);

        if (fromColor == null || targetColor == null) {
            return false;
        }

        return fromColor.equals(targetColor);
    }
}