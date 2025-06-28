# builder
FROM gradle:8.5-jdk21 AS builder
WORKDIR /workspace

COPY build.gradle.kts .
COPY settings.gradle.kts .
COPY gradle.properties .

COPY june/ june/

RUN gradle :june:bootJar --no-daemon

# runtime
FROM eclipse-temurin:21-jre-jammy
WORKDIR /app

COPY --from=builder /workspace/june/build/libs/*.jar app.jar

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
