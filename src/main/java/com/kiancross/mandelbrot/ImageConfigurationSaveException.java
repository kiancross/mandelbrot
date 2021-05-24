/*
 * Copyright (C) 2021 Kian Cross
 */

package mandelbrot;

/**
 * Exception thrown if there is an error saving the image configuration
 * to file.
*/
public class ImageConfigurationSaveException extends Exception {

    private final static long serialVersionUID = 1;

    /**
     * Construct the exception.
     *
     * @param e The exception that triggered this exception.
    */
    public ImageConfigurationSaveException(final Throwable e) {
      super(e);
    }
}

