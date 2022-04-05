FROM openjdk:11
ARG JAR_FILE=target/newrelic-challenge-1.0.jar
COPY texts /texts
COPY ${JAR_FILE} newrelic-challenge-1.0.jar
ENTRYPOINT ["java","-jar","/newrelic-challenge-1.0.jar"]
