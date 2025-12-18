# Etapa de construcción optimizada con Alpine
FROM eclipse-temurin:17-jdk-alpine AS build
WORKDIR /app

# Instalar herramientas necesarias
RUN apk add --no-cache bash dos2unix

# Permitir definir la tarea/objetivo de gradle (por defecto omite tests para builds locales)
ARG BUILD_TARGET="bootJar -x test"

# Copiar archivos de configuración de Gradle primero (para aprovechar cache de Docker)
COPY gradle/ gradle/
COPY gradlew gradlew.bat build.gradle settings.gradle lombok.config ./
COPY config/ config/

# Convertir line endings y hacer ejecutable gradlew
RUN dos2unix ./gradlew && chmod +x ./gradlew

# Descargar dependencias (esta capa se cachea si no cambian las dependencias)
RUN ./gradlew dependencies --no-daemon --stacktrace

# Copiar código fuente y compilar usando la tarea parametrizable
COPY src/ src/
RUN ./gradlew ${BUILD_TARGET} --no-daemon --stacktrace --build-cache

# Etapa de ejecución ultra liviana
FROM eclipse-temurin:17-jre-alpine

# Archivo JAR a copiar desde la etapa de build (permite versionado dinámico)
ARG JAR_FILE=/app/build/libs/*.jar

# Crear usuario no root para seguridad
RUN addgroup -g 1001 -S appuser && \
    adduser -u 1001 -S appuser -G appuser && \
    apk add --no-cache curl

USER appuser
WORKDIR /app

# Copiar solo el JAR necesario (usa wildcard para futuras versiones)
COPY --from=build --chown=appuser:appuser ${JAR_FILE} app.jar

# Variables de entorno para optimización JVM
ENV JAVA_OPTS="-Xms128m -Xmx256m -XX:+UseG1GC -XX:+UseContainerSupport -XX:MaxRAMPercentage=75 -Djava.security.egd=file:/dev/./urandom -Dspring.backgroundpreinitializer.ignore=true"

EXPOSE 8080

# Health check para Docker Swarm/Kubernetes
HEALTHCHECK --interval=30s --timeout=3s --start-period=45s --retries=3 \
    CMD curl -f http://localhost:8080/graduate-insights/v1/api/actuator/health || exit 1

ENTRYPOINT ["sh", "-c", "exec java $JAVA_OPTS -jar app.jar"]
