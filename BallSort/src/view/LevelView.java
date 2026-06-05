package view;

import game.Game;
import game.LevelListener;
import model.Level;
import model.Tube;
import model.TubeSelectionListener;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LevelView extends JPanel implements TubeSelectionListener, LevelListener {

    private static final int TUBES_PER_ROW = 3;

    private Level _level;
    private final Map<Tube, TubeWidget> _tubeWidgets = new HashMap<>();
    private final Game _game;
    private TubeWidget _errorWidget = null;

    private Timer _errorTimer = new Timer(300, e -> {
        if (_errorWidget != null) {
            _errorWidget.clearError();
            _errorWidget = null;
        }
    });

    public LevelView(Level level, Game game) {
        _game = game;
        _level = level;

        _level.addTubeSelectionListener(this);
        _level.addLevelListener(this);

        setLayout(new GridBagLayout());

        _errorTimer.setRepeats(false);

        setBackground(Color.DARK_GRAY);

        rebuild();
    }

    public void updateLevel(Level newLevel) {
        _level.removeTubeSelectionListener(this);
        _level.removeLevelListener(this);
        _level = newLevel;
        _level.addTubeSelectionListener(this);
        _level.addLevelListener(this);
        _tubeWidgets.clear();
        removeAll();
        rebuild();
        revalidate();
        repaint();
    }

    private void rebuild() {
        List<Tube> tubes = _level.getTubes();
        int tubeCount = tubes.size();

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(25, 10, 15, 10);

        for (int i = 0; i < tubeCount; i++) {
            Tube tube = tubes.get(i);
            TubeWidget widget = new TubeWidget(tube, _game.getRules());
            _tubeWidgets.put(tube, widget);

            gbc.gridx = i % TUBES_PER_ROW;
            gbc.gridy = i / TUBES_PER_ROW;
            add(widget, gbc);
        }
    }

    private void updateTubeVisual(Tube tube) {
        TubeWidget widget = _tubeWidgets.get(tube);
        if (widget != null) {
            widget.updateSelectionVisual();
        }
    }

    @Override
    public void onFirstTubeSelected(Tube tube) {
        updateTubeVisual(tube);
    }

    @Override
    public void onFirstTubeDeselected(Tube tube) {
        updateTubeVisual(tube);
    }

    @Override
    public void onMoveAttempt(boolean success, Tube from, Tube to) {
        if (success) {
            TubeWidget fromWidget = _tubeWidgets.get(from);
            TubeWidget toWidget = _tubeWidgets.get(to);
            if (fromWidget != null) fromWidget.repaint();
            if (toWidget != null) toWidget.repaint();
        } else {
            TubeWidget errorWidget = _tubeWidgets.get(to);
            if (errorWidget != null) {
                errorWidget.setError();
                _errorWidget = errorWidget;
                _errorTimer.start();
            }
        }
    }

    @Override
    public void onGameCompleted() {
        JOptionPane.showMessageDialog(this, "Победа!");
    }

    @Override
    public void repaint() {
        super.repaint();
        if (_tubeWidgets != null) {
            for (TubeWidget tubeWidget : _tubeWidgets.values()) {
                tubeWidget.repaint();
                tubeWidget.updateSelectionVisual();
            }
        }
    }
}