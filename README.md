[![build](https://img.shields.io/travis/seekaddo/movies-REST-webapp.svg)](https://travis-ci.org/seekaddo/movies-REST-webapp)
## Java Movie REST Service

This is a simple REST Service for the Movies in in San Francisco

Data source: https://data.sfgov.org/Culture-and-Recreation/Film-Locations-in-San-Francisco/yitu-d5am

# REST-Service Server
Server: Tomcat server 9 with Jersy Servlet-container
Test: using  JerseyTest and grizzly
Json API: Json.org
Configured in IntelliJ Ultimate.



# Run the Application

There are 3 was to run this project.
1. Import it using maven into any IDEA or IntelliJ
2. Use the Eclipse IDEA for Java and import with maven.

You can test the programm after starting the tomcat server.
In your terminal:
http://localhost:8080/rest/sfmservice     #without filter support to output all the movies in json.
http://localhost:8080/rest/sfmservice/180 # with filter support 

3. Test this using maven. Just forget about the whole set up complications and run
mvn clean test


3. Tomcat maven deploy plugin is not configured or support.


# How I tackled this challenge under 3 hours (part from the readme part)

I read the task/challenge and tired to familiarised myself with the Json API, by justing trying it out in the browser
with and without parameters(filters).
After that I downloaded the tomcat server 9.0 and configured it in IntelliJ. (No Jboss server, why: lightweight and Jboss server configurations takes more time compared to tomcat)

I used maven for rapid protoyping with the following configurations.

```bash
mvn archetype:generate -DarchetypeArtifactId=jersey-heroku-webapp  
 -DarchetypeGroupId=org.glassfish.jersey.archetypes -DinteractiveMode=false 
 -DgroupId=com.moviesREST -DartifactId=movies-REST-webapp -Dpackage=com.moviesREST 
 -DarchetypeVersion=2.27
 
```