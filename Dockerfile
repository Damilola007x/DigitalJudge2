# Step 1: Build
FROM maven:3.9.6-eclipse-temurin-21-alpine AS build
COPY . .
RUN mvn clean package -DskipTests

# Step 2: Run
FROM eclipse-temurin:21-jdk-alpine
# This copies the file and renames it to app.jar for simplicity
COPY --from=build /target/*.jar app.jar

EXPOSE 8080

# We use app.jar here because that's what we named it in the line above
ENTRYPOINT java -Dserver.port=8080 -Dgemini.api.key=${GEMINI_API_KEY} -jar app.jar