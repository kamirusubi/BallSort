package view;

import model.Level;
import model.Tube;
import game.Game;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LevelView extends JPanel {

    private Level _level;
    private final Map<Tube, TubeWidget> _tubeWidgets;
    private TubeWidget _selectedWidget = null;
    private final Game _game;
    private static final int TUBES_PER_ROW = 3;
    private Timer _errorTimer;
    private TubeWidget _errorWidget = null;

    public LevelView(Level level, Game game) {
        _game = game;
        _tubeWidgets = new HashMap<>();
        _level = level;

        setLayout(new GridBagLayout());
        _errorTimer = new Timer(1000, e -> {
            if (_errorWidget != null) {
                _errorWidget.clearError();
                _errorWidget = null;
            }
        });
        _errorTimer.setRepeats(false);

        setBackground(Color.DARK_GRAY);

        rebuild();
    }

    public void updateLevel(Level newLevel) {
        _level = newLevel;
        _selectedWidget = null;
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
            TubeWidget widget = new TubeWidget(tube);
            _tubeWidgets.put(tube, widget);

            widget.addMouseListener(new java.awt.event.MouseAdapter() {
                @Override
                public void mouseClicked(java.awt.event.MouseEvent e) {
                    onTubeClick(tube);
                }
            });

            int row = i / TUBES_PER_ROW;
            int col = i % TUBES_PER_ROW;

            gbc.gridx = col;
            gbc.gridy = row;
            add(widget, gbc);
        }
    }

    private void onTubeClick(Tube tube) {
        if (_selectedWidget == null) {
            if (!tube.isEmpty()) {
                _selectedWidget = _tubeWidgets.get(tube);
                _selectedWidget.setSelected(true);
            }
        } else {
            Tube fromTube = _selectedWidget.getTube();
            if (fromTube == tube) {
                _selectedWidget.setSelected(false);
                _selectedWidget = null;
            } else {
                if (_game.tryMove(fromTube, tube)) {
                    _selectedWidget.setSelected(false);
                    _selectedWidget = null;
                    repaint();

                    if (_game.isLevelCompleted()) {
                        JOptionPane.showMessageDialog(this, "Победа!");
                    }
                } else {
                    TubeWidget errorWidget = _tubeWidgets.get(tube);
                    errorWidget.setError();
                    _errorWidget = errorWidget;
                    _errorTimer.start();

                    _selectedWidget.setSelected(false);
                    _selectedWidget = null;
                }
            }
        }
    }


    @Override
    public void repaint() {
        super.repaint();
        if (_tubeWidgets != null) {
            for (TubeWidget widget : _tubeWidgets.values()) {
                widget.repaint();
            }
        }
    }
}