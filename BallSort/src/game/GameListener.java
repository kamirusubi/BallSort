package game;

import model.Tube;

public interface GameListener {
    void onMoveAttempt(boolean success, Tube from, Tube to);
    void onGameCompleted();
}