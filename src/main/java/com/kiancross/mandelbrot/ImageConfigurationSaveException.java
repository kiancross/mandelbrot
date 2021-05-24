/*
 * Copyright (C) 2021 Kian Cross
 */

package com.kiancross.mandelbrot;

/**
 * Exception thrown if there is an error saving the image configuration to file.
 */
public class ImageConfigurationSaveException extends Exception {
  private static final long serialVersionUID = 1;

  /**
   * Construct the exception.
   *
   * @param e The exception that triggered this exception.
   */
  public ImageConfigurationSaveException(final Throwable e) {
    super(e);
  }
}

