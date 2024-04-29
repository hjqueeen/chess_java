package hwr.oop.chess.application;

import hwr.oop.chess.application.figures.Figure;
import hwr.oop.chess.application.figures.FigureColor;

import java.util.ArrayList;

public class Cell {
  private Figure figure;
  private Cell topCell;
  private Cell bottomCell;
  private Cell leftCell;
  private Cell rightCell;
  private Cell topLeftCell;
  private Cell topRightCell;
  private Cell bottomLeftCell;
  private Cell bottomRightCell;

  private final int y;
  private final int x;

  public Cell(int x, int y) {
    if (x < 1 || x > 8 || y < 1 || y > 8) {
      throw new IllegalArgumentException("Invalid Position");
    }

    this.x = x;
    this.y = y;
  }

  public Cell(char x, int y) {
    this(x - 96, y);
  }

  public boolean isValidCoordinate(int c) {
    return c >= 1 && c <= 8;
  }

  public boolean isInvalidCoordinate(int c) {
    return !isValidCoordinate(c);
  }

  public boolean isValidCoordinate(int x, int y) {
    return isValidCoordinate(x) && isValidCoordinate(y);
  }

  public boolean isInvalidCoordinate(int x, int y) {
    return !isValidCoordinate(x, y);
  }

  public boolean isChecked(Cell current, FigureColor myColor) {
    return false;
  }

  // Method to set the figure
  public void setFigure(Figure figure) {
    this.figure = figure;
  }

  // Method to get the figure
  public Figure figure() {
    return figure;
  }

  // Methods to set adjacent positions
  public void setTopCell(Cell position) {
    this.topCell = position;
  }

  public void setBottomCell(Cell position) {
    this.bottomCell = position;
  }

  public void setLeftCell(Cell position) {
    this.leftCell = position;
  }

  public void setRightCell(Cell position) {
    this.rightCell = position;
  }

  public void setTopLeftCell(Cell position) {
    this.topLeftCell = position;
  }

  public void setTopRightCell(Cell position) {
    this.topRightCell = position;
  }

  public void setBottomLeftCell(Cell position) {
    this.bottomLeftCell = position;
  }

  public void setBottomRightCell(Cell position) {
    this.bottomRightCell = position;
  }

  // Methods to get adjacent positions
  public Cell topCell() {
    return topCell;
  }

  public Cell bottomCell() {
    return bottomCell;
  }

  public Cell leftCell() {
    return leftCell;
  }

  public Cell rightCell() {
    return rightCell;
  }

  public Cell topLeftCell() {
    return topLeftCell;
  }

  public Cell topRightCell() {
    return topRightCell;
  }

  public Cell bottomLeftCell() {
    return bottomLeftCell;
  }

  public Cell bottomRightCell() {
    return bottomRightCell;
  }

  // Method to return the index of the column to which the position belongs
  public int x() {
    return x;
  }

  // Method to return the index of the row to which the position belongs
  public int y() {
    return y;
  }

  // Method to return all positions in the row to which this position belongs
  //  public List<Cell> getCellsInRow() {
  //    ArrayList<Cell> cells = this.allCells();
  //    cells.removeIf(cell -> cell.y() != this.y());
  //    return cells;
  //  }

  // Method to return all positions in the column to which this position belongs
  //  public List<Cell> getCellsInColumn() {
  //    ArrayList<Cell> cells = Board.allCells();
  //    cells.removeIf(cell -> cell.x() != this.x());
  //    return cells;
  //  }

  public Cell cellInDirection(CellDirection direction) {
    return switch (direction) {
      case LEFT -> leftCell();
      case RIGHT -> rightCell();
      case TOP -> topCell();
      case TOP_LEFT -> topLeftCell();
      case TOP_RIGHT -> topRightCell();
      case BOTTOM -> bottomCell();
      case BOTTOM_LEFT -> bottomLeftCell();
      case BOTTOM_RIGHT -> bottomRightCell();
    };
  }

  public void addAvailableCellsInDirectionToList(ArrayList<Cell> list, CellDirection direction) {
    Cell current = this;
    while ((current = current.cellInDirection(direction)) != null) {
      boolean cellIsEmpty = current.figure() == null;
      boolean enemyIsOnField = current.figure().color() != figure().color();

      if (cellIsEmpty || enemyIsOnField) {
        list.add(current);
      }
      if (!cellIsEmpty) {
        break;
      }
    }
  }

  public boolean isEqualTo(Cell pos1) {
    Cell pos2 = this;
    return (pos1.x() == pos2.x()) && (pos1.y() == pos2.y());
  }

  public void connectTo(Cell anotherCell) {
    if (anotherCell == null) {
      return;
    }

    Cell currentCell = this;
    // -1 if anotherCell is to the left
    // 1 if anotherCell is to the right
    int diffX = anotherCell.x() - x;

    // -1 if anotherCell is below
    // 1 if anotherCell is above
    int diffY = anotherCell.y() - y;

    // do not connect edges
    /*if (isInvalidCoordinate(diffX + x, diffX + y)) {
      throw new IllegalArgumentException("The cell would be outside of the gameboard");
    }*/

    final String notNeighboursError = "The cells are not neighbours to each other";

    switch (diffX) {
      case 0 -> {
        // anotherCell is above or below currentCell
        switch (diffY) {
          case 1 -> setTopCell(anotherCell);
          case 0 -> throw new IllegalArgumentException("The cells are identical");
          case -1 -> setBottomCell(currentCell);
          default -> throw new IllegalArgumentException(notNeighboursError);
        }
      }
      case 1 -> {
        // anotherCell is right of currentCell
        switch (diffY) {
          case 1 -> setTopRightCell(currentCell);
          case 0 -> setRightCell(currentCell);
          case -1 -> setBottomRightCell(currentCell);
          default -> throw new IllegalArgumentException(notNeighboursError);
        }
      }
      case -1 -> {
        // anotherCell is left of currentCell
        switch (diffY) {
          case 1 -> setTopLeftCell(currentCell);
          case 0 -> setLeftCell(currentCell);
          case -1 -> setBottomLeftCell(currentCell);
          default -> throw new IllegalArgumentException(notNeighboursError);
        }
      }
    }
  }
}
