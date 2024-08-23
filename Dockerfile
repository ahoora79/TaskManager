# Stage 1: Build
FROM eclipse-temurin:19.0.2_7-jre-jammy
#MAINTAINER BALOOT_DEVELOPER
ADD target/TaskManager-0.0.1-SNAPSHOT.jar TaskManager.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar","TaskManager.jar"]