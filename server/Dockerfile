
FROM eclipse-temurin:21-jdk-alpine

WORKDIR /deployments

# Copy files directly from the build context
COPY lib/ /deployments/lib/
COPY app/ /deployments/app/
COPY quarkus/ /deployments/quarkus/
COPY quarkus-run.jar /deployments/quarkus-run.jar

EXPOSE 8083

ENV JAVA_OPTIONS="-Dquarkus.http.host=0.0.0.0 -Djava.util.logging.manager=org.jboss.logmanager.LogManager"

CMD ["java", "-jar", "quarkus-run.jar"]