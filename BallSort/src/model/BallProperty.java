package model;

import java.util.Objects;

public abstract class BallProperty {
    @Override
    public abstract String toString();

    @Override
    public final boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        return equalsSpecific((BallProperty) obj);
    }

    protected abstract boolean equalsSpecific(BallProperty other);
}