# ====== MODE DÉVELOPPEMENT ======
FROM maven:3.9-eclipse-temurin-21 AS dev

WORKDIR /app

# Copie les fichiers de dépendances d'abord (cache Docker)
COPY pom.xml .
RUN mvn dependency:go-offline -B

# Copie le reste du code
COPY src ./src

# Lance avec spring-boot-devtools pour le hot-reload
CMD ["mvn", "clean", "spring-boot:run", "-Dspring-boot.run.jvmArguments=-Dspring.devtools.restart.enabled=true"]

# Staging

FROM dev AS build
RUN mvn package -DskipTests

FROM eclipse-temurin:21-jre-alpine AS staging
WORKDIR /app
COPY --from=build /app/target/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
