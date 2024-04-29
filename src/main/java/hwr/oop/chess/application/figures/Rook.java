package hwr.oop.chess.application.figures;

import hwr.oop.chess.application.Cell;
import hwr.oop.chess.application.CellDirection;

import java.util.ArrayList;

import static hwr.oop.chess.application.Cell.isOpponentKing;

public class Rook implements Figure {
  private static final FigureType type = FigureType.ROOK;
  private final FigureColor color;

  public Rook(FigureColor color) {
    this.color = color;
  }

  public ArrayList<Cell> getAvailableCells(Cell currentCell) {
    ArrayList<Cell> cells = new ArrayList<>();

    // Check above
    currentCell.addAvailableCellsInDirectionToList(cells, CellDirection.LEFT);
    currentCell.addAvailableCellsInDirectionToList(cells, CellDirection.RIGHT);
    currentCell.addAvailableCellsInDirectionToList(cells, CellDirection.TOP);
    currentCell.addAvailableCellsInDirectionToList(cells, CellDirection.BOTTOM);

    System.out.println("availableCell" + cells.toArray().length);
    return cells;
  }

  public boolean canMoveTo(Cell prevCell, Cell nextCell) {
    ArrayList<Cell> availableCell = getAvailableCells(prevCell);
    System.out.println("canMove: " + availableCell.contains(nextCell));
    return availableCell.contains(nextCell);
  }

  public char symbol() {
    return color == FigureColor.WHITE ? 'R' : 'r';
  }

  public FigureColor color() {
    return color;
  }

  public FigureType type() {
    return type;
  }
}
