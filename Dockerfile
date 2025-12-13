# Stage 1: Build the application and create a custom JRE
FROM eclipse-temurin:21-jdk-alpine AS builder

# Install jdeps for module analysis
RUN apk add --no-cache binutils

WORKDIR /app

# Copy build files
COPY mvnw .
COPY .mvn/ .mvn/
COPY pom.xml .

# Download dependencies (cached unless pom.xml changes)
RUN chmod +x mvnw && \
    ./mvnw dependency:go-offline -B

# Copy source code
COPY src/ src/

# Build the application and extract dependencies
RUN ./mvnw clean package -DskipTests -B && \
    mkdir -p target/dependency && \
    (cd target/dependency; jar -xf ../*.jar) && \
    jdeps --ignore-missing-deps \
          --multi-release 21 \
          --print-module-deps \
          --class-path 'target/dependency/BOOT-INF/lib/*' \
          target/*.jar > jre-deps.info

# Create a custom JRE with only required modules
RUN jlink --add-modules $(cat jre-deps.info),jdk.crypto.ec,jdk.crypto.cryptoki,jdk.localedata \
          --strip-debug \
          --no-man-pages \
          --no-header-files \
          --compress=2 \
          --output /custom-jre

# Extract application layers
RUN java -Djarmode=layertools -jar target/*.jar extract

# Final stage
FROM alpine:3.18

# Install minimal dependencies
RUN apk add --no-cache tzdata

# Create non-root user and group
RUN addgroup -S spring && adduser -S spring -G spring

# Set timezone (change as needed)
ENV TZ=Asia/Ho_Chi_Minh

# Set working directory
WORKDIR /app

# Copy custom JRE from builder
COPY --from=builder /custom-jre /opt/java/openjdk

# Set environment variables
ENV JAVA_HOME=/opt/java/openjdk
ENV PATH="${JAVA_HOME}/bin:${PATH}"

# Copy application layers from builder
COPY --from=builder --chown=spring:spring /app/dependencies/ ./
COPY --from=builder --chown=spring:spring /app/spring-boot-loader/ ./
COPY --from=builder --chown=spring:spring /app/snapshot-dependencies/ ./
COPY --from=builder --chown=spring:spring /app/application/ ./

# Switch to non-root user
USER spring:spring

# Expose application port
EXPOSE 8080

# JVM options (optimized for containerized environments)
ENV JAVA_OPTS="\
  -XX:+UseContainerSupport \
  -XX:MaxRAMPercentage=75.0 \
  -XX:+ExitOnOutOfMemoryError \
  -XX:+UseZGC \
  -XX:+ZUncommit \
  -XX:ZUncommitDelay=300 \
  -XX:MaxGCPauseMillis=200 \
  -Djava.security.egd=file:/dev/./urandom \
  -Dspring.config.location=optional:classpath:/,optional:file:/app/config/"

# Health check
HEALTHCHECK --interval=30s --timeout=3s --start-period=10s --retries=3 \
  CMD wget --no-verbose --tries=1 --spider http://localhost:8080/actuator/health || exit 1

# Entrypoint
ENTRYPOINT ["java", "org.springframework.boot.loader.launch.JarLauncher"]