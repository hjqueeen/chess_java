package hwr.oop.chess.persistence;

import hwr.oop.chess.application.Board;
import hwr.oop.chess.application.Cell;
import hwr.oop.chess.application.Coordinate;
import hwr.oop.chess.application.figures.*;

import java.util.*;
import java.util.function.Supplier;
import java.util.regex.Pattern;

public class FenNotation {
  private final Board board;
  private FigureColor turn;
  private int halfMove;
  private int fullMove;

  private FenNotation(Board board) {
    this.board = board;
  }

  public static void parseFEN(Board board, String fenString) {
    if(!isValidFEN(fenString)){
      throw new IllegalArgumentException("This is an invalid FEN string!");
    }

    List<String> parts = List.of(fenString.split(" "));
    FenNotation fen = new FenNotation(board);
    fen.parsePiecePlacement(parts.getFirst());
    fen.parseTurn(parts.get(1));
    fen.parseCastlingForRook(parts.get(2));
    fen.parseCastlingForKing(parts.get(2));
    fen.parseEnPassant(parts.get(3));
    fen.parseHalfMove(parts.get(4));
    fen.parseFullMove(parts.getLast());

    board.initializeWith(fen.turn, fen.halfMove, fen.fullMove);
  }

  public static boolean isValidFEN(String fen) {
    String FEN_PATTERN = "^([rnbqkpRNBQKP1-8]{1,8}/){7}[rnbqkpRNBQKP1-8]{1,8} [wb] ([KQkq]{1,4}|-) ([a-h][36]|-) (\\d+) (\\d+)$";

    // Check if the FEN matches the regular expression pattern
    if (!Pattern.matches(FEN_PATTERN, fen)) {
      return false;
    }

    // Validate the first part of the FEN (the board state)
    List<String> parts = List.of(fen.split(" "));
    List<String> rows =  List.of(parts.getFirst().split("/"));            ;

    for (String row : rows) {
      if (!isValidRow(row)) {
        return false;
      }
    }

    // Validate castling availability
    String castling = parts.get(2);
    if (!isValidCastling(castling, rows)) {
      return false;
    }

    return true;
  }

  private static boolean isValidRow(String row) {
    int count = 0;
    for (char c : row.toCharArray()) {
      if (Character.isDigit(c)) {
        count += c - '0';  // If it's a digit, add that many empty squares
      } else {
        count++;
      }
    }
    return count == 8;  // Each row must have exactly 8 squares
  }

  private static boolean isValidCastling(String castling, List<String> rows) {
    boolean whiteKingAtInitial = checkPieceAtPosition(rows.get(7), 'K', 4);
    boolean blackKingAtInitial = checkPieceAtPosition(rows.get(0), 'k', 4);
    boolean whiteRookAtInitialKingSide = checkPieceAtPosition(rows.get(7), 'R', 7);
    boolean whiteRookAtInitialQueenSide = checkPieceAtPosition(rows.get(7), 'R', 0);
    boolean blackRookAtInitialKingSide = checkPieceAtPosition(rows.get(0), 'r', 7);
    boolean blackRookAtInitialQueenSide = checkPieceAtPosition(rows.get(0), 'r', 0);

    // Define lambdas for each castling condition
    Map<Character, Supplier<Boolean>> castlingChecks = new HashMap<>();
    castlingChecks.put('K', () -> whiteKingAtInitial && whiteRookAtInitialKingSide);
    castlingChecks.put('Q', () -> whiteKingAtInitial && whiteRookAtInitialQueenSide);
    castlingChecks.put('k', () -> blackKingAtInitial && blackRookAtInitialKingSide);
    castlingChecks.put('q', () -> blackKingAtInitial && blackRookAtInitialQueenSide);

    for (char c : castling.toCharArray()) {
      if (c == '-') {
        continue; // No castling available, this is always valid
      }
      Supplier<Boolean> check = castlingChecks.get(c);
      if (check == null || !check.get()) {
        return false;
      }
    }

    return true;
  }

  private static boolean checkPieceAtPosition(String row, char piece, int position) {
    int col = 0;
    for (char c : row.toCharArray()) {
      if (Character.isDigit(c)) {
        col += c - '0';
      } else {
        if (c == piece && col == position) {
          return true;
        }
        col++;
      }
    }
    return false;
  }

  public static String generateFen(Board board) {
    FenNotation fen = new FenNotation(board);
    StringJoiner joiner = new StringJoiner(" ");

    joiner.add(fen.generatePiecePlacement());
    joiner.add(fen.generateTurn());
    joiner.add(fen.generateCastling());
    joiner.add(fen.generateEnPassant());
    joiner.add(fen.generateHalfMove());
    joiner.add(fen.generateFullMove());

    return joiner.toString();
  }

  public static String extractFenKeyParts(String fen) {
    String[] parts = fen.split(" ");
    return parts[0] + " " + parts[2] + " " + parts[3];
  }

