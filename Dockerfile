FROM openjdk:21 as builder

WORKDIR /app

COPY ./.mvn ./.mvn
COPY ./mvnw .
COPY ./pom.xml .
RUN ./mvnw clean package -Dmaven.test.skip -Dmaven.main.skip -Dspring-boot.repackage.skip && rm -r ./target/

COPY ./src ./src

RUN ./mvnw clean package -DskipTests
FROM openjdk:21

WORKDIR /app

COPY --from=builder /app/target/esperar_app-0.0.1-SNAPSHOT.jar .
ENV PORT 7000
EXPOSE $PORT

ENTRYPOINT ["java", "-jar", "./esperar_app-0.0.1-SNAPSHOT.jar"]