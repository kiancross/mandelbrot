/*
 * Copyright (C) 2021 Kian Cross
 */

package mandelbrot;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

// https://www.oracle.com/java/technologies/javase-downloads.html

/**
 * Tests for the bound class.
*/
public class BoundTests {

  /**
   * Test getting the minimum value.
  */
  @Test
  public void testGetMinimum() {
    
    final ComplexNumber minimum = new ComplexNumber(1, 1);
    final ComplexNumber maximum = new ComplexNumber(2, 2);

    final Bound bound = new Bound(minimum, maximum);

    assertEquals(minimum, bound.getMinimum());
  }

  /**
   * Test getting the maximum value.
  */
  @Test
  public void testGetMaximum() {
    
    final ComplexNumber minimum = new ComplexNumber(1, 1);
    final ComplexNumber maximum = new ComplexNumber(2, 2);

    final Bound bound = new Bound(minimum, maximum);

    assertEquals(maximum, bound.getMaximum());
  }

  /**
   * Test passing null as the minimum value throws an exception.
  */
  @Test
  public void testNullMinimumThrows() {
    assertThrows(IllegalArgumentException.class, () -> {
      new Bound(null, new ComplexNumber(2, 2));
    });
  }

  /**
   * Test passing null as the maximum value throws an exception.
  */
  @Test
  public void testNullMaximumThrows() {
    assertThrows(IllegalArgumentException.class, () -> {
      new Bound(new ComplexNumber(1, 1), null);
    });
  }
  
  /**
   * Test that the getRange method works.
  */
  @Test
  public void testGetRange() {
    final ComplexNumber minimum = new ComplexNumber(1, 1);
    final ComplexNumber maximum = new ComplexNumber(2, 2);

    final Bound bound = new Bound(minimum, maximum);

    assertEquals(maximum.minus(minimum), bound.getRange());
  }
  
  /**
   * Test the isEquals implementation with two instances that should be
   * equal.
  */
  @Test
  public void testIsEquals() {
    final ComplexNumber minimum1 = new ComplexNumber(1, 1);
    final ComplexNumber maximum1 = new ComplexNumber(2, 2);

    final ComplexNumber minimum2 = new ComplexNumber(1, 1);
    final ComplexNumber maximum2 = new ComplexNumber(2, 2);

    final Bound bound1 = new Bound(minimum1, maximum1);
    final Bound bound2 = new Bound(minimum2, maximum2);

    assertEquals(bound1, bound2);
  }
  
  /**
   * Test the isEquals implementation with two instances that should not be
   * equal.
  */
  @Test
  public void testIsNotEquals() {
    final ComplexNumber minimum1 = new ComplexNumber(1, 1);
    final ComplexNumber maximum1 = new ComplexNumber(2, 2);

    final ComplexNumber minimum2 = new ComplexNumber(1, 1);
    final ComplexNumber maximum2 = new ComplexNumber(2, 3);

    final Bound bound1 = new Bound(minimum1, maximum1);
    final Bound bound2 = new Bound(minimum2, maximum2);

    assertNotEquals(bound1, bound2);
  }
}
