/*
 * Copyright (C) 2021 Kian Cross
 */

package com.kiancross.mandelbrot;

import com.kiancross.typedtextfield.DoubleTextField;
import com.kiancross.typedtextfield.IntegerTextField;
import java.io.File;
import java.io.IOException;
import javafx.animation.PauseTransition;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.embed.swing.SwingFXUtils;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Control;
import javafx.scene.control.Label;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Duration;
import javax.imageio.ImageIO;

/**
 * GUI for the explorer using JavaFX.
 */
public class View extends Application {

  /**
   * The canvas used to draw the image.
   */
  final Canvas canvas = new Canvas();

  /**
   * The image generator used to generate the image.
   */
  final ImageGenerator imageGenerator;

  /**
   * Constructor to create the GUI.
   */
  public View() {
    imageGenerator = new ImageGenerator(getColorThemes()[0]);
  }

  @Override
  public void start(Stage primaryStage) {

    final VBox root = new VBox();
    final Scene scene = new Scene(root);

    final Pane optionsPane = getOptionsPane(primaryStage);
    final Group imageGroup = getImageGroup();

    // When the scene resizes the image is redrawn.
    addSceneResizeListeners(scene, optionsPane);

    // When the configuration changes, the image is redrawn.
    imageGenerator.getCurrentConfigurationProperty().addListener((a, b, c) -> redrawImage());

    // When the UI is clicked, focus is set to the root. This means that the
    // user can click anywhere on the screen to remove focus from a text field
    // and redraw the UI.
    root.setOnMouseClicked(e -> root.requestFocus());

    root.getChildren().addAll(optionsPane, imageGroup);

    final double initialWidth = 1000;
    final double initialHeight = 800;

    primaryStage.setScene(scene);
    primaryStage.setTitle("Mandelbrot Explorer");
    primaryStage.setMinWidth(initialWidth);
    primaryStage.setMinHeight(initialHeight);
    primaryStage.show();
  }

  /**
   * Displays an exception in a dialog to the user.
   *
   * @param e The exception to display.
   */
  private void displayException(final Throwable e) {
    final Alert alert = new Alert(Alert.AlertType.ERROR);
    alert.setTitle("Error");
    alert.setHeaderText("An Error Occurred");

    final Group group = new Group();

    group.getChildren().addAll(new Text(e.getMessage()));
    alert.getDialogPane().setContent(group);

    alert.showAndWait();
  }

  /**
   * Add a resize listener that is called when the screen is resized.
   *
   * @param scene The scene which fires the resize events.
   * @param optionsPane The options pane at the top of the screen - this is required because it's
   *        height must be subtracted from the height of the canvas.
   */
  private void addSceneResizeListeners(final Scene scene, final Pane optionsPane) {

    // Here a debouncer is used to only actually call the callback once there have
    // been
    // no events fired for 100ms. Otherwise, when the user resized the window, the
    // callback would be called for each pixel change in size, which would in turn
    // recalculate all the Mandelbrot iteration values. The result would be a very
    // very slow, laggy, and unresponsive resize experience for the user.
    final PauseTransition debouncer = new PauseTransition(new Duration(100));

    debouncer.setOnFinished(e -> {
      canvas.setWidth(scene.getWidth());

      // Take away the height of the options pane.
      canvas.setHeight(scene.getHeight() - optionsPane.getHeight());

      redrawImage();
    });

    scene.widthProperty().addListener((a, b, c) -> debouncer.playFromStart());
    scene.heightProperty().addListener((a, b, c) -> debouncer.playFromStart());
  }

