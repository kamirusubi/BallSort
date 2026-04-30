import model.Ball;
import model.ColorProperty;
import org.junit.jupiter.api.Test;
import rules.ColorSequenceRule;

import java.awt.Color;

import static org.junit.jupiter.api.Assertions.*;

class ColorSequenceRuleTest {

    private final ColorSequenceRule rule = new ColorSequenceRule();

    @Test
    void test01_canStackWithNullBottom() {
        Ball top = new Ball(new ColorProperty(Color.RED));
        assertTrue(rule.canStack(top, null));
    }

    @Test
    void test02_canStackSameColor() {
        Ball top = new Ball(new ColorProperty(Color.RED));
        Ball bottom = new Ball(new ColorProperty(Color.RED));
        assertTrue(rule.canStack(top, bottom));
    }

    @Test
    void test03_canStackDifferentColors() {
        Ball top = new Ball(new ColorProperty(Color.RED));
        Ball bottom = new Ball(new ColorProperty(Color.BLUE));
        assertFalse(rule.canStack(top, bottom));
    }

    @Test
    void test04_canStackTopMissingColorProperty() {
        Ball top = new Ball();
        Ball bottom = new Ball(new ColorProperty(Color.RED));
        assertFalse(rule.canStack(top, bottom));
    }

    @Test
    void test05_canStackBottomMissingColorProperty() {
        Ball top = new Ball(new ColorProperty(Color.RED));
        Ball bottom = new Ball();
        assertFalse(rule.canStack(top, bottom));
    }

    @Test
    void test06_canStackBothMissingColorProperty() {
        Ball top = new Ball();
        Ball bottom = new Ball();
        assertFalse(rule.canStack(top, bottom));
    }
}