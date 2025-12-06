# Build stage
FROM maven:3.9.6-eclipse-temurin-21 AS build
WORKDIR /app

COPY pom.xml .
RUN mvn dependency:go-offline -B

COPY src ./src
RUN mvn clean package -DskipTests

# Run stage (JRE is lighter than JDK)
FROM eclipse-temurin:21-jre
WORKDIR /app

COPY --from=build /app/target/*.jar app.jar

EXPOSE 8080

CMD ["java", "-Xmx192m", "-Xms128m", "-XX:MaxMetaspaceSize=96m", "-XX:ReservedCodeCacheSize=32m", "-XX:MaxDirectMemorySize=32m", "-XX:MaxRAMPercentage=70", "-jar", "app.jar"]
