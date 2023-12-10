# Morax
Morax is a backend service for Foocation application - Big project in UI UX class that use Kotlin Springboot 

## Important note: 
- This application is deployed at Google Cloud Platform with url base `http://35.198.240.131:8081`. But it doesn't work all day. 
    If you want to call api from this URL, please contact me first via [Facebook account](https://www.facebook.com/ACProbs)
- However, you can run it locally by following this instruction 

## How to run this project on local machine 

### 1. Prerequisite
- [Docker Engine](https://docs.docker.com/engine/install/), [Docker Desktop](https://www.docker.com/products/docker-desktop/) for implementing database server 
- [Java 17 SE](https://www.java.com/en/download/)
- Editor like [Visual Studio Code](https://code.visualstudio.com/download) or IDE like [IntelliJ](https://www.jetbrains.com/idea/download/?section=windows)
- [Gradle](https://gradle.org/install/) for library management
- [Studio 3T](https://studio3t.com/download/) for MongoDB GUI

### 2. How to run Morax application 

#### Checkout to the branch that set up for run locally 

- Run command `git checkout backend-local` to go to project that support run locally

#### Create database server 

- Run `docker-compose up -d` (Window) or `docker compose up`(for Linux or MacOSX) to start MongoDB

#### Run Morax springboot

- Clone project 
- Open project in Text Editor or IDE 
- In terminal run this command: `gradle bootRun`
- After run successfully, go to [Swagger UI](http://localhost:8081/swagger-ui/index.html) to read document for the endpoints 