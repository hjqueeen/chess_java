package hwr.oop.chess.application.figures;

import hwr.oop.chess.application.Board;
import hwr.oop.chess.application.Cell;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class KnightTest {
  Board board;

  @BeforeEach
  void setUp() {
    board = new Board(true);
  }

  @Test
  void createKnight() {
    Knight knight = new Knight(FigureColor.BLACK);
    assertThat(knight.color()).isEqualTo(FigureColor.BLACK);
    assertThat(knight.type()).isEqualTo(FigureType.KNIGHT);
  }

  @ParameterizedTest
  @ValueSource(chars = {'a', 'c'})
  void moveWhiteKnight_isAllowed(char args) {
    Cell from = board.findCell('b', 1);
    Cell to = board.findCell(args, 3);

    Figure knight = from.figure();
    assertThat(knight.type()).isEqualTo(FigureType.KNIGHT);
    assertThat(knight.canMoveTo(from, to)).isTrue();
  }

  @ParameterizedTest
  @ValueSource(ints = {2, 3})
  void moveWhiteKnight_cannotMoveForward(int args) {
    Cell from = board.findCell('b', 1);
    Cell to = board.findCell('b', args);

    Figure knight = from.figure();
    assertThat(knight.type()).isEqualTo(FigureType.KNIGHT);
    assertThat(knight.canMoveTo(from, to)).isFalse();
  }

  @ParameterizedTest
  @ValueSource(ints = {0, -1})
  void moveWhiteKnight_cannotMoveIntoVoid(int args) {
    Cell from = board.findCell('b', 1);
    Cell to = board.findCell('b', args);

    Figure knight = from.figure();
    assertThat(knight.type()).isEqualTo(FigureType.KNIGHT);
    assertThat(knight.canMoveTo(from, to)).isFalse();
  }

  @ParameterizedTest
  @ValueSource(chars = {'a', 'c'})
  void moveBlackKnight_isAllowed(char args) {
    Cell from = board.findCell('b', 8);
    Cell to = board.findCell(args, 6);

    Figure knight = from.figure();
    assertThat(knight.type()).isEqualTo(FigureType.KNIGHT);
    assertThat(knight.canMoveTo(from, to)).isTrue();
  }

  @ParameterizedTest
  @ValueSource(ints = {6, 7})
  void moveBlackKnight_isNotAllowed(int args) {
    Cell from = board.findCell('b', 8);
    Cell to = board.findCell('b', args);

    Figure knight = from.figure();
    assertThat(knight.type()).isEqualTo(FigureType.KNIGHT);
    assertThat(knight.canMoveTo(from, to)).isFalse();
  }

  @ParameterizedTest
  @ValueSource(ints = {9, 10})
  void moveBlackKnight_cannotMoveIntoVoid(int args) {
    Cell from = board.findCell('b', 8);
    Cell to = board.findCell('b', args);

    Figure knight = from.figure();
    assertThat(knight.type()).isEqualTo(FigureType.KNIGHT);
    assertThat(knight.canMoveTo(from, to)).isFalse();
  }
}