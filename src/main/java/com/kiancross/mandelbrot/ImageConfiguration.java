/*
 * Copyright (C) 2021 Kian Cross
 */

package mandelbrot;

import java.io.Serializable;

/**
 * Class to represent the configuration used to display an image of the
 * mandelbrot set. An instance of the class can't be made directly - the
 * builder must be used.
*/
public class ImageConfiguration implements Serializable {

  private static final long serialVersionUID = 1;

  /**
   * Bound to use when generating the iteration values.
  */
  final private Bound bound;

  /**
   * Escape radius to use when calculating the iteration values.
  */
  final private double escapeRadius;

  /**
   * Maximum number of iterations before assuming the value of C
   * lies within the set.
  */
  final private int maximumIterations;

  /**
   * Whether the zoom amount should be overlayed onto the image.
  */
  final private boolean overlayZoom;

  /**
   * Colour theme to be used to display the image.
  */
  final private ColorGradient colorTheme;

  /**
   * A builder (using the builder design pattern) to create an
   * {@link mandelbrot.ImageConfiguration}.
  */
  public static class Builder {
  
    /**
     * Bound to use when generating the iteration values.
    */
    private Bound bound;

    /**
     * Escape radius to use when calculating the iteration values.
    */
    private double escapeRadius;

    /**
     * Maximum number of iterations before assuming the value of C
     * lies within the set.
    */
    private int maximumIterations;

    /**
     * Whether the zoom amount should be overlayed onto the image.
    */
    private boolean overlayZoom;

    /**
     * Colour theme to be used to display the image.
    */
    private ColorGradient colorTheme;

    /**
     * Use a given configuration as the base for the new configuration.
     * 
     * @param configuration The configuration to use as the base for the new configuration.
     * @return This builder.
    */
    public Builder use(final ImageConfiguration configuration) {
      this.bound = configuration.getBound();
      this.escapeRadius = configuration.getEscapeRadius();
      this.maximumIterations = configuration.getMaximumIterations();
      this.overlayZoom = configuration.getOverlayZoom();
      this.colorTheme = configuration.getColorTheme();

      return this;
    }

    /**
     * Set the bound.
     *
     * @param bound Bound to use when generating the iteration values.
     * @return This builder.
    */
    public Builder bound(final Bound bound) {
      this.bound = bound;
      return this;
    }

    /**
     * Set the escape radius.
     *
     * @param escapeRadius Escape radius to use when calculating the iteration values.
     * @return This builder.
    */
    public Builder escapeRadius(final double escapeRadius) {
      this.escapeRadius = escapeRadius;
      return this;
    }

    /**
     * Set the maximum number of iterations.
     *
     * @param maximumIterations Maximum number of iterations before assuming the value
     * of C lies within the set.
     *
     * @return This builder.
    */
    public Builder maximumIterations(final int maximumIterations) {
      this.maximumIterations = maximumIterations;
      return this;
    }

    /**
     * Set whether the zoom value should be overlayed.
     *
     * @param overlayZoom Whether the zoom should be overlayed onto the image.
     * @return This builder.
    */
    public Builder overlayZoom(final boolean overlayZoom) {
      this.overlayZoom = overlayZoom;
      return this;
    }

    /**
     * Set the colour theme.
     *
     * @param colorTheme Colour theme to be used to display the image.
     * @return This builder.
    */
    public Builder colorTheme(final ColorGradient colorTheme) {
      this.colorTheme = colorTheme;
      return this;
    }

    /**
     * Builds an {@link mandelbrot.ImageConfiguration} from the set
     * values.
     *
     * @return The configuration.
    */
    public ImageConfiguration build() {
      return new ImageConfiguration(
        bound, escapeRadius, maximumIterations,
        overlayZoom, colorTheme
      );
    }
  }

  /**
   * Construct an image configuration.
   *
   * @param bound Bound to use when generating the iteration values.
   *
   * @param escapeRadius Escape radius to use when calculating the iteration
   * values.
   *
   * @param maximumIterations Maximum number of iterations before assuming the
   * value of C lies within the set.
   *
   * @param overlayZoom Whether the zoom amount should be overlayed onto the image.
   * @param colorTheme Colour theme to be used to display the image.
  */
  private ImageConfiguration(
    final Bound bound,
    final double escapeRadius,
    final int maximumIterations,
    final boolean overlayZoom,
    final ColorGradient colorTheme
  ) {

    this.bound = bound;
    this.escapeRadius = escapeRadius;
    this.maximumIterations = maximumIterations;
    this.overlayZoom = overlayZoom;
    this.colorTheme = colorTheme;
  }

  /**
   * Get the bound.
   *
   * @return The bound used when generating the iteration values.
  */
  public Bound getBound() {
    return bound;
  }

  /**
   * Get the escape radius.
   * @return The escape radius used when calculating the iteration
   * values.
  */
  public double getEscapeRadius() {
    return escapeRadius;
  }

  /**
   * Get the maximum number of iterations.
   *
   * @return The maximum number of iterations done before assuming the
   * value of C lies within the set.
  */
  public int getMaximumIterations() {
    return maximumIterations;
  }

  /**
   * Whether the zoom should be overlayed onto the image.
   *
   * @return Whether the zoom should be overlayed onto the image.
  */
  public boolean getOverlayZoom() {
    return overlayZoom;
  }

  /**
   * Get the colour theme.
   *
   * @return The colour theme used to display the image.
  */
  public ColorGradient getColorTheme() {
    return colorTheme;
  }

  @Override
  public boolean equals(final Object b) {
    
    if (!(b instanceof ImageConfiguration))  {
      return false;
    }

    final ImageConfiguration configuration = (ImageConfiguration) b;

    return bound.equals(configuration.getBound()) &&
      escapeRadius == configuration.getEscapeRadius() &&
      maximumIterations == configuration.getMaximumIterations() &&
      overlayZoom == configuration.getOverlayZoom() &&
      colorTheme.equals(configuration.getColorTheme());
  }

  @Override
  public int hashCode() {
  
    //Taken from: https://medium.com/codelog/overriding-hashcode-
    //            method-effective-java-notes-723c1fedf51c

    int result = 17;

    final long escapeRadiusLong = Double.doubleToLongBits(escapeRadius);

    result = 31 * result + bound.hashCode();
    result = 31 * result + (int) (escapeRadiusLong ^ (escapeRadiusLong >>> 32));
    result = 31 * result + (maximumIterations ^ (maximumIterations >>> 32));
    result = 31 * result + (overlayZoom ? 1 : 0);
    result = 31 * result + colorTheme.hashCode();

    return result;
  }
}
