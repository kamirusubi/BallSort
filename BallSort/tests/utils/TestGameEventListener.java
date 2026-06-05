package utils;

import game.GameEventListener;
import model.Tube;
import java.util.ArrayList;
import java.util.List;

public class TestGameEventListener implements GameEventListener {
    public int tubeSelectedCount = 0;
    public int tubeDeselectedCount = 0;
    public int moveSucceededCount = 0;
    public int moveFailedCount = 0;
    public int gameCompletedCount = 0;

    public Tube lastSelectedTube = null;
    public int lastLiftedCount = 0;
    public Tube lastDeselectedTube = null;
    public Tube lastMoveFrom = null;
    public Tube lastMoveTo = null;
    public int lastMovedCount = 0;

    public final List<String> callHistory = new ArrayList<>();

    @Override
    public void onTubeSelected(Tube tube, int liftedCount) {
        tubeSelectedCount++;
        lastSelectedTube = tube;
        lastLiftedCount = liftedCount;
        callHistory.add("onTubeSelected(" + tube + ", " + liftedCount + ")");
    }

    @Override
    public void onTubeDeselected(Tube tube) {
        tubeDeselectedCount++;
        lastDeselectedTube = tube;
        callHistory.add("onTubeDeselected(" + tube + ")");
    }

    @Override
    public void onMoveSucceeded(Tube from, Tube to, int movedCount) {
        moveSucceededCount++;
        lastMoveFrom = from;
        lastMoveTo = to;
        lastMovedCount = movedCount;
        callHistory.add("onMoveSucceeded(" + from + ", " + to + ", " + movedCount + ")");
    }

    @Override
    public void onMoveFailed(Tube from, Tube to) {
        moveFailedCount++;
        lastMoveFrom = from;
        lastMoveTo = to;
        callHistory.add("onMoveFailed(" + from + ", " + to + ")");
    }

    @Override
    public void onGameCompleted() {
        gameCompletedCount++;
        callHistory.add("onGameCompleted");
    }

    public void clear() {
        tubeSelectedCount = 0;
        tubeDeselectedCount = 0;
        moveSucceededCount = 0;
        moveFailedCount = 0;
        gameCompletedCount = 0;
        lastSelectedTube = null;
        lastLiftedCount = 0;
        lastDeselectedTube = null;
        lastMoveFrom = null;
        lastMoveTo = null;
        lastMovedCount = 0;
        callHistory.clear();
    }

    public boolean isTubeSelectedCalled() {
        return tubeSelectedCount > 0;
    }

    public boolean isMoveSucceededCalled() {
        return moveSucceededCount > 0;
    }

    public boolean isMoveFailedCalled() {
        return moveFailedCount > 0;
    }

    public boolean isGameCompletedCalled() {
        return gameCompletedCount > 0;
    }
}