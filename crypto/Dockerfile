# Phase de build
FROM maven:3.8.4-openjdk-17 AS build

WORKDIR /app

# Copier le pom.xml et le répertoire src
COPY pom.xml .
COPY src ./src

# Compiler l'application sans exécuter les tests
RUN mvn clean install -DskipTests

# Phase d'exécution
FROM openjdk:17-jdk-slim

WORKDIR /app

# Copier le fichier JAR depuis la phase de build
COPY --from=build /app/target/crypto-0.0.1-SNAPSHOT.jar app.jar

# Copier les ressources (templates et static)
COPY --from=build /app/src/main/resources/templates /app/templates
COPY --from=build /app/src/main/resources/static /app/static

# Exposer le port 8080
EXPOSE 8080

# Démarrer l'application
ENTRYPOINT ["java", "-jar", "app.jar"]
