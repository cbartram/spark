<p align="center">
    <img height="320" width="320" src="https://static.dribbble.com/users/889542/screenshots/3888121/lightning_bolt_logo_dribbble-03.jpg" alt="logo" />
</p>

# Spark

![Travis (.com)](https://img.shields.io/travis/com/cbartram/spark)
![Travis (.com)](https://img.shields.io/static/v1?label=Version&message=1.0.0&color=brightgreen)
![GitHub](https://img.shields.io/github/license/cbartram/spark)

Spark is an open source automation framework for modifying and retrieving values in an Old School RuneScape Client. 
This library can be used to reverse engineer the Runescape client or serve as a foundation to creating a Bot
or Enhanced Client.

## Installation

This project uses Java 8 and is built with [Gradle](https://gradle.org/). You can install Java with [Homebrew](https://brew.sh)
using the following command: 

```shell script
$ brew cask install java
$ brew tap adoptopenjdk/openjdk
$ brew cask install adoptopenjdk8
```

In order to download and add the necessary dependencies to the classpath you should install [Gradle](https://gradle.org)
with [Homebrew](https://brew.sh)

```shell script
$ brew install gradle
```

### Building the Project

To build the project run: 

```shell script
$ gradle build
```

## Usage

To use spark simply 

```java

class Main {
public static void main(String[] args) {
        System.out.println("TODO");
    }
    
}
```

## Running Tests

To run the unit tests simply run:

```shell script
$ gradle test
```


### Integration Tests

This section will be filled out at a later date.

### Performance Tests

This section will be filled out at a later date.


## Deploying

This section will be filled out at a later date.


## Built With

Spark was built with the following tools!

- **Java 8** - Programming Language Used
- **Gradle** - Build tool and dependency management
- **ASM** - Bytecode Manipulation Library

## Contributing

Pull requests are welcome. For major changes, please open an issue first to discuss what you would like to change.

Please make sure to update tests as appropriate.

## License
[MIT](https://github.com/cbartram/spark/master/blob/LICENSE.txt/)

## Acknowledgements

- **ASM** For making a great Bytecode manipulation library
