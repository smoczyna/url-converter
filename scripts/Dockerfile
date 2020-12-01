FROM adoptopenjdk/openjdk11:latest
RUN mkdir /opt/app
COPY /target/urlshortener-1.0.0.jar /opt/app
CMD ["java", "-jar", "/opt/app/urlshortener-1.0.0.jar"]