  /**
   * Defines some preset colour themes that the user can select when drawing the image.
   *
   * @return An array of colour gradients, which are the themes.
   */
  private ColorGradient[] getColorThemes() {

    final ColorGradient bulb =
        new ColorGradient(Color.rgb(35, 240, 199), Color.rgb(255, 227, 71), "Bulb");
    bulb.setStop(1 / 4.0, Color.rgb(239, 118, 122));
    bulb.setStop(2 / 4.0, Color.rgb(125, 122, 188));
    bulb.setStop(3 / 4.0, Color.rgb(100, 87, 166));

    final ColorGradient fire = new ColorGradient(Color.BLACK, Color.WHITE, "Fire");
    fire.setStop(0.2, Color.rgb(255, 0, 0));
    fire.setStop(0.8, Color.rgb(255, 255, 0));

    final ColorGradient ocean = new ColorGradient(Color.BLACK, Color.WHITE, "Ocean");
    ocean.setStop(1 / 5.0, Color.rgb(44, 115, 210));
    ocean.setStop(2 / 5.0, Color.rgb(0, 129, 207));
    ocean.setStop(3 / 5.0, Color.rgb(0, 137, 186));
    ocean.setStop(4 / 5.0, Color.rgb(0, 142, 155));

    final ColorGradient capillary =
        new ColorGradient(Color.rgb(8, 15, 15), Color.rgb(138, 3, 3), "Capillary");
    capillary.setStop(1 / 4.0, Color.rgb(164, 186, 183));
    capillary.setStop(2 / 4.0, Color.rgb(239, 242, 192));
    capillary.setStop(3 / 4.0, Color.rgb(190, 165, 125));

    final ColorGradient eco =
        new ColorGradient(Color.rgb(0, 36, 0), Color.rgb(219, 210, 224), "Eco");
    eco.setStop(1 / 4.0, Color.rgb(39, 59, 9));
    eco.setStop(2 / 4.0, Color.rgb(88, 100, 29));
    eco.setStop(3 / 4.0, Color.rgb(123, 144, 75));

    final ColorGradient grey = new ColorGradient(Color.BLACK, Color.WHITE, "Grey");

    // Everything is white, apart from the values that lie within the set which are
    // always
    // set to black by the ColorGradient.
    final ColorGradient blackAndWhite =
        new ColorGradient(Color.WHITE, Color.WHITE, "Black and White");

    return new ColorGradient[] {bulb, fire, ocean, capillary, eco, grey, blackAndWhite};
  }

  /**
   * Generate a HBox from a set of nodes.
   *
   * @param nodes A set of nodes to be added to the HBox.
   * @return The HBox that has been generated.
   */
  private HBox generateHbox(final Node... nodes) {
    final HBox hbox = new HBox();

    hbox.setSpacing(8);
    hbox.setAlignment(Pos.CENTER_LEFT);
    hbox.getChildren().addAll(nodes);

    return hbox;
  }

  /**
   * Get the options pane, which contains all the configurable parameters.
   *
   * @param stage The parent stage for the options pane - this is required for when dialogs are
   *        displayed.
   *
   * @return The options pane.
   */
  private Pane getOptionsPane(final Stage stage) {

    final FlowPane root = new FlowPane();

    root.setVgap(8);
    root.setHgap(8);

    root.setPadding(new Insets(8, 8, 8, 8));

    root.getChildren().addAll(getUndoButton(), getRedoButton(), getResetAllButton(),
        getExportStateButton(stage), getImportStateButton(stage), getExportImageButton(stage),
        getOverlayZoomCheckBox(),

        // HBoxes are used within the options pane so that these items always appear
        // next to
        // each other and aren't broken up by the FlowPane.
        generateHbox(new Label("Colour Scheme:"), getColorThemesComboBox()),
        generateHbox(new Label("Pan X Amount:"), getPanNodeX()),
        generateHbox(new Label("Pan Y Amount:"), getPanNodeY()),
        generateHbox(new Label("Maximum Iterations:"), getMaximumIterationsTextField()),
        generateHbox(new Label("Escape Radius:"), getEscapeRadiusTextField()));

    return root;
  }

