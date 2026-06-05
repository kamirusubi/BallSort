package game;

import model.Tube;

public interface LevelListener {
    void onMoveAttempt(boolean success, Tube from, Tube to);
    void onGameCompleted();
}