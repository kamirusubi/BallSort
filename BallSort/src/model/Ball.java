package model;

import java.util.*;

public class Ball {
    private final List<BallProperty> _properties = new ArrayList<>();

    public Ball(BallProperty... properties) {
        for (BallProperty property : properties) {
            addProperty(property);
        }
    }

    public void addProperty(BallProperty property) {
        if (property == null) {
            throw new IllegalArgumentException("Property cannot be null");
        }

        if (getProperty(property.getClass()) != null) {
            throw new IllegalStateException(
                    "Property of type " + property.getClass().getSimpleName() + " already exists"
            );
        }

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
        StringBuilder sb = new StringBuilder();
        for (BallProperty property : _properties) {
            if (!(property instanceof ColorProperty)) {
                sb.append(property.toString());
            }
        }
        return sb.toString();
    }
}