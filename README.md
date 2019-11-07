# Spring Resilience Example

This is a simple mini-microservice example that showcases resilience functionalities  using Spring Boot & Spring Cloud Netflix Hystrix.
A sample app, `resilientApp`, will use Circuit Breaker interacting with `failingApp`. `resilientApp` has a Hystrix Dashboard running, so the current state of the Circuit Breaker can be observed easily.

## ResilientApp

`resilientApp` gives callers recommendations on what to wear in a given Location. It accepts a location ID and returns an appropriate outfit. The appropriate outfit is decided upon according to the current temperature at the specified location. The most recent temperature reading is retrieved from `failingApp`, which is likely to fail.
In order to handle failing calls, Hystrix Circuit Breaker is used, defaulting to a fixed temperature value.

## FailingApp

`failingApp` gives callers the most recent temperature reading for a given location. It fails faily often thanks to serious monkey business:

* ChaosMonkey: For 50% of all calls throws a `RuntimeException`, otherwise does nothing.
* LatencyMonkey: For 50% of all calls waits for 15 seconds, otherwise does nothing.

## How to run

1. Clone this repository
2. Start `failingApp` (`cd failingApp && mvn spring-boot:run`)
3. Start `resilientApp` (`cd resilientApp && mvn spring-boot:run`)
4. Perform calls against `resilientApp` (`curl localhost:8080/recommender/outfit/123`)
5. Access [Hystrix Dashboard](http://localhost:8080/hystrix/monitor?stream=http%3A%2F%2Flocalhost%3A8080%2Factuator%2Fhystrix.stream&title=Resilient%20App) to observe the status of the circuits.
6. To perform a number of concurrent requests, run `./perform_GETs.sh`.

## Endpoints

* `resilientApp`: `localhost:8080/recommender/outfit/<id>`
* `failingApp`: `http://localhost:8081/locations/<id>/temperature`


## TBD

* Use Feign to make REST call to FailingApp
* Play around with various Circuit Breaker configs

