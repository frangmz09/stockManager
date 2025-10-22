# Etapa 1: Build
FROM maven:3.9.5-eclipse-temurin-17 AS build

WORKDIR /app

# Copiar pom.xml y descargar dependencias primero para caching
COPY pom.xml .
RUN mvn dependency:go-offline

# Copiar código fuente y compilar
COPY src ./src
RUN mvn clean package -DskipTests

# Etapa 2: Runtime
FROM eclipse-temurin:17-jdk AS runtime

WORKDIR /app

# Copiar jar compilado
COPY --from=build /app/target/stockManager-0.0.1-SNAPSHOT.jar app.jar

# Copiar agente New Relic
COPY newrelic /app/newrelic

# Crear directorio de logs de New Relic con permisos
RUN mkdir -p /app/newrelic/logs

# Variables de entorno para New Relic
# Se pueden pasar al ejecutar el contenedor o usar valores por defecto
ENV NEW_RELIC_APP_NAME=${NEW_RELIC_APP_NAME}
ENV NEW_RELIC_LICENSE_KEY=${NEW_RELIC_LICENSE_KEY}

# Exponer puerto de la app
EXPOSE 8080

# Ejecutar app con New Relic
ENTRYPOINT ["java", "-javaagent:/app/newrelic/newrelic.jar", "-jar", "app.jar"]
