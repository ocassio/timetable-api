FROM openjdk:8-jdk-alpine

RUN mkdir /timetable-api
WORKDIR /timetable-api

COPY target/timetable-api-1.0-SNAPSHOT.jar .

CMD ["java", "-jar", "timetable-api-1.0-SNAPSHOT.jar"]