package model;

public enum Charge {
    POSITIVE,
    NEGATIVE,
    NEUTRAL;

    @Override
    public String toString() {
        switch (this) {
            case POSITIVE: return "+";
            case NEGATIVE: return "-";
            case NEUTRAL: return "0";
            default: return "?";
        }
    }
}