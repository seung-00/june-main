# 1단계: 빌드 환경
FROM gradle:8.5-jdk17 AS builder
WORKDIR /workspace

# 루트 설정 파일 복사
COPY build.gradle.kts .
COPY settings.gradle.kts .
COPY gradle.properties .

# june 모듈만 복사
COPY june/ june/

# 빌드
RUN gradle :june:bootJar --no-daemon

# 2단계: 실행 환경
FROM eclipse-temurin:17-jre-jammy
WORKDIR /app

COPY --from=builder /workspace/june/build/libs/*.jar app.jar

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