  private void parsePiecePlacement(String pieces) {
    List<Cell> allCells = board.allCells();
    List<String> rows = List.of(pieces.split("/"));
    for (String row : rows) {
      for (char c : row.toCharArray()) {
        if (Character.isDigit(c)) {
          int emptySpaces = Character.getNumericValue(c);
          for (int i = 0; i < emptySpaces; i++) {
            Cell cell = allCells.removeFirst();
            cell.setFigure(null);
          }
          continue;
        }

        Cell cell = allCells.removeFirst();
        cell.setFigure(Figure.fromChar(c));
      }
    }
  }

  private String generatePiecePlacement() {
    StringJoiner rows = new StringJoiner("/");
    StringBuilder row = new StringBuilder();
    int emptyCount = 0;

    List<Cell> allCells = board.allCells();
    // Sort only Y position
    allCells.sort(
        (c1, c2) -> {
          if (c1.y() != c2.y()) {
            return Integer.compare(c1.y().toInt(), c2.y().toInt());
          }
          return 0;
        });

    for (Cell cell : allCells) {
      if (cell.isFree()) {
        emptyCount++;
      }

      if (emptyCount > 0 && (cell.isOccupied() || !cell.hasRightCell())) {
        row.append(emptyCount);
        emptyCount = 0;
      }

      if (cell.isOccupied()) {
        row.append(cell.figure().symbol());
      }

      if (!cell.hasRightCell()) {
        rows.add(row.toString());
        row.setLength(0);
      }
    }
    return String.join("/", Arrays.asList(rows.toString().split("/")).reversed());
  }

  private void parseTurn(String turn) {
    this.turn = turn.charAt(0) == 'w' ? FigureColor.WHITE : FigureColor.BLACK;
  }

  private String generateTurn() {
    return board.turn().equals(FigureColor.WHITE) ? "w" : "b";
  }

  private void parseCastlingForRook(String castling) {
    for (char c : List.of('Q', 'K', 'q', 'k')) {
      if (castling.indexOf(c) > -1) {
        continue;
      }
      FigureColor color = Character.isUpperCase(c) ? FigureColor.WHITE : FigureColor.BLACK;
      Coordinate x = Character.toLowerCase(c) == 'q' ? Coordinate.ONE : Coordinate.EIGHT;
      Coordinate y = Character.isLowerCase(c) ? Coordinate.EIGHT : Coordinate.ONE;

      Cell rookCell = board.findCell(x, y);
      if (rookCell.isOccupiedBy(color, FigureType.ROOK)) {
        ((Rook) rookCell.figure()).figureMoved();
      }
    }
  }

  private void parseCastlingForKing(String castling) {
    if (!castling.contains("Q") && !castling.contains("K")) {
      Cell kingCell = board.findKing(FigureColor.WHITE);
      ((King) kingCell.figure()).figureMoved();
    }

    if (!castling.contains("q") && !castling.contains("k")) {
      Cell kingCell = board.findKing(FigureColor.BLACK);
      ((King) kingCell.figure()).figureMoved();
    }
  }

  private String generateCastling() {
    StringBuilder castling = new StringBuilder();
    for (char c : List.of('K', 'Q', 'k', 'q')) {
      FigureColor color = Character.isUpperCase(c) ? FigureColor.WHITE : FigureColor.BLACK;
      Cell kingCell = board.findKing(color);
      King king = (King) kingCell.figure();
      if (king.hasMoved()) {
        continue;
      }

      Coordinate x = Character.toLowerCase(c) == 'q' ? Coordinate.ONE : Coordinate.EIGHT;
      Coordinate y = Character.isLowerCase(c) ? Coordinate.EIGHT : Coordinate.ONE;
      Cell rookCell = board.findCell(x, y);
      if (rookCell.isOccupiedBy(color, FigureType.ROOK) && !((Rook) rookCell.figure()).hasMoved()) {
        castling.append(c);
      }
    }
    return castling.isEmpty() ? "-" : castling.toString();
  }

  private void parseEnPassant(String enPassantStr) {
    if (!enPassantStr.equals("-")) {
      board.findCell(enPassantStr).setIsEnPassant(true);
    }
  }

  private String generateEnPassant() {
    for (Cell cell : board.allCells()) {
      if (cell.isEnPassant()) {
        return cell.toCoordinates().toLowerCase();
      }
    }
    return "-";
  }

  private void parseHalfMove(String halfMove) {
    this.halfMove = Integer.parseInt(halfMove);
  }

  private String generateHalfMove() {
    return String.valueOf(board.halfMove());
  }

  private void parseFullMove(String fullMove) {
    this.fullMove = Integer.parseInt(fullMove);
  }

  private String generateFullMove() {
    return String.valueOf(board.fullMove());
  }
}
