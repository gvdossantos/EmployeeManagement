FROM eclipse-temurin:21-jre-alpine
WORKDIR /app
COPY target/employee-management-0.0.1-SNAPSHOT.jar /app/emp-management.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "emp-management.jar"]
