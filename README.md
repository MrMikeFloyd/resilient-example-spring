# Spring Resilience Example

This is a simple mini-microservice example that showcases resilience functionalities  using Spring Boot & Spring Cloud Netflix Hystrix.
A sample app, `resilientApp`, will use Circuit Breaker interacting with `failingApp`. `resilientApp` has a Hystrix Dashboard running, so the current state of the Circuit Breaker can be observed easily.

## ResilientApp

`resilientApp` gives callers recommendations on which places to visit (according to their popularity) and what to wear in these places (according to their outside temperature). For making recommendations, `resilientApp` retrieves data on the popularity and the current temperature for a given location from another application, `failingApp`, which is likely to fail.
In order to handle failing calls, Hystrix Circuit Breaker is used, using fallback methods to give callers a timely response.
`resilientApp` exposes 2 endpoints:

* `localhost:8080/recommender/<location>/visit` For advice on whether or not to visit a given location id
* `localhost:8080/recommender/<location>/outfit` For outfit recommendations for a given location id


## FailingApp

`failingApp` exposes 2 endpoints, one for a location's popularity score, and one for a location's most recent temperature reading:

* `localhost:8081/locations/123/popularity` For popularity scores (fairly stable)
* `localhost:8081/locations/123/temperature` For temperature readings (_unstable_)

While the popularity score endpoint is fairly stable, the resource providing temperature readings fails faily often thanks to some serious monkey business:

* _ChaosMonkey_: For 50% of all calls throws a `RuntimeException`, otherwise does nothing.
* _LatencyMonkey_: For 50% of all calls waits for 15 seconds, otherwise does nothing.

## How to run

1. Clone this repository
2. Start `failingApp` (`cd failingApp && mvn spring-boot:run`)
3. Start `resilientApp` (`cd resilientApp && mvn spring-boot:run`)
4. Perform calls against `resilientApp` (`curl localhost:8080/recommender/123/outfit`)
5. Access [Hystrix Dashboard](http://localhost:8080/hystrix/monitor?stream=http%3A%2F%2Flocalhost%3A8080%2Factuator%2Fhystrix.stream&title=Resilient%20App) to observe the status of the circuits.
6. To perform a number of concurrent requests, run `./perform_GETs.sh`.

## Further reading

More on Hystrix [here](https://cloud.spring.io/spring-cloud-netflix/multi/multi__circuit_breaker_hystrix_clients.html).

