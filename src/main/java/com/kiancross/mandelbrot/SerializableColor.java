/*
 * Copyright (C) 2021 Kian Cross
 */

package mandelbrot;

import javafx.scene.paint.Color;
import java.io.Serializable;
import java.io.ObjectOutputStream;
import java.io.ObjectInputStream;
import java.io.IOException;
import java.io.ObjectStreamException;

/**
 * This is a simple wrapper around the Color class which makes it serializable.
 * Ideally this class would extend the Color class, but the Color class has been
 * set to final, therefore this can't be done.
*/
public class SerializableColor implements Serializable {

  private static final long serialVersionUID = 1;

  /**
   * The colour being stored.
  */
  private Color color;

  /**
   * Constructor for the colour.
   *
   * @param The colour to be stored.
  */
  public SerializableColor(final Color color) {

    if (color == null) {
      throw new IllegalArgumentException("color must not be null");
    }

    this.color = color;
  }

  /**
   * Gets the colour being stored.
   *
   * @return The colour being stored.
  */
  public Color getColor() {
    return color;
  }

  /**
   * Serialises the object.
   *
   * @param out The object output stream.
   *
   * @throws IOException If there was a problem when writing to the output stream.
  */
  private void writeObject(final ObjectOutputStream out) throws IOException {
    out.writeDouble(color.getRed());
    out.writeDouble(color.getGreen());
    out.writeDouble(color.getBlue());
    out.writeDouble(color.getOpacity());
  }

  /**
   * De-serialises the object.
   *
   * @param in The object input stream.
   *
   * @throws IOException If there was an problem when reading from the object input stream.
   * @throws ClassNotFoundException Should not be thrown as only primitives are being read.
  */  
  private void readObject(final ObjectInputStream in) throws IOException, ClassNotFoundException {

    final double red = in.readDouble();
    final double green = in.readDouble();
    final double blue = in.readDouble();
    final double opacity = in.readDouble();

    color = new Color(red, green, blue, opacity);
  }
  
  @Override
  public boolean equals(final Object o) {

    final Color oColor;

    if (o instanceof SerializableColor) {

      oColor = ((SerializableColor) o).getColor();
    
    } else if (o instanceof Color) {
      oColor = (Color) o;

    } else {
      return false;
    }
    
    return color.equals(oColor);
  }

  @Override
  public int hashCode() {
    return color.hashCode();
  }
}
