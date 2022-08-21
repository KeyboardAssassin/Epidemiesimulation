
# ![Banner](https://i.ibb.co/89XDJFZ/Epidemiesimulation-Backend.png)
## Konzeption und Implementierung einer Epidemiesimulation

[![Build Status](https://travis-ci.org/joemccann/dillinger.svg?branch=master)](https://travis-ci.org/joemccann/dillinger) ![enter image description here](https://img.shields.io/github/last-commit/keyboardassassin/Epidemiesimulation)

Hierbei handelt es sich um eine interaktive Epidemiesimulation, bei welcher der Nutzer Maßnahmen zur Eindämmung dynamisch selbst wählen kann.

- Entwicklung mit Java 17 (17.0.3)
- Gradle Projektstruktur mit externen Libraries (und JUnit Testing)
- Betreut durch Herrn. Prof. Dr. Jörg Sahm & Frau. Anja Haußen
- Darstellung/Interaktion im Browser durch Vue.js ([Frontend](https://github.com/KeyboardAssassin/Corona-Simulation-Frontend))

## Key Features

- Working simulation with every day infection calculation depending on
    - population density
    - infection date
    - vaccination
    - social distancing and contact restrictions
- Rest-API (Spring Framework) for transfer to the Frontend
- Statistics
    - history of recovered, history of currently infected
    - current infections, incidence, r-value, death cases
- Possibility to activate Measures
    - vaccination
        - development, deployment
    - medication
        - development, usage
    - social distancing
    - contact restrictions with parameters
        - level (city, state or country)
        - amount of days


## Technologies

Corona Simulation (backend) currently uses:

- [Java 17] 			- Backend programming language
- [Gradle 7.4.2] 	- Project structure - Build tool
- [Gson 2.9.0] 	- Better JSON Handling library
- [Spring] 			- Framework for API Requests (data transfer with [Frontend](https://github.com/KeyboardAssassin/Corona-Simulation-Frontend))
- [Junit 5.8.2] 	- Testing library

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

## Usage of the API
![Example Requests](https://i.ibb.co/0DxjMvR/rsz-carbon.png)

All usable requests can be found at the playground.http