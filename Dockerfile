# ---------- Build stage ----------
FROM maven:3.9.9-eclipse-temurin-17 AS build

WORKDIR /app

# Copy Maven files first for better Docker layer caching
COPY pom.xml .

RUN mvn dependency:go-offline -B

# Copy source code
COPY src ./src

# Build the application
RUN mvn clean package -DskipTests


# ---------- Runtime stage ----------
FROM eclipse-temurin:17-jre

WORKDIR /app

# Copy the built JAR from the build stage
COPY --from=build /app/target/*.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]