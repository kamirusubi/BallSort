import model.Charge;
import model.ChargeProperty;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ChargePropertyTest extends BallPropertyTest<ChargeProperty> {

    @Override
    protected ChargeProperty createInstance() {
        return new ChargeProperty(Charge.POSITIVE);
    }

    @Override
    protected ChargeProperty createDifferentInstance() {
        return new ChargeProperty(Charge.NEGATIVE);
    }

    @Test
    void testGetCharge() {
        ChargeProperty property = new ChargeProperty(Charge.NEUTRAL);
        assertEquals(Charge.NEUTRAL, property.getCharge());
    }

    @Test
    void testToString() {
        ChargeProperty positive = new ChargeProperty(Charge.POSITIVE);
        assertEquals("+", positive.toString());

        ChargeProperty negative = new ChargeProperty(Charge.NEGATIVE);
        assertEquals("-", negative.toString());

        ChargeProperty neutral = new ChargeProperty(Charge.NEUTRAL);
        assertEquals("0", neutral.toString());
    }
}