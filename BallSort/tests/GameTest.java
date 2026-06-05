import game.Game;
import model.Level;
import model.Tube;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import rules.CompositeSequenceRule;
import rules.SequenceRule;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class GameTest {

    private Game game;

    @BeforeEach
    void setUp() {
        game = new Game();
        game.startForTests();
    }

    @Test
    void test01_GetRulesReturnsCompositeRule() {
        assertNotNull(game.getRules());
        assertInstanceOf(SequenceRule.class, game.getRules());
    }

    @Test
    void test02_StartSetsLevel() {
        Game newGame = new Game();
        assertNull(newGame.getCurrentLevel());

        newGame.startForTests();

        assertNotNull(newGame.getCurrentLevel());
    }

    @Test
    void test03_ResetOnNullLevelDoesNothing() {
        Game newGame = new Game();
        assertDoesNotThrow(newGame::reset);
    }

    @Test
    void test04_GetCurrentLevel() {
        assertNotNull(game.getCurrentLevel());
        assertInstanceOf(Level.class, game.getCurrentLevel());
    }
}