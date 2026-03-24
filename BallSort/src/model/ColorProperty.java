package model;

import java.util.Objects;

public class ColorProperty implements BallProperty {
    private final String _color;

    public ColorProperty(String color) {
        _color = color;
    }

    public String getColor() {
        return _color;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;

        if (obj == null || getClass() != obj.getClass()) return false;
        
        ColorProperty that = (ColorProperty) obj;
        return Objects.equals(_color, that._color);
    }

    @Override
    public String toString() {
        return _color;
    }
}