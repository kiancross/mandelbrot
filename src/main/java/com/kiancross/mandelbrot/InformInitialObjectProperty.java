/*
 * Copyright (C) 2021 Kian Cross
 */

package mandelbrot;

import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;

/**
 * This is a wrapper around the {@link javafx.beans.property.SimpleObjectProperty} class
 * that offers a slightly modified addListner implementation, that calls the callback
 * immediately with the current value.
 *
 * @param <T> The type of the object being stored in this property.
*/
public class InformInitialObjectProperty<T> extends SimpleObjectProperty<T> {

  /**
   * Add a listener, called when the value changes.
   *
   * @param listener The listener to be called.
   * @param informInitial Boolean indicating if the listener should be called immediately. If set
   * to false, this is the same as calling the method without this parameter set.
  */
  public void addListener(final ChangeListener<? super T> listener, final boolean informInitial) {

    super.addListener(listener);

    if (informInitial) {
      listener.changed(this, getValue(), getValue()) ;
    }
  }
}
