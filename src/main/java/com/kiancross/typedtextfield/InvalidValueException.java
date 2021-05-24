/*
 * Copyright (C) 2021 Kian Cross
 */

package typedtextfield;

/**
 * Exception thrown if a given value for one of the text fields is invalid.
*/
class InvalidValueException extends Exception {

  private static final long serialVersionUID = 1;
  
  /**
   * Constructor for exception.
  */
  public InvalidValueException() {
    super();
  }

  /**
   * Constructor for exception.
   *
   * @param e The exception that triggered this exception.
  */
  public InvalidValueException(final Throwable e) {
    super(e);
  }
}
