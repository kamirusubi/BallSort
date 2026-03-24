package view;

import model.Tube;
import model.Ball;
import model.ColorProperty;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;

public class TubeWidget extends JPanel {

    public static final int TUBE_WIDTH = 40;
    public static final int BALL_DIAMETER = 30;
    private static final int BOTTOM_PADDING = 2;
    private static final int TOP_PADDING = 15;

    private static final Map<String, Color> BALL_COLORS = new HashMap<>();

    static {
        BALL_COLORS.put("RED", Color.RED);
        BALL_COLORS.put("BLUE", Color.BLUE);
        BALL_COLORS.put("GREEN", Color.GREEN);
        BALL_COLORS.put("YELLOW", Color.YELLOW);
        BALL_COLORS.put("ORANGE", Color.ORANGE);
    }

    private static final Color ERROR_BORDER_COLOR = Color.RED;


    private final Tube _tube;
    private boolean _isSelected = false;

    public TubeWidget(Tube tube) {
        _tube = tube;

        int height = calculateHeight();
        setPreferredSize(new Dimension(TUBE_WIDTH, height));
        setMinimumSize(new Dimension(TUBE_WIDTH, height));
        setMaximumSize(new Dimension(TUBE_WIDTH, height));

        setBackground(Color.LIGHT_GRAY);
        setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));
    }

    private int calculateHeight() {
        int capacity = _tube.getCapacity();
        int totalBallsHeight = capacity * BALL_DIAMETER;
        return totalBallsHeight + BOTTOM_PADDING + TOP_PADDING;
    }

    public Tube getTube() {
        return _tube;
    }

    public void setSelected(boolean selected) {
        _isSelected = selected;
        if (selected) {
            setBorder(BorderFactory.createLineBorder(Color.GREEN, 3));
        } else {
            setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));
        }
    }

    public void setError() {
        setBorder(BorderFactory.createLineBorder(ERROR_BORDER_COLOR, 3));
        repaint();
    }

    public void clearError() {
        if (_isSelected) {
            setBorder(BorderFactory.createLineBorder(Color.GREEN, 3));
        } else {
            setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));
        }
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        int startY = getHeight() - BOTTOM_PADDING;

        java.util.List<Ball> balls = _tube.getBalls();

        for (int i = 0; i < balls.size(); i++) {
            Ball ball = balls.get(i);
            ColorProperty colorProp = ball.getProperty(ColorProperty.class);
            String colorName = colorProp != null ? colorProp.getColor() : "GRAY";
            Color ballColor = BALL_COLORS.getOrDefault(colorName, Color.GRAY);

            int y = startY - (i + 1) * BALL_DIAMETER;

            if (_isSelected && i == balls.size() - 1) {
                y -= 10;
            }

            int x = (TUBE_WIDTH - BALL_DIAMETER) / 2;

            g.setColor(ballColor);
            g.fillOval(x, y, BALL_DIAMETER, BALL_DIAMETER);
            g.setColor(Color.BLACK);
            g.drawOval(x, y, BALL_DIAMETER, BALL_DIAMETER);
        }
    }
}