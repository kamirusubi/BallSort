package view;

import model.Tube;
import model.Ball;

import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.util.Collections;

public class TubeWidget extends JPanel {

    public static final int TUBE_WIDTH = 40;
    public static final int BALL_DIAMETER = 30;
    private static final int BOTTOM_PADDING = 2;
    private static final int TOP_PADDING = 15;
    private static final Color ERROR_BORDER_COLOR = Color.RED;
    private static final Color SELECTED_BORDER_COLOR = Color.GREEN;

    private final Tube _tube;
    private boolean _isSelected = false;
    private boolean _hasError = false;
    private List<Ball> _liftedBalls = Collections.emptyList();

    public TubeWidget(Tube tube) {
        _tube = tube;

        int height = calculateHeight();
        setPreferredSize(new Dimension(TUBE_WIDTH, height));
        setMinimumSize(new Dimension(TUBE_WIDTH, height));
        setMaximumSize(new Dimension(TUBE_WIDTH, height));

        setBackground(Color.LIGHT_GRAY);
        updateBorder();
    }

    public Tube getTube() {
        return _tube;
    }

    public void setSelected(boolean selected) {
        _isSelected = selected;
        updateBorder();
        repaint();
    }

    public void setError(boolean error) {
        _hasError = error;
        updateBorder();
        if (!error) {
            repaint();
        }
    }

    public void setLiftedBalls(List<Ball> liftedBalls) {
        _liftedBalls = liftedBalls != null ? liftedBalls : Collections.emptyList();
        repaint();
    }

    private void updateBorder() {
        if (_hasError) {
            setBorder(BorderFactory.createLineBorder(ERROR_BORDER_COLOR, 3));
        } else if (_isSelected) {
            setBorder(BorderFactory.createLineBorder(SELECTED_BORDER_COLOR, 3));
        } else {
            setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));
        }
    }

    private int calculateHeight() {
        int capacity = _tube.getCapacity();
        int totalBallsHeight = capacity * BALL_DIAMETER;
        return totalBallsHeight + BOTTOM_PADDING + TOP_PADDING;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        int startY = getHeight() - BOTTOM_PADDING;
        List<Ball> balls = _tube.getBalls();

        for (int i = 0; i < balls.size(); i++) {
            Ball ball = balls.get(i);
            BallWidget widget = new BallWidget(ball);

            boolean isLifted = _isSelected && _liftedBalls.contains(ball);
            widget.setLifted(isLifted);

            int y = startY - (i + 1) * BallWidget.BALL_DIAMETER;
            int x = (TUBE_WIDTH - BallWidget.BALL_DIAMETER) / 2;

            widget.draw(g, x, y);
        }
    }
}