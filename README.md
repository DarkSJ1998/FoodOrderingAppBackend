## FoodOrderingAppBackend 

- In this project, I have developed REST API endpoints of various functionalities required for the web app FoodOrderingApp.

- In order to observe the functionality of the endpoints, we will use the Swagger user interface and store the data in the PostgreSQL database. Also, the project has been implemented using Java Persistence API (JPA).

#### Consists of the following Controllers:-
- CustomerController
- AddressController
- RestaurantController
- CategoryController

#### Database Schema:-

> ###### URL : 
- https://images.upgrad.com/21d467d9-58b2-4995-b5a6-5d75f6b0cef9-db-schema-college-capstone.jpg

#### Steps to setup the application :-

##### Following are the steps to create the database:
- Create a database in your Postgres named “restaurantdb”.
- Run the following command in the IntelliJ terminal, where your stub file of the project is open: “mvn clean install -Psetup -DskipTests”.
- This will automatically create tables in your database and populate them with sample data, which we have already provided.

> Note: Whenever you want to create a new database, you can just follow step 2.

---

##### Once the required tables and database schema are created in the database, you may configure the database in the Spring Boot project by making the required changes in the following instructions of the 'application.properties' file:

- spring.datasource.url = jdbc:postgresql://localhost: < port > /restaurantdb
- spring.datasource.customername = < username >
- spring.datasource.password = < password > 

> You also need to make changes in the localhost.properties file:
- server.port = < port >
- server.host = localhost
- database.name = restaurantdb
- database.customer = < username >
- database.password = < password >

In both of the above-mentioned files, you need to make sure that port, username and password is set according to your own system.

You can use Swagger UI to check the endpoints that have been created.

This is the link to Swagger UI:
> localhost:8080/api/swagger-ui.html

Here is what the Swagger UI looks like:
> ###### URL : 
- https://images.upgrad.com/4448fbd9-d8f6-4041-9e2e-2469d813e722-Capture.PNG

#### Project Structure
The main module is divided into three sub-modules FoodOrderingApp-api, FoodOrderingApp-db and FoodOrderingApp-service.

> FoodOrderingApp-api
- configuration - This directory consists of all the required configuration files of the project.
- controller - This directory consists of all the controller classes.
- exception - This directory must consist of the exception handlers for all the exceptions.
- endpoints - This directory consists of the json file which is used to generate Request and Response models.
- test - This directory consists of tests for all the controller classes.

> FoodOrderingApp-db
- config - This directory consists of the database properties and environment properties for local development.
- sql - This directory consists of all the SQL queries to create database schema tables.

> FoodOrderingApp-service
- business - This directory consists of all the implementations of business logic.
- common - This directory contains the error code implementation, any enum data type and any unexpected exception handling.
- dao - This directory allows us to isolate the application/business layer from the persistence layer and consists of the implementation of all the data access object classes.
- entity - This directory consists of all the entity classes related to the project to map these class objects with the database.
- exception - This directory consists of all the exceptions related to the project.