FROM azul/zulu-openjdk-alpine:11
LABEL authors="bartek"
VOLUME /tmp
COPY /target/PhotoAppApiUsers-0.0.1-SNAPSHOT.jar users.jar

ENTRYPOINT ["java", "-jar","users.jar"]