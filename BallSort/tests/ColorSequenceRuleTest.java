import model.Ball;
import model.ColorProperty;
import org.junit.jupiter.api.Test;
import rules.ColorSequenceRule;

import java.awt.Color;

import static org.junit.jupiter.api.Assertions.*;

class ColorSequenceRuleTest {

    private final ColorSequenceRule rule = new ColorSequenceRule();

    @Test
    void testCanStackWithNullBottom() {
        Ball top = new Ball(new ColorProperty(Color.RED));
        assertTrue(rule.canStack(top, null));
    }

    @Test
    void testCanStackSameColor() {
        Ball top = new Ball(new ColorProperty(Color.RED));
        Ball bottom = new Ball(new ColorProperty(Color.RED));
        assertTrue(rule.canStack(top, bottom));
    }

    @Test
    void testCanStackDifferentColors() {
        Ball top = new Ball(new ColorProperty(Color.RED));
        Ball bottom = new Ball(new ColorProperty(Color.BLUE));
        assertFalse(rule.canStack(top, bottom));
    }

    @Test
    void testCanStackTopMissingColorProperty() {
        Ball top = new Ball();
        Ball bottom = new Ball(new ColorProperty(Color.RED));
        assertFalse(rule.canStack(top, bottom));
    }

    @Test
    void testCanStackBottomMissingColorProperty() {
        Ball top = new Ball(new ColorProperty(Color.RED));
        Ball bottom = new Ball();
        assertFalse(rule.canStack(top, bottom));
    }

    @Test
    void testCanStackBothMissingColorProperty() {
        Ball top = new Ball();
        Ball bottom = new Ball();
        assertFalse(rule.canStack(top, bottom));
    }
}