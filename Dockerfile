# Etapa 1: Build
FROM maven:3.9.5-eclipse-temurin-17 AS build

WORKDIR /app
COPY pom.xml .
RUN mvn dependency:go-offline
COPY src ./src
RUN mvn clean package -DskipTests

# Etapa 2: Runtime
FROM eclipse-temurin:17-jdk-alpine
WORKDIR /app

# Copiar jar de la app
COPY --from=build /app/target/stockManager-0.0.1-SNAPSHOT.jar app.jar

# Copiar agente New Relic
COPY newrelic /app/newrelic

# Variables de entorno (pueden ser sobreescritas en Render)
ENV NEW_RELIC_APP_NAME=${NEW_RELIC_APP_NAME}
ENV NEW_RELIC_LICENSE_KEY=${NEW_RELIC_LICENSE_KEY}

EXPOSE 8080

# Ejecutar app con New Relic
ENTRYPOINT ["java", "-javaagent:/app/newrelic/newrelic.jar", "-jar", "app.jar"]
