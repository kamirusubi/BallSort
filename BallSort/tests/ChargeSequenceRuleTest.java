import model.Ball;
import model.Charge;
import model.ChargeProperty;
import org.junit.jupiter.api.Test;
import rules.ChargeSequenceRule;

import static org.junit.jupiter.api.Assertions.*;

class ChargeSequenceRuleTest {

    private final ChargeSequenceRule rule = new ChargeSequenceRule();

    @Test
    void test01_canStackWithNullBottom() {
        Ball top = new Ball(new ChargeProperty(Charge.POSITIVE));
        assertTrue(rule.canStack(top, null));
    }

    @Test
    void test02_canStackPositiveOnNegative() {
        Ball top = new Ball(new ChargeProperty(Charge.POSITIVE));
        Ball bottom = new Ball(new ChargeProperty(Charge.NEGATIVE));
        assertTrue(rule.canStack(top, bottom));
    }

    @Test
    void test03_canStackNegativeOnPositive() {
        Ball top = new Ball(new ChargeProperty(Charge.NEGATIVE));
        Ball bottom = new Ball(new ChargeProperty(Charge.POSITIVE));
        assertTrue(rule.canStack(top, bottom));
    }

    @Test
    void test04_cannotStackPositiveOnPositive() {
        Ball top = new Ball(new ChargeProperty(Charge.POSITIVE));
        Ball bottom = new Ball(new ChargeProperty(Charge.POSITIVE));
        assertFalse(rule.canStack(top, bottom));
    }

    @Test
    void test05_cannotStackNegativeOnNegative() {
        Ball top = new Ball(new ChargeProperty(Charge.NEGATIVE));
        Ball bottom = new Ball(new ChargeProperty(Charge.NEGATIVE));
        assertFalse(rule.canStack(top, bottom));
    }

    @Test
    void test06_canStackNeutralOnAnything() {
        Ball top = new Ball(new ChargeProperty(Charge.NEUTRAL));
        Ball bottomPositive = new Ball(new ChargeProperty(Charge.POSITIVE));
        Ball bottomNegative = new Ball(new ChargeProperty(Charge.NEGATIVE));
        Ball bottomNeutral = new Ball(new ChargeProperty(Charge.NEUTRAL));

        assertTrue(rule.canStack(top, bottomPositive));
        assertTrue(rule.canStack(top, bottomNegative));
        assertTrue(rule.canStack(top, bottomNeutral));
    }

    @Test
    void test07_canStackAnythingOnNeutral() {
        Ball bottom = new Ball(new ChargeProperty(Charge.NEUTRAL));
        Ball topPositive = new Ball(new ChargeProperty(Charge.POSITIVE));
        Ball topNegative = new Ball(new ChargeProperty(Charge.NEGATIVE));
        Ball topNeutral = new Ball(new ChargeProperty(Charge.NEUTRAL));

        assertTrue(rule.canStack(topPositive, bottom));
        assertTrue(rule.canStack(topNegative, bottom));
        assertTrue(rule.canStack(topNeutral, bottom));
    }

    @Test
    void test08_canStackWithMissingChargeProperty() {
        Ball top = new Ball();
        Ball bottom = new Ball(new ChargeProperty(Charge.POSITIVE));
        assertTrue(rule.canStack(top, bottom));
    }
}