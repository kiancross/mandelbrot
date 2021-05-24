/*
 * Copyright (C) 2021 Kian Cross
 */

package mandelbrot;

import java.util.Map;
import java.util.Set;
import java.util.HashMap;
import java.io.Serializable;
import javafx.scene.paint.Color;

/**
 * Represents a colour gradient.
*/
public class ColorGradient implements Serializable {
  
  private static final long serialVersionUID = 1;
  
  /**
   * The data structure used to store the gradient.
  */
  private final Map<Double, SerializableColor> colorMap = new HashMap<Double, SerializableColor>();

  /**
   * The name of the gradient (human readable);
  */
  private String name;

  /**
   * Used by the equals method - but should not be accessed outside of the
   * class itself, hence the protected modifier.
   * 
   * @return The color map data structure.
  */
  protected Map<Double, SerializableColor> getColorMap() {
    return colorMap;
  }

  /**
   * Construct a color gradient.
   *
   * @param The start colour.
   * @param The end colour.
   * @param The name of the gradient.
  */
  public ColorGradient(final Color start, final Color end, final String name) {

    this.name = name;

    colorMap.put((double) 0, new SerializableColor(start));
    colorMap.put((double) 1, new SerializableColor(end));
  }
  
  /**
   * Set a stop within the gradient.
   *
   * @param stop The stop: a number in-between 0 and 1.
   * @param color The color corresponding to this stop.
  */
  public void setStop(final double stop, final Color color) {

    if (stop < 0 || stop > 1) {
      throw new IllegalArgumentException("Stop must be between 0 and 1.");
    }

    colorMap.put(stop, new SerializableColor(color));
  }

  /**
   * Get the color at a given position.
  */   
  public Color getColor(final double position) {

    if (position < 0 || position > 1) {
      throw new IllegalArgumentException("Position must be between 0 and 1.");
    }

    // Initial start and end values.
    double start = 0;
    double end = 1;

    // Find the smallest value greater than the given position and
    // the largest value smaller than the given position.
    for (Double stop: colorMap.keySet()) {
      
      if (position > stop && stop > start) {
        start = stop;

      } else if (position < stop && stop < end) {
        end = stop;
      }
    }

    final Color startColor = colorMap.get(start).getColor();
    final Color endColor = colorMap.get(end).getColor();

    final double normalisedPosition = (position - start) / (end - start);

    // Get the new colour in-between the two colours at the given position.
    return startColor.interpolate(endColor, normalisedPosition);
  }

  @Override
  public boolean equals(final Object o) {
    
    if (!(o instanceof ColorGradient)) {
      return false;
    }

    final ColorGradient g = (ColorGradient) o;

    return colorMap.equals(g.getColorMap());
  }

  @Override
  public int hashCode() {
    return colorMap.hashCode();
  }

  @Override
  public String toString() {
    return name;
  } 
}
