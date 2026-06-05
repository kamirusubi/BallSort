import game.Game;
import model.Level;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class GameTest {

    private Game game;

    @BeforeEach
    void setUp() {
        game = new Game();
        game.startForTests();
    }

    @Test
    void test01_StartSetsLevel() {
        Game newGame = new Game();
        assertNull(newGame.getCurrentLevel());

        newGame.startForTests();

        assertNotNull(newGame.getCurrentLevel());
    }

    @Test
    void test02_ResetOnNullLevelDoesNothing() {
        Game newGame = new Game();
        assertDoesNotThrow(newGame::reset);
    }

    @Test
    void test03_GetCurrentLevel() {
        assertNotNull(game.getCurrentLevel());
        assertInstanceOf(Level.class, game.getCurrentLevel());
    }

    @Test
    void test04_IsLevelCompletedInitiallyFalse() {
        assertFalse(game.isLevelCompleted());
    }

    @Test
    void test05_StartRandomLevel() {
        Game newGame = new Game();
        newGame.start();
        assertNotNull(newGame.getCurrentLevel());
    }
}