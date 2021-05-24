/*
 * Copyright (C) 2021 Kian Cross
 */

package mandelbrot;

import javafx.scene.paint.Color;

import java.io.File;
import java.io.IOException;
import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import java.io.FileInputStream;
import java.io.ObjectInputStream;

/**
 * Manages the creation of the mandelbrot image, allowing parameters to be set
 * to modify the resultant image.
*/
public class ImageGenerator {

  /**
   * Boolean value indicating whether the iterations need to be recalculated.
   * Changing some options require the iterations to be recalculated, whereas
   * some do not.
  */
  boolean recalculateMandelbrotIterations = true;

  /**
   * The last calculated iteration values.
  */
  int[][] iterationValues;

  /**
   * The image configuration manager.
  */
  final private ImageConfigurationManager configurationManger;

  /**
   * Construct an image generator.
   *
   * @param initialColorTheme The initial colour theme that should be used.
  */
  public ImageGenerator(final ColorGradient initialColorTheme) {
    
    final ImageConfiguration initialConfiguration = new ImageConfiguration.Builder()
      .bound(new Bound(new ComplexNumber(-2, -1.25), new ComplexNumber(0.7, 1.25)))
      .escapeRadius(2)
      .maximumIterations(1000)
      .overlayZoom(true)
      .colorTheme(initialColorTheme)
      .build();

    configurationManger = new ImageConfigurationManager(initialConfiguration);

    // Whenever the configuration changes, check if the iteration values need to be
    // recalculated. The following value changes require the values to be recalculated:
    // bound, maximumIterations, escapeRadius.
    configurationManger.getCurrentConfigurationProperty()
      .addListener((a, oldConfiguration, newConfiguration) -> {

        if (!(
          oldConfiguration.getBound().equals(newConfiguration.getBound()) &&
          oldConfiguration.getMaximumIterations() == newConfiguration.getMaximumIterations() &&
          oldConfiguration.getEscapeRadius() == newConfiguration.getEscapeRadius()
        )) {
          recalculateMandelbrotIterations = true;
        }
      });
  }

  /**
   * Take a bound and scales it such that the aspect ratio of the imaginary part to the yAxis
   * and the real part to the x-axis are the same. This prevents the image from being scaled
   * disproportionally (resulting in a stretched/squashed image).
   *
   * @param bound The bound to be normalised.
   * @param xRange The width of the image being displayed.
   * @param yRange The height of the image being displayed
   * @return The normalised bound.
  */
  private Bound normaliseBound(final Bound bound, final double xRange, final double yRange) {
  
    final ComplexNumber minimum = bound.getMinimum();
    final ComplexNumber maximum = bound.getMaximum();

    final ComplexNumber range = bound.getRange();

    final double realPartRange = range.getReal();
    final double imaginaryPartRange = range.getImaginary();

    final double xRatio = xRange / realPartRange;
    final double yRatio = yRange / imaginaryPartRange;

    final double addToRealRange;
    final double addToImaginaryRange;

    // Here we calculate the additional value needed to add to the range to make the
    // two rations equal.
    if (xRatio > yRatio) {
      addToRealRange = ((imaginaryPartRange * xRange) / yRange) - realPartRange;
      addToImaginaryRange = 0;

    } else {
    
      addToRealRange = 0;
      addToImaginaryRange = ((realPartRange * yRange) / xRange) - imaginaryPartRange;
    }
      
    // We then add an equal amount of the additional range to both the minimum
    // and maximum. This ensures the image remains centred.
    final ComplexNumber newMinimum = new ComplexNumber(
      minimum.getReal() - (addToRealRange / 2),
      minimum.getImaginary() - (addToImaginaryRange / 2)
    );

    final ComplexNumber newMaximum = new ComplexNumber(
      maximum.getReal() + (addToRealRange / 2),
      maximum.getImaginary() + (addToImaginaryRange / 2)
    );

    return new Bound(newMinimum, newMaximum);
  }

  /**
   * Checks if the iteration values need recalculating. This is based on the flag that indicates
   * if the values need recalculating and also a check to see if the image size is different.
   *
   * @param xResolution The x resolution of the image being drawn.
   * @param yResolution The y resolution of the image being drawn.
   *
   * @return A boolean value indicating whether the value needs recalculating.
  */
  private boolean checkIfRecalculationNeeded(final int xResolution, final int yResolution) {
    return recalculateMandelbrotIterations ||
           xResolution != iterationValues.length ||
           yResolution != iterationValues[0].length;
  }

