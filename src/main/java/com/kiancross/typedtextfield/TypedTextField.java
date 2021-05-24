/*
 * Copyright (C) 2021 Kian Cross
 */

package com.kiancross.typedtextfield;

import java.util.ArrayList;
import java.util.List;
import javafx.application.Platform;
import javafx.beans.value.WritableValue;
import javafx.scene.control.TextField;


/**
 * A text field used to store a specific type. The text field is validated to only accept this type.
 *
 * @param <T> The type being stored by this text field.
 *
 * @param <S> The type of the property used to store the value of this text field.
 */
public abstract class TypedTextField<T, S extends WritableValue<? super T>> extends TextField {

  /**
   * Gets the typed value from a string.
   *
   * @param stringValue A string representing the typed value.
   * @return The typed value converted from the string.
   * @throws InvalidValueException If the string could not be converted to the typed value.
   */
  protected abstract T getTypedValueFromString(String stringValue) throws InvalidValueException;

  /**
   * Gets the default value for the text field.
   *
   * @return The default value for the text field.
   */
  protected abstract T getDefaultTypedValue();

  /**
   * Get an instance of the property used to store the value of the text field.
   */
  protected abstract S getProperty();

  /**
   * Get the typed value from the property. Unfortunately this can't be done from inside this
   * abstract class, as the return value from WritableValue.getValue is not of type T, therefore the
   * class extending this class must implement it, ensuring a value of type T is returned.
   */
  protected abstract T getTypedValue();

  /**
   * The property used to store the typed value of the text field.
   */
  private final S typedProperty;

  /**
   * A list of callbacks that are executed to check if a value is valid.
   */
  private final List<Validator<T>> validatorCallbacks = new ArrayList<Validator<T>>();

  /**
   * Construct a typed text field.
   */
  public TypedTextField() {
    super();

    typedProperty = getProperty();

    addValueChangeListener();
    addFocusedChangeListener();

    // Set the text in the text field to the default value.
    setText(getDefaultTypedValue().toString());
  }

  /**
   * Method to call the validators to check if a value is valid.
   *
   * @param value The value to check.
   * @return Whether the value is valid.
   */
  private boolean isTypedValueValid(final T value) {

    boolean valid = true;

    for (Validator<T> validator : validatorCallbacks) {
      valid = valid && validator.validate(value);
    }

    return valid;
  }

  /**
   * Add a change listener to the value of the text field.
   */
  private void addValueChangeListener() {

    // Called whenever the value changes.
    textProperty().addListener((o, oldValue, newValue) -> {
      try {

        // Use the default value if the text field is empty.
        if (newValue.equals("")) {

          typedProperty.setValue(getDefaultTypedValue());

          // Otherwise convert the given value to the typed value.
        } else {

          final T typedValue = getTypedValueFromString(newValue);

          // Check if the value is valid by any validators that have been
          // added.
          if (isTypedValueValid(typedValue)) {
            typedProperty.setValue(getTypedValueFromString(newValue));

          } else {
            throw new InvalidValueException();
          }
        }

        // If the string value couldn't be converted to a typed value then just
        // use the old value.
      } catch (InvalidValueException e) {

        // https://bugs.openjdk.java.net/browse/JDK-8081700
        // Using runLater as recommended in this bug report - you can't use setText
        // inside an addListener for a text change.
        Platform.runLater(() -> setText(oldValue));
      }
    });
  }

  /**
   * Add a change listener to the focused property of the text field.
   */
  private void addFocusedChangeListener() {

    // Called whenever focus inside the text field changes.
    focusedProperty().addListener((a, b, newFocus) -> {

      // If not focused.
      if (!newFocus) {

        final String stringValue = getText();
        final T defaultTypedValue = getDefaultTypedValue();

        // If value of text field is empty, then set the value to the default value.
        // Note that this will call the changeListener for the text property.
        if (stringValue.equals("")) {

          setText(defaultTypedValue.toString());

        } else {
          try {

            // Get the typed value. Not this should never fail due to the validation above.
            final T typedValue = getTypedValueFromString(stringValue);

            // Ensures the string representation for the default value is always
            // the value displayed in the text field when the default value is set.
            if (typedValue.equals(defaultTypedValue)) {
              setText(defaultTypedValue.toString());
            }

          } catch (InvalidValueException e) {
            throw new RuntimeException(e);
          }
        }
      }
    });
  }

  /**
   * Returned the typed property.
   *
   * @return The typed property.
   */
  public S typedProperty() {
    return typedProperty;
  }

  /**
   * Set the typed value. Note that we can do this inside the abstract class, unlike
   * {@link com.kiancross.typedtextfield.TypedTextField#getTypedValue}.
   */
  public void setTypedValue(final T value) {
    setText(value.toString());
  }

  /**
   * Add a validator.
   *
   * @param validator The validator to add.
   */
  public void addValidator(final Validator<T> validator) {

    if (validator == null) {
      throw new IllegalArgumentException("validator must be non-null");
    }

    validatorCallbacks.add(validator);
  }
}
