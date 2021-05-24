/*
 * Copyright (C) 2021 Kian Cross
 */

package com.kiancross.typedtextfield;

import javafx.beans.property.SimpleIntegerProperty;

/**
 * Text field for an integer value.
 */
public class IntegerTextField extends TypedTextField<Integer, SimpleIntegerProperty> {

  /**
   * The default value of the text field.
   */
  private final int defaultValue;

  /**
   * Constructor for the text field.
   *
   * @param defaultValue The default value of the text field.
   */
  public IntegerTextField(final int defaultValue) {
    this.defaultValue = defaultValue;
  }

  @Override
  protected Integer getTypedValueFromString(final String stringValue) throws InvalidValueException {

    try {

      // If a minus sign is entered, return the default value.
      if (stringValue.equals("-")) {
        return getDefaultTypedValue();

      } else {
        return Integer.parseInt(stringValue);

      }

    } catch (NumberFormatException e) {
      throw new InvalidValueException(e);
    }
  }

  @Override
  protected Integer getDefaultTypedValue() {
    return Integer.valueOf(defaultValue);
  }

  @Override
  protected SimpleIntegerProperty getProperty() {
    return new SimpleIntegerProperty();
  }

  @Override
  public Integer getTypedValue() {
    return typedProperty().getValue();
  }
}

