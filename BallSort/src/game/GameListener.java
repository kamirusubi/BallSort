package game;

import model.Tube;

public interface GameListener {
    void onGameStarted();
    void onGameWon();
    void onInvalidMove(String message);
    void onTubeSelected(Tube tube);
    void onTubeDeselected();
    void onMovePerformed(Tube from, Tube to);
}