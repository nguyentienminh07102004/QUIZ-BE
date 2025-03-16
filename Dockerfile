FROM 3.9.6-amazoncorretto-17-debian AS build
WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN mvn clean package -DskipTests

FROM openjdk:17-ea-oracle
WORKDIR /app
COPY --from=build /app/target/*.war app.war

ENTRYPOINT ["java", "-jar", "app.war"]