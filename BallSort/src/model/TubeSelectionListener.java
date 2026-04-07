package model;

public interface TubeSelectionListener {
    void onTubeSelected(Tube tube);
    void onTubeDeselected(Tube tube);
    void onTwoTubesSelected(Tube from, Tube to);
}
