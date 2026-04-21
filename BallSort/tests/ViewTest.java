import game.Game;
import model.Ball;
import model.Level;
import model.Tube;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import view.BallWidget;
import view.LevelView;
import view.TubeWidget;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

import static org.junit.jupiter.api.Assertions.*;

class ViewTest {

    private Game game;
    private Level level;

    @BeforeEach
    void setUp() {
        game = new Game();
        game.startForTests();
        level = game.getCurrentLevel();
    }

    @Test
    void test01_BallWidgetCreation() {
        Tube tube = level.getTubes().get(0);
        Ball ball = tube.getBalls().get(0);

        BallWidget widget = new BallWidget(ball);

        assertNotNull(widget);
        assertEquals(30, BallWidget.BALL_DIAMETER);
        assertFalse(widget.isOpaque());
    }

    @Test
    void test02_BallWidgetSetLifted() {
        Tube tube = level.getTubes().get(0);
        Ball ball = tube.getBalls().get(0);

        BallWidget widget = new BallWidget(ball);

        widget.setLifted(true);
        assertDoesNotThrow(() -> widget.setLifted(false));
    }

    @Test
    void test03_TubeWidgetCreation() {
        Tube tube = level.getTubes().get(0);

        TubeWidget widget = new TubeWidget(tube, game.getRules());

        assertNotNull(widget);
        assertEquals(tube, widget.getTube());
        assertEquals(40, TubeWidget.TUBE_WIDTH);
        assertEquals(30, TubeWidget.BALL_DIAMETER);
    }

    @Test
    void test04_TubeWidgetPreferredSize() {
        Tube tube = new Tube(4);
        TubeWidget widget = new TubeWidget(tube, game.getRules());

        Dimension preferredSize = widget.getPreferredSize();

        assertEquals(40, preferredSize.width);
        int expectedHeight = 4 * 30 + 2 + 15;
        assertEquals(expectedHeight, preferredSize.height);
    }


    @Test
    void test05_LevelViewCreation() {
        LevelView levelView = new LevelView(level, game);

        assertNotNull(levelView);
        assertEquals(Color.DARK_GRAY, levelView.getBackground());
        assertTrue(levelView.getLayout() instanceof GridBagLayout);
    }

    @Test
    void test06_LevelViewUpdateLevel() {
        LevelView levelView = new LevelView(level, game);
        Level newLevel = game.getCurrentLevel();

        assertDoesNotThrow(() -> levelView.updateLevel(newLevel));
    }

    @Test
    void test7_LevelViewRepaint() {
        LevelView levelView = new LevelView(level, game);

        assertDoesNotThrow(() -> levelView.repaint());
    }

    @Test
    void test9_LevelViewMultipleUpdates() {
        LevelView levelView = new LevelView(level, game);

        assertDoesNotThrow(() -> {
            levelView.updateLevel(level);
            levelView.updateLevel(level);
            levelView.repaint();
        });
    }
}