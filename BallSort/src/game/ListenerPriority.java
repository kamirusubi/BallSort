package game;

public enum ListenerPriority {
    HIGHEST(300),
    HIGH(200),
    NORMAL(100),
    LOW(50),
    LOWEST(0);

    private final int _value;

    ListenerPriority(int value) {
        _value = value;
    }

    public int getValue() {
        return _value;
    }
}