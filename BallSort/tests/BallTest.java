import model.Ball;
import model.BallProperty;
import model.ColorProperty;
import org.junit.jupiter.api.Test;

import java.awt.Color;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class BallTest {

    @Test
    void test01_AddPropertyAndGetProperties() {
        Ball ball = new Ball();

        ColorProperty colorProperty = new ColorProperty(Color.RED);
        ball.addProperty(colorProperty);

        List<BallProperty> properties = ball.getProperties();

        assertNotNull(properties);
        assertEquals(1, properties.size());
        assertTrue(properties.contains(colorProperty));
        assertEquals(colorProperty, ball.getProperty(ColorProperty.class));
    }

    @Test
    void test02_AddMultiplePropertiesAndGetProperties() {
        Ball ball = new Ball();

        ColorProperty redProperty = new ColorProperty(Color.RED);
        ColorProperty blueProperty = new ColorProperty(Color.BLUE);

        ball.addProperty(redProperty);
        ball.addProperty(blueProperty);

        List<BallProperty> properties = ball.getProperties();

        assertNotNull(properties);
        assertEquals(2, properties.size());
        assertTrue(properties.contains(redProperty));
        assertTrue(properties.contains(blueProperty));

        assertEquals(redProperty, ball.getProperty(ColorProperty.class));
    }
}