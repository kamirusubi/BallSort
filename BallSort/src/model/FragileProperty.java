package model;

public class FragileProperty extends BallProperty {

    @Override
    public String toString() {
        return "Хр";
    }

    @Override
    protected boolean equalsSpecific(BallProperty other) {
        return true;
    }
}