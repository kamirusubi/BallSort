package view;

import game.Game;
import game.GameEventListener;
import model.Level;
import model.Tube;
import model.Ball;

import javax.swing.*;
import java.awt.*;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LevelView extends JPanel {

    private static final int TUBES_PER_ROW = 3;

    private Level _level;
    private final Map<Tube, TubeWidget> _tubeWidgets = new HashMap<>();
    private final Game _game;
    private Timer _errorTimer;
    private Tube _errorTube = null;
    private GameEventListener _currentListener;

    public LevelView(Game game) {
        _game = game;
        _level = _game.getCurrentLevel();

        setLayout(new GridBagLayout());
        setBackground(Color.DARK_GRAY);

        _errorTimer = new Timer(300, e -> clearError());
        _errorTimer.setRepeats(false);

        rebuild();

        if (_level != null) {
            subscribeToLevel();
        }
    }

    private void subscribeToLevel() {
        _currentListener = new GameEventListener() {
            @Override
            public void onTubeSelected(Tube tube) {
                updateAllVisuals();
            }

            @Override
            public void onTubeDeselected(Tube tube) {
                updateAllVisuals();
            }

            @Override
            public void onMoveSucceeded(Tube from, Tube to, int movedCount) {
                updateAllVisuals();
            }

            @Override
            public void onMoveFailed(Tube from, Tube to) {
                if (to != null) {
                    showErrorOnTube(to);
                }
                updateAllVisuals();
            }

            @Override
            public void onGameCompleted() {
                updateAllVisuals();
                JOptionPane.showMessageDialog(LevelView.this, "Победа!");
            }
        };
        _level.addEventListener(_currentListener);
    }

    private void unsubscribeFromLevel() {
        if (_level != null && _currentListener != null) {
            _level.removeEventListener(_currentListener);
            _currentListener = null;
        }
    }

    public void updateLevel(Level newLevel) {
        unsubscribeFromLevel();

        _level = newLevel;
        _tubeWidgets.clear();
        removeAll();
        rebuild();
        revalidate();
        repaint();

        if (_level != null) {
            subscribeToLevel();
        }
    }

    private void rebuild() {
        int tubeCount = _level.getTubeCount();

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(25, 10, 15, 10);

        for (int i = 0; i < tubeCount; i++) {
            Tube tube = _level.getTubeAt(i);
            TubeWidget widget = new TubeWidget(tube);
            _tubeWidgets.put(tube, widget);

            final Tube currentTube = tube;
            widget.addMouseListener(new java.awt.event.MouseAdapter() {
                @Override
                public void mouseClicked(java.awt.event.MouseEvent e) {
                    onTubeClick(currentTube);
                }
            });

            gbc.gridx = i % TUBES_PER_ROW;
            gbc.gridy = i / TUBES_PER_ROW;
            add(widget, gbc);
        }

        updateAllVisuals();
    }

    private void onTubeClick(Tube clickedTube) {
        if (_level != null) {
            _level.handleTubeClick(clickedTube);
        }
    }

    private void updateAllVisuals() {
        if (_level == null) return;

        Tube pendingTube = _level.getPendingTube();

        for (Map.Entry<Tube, TubeWidget> entry : _tubeWidgets.entrySet()) {
            Tube tube = entry.getKey();
            TubeWidget widget = entry.getValue();

            if (widget == null) continue;

            widget.setSelected(tube == pendingTube);

            if (tube == pendingTube && pendingTube != null) {
                List<Ball> liftedBalls = _level.getSequenceToMove(tube);
                widget.setLiftedBalls(liftedBalls);
            } else {
                widget.setLiftedBalls(Collections.emptyList());
            }

            widget.repaint();
        }
    }

    private void showErrorOnTube(Tube tube) {
        TubeWidget widget = _tubeWidgets.get(tube);
        if (widget != null) {
            widget.setError(true);
            _errorTube = tube;
            _errorTimer.start();
        }
    }

    private void clearError() {
        if (_errorTube != null) {
            TubeWidget widget = _tubeWidgets.get(_errorTube);
            if (widget != null) {
                widget.setError(false);
                widget.repaint();
            }
            _errorTube = null;
        }
    }
}