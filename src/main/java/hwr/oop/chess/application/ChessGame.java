package hwr.oop.chess.application;

import hwr.oop.chess.persistence.FenNotation;
import hwr.oop.chess.persistence.Persistence;

public class ChessGame {
  private final Persistence persistence;
  private final Board board;

  public ChessGame(Persistence persistence, boolean isNew) {
    this.persistence = persistence;
    board = new Board(false);
    if (isNew) {
      newGame();
    } else {
      loadGame();
    }
  }

  private void newGame() {
    board.addFiguresToBoard();
  }

  private void loadGame() {
    persistence.loadGame();
    FenNotation.parseFEN(board, persistence.loadState("fen"));
  }

  public void saveGame() {
    persistence.storeState("fen", FenNotation.generateFen(board));
    persistence.saveGame();
  }

  public Board board() {
    return board;
  }
}
