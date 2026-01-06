from gradle:8.3-jdk17 as build

WORKDIR /app

COPY gradlew .
COPY gradle gradle
copy build.gradle .
COPY settings.gradle .

COPY src src

run ./gradlew clean bootjar --no-daemon

from eclipse-temurin:17-jdk-alpine
WORKDIR /app

COPY --from=build /app/build/libs/q4anda-0.0.1.jar .
expose 8080

ENTRYPOINT ["java","-jar","q4anda-0.0.1.jar"]