package view;

import model.Tube;
import model.Ball;
import rules.SequenceRule;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class TubeWidget extends JPanel {

    public static final int TUBE_WIDTH = 40;
    public static final int BALL_DIAMETER = 30;
    private static final int BOTTOM_PADDING = 2;
    private static final int TOP_PADDING = 15;
    private static final Color ERROR_BORDER_COLOR = Color.RED;


    private final Tube _tube;
    private final SequenceRule _rules;

    public TubeWidget(Tube tube, SequenceRule rules) {
        _tube = tube;
        _rules = rules;

        int height = calculateHeight();
        setPreferredSize(new Dimension(TUBE_WIDTH, height));
        setMinimumSize(new Dimension(TUBE_WIDTH, height));
        setMaximumSize(new Dimension(TUBE_WIDTH, height));

        setBackground(Color.LIGHT_GRAY);
        setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));

        addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                onTubeClick();
            }
        });
    }

    private int calculateHeight() {
        int capacity = _tube.getCapacity();
        int totalBallsHeight = capacity * BALL_DIAMETER;
        return totalBallsHeight + BOTTOM_PADDING + TOP_PADDING;
    }

    public Tube getTube() {
        return _tube;
    }

    private void onTubeClick() {
        _tube.setSelected(!_tube.isSelected());
    }

    public void updateSelectionVisual() {
        if (_tube.isSelected()) {
            setBorder(BorderFactory.createLineBorder(Color.GREEN, 3));
        } else {
            setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));
        }
    }

    public void setError() {
        setBorder(BorderFactory.createLineBorder(ERROR_BORDER_COLOR, 3));
    }

    public void clearError() {
        updateSelectionVisual();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        int startY = getHeight() - BOTTOM_PADDING;

        List<Ball> balls = _tube.getBalls();

        List<Ball> liftedBalls = _tube.isSelected() ? _tube.peekSequence(_rules) : java.util.Collections.emptyList();

        for (int i = 0; i < balls.size(); i++) {
            Ball ball = balls.get(i);

            BallWidget widget = new BallWidget(ball);
            if (_tube.isSelected() && liftedBalls.contains(ball)) {
                widget.setLifted(true);
            }

            int y = startY - (i + 1) * BallWidget.BALL_DIAMETER;
            int x = (TUBE_WIDTH - BallWidget.BALL_DIAMETER) / 2;

            widget.draw(g, x, y);
        }
    }
}