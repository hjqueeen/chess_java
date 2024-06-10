package hwr.oop.chess.persistence;

import static org.assertj.core.api.Assertions.*;

import hwr.oop.chess.cli.InvalidUserInputException;
import hwr.oop.chess.cli.Main;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

class CSVFilePersistenceTest {
  private final Persistence persistence = new CSVFilePersistence();
  private Path gameCsvFile = Paths.get("game_9999.csv");

  @BeforeEach
  @AfterEach
  void cleanUp() {
    try {
      if (Files.exists(gameCsvFile)) {
        Files.delete(gameCsvFile);
      }
    } catch (IOException e) {
      fail("The cleanup was not successful: " + e.getMessage());
    }
  }

  @ParameterizedTest
  @ValueSource(ints = {9999, 1234})
  void LoadGame(int gameId) throws IOException {
    gameCsvFile = Paths.get("game_" + gameId + ".csv");
    createGame(gameId);
    persistence.setGameId(gameId);
    persistence.loadGame();

    assertThat(persistence.loadState("fen"))
        .isEqualTo("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 0");
    assertThat(persistence.loadState("endType")).isEqualTo("NOT_END");
    assertThat(persistence.loadState("isDrawOffered")).isEqualTo("0");
    assertThat(persistence.loadState("whiteScore")).isEqualTo("0");
    assertThat(persistence.loadState("blackScore")).isEqualTo("0");
    assertThat(persistence.loadState("winner")).isNull();
    assertThat(Files.exists(gameCsvFile)).isTrue();

    assertThat(Files.deleteIfExists(gameCsvFile)).isTrue();
    assertThatThrownBy(persistence::loadGame)
        .isInstanceOf(InvalidUserInputException.class)
        .hasMessageContaining("game_" + gameId + ".csv");
    assertThat(persistence.loadState("fen")).isNull();
    assertThat(persistence.loadState("endType")).isNull();
    assertThat(persistence.loadState("isDrawOffered")).isNull();
    assertThat(persistence.loadState("whiteScore")).isNull();
    assertThat(persistence.loadState("blackScore")).isNull();
  }

  private void createGame(int gameId) {
    Main.main(new String[] {"create", String.valueOf(gameId)});
  }

  @Test
  void checkIfMoveEventOnBoardSavesChanges() {
    Main.main(new String[] {"create", "9999"});
    Main.main(new String[] {"on", "9999", "move", "b2", "b3"});

    persistence.setGameId(9999);
    persistence.loadGame();
    assertThat(persistence.gameId()).isEqualTo(9999);
    assertThat(persistence.loadState("fen"))
        .isEqualTo("rnbqkbnr/pppppppp/8/8/8/1P6/P1PPPPPP/RNBQKBNR b KQkq - 0 0");
  }

  @Test
  void checkIfPromotionOnBoardSavesChanges() {
    Main.main(new String[] {"create", "9999"});

    persistence.setGameId(9999);
    persistence.loadGame();
    persistence.storeState("fen", "2PP4/8/8/8/7k/8/PP6/7K w - - 0 1");
    persistence.saveGame();

    Main.main(new String[] {"on", "9999", "promote", "c8", "ROOK"});

    persistence.loadGame();
    assertThat(persistence.loadState("fen")).isEqualTo("2RP4/8/8/8/7k/8/PP6/7K w - - 0 1");
  }

  @Test
  void negativeGameId() {
    assertThatThrownBy(() -> persistence.setGameId(0))
        .isInstanceOf(InvalidUserInputException.class)
        .hasMessageContaining("The game ID must be a positive integer (1 or larger).");
    assertThatThrownBy(() -> persistence.setGameId(-1))
        .isInstanceOf(InvalidUserInputException.class)
        .hasMessageContaining("The game ID must be a positive integer (1 or larger).");
  }

  @Test
  void writeFileIsLocked() throws IOException {
    Files.createDirectories(gameCsvFile);
    persistence.setGameId(9999);
    assertThatThrownBy(persistence::saveGame)
        .isInstanceOf(InvalidUserInputException.class)
        .hasMessageContaining("game_9999.csv")
        .hasMessageContaining(
            "The Game #9999 could not be saved. Please verify that the current folder is not protected.");
  }
}
