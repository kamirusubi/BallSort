package utils;
import game.GameListener;
import model.Tube;

import java.util.ArrayList;
import java.util.List;

public class TestGameListener implements GameListener {
    public int moveAttemptCount = 0;
    public int successCount = 0;
    public int failureCount = 0;
    public int gameCompletedCount = 0;

    public Tube moveFrom = null;
    public Tube moveTo = null;
    public Boolean lastMoveSuccess = null;

    public final List<String> callHistory = new ArrayList<>();

    @Override
    public void onMoveAttempt(boolean success, Tube from, Tube to) {
        moveAttemptCount++;
        lastMoveSuccess = success;
        moveFrom = from;
        moveTo = to;

        if (success) {
            successCount++;
            callHistory.add("onMoveAttempt(success)");
        } else {
            failureCount++;
            callHistory.add("onMoveAttempt(failure)");
        }
    }

    @Override
    public void onGameCompleted() {
        gameCompletedCount++;
        callHistory.add("onGameCompleted");
    }

    public void clear() {
        moveAttemptCount = 0;
        successCount = 0;
        failureCount = 0;
        gameCompletedCount = 0;
        moveFrom = null;
        moveTo = null;
        lastMoveSuccess = null;
        callHistory.clear();
    }

    public boolean isMoveAttemptCalled() {
        return moveAttemptCount > 0;
    }

    public boolean isGameCompletedCalled() {
        return gameCompletedCount > 0;
    }
}