package model;

public class ChargeProperty extends BallProperty {

    private final Charge _charge;

    public ChargeProperty(Charge charge) {
        _charge = charge;
    }

    public Charge getCharge() {
        return _charge;
    }

    @Override
    public String toString() {
        return _charge.toString();
    }

    @Override
    protected boolean equalsSpecific(BallProperty other) {
        ChargeProperty that = (ChargeProperty) other;
        return this._charge == that._charge;
    }
}