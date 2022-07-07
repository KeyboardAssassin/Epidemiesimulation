# Bachelorarbeit 
## Konzeption und Implementierung einer Epidemiesimulation

[![Build Status](https://travis-ci.org/joemccann/dillinger.svg?branch=master)](https://travis-ci.org/joemccann/dillinger)

Hierbei handelt es sich um eine interaktive Epidemiesimulation, bei welcher der Nutzer Maßnahmen zur Eindämmung dynamisch selbst wählen kann. 

- Entwicklung mit Java 17 (17.0.3)
- Gradle Projektstruktur mit externen Libraries und JUnit Testing
- Betreut durch Herrn. Prof. Dr. Jörg Sahm & Frau. Anja Haußen
- Darstellung/Interaktion im Browser durch Vue.js ([Frontend](https://github.com/KeyboardAssassin/Corona-Simulation-Frontend))

## Features

- Working simulation with every day infection calculation
- Overview over a lot of statistics (histories, current infections, r-values)
- Possibility to activate Measures (vaccination development, vaccination deployment ...)


## Technologies

Corona Simulation currently uses:

- [Java 17] - Backend technology!
- [Gradle 7.4.2] - Project structure - Build tool
- [Gson 2.9.0] - Better JSON Handling library
- [Spring] - Framework for API Requests (data transfer with [Frontend](https://github.com/KeyboardAssassin/Corona-Simulation-Frontend))
- [Junit 5.8.2] - Great testing library

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