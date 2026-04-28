import model.ColorProperty;
import org.junit.jupiter.api.Test;

import java.awt.Color;

import static org.junit.jupiter.api.Assertions.*;

class ColorPropertyTest extends BallPropertyTest<ColorProperty> {

    @Override
    protected ColorProperty createInstance() {
        return new ColorProperty(Color.RED);
    }

    @Override
    protected ColorProperty createDifferentInstance() {
        return new ColorProperty(Color.BLUE);
    }

    @Test
    void testGetColor() {
        ColorProperty property = new ColorProperty(Color.GREEN);
        assertEquals(Color.GREEN, property.getColor());
    }

    @Test
    void testToString() {
        ColorProperty property = new ColorProperty(Color.RED);
        assertNotNull(property.toString());
    }
}