  /**
   * Returns the maximum iterations text field.
   *
   * @return The maximum iterations text field.
   */
  private Control getMaximumIterationsTextField() {
    final IntegerTextField textField = new IntegerTextField(1);
    textField.setPrefWidth(80);
    textField.addValidator((value) -> {
      return value > 0;
    });

    // If the configuration changes, set the value of this text field to the value
    // in the configuration.
    imageGenerator.getCurrentConfigurationProperty().addListener(
        (a, b, configuration) -> textField.setTypedValue(configuration.getMaximumIterations()),
        true);

    // When focus is lost, update the image generator with the new value.
    textField.focusedProperty().addListener((a, b, focused) -> {
      if (!focused) {
        imageGenerator.setMaximumIterations(textField.getTypedValue().intValue());
      }
    });

    return textField;
  }

  /**
   * Return the escape radius text field.
   *
   * @return The escape radius text field.
   */
  private Control getEscapeRadiusTextField() {

    final DoubleTextField textField = new DoubleTextField(0);
    textField.setPrefWidth(80);
    textField.addValidator((value) -> {
      return value >= 0;
    });

    // If the configuration changes, set the value of this text field to the value
    // in the configuration.
    imageGenerator.getCurrentConfigurationProperty().addListener(
        (a, b, configuration) -> textField.setTypedValue(configuration.getEscapeRadius()), true);

    // When focus is lost, update the image generator with the new value.
    textField.focusedProperty().addListener((a, b, focused) -> {
      if (!focused) {
        imageGenerator.setEscapeRadius(textField.getTypedValue().doubleValue());
      }
    });

    return textField;
  }

  /**
   * Get the pan y node. This includes the button and text field.
   *
   * @return The pan y node.
   */
  private Node getPanNodeY() {
    final Button button = new Button("Pan Y");

    final IntegerTextField textField = new IntegerTextField(0);
    textField.setPrefWidth(80);

    // When the button is pressed, pan in the y direction.
    button.setOnAction(event -> {
      imageGenerator.pan(canvas.getWidth(), canvas.getHeight(), 0, textField.getTypedValue());
    });

    return generateHbox(textField, button);
  }

  /**
   * Get the pan x node. This includes the button and text field.
   *
   * @return The pan x node.
   */
  private Node getPanNodeX() {

    final IntegerTextField textField = new IntegerTextField(0);
    textField.setPrefWidth(80);

    final Button button = new Button("Pan X");

    // When the button is pressed, pan in the x direction.
    button.setOnAction(event -> {
      imageGenerator.pan(canvas.getWidth(), canvas.getHeight(), textField.getTypedValue(), 0);
    });

    return generateHbox(textField, button);
  }

  /**
   * Returns the combo box used to select a colour theme.
   */
  private Control getColorThemesComboBox() {
    final ComboBox<ColorGradient> comboBox =
        new ComboBox<ColorGradient>(FXCollections.observableArrayList(getColorThemes()));

    // If the configuration changes, set the value of this combo box to the value
    // in the configuration.
    imageGenerator.getCurrentConfigurationProperty().addListener((a, b, configuration) -> {
      comboBox.setValue(configuration.getColorTheme());
    }, true);

    // When the item in the combo box changes, update the configuration with the new
    // value.
    comboBox.getSelectionModel().selectedItemProperty().addListener((a, b, colorTheme) -> {
      imageGenerator.setColorTheme(colorTheme);
    });

    return comboBox;
  }

  /**
   * Return the overlay zoom check box.
   *
   * @return The overlay zoom check box.
   */
  private Control getOverlayZoomCheckBox() {
    final CheckBox checkBox = new CheckBox("Overlay Zoom");

    // If the configuration changes, set the value of this checkbox to the value
    // in the configuration.
    imageGenerator.getCurrentConfigurationProperty().addListener((a, b, configuration) -> {
      checkBox.setSelected(configuration.getOverlayZoom());
    }, true);

    // Update the configuration when the value changes.
    checkBox.selectedProperty()
        .addListener((a, b, checked) -> imageGenerator.setOverlayZoom(checked));

    return checkBox;
  }

