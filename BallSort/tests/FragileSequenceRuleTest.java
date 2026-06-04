import model.Ball;
import model.FragileProperty;
import model.ColorProperty;
import org.junit.jupiter.api.Test;
import rules.FragileSequenceRule;

import java.awt.Color;

import static org.junit.jupiter.api.Assertions.*;

class FragileSequenceRuleTest {

    private final FragileSequenceRule rule = new FragileSequenceRule();

    @Test
    void test01_canStackWithNullBottom() {
        Ball top = new Ball(new FragileProperty());
        assertTrue(rule.canStack(top, null));
    }

    @Test
    void test02_canStackOnNonFragileBottom() {
        Ball top = new Ball(new ColorProperty(Color.RED));
        Ball bottom = new Ball(new ColorProperty(Color.RED));

        assertTrue(rule.canStack(top, bottom));
    }

    @Test
    void test03_cannotStackOnFragileBottom() {
        Ball top = new Ball(new ColorProperty(Color.RED));
        Ball bottom = new Ball(new FragileProperty());

        assertFalse(rule.canStack(top, bottom));
    }

    @Test
    void test04_cannotStackOnFragileBottomWithMultipleProperties() {
        Ball top = new Ball(new ColorProperty(Color.RED));
        Ball bottom = new Ball(new FragileProperty(), new ColorProperty(Color.BLUE));

        assertFalse(rule.canStack(top, bottom));
    }

    @Test
    void test05_canStackOnNonFragileBottomWithOtherProperties() {
        Ball top = new Ball(new ColorProperty(Color.RED));
        Ball bottom = new Ball(new ColorProperty(Color.RED));

        assertTrue(rule.canStack(top, bottom));
    }

    @Test
    void test06_canStackFragileOnNonFragile() {
        Ball top = new Ball(new FragileProperty());
        Ball bottom = new Ball(new ColorProperty(Color.RED));

        assertTrue(rule.canStack(top, bottom));
    }

    @Test
    void test07_cannotStackFragileOnFragile() {
        Ball top = new Ball(new FragileProperty());
        Ball bottom = new Ball(new FragileProperty());

        assertFalse(rule.canStack(top, bottom));
    }
}