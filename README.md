# Mandelbrot Explorer

[![Continuous Integration](https://github.com/kiancross/mandelbrot/actions/workflows/continous-integration.yaml/badge.svg?event=push)](https://github.com/kiancross/mandelbrot/actions/workflows/continous-integration.yaml)
[![Continuous Integration](https://github.com/kiancross/mandelbrot/actions/workflows/codeql.yaml/badge.svg?event=schedule)](https://github.com/kiancross/mandelbrot/actions/workflows/codeql.yaml)
[![codecov](https://codecov.io/gh/kiancross/mandelbrot/branch/master/graph/badge.svg?token=cFjwBIoJ4c)](https://codecov.io/gh/kiancross/mandelbrot)

A [Mandelbrot set](https://en.wikipedia.org/wiki/Mandelbrot_set) explorer
written in Java.

 * [Usage](#usage)
 * [Gallery](#gallery)
 * [Developers](#developers)
 * [License](#license)

## Usage

The options available on the user interface are described
below.

|Option/Button Name| Description |
|------------------|-------------|
| Undo | Undoes the previous action. |
| Redo | Redoes the undone action. |
| Reset All | Resets all options back to default. |
| Export State | Exports the explorer's state. |
| Import State | Imports a previously exported explorer state. |
| Export Image | Exports the current image as a PNG. |
| Overlay Zoom | Overlay the zoom factor at the top left of the display. |
| Colour Scheme | The colour scheme to use for the display. |
| Pan X Amount | The amount to pan the display in the horizontal direction when the 'Pan X' button is pressed. |
| Pan X | Pan the display in the horizontal direction. |
| Pan Y Amount | The amount to pan the display in the vertical direction when the 'Pan Y' button is pressed. |
| Pan Y | Pan the display in the vertical direction. |
| Maximum Iterations | The maximum number of iterations to use when checking if an initial value will cross the 'Escape Radius'. |
| Escape Radius | An arbitrary threshold used to colour code the display, depending on how quickly the threshold is crossed. |

## Gallery
### Capillary
![Mandelbrot Set Example Capillary](https://github.com/kiancross/mandelbrot/blob/master/examples/capillary.png)

### Bulb
![Mandelbrot Set Example Bulb](https://github.com/kiancross/mandelbrot/blob/master/examples/bulb.png)

### Eco
![Mandelbrot Set Example Eco](https://github.com/kiancross/mandelbrot/blob/master/examples/eco.png)

### Eco Spiral
![Mandelbrot Set Example Eco Spiral](https://github.com/kiancross/mandelbrot/blob/master/examples/eco-spiral.png)

### Fire
![Mandelbrot Set Example Fire](https://github.com/kiancross/mandelbrot/blob/master/examples/fire.png)

### Ocean
![Mandelbrot Set Example Ocean](https://github.com/kiancross/mandelbrot/blob/master/examples/ocean.png)

### Black and White
![Mandelbrot Set Example Black and White](https://github.com/kiancross/mandelbrot/blob/master/examples/black-and-white.png)

### Greyscale
![Mandelbrot Set Example Grey](https://github.com/kiancross/mandelbrot/blob/master/examples/grey.png)

## Developers
Developers can use the `./gradlew` script to build and test the
application. `./gradlew tasks` will show all of the available
commands and their descriptions.

Developer documentation is available [here](kiancross.github.io/mandelbrot/).

## License

Code in this repository is licensed under the
[MIT license](https://github.com/kiancross/mandelbrot/blob/master/LICENSE).
