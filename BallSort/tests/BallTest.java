import model.Ball;
import model.BallProperty;
import model.ColorProperty;
import model.ChargeProperty;
import model.Charge;
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
        ChargeProperty chargeProperty = new ChargeProperty(Charge.POSITIVE);

        ball.addProperty(redProperty);
        ball.addProperty(chargeProperty);

        List<BallProperty> properties = ball.getProperties();

        assertNotNull(properties);
        assertEquals(2, properties.size());
        assertTrue(properties.contains(redProperty));
        assertTrue(properties.contains(chargeProperty));

        assertEquals(redProperty, ball.getProperty(ColorProperty.class));
        assertEquals(chargeProperty, ball.getProperty(ChargeProperty.class));
    }

    @Test
    void test03_CannotAddSamePropertyTypeTwice() {
        Ball ball = new Ball();

        ColorProperty redProperty1 = new ColorProperty(Color.RED);
        ColorProperty redProperty2 = new ColorProperty(Color.RED);

        ball.addProperty(redProperty1);

        IllegalStateException exception = assertThrows(IllegalStateException.class, () -> {
            ball.addProperty(redProperty2);
        });

        assertEquals("Property of type ColorProperty already exists", exception.getMessage());
        assertEquals(1, ball.getProperties().size());
    }

    @Test
    void test04_CannotAddSamePropertyTypeViaConstructor() {
        ColorProperty redProperty = new ColorProperty(Color.RED);
        ColorProperty blueProperty = new ColorProperty(Color.BLUE);

        IllegalStateException exception = assertThrows(IllegalStateException.class, () -> {
            new Ball(redProperty, blueProperty);
        });

        assertEquals("Property of type ColorProperty already exists", exception.getMessage());
    }

    @Test
    void test05_AddNullPropertyThrowsException() {
        Ball ball = new Ball();

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            ball.addProperty(null);
        });

        assertEquals("Property cannot be null", exception.getMessage());
    }

    @Test
    void test06_CanAddDifferentPropertyTypes() {
        Ball ball = new Ball();

        ColorProperty colorProperty = new ColorProperty(Color.RED);
        ChargeProperty chargeProperty = new ChargeProperty(Charge.POSITIVE);

        ball.addProperty(colorProperty);
        ball.addProperty(chargeProperty);

        assertEquals(2, ball.getProperties().size());
        assertEquals(colorProperty, ball.getProperty(ColorProperty.class));
        assertEquals(chargeProperty, ball.getProperty(ChargeProperty.class));
    }

    @Test
    void test07_GetPropertyReturnsNullForMissingType() {
        Ball ball = new Ball();

        ball.addProperty(new ColorProperty(Color.RED));

        assertNull(ball.getProperty(ChargeProperty.class));
    }
}