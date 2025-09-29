# ---- build stage ----
FROM maven:3.9.9-eclipse-temurin-21 AS build
WORKDIR /src
COPY . .
# build only the module (adjust name if different)
RUN mvn -q -DskipTests -pl taskservice -am package

# ---- runtime stage ----
FROM eclipse-temurin:21-jre-alpine
WORKDIR /app
# copy the jar produced by the module
COPY --from=build /src/taskservice/target/*.jar /app/app.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar","/app/app.jar"]