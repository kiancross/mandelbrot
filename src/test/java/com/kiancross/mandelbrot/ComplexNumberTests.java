/*
 * Copyright (C) 2021 Kian Cross
 */

package mandelbrot;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

/**
 * Tests for the complex number class.
*/
public class ComplexNumberTests {

  /**
   * Test getting the real part works.
  */
  @Test
  public void getReal() {
    final ComplexNumber c = new ComplexNumber(1, 2);
    assertEquals(1, c.getReal());
  }

  /**
   * Test getting the imaginary part works.
  */
  @Test
  public void getImaginary() {
    final ComplexNumber c = new ComplexNumber(1, 2);
    assertEquals(2, c.getImaginary());
  }

  /**
   * Test calculating the modulus works.
  */
  @Test
  public void getModulus() {
    final ComplexNumber c = new ComplexNumber(3, 4);
    assertEquals(5, c.getModulus());
  }
  
  /**
   * Test getting the absolute square works.
  */
  @Test
  public void getAbsoluteSquare() {
    final ComplexNumber c = new ComplexNumber(3, 4);
    assertEquals(25, c.getAbsoluteSquare());
  }

  /**
   * Test getting the square works.
  */
  @Test
  public void testSquare() {
    final ComplexNumber c = new ComplexNumber(3, 4);
    assertEquals(new ComplexNumber(-7, 24), c.square());
  }
  
  /**
   * Test multiplying two complex numbers together works.
  */
  @Test
  public void testMultiplyByComplex() {
    final ComplexNumber c1 = new ComplexNumber(3, 4);
    final ComplexNumber c2 = new ComplexNumber(7, 8);
    assertEquals(new ComplexNumber(-11, 52), c1.multiply(c2));
  }
  
  /**
   * Test multiplying a complex number with an all zero complex number
   * works.
  */
  @Test
  public void testMultiplyByComplexZero() {
    final ComplexNumber c1 = new ComplexNumber(0, 0);
    final ComplexNumber c2 = new ComplexNumber(7, 8);
    assertEquals(new ComplexNumber(0, 0), c1.multiply(c2));
  }
  
  /**
   * Test multiplying a complex number by a scalar works.
  */
  @Test
  public void testMultiplyByScalar() {
    final ComplexNumber c = new ComplexNumber(3, 4);

    assertEquals(new ComplexNumber(15, 20), c.multiply(5));
  }
  
  /**
   * Test multiplying a complex number by 0 works.
  */
  @Test
  public void testMultiplyByScalarZero() {
    final ComplexNumber c = new ComplexNumber(3, 4);

    assertEquals(new ComplexNumber(0, 0), c.multiply(0));
  }
  
  /**
   * Test adding two complex numbers works.
  */
  @Test
  public void testAdd() {
    final ComplexNumber c1 = new ComplexNumber(3, 4);
    final ComplexNumber c2 = new ComplexNumber(7, 8);

    assertEquals(new ComplexNumber(10, 12), c1.add(c2));
  }
  
  /**
   * Test adding a zero complex number to another complex number.
  */
  @Test
  public void testAddZerno() {
    final ComplexNumber c1 = new ComplexNumber(0, 0);
    final ComplexNumber c2 = new ComplexNumber(7, 8);

    assertEquals(new ComplexNumber(7, 8), c1.add(c2));
  }
  
  /**
   * Test subtracting two complex numbers works.
  */
  @Test
  public void testMinus() {
    final ComplexNumber c1 = new ComplexNumber(3, 4);
    final ComplexNumber c2 = new ComplexNumber(7, 8);

    assertEquals(new ComplexNumber(-4, -4), c1.minus(c2));
  }
  
  /**
   * Test that two equal complex numbers return true from the
   * equals method.
  */
  @Test
  public void testIsEquals() {
    final ComplexNumber c1 = new ComplexNumber(7, 8);
    final ComplexNumber c2 = new ComplexNumber(7, 8);

    assertTrue(c1.equals(c2));
  }

  /**
   * Test that two non-equal complex numbers return false from
   * the equals method.
  */
  @Test
  public void testIsNotEquals() {
    final ComplexNumber c1 = new ComplexNumber(7, 8);
    final ComplexNumber c2 = new ComplexNumber(6, 8);

    assertFalse(c1.equals(c2));
  }
}

