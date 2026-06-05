package game;

import model.Tube;

public interface GameEventListener {

    void onTubeSelected(Tube tube, int liftedCount);

    void onTubeDeselected(Tube tube);

    void onMoveSucceeded(Tube from, Tube to, int movedCount);

    void onMoveFailed(Tube from, Tube to);

    void onGameCompleted();

    default ListenerPriority getPriority() {
        return ListenerPriority.LOW;
    }
}