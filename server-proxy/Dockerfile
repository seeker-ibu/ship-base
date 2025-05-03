FROM openjdk:8-jdk-alpine
WORKDIR /app
COPY target/serverproxy-1.0-SNAPSHOT.jar serverproxy.jar
COPY common-1.0.0.jar common.jar
CMD ["java", "-cp", "serverproxy.jar:common.jar", "main.java.com.serverproxy.main.ServerProxyMain"]

