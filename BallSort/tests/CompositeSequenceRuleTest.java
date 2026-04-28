import model.Ball;
import model.Charge;
import model.ChargeProperty;
import model.ColorProperty;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import rules.ColorSequenceRule;
import rules.ChargeSequenceRule;
import rules.CompositeSequenceRule;

import java.awt.Color;

import static org.junit.jupiter.api.Assertions.*;

class CompositeSequenceRuleTest {

    private CompositeSequenceRule rule = new CompositeSequenceRule();

    @Test
    void testEmptyCompositeRuleAllowsAnyStack() {
        Ball top = new Ball(new ColorProperty(Color.RED));
        Ball bottom = new Ball(new ColorProperty(Color.BLUE));
        assertTrue(rule.canStack(top, bottom));
    }

    @Test
    void testCompositeRuleWithSingleRule() {
        rule.addRule(new ColorSequenceRule());
        Ball top = new Ball(new ColorProperty(Color.RED));
        Ball bottom = new Ball(new ColorProperty(Color.RED));
        assertTrue(rule.canStack(top, bottom));
    }

    @Test
    void testCompositeRuleWithSingleRuleFails() {
        rule.addRule(new ColorSequenceRule());
        Ball top = new Ball(new ColorProperty(Color.RED));
        Ball bottom = new Ball(new ColorProperty(Color.BLUE));
        assertFalse(rule.canStack(top, bottom));
    }

    @Test
    void testCompositeRuleWithMultipleRulesAllPass() {
        rule.addRule(new ColorSequenceRule());
        rule.addRule(new ChargeSequenceRule());

        Ball top = new Ball(new ColorProperty(Color.RED), new ChargeProperty(Charge.POSITIVE));
        Ball bottom = new Ball(new ColorProperty(Color.RED), new ChargeProperty(Charge.NEGATIVE));

        assertTrue(rule.canStack(top, bottom));
    }

    @Test
    void testCompositeRuleWithMultipleRulesOneFails() {
        rule.addRule(new ColorSequenceRule());
        rule.addRule(new ChargeSequenceRule());

        Ball top = new Ball(new ColorProperty(Color.RED), new ChargeProperty(Charge.POSITIVE));
        Ball bottom = new Ball(new ColorProperty(Color.BLUE), new ChargeProperty(Charge.NEGATIVE));

        assertFalse(rule.canStack(top, bottom));
    }

    @Test
    void testConstructorWithRules() {
        CompositeSequenceRule ruleWithRules = new CompositeSequenceRule(
                new ColorSequenceRule(),
                new ChargeSequenceRule()
        );

        Ball top = new Ball(new ColorProperty(Color.RED), new ChargeProperty(Charge.POSITIVE));
        Ball bottom = new Ball(new ColorProperty(Color.RED), new ChargeProperty(Charge.NEGATIVE));

        assertTrue(ruleWithRules.canStack(top, bottom));
    }

    @Test
    void testClearRules() {
        rule.addRule(new ColorSequenceRule());
        rule.clearRules();

        Ball top = new Ball(new ColorProperty(Color.RED));
        Ball bottom = new Ball(new ColorProperty(Color.BLUE));
        assertTrue(rule.canStack(top, bottom));
    }

    @Test
    void testGetRulesReturnsUnmodifiableList() {
        rule.addRule(new ColorSequenceRule());
        assertThrows(UnsupportedOperationException.class, () -> rule.getRules().add(new ChargeSequenceRule()));
    }
}