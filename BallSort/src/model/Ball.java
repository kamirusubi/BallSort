package model;

import java.util.*;

public class Ball {
    private final List<BallProperty> _properties = new ArrayList<>();

    public Ball(BallProperty... properties) {
        _properties.addAll(Arrays.asList(properties));
    }

    public void addProperty(BallProperty property) {
        _properties.add(property);
    }

    @SuppressWarnings("unchecked")
    public <T extends BallProperty> T getProperty(Class<T> type) {
        for (BallProperty property : _properties) {
            if (type.isInstance(property)) {
                return (T) property;
            }
        }
        return null;
    }

    public List<BallProperty> getProperties() {
        return Collections.unmodifiableList(_properties);
    }

    @Override
    public String toString() {
        ColorProperty colorProp = getProperty(ColorProperty.class);
        return colorProp != null ? colorProp.getColor().substring(0, 1) : "?";
    }
}