# ---- build stage ----
FROM maven:3.9.9-eclipse-temurin-21 AS build
WORKDIR /src
COPY . .

# build only the taskservice module, verbose logs, and skip tests/ITs in image build
RUN --mount=type=cache,target=/root/.m2 \
    mvn -B -f taskservice/pom.xml -DskipTests -DskipITs package

# ---- runtime stage ----
FROM eclipse-temurin:21-jre-alpine
WORKDIR /app
COPY --from=build /src/taskservice/target/*.jar /app/app.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar","/app/app.jar"]