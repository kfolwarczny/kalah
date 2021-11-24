FROM maven:3.8.4-eclipse-temurin-17
ADD . /build
WORKDIR /build
CMD mvn spring-boot:run
