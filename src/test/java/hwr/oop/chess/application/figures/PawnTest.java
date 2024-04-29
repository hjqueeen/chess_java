package hwr.oop.chess.application.figures;

import hwr.oop.chess.application.Board;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import hwr.oop.chess.application.Cell;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

class PawnTest {
  private Board board;

  @BeforeEach
  public void setUp() {
    // Initialize the board
    board = new Board(true);
  }

  @Test
  void createPawn() {
    Pawn pawn = new Pawn(FigureColor.BLACK);
    Assertions.assertThat(pawn.color()).isEqualTo(FigureColor.BLACK);
    Assertions.assertThat(pawn.type()).isEqualTo(FigureType.PAWN);
  }

  @ParameterizedTest
  @ValueSource(ints = {3, 4})
  void moveWhitePawn_isAllowed(int args) {
    Cell from = board.cell('d', 2);
    Cell to = board.cell('d', args);

    Figure pawn = from.getFigure();
    Assertions.assertThat(pawn.type()).isEqualTo(FigureType.PAWN);
    Assertions.assertThat(pawn.canMoveTo(from, to)).isTrue();
  }

  @ParameterizedTest
  @ValueSource(ints = {1, 2, 5})
  void moveWhitePawn_isNotAllowed(int args) {
    Cell from = board.cell('d', 2);
    Cell to = board.cell('d', args);

    Figure pawn = from.getFigure();
    Assertions.assertThat(pawn.type()).isEqualTo(FigureType.PAWN);
    Assertions.assertThat(pawn.canMoveTo(from, to)).isFalse();
  }

  @Test
  void moveWhitePawn_twoFieldsOnlyOnStart() {
    Cell from = board.cell('d', 2);
    Cell to = board.cell('d', 3);
    Cell then = board.cell('d', 5);

    Figure pawn = from.getFigure();
    Assertions.assertThat(pawn.type()).isEqualTo(FigureType.PAWN);
    Assertions.assertThat(pawn.canMoveTo(from, to)).isTrue();
    Assertions.assertThat(pawn.canMoveTo(to, then)).isFalse();
  }

  @ParameterizedTest
  @ValueSource(ints = {6})
  void moveBlackPawn_isAllowed(int args) {
    Cell from = board.cell('c', 7);
    Cell to = board.cell('c', args);

    Figure pawn = from.getFigure();
    Assertions.assertThat(pawn.type()).isEqualTo(FigureType.PAWN);
    Assertions.assertThat(pawn.canMoveTo(from, to)).isTrue();
  }

  @ParameterizedTest
  @ValueSource(ints = {8, 7, 4})
  void moveBlackPawn_isNotAllowed(int args) {
    Cell from = board.cell('c', 7);
    Cell to = board.cell('c', args);

    Figure pawn = from.getFigure();
    Assertions.assertThat(pawn.type()).isEqualTo(FigureType.PAWN);
    Assertions.assertThat(pawn.canMoveTo(from, to)).isFalse();
  }

  @Test
  void moveBlackPawn_twoFieldsOnlyOnStart() {
    Cell from = board.cell('c', 7);
    Cell to = board.cell('c', 6);
    Cell then = board.cell('c', 4);

    Figure pawn = from.getFigure();
    Assertions.assertThat(pawn.type()).isEqualTo(FigureType.PAWN);
    Assertions.assertThat(pawn.canMoveTo(from, to)).isTrue();
    Assertions.assertThat(pawn.canMoveTo(to, then)).isFalse();
  }
}
