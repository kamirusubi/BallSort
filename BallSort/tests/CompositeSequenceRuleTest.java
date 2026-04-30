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
    void test01_emptyCompositeRuleAllowsAnyStack() {
        Ball top = new Ball(new ColorProperty(Color.RED));
        Ball bottom = new Ball(new ColorProperty(Color.BLUE));
        assertTrue(rule.canStack(top, bottom));
    }

    @Test
    void test02_compositeRuleWithSingleRule() {
        rule.addRule(new ColorSequenceRule());
        Ball top = new Ball(new ColorProperty(Color.RED));
        Ball bottom = new Ball(new ColorProperty(Color.RED));
        assertTrue(rule.canStack(top, bottom));
    }

    @Test
    void test03_compositeRuleWithSingleRuleFails() {
        rule.addRule(new ColorSequenceRule());
        Ball top = new Ball(new ColorProperty(Color.RED));
        Ball bottom = new Ball(new ColorProperty(Color.BLUE));
        assertFalse(rule.canStack(top, bottom));
    }

    @Test
    void test04_compositeRuleWithMultipleRulesAllPass() {
        rule.addRule(new ColorSequenceRule());
        rule.addRule(new ChargeSequenceRule());

        Ball top = new Ball(new ColorProperty(Color.RED), new ChargeProperty(Charge.POSITIVE));
        Ball bottom = new Ball(new ColorProperty(Color.RED), new ChargeProperty(Charge.NEGATIVE));

        assertTrue(rule.canStack(top, bottom));
    }

    @Test
    void test05_compositeRuleWithMultipleRulesOneFails() {
        rule.addRule(new ColorSequenceRule());
        rule.addRule(new ChargeSequenceRule());

        Ball top = new Ball(new ColorProperty(Color.RED), new ChargeProperty(Charge.POSITIVE));
        Ball bottom = new Ball(new ColorProperty(Color.BLUE), new ChargeProperty(Charge.NEGATIVE));

        assertFalse(rule.canStack(top, bottom));
    }

    @Test
    void test06_constructorWithRules() {
        CompositeSequenceRule ruleWithRules = new CompositeSequenceRule(
                new ColorSequenceRule(),
                new ChargeSequenceRule()
        );

        Ball top = new Ball(new ColorProperty(Color.RED), new ChargeProperty(Charge.POSITIVE));
        Ball bottom = new Ball(new ColorProperty(Color.RED), new ChargeProperty(Charge.NEGATIVE));

        assertTrue(ruleWithRules.canStack(top, bottom));
    }

    @Test
    void test07_clearRules() {
        rule.addRule(new ColorSequenceRule());
        rule.clearRules();

        Ball top = new Ball(new ColorProperty(Color.RED));
        Ball bottom = new Ball(new ColorProperty(Color.BLUE));
        assertTrue(rule.canStack(top, bottom));
    }

    @Test
    void test08_getRulesReturnsUnmodifiableList() {
        rule.addRule(new ColorSequenceRule());
        assertThrows(UnsupportedOperationException.class, () -> rule.getRules().add(new ChargeSequenceRule()));
    }
}