# Bachelorarbeit 
## Konzeption und Implementierung einer Epidemiesimulation

[![Build Status](https://travis-ci.org/joemccann/dillinger.svg?branch=master)](https://travis-ci.org/joemccann/dillinger)

Hierbei handelt es sich um eine interaktive Virussimulation, bei welcher der Nutzer Maßnahmen zur Eindämmung selbst wählen kann.

- Entwicklung mit Java 15
- Gradle Projektstruktur mit externen Libraries und JUnit Testing
- Betreut durch Herrn. Prof. Dr. Jörg Sahm & Frau. Anja Haußen

## Features

- Working simulation with every day infection calculation
- Overview over a lot of statistics (histories, current infections, r-values)
- Possibility to activate Measures (vaccination development, vaccination deployment ...)


## Tech

Corona Simulation currently uses:

- [Java 15] - Backend!
- [Gradle 7.4.2] - project structure - build tool
- [gson 2.9.0] - Better JSON Handling library
- [Junit 5.8.2] - great testing library

## Installation

Corona-Simulation doesn't need anything to be compiled other than a Java SDK.

On windows just run the gradlew build command.

```sh
gradlew build
java -jar build\libs\corona-simulation.jar
```

On Linux/Mac it is pretty similar.

```sh
./gradlew build
java -jar build\libs\corona-simulation.jar
```