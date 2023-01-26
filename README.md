# Customer Rewards Service

This application calculates the rewards for customer based on their transactions.

### How to build
```
./gradlew clean build
```

### How to run all tests
```
./gradlew clean test
```

### How to run the application
```
./gradlew bootRun
```

### How to access API Docs
Once you run your application locally, Tomcat server will start at port ```8080```. You can then access the APIs from below Swagger url
* Swagger URL: http://localhost:8080/customer-reward/api/swagger-ui/index.html

### How to access H2 database
H2 database client can be accessed with the following url. The DB password is in ```application.yml``` file
* H2 DB URL: http://localhost:8080/customer-reward/api/h2-console/login.jsp

PS: This application does not implement any authentication/authorization for securing the endpoints as it has not been mentioned in the coding assignment.