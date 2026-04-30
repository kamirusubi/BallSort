import model.BallProperty;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

abstract class BallPropertyTest<T extends BallProperty> {

    protected abstract T createInstance();
    protected abstract T createDifferentInstance();

    @Test
    void test01_equalsSameInstance() {
        T instance = createInstance();
        assertEquals(instance, instance);
    }

    @Test
    void test02_equalsNull() {
        T instance = createInstance();
        assertNotEquals(null, instance);
    }

    @Test
    void test03_equalsDifferentClass() {
        T instance = createInstance();
        assertNotEquals(instance, "string");
    }

    @Test
    void test04_equalsIdenticalProperties() {
        T instance1 = createInstance();
        T instance2 = createInstance();
        assertEquals(instance1, instance2);
    }

    @Test
    void test05_equalsDifferentProperties() {
        T instance1 = createInstance();
        T instance2 = createDifferentInstance();
        assertNotEquals(instance1, instance2);
    }
}