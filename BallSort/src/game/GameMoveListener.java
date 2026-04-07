package game;

import model.Tube;

public interface GameMoveListener {
    void onMoveAttempt(boolean success, Tube from, Tube to);
    void onGameCompleted();
}