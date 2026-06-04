import model.ColorProperty;
import model.FragileProperty;
import org.junit.jupiter.api.Test;

import java.awt.*;

import static org.junit.jupiter.api.Assertions.*;

class FragilePropertyTest extends BallPropertyTest<FragileProperty> {

    @Override
    protected FragileProperty createInstance() {
        return new FragileProperty();
    }

    @Override
    protected FragileProperty createDifferentInstance() {
        return new FragileProperty();
    }

    @Test
    void test_toString() {
        FragileProperty property = new FragileProperty();
        assertEquals("Хр", property.toString());
    }

    @Test
    void test_equalsWithNull() {
        FragileProperty property = new FragileProperty();
        assertNotEquals(null, property);
    }

    @Test
    void test_equalsWithDifferentClass() {
        FragileProperty property = new FragileProperty();
        assertNotEquals(property, new ColorProperty(Color.RED));
    }
}