  /**
   * Translates an (x, y) coordinate to a point on the complex plane.
   *
   * @param x The x coordinate.
   * @param y The y coordinate.
   *
   * @param xRange The width of the x axis.
   * @param yRange The height of the y axis.
  */
  private ComplexNumber translateToComplexPlane(
    final double x,
    final double y,
    final double xRange,
    final double yRange
  ) {

    final Bound currentBound = normaliseBound(
      configurationManger.getCurrentConfiguration().getBound(),
      xRange, yRange
    );

    final ComplexNumber minimumC = currentBound.getMinimum();
    final ComplexNumber maximumC = currentBound.getMaximum();

    final double realRange = maximumC.getReal() - minimumC.getReal();
    final double imaginaryRange = maximumC.getImaginary() - minimumC.getImaginary();

    final double realScaleFactor = realRange / xRange;
    final double imaginaryScaleFactor = imaginaryRange / yRange;

    final double u = minimumC.getReal() + (x * realScaleFactor);
    final double v = maximumC.getImaginary() - (y * imaginaryScaleFactor);

    return new ComplexNumber(u, v);
  }

  /**
   * Zooms in (scales) the image to be produced.
   *
   * @param xRange The width of the x-axis.
   * @param yRange The width of the y-axis.
   * @param xMin The minimum value of x for which the image should show.
   * @param xMax The maximum value of x for which the image should show.
   * @param yMin The minimum value of y for which the image should show.
   * @param yMax The maximum value of y for which the image should show.
  */
  public void zoom(
    final double xRange, final double yRange,
    final double xMin, final double xMax,
    final double yMin, final double yMax
  ) {

    // Translate these values to the complex plane, then use them as the new bound for
    // the image.

    final ComplexNumber minimum = translateToComplexPlane(xMin, yMin, xRange, yRange);
    final ComplexNumber maximum = translateToComplexPlane(xMax, yMax, xRange, yRange);

    final ImageConfiguration newConfiguration = new ImageConfiguration.Builder()
      .use(configurationManger.getCurrentConfiguration())
      .bound(new Bound(minimum, maximum))
      .build();

    configurationManger.addConfiguration(newConfiguration);
  }

  /**
   * Pans the view of the image a certain amount along the x-axis and y-axis.
   * A positive value for xShift shifts the view right and therefore the image left.
   * A positive value for yShift shifts the view up and therefore the image down.
   *
   * @param xRange The width of the x-axis.
   * @param yRange The width of the y-axis.
   * @param xShift The amount to shift along the x-axis.
   * @param yShift The amount to shift along the y-axis.
  */
  public void pan(
    final double xRange, final double yRange,
    final int xShift, final int yShift
  ) {

    final ComplexNumber minimum = translateToComplexPlane(xShift, yRange - yShift, xRange, yRange);

    final ComplexNumber maximum = translateToComplexPlane(
      xShift + xRange, -yShift,
      xRange, yRange
    );

    final ImageConfiguration newConfiguration = new ImageConfiguration.Builder()
      .use(configurationManger.getCurrentConfiguration())
      .bound(new Bound(minimum, maximum))
      .build();

    configurationManger.addConfiguration(newConfiguration);
  }

  /**
   * Set the maximum number of iterations to use when calculating the iteration
   * values.
   * 
   * @param maximumIterations The maximum number of iterations to use when calculating
   * the iteration values.
  */
  public void setMaximumIterations(final int maximumIterations) {

    final ImageConfiguration newConfiguration = new ImageConfiguration.Builder()
      .use(configurationManger.getCurrentConfiguration())
      .maximumIterations(maximumIterations)
      .build();

    configurationManger.addConfiguration(newConfiguration);
  }
  
  /**
   * Set the escape radius to use when calculating the iteration values.
   *
   * @param escapeRadius The escape radius to use when calculate the iteration values.
  */
  public void setEscapeRadius(final double escapeRadius) {

    final ImageConfiguration newConfiguration = new ImageConfiguration.Builder()
      .use(configurationManger.getCurrentConfiguration())
      .escapeRadius(escapeRadius)
      .build();

    configurationManger.addConfiguration(newConfiguration);
  }

  /**
   * Set the colour theme to use for the image.
   *
   * @param theme The colour theme to use for the image.
  */
  public void setColorTheme(final ColorGradient theme) {

    final ImageConfiguration newConfiguration = new ImageConfiguration.Builder()
      .use(configurationManger.getCurrentConfiguration())
      .colorTheme(theme)
      .build();
  
    configurationManger.addConfiguration(newConfiguration);
  }
  
