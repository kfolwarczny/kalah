# Kalah game

Simple implementation of Kalah game see ->> https://en.wikipedia.org/wiki/Kalah
The app is build in Spring Boot on Java 17.

Required external resources: MongoDB as game storage

System is monitored via Prometheus and Grafana.

All API documentation could be found on http://localhost:8080/swagger-ui.hmtl

## How to build
To build the app just execute `mvn clean install`

## Docker 
The app and all required things is dockerized, to use docker please execute command:
`docker-compose up -d` in root directory. This will lunch:
- mongoDB --> 27017
- prometheus --> 9090
- grafana --> 3000
- kalah app --> 8080


TODOs:
- implement all left game corner cases like putting a stone in empty pit.
- add bussiness KPIs to monitor them in Grafana like number of games, total number of players, etc.
