package model;

public interface TubeSelectionListener {
    void onTwoTubesSelected(Tube from, Tube to);
    void onFirstTubeSelected(Tube tube);
    void onFirstTubeDeselected(Tube tube);
}
