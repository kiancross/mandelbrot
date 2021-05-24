/*
 * Copyright (C) 2021 Kian Cross
 */

package typedtextfield;

/**
 * Interface to define a validator for a typed text field.
*/
public interface IValidator<T> {

  /**
   * Method which when called should check if a value is valid.
   *
   * @param value The value to be checked.
   * @return Whether the value is valid.
  */
  public boolean validate(T value);
}