  /**
   * Get the export image button.
   *
   * @param stage The parent stage (used to display a model file selection dialog).
   *
   * @return The export image button.
   */
  private Control getExportImageButton(final Stage stage) {

    final Button button = new Button("Export Image");

    // When the button is clicked.
    button.setOnAction(event -> {

      // Create a writeable image.
      final WritableImage writableImage =
          new WritableImage((int) canvas.getWidth(), (int) canvas.getHeight());

      final FileChooser fileChooser = new FileChooser();
      final File file = fileChooser.showSaveDialog(stage);

      // If no file was selected then the value will be null.
      // In such a case do nothing.
      if (file != null) {

        // Take a snapshot of the canvas.
        canvas.snapshot((s) -> {

          try {

            // Convert the writeable image to a BufferedImage and then write this to
            // a file. Some code taken from here:
            // https://community.oracle.com/thread/2450090?start=0&tstart=0
            ImageIO.write(SwingFXUtils.fromFXImage(writableImage, null), "png", file);
          } catch (IOException e) {
            displayException(e);
          }

          // Callback has to return a value - just return null.
          return null;

        }, null, writableImage);
      }
    });

    return button;
  }

  /**
   * Get the reset all button.
   *
   * @return The reset all button.
   */
  private Button getResetAllButton() {

    final Button button = new Button("Reset All");

    // When the button is pressed.
    button.setOnAction((e) -> imageGenerator.resetAll());

    // If the configuration changes, update the button's disabled state.
    imageGenerator.getCurrentConfigurationProperty()
        .addListener((a, b, c) -> button.setDisable(!imageGenerator.canUndo()), true);

    return button;

  }

  /**
   * Get the undo button.
   *
   * @return The undo button.
   */
  private Button getUndoButton() {

    final Button button = new Button("Undo");

    // When the button is pressed.
    button.setOnAction((e) -> imageGenerator.undo());

    // If the configuration changes, update the button's disabled state.
    imageGenerator.getCurrentConfigurationProperty()
        .addListener((a, b, c) -> button.setDisable(!imageGenerator.canUndo()), true);

    return button;
  }

  /**
   * Get the redo button.
   *
   * @return The redo button.
   */
  private Button getRedoButton() {

    final Button button = new Button("Redo");

    // When the button is pressed.
    button.setOnAction((e) -> imageGenerator.redo());

    // If the configuration changes, update the button's disabled state.
    imageGenerator.getCurrentConfigurationProperty()
        .addListener((a, b, c) -> button.setDisable(!imageGenerator.canRedo()), true);

    return button;

  }

  /**
   * Get the export state button.
   *
   * @param stage The parent stage - used to display the file selector.
   * @return The export state button.
   */
  private Control getExportStateButton(final Stage stage) {

    final Button button = new Button("Export State");

    button.setOnAction(event -> {

      final FileChooser fileChooser = new FileChooser();
      final File file = fileChooser.showSaveDialog(stage);

      // Null is no file has been selected.
      if (file != null) {
        try {

          imageGenerator.saveConfiguration(file);

        } catch (ImageConfigurationSaveException e) {

          displayException(e);

        }
      }
    });

    return button;
  }

  /**
   * Get the import state button.
   *
   * @param stage The parent stage - used to display the file selector.
   * @return The import state button.
   */
  private Control getImportStateButton(final Stage stage) {

    final Button button = new Button("Import State");

    button.setOnAction(event -> {

      final FileChooser fileChooser = new FileChooser();
      final File file = fileChooser.showOpenDialog(stage);

      // Null if no file selected.
      if (file != null) {
        try {

          imageGenerator.loadConfiguration(file);

        } catch (ImageConfigurationLoadException e) {

          displayException(e);

        }
      }
    });

    return button;
  }

