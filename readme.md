
# Calculator Plugin for Paper
 
My first plugin that i quickly made for different calculations. More will be added in the future.
## Features

- **Mathematical Calculator** (`/calc`)
    - Evaluate complex mathematical expressions in-game
    - Support for basic arithmetic operations (addition, subtraction, multiplication, division)
    - Advanced mathematical functions (sin, cos, tan, log, sqrt, etc.)
    - Exponentiation and parentheses for operation order
    - User-friendly error messages for invalid expressions

- **Coordinate Converter** (`/coord`)
    - Convert coordinates between Nether and Overworld dimensions
    - Automatically applies the 1:8 scale ratio between dimensions
    - Preserves Y-coordinate values
    - Tab completion suggests current player coordinates

- **Distance Calculator** (`/distance`)
    - Calculate the exact Euclidean distance between two 3D points
    - Useful for planning builds and travel routes
    - Tab completion suggests current player coordinates

- **Stack Calculator** (`/stack`)
    - Convert between item stacks and individual items
    - Support for custom stack sizes (default: 64)
    - Calculate total items from stacks and remainder
    - Calculate stacks and remainder from total items
    - Useful for inventory management and resource planning

- **Quality of Life Features**
    - Tab completion for all commands
    - Colored output for better readability

## Requirements

- Java 21 or higher
- Paper Minecraft server 1.21.x

## Installation

1. Download the latest release JAR file from the [Releases](https://github.com/KreativeName1/Calculator-MC-Paper/releases) page
2. Place the JAR file in your server's `plugins` directory
3. Restart your server or use a plugin manager to load the plugin

## Building from Source

### Prerequisites

- Java Development Kit (JDK) 21 or higher
- Git

### Clone the Repository

```bash
git clone https://github.com/KreativeName1/Calculator-MC-Paper.git
cd Calculator-MC-Paper
```

Build with Gradle
The project uses Gradle as its build system. You can build the project using the included Gradle wrapper:
```bash
./gradlew shadowJar
```
The compiled JAR file will be located in the build/libs directory.

## Usage

### Calculator Command

**Basic Usage:**
```
/calc <expression>
```

**Examples:**
```
/calc 2+2
/calc sin(45) * cos(30)
/calc 5^2 + sqrt(16)
/calc 10/3
/calc log(100)
```

### Coordinate Converter

Convert coordinates between the Nether and Overworld dimensions.

**Basic Usage:**
```
/coord [nether|overworld] <x> <y> <z>
```

**Examples:**
```
/coord nether 800 64 -1200     # Converts Overworld coordinates to Nether
/coord overworld 100 64 -150   # Converts Nether coordinates to Overworld
```

**Aliases:** `/coordinates`

### Distance Calculator

Calculate the Euclidean distance between two 3D points.

**Basic Usage:**
```
/distance <x1> <y1> <z1> <x2> <y2> <z2>
```

**Examples:**
```
/distance 0 64 0 100 70 200    # Calculates distance between two points
/distance -500 40 300 200 80 -100
```

**Aliases:** `/dist`

### Stack Calculator

Convert between item stacks and individual items with customizable stack sizes.

**Basic Usage:**
```
/stack toStacks <items> [stackSize]
/stack toItems <stacks> [stackSize]
/stack toItems <stacks> <remainder> [stackSize]
```

**Examples:**
```
/stack toStacks 128           # Converts 128 items to stacks (default stack size: 64)
/stack toStacks 320 16        # Converts 320 items to stacks with stack size of 16
/stack toItems 5              # Converts 5 stacks to items (default stack size: 64)
/stack toItems 3 16           # Converts 3 stacks to items with stack size of 16
/stack toItems 2 40           # Converts 2 stacks and 40 items to total items
/stack toItems 4 10 16        # Converts 4 stacks and 10 items to total items (stack size: 16)
```

**Aliases:** `/stacks`

## Dependencies

This plugin uses the following libraries:
- [exp4j](https://www.objecthunter.net/exp4j/) - For evaluating mathematical expressions

## Development

This project is built using:
- Java 21
- Gradle build system
- Paper API 1.21.5

### Project Structure

- `src/main/java/` - Java source files
- `src/main/resources/` - Resource files including plugin.yml
- `build.gradle` - Gradle build configuration

### Setting Up Development Environment

1. Clone the repository
2. Import the project into your IDE as a Gradle project
3. Run `./gradlew runServer` to start a development server with the plugin

## License

This project is licensed under the [MIT License](LICENSE).

