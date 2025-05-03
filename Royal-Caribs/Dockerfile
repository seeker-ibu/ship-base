FROM openjdk:8-jdk-alpine
WORKDIR /app
COPY target/royalcaribs-1.0-SNAPSHOT.jar royalcaribs.jar
COPY common-1.0.0.jar common.jar
CMD ["java", "-jar", "royalcaribs.jar"]
CMD ["java", "-cp", "royalcaribs.jar:common.jar", "main.java.com.royalcaribs.main.ClientProxyMain"]