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
        ChargeProperty chargeProp = getProperty(ChargeProperty.class);
        return chargeProp.toString();
    }
}