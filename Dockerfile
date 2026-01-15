FROM maven AS builder

WORKDIR /app

COPY mvnw .
COPY .mvn .mvn
COPY pom.xml .

RUN chmod +x mvnw

RUN ./mvnw dependency:go-offline -B

COPY src ./src

RUN ./mvnw clean package

FROM eclipse-temurin:21-jre-alpine
LABEL authors="Bästa Gruppen Java 2025"

WORKDIR /app

COPY --from=builder /app/target/*.jar app.jar

EXPOSE 8080/tcp

ENTRYPOINT ["java", "-jar", "app.jar"]