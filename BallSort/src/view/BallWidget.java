package view;

import model.Ball;
import model.ColorProperty;

import javax.swing.*;
import java.awt.*;

public class BallWidget extends JPanel {

    public static final int BALL_DIAMETER = 30;

    private final Ball _ball;
    private boolean _isLifted = false;

    public BallWidget(Ball ball) {
        _ball = ball;
        setOpaque(false);
    }

    public void setLifted(boolean lifted) {
        _isLifted = lifted;
    }

    public void draw(Graphics g, int x, int y) {
        ColorProperty colorProp = _ball.getProperty(ColorProperty.class);
        Color ballColor = colorProp != null ? colorProp.getColor() : Color.GRAY;

        int drawY = _isLifted ? y - 10 : y;

        g.setColor(ballColor);
        g.fillOval(x, drawY, BALL_DIAMETER, BALL_DIAMETER);
        g.setColor(Color.BLACK);
        g.drawOval(x, drawY, BALL_DIAMETER, BALL_DIAMETER);
    }
}