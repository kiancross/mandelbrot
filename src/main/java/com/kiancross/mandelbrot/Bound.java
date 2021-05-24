/*
 * Copyright (C) 2021 Kian Cross
 */

package com.kiancross.mandelbrot;

import java.io.Serializable;

/**
 * Represents a bound of two complex numbers: a minimum and a maximum.
 */
public class Bound implements Serializable {

  private static final long serialVersionUID = 1;

  /**
   * The minimum value.
   */
  private final ComplexNumber minimum;

  /**
   * The maximum value.
   */
  private final ComplexNumber maximum;

  /**
   * Constructor to create a bound.
   *
   * @param minimum The minimum value.
   * @param maximum The maximum value.
   */
  public Bound(final ComplexNumber minimum, final ComplexNumber maximum) {

    if (minimum == null || maximum == null) {
      throw new IllegalArgumentException("minimum and maximum must be non-null parameters.");
    }

    this.minimum = minimum;
    this.maximum = maximum;
  }

  /**
   * Get the minimum value.
   *
   * @return The minimum value.
   */
  public ComplexNumber getMinimum() {
    return minimum;
  }

  /**
   * Get the maximum value.
   *
   * @return The maximum value.
   */
  public ComplexNumber getMaximum() {
    return maximum;
  }

  /**
   * Get the range of the minimum and maximum.
   */
  public ComplexNumber getRange() {
    return maximum.minus(minimum);
  }

  @Override
  public boolean equals(final Object o) {

    if (!(o instanceof Bound)) {
      return false;
    }

    final Bound b = (Bound) o;

    return minimum.equals(b.getMinimum()) && maximum.equals(b.getMaximum());
  }

  @Override
  public int hashCode() {
    int result = 17;

    result = 31 * result + minimum.hashCode();
    result = 31 * result + minimum.hashCode();

    return result;
  }

  @Override
  public String toString() {
    return String.format("Min: %s, Max: %s", minimum.toString(), maximum.toString());
  }
}
