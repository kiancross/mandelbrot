/*
 * Copyright (C) 2021 Kian Cross
 */

package mandelbrot;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertArrayEquals;

import org.junit.jupiter.api.Test;

import java.util.Arrays;

/**
 * Tests for the generation of the mandelbrot iteration values. These unit tests
 * are not too extensive, as the proof of it working is fairly clear in the image
 * result. It is a simple sanity test to ensure that the values at least appear
 * to be correct and that edge cases are dealt with correctly.
*/
public class MandelbrotSetIterationCountGeneratorTests {

  /**
   * Test generating values with one thread.
  */
  @Test
  public void testWithOneThread() {
    
    final MandelbrotSetIterationCountGenerator generator = new MandelbrotSetIterationCountGenerator(
      new Bound(new ComplexNumber(-2, -2), new ComplexNumber(2, 2)), 1000, 2
    );

    final int[][] iterationValues = generator.calculate(4, 4, 1);

    assertArrayEquals(new int[] {1, 1, -1, 1}, iterationValues[0]);
    assertArrayEquals(new int[] {1, 3, -1, 3}, iterationValues[1]);
    assertArrayEquals(new int[] {2, -1, -1, -1}, iterationValues[2]);
    assertArrayEquals(new int[] {1, 2, 3, 2}, iterationValues[3]);
  }
  
  /**
   * Test generating values with multiple threads.
  */
  @Test
  public void testWithMultipleThreads() {
    
    final MandelbrotSetIterationCountGenerator generator = new MandelbrotSetIterationCountGenerator(
      new Bound(new ComplexNumber(-2, -2), new ComplexNumber(2, 2)), 1000, 2
    );

    final int[][] iterationValues = generator.calculate(4, 4, 3);

    assertArrayEquals(new int[] {1, 1, -1, 1}, iterationValues[0]);
    assertArrayEquals(new int[] {1, 3, -1, 3}, iterationValues[1]);
    assertArrayEquals(new int[] {2, -1, -1, -1}, iterationValues[2]);
    assertArrayEquals(new int[] {1, 2, 3, 2}, iterationValues[3]);
  }

  /**
   * Test generating values with 0x0 resolution.
  */
  @Test
  public void testWithZeroResolution() {
    final MandelbrotSetIterationCountGenerator generator = new MandelbrotSetIterationCountGenerator(
      new Bound(new ComplexNumber(-2, -2), new ComplexNumber(2, 2)), 1000, 2
    );

    final int[][] iterationValues = generator.calculate(0, 0, 1);

    assertEquals(0, iterationValues.length);
  }

  /**
   * Test that a null bound throws an exception.
  */ 
  @Test
  public void testNullBound() {
    assertThrows(IllegalArgumentException.class, () -> {
      final MandelbrotSetIterationCountGenerator generator = new MandelbrotSetIterationCountGenerator(
        null, 1000, 2
      );
    });
  }
  
  /**
   * Test that attempting to calculate values with 0 threads throws
   * an exception.
  */
  @Test
  public void testWithZeroThreads() {

    final MandelbrotSetIterationCountGenerator generator = new MandelbrotSetIterationCountGenerator(
      new Bound(new ComplexNumber(-2, -2), new ComplexNumber(2, 2)), 1000, 2
    );

    assertThrows(IllegalArgumentException.class, () -> {
      generator.calculate(10, 10, 0);
    });
  }
}
