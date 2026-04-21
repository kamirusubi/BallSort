package view;

import model.Ball;
import model.ColorProperty;

import javax.swing.*;
import java.awt.*;

import model.ChargeProperty;
import model.Charge;

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

    public void dr31aw(Graphics g, int x, int y) {
        ColorProperty colorProp = _ball.getProperty(ColorProperty.class);
        Color ballColor = colorProp != null ? colorProp.getColor() : Color.GRAY;

        int drawY = _isLifted ? y - 10 : y;

        g.setColor(ballColor);
        g.fillOval(x, drawY, BALL_DIAMETER, BALL_DIAMETER);
        g.setColor(Color.BLACK);
        g.drawOval(x, drawY, BALL_DIAMETER, BALL_DIAMETER);
    }

    public void draw(Graphics g, int x, int y) {
        ColorProperty colorProp = _ball.getProperty(ColorProperty.class);
        Color ballColor = colorProp != null ? colorProp.getColor() : Color.GRAY;

        int drawY = _isLifted ? y - 10 : y;

        g.setColor(ballColor);
        g.fillOval(x, drawY, BALL_DIAMETER, BALL_DIAMETER);
        g.setColor(Color.BLACK);
        g.drawOval(x, drawY, BALL_DIAMETER, BALL_DIAMETER);

        ChargeProperty chargeProp = _ball.getProperty(ChargeProperty.class);
        if (chargeProp != null) {
            Charge charge = chargeProp.getCharge();
            g.setColor(Color.BLACK);
            g.setFont(new Font("Arial", Font.BOLD, 14));
            FontMetrics fm = g.getFontMetrics();

            String symbol = _ball.toString();

            int symbolX = x + (BALL_DIAMETER - fm.stringWidth(symbol)) / 2;
            int symbolY = drawY + (BALL_DIAMETER + fm.getAscent() - fm.getDescent()) / 2;

            g.drawString(symbol, symbolX, symbolY);
        }
    }
}