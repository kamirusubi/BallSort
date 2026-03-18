package model;

import java.util.Objects;

public class ChargeProperty implements BallProperty {
    private final Charge _charge;

    public ChargeProperty(Charge charge) {
        _charge = charge;
    }

    public Charge getCharge() {
        return _charge;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        ChargeProperty other = (ChargeProperty) obj;
        return _charge == other._charge;
    }

    @Override
    public String toString() {
        return _charge.toString();
    }
}