package ListenerTests;

import game.GameEventListener;
import game.ListenerPriority;
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

    private int callSequenceNumber = 0;
    public final List<CallRecord> timedCallHistory = new ArrayList<>();

    private ListenerPriority priority = ListenerPriority.LOW;

    public TestGameEventListener() {
    }

    public TestGameEventListener(ListenerPriority priority) {
        this.priority = priority;
    }

    public void setPriority(ListenerPriority priority) {
        this.priority = priority;
    }

    @Override
    public ListenerPriority getPriority() {
        return priority;
    }

    public static class CallRecord {
        public final int sequence;
        public final String methodName;
        public final long timestamp;

        public CallRecord(int sequence, String methodName) {
            this.sequence = sequence;
            this.methodName = methodName;
            this.timestamp = System.nanoTime();
        }
    }

    @Override
    public void onTubeSelected(Tube tube, int liftedCount) {
        tubeSelectedCount++;
        lastSelectedTube = tube;
        lastLiftedCount = liftedCount;
        callHistory.add("onTubeSelected(" + tube + ", " + liftedCount + ")");
        timedCallHistory.add(new CallRecord(++callSequenceNumber, "onTubeSelected"));
    }

    @Override
    public void onTubeDeselected(Tube tube) {
        tubeDeselectedCount++;
        lastDeselectedTube = tube;
        callHistory.add("onTubeDeselected(" + tube + ")");
        timedCallHistory.add(new CallRecord(++callSequenceNumber, "onTubeDeselected"));
    }

    @Override
    public void onMoveSucceeded(Tube from, Tube to, int movedCount) {
        moveSucceededCount++;
        lastMoveFrom = from;
        lastMoveTo = to;
        lastMovedCount = movedCount;
        callHistory.add("onMoveSucceeded(" + from + ", " + to + ", " + movedCount + ")");
        timedCallHistory.add(new CallRecord(++callSequenceNumber, "onMoveSucceeded"));
    }

    @Override
    public void onMoveFailed(Tube from, Tube to) {
        moveFailedCount++;
        lastMoveFrom = from;
        lastMoveTo = to;
        callHistory.add("onMoveFailed(" + from + ", " + to + ")");
        timedCallHistory.add(new CallRecord(++callSequenceNumber, "onMoveFailed"));
    }

    @Override
    public void onGameCompleted() {
        gameCompletedCount++;
        callHistory.add("onGameCompleted");
        timedCallHistory.add(new CallRecord(++callSequenceNumber, "onGameCompleted"));
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
        callSequenceNumber = 0;
        timedCallHistory.clear();
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

    public int getCallSequenceForMethod(String methodName) {
        for (CallRecord record : timedCallHistory) {
            if (record.methodName.equals(methodName)) {
                return record.sequence;
            }
        }
        return -1;
    }

    public boolean wasMethodCalledBefore(String firstMethod, String secondMethod) {
        int firstSeq = getCallSequenceForMethod(firstMethod);
        int secondSeq = getCallSequenceForMethod(secondMethod);
        return firstSeq > 0 && secondSeq > 0 && firstSeq < secondSeq;
    }
}