FROM maven:3-jdk-8-alpine

WORKDIR /code

ADD pom.xml /code/pom.xml
RUN mvn dependency:resolve

ADD src /code/src

RUN mvn package

CMD ["java", "-Djetty.port=80", "-jar", "target/kdom-service-jar-with-dependencies.jar"]
