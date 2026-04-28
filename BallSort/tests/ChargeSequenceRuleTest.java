import model.Ball;
import model.Charge;
import model.ChargeProperty;
import org.junit.jupiter.api.Test;
import rules.ChargeSequenceRule;

import static org.junit.jupiter.api.Assertions.*;

class ChargeSequenceRuleTest {

    private final ChargeSequenceRule rule = new ChargeSequenceRule();

    @Test
    void testCanStackWithNullBottom() {
        Ball top = new Ball(new ChargeProperty(Charge.POSITIVE));
        assertTrue(rule.canStack(top, null));
    }

    @Test
    void testCanStackPositiveOnNegative() {
        Ball top = new Ball(new ChargeProperty(Charge.POSITIVE));
        Ball bottom = new Ball(new ChargeProperty(Charge.NEGATIVE));
        assertTrue(rule.canStack(top, bottom));
    }

    @Test
    void testCanStackNegativeOnPositive() {
        Ball top = new Ball(new ChargeProperty(Charge.NEGATIVE));
        Ball bottom = new Ball(new ChargeProperty(Charge.POSITIVE));
        assertTrue(rule.canStack(top, bottom));
    }

    @Test
    void testCannotStackPositiveOnPositive() {
        Ball top = new Ball(new ChargeProperty(Charge.POSITIVE));
        Ball bottom = new Ball(new ChargeProperty(Charge.POSITIVE));
        assertFalse(rule.canStack(top, bottom));
    }

    @Test
    void testCannotStackNegativeOnNegative() {
        Ball top = new Ball(new ChargeProperty(Charge.NEGATIVE));
        Ball bottom = new Ball(new ChargeProperty(Charge.NEGATIVE));
        assertFalse(rule.canStack(top, bottom));
    }

    @Test
    void testCanStackNeutralOnAnything() {
        Ball top = new Ball(new ChargeProperty(Charge.NEUTRAL));
        Ball bottomPositive = new Ball(new ChargeProperty(Charge.POSITIVE));
        Ball bottomNegative = new Ball(new ChargeProperty(Charge.NEGATIVE));
        Ball bottomNeutral = new Ball(new ChargeProperty(Charge.NEUTRAL));

        assertTrue(rule.canStack(top, bottomPositive));
        assertTrue(rule.canStack(top, bottomNegative));
        assertTrue(rule.canStack(top, bottomNeutral));
    }

    @Test
    void testCanStackAnythingOnNeutral() {
        Ball bottom = new Ball(new ChargeProperty(Charge.NEUTRAL));
        Ball topPositive = new Ball(new ChargeProperty(Charge.POSITIVE));
        Ball topNegative = new Ball(new ChargeProperty(Charge.NEGATIVE));
        Ball topNeutral = new Ball(new ChargeProperty(Charge.NEUTRAL));

        assertTrue(rule.canStack(topPositive, bottom));
        assertTrue(rule.canStack(topNegative, bottom));
        assertTrue(rule.canStack(topNeutral, bottom));
    }

    @Test
    void testCanStackWithMissingChargeProperty() {
        Ball top = new Ball();
        Ball bottom = new Ball(new ChargeProperty(Charge.POSITIVE));
        assertTrue(rule.canStack(top, bottom));
    }
}