/*
 * Copyright (C) 2021 Kian Cross
 */

package com.kiancross.mandelbrot;

import java.util.ArrayDeque;
import java.util.Deque;

/**
 * Used to manage image configurations - allows new configurations to be added and stores the
 * history so that actions can be undone/redone.
 */
public class ImageConfigurationManager {

  /**
   * History stack (used for undo).
   */
  private final Deque<ImageConfiguration> history = new ArrayDeque<ImageConfiguration>();

  /**
   * Future stack (used for redo).
   */
  private final Deque<ImageConfiguration> future = new ArrayDeque<ImageConfiguration>();

  /**
   * The initial configuration.
   */
  private final ImageConfiguration initialConfiguration;

  /**
   * Property used to store the current configuration - this allows other parts of the code to
   * observe this property and execute code when it changes.
   */
  private final InformInitialObjectProperty<ImageConfiguration> currentConfigurationProperty =
      new InformInitialObjectProperty<ImageConfiguration>();

  /**
   * Construct a configuration manager.
   *
   * @param initialConfiguration The initial configuration.
   */
  public ImageConfigurationManager(final ImageConfiguration initialConfiguration) {
    this.initialConfiguration = initialConfiguration;
    currentConfigurationProperty.setValue(initialConfiguration);
  }

  /**
   * Undo the last call to {@link mandelbrot.ImageConfigurationManager#addConfiguration}.
   */
  public void undo() {
    future.addFirst(history.removeFirst());
    currentConfigurationProperty.setValue(getCurrentConfiguration());
  }

  /**
   * Redo the last call to {@link mandelbrot.ImageConfigurationManager#undo}.
   */
  public void redo() {
    history.addFirst(future.removeFirst());
    currentConfigurationProperty.setValue(getCurrentConfiguration());
  }

  /**
   * Check if there are any actions to undo.
   *
   * @return If there are any actions to undo.
   */
  public boolean canUndo() {
    return !history.isEmpty();
  }

  /**
   * Check if there are any actions to redo.
   *
   * @return If there are any actions to redo.
   */
  public boolean canRedo() {
    return !future.isEmpty();
  }

  /**
   * Reset everything back to the initial configuration. This action can not be undone.
   */
  public void resetAll() {
    history.clear();
    future.clear();
    currentConfigurationProperty.setValue(getCurrentConfiguration());
  }

  /**
   * Get the initial configuration value.
   *
   * @return The initial configuration value.
   */
  public ImageConfiguration getInitialConfiguration() {
    return initialConfiguration;
  }

  /**
   * Get the configuration property.
   *
   * @return The current configuration property.
   */
  public InformInitialObjectProperty<ImageConfiguration> getCurrentConfigurationProperty() {
    return currentConfigurationProperty;
  }

  /**
   * Get the current configuration.
   *
   * @return The current configurations.
   */
  public ImageConfiguration getCurrentConfiguration() {

    // This method intentionally does not call the getValue method of the
    // property. The current configuration must be determined from the
    // data structures.
    if (history.isEmpty()) {
      return initialConfiguration;

    } else {
      return history.getFirst();
    }
  }

  /**
   * Add a new configuration. If the configuration has not changed it will not be added to the undo
   * stack.
   *
   * @param newConfiguration The new configuration to add.
   *
   * @return Boolean value indicating if the configuration was added to the undo stack.
   */
  public boolean addConfiguration(final ImageConfiguration newConfiguration) {

    if (getCurrentConfiguration().equals(newConfiguration)) {

      return false;

    } else {

      future.clear();
      history.addFirst(newConfiguration);
      currentConfigurationProperty.setValue(newConfiguration);

      return true;
    }
  }
}
