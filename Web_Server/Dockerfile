FROM openjdk:21
WORKDIR /springbootdemo
COPY ./app/build/libs/app-0.0.1-SNAPSHOT.jar app.jar
# 在容器中暴露端口 8080
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]