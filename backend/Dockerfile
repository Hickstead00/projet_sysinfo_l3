# ====== MODE DÉVELOPPEMENT ======
FROM maven:3.9-eclipse-temurin-21 AS dev

WORKDIR /app

# Copie les fichiers de dépendances d'abord (cache Docker)
COPY pom.xml .
RUN mvn dependency:go-offline -B

# Copie le reste du code
COPY src ./src

# Lance avec spring-boot-devtools pour le hot-reload
CMD ["mvn", "spring-boot:run", "-Dspring-boot.run.jvmArguments=-Dspring.devtools.restart.enabled=true"]