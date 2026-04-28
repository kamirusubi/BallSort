import model.BallProperty;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

abstract class BallPropertyTest<T extends BallProperty> {

    protected abstract T createInstance();
    protected abstract T createDifferentInstance();

    @Test
    void testEqualsSameInstance() {
        T instance = createInstance();
        assertEquals(instance, instance);
    }

    @Test
    void testEqualsNull() {
        T instance = createInstance();
        assertNotEquals(null, instance);
    }

    @Test
    void testEqualsDifferentClass() {
        T instance = createInstance();
        assertNotEquals(instance, "string");
    }

    @Test
    void testEqualsIdenticalProperties() {
        T instance1 = createInstance();
        T instance2 = createInstance();
        assertEquals(instance1, instance2);
    }

    @Test
    void testEqualsDifferentProperties() {
        T instance1 = createInstance();
        T instance2 = createDifferentInstance();
        assertNotEquals(instance1, instance2);
    }
}