package view;

import game.Game;

import javax.swing.*;
import java.awt.*;

public class GameFrame extends JFrame {

    private final Game _game = new Game();
    private final LevelView _levelView;

    public GameFrame() {
        _game.start();

        setTitle("BallSort");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));

        JPanel buttonPanel = getJPanel();
        mainPanel.add(buttonPanel);

        _levelView = new LevelView(_game.getCurrentLevel(), _game);
        mainPanel.add(_levelView);

        add(mainPanel);
        pack();
        setLocationRelativeTo(null);
        setResizable(false);
    }

    private JPanel getJPanel() {
        JPanel buttonPanel = new JPanel();
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));

        JButton resetButton = new JButton("Сброс");
        resetButton.addActionListener(e -> {
            _game.reset();
            _levelView.updateLevel(_game.getCurrentLevel());
        });

        JButton newLevelButton = new JButton("Новый уровень");
        newLevelButton.addActionListener(e -> {
            _game.start();
            _levelView.updateLevel(_game.getCurrentLevel());
            pack();
        });

        buttonPanel.add(resetButton);
        buttonPanel.add(newLevelButton);
        buttonPanel.setBackground(Color.DARK_GRAY);
        return buttonPanel;
    }
}