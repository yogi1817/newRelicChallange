# Mac vs windows
If you are running this on mac mvn clean package will work, if you are running on windows you might have to say ./mvnw clean package. Also make sure mvn is configured in the machine where you are running this.

# Open API and Swagger feature used
If you take a look into the api folder you will notice a yml file that has request and response of the application.
pom.xml has a plugin for open api generator, that generates all supporting classes for request and response.

Also in the config package there is a SwaggerConfig file that redirect you to swagger page when you hit localhost:8080

# CustomException example used as well
If you go to exception package you will notice some custom exceptions that I am using in adapters layer
A test case is also written for the same.

# Running the spring boot app

There are 2 ways to run this app:-
1. Run the app as a spring boot main and pass file name as command line params.
```
./mvnw clean package
java -jar target/newrelic-challenge-1.0.jar texts\moby-dick.txt
You will see the logs. Ctrl+C to exit out of it
```

2. Run as a spring boot on 8080 and call api from swagger
```
a. Make sure 8080 port is not running
b. mvn clean package
c. java -jar target/new-relic-challenge-1.0.jar
d. Go to localhost:8080
e. Expand word count method and click on try it out
f. Pass the below body and click run, you will see the response as requested.
{
  "fileNames": [
    "texts\/moby-dick.txt",
    "texts\/brothers-karamazov.txt"
  ]
}
```

3. Run as a docker image
```
a. Make Sure nothng is running on 8080 - netstat -ano | findstr :8080(windows)
b. ./mvnw clean package
c. docker build -t newrelic/challange-docker .
d. docker run -p 8080:8080 newrelic/challange-docker
And then you can access the swagger page as in step 2
```

4. Running from STDIN
```
    Commenting out this code because Spring boot will stop at Stdin and will not run untill an input is provided.
    This can be achieved if I will use plain java and not spring boot framework and this will stop the spring boot run.
    CommandLine.java has the code but commented to make sure spring boot works.
```

## Test
To run the test please run mvn clean verify.

## Thing that has not been considered
1. Taking stdin from command line, right now the user can send command line arguments as shown in step1 but not as stdin
2. Have to considered Unicode specifically.
#   n e w R e l i c C h a l l a n g e  
 