package hwr.oop.chess.application.figures;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;

class FigureColorTest {
  @Test
  void testEnumValues() { // Test if the enum contains the valid values
    assertEquals(2, FigureColor.values().length); // Check if there are exactly 2 values
    assertEquals(FigureColor.WHITE, FigureColor.valueOf("WHITE"));
    assertEquals(FigureColor.BLACK, FigureColor.valueOf("BLACK"));
  }
}
