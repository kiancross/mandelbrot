/*
 * Copyright (C) 2021 Kian Cross
 */

package com.kiancross.mandelbrot;

/**
 * Exception thrown if there is an error loading the image configuration from file.
 */
public class ImageConfigurationLoadException extends Exception {
  private static final long serialVersionUID = 1;

  /**
   * Construct the exception.
   *
   * @param e The exception that triggered this exception.
   */
  public ImageConfigurationLoadException(final Throwable e) {
    super(e);
  }
}
