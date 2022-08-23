
# ![Banner](https://i.ibb.co/1MmG2Jk/Backend.png)
## Konzeption und Implementierung einer Epidemiesimulation

![Last Commit](https://img.shields.io/github/last-commit/keyboardassassin/Epidemiesimulation)

This is an interactive epidemic simulation in which the user can dynamically choose containment measures himself.

- Development with Java 17 (17.0.3)
- Gradle project structure with external libraries (and JUnit testing)
- Supervised by Mr. Prof. Dr. Jörg Sahm & Mrs. Math. Dipl. Anja Haussen
- Display/interaction in the browser by Vue.js ([Frontend](https://github.com/KeyboardAssassin/Corona-Simulation-Frontend))

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
![Example Requests](https://i.ibb.co/s2c7GvN/carbon-2.png)

All usable requests can be found at the playground.http