/*
 * Copyright (C) 2021 Kian Cross
 */

package typedtextfield;

import javafx.scene.control.TextField;
import javafx.beans.property.SimpleDoubleProperty;

/**
 * Text field for a double value.
*/
public class DoubleTextField extends TypedTextField<Double, SimpleDoubleProperty> {

  /**
   * The default value of the text field.
  */
  final private double defaultValue;

  /**
   * Constructor for the text field.
   *
   * @param defaultValue The default value of the text field.
  */
  public DoubleTextField(final double defaultValue) {
    this.defaultValue = defaultValue;
  }

  @Override
  protected Double getTypedValueFromString(
    final String stringValue
  ) throws InvalidValueException {
  
    try {

      // If a minus sign is entered, return the default value.
      if (stringValue.equals("-")) {

        return getDefaultTypedValue();

      } else {

        return Double.parseDouble(stringValue);
      }

    } catch (NumberFormatException e) {
      throw new InvalidValueException(e);
    }
  }

  @Override
  protected Double getDefaultTypedValue() {
    return Double.valueOf(defaultValue);
  }

  @Override
  protected SimpleDoubleProperty getProperty() {
    return new SimpleDoubleProperty();
  }  

  @Override
  public Double getTypedValue() {
    return typedProperty().getValue();
  }
}
