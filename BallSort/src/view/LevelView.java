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

    private static final int TUBES_PER_ROW = 3;

    private Level _level;
    private final Map<Tube, TubeWidget> _tubeWidgets = new HashMap<>();
    private TubeWidget _selectedWidget = null;
    private final Game _game;
    private Timer _errorTimer;
    private TubeWidget _errorWidget = null;

    public LevelView(Level level, Game game) {
        _game = game;
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

            gbc.gridx = i % TUBES_PER_ROW;
            gbc.gridy = i / TUBES_PER_ROW;
            add(widget, gbc);
        }
    }

    private void onTubeClick(Tube tube) {
        if (_selectedWidget == null) { // если первое нажатие
            if (!tube.isEmpty()) {
                _selectedWidget = _tubeWidgets.get(tube);
                _selectedWidget.setSelected(true);
            }
        } else { // если второе нажатие
            Tube fromTube = _selectedWidget.getTube();
            if (fromTube == tube) { // нажали на ту же трубу
                _selectedWidget.setSelected(false); // отменяем выделение
                _selectedWidget = null;
            } else { // нажали на другую трубу
                if (_game.tryMove(fromTube, tube)) { // если получилось переместить шарик
                    _selectedWidget.setSelected(false); //отменяем выделение и проверяем победу
                    _selectedWidget = null;
                    repaint();

                    if (_game.isLevelCompleted()) {
                        JOptionPane.showMessageDialog(this, "Победа!");
                    }
                } else { // если не получилось переместить шарик
                    TubeWidget errorWidget = _tubeWidgets.get(tube); // отменяем выделение и выделяем красным
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
            for (TubeWidget tubeWidget : _tubeWidgets.values()) {
                tubeWidget.repaint();
            }
        }
    }
}