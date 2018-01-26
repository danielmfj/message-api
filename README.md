## Simple Message API

### Tech stack
- JDK8
- Spring Boot
- Spring Security
- JUnit 4
- Thymeleaf
- Bootstrap
- Docker
- Maven Wrapper

### Instructions
Check the project out:
```
$ git clone https://github.com/danielmfj/message-api.git
```
Run the Maven Wrapper:
```
$ cd message-api
$ ./mvnw spring-boot:run
```
Web-application and API will be accessible from:```http://localhost:8080/```

### API Endpoints
```
Add message               - {[/message/add],methods=[POST],consumes=[application/json;charset=UTF-8]}
Edit message              - {[/message/edit],methods=[POST],consumes=[application/json;charset=UTF-8]}
Remove message            - {[/message/remove/{messageId}],methods=[POST]}
Retrieve messages by user - {[/user/{userId}/messages],methods=[GET]}
Retrieve all messages     - {[/messages],methods=[GET]}
```

Note: User must be registered to use the application