  /**
   * Returns the image group. This is the part of the UI that contains the Mandelbrot image, zoom
   * rectangle and zoom amount.
   *
   * @return The image group.
   */
  private Group getImageGroup() {

    final Group group = new Group();

    final Rectangle zoomRectangle = new Rectangle();

    zoomRectangle.setFill(Color.TRANSPARENT);
    zoomRectangle.setStroke(Color.BLUE);
    zoomRectangle.setStrokeWidth(2);
    zoomRectangle.setVisible(false);

    // When the mouse is clicked, set the x and y of the rectangle to
    // the position where the mouse was clicked.
    group.setOnMousePressed((mouseEvent) -> {
      zoomRectangle.setX(mouseEvent.getX());
      zoomRectangle.setY(mouseEvent.getY());
      zoomRectangle.setVisible(true);
    });

    // When the mouse pressed is released, do the zoom action.
    group.setOnMouseReleased((mouseEvent) -> {
      final double rectangleWidth = zoomRectangle.getWidth();
      final double rectangleHeight = zoomRectangle.getHeight();

      zoomRectangle.setVisible(false);
      zoomRectangle.setWidth(0);
      zoomRectangle.setHeight(0);

      // If it is a tiny rectangle, assume the user just clicked the canvas
      // but didn't intend to zoom.
      if (rectangleWidth < 5 || rectangleHeight < 5) {
        return;
      }

      final double rectangleX = zoomRectangle.getX();
      final double rectangleY = zoomRectangle.getY();

      imageGenerator.zoom(canvas.getWidth(), canvas.getHeight(), rectangleX,
          rectangleX + rectangleWidth, rectangleY + rectangleHeight, rectangleY);
    });

    // When the mouse is dragged, update the hight and width of the rectangle.
    group.setOnMouseDragged((mouseEvent) -> {
      final double rectangleWidth = mouseEvent.getX() - zoomRectangle.getX();
      final double widthScaleFactor = canvas.getHeight() / canvas.getWidth();

      zoomRectangle.setWidth(rectangleWidth);
      zoomRectangle.setHeight(rectangleWidth * widthScaleFactor);
    });

    group.getChildren().addAll(canvas, zoomRectangle);

    return group;
  }

  /**
   * Redraw the mandelbrot image.
   */
  private void redrawImage() {

    final GraphicsContext graphicsContext = canvas.getGraphicsContext2D();
    final PixelWriter pixelWriter = graphicsContext.getPixelWriter();

    final double xRange = canvas.getWidth();
    final double yRange = canvas.getHeight();

    // The resolution is the integer value of the width and height (has to be a
    // whole number
    // of pixels).
    final int xResolution = (int) xRange;
    final int yResolution = (int) yRange;

    // Get the image.
    final Color[][] image = imageGenerator.generate(xResolution, yResolution);

    // Write the image to the canvas.
    for (int x = 0; x < image.length; x++) {
      for (int y = 0; y < image[0].length; y++) {

        // The y axis is flipped (positive axis is in the direction of top to bottom
        // of screen, whereas complex plane has a y axis where the positive direction
        // is upwards), therefore we need to invert the image on the y-axis. That being
        // said, the image is symmetrical around the x-axis, so this is done for the
        // transformations to be displayed correctly - the initial image would look
        // identical without this correction.
        pixelWriter.setColor(x, yResolution - y, image[x][y]);
      }
    }

    // Display the zoom value if this is set in the configuration.
    if (imageGenerator.getCurrentConfigurationProperty().getValue().getOverlayZoom()) {
      graphicsContext.setStroke(Color.BLACK);
      graphicsContext.setFill(Color.WHITE);
      graphicsContext.setFont(new Font(22));
      graphicsContext.setLineWidth(2);

      final String currentZoomText =
          String.format("Zoom: %.2f", imageGenerator.getCurrentZoom(xRange, yRange));

      // Use white text with a black outline. This ensures it can be read
      // on any coloured background.
      graphicsContext.strokeText(currentZoomText, 10, 30);
      graphicsContext.fillText(currentZoomText, 10, 30);
    }
  }
}
