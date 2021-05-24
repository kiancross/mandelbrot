/*
 * Copyright (C) 2021 Kian Cross
 */

package com.kiancross.mandelbrot;

/**
 * Implements the mandelbrot set calculations.
 */
public class MandelbrotSetIterationCountGenerator {

  /**
   * Bound representing the minimum and maximum complex number to have iteration values calculated
   * for.
   */
  final Bound bound;

  /**
   * Maximum number of iterations before assuming the complex number lies within the mandelbrot set.
   */
  final int maximumIterations;

  /**
   * The escape radius to use when calculating the iteration values.
   */
  final double escapeRadius;

  /**
   * Constructor for the generator.
   *
   * @param bound Bound representing the minimum and maximum complex number to have iteration values
   *        calculated for.
   *
   * @param maximumIterations Maximum number of iterations before assuming the complex number lies
   *        within the mandelbrot set.
   *
   * @param escapeRadius The escape radius to use when calculating the iteration values.
   */
  public MandelbrotSetIterationCountGenerator(final Bound bound, final int maximumIterations,
      final double escapeRadius) {

    if (bound == null) {
      throw new IllegalArgumentException("bound must not be null");
    }

    this.bound = bound;
    this.maximumIterations = maximumIterations;
    this.escapeRadius = escapeRadius;
  }

  /**
   * Calculate the number of iterations required for the given complex number to exceed the escape
   * radius.
   *
   * @param c The complex number to be used in the mandelbrot iterative calculation.
   *
   * @param The number of iterations taken to exceed the radius, or -1 if the maximum number of
   *        iterations was reached.
   */
  private int calculateIterations(final ComplexNumber c) {

    ComplexNumber z = new ComplexNumber(0, 0);

    for (int i = 1; i <= maximumIterations; i++) {

      z = z.square().add(c);

      if (z.getAbsoluteSquare() > (escapeRadius * escapeRadius)) {
        return i;
      }
    }

    return -1;
  }

  /**
   * Gets a subtask of the calculation which can be used to run the calculation in multiple threads.
   *
   * @param set The array to place the results.
   * @param totalThreads The total number of threads being used.
   * @param threadNumber The thread number of this subtask.
   * @param xresolution The resolution of the x-axis.
   * @param yresolution The resolution of the y-axis.
   *
   * @return Returns a runnable that can be called to execute the task.
   */
  private Runnable getSubtask(final int[][] set, final int totalThreads, final int threadNumber,
      final int xresolution, final int yresolution) {
    return new Runnable() {
      public void run() {

        final ComplexNumber minimumC = bound.getMinimum();
        final ComplexNumber maximumC = bound.getMaximum();

        final double realStepSize =
            (maximumC.getReal() - minimumC.getReal()) / (double) xresolution;

        final double imaginaryStepSize =
            (maximumC.getImaginary() - minimumC.getImaginary()) / (double) yresolution;

        // Using floor and ceil here to ensure that the full range of values are calculated. If the
        // value was simply casted to an int then round down would always occur and there would be
        // gaps in range of values each task calculates.
        final int xStart = (int) Math.floor(threadNumber * (xresolution / (double) totalThreads));
        final int xEnd =
            (int) Math.ceil((threadNumber + 1) * (xresolution / (double) totalThreads));

        for (int x = xStart; x < xEnd; x++) {

          final double realPart = minimumC.getReal() + (x * realStepSize);

          for (int y = 0; y < yresolution; y++) {

            final double imaginaryPart = minimumC.getImaginary() + (y * imaginaryStepSize);

            set[x][y] = calculateIterations(new ComplexNumber(realPart, imaginaryPart));
          }
        }
      }
    };
  }

  /**
   * Calculate the iteration values for a given resolution using a certain number of threads.
   *
   * @param xresolution The resolution of the x-axis.
   * @param yresolution The resolution of the y-axis.
   * @param numberOfThreads The number of threads to run the calculation on.
   *
   * @return A 2D array containing the iteration values. Each position in the array can be accessed
   *         as so: array[x][y].
   */
  public int[][] calculate(final int xresolution, final int yresolution,
      final int numberOfThreads) {

    if (numberOfThreads < 1) {
      throw new IllegalArgumentException("Number of threads must be greater than zero.");
    }

    final int[][] set = new int[xresolution][yresolution];
    final Thread[] tasks = new Thread[numberOfThreads];

    for (int i = 0; i < numberOfThreads; i++) {

      final Thread thread =
          new Thread(getSubtask(set, numberOfThreads, i, xresolution, yresolution));

      thread.start();

      tasks[i] = thread;
    }

    try {
      for (int i = 0; i < numberOfThreads; i++) {
        tasks[i].join();
      }
    } catch (InterruptedException e) {
      throw new RuntimeException(e);
    }

    return set;
  }
}
