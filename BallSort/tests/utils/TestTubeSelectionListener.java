package utils;

import model.Tube;
import model.TubeSelectionListener;
import java.util.ArrayList;
import java.util.List;

public class TestTubeSelectionListener implements TubeSelectionListener {
    public int firstTubeSelectedCount = 0;
    public int firstTubeDeselectedCount = 0;

    public Tube selectedTube = null;
    public Tube deselectedTube = null;

    public final List<String> callHistory = new ArrayList<>();

    @Override
    public void onFirstTubeSelected(Tube tube) {
        firstTubeSelectedCount++;
        selectedTube = tube;
        callHistory.add("onFirstTubeSelected");
    }

    @Override
    public void onFirstTubeDeselected(Tube tube) {
        firstTubeDeselectedCount++;
        deselectedTube = tube;
        callHistory.add("onFirstTubeDeselected");
    }

    public void clear() {
        firstTubeSelectedCount = 0;
        firstTubeDeselectedCount = 0;
        selectedTube = null;
        deselectedTube = null;
        callHistory.clear();
    }

    public boolean isFirstTubeSelectedCalled() {
        return firstTubeSelectedCount > 0;
    }

    public boolean isFirstTubeDeselectedCalled() {
        return firstTubeDeselectedCount > 0;
    }
}