package model;

import java.awt.Color;
import java.util.Objects;

public class ColorProperty extends BallProperty {

    private final Color _color;

    public ColorProperty(Color color) {
        _color = color;
    }

    public Color getColor() {
        return _color;
    }

    @Override
    public String toString() {
        return _color.toString();
    }

    @Override
    protected boolean equalsSpecific(BallProperty other) {
        ColorProperty otherColor = (ColorProperty) other;
        return Objects.equals(_color, otherColor._color);
    }
}