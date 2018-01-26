FROM java:8
VOLUME /tmp
ADD message-api.jar message-api.jar
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/message-api.jar"]