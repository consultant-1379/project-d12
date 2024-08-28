From openjdk:18-alpine
copy ./target/microservicemanager-0.0.1-SNAPSHOT.jar microservicemanager-0.0.1-SNAPSHOT.jar
CMD ["java","-jar","microservicemanager-0.0.1-SNAPSHOT.jar"]