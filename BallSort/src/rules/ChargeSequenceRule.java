package rules;

import model.Ball;
import model.ChargeProperty;
import model.Charge;

public class ChargeSequenceRule implements SequenceRule {

    @Override
    public boolean canStack(Ball topBall, Ball bottomBall) {
        if (bottomBall == null) {
            return true;
        }

        ChargeProperty topCharge = topBall.getProperty(ChargeProperty.class);
        ChargeProperty bottomCharge = bottomBall.getProperty(ChargeProperty.class);

        Charge top = (topCharge != null) ? topCharge.getCharge() : Charge.NEUTRAL;
        Charge bottom = (bottomCharge != null) ? bottomCharge.getCharge() : Charge.NEUTRAL;

        if (top == Charge.POSITIVE && bottom == Charge.POSITIVE) {
            return false;
        }

        if (top == Charge.NEGATIVE && bottom == Charge.NEGATIVE) {
            return false;
        }
        return true;
    }
}