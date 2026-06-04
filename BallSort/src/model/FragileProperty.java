package model;

public class FragileProperty implements BallProperty{
    @Override
    public boolean equals(Object obj) {
        return obj instanceof FragileProperty;
    }

    @Override
    public String toString() {
        return "Хр";
    }
}