  /**
   * Set whether the zoom should be overlayed onto the image.
   *
   * @param overlayZoom Whether the zoom should be overlayed onto the image.
  */
  public void setOverlayZoom(final boolean overlayZoom) {

    final ImageConfiguration newConfiguration = new ImageConfiguration.Builder()
      .use(configurationManger.getCurrentConfiguration())
      .overlayZoom(overlayZoom)
      .build();
  
    configurationManger.addConfiguration(newConfiguration);
  }

  /**
   * Undo the last change.
  */
  public void undo() {
    configurationManger.undo();
  }

  /**
   * Redo the last undo.
  */
  public void redo() {
    configurationManger.redo();
  }

  /**
   * Reset all settings.
  */
  public void resetAll() {
    configurationManger.resetAll();
  }

  /**
   * Check if the undo operation can be done.
  */
  public boolean canUndo() {
    return configurationManger.canUndo();
  }
  
  /**
   * Check if the redo operation can be done.
  */
  public boolean canRedo() {
    return configurationManger.canRedo();
  }

  /**
   * Gets the current configuration property. A listener can be added that is called on changes.
   *
   * @return The current configuration property.
  */
  public InformInitialObjectProperty<ImageConfiguration> getCurrentConfigurationProperty() {
    return configurationManger.getCurrentConfigurationProperty();
  }

  /**
   * Gets the current zoom amount.
   *
   * @param xRange The width of the x-axis.
   * @param yRange The height of the y-axis.
  */
  public double getCurrentZoom(final double xRange, final double yRange) {

    final ImageConfiguration initialConfiguration = configurationManger.getInitialConfiguration();
    final ImageConfiguration currentConfiguration = configurationManger.getCurrentConfiguration();

    final ComplexNumber initialRange = normaliseBound(
      initialConfiguration.getBound(),
      xRange, yRange
    ).getRange();

    final ComplexNumber currentRange = normaliseBound(
      currentConfiguration.getBound(),
      xRange, yRange
    ).getRange();

    return initialRange.getReal() / currentRange.getReal();
  }

  /**
   * Saves the image configuration to a given file.
   *
   * @param file The file to save the image configuration to.
   *
   * @throws ImageConfigurationSaveException Exception thrown if there was an error saving
   * the configuration.
  */
  public void saveConfiguration(final File file) throws ImageConfigurationSaveException {

    try (final ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(file))) {
      out.writeObject(configurationManger.getCurrentConfiguration());

    } catch (IOException e) {
      throw new ImageConfigurationSaveException(e);
    }
  }

  /**
   * Loads the image configuration from a given file.
   *
   * @param file The file to save the image configuration to.
   *
   * @throws ImageConfigurationLoadException Exception thrown if there was an error loading
   * the configuration.
  */
  public void loadConfiguration(final File file) throws ImageConfigurationLoadException {

    try (final ObjectInputStream in = new ObjectInputStream(new FileInputStream(file))) {

      configurationManger.addConfiguration((ImageConfiguration) in.readObject());

    } catch (ClassNotFoundException e) {
      throw new ImageConfigurationLoadException(e);

    } catch (IOException e) {
      throw new ImageConfigurationLoadException(e);

    }
  }

  /**
   * Generate an image of a given resolution.
   *
   * @param xResolution Number of pixels the width of the image should be.
   * @param yResolution Number of pixels the height of the image should be.
   *
   * @return A 2D array of pixels.
  */
  public Color[][] generate(
    final int xResolution,
    final int yResolution
  ) {

    /*
     * Using more threads than the number of cores the computer has may seem pointless.
     * But actually, as some threads complete a lot quicker than others, due to the way
     * the calculations are divided, it means that if one thread the calculation very
     * quickly, there are still more threads to utilise all of the cores. The number 24 
     * is arbitrary: there is probably a sweet spot somewhere but I haven't done an experiment
     * to find it.
    */
    final int numberOfThreads = 24;
    
    final ImageConfiguration configuration = configurationManger.getCurrentConfiguration();
   
    if (checkIfRecalculationNeeded(xResolution, yResolution)) {

      final MandelbrotSetIterationCountGenerator iterationCountGenerator =
        new MandelbrotSetIterationCountGenerator(
          normaliseBound(configuration.getBound(), xResolution, yResolution),
          configuration.getMaximumIterations(),
          configuration.getEscapeRadius()
        );
      
      iterationValues = iterationCountGenerator.calculate(
        xResolution, yResolution, numberOfThreads
      );

      recalculateMandelbrotIterations = false;
    }

    final HistogramColorMapper colorMapper = new HistogramColorMapper(
      configuration.getMaximumIterations(),
      configuration.getColorTheme()
    );
    
    return colorMapper.mapIterationsToColors(iterationValues);
  }
}
