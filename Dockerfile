FROM dockerfile/java:oracle-java8

# Install maven
RUN apt-get update
RUN apt-get install -y maven

# Polish locale
RUN locale-gen pl_PL.UTF-8
ENV LANG pl_PL.UTF-8

WORKDIR /code

# Prepare by downloading dependencies
ADD pom.xml /code/pom.xml
RUN ["mvn", "dependency:resolve"]
RUN ["mvn", "verify"]

# Adding source, compile and package into a fat jar
ADD src /code/src
RUN ["mvn", "clean"]
RUN ["mvn", "package"]

EXPOSE 4567
CMD ["/usr/lib/jvm/java-8-oracle/bin/java", "-jar", "target/random-people-jar-with-dependencies.jar"]
