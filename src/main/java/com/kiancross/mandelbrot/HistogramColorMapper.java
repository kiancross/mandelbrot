/*
 * Copyright (C) 2021 Kian Cross
 */

package com.kiancross.mandelbrot;

import javafx.scene.paint.Color;

/**
 * An implementation of the algorithm described here:
 * https://en.wikipedia.org/wiki/Mandelbrot_set#Histogram_coloring
 */
public class HistogramColorMapper {

  /**
   * The maximum number of iterations.
   */
  private int maximumIterations;

  /**
   * The gradient used to calculate the colours.
   */
  private ColorGradient gradient;

  /**
   * Construct the histogram mapper.
   *
   * @param maximumIterations The maximum number of iterations that will appear in the list of
   *        iterations.
   *
   * @param gradient The gradient used to calculate the colours.
   */
  public HistogramColorMapper(final int maximumIterations, final ColorGradient gradient) {
    this.maximumIterations = maximumIterations;
    this.gradient = gradient;
  }

  /**
   * Takes an array of iteration values and returns an array where each position corresponds to an
   * iteration value, and the value at this position is the number of times the iteration value
   * occurred.
   *
   * @param iterationValues The iteration values.
   * @return Number of times each iteration value occurred.
   */
  private int[] getIterationsCount(final int[][] iterationValues) {

    final int[] iterationsCount = new int[maximumIterations];

    final int xLength = iterationValues.length;
    final int yLength = iterationValues[0].length;

    for (int x = 0; x < xLength; x++) {
      for (int y = 0; y < yLength; y++) {

        if (iterationValues[x][y] > 0) {
          iterationsCount[iterationValues[x][y] - 1]++;
        }
      }
    }

    return iterationsCount;
  }

  /**
   * Takes an array and sums all the elements in it.
   *
   * @param array The array to sum.
   * @return The sum of the items in the array.
   */
  private int sumArray(final int[] array) {

    int total = 0;

    for (int i = 0; i < array.length; i++) {
      total += array[i];
    }

    return total;
  }

  /**
   * Takes an array of iteration values and maps each of these values to a colour.
   * 
   * @param iterationValues The iteration values.
   * @return 2D array where each position in iterationValues corresponds to a colour.
   */
  public Color[][] mapIterationsToColors(final int[][] iterationValues) {

    final int[] iterationsCount = getIterationsCount(iterationValues);
    final int total = sumArray(iterationsCount);

    final int xLength = iterationValues.length;
    final int yLength = iterationValues[0].length;

    final Color[][] colorMap = new Color[xLength][yLength];

    for (int x = 0; x < xLength; x++) {
      for (int y = 0; y < yLength; y++) {

        final int iterations = iterationValues[x][y];

        // If the iteration limit was exceeded then the value will be -1 (indicating that the
        // value was inside the set up to the maximum iteration value). In this case the colour
        // should be black.
        if (iterations == -1) {

          colorMap[x][y] = Color.BLACK;

        } else {

          double factor = 0;

          for (int i = 0; i < iterations; i++) {
            factor += iterationsCount[i];
          }

          factor /= (double) total;

          colorMap[x][y] = gradient.getColor(factor);
        }
      }
    }

    return colorMap;
  }
}
