# Stage 1: Build with Gradle
FROM gradle:jdk17 AS build
WORKDIR /app
COPY . /app
RUN gradle clean build -x test -x spotlessJavaCheck -x spotbugsMain

# Stage 2: Run with Java 17
FROM eclipse-temurin:17-jdk-alpine
COPY --from=build /app/build/libs/*.jar app.jar
ENTRYPOINT ["java","-jar","/app.jar"]