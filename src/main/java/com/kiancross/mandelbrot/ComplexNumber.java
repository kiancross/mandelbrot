/*
 * Copyright (C) 2021 Kian Cross
 */

package mandelbrot;

import java.lang.Math;
import java.io.Serializable;

/**
 * Represents a complex number.
*/
public class ComplexNumber implements Serializable {
  
  private static final long serialVersionUID = 1;

  /**
   * The real part of the complex number.
  */
  final private double real;

  /**
   * The imaginary part of the complex number.
  */
  final private double imaginary;
  
  /**
   * Constructs a complex number.
   *
   * @param real The real part of the complex number.
   * @param imaginary The imaginary part of the complex number.
  */
  public ComplexNumber(final double real, final double imaginary) {
    this.real = real;
    this.imaginary = imaginary;
  }

  /**
   * Get the real part of the complex number.
   *
   * @return The real part of the complex number.
  */
  public double getReal() {
    return real;
  }
  
  /**
   * Get the imaginary part of the complex number.
   *
   * @return The imaginary part of the complex number.
  */
  public double getImaginary() {
    return imaginary;
  }

  /**
   * Calculate the modulus of the complex number.
   *
   * @return The modulus of the complex number.
  */
  public double getModulus() {
    return Math.sqrt(getAbsoluteSquare());
  }

  /**
   * Calculate the absolute square of the complex number.
   *
   * @return The absolute square of the complex number.
  */
  public double getAbsoluteSquare() {
    return (real * real) + (imaginary * imaginary);
  }

  /**
   * Calculate the square of the complex number.
   *
   * @return The square of the complex number.
  */
  public ComplexNumber square() {
    return multiply(this);
  }

  /**
   * Multiply two complex number.
   *
   * @param b Complex number to multiply with this complex number.
   * @return The resultant complex number.
  */
  public ComplexNumber multiply(final ComplexNumber b) {

    final double newReal = (getReal() * b.getReal()) - (getImaginary() * b.getImaginary());
    final double newImaginary = (getReal() * b.getImaginary()) + (getImaginary() * b.getReal());

    return new ComplexNumber(newReal, newImaginary);
  }
 
  /**
   * Multiply a complex number with a scalar.
   *
   * @param s The scalar to multiple this complex number by.
   * @return The resultant complex number.
  */ 
  public ComplexNumber multiply(final double s) {
    return new ComplexNumber(s * getReal(), s * getImaginary());
  }

  /**
   * Add to complex numbers.
   *
   * @param b The complex number to add to this complex number.
   * @return The resultant complex number.
  */
  public ComplexNumber add(final ComplexNumber b) {
    return new ComplexNumber(getReal() + b.getReal(), getImaginary() + b.getImaginary());
  }

  /**
   * Take a complex number away from this complex number. This is the same as
   * calling add(b.multiply(-1)).
   *
   * @param b The complex number to takeaway from this complex number.
   * @return The resultant complex number.
  */
  public ComplexNumber minus(final ComplexNumber b) {
    return add(b.multiply(-1));
  }

  @Override
  public String toString() {
    return String.format("%f + %fi", getReal(), getImaginary());
  }

  @Override
  public boolean equals(final Object o) {

    if (!(o instanceof ComplexNumber)) {
      return false;
    }

    final ComplexNumber c = (ComplexNumber) o;

    return real == c.getReal() && imaginary == c.getImaginary();
  }

  @Override
  public int hashCode() {

    //Taken from: https://medium.com/codelog/overriding-hashcode-
    //            method-effective-java-notes-723c1fedf51c

    int result = 17;

    final long realLong = Double.doubleToLongBits(real);
    final long imaginaryLong = Double.doubleToLongBits(imaginary);

    result = 31 * result + (int) (realLong ^ (realLong >>> 32));
    result = 31 * result + (int) (imaginaryLong ^ (imaginaryLong >>> 32));

    return result;
  }